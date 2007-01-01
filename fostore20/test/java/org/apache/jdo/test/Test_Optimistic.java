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

import javax.jdo.JDOException;
import javax.jdo.JDOHelper;
import javax.jdo.JDOOptimisticVerificationException;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that we correctly verify instance values during an optimistic
* transaction.  In this test we create instances, and then get their object
* id's.  We then use those object id's to create objects in a second PM.  We
* then modify the instances in the first PM, and commit them.  Then modify the
* values in some of the instances in an optimistic transaction in the second
* PM, and commit: this should fail, and we should get an exception which
* contains two other exceptions, one for each of the instances which failed to
* verify.
*
* @author Dave Bristor
*/
public class Test_Optimistic extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Optimistic.class);
    }

    /**
     * Inserts some number of objects in the database
     */
    public void test() {
        insertObjects();
    }

    // Gets an extent of points, and changes them.
    protected void insertObjects() {
        PersistenceManager pm1 = null;
        PersistenceManager pm2 = null; 
        PersistenceManager pm3 = null;
        Transaction tx1 = null;
        Transaction tx2 = null;
        Transaction tx3 = null;
        try {
            pm1 = pmf.getPersistenceManager();
            tx1 = pm1.currentTransaction();
            if (debug) logger.debug("\nINSERT");

            tx1.setOptimistic(false);
            tx1.begin();
            
            PCPoint p1a = new PCPoint(3, 5);
            pm1.makePersistent(p1a);
            PCPoint p1b = new PCPoint(7, 9);
            pm1.makePersistent(p1b);
            PCPoint p1c = new PCPoint(4, 8);
            pm1.makePersistent(p1c);
            
            Object oid_1a = JDOHelper.getObjectId(p1a);
            if (debug) logger.debug("p1a " + p1a + ", oid=" + oid_1a);
            Object oid_1b = JDOHelper.getObjectId(p1b);
            if (debug) logger.debug("p1b " + p1b + ", oid=" + oid_1b);
            Object oid_1c = JDOHelper.getObjectId(p1c);
            if (debug) logger.debug("p1c " + p1c + ", oid=" + oid_1c);

            tx1.commit();
            
            // Start tx2
            pm2 = pmf.getPersistenceManager();
            tx2 = pm2.currentTransaction();
            tx2.setOptimistic(true);
            tx2.begin();
            
            PCPoint p2a = (PCPoint)pm2.getObjectById(oid_1a, true);
            PCPoint p2b = (PCPoint)pm2.getObjectById(oid_1b, true);
            PCPoint p2c = (PCPoint)pm2.getObjectById(oid_1c, true);

            if (debug)
                printValues("p1X made persistent, p2X from getObjectById(getObjectId) technique",
                            p1a, p1b, p1c, p2a, p2b, p2c);
            
            tx1 = pm1.currentTransaction();
            tx1.setOptimistic(false);
            tx1.begin();
            p1a.setX(555);
            p1b.setX(123);
            // p1c.setX(blah blah); // intentionally, we do *not* change p1c
            tx1.commit();
            
            if (debug) 
                printValues("after change in Tx1 to p1X",
                            p1a, p1b, p1c, p2a, p2b, p2c);
            
            p2a.setY(new Integer(7890)); // This should fail to verify
            p2b.setY(new Integer(1212)); // Ditto p2a
            p2c.setY(new Integer(1357)); // This should succeed (but roll back)
            
            if (debug) 
                printValues("after changes in Tx2 to p1a & p1b",
                            p1a, p1b, p1c, p2a, p2b, p2c);
            
            try {
                tx2.commit();
            } catch (JDOException ex) {
                if (ex instanceof JDOOptimisticVerificationException) {
                    JDOOptimisticVerificationException jex =
                        (JDOOptimisticVerificationException)ex;
                    Throwable nested[] = jex.getNestedExceptions();
                    if (nested.length == 2) {                
                        // XXX This might be improved: it *could* be a datastore
                        // exception, but not one which is for the expected verify
                        // failure.
                        if (debug) 
                            logger.debug("In optimistic transaction, caught expected exception");
                    } else {
                        fail("In optimistic transaction caught unexpected exception:" + ex);
                    }
                } else {
                    fail("In optimistic transaction caught unexpected exception:" + ex);
                }
                try {
                    if (tx2.isActive()) {
                        // MBO: is this legal?
                        if (debug) {
                            logger.debug("Transaction tx2 active; rolling back");
                            printValues("before rollback",
                                        p1a, p1b, p1c, p2a, p2b, p2c);
                        }
                        tx2.rollback();
                    } else {
                        if (debug) logger.debug("Transaction tx2 rolled back");
                    }
                } catch (JDOUserException ex2) {
                    // MBO: might not be necessary, see above
                    fail("Can't rollback" + ex2);
                }
            } finally {
                if (debug) printValues("finally", p1a, p1b, p1c, p2a, p2b, p2c);
                assertPCPointEquals("In finally: unexpected value of p1a", 555, 5, p1a);
                assertPCPointEquals("In finally: unexpected value of p1b", 123, 9, p1b);
                assertPCPointEquals("In finally: unexpected value of p1c", 4, 8, p1c);
                assertPCPointEquals("In finally: unexpected value of p2a", 3, 5, p2a);
                assertPCPointEquals("In finally: unexpected value of p2b", 7, 9, p2b);
                assertPCPointEquals("In finally: unexpected value of p2c", 4, 8, p2c);
            }
            
            pm3 = pmf.getPersistenceManager();
            tx3 = pm3.currentTransaction();
            tx3.setOptimistic(false);
            tx3.begin();
            PCPoint p3a = (PCPoint)pm3.getObjectById(oid_1a, false);
            PCPoint p3b = (PCPoint)pm3.getObjectById(oid_1b, false);
            PCPoint p3c = (PCPoint)pm3.getObjectById(oid_1c, false);
            if (debug)
                printValues("values labelled p2X are really p3X in this list",
                            p1a, p1b, p1c, p3a, p3b, p3c);
            assertPCPointEquals("tx3: unexpected value of p1a", 555, 5, p1a);
            assertPCPointEquals("tx3: unexpected value of p1b", 123, 9, p1b);
            assertPCPointEquals("tx3: unexpected value of p1c", 4, 8, p1c);
            assertPCPointEquals("tx3: unexpected value of p3a", 555, 5, p3a);
            assertPCPointEquals("tx3: unexpected value of p3b", 123, 9, p3b);
            assertPCPointEquals("tx3: unexpected value of p3c", 4, 8, p3c);
            tx3.commit();
        }
        finally {
            if (tx3 != null && tx3.isActive())
                tx3.rollback();
            if (pm3 != null && !pm3.isClosed())
                pm3.close();
            if (tx2 != null && tx2.isActive())
                tx2.rollback();
            if (pm2 != null && !pm2.isClosed())
                pm2.close();
            if (tx1 != null && tx1.isActive())
                tx1.rollback();
            if (pm1 != null && !pm1.isClosed())
                pm1.close();
        }
    }
    
    private void printValues(
        String str, PCPoint p1a, PCPoint p1b, PCPoint p1c,
        PCPoint p2a, PCPoint p2b, PCPoint p2c) {

        logger.debug(str);
        logger.debug("p1a: " + p1a);
        logger.debug("p1b: " + p1b);
        logger.debug("p1c: " + p1c);
        logger.debug("p2a: " + p2a);
        logger.debug("p2b: " + p2b);
        logger.debug("p2c: " + p2c);
    }

    private void assertPCPointEquals(String msg, int x, int y, PCPoint actual)
    {
        assertEquals(msg + " field x", x, actual.getX());
        assertEquals(msg + " field y", new Integer(y), actual.getY());
    }
    
}
