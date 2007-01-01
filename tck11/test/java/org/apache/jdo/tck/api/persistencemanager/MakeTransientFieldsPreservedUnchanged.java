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

package org.apache.jdo.tck.api.persistencemanager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> MakeTransient Fields Preserved Unchanged
 *<BR>
 *<B>Keywords:</B> transient
 *<BR>
 *<B>Assertion IDs:</B> A12.5.7-15, A12.5.7-18
 *<BR>
 *<B>Assertion Description: </B>
If the instance passed to PersistenceManager.makeTransient or makeTransientAll has field values (persistent-nontransactional or persistent-clean), the fields in the cache are preserved unchanged. The instance(s) are not modified in any way.
 */

public class MakeTransientFieldsPreservedUnchanged extends PersistenceManagerTest {
       
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.7-15, A12.5.7-18 (MakeTransientFieldsPreservedUnchanged) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MakeTransientFieldsPreservedUnchanged.class);
    }

    private PCPoint p1 = null;
    private PCPoint p2 = null;
    private PCPoint p3 = null;
    private PCPoint p4 = null;
    private PCPoint p5 = null;

    private PCPoint np1 = null;
    private PCPoint np2 = null;
    private PCPoint np3 = null;
    private PCPoint np4 = null;
    private PCPoint np5 = null;

    /** */
    public void testMakeTransientFieldsPreservedUnchanged() {
        pm = getPM();

        createObjects(pm);
        runTestMakeTransientFieldsPreservedUnchanged1(pm);
        runTestMakeTransientFieldsPreservedUnchangedAll1(pm);
        runTestMakeTransientFieldsPreservedUnchangedAll2(pm);
        
        pm.close();
        pm = null;
    }

    /** */
    private void createObjects(PersistenceManager pm) {
        createPCleanObjects(pm);
        createPNonTransactionalObjects(pm);
    }
    
    /** */
    private void  createPCleanObjects(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            // create P-clean instances
            tx.setOptimistic(false);
            tx.begin();
            
            p1 = new PCPoint(1,3);
            p2 = new PCPoint(2,4);
            p3 = new PCPoint(3,5);
            p4 = new PCPoint(4,6);
            p5 = new PCPoint(5,7);

            pm.makePersistent(p1);
            pm.makePersistent(p2);
            pm.makePersistent(p3);
            pm.makePersistent(p4);
            pm.makePersistent(p5);
            tx.commit();

            tx.begin();
            p1.getX();
            p2.getX();
            p3.getX();
            p4.getX();
            p5.getX();
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void createPNonTransactionalObjects(PersistenceManager pm) {
        if (isOptimisticSupported()) {
            Transaction tx = pm.currentTransaction();
            try {
                tx.setOptimistic(true);
                tx.begin();
                
                np1 = new PCPoint(1,3);
                np2 = new PCPoint(2,4);
                np3 = new PCPoint(3,5);
                np4 = new PCPoint(4,6);
                np5 = new PCPoint(5,7);
                
                pm.makePersistent(np1);
                pm.makePersistent(np2);
                pm.makePersistent(np3);
                pm.makePersistent(np4);
                pm.makePersistent(np5);
                tx.commit();
                
                tx.begin();
                np1.getX();
                np2.getX();
                np3.getX();
                np4.getX();
                np5.getX();
                tx.commit();
                tx = null;
            }
            finally {
                if ((tx != null) && tx.isActive())
                    tx.rollback();
            }
        }
    }
    
    /* test makeTransient (Object pc)
     * instance passed has field values and is P-nontransactional *
     */
    private void runTestMakeTransientFieldsPreservedUnchanged1(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.makeTransient(p1);
            tx.commit();
            tx = null;

            if (!testState(p1, TRANSIENT, "transient")) {
                fail(ASSERTION_FAILED,
                     "expected TRANSIENT instance, instance " + p1 + 
                     " is " + getStateOfInstance(p1));
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /* test makeTransientAll (Collection pcs) */
    private void runTestMakeTransientFieldsPreservedUnchangedAll1(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Collection col1 = new HashSet();
            col1.add(p2);
            col1.add(p3);

            pm.makeTransientAll(col1);
            tx.commit();
            tx = null;
            
            for (Iterator iter = col1.iterator(); iter.hasNext();) {
                PCPoint p = (PCPoint) iter.next();
                if (!testState(p, TRANSIENT, "transient")) {
                    fail(ASSERTION_FAILED,
                         "expected TRANSIENT instance, instance " + p + 
                         " is " + getStateOfInstance(p));
                }
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /* test makeTransientAll (Object[] o) */
    private void runTestMakeTransientFieldsPreservedUnchangedAll2(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            int NUM_OBJS = 2;
            tx.begin();

            Collection col1 = new HashSet();
            col1.add(p4);
            col1.add(p5);

            Object[] obj1= col1.toArray();

            pm.makeTransientAll(obj1);
            tx.commit();
            tx = null;
            
            for (int i=0; i < NUM_OBJS; ++i ) {
                PCPoint p = (PCPoint) obj1[i];
                if (!testState(p, TRANSIENT, "transient")) {
                    fail(ASSERTION_FAILED,
                         "expected TRANSIENT instance, instance " + p + 
                         " is " + getStateOfInstance(p));
                }
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}

