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

package org.apache.jdo.tck.api.persistencemanager.lifecycle;

import java.util.Collection;

import javax.jdo.JDOUserException;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
/**
 *<B>Title:</B> Make Transactional A Persistent Nontransactional Instance
 *<BR>
 *<B>Keywords:</B> persistentnontransactional
 *<BR>
 *<B>Assertion IDs:</B> A12.5.7-24
 *<BR>
 *<B>Assertion Description: </B>
PersistenceManager.makeTransactional and makeTransactionalAll can be used to mark a nontransactional persistent instance as being part of the read-consistency set of the transaction. In this case, the call must be made in the context of an active transaction, or a JDOUserException is thrown. To test this, get an instance in the persistence-nontransactional state, make it transactional, then in a
different transaction commit a change to the instance. An exception should then be thrown when the first transaction commits.

 */

public class MakeTransactionalANontransactionalPersistentInstance extends PersistenceManagerTest {
       
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.7-24 (MakeTransactionalANontransactionalPersistentInstance) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MakeTransactionalANontransactionalPersistentInstance.class);
    }
    
    private PCPoint p1 = null;
    private PCPoint p2 = null;
    private PCPoint p3 = null;
    private PCPoint p4 = null;
    private PCPoint p5 = null;
    private PCPoint p6 = null;
    private PCPoint p7 = null;

    private Collection col1 = new java.util.HashSet();
    private Collection col2 = new java.util.HashSet();

    /** */
    public void testTransactionalInstance() {
        if (isOptimisticSupported()) {
            pm = getPM();

            createPNonTranObjects();
            
            /* Positive cases */
            runTestMakeTransactional();
            runTestMakeTransactionalAll1();
            runTestMakeTransactionalAll2();
            
            /* Negative cases */
            runTestMakeTransactionalNeg();
            runTestMakeTransactionalAll1Neg();
            runTestMakeTransactionalAll2Neg();
            
            pm.close();
            pm = null;
        }
    }

    /** */
    private void createPNonTranObjects() {
        Transaction tx = pm.currentTransaction();
        try {
            tx.setOptimistic(false);
            tx.begin();
            p1 = new PCPoint(1,3);
            p2 = new PCPoint(2,4);
            p3 = new PCPoint(3,5);
            p4 = new PCPoint(4,6);
            p5 = new PCPoint (5,7);
            p6 = new PCPoint (6,8);
            p7 = new PCPoint (7,9);

            col1.add(p2);
            col1.add(p3);
            col1.add(p4);

            col2.add(p5);
            col2.add(p6);
            col2.add(p7);

            pm.makePersistent(p1);
            pm.makePersistent(p2);
            pm.makePersistent(p3);
            pm.makePersistent(p4);
            pm.makePersistent(p5);
            pm.makePersistent(p6);
            pm.makePersistent(p7);
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    public void runTestMakeTransactional() {
        Transaction tx = pm.currentTransaction();
        try {
            if (debug) logger.debug(" ** in runTestMakeTransactional()");
            int curr;

            tx.setOptimistic(true);
            tx.begin();

            if (makePersistentNonTranInstance(p1)) {
                pm.makeTransactional(p1);
            }
            tx.commit();
            tx = null;
            if (debug) logger.debug(" \nPASSED in runTestMakeTransactional()");
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    public void runTestMakeTransactionalNeg() {
        Transaction tx = pm.currentTransaction();
        try {
            if (debug) logger.debug(" ** in runTestMakeTransactionalNeg()");
            int curr;

            tx.setOptimistic(true);
            tx.begin();

            if (makePersistentNonTranInstance(p1)) {
                ; // expected result
            }
            tx.commit();
            tx = null;

            try {
                pm.makeTransactional(p1);
                fail(ASSERTION_FAILED,
                     "pm.makeTransactional should throw JDOUserException outside of tx");
            }
            catch (JDOUserException ex) {
                // expected exception
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    public void runTestMakeTransactionalAll1() {
        Transaction tx = pm.currentTransaction();
        try {
            if (debug) logger.debug(" ** in runTestMakeTransactionalAll1()");
            int curr;
            tx.setOptimistic(true);
            tx.begin();

            if ( makePersistentNonTranInstance(p2) &&
                 makePersistentNonTranInstance(p3) &&
                 makePersistentNonTranInstance(p4)) {

                pm.makeTransactionalAll(col1);
            }

            tx.commit();
            tx = null;
            if (debug)
                logger.debug(" \nPASSED in runTestMakeTransactionalAll1()");
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    public void runTestMakeTransactionalAll1Neg() {
        Transaction tx = pm.currentTransaction();
        try {
            if (debug) logger.debug(" ** in runTestMakeTransactionalAll1Neg()");
            int curr;
            tx.setOptimistic(true);
            tx.begin();

            if ( makePersistentNonTranInstance(p2) &&
                 makePersistentNonTranInstance(p3) &&
                 makePersistentNonTranInstance(p4)) {
                ;  // expected result
            }
            tx.commit();

            try {
                pm.makeTransactionalAll(col1);
                fail(ASSERTION_FAILED,
                     "pm.makeTransactionalAll(Collection) should throw JDOUserException outside of tx");
            }
            catch (JDOUserException ex) {
                // expected exception
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    public void runTestMakeTransactionalAll2() {
        Transaction tx = pm.currentTransaction();
        try {
            if (debug) logger.debug(" ** in runTestMakeTransactionalAll2()");
            int curr;
            tx.setOptimistic(true);
            tx.begin();

            Object[] objArray = col2.toArray();

            if ( makePersistentNonTranInstance(p5) &&
                 makePersistentNonTranInstance(p6) &&
                 makePersistentNonTranInstance(p7)) {

                pm.makeTransactionalAll(objArray);

            }
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    public void runTestMakeTransactionalAll2Neg() {
        Transaction tx = pm.currentTransaction();
        try {
            if (debug) logger.debug(" ** in runTestMakeTransactionalAll2Neg()");
            int curr;
            tx = pm.currentTransaction();
            tx.setOptimistic(true);
            tx.begin();

            Object[] objArray = col2.toArray();
            if ( makePersistentNonTranInstance(p5) &&
                 makePersistentNonTranInstance(p6) &&
                 makePersistentNonTranInstance(p7)) {
                ;  // expected result
            }
            tx.commit();

            try {
                pm.makeTransactionalAll(objArray);
                fail(ASSERTION_FAILED,
                     "pm.makeTransactionalAll(Object[]) should throw JDOUserException outside of tx");
            }
            catch (JDOUserException ex) {
                // expected exception
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     *
     * @param obj the PCPoint instance
     * @return true
     */
    public boolean makePersistentNonTranInstance (PCPoint obj) {
        int val = obj.getX();
        int curr = currentState(obj);
        if ( curr != PERSISTENT_NONTRANSACTIONAL &&
             curr != HOLLOW) {
            fail(ASSERTION_FAILED,
                 "expected P-HOLLOW or P-NONTX instance, instance " + obj + 
                 " is " + getStateOfInstance(obj) + ".");
            return false;
        }
        return true;
    }
}
