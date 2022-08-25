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
 *<B>Title:</B> MakeTransactionalAllFails
 *<BR>
 *<B>Keywords:</B> exception
 *<BR>
 *<B>Assertion ID:</B> A12.5.7-4
 *<BR>
 *<B>Assertion Description: </B>
If a collection or array of instances is passed to
 *PersistenceManager.makeTransactionalAll, and one or more of the instances
 *fail to complete the required o peration, then all instances will be
 *attempted, and a JDOUserException will be thrown which contains a nested
 *exception array, each exception of which conta ins one of the failing
 *instances. The succeeding instances will transition to the specified life
 *cycle state, and the failing instances will remain in their current state.

 */

public class MakeTransactionalAllFails extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.7-4 (MakeTransactionalAllFails) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MakeTransactionalAllFails.class);
    }
 
    private PCPoint p1 = null;
    private Point p2 = null;
    private Collection<Object> col1 = new HashSet<>();

    /** */
    public void testTransactionalInst() {
        pm = getPM();

        createObjects();
        runTestMakeTransactionalAll1(pm);
        runTestMakeTransactionalAll2(pm);

        pm.close();
        pm = null;
    }

    /** */
    private void createObjects() {
        p1 = new PCPoint(1,3);
        p2 = new Point(2,4);
        col1 = new HashSet<>();
        col1.add(p1);
        col1.add(p2);
    }

    /* test makeTransactional (Collection pcs) */
    private void runTestMakeTransactionalAll1(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        if (debug) logger.debug(" in runTestMakeTransactionalAll2() ");
        try {
            tx.begin();
            
            col1.add(p1);
            col1.add(p2);
            try {
                pm.makeTransactionalAll(col1);
                fail(ASSERTION_FAILED,
                     "pm.makeTransactionalAll(Collection) should throw JDOUserException when called for collection icnluding instance of non-pc class");
            }
            catch (JDOUserException ex) {
                // expected exception
            }
            tx.rollback();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /* test makeTransactionalAll (Object[] pcs) */
    private void runTestMakeTransactionalAll2(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        if (debug) logger.debug(" in runTestMakeTransactionalAll2() ");
        try {
            tx.begin();
            try {
                pm.makeTransactionalAll(col1.toArray());
                fail(ASSERTION_FAILED,
                     "pm.makeTransactionalAll(Object[]) should throw JDOUserException when called for collection icnluding instance of non-pc class");
            }
            catch (JDOUserException ex) {
                // expected exception
            }
            tx.rollback();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
