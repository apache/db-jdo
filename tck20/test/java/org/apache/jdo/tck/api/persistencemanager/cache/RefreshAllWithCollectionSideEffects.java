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

package org.apache.jdo.tck.api.persistencemanager.cache;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.ThreadExceptionHandler;

/**
 *<B>Title:</B> Refresh All With Collection Side Effects
 *<BR>
 *<B>Keywords:</B> cache
 *<BR>
 *<B>Assertion ID:</B> A12.5.1-5C
 *<BR>
 *<B>Assertion Description: </B>
The refreshAll(Collection pcs) updates the values in the parameter
instance[s] from the data in the data store. ((The intended use is for optimistic transactions where the state of
the JDO Instance is not guaranteed to reflect the state in the data store. This method can be used to minimize
the occurrence of commit failures due to mismatch between the state of cached instances and the state of
data in the data store.)) This can be tested by using 2 PersistenceManagers, independently change an object,
then refresh.
 */


/** */
public class RefreshAllWithCollectionSideEffects extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.1-5C (RefreshAllWithCollectionSideEffects) failed: ";
    
    /** */
    static final int DELAY = 100;

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(RefreshAllWithCollectionSideEffects.class);
    }
    
    /** */
    public void test () throws Exception {
        PersistenceManagerFactory pmf = getPMF();
        PersistenceManager pm1 = pmf.getPersistenceManager();
        PersistenceManager pm2 = pmf.getPersistenceManager();
        
        try {
            runTestRefreshAllWithCollectionSideEffects(pm1, pm2);
        }
        finally {
            cleanupPM(pm2);
            pm2 = null;
            cleanupPM(pm1);
            pm1 = null;
        }
    }

    /** */
    public void runTestRefreshAllWithCollectionSideEffects(PersistenceManager pm1, 
                                                           PersistenceManager pm2) throws Exception {
        if (debug) logger.debug("\nSTART RefreshAllWithCollectionSideEffects");

        ThreadExceptionHandler group = new ThreadExceptionHandler();
        RefreshColThreadT1 thread1 = new RefreshColThreadT1(pm1);
        Thread  T1 = new Thread(group, thread1);
        RefreshColThreadT2 thread2 = new RefreshColThreadT2(pm2);
        Thread  T2 = new Thread(group, thread2);
        thread1.setOther(thread2);
        thread2.setOther(thread1);
        
        T1.start();
        T2.start();
        
        T1.join();
        T2.join();
        
        Throwable t1Problem = group.getUncaughtException(T1);
        if (t1Problem != null) {
            if (debug) {
                logger.debug("RefreshAllWithCollectionSideEffects ThreadT1 results in uncaught exception");
                t1Problem.printStackTrace();
            }
            fail(ASSERTION_FAILED,
                 "ThreadT1 results in exception " + t1Problem);
        }
        Throwable t2Problem = group.getUncaughtException(T2);
        if (t2Problem != null) {
            if (debug) {
                logger.debug("RefreshAllWithCollectionSideEffects ThreadT2 results in uncaught exception");
                t2Problem.printStackTrace();
            }
            fail(ASSERTION_FAILED,
                 "ThreadT2 results in exception " + t2Problem);
        }

        if (debug) logger.debug("END RefreshAllWithCollectionSideEffects");
    }

    /** */
    class RefreshColThreadT1 implements Runnable{

        PersistenceManager pm;
        RefreshColThreadT2 other;
        boolean commitDone;
    
        /** */
        RefreshColThreadT1(PersistenceManager pm) {
            this.pm = pm;
            this.other = null;
            this.commitDone = false;
        }
        
        /** */
        void setOther(RefreshColThreadT2 other) {
            this.other = other;
        }

        /** */
        boolean isCommitDone() {
            return commitDone;
        }

        /** */
        synchronized public void run() {
            PCPoint n1 = new PCPoint (5,1);
            PCPoint n2 = new PCPoint (5,2);
            Transaction tx = pm.currentTransaction();
            try {
                tx.begin();
                n1.setX(500);
                n2.setX(501);

                Collection col1 = new HashSet();
                col1.add(n1);
                col1.add(n2);

                pm.makePersistentAll(col1);
                pm.refreshAll(col1);
                RefreshAllWithCollectionSideEffects.this.logger.debug(
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
                RefreshAllWithCollectionSideEffects.this.logger.debug(
                    "  ThreadT1: commit finished.");
            } 
            finally {
                if ((tx != null) && tx.isActive())
                    tx.rollback();
            }
        }
    }

    /** */
    class RefreshColThreadT2 implements Runnable{

        PersistenceManager pm = null;
        RefreshColThreadT1 other = null;
        boolean done = false;
    
        /** */
        RefreshColThreadT2(PersistenceManager pm) {
            this.pm = pm;
            this.other = null;
            this.done = false;
        }

        /** */
        boolean isDone() {
            return done;
        }
        

        /** */
        void setOther(RefreshColThreadT1 other) {
            this.other = other;
        }

        /** */
        synchronized public void run() {
            PCPoint p1 = new PCPoint (5,1);
            PCPoint p2 = new PCPoint (5,2);
            Transaction tx = pm.currentTransaction();
            try {
                tx.begin();
                p1.setX(200);
                p2.setX(201);

                Collection col1 = new HashSet();
                col1.add(p1);
                col1.add(p2);
                pm.makePersistentAll(col1);
                pm.refreshAll(col1);
                done = true;

                RefreshAllWithCollectionSideEffects.this.logger.debug(
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
                RefreshAllWithCollectionSideEffects.this.logger.debug(
                    "  ThreadT2: commit finished.");
            } 
            finally {
                if ((tx != null) && tx.isActive())
                    tx.rollback();
            }
        }
    }
}
