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
import java.util.Iterator;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
/**
 *<B>Title:</B> Make Nontransactional a Dirty Instance
 *<BR>
 *<B>Keywords:</B> exception
 *<BR>
 *<B>Assertion IDs:</B> A12.5.7-28
 *<BR>
 *<B>Assertion Description: </B>
If PersistenceManager.makeNontransactional or makeNontransactionalAll is called with an explicit dirty parameter instance, a JDOUserException is thrown.

 */

public class MakeNontransactionalDirtyInstance extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.7-28 (MakeNontransactionalDirtyInstance) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MakeNontransactionalDirtyInstance.class);
    }

    private PCPoint p1 = null;
    private PCPoint p2 = null;
    private PCPoint p3 = null;
    private PCPoint p4 = null;
    private PCPoint p5 = null;
    private PCPoint p6 = null;
    private PCPoint p7 = null;

    private PCPoint c1 = null;
    private PCPoint c2 = null;
    private PCPoint c3 = null;
    private PCPoint c4 = null;
    private PCPoint c5 = null;
    private PCPoint c6 = null;
    private PCPoint c7 = null;

    private Collection pcol1 = new HashSet();
    private Collection pcol2 = new HashSet();
    private Collection ccol1 = new HashSet();
    private Collection ccol2 = new HashSet();

    /** */
    public void testMakeNontransactional() {
        pm = getPM();
        /*  call with an explicit persistent dirty parameter instance */
        String pstate="persistent";
        createPersistentObjects(pm);
        if (debug)
            logger.debug(" -- testing with persistent dirty instance -- ");
        runTestMakeNontransactionalDirtyInstance(pm, p1,pstate);
        runTestMakeNontransactionalDirtyInstanceAll1(pm, pcol1, pstate);
        runTestMakeNontransactionalDirtyInstanceAll2(pm, pcol2, pstate);
        
        /*  call with an explicit transient dirty parameter instance */
        pstate="transient";
        createTcleanObjects(pm);
        if (debug)
            logger.debug (" -- testing with transient dirty instance -- ");
        runTestMakeNontransactionalDirtyInstance(pm, c1,pstate);
        runTestMakeNontransactionalDirtyInstanceAll1(pm, ccol1, pstate);
        runTestMakeNontransactionalDirtyInstanceAll2(pm, ccol2, pstate);
        
        pm.close();
        pm = null;
    }
    
    /** */
    private void createTcleanObjects(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            c1 = new PCPoint(1,3);
            c2 = new PCPoint(2,4);
            c3 = new PCPoint(3,5);
            c4 = new PCPoint(4,6);
            c5 = new PCPoint (5,7);
            c6 = new PCPoint (6,8);
            c7 = new PCPoint (7,9);
            
            ccol1.add(c2);
            ccol1.add(c3);
            ccol1.add(c4);
            
            ccol2.add(c5);
            ccol2.add(c6);
            ccol2.add(c7);
            
            pm.makeTransactional(c1);
            pm.makeTransactional(c2);
            pm.makeTransactional(c3);
            pm.makeTransactional(c4);
            pm.makeTransactional(c5);
            pm.makeTransactional(c6);
            pm.makeTransactional(c7);
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void createPersistentObjects(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            p1 = new PCPoint(1,3);
            p2 = new PCPoint(2,4);
            p3 = new PCPoint(3,5);
            p4 = new PCPoint(4,6);
            p5 = new PCPoint (5,7);
            p6 = new PCPoint (6,8);
            p7 = new PCPoint (7,9);

            pm.makePersistent(p1);
            pm.makePersistent(p2);
            pm.makePersistent(p3);
            pm.makePersistent(p4);
            pm.makePersistent(p5);
            pm.makePersistent(p6);
            pm.makePersistent(p7);

            pcol1.add(p2);
            pcol1.add(p3);
            pcol1.add(p4);

            pcol2.add(p5);
            pcol2.add(p6);
            pcol2.add(p7);
            tx.commit();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
    
    /** */
    private void runTestMakeNontransactionalDirtyInstance(PersistenceManager pm, PCPoint obj, String state) {
        Transaction tx = pm.currentTransaction();
        try {
            if (debug)
                logger.debug(" ** in testMakeNontransactionalDirtyInstance()");
            int curr;
            tx.begin();
            if (makeAndTestDirtyInstance(obj,state)) {
                try {
                    pm.makeNontransactional(obj);
                    fail (ASSERTION_FAILED, 
                          "pm.makeNontransactional should throw a JDOUserException when called for P-DIRTY instance.");
                }
                catch (JDOUserException ex) {
                    // expected exception
                }
            }
            tx.rollback();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestMakeNontransactionalDirtyInstanceAll1(PersistenceManager pm, Collection tcol, String state) {
        Transaction tx = pm.currentTransaction();
        boolean stopFlag = false;
        try {
            if (debug)
                logger.debug(" ** in testMakeNontransactionalDirtyInstanceAll1()");
            int curr;
            tx.begin();

            Iterator iter = tcol.iterator();
            while (iter.hasNext()) {
                PCPoint p = (PCPoint) iter.next();
                if (makeAndTestDirtyInstance(p,state))
                    stopFlag = true;
            }

            if (! stopFlag) {
                try {
                    pm.makeNontransactionalAll(tcol);
                    fail (ASSERTION_FAILED, 
                          "pm.makeNontransactionalAll(Collection) should throw a JDOUserException when called for a collection including P-DIRTY instances.");
                }
                catch (JDOUserException ex) {
                    // expected exception
                }
            }
            tx.rollback();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestMakeNontransactionalDirtyInstanceAll2(PersistenceManager pm, 
                                                              Collection tcol, 
                                                              String state) {
        Transaction tx = pm.currentTransaction();
        boolean stopFlag = false;
        try {
            if (debug)
                logger.debug(" ** in testMakeNontransactionalDirtyInstanceAll2()");
            tx.begin();

            Iterator iter = tcol.iterator();
            while (iter.hasNext()) {
                PCPoint p = (PCPoint) iter.next();
                if (makeAndTestDirtyInstance(p,state))
                    stopFlag = true;
            }

            Object[] objArray = tcol.toArray();

            if (! stopFlag) {
                try {
                    pm.makeNontransactionalAll(objArray);
                    fail (ASSERTION_FAILED, 
                          "pm.makeNontransactionalAll(Object []) should throw a JDOUserException when called for an arry including P-DIRTY instances.");
                }
                catch (JDOUserException ex) {
                    // expected exception
                }
            }
            tx.rollback();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private boolean makeAndTestDirtyInstance (PCPoint obj, String state) {
        int i = obj.getX();
        i = i*10;

        obj.setX(i);
        int curr = currentState(obj);

        if (state.equals("persistent")) {
            if ( curr != PERSISTENT_DIRTY ) {
                fail(ASSERTION_FAILED,
                     " Object not in persistent dirty state " +
                     " for " + obj.getX() + " current state: " + curr);
                return false;
            }
        }
        else if (state.equals("transient")) {
            if ( curr != TRANSIENT_DIRTY ) {
                fail(ASSERTION_FAILED,
                     " Object not in transient dirty state " +
                     " for " + obj.getX() + " current state: " + curr);
                return false;
            }
        }
        return true;
    }
}

