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
 
package org.apache.jdo.tck.lifecycle;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.jdo.Extent;
import javax.jdo.JDOException;
import javax.jdo.JDOFatalException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import junit.framework.AssertionFailedError;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.lifecycle.StateTransitionObj;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.ThreadExceptionHandler;

/**
 *<B>Title:</B> Test PM ability to share PC classes but not PC instances
 *<BR>
 *<B>Keywords:</B> PM share PC class
 *<BR>
 *<B>Assertion IDs:</B> A5.2-1,A5.2-2,A5.2-3
 *<BR>
 *<B>Assertion Description: </B>
 Several JDO PersistenceManagers might be coresident and might
 share the same persistence capable classes] (although a JDO instance can be
 associated with only one PersistenceManager at a time)
 */

public class PMsCanSharePCClassesButNotPCInstances extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertions A5.2-1,A5.2-2,A5.2-3 (PMsCanSharePCClassesButNotPCInstances) failed: ";

    /** The number of active threads. Each thread has its own PM. */
    private int threads = 0;

    /** The total number of attempts to insert PC instances */
    private int attempts = 0;

    /** The number of inserters. Each inserter runs in its own thread. */
    private int inserters = 5;

    /** The number of objects inserted. */
    protected int insertedCount = 0;

    /** The number of objects that is expected to have been inserted. */
    protected int insertedCountExpected = 0;

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(PMsCanSharePCClassesButNotPCInstances.class);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(StateTransitionObj.class);
    }
    
    public void testSharedPC() {
        // test shared PC - only one PM should succeed to insert the shared PC
        threads = 0;
        attempts = 0;
        insertedCount = 0;
        insertedCountExpected = 1;
        insertObjects(true);
    }

    public void testNonSharedPC() {
        // test non-shared PCs - each PM should succeed to insert its own non-shared PC
        threads = 0;
        attempts = 0;
        //insertedCount = 0; //Don't reset this value since we have not deleted the inserted object from the shared PC test above
        insertedCountExpected += inserters;
        insertObjects(false);
    }

    protected void insertObjects(boolean sharedPC) {
        Object pc = null;

        ThreadExceptionHandler threadGroup = new ThreadExceptionHandler();
        for (int i = 0; i < inserters; i++) {
            if (sharedPC) { // each thread shares one PC
                if (pc==null)
                    pc = new StateTransitionObj(i);
            }
            else { // each thread has its own PC
                pc = new StateTransitionObj(i);
            }
            threads++;
            Thread t = new Thread(threadGroup, new Inserter(pmf, pc, sharedPC));
            t.setName("Inserter-" + i);
            t.start();
        }
        
        Thread.yield();
        
        while (! attemptsComplete()) { 
            try  {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                logger.debug("interrupted while waiting for threads to insert");
            }
        }

        while (threads > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                logger.debug("interrupted while waiting for threads to finish");
            }
        }
        
        Collection exceptions = threadGroup.getAllUncaughtExceptions();
        for (Iterator i = exceptions.iterator(); i.hasNext(); ) {
            Map.Entry entry = (Map.Entry) i.next();
            Thread thread = (Thread)entry.getKey();
            Throwable throwable = (Throwable)entry.getValue();
            String message = "Uncaught exception " + throwable + " in thread " + thread;
            if( throwable instanceof AssertionFailedError )
                fail(ASSERTION_FAILED, message);
            else
                throw new JDOFatalException(message, throwable);
        }
        
    }

    synchronized void signal() {
        threads--;
    }

    protected synchronized void incrAttempts() {
        attempts++;
    }

    protected synchronized boolean attemptsComplete() {
        return attempts == inserters;
    }

    class Inserter implements Runnable {
        private final PersistenceManager pm;
        private final Object pc;
        private final Class instanceClass;
        private final boolean sharedPC;

        Inserter(PersistenceManagerFactory pmf, Object pc, boolean sharedPC) {
            this.pm = pmf.getPersistenceManager();
            this.pc = pc;
            this.instanceClass = pc.getClass();
            this.sharedPC = sharedPC;
        }

        protected synchronized void announce(String msg, Object pc) {
            insertedCount++;
            Object oid = JDOHelper.getObjectId(pc);
            if (debug)
                logger.debug(msg + this.getClass().getName() + ": " + oid +
                             ", " + pc);
        }

        public void run() {
            Transaction tx = null;
            try {
                if (debug) logger.debug("running");
                tx = pm.currentTransaction();
                tx.begin();
                try {
                    pm.makePersistent(pc);
                    announce("inserted ", pc);
                    tx.commit();
                }
                catch (JDOException ex) {
                    if (!sharedPC) //we expect an exception for all but one of the inserters in the case of a shared PC
                        throw ex;
                }
                finally {
                    incrAttempts();
                    if (tx != null && tx.isActive())
                        tx.rollback();
                }

                while (!attemptsComplete()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        if (debug) logger.debug(" interrupted");
                    }
                }

                //Verify that the number of inserted objects matches the expected number
                if (insertedCount!=insertedCountExpected)
                {
                    fail(ASSERTION_FAILED,
                         "Wrong number of objects in extent. Inserted=" + 
                         insertedCount + " Expected=" + insertedCountExpected);
                }

                if (debug) logger.debug("getting Extent");
                int objCount = 0;
                if (debug)
                    logger.debug("getting Extent of " + instanceClass.getName());
                tx.begin();
                Extent e = pm.getExtent(instanceClass, false);
                for (Iterator i = e.iterator(); i.hasNext();) {
                    Object instance = (Object)i.next();
                    objCount++;
                }
                tx.commit();

                //Verify that the number of inserted objects matches the number of objects in the extent
                if (insertedCount!=objCount)
                {
                    fail(ASSERTION_FAILED,
                         "Wrong number of objects in extent. Inserted="+insertedCount+" Found="+objCount);
                }
            } finally {
                if (tx != null && tx.isActive())
                    tx.rollback();
                if (pm != null && !pm.isClosed())
                    pm.close();
                PMsCanSharePCClassesButNotPCInstances.this.signal();
            }
        }

        public String toString() {
            String rc = "Inserter ";
            if (debug) {
                rc += Thread.currentThread().toString();
            }
            return rc;
        }
    }
}
