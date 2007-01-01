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
import java.util.Vector;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCCollections;
import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.sco.SCOCollection;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that SCO ArrayList correctly performs update operations
* in optimistic transactions.
*
* @author Marina Vatkina
*/
public class Test_SCOArrayListOptimistic extends Test_SCO_Base {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SCOArrayListOptimistic.class);
    }
    
    /** */
    public void test() {
        insertObjects();
        runArrayListOptimisticTest();
    }

    protected void runArrayListOptimisticTest() {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.setOptimistic(true);
            tx.setRetainValues(false);

            ArrayList c = new ArrayList();
            PCPoint p = new PCPoint(1, 1);
            c.add(p);
            p = new PCPoint(2, 2);
            c.add(p);
            
            tx.begin();
            PCCollections pcCollections =
                (PCCollections) pm.getObjectById(oid_collections, true);
            tx.commit();
            assertCollectionSize("ArrayList: ", pcCollections.getSCOArrayList(), 7);
            
            tx.begin();
            if (debug)
                logger.debug ("pcCollections: " + pcCollections.toString());
            tx.commit();
        
            tx.begin();
            ArrayList al = new ArrayList();
            pcCollections.setSCOArrayList(al);
            
            tx.commit();
            
            tx.begin();
            ArrayList arr = pcCollections.getSCOArrayList();
            arr.add(p);
            tx.commit();
            assertCollectionSize("ArrayList after add: ",
                                 pcCollections.getSCOArrayList(), 8);
            
            /* Next operation fails after:
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            arr.remove(p);
            tx.commit();
            assertCollectionSize("ArrayList after remove: ", pcCollections.getSCOArrayList(), ?);
            */

            tx.begin();
            arr = pcCollections.getSCOArrayList();
            arr.add(1, p);
            tx.commit();
            assertCollectionSize("ArrayList after add after 1: ",
                                 pcCollections.getSCOArrayList(), 9);
            
            /* Next operation fails after:
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            Object o = arr.remove(1);
            tx.commit();
            assertCollectionSize("ArrayList after remove #1: ", pcCollections.getSCOArrayList(), ?);
            if (verbose) logger.debug("removed #1: " + o);
            */

            tx.begin();
            arr = pcCollections.getSCOArrayList();
            arr.addAll(c);
            tx.commit();
            assertCollectionSize("ArrayList after addAll 2: ",
                                 pcCollections.getSCOArrayList(), 11);

            /* Next operation fails after:
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            arr.removeAll(c);
            tx.commit();
            assertCollectionSize("ArrayList after removeAll 2: ", pcCollections.getSCOArrayList(), ?);
            */

            tx.begin();
            arr = pcCollections.getSCOArrayList();
            arr.addAll(1, c);
            tx.commit();
            assertCollectionSize("ArrayList after addAll 2 from 1: ",
                                 pcCollections.getSCOArrayList(), 13);
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            arr.retainAll(c);
            tx.commit();
            assertCollectionSize("ArrayList after retainAll 2: ",
                                 pcCollections.getSCOArrayList(), 6);
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            c = (ArrayList) arr.clone();
            c.add(p);
            assertNotIsDirty("Is dirty: ", pcCollections);
            tx.commit();
            assertCollectionSize("ArrayList after clone: ",
                                 pcCollections.getSCOArrayList(), 6);
            assertNullOwner("Owner of clone: ", (SCOCollection)c);
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            arr.clear();
            tx.commit();
            assertCollectionSize("ArrayList after clear: ",
                                 pcCollections.getSCOArrayList(), 0);
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
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            PCCollections pcCollections = new PCCollections();
        
            ArrayList al = new ArrayList();
            pcCollections.setSCOArrayList(al);
            
            Vector v = new Vector(3);
            pcCollections.setSCOVector(v);
            
            LinkedList ll = new LinkedList();
            pcCollections.setSCOLinkedList(ll);
            
            PCPoint pcPoint = new PCPoint(42, 99);
            if (debug) logger.debug("Before makePersistent: " + pcCollections);
            
            // Next statement allows this to work whether or not reachability or
            // navigation work.
            pm.makePersistent(pcPoint);
            
            // The order of this and the previous statements is important!
            pm.makePersistent(pcCollections);
            if (debug) logger.debug("After makePersistent: " + pcCollections);
            tx.commit();
            
            
            // Next 2 statements allow this to work whether or not reachability or
            // navigation work.
            oid_point = pm.getObjectId(pcPoint);
            oids.add(oid_point);
            
            oid_collections = pm.getObjectId(pcCollections);
            
            if (debug)
                logger.debug("inserted pcCollections: " + oid_collections);
            
            oids.add(oid_collections);
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

}
