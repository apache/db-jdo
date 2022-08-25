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
import java.util.HashSet;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.Point;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;

/**
 *<B>Title:</B> MakeNontransactionalAllFails
 *<BR>
 *<B>Keywords:</B> exception
 *<BR>
 *<B>Assertion ID:</B> A12.5.7-5
 *<BR>
 *<B>Assertion Description: </B>
If a collection or array of instances is passed to PersistenceManager.makeNontransactionalAll, and one or more of the instances fail to complete the required operation, then all instances will be attempted, and a JDOUserException will be thrown which contains a nested exception array, each exception of which contains one of the failing instances. The succeeding instances will transition to the specified life cycle state, and the failing instances will remain in their current state.

 */

public class MakeNontransactionalAllFails extends PersistenceManagerTest {
       
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.7-5 (MakeNontransactionalAllFails) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MakeNontransactionalAllFails.class);
    }
    
    private PCPoint p1 = null;
    private PCPoint p2 = null;
    private PCPoint p3 = null;
    private PCPoint p4 = null;
    private Point p5 = null;

    /** */
    public void testTransactionalInst() {
        pm = getPM();

        createObjects(pm);
        runTestMakeNontransactional1(pm);
        runTestMakeNontransactionalAll1(pm);

        pm.close();
        pm = null;
    }
    
    /** */
    private void createObjects(PersistenceManager pm) {
        p1 = new PCPoint(1,3);
        p2 = new PCPoint(2,4);
        p3 = new PCPoint(3,5);
        p4 = new PCPoint(4,6);
    }
    
    /* test makeNontransactional (Object pc) */
    private void runTestMakeNontransactional1(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.makePersistent(p1);
            try {
                pm.makeNontransactional(p1);
                fail(ASSERTION_FAILED,
                     "pm.makeNontransactional should throw a JDOUserException when called for a P-NEW instance.");
            }
            catch (JDOUserException ex) {
                // expected exception
            }
            tx.rollback();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
    
    /* test makeNontransactionalAll (Collection pcs) */
    private void runTestMakeNontransactionalAll1(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.makePersistent(p2);
            tx.commit();

            tx.begin();
            pm.makePersistent(p3);
            pm.makePersistent(p4);

            Collection<PCPoint> col1 = new HashSet<>();
            col1.add(p2);
            col1.add(p3);
            col1.add(p4);
            
            try {
                pm.makeNontransactionalAll(col1);
                fail(ASSERTION_FAILED,
                     "pm.makeNontransactionalAll(Collection) should throw a JDOUserException when called for collection including P-NEW instances.");
            }
            catch (JDOUserException ex) {
                // expected exception
            }
            tx.rollback();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /* test makeNontransactionalAll (Object[] pcs) */
    private void testMakeNontransactionalAll2(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx = pm.currentTransaction();
            tx.begin();
            pm.makePersistent(p2);
            tx.commit();
            
            tx.begin();
            pm.makePersistent(p3);
            pm.makePersistent(p4);
            
            Collection<PCPoint> col1 = new HashSet<>();
            col1.add(p2);
            col1.add(p3);
            col1.add(p4);
            
            try {
                pm.makeNontransactionalAll(col1.toArray());
                fail(ASSERTION_FAILED,
                     "pm.makeNontransactionalAll(Object []) should throw a JDOUserException when called for an array including P-NEW instances.");
            }
            catch (JDOUserException ex) {
                // expected exception
            }
            tx.rollback();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
