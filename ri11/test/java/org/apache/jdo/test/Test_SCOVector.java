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

package org.apache.jdo.test;

import java.util.ArrayList;
import java.util.Vector;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCCollections;
import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.sco.SCOCollection;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that SCO Vector correctly performs all update operations
* in both datastore and optimistic transactions.
*
* @author Marina Vatkina
* @version 1.0.1
*/
public class Test_SCOVector extends Test_SCO_Base {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SCOVector.class);
    }

    /** */
    public void test() {
        insertObjects();
        runVectorTest(false, false);
    }
    
    /** */
    public void testRetainValues() {
        insertObjects();
        runVectorTest(false, true);
    }
    
    /** */
    public void testOptimistic() {
        insertObjects();
        runVectorTest(true, false);
    }

    /** */
    public void testOptimisticRetainValues() {
        insertObjects();
        runVectorTest(true, true);
    }

    /** */
    protected void runVectorTest(boolean optimistic, boolean retainValues) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug)
                logger.debug("Running " + (optimistic?"optimistic":"datastore") + 
                             " transactions with retainValues = " + retainValues);
            tx.setOptimistic(optimistic);
            tx.setRetainValues(retainValues);

            ArrayList c = new ArrayList();
            PCPoint p = new PCPoint(1, 1);
            c.add(p);
            p = new PCPoint(2, 2);
            c.add(p);

            tx.begin();
            PCCollections pcCollections =
                (PCCollections) pm.getObjectById(oid_collections, true);
            tx.commit();

            tx.begin();
            Vector arr = null;
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector: ", arr, 9);
            arr.add(p);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after add: ", arr, 10);
            arr.remove(p);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after remove: ", arr, 9);
            arr.addElement(p);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after addElement: ", arr, 10);
            if (debug) logger.debug("Element added as #: " + arr.indexOf(p));
            assertEquals("Element added as #: ", 9, arr.indexOf(p));
            boolean b = arr.removeElement(p);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after removeElement: ", arr, 9);
            if (debug) logger.debug("Element was removed: " + b);
            assertTrue("Element not removed", b);
            arr.setElementAt(p, 1);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after setElementAt: ", arr, 9);
            if (debug) logger.debug("Element set as #: " + arr.indexOf(p));
            assertEquals("Element set as #: ", 1, arr.indexOf(p));
            arr.removeElementAt(1);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after removeElementAt #1: ", arr, 8);
            arr.add(1, p);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after add #1: ", arr, 9);
            Object o = arr.remove(1);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after remove #1: ", arr, 8);
            if (debug) logger.debug("removed #1: " + o);
            assertEquals("removed #1: ", p, o);
            arr.addAll(c);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after addAll 2: ", arr, 10);
            arr.removeAll(c);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after removeAll 2: ", arr, 8);
            arr.addAll(1, c);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after addAll 2 from 1: ", arr, 10);
            arr.retainAll(c);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after retainAll 2: ", arr, 2);
            arr.insertElementAt(p, 1);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after insertElementAt #1: ", arr, 3);
            Vector v = (Vector) arr.clone();
            v.add(p);
            assertNotIsDirty("Is dirty: ", pcCollections);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after clone: ", arr, 3);
            assertNullOwner("Owner of clone: ", (SCOCollection)v);
            arr.removeAllElements();
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after removeAllElements: ", arr, 0);
            pcCollections.setSCOVector(new Vector());
            arr = pcCollections.getSCOVector();
            o = arr.set(1, p);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after restore and set #1: ", arr, 8);
            if (debug) logger.debug("Replaced #1: " + o);
            assertEquals("Replaced #1: ", "Call me Ishmael.", o);
            arr.clear();
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector after clear: ", arr, 0);
            pcCollections.setSCOVector(new Vector());
            PCPoint pcPoint = new PCPoint(42, 99);
            pcCollections.getSCOVector().add(pcPoint);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOVector();
            assertCollectionSize("Vector restored: ", arr, 9);
            tx.commit();
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    /** */
    protected void insertObjects() {
        insertAllTypes();
    }
}
