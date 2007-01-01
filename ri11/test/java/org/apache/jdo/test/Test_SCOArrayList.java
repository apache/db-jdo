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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCCollections;
import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.sco.SCOCollection;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that SCO ArrayList correctly performs all update operations
* in both datastore and optimistic transactions.
*
* @author Marina Vatkina
* @version 1.0.1
*/
public class Test_SCOArrayList extends Test_SCO_Base {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SCOArrayList.class);
    }

    /** */
    public void test() {
         insertObjects();
         runArrayListTest(false, false);
    }
    
    /** */
    public void testRetainValues() {
         insertObjects();
         runArrayListTest(false, true);
    }

    /** */
    public void testOptimistic() {
         insertObjects();
         runArrayListTest(true, false);
    }

    /** */
    public void testOptimisticRetainValues() {
         insertObjects();
         runArrayListTest(true, true);
    }

    /** */
    protected void runArrayListTest(boolean optimistic, boolean retainValues) {
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
            ArrayList arr = null;
            arr = pcCollections.getSCOArrayList();
            assertCollectionSize("ArrayList: ", arr, 8);
            arr.add(p);
            tx.commit();
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            assertCollectionSize("ArrayList after add: ", arr, 9);
            arr.remove(p);
            tx.commit();
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            assertCollectionSize("ArrayList after remove: ", arr, 8);
            arr.add(1, p);
            tx.commit();
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            assertCollectionSize("ArrayList after add after 1: ", arr, 9);
            Object o = arr.remove(1);
            tx.commit();
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            assertCollectionSize("ArrayList after remove #1: ", arr, 8);
            if (debug) logger.debug("removed #1: " + o);
            assertEquals("Wrong removed instance", p, o);
            arr.addAll(c);
            tx.commit();
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            assertCollectionSize("ArrayList after addAll 2: ", arr, 10);
            arr.removeAll(c);
            tx.commit();
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            assertCollectionSize("ArrayList after removeAll 2: ", arr, 8);
            arr.addAll(1, c);
            tx.commit();
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            assertCollectionSize("ArrayList after addAll 2 from 1: ", arr, 10);
            arr.retainAll(c);
            tx.commit();
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            assertCollectionSize("ArrayList after retainAll 2: ", arr, 2);
            c = (ArrayList) arr.clone();
            c.add(p);
            assertNotIsDirty("Is dirty: ", pcCollections);
            tx.commit();
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            assertCollectionSize("ArrayList after clone: ", arr, 2);
            assertNullOwner("Owner of clone: ", (SCOCollection)c);
            arr.clear();
            tx.commit();
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            assertCollectionSize("ArrayList after clear: ", arr, 0);
            pcCollections.setSCOArrayList(new ArrayList());
            PCPoint pcPoint = new PCPoint(42, 99);
            pcCollections.getSCOArrayList().add(pcPoint);
            tx.commit();
            
            tx.begin();
            arr = pcCollections.getSCOArrayList();
            assertCollectionSize("ArrayList restored: ", arr, 8);
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
