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

package org.apache.jdo.test;

import java.util.ArrayList;
import java.util.LinkedList;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCCollections;
import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.sco.SCOCollection;
import org.apache.jdo.test.util.JDORITestRunner;

/**
 * Tests that SCO LinkedList correctly performs all update operations
 * in both datastore and optimistic transactions.
 *
 * @author Marina Vatkina
 * @version 1.0.1
 */
public class Test_SCOLinkedList extends Test_SCO_Base {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SCOLinkedList.class);
    }

    /** */
    public void test() {
        insertObjects();
        runLinkedListTest(false, false);
    }
    
    /** */
    public void testRetainValues() {
        insertAllTypes();
        runLinkedListTest(false, true);
    }

    /** */
    public void testOptimistic() {
        insertObjects();
        runLinkedListTest(true, false);
    }
    
    /** */
    public void testOptimisticRetainValues() {
        insertAllTypes();
        runLinkedListTest(true, true);
    }
    
    /** */
    protected void runLinkedListTest(boolean optimistic, boolean retainValues) {
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
            PCCollections pcCollections = (PCCollections) pm.getObjectById(oid_collections, true);
            tx.commit();
            
            tx.begin();
            LinkedList arr = null;
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList: ", arr, 9);
            arr.add(p);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after add: ", arr, 10);
            arr.remove(p);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after remove: ", arr, 9);
            arr.addFirst(p);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after addFirst: ", arr, 10);
            if (debug) logger.debug("Element added as #: " + arr.indexOf(p));
            assertEquals("Element added as #: ", 0, arr.indexOf(p));
            Object o = arr.removeFirst();
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after removeFirst: ", arr, 9);
            if (debug) logger.debug("Element was removed: " + o);
            assertEquals("Element was removed: ", p, o);
            arr.addLast(p);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after addLast: ", arr, 10);
            if (debug) logger.debug("Element added as #: " + arr.indexOf(p));
            assertEquals("Element added as #: ", 9, arr.indexOf(p));
            o = arr.removeLast();
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after removeLast: ", arr, 9);
            if (debug) logger.debug("Element was removed: " + o);
            assertEquals("Element was removed: ", p, o);
            arr.add(1, p);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after add #1: ", arr, 10);
            o = arr.remove(1);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after remove #1: ", arr, 9);
            if (debug) logger.debug("removed #1: " + o);
            assertEquals("removed #1: ", p, o);
            arr.addAll(c);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after addAll 2: ", arr, 11);
            arr.removeAll(c);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after removeAll 2: ", arr, 9);
            arr.addAll(1, c);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after addAll 2 from 1: ", arr, 11);
            arr.retainAll(c);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after retainAll 2: ", arr, 2);
            LinkedList v = (LinkedList) arr.clone();
            v.add(p);
            assertNotIsDirty("Is dirty: ", pcCollections);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after clone: ", arr, 2);
            assertNullOwner("Owner of clone: ", (SCOCollection)v);
            o = arr.set(1, o);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after set #1: ", arr, 2);
            if (debug) logger.debug("Replaced #1: " + o);
            assertEquals("Replaced #1: ", p, o);
            arr.clear();
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList after clear: ", arr, 0);
            pcCollections.setSCOLinkedList(new LinkedList());
            PCPoint pcPoint = new PCPoint(42, 99);
            pcCollections.getSCOLinkedList().add(pcPoint);
            tx.commit();

            tx.begin();
            arr = pcCollections.getSCOLinkedList();
            assertCollectionSize("LinkedList restored: ", pcCollections.getSCOLinkedList(), 9);
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
