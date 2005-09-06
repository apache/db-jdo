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


package org.apache.jdo.tck.api.persistencemanager.lifecycle;

import java.util.Collection;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;

/**
 *<B>Title:</B> MakePersistent Fails If Instance Managed By Another PersistenceManager
 *<BR>
 *<B>Keywords:</B> exception
 *<BR>
 *<B>Assertion IDs:</B> A12.5.7-8.
 *<BR>
 *<B>Assertion Description: </B>
PersistenceManager.makePersistent and makePersistentAll will throw a JDOUserException if the parameter instance is managed by a different PersistenceManager.

 */

public class MakePersistentFailsIfInstanceManagedByAnotherPersistenceManager extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.7-8 (MakePersistentFailsIfInstanceManagedByAnotherPersistenceManager) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MakePersistentFailsIfInstanceManagedByAnotherPersistenceManager.class);
    }

    private PCPoint p1 = null;
    private PCPoint p2 = null;
    private PCPoint p3 = null;
    private PCPoint p4 = null;
    private PCPoint p5 = null;

    /** */
    public void testMakePersistentFailsIfInstanceManagedByAnotherPersistenceManager() {
        pm = getPM();

        PersistenceManager pm2 = getPMF().getPersistenceManager();
        try {
            createObjects(pm2);
        
            /* positive tests */
            runTestMakePersistent(pm);
            runTestMakePersistentAll1(pm);
            runTestMakePersistentAll2(pm);
        }
        finally {
            cleanupPM(pm2);
            pm2 = null;
            cleanupPM(pm);
            pm = null;
        }
    }

    /** */
    private void  createObjects(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
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
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /* test makePersistent (Object pc) */
    private void runTestMakePersistent(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            try {
                pm.makePersistent(p1);
                fail(ASSERTION_FAILED,
                     "pm.makePersistent should throw JDOUserException if instance is already made persistence by different pm.");
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

    /* test makePersistentAll (Collection pcs) */
    private void runTestMakePersistentAll1(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Collection col1 = new java.util.HashSet();
            col1.add(p2);
            col1.add(p3);
            
            try {
                pm.makePersistentAll(col1);
                fail(ASSERTION_FAILED,
                     "pm.makePersistentAll(Collection) should throw JDOUserException if instance is already made persistence by different pm.");
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

    /* test makePersistentAll (Object[] o) */
    private void runTestMakePersistentAll2(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Collection col1 = new java.util.HashSet();
            col1.add(p4);
            col1.add(p5);
            Object[] obj1= col1.toArray();

            try {
                pm.makePersistentAll(obj1);
                fail(ASSERTION_FAILED,
                     "pm.makePersistentAll(Object[]) should throw JDOUserException if instance is already made persistence by different pm.");
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
