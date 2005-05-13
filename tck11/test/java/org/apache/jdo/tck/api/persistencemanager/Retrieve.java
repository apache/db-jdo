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


package org.apache.jdo.tck.api.persistencemanager;

import java.util.ArrayList;
import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PCPoint2;
import org.apache.jdo.tck.pc.mylib.PCRect;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Retrieve
 *<BR>
 *<B>Keywords:</B> cache
 *<BR>
 *<B>Assertion IDs:</B> A12.6.1-2, A12.6.1-5
 *<BR>
 *<B>Assertion Description: </B>
These methods request the PersistenceManager to load all persistent fields into 
the parameter instances. Subsequent to this call, the application might 
call makeTransient on the parameter instances, and the fields can no 
longer be touched by the PersistenceManager. The PersistenceManager 
might also retrieve related instances according to a pre-read policy 
(not specified by JDO).
The JDO PersistenceManager loads persistent values from the datastore into the instance
and if the class of the instance implements InstanceCallbacks calls jdoPostLoad.
 */

public class Retrieve extends PersistenceManagerTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertions A12.6.1-2, A12.6.1-5 (Retrieve) failed: ";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(Retrieve.class);
    }

    private PCPoint p1 = null;
    private String p1print = null;
    private PCPoint p2 = null;
    private PCPoint2 p3 = null;
    private PCRect rect = null;

    private static final Integer one = new Integer(1);
    private static final Integer three = new Integer(3);

    /** */
    public void test () {
        pm = getPM();

        runTestRetrieve(pm);
        runTestRetrieveAllWithCollection(pm);
        runTestRetrieveAllWithArray(pm);
        runTestRetrieveAllWithCollectionDFGtrue(pm);
        runTestRetrieveAllWithArrayDFGtrue(pm);
        runTestRetrieveAllWithCollectionDFGfalse(pm);
        runTestRetrieveAllWithArrayDFGfalse(pm);

        pm.close();
        pm = null;
    }

    /** */
    private void runTestRetrieve(PersistenceManager pm) {
        createObjects(pm);
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.retrieve(p1);
            pm.retrieve(p3);
            pm.retrieve(rect);
            pm.makeTransient(p1);
            pm.makeTransient(rect);
            tx.commit();
            tx = null;
            checkP1();
            checkP3();
            checkRectP1();
            checkRectId();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestRetrieveAllWithCollection(PersistenceManager pm) {
        createObjects(pm);
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Collection coll = new ArrayList();
            coll.add(p1);
            coll.add(p3);
            coll.add(rect);
            pm.retrieveAll(coll);
            pm.makeTransientAll(coll);
            tx.commit();
            tx = null;
            checkP1();
            checkP3();
            checkRectP1();
            checkRectId();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestRetrieveAllWithCollectionDFGtrue(PersistenceManager pm) {
        createObjects(pm);
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Collection coll = new ArrayList();
            coll.add(p1);
            coll.add(p3);
            coll.add(rect);
            pm.retrieveAll(coll, true);
            pm.makeTransientAll(coll);
            tx.commit();
            tx = null;
            checkP1();
            checkP3();
            // checkRectP1(); p1 is not in the default fetch group by default
            checkRectId();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestRetrieveAllWithCollectionDFGfalse(PersistenceManager pm) {
        createObjects(pm);
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Collection coll = new ArrayList();
            coll.add(p1);
            coll.add(p3);
            coll.add(rect);
            pm.retrieveAll(coll, true);
            pm.makeTransientAll(coll);
            tx.commit();
            tx = null;
            checkP1();
            checkP3();
            checkRectP1();
            checkRectId();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestRetrieveAllWithArray(PersistenceManager pm) {
        createObjects(pm);
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Object[] objs = new Object[3];
            objs[0] = p1;
            objs[1] = p3;
            objs[2] = rect;
            pm.retrieveAll(objs);
            pm.makeTransientAll(objs);
            tx.commit();
            tx = null;
            checkP1();
            checkP3();
            checkRectP1();
            checkRectId();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestRetrieveAllWithArrayDFGtrue(PersistenceManager pm) {
        createObjects(pm);
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Object[] objs = new Object[3];
            objs[0] = p1;
            objs[1] = p3;
            objs[2] = rect;
            pm.retrieveAll(objs, true);
            pm.makeTransientAll(objs);
            tx.commit();
            tx = null;
            checkP1();
            checkP3();
            // checkRectP1(); p1 is not in the default fetch group by default
            checkRectId();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestRetrieveAllWithArrayDFGfalse(PersistenceManager pm) {
        createObjects(pm);
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Object[] objs = new Object[3];
            objs[0] = p1;
            objs[1] = p3;
            objs[2] = rect;
            pm.retrieveAll(objs, true);
            pm.makeTransientAll(objs);
            tx.commit();
            tx = null;
            checkP1();
            checkP3();
            checkRectP1();
            checkRectId();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void createObjects(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.setRetainValues(false);
            tx.begin();
            p1   = new PCPoint(1,1);
            p1print = p1.toString();
            p2   = new PCPoint(2,2);
            p3   = new PCPoint2(3,3);
            rect = new PCRect(100, p1, p2);
            pm.makePersistent(p1);
            pm.makePersistent(p2);
            pm.makePersistent(p3);
            pm.makePersistent(rect);
            if (debug) {
                logger.debug("p1: " + p1.name());
                logger.debug("p2: " + p2.name());
                logger.debug("p3: " + p3.name());
                logger.debug("rect id: " + rect.getId() + 
                             ", upperLeft: " + rect.getUpperLeft().name() + 
                             ", lowerRight: " + rect.getLowerRight().name());
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
    private void checkP1() {
        if (((p1.getX() != 1) || (!one.equals(p1.getY())))) {
            fail(ASSERTION_FAILED,
                 "Error in p1 fields. Expected x:1, y:1; got x:" + p1.getX() +
                 ", y:" + p1.getY());
        }
    }

    /** */
    private void checkP3() {
        if ((p3.getX() != 3) || (!three.equals(p3.getY()))) {
            fail(ASSERTION_FAILED,
                 "Error in p3 fields. Expected x:3, y:3; got x:" + p3.getX() +
                 ", y:" + p3.getY());
        }

        if (!p3.wasPostLoadCalled()) {
            fail(ASSERTION_FAILED,
                 "missing call of jdoPostLoad for p3");
        }
    }

    /** */
    private void checkRectId() {
        if (rect.getId() == 0) {
            fail(ASSERTION_FAILED,
                 "Error in rect field id. Expected id!= 0; got id:0");
        }
    }

    /** */
    private void checkRectP1() {
        if (rect.getUpperLeft()!= p1) {
            fail(ASSERTION_FAILED,
	         "Error in rect field upperLeft. Expected:" + p1print +
                 ", got:" + rect.getUpperLeft());
        }
    }
}
