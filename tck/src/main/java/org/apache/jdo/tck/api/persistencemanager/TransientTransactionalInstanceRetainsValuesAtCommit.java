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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Transient Transactional Instance Retains Values At Commit
 *<BR>
 *<B>Keywords:</B> transienttransactional
 *<BR>
 *<B>Assertion ID:</B> A12.5.7-21
 *<BR>
 *<B>Assertion Description: </B>
If the transaction in which an instance is made transactional (by calling PersistenceManager.makeTransactional or makeTransactionalAll) commits, then the transient instance retains its values.

 */

public class TransientTransactionalInstanceRetainsValuesAtCommit extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.7-21 (TransientTransactionalInstanceRetainsValuesAtCommit) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(TransientTransactionalInstanceRetainsValuesAtCommit.class);
    }

    private PCPoint p1 = null;
    private PCPoint p2 = null;
    private PCPoint p3 = null;
    private PCPoint p4 = null;
    private PCPoint p5 = null;

    private Collection<PCPoint> col1 = new HashSet<>();
    private Collection<PCPoint> col2 = new HashSet<>();


    /** */
    public void testTransientTransactionalInstanceRetainsValuesAtCommit() {
        pm = getPM();

        createObjects(pm);
        runTestTransientTransactional1(pm);
        runTestTransientTransactional2(pm);
        runTestTransientTransactional3(pm);

        pm.close();
        pm = null;
    }

    /** */
    private void createObjects(PersistenceManager pm) {
        p1 = new PCPoint(1,3);
        p2 = new PCPoint(2,4);
        p3 = new PCPoint(3,5);
        p4 = new PCPoint(4,6);
        p5 = new PCPoint (5,7);
        
        col1.add(p2);
        col1.add(p3);
        
        col2.add(p4);
        col2.add(p5);
    }

    /** */
    private void runTestTransientTransactional1(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        if (debug) logger.debug(" ** in runTestTransientTransactional1() ");
        try {
            PCPoint p1 = new PCPoint(8,8);
            p1.setX(100);
            tx.begin();
            p1.setX(200);
            pm.makeTransactional(p1);
            p1.setX(300);
            tx.commit();
            tx = null;

            assertGetX (p1, 300, ASSERTION_FAILED, "runTestTransientTransactional1");
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestTransientTransactional2(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        if (debug) logger.debug(" ** in runTestTransientTransactional2() ");
        try {
            p2.setX(100);
            p3.setX(100);
            tx.begin();
            p2.setX(201);
            p3.setX(201);
            pm.makeTransactionalAll(col1);
            p2.setX(301);
            p3.setX(301);
            tx.commit();
            tx = null;

            assertGetX (p2, 301, ASSERTION_FAILED, "runTestTransientTransactional2");
            assertGetX (p3, 301, ASSERTION_FAILED, "runTestTransientTransactional2");
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestTransientTransactional3(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        if (debug) logger.debug(" ** in runTestTransientTransactional3() ");
        try {
            p4.setX(100);
            p5.setX(100);
            tx.begin();
            p4.setX(201);
            p5.setX(201);
            pm.makeTransactionalAll(col2.toArray());
            p4.setX(301);
            p5.setX(301);
            tx.commit();
            tx = null;

            assertGetX (p4, 301, ASSERTION_FAILED, "runTestTransientTransactional2");
            assertGetX (p5, 301, ASSERTION_FAILED, "runTestTransientTransactional2");
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
