/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.apache.jdo.tck.api.persistencemanager;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.ThreadExceptionHandler;

/**
 *<B>Title:</B> Thread Safe
 *<BR>
 *<B>Keywords:</B> multithreaded
 *<BR>
 *<B>Assertion ID:</B> A12.4-1.
 *<BR>
 *<B>Assertion Description: </B>
It is a requirement for all JDO implementations to be thread-safe. That is, the behavior of the implementation must be predictable in the presence of multiple application threads.  This assertion will generate multiple test cases to be evaluated.

 */


public class ThreadSafe extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.4-1 (ThreadSafe) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ThreadSafe.class);
    }

    private PersistenceManagerFactory   pmf;
    PCPoint p1 = null;
    private int totalThreadCount = 10;
    private int completedThreadCount = 0;
    private int succeeds = 0;
    private int catchNumber = 0;

    /** */
    public void testThreadSafe() throws Exception  {
        if(debug) logger.debug("\nSTART testThreadSafe");
        pmf = getPMF();

        p1 = new PCPoint(3,3);

        ThreadExceptionHandler group = new ThreadExceptionHandler();
        Thread[] threads = new Thread[totalThreadCount];
        for (int i=0; i < totalThreadCount; i++) {
            Thread t = new Thread(group, new PMThread(pmf, p1));
            t.setName("ThreadSafeID-" + i);
            threads[i] = t;
            t.start();
        }

        for (int i = 0; i < totalThreadCount; i++) {
            try {   
                threads[i].join();
            } 
            catch (InterruptedException e) { 
                // ignored
            }
       }
        
        checkResults(group);
    }

    /** */
    protected synchronized void complete() {
        completedThreadCount++;
    }
    
    /** */
    protected synchronized void winning(PCPoint pc) {
        if (debug)
            logger.debug("[" + Thread.currentThread().getName()  + "]: succeeds");
        succeeds++;
    }

    /** */
    public void checkResults(ThreadExceptionHandler group) {
        // check unhandled exceptions
        Set uncaught = group.getAllUncaughtExceptions();
        if ((uncaught != null) && !uncaught.isEmpty()) {
            StringBuffer report = new StringBuffer("Uncaught exceptions:\n");
            for (Iterator i = uncaught.iterator(); i.hasNext();) {
                Map.Entry next = (Map.Entry)i.next();
                Thread thread = (Thread)next.getKey();
                Throwable problem = (Throwable)next.getValue();
                report.append(
                     "Uncaught exception " + problem + " in thread " + thread + "\n");
            }
            fail(ASSERTION_FAILED, report.toString());
        }

        if (succeeds != 1) {
            fail(ASSERTION_FAILED,
                 "Only one thread should succeed, number of succeeded threads is " + succeeds);
        }
        
        if (catchNumber != totalThreadCount - 1) {
            fail(ASSERTION_FAILED,
                 "All but one threads should throw exceptions, expected " + 
                 (totalThreadCount - 1) + ", got " + catchNumber);
        }
    }

/** */
    class PMThread implements Runnable {
        private final PersistenceManager pm;
        private final Object pc;

        /** */
        PMThread(PersistenceManagerFactory pmf, PCPoint pc) {
            this.pm = pmf.getPersistenceManager();
            this.pc = pc;
        }

        /** */
        public void run() {
            String threadName = Thread.currentThread().getName();
            Transaction tx = pm.currentTransaction();
            try {
                ThreadSafe.this.logger.debug("[" + threadName + "]: running");
                tx.begin();
                pm.makePersistent(pc);
                tx.commit();
                tx = null;
                winning((PCPoint) pc);
                complete();
            } 
            catch (JDOUserException ex) {
                ThreadSafe.this.logger.debug("[" + threadName +
                                             "]: throws expected " + ex);
                catchNumber++;
                complete();
            } 
            finally {
                if ((tx != null) && tx.isActive()) {
                    tx.rollback();
                }
                pm.close();
            }
        }		// run
    }  // class PMThread
}

