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
import java.util.Iterator;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> MakeTransactional
 *<BR>
 *<B>Keywords:</B> transienttransactional lifecycle
 *<BR>
 *<B>Assertion IDs:</B> A12.5.7-20
 *<BR>
 *<B>Assertion Description: </B>
PersistenceManager.makeTransactional and makeTransactionalAll makes a transient instance transactional and causes a state transition to transient-clean. After the method completes, the instance observes transaction boundaries.

 */


public class MakeTransactional extends PersistenceManagerTest{
       
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.7-20 (MakeTransactional) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MakeTransactional.class);
    }
 
    private PCPoint p = null;
    private PCPoint p1 = null;
    private PCPoint p2 = null;
    private PCPoint p3 = null;
    private PCPoint p4 = null;

    /** */  
    public void testTransactionalInst() {
    	pm = getPM();
    	
        createObjects();
        testMakeTransactional(pm);
        testMakeTransactionalAll1(pm);
        testMakeTransactionalAll2(pm);
        
        pm.close();
        pm = null;
    }

    /** */
    private void createObjects() {
    	p1 = new PCPoint(1,3);
        p2 = new PCPoint(2,4);
        p3 = new PCPoint(3,5);
        p4 = new PCPoint(4,6);
    }

    /** */
    public void testMakeTransactional(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            
            pm.makeTransactional(p1);
            if (currentState(p1) != TRANSIENT_CLEAN) {
                fail(ASSERTION_FAILED,
                     "Expected T-CLEAN instance, instance is " + 
                     getStateOfInstance(p1) + ".");
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
    public void testMakeTransactionalAll1(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            
            Collection col1 = new java.util.HashSet();
            col1.add(p1);
            col1.add(p2);
            col1.add(p3);
            col1.add(p4);
            
            pm.makeTransactionalAll(col1);
            Iterator iter = col1.iterator();
            while (iter.hasNext() ) {
                PCPoint p = (PCPoint) iter.next();
                if (currentState(p) != TRANSIENT_CLEAN) {
                    fail(ASSERTION_FAILED,
                         "Expected T-CLEAN instance, instance " + p + " is " + 
                         getStateOfInstance(p1) + ".");
                }
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
    private void testMakeTransactionalAll2(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            
            Object[] objArray = {p1,p2,p3,p4};
            pm.makeTransactionalAll(objArray);
            
            for ( int i=0; i < objArray.length; i++) {
                p = (PCPoint) objArray[i];
                if (currentState(p) != TRANSIENT_CLEAN) {
                    fail(ASSERTION_FAILED,
                         "Expected T-CLEAN instance, instance " + p + " is " + 
                         getStateOfInstance(p1) + ".");
                }
            }
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
