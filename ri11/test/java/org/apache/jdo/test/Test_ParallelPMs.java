/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.apache.jdo.test;

import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PointFactory;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.Factory;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that multiple PersistenceManager's, each running in their own thread,
* can successfully insert objects into the store.
*
* @author Dave Bristor
*/
public class Test_ParallelPMs extends AbstractTest {

    /** One thread per PM. */
    private int threads = 0;

    /** Factory for creating PersistenceCapable instances that are stored. */
    private LocalFactory localFactory;

    /** If true, then each PM tries to insert the same "shared" PC instance.
     * Otherwise, each PM tries to insert its own, independent PC instance.
     */  
    private boolean share;

    /** */
    private int attempts;

    /** */
    private Throwable throwable = null;

    /** The number of objects that is expected to have been inserted. */
    protected int insertedCountExpected = 0;

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_ParallelPMs.class);
    }

    /**  */
    public void testShared() throws Throwable {
        share = true;
        localFactory = new LocalFactory(share);
        insertedCountExpected++;
        insertObjects();
        if (throwable != null)
            throw throwable;
    }

    /**  */
    public void testNotShared() throws Throwable {
        share = false;
        localFactory = new LocalFactory(share);
        insertedCountExpected += numInsert;
        insertObjects();
        if (throwable != null)
            throw throwable;
    }
                
    // Creates PC instances, which can be either shared among
    // PersistenceManagers or not, depending on how the factory is created.
    //
    // This is so that, by changing the system property "share", we
    // can test two conditions:
    //
    // (a) each PM-in-a-thread stores it's own PC: each should succeed.
    // (b) each PM-in-a-thread stores the *same* PC: only one should succeed.
    //
    class LocalFactory {
        private final Object pc;

        LocalFactory(boolean share) {
            if (debug)
                logger.debug(Thread.currentThread() +
                             " LocalFactory: " + share);
            if (share) {
                pc = factory.create(-999);
            } else {
                pc = null;
            }
        }

        // If the factory was created with arg=true, then always return the
        // same instance.
        Object create(int i) {
            return null == pc ? factory.create(i) : pc;
        }
    }

    /**
     * ThreadGroup to pass an exception thrown by a test thread to the
     * Test_ParallelPMs instance, such that the test method can throw the
     * exception and the test framework can handle it.
     */
    class MyThreadGroup extends ThreadGroup 
    {   
        MyThreadGroup () {
            super("Test_ParallelPMs");
        }
        
        public void uncaughtException(Thread t, Throwable e) {
            throwable = e;
        }
    }

    protected void insertObjects() {
        Inserter inserters[] = new Inserter[numInsert];

        MyThreadGroup threadGroup = new MyThreadGroup();
        for (int i = 0; i < numInsert; i++) {
            Object pc = localFactory.create(i);
            threads++;
            Thread t = new Thread(threadGroup, new Inserter(pmf, pc));
            t.setName("Inserter-" + i);
            t.start();
        }

        Thread.currentThread().yield();

        while (! attemptsComplete()) { 
            try  {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException ex) {
                System.err.println("interrupted while waiting for threads to insert");
            }
        }

        while (threads > 0) {
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException ex) {
                System.err.println("interrupted while waiting for threads to finish");
            }
        }
    }

    synchronized void signal() {
        threads--;
    }

    protected synchronized void incrAttempts() {
        attempts++;
    }

    protected synchronized boolean attemptsComplete() {
        return attempts == numInsert;
    }

    protected int getDefaultInsert()
    {
        return 5;
    }
    

    class Inserter implements Runnable {
        private final PersistenceManager pm;
        private final Object pc;
        private final Class instanceClass;

        Inserter(PersistenceManagerFactory pmf, Object pc) {
            this.pm = pmf.getPersistenceManager();
            this.pc = pc;
            this.instanceClass = pc.getClass();
        }
        
        public void run() {
            Transaction tx = null;
            try {
                if (debug)
                    logger.debug(Thread.currentThread() + " running");
                tx = pm.currentTransaction();
                tx.begin();
                try {
                    pm.makePersistent(pc);
                    announce("inserted ", pc);
                    tx.commit();
                }
                catch (JDOException ex) {
                    if (!share)
                        throw ex;
                }
                finally {
                    if (tx != null && tx.isActive())
                        tx.rollback();
                }
                incrAttempts();

                while (!attemptsComplete()) {
                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException ex) {
                        if (debug) 
                            logger.debug(Thread.currentThread() +
                                         " interrupted");
                    }
                }

                // Verify that the number of inserted objects matches
                // the expected number 
                if (insertedCount!=insertedCountExpected) {
                    assertEquals("Wrong number of objects in extent", 
                                 insertedCount, insertedCountExpected);
                }

                boolean messagePrinted = false;
                int objCount = 0;
                if (debug) 
                    logger.debug(Thread.currentThread() + " getting Extent of " +
                                 instanceClass.getName());
                Extent e = pm.getExtent(instanceClass, false);
                for (Iterator i = e.iterator(); i.hasNext();) {
                    Object instance = (Object)i.next();
                    if (debug) {
                        if (objCount < maxMessages) {
                            logger.debug(Thread.currentThread() +
                                         " " + instance);
                        } else if (! messagePrinted) {
                            logger.debug(Thread.currentThread() +
                                         " skipping remaining extent messages");
                            messagePrinted = true;
                        }
                    }
                    objCount++;
                }
                assertEquals("Wrong number of objects in extent", 
                             insertedCount, objCount);
            } finally {
                if (tx != null && tx.isActive())
                    tx.rollback();
                if (pm != null && !pm.isClosed())
                    pm.close();
                Test_ParallelPMs.this.signal();
            }
        }

        public String toString() {
            String rc = "Inserter ";
            if (debug) {
                rc += Thread.currentThread();
            }
            return rc;
        }
    }

    /** */
    protected Factory getFactory(int verify) {
        PointFactory rc = new PointFactory();
        rc.setVerify(verify);
        return rc;
    }
}
