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

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.ThreadExceptionHandler;

/**
 *<B>Title:</B> Refresh Side Effects
 *<BR>
 *<B>Keywords:</B> cache
 *<BR>
 *<B>Assertion ID:</B> A12.5.1-5A
 *<BR>
 *<B>Assertion Description: </B>
 The refresh method updates the values in the parameter
 instance[s] from the data in the data store. ((The intended use is for optimistic transactions where the state of
 the JDO Instance is not guaranteed to reflect the state in the data store. This method can be used to minimize
 the occurrence of commit failures due to mismatch between the state of cached instances and the state of
 data in the data store.)) This can be tested by using 2 PersistenceManagers, independently change an object,
 then refresh.
 */

/** */
public class RefreshSideEffects extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.1-5A (RefreshSideEffects) failed: ";
    
    /** */
    static final int DELAY = 100;

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(RefreshSideEffects.class);
    }

    /** */
    public void test () throws Exception {
        PersistenceManagerFactory pmf = getPMF();
        PersistenceManager pm1 = pmf.getPersistenceManager();
        PersistenceManager pm2 = pmf.getPersistenceManager();
        
        try {
            runTestRefreshSideEffects(pm1, pm2);
        }
        finally {
            cleanupPM(pm2);
            pm2 = null;
            cleanupPM(pm1);
            pm1 = null;
        }
    }

    /** */
    private void runTestRefreshSideEffects(PersistenceManager pm1, 
                                           PersistenceManager pm2) throws Exception {
        if (debug) logger.debug ("\nSTART RefreshSideEffects");

        ThreadExceptionHandler group = new ThreadExceptionHandler();
        RefreshThreadT1 thread1 = new RefreshThreadT1(pm1);
        Thread T1 = new Thread(group, thread1);
        RefreshThreadT2 thread2 = new RefreshThreadT2(pm2);
        Thread T2 = new Thread(group, thread2);
        thread1.setOther(thread2);
        thread2.setOther(thread1);

        T1.start();
        T2.start();
        
        T1.join();
        T2.join();
        
        Throwable t1Problem = group.getUncaughtException(T1);
        if (t1Problem != null) {
            if (debug) {
                logger.debug("RefreshAllNoParameterSideEffects ThreadT1 results in uncaught exception");
                t1Problem.printStackTrace();
            }
            fail(ASSERTION_FAILED,
                 "ThreadT1 results in exception " + t1Problem);
        }
        Throwable t2Problem = group.getUncaughtException(T2);
        if (t2Problem != null) {
            if (debug) {
                logger.debug("RefreshAllNoParameterSideEffects ThreadT2 results in uncaught exception");
                t2Problem.printStackTrace();
            }
            fail(ASSERTION_FAILED,
                 "ThreadT2 results in exception " + t2Problem);
        }

        if (debug) logger.debug ("END RefreshSideEffects");
    }

    /** */
    class RefreshThreadT1 implements Runnable {

        PersistenceManager pm;
        RefreshThreadT2 other;
        boolean commitDone;

        /** */
        RefreshThreadT1(PersistenceManager pm) {
            this.pm = pm;
            this.other = null;
            this.commitDone = false;
        }

        /** */
        void setOther(RefreshThreadT2 other) {
            this.other = other;
        }

        /** */
        boolean isCommitDone() {
            return commitDone;
        }
        
        /** */
        synchronized public void run() {
            PCPoint n1 = new PCPoint (5,1);
            Transaction tx = pm.currentTransaction();
            try {
                RefreshSideEffects.this.logger.debug("  ThreadT1: START");
                tx.begin();
                n1.setX(500);
                pm.makePersistent(n1);
                pm.refresh(n1);

                RefreshSideEffects.this.logger.debug(
                    "  ThreadT1: waiting for ThreadT2.done");
                while (!other.isDone()) {
                    try {
                        Thread.sleep(DELAY);
                    }
                    catch (InterruptedException ex) {
                        // ignore
                    }
                }

                tx.commit();
                tx = null;
                commitDone = true;
                RefreshSideEffects.this.logger.debug(
                    "  ThreadT1: commit finished.");
            }
            finally {
                if ((tx != null) && tx.isActive())
                    tx.rollback();
            }
        }
    }

    /** */
    class RefreshThreadT2 implements Runnable {

        PersistenceManager pm;
        RefreshThreadT1 other;
        boolean done;
    
        /** */
        RefreshThreadT2(PersistenceManager pm) {
            this.pm = pm;
            this.other = null;
            this.done = false;
        }

        /** */
        boolean isDone() {
            return done;
        }
        

        /** */
        void setOther(RefreshThreadT1 other) {
            this.other = other;
        }

        /* test refresh() */
        synchronized public void run() {
            PCPoint p1 = new PCPoint (5,1);
            Transaction tx = pm.currentTransaction();
            try {
                RefreshSideEffects.this.logger.debug("  ThreadT2: START");
                tx.begin();
                p1.setX(201);
                pm.makePersistent(p1);
                pm.refresh(p1);
                done = true;

                RefreshSideEffects.this.logger.debug(
                    "  ThreadT2: waiting for commit of ThreadT1");
                while (!other.isCommitDone()) {
                    try {  
                        Thread.sleep(DELAY);
                    }
                    catch (InterruptedException ex) {
                        // ignore
                    }
                }
                tx.commit();
                tx = null;
                RefreshSideEffects.this.logger.debug(
                    "  ThreadT2: commit finished.");
            } 
            finally {
                if ((tx != null) && tx.isActive())
                    tx.rollback();
            }
        }
    }
}
