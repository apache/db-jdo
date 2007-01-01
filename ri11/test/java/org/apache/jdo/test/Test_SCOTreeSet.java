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
import java.util.Set;
import java.util.TreeSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCCollections;
import org.apache.jdo.sco.SCOCollection;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that SCO TreeSet correctly performs all update operations
* in both datastore and optimistic transactions.
*
* @author Marina Vatkina
* @version 1.0.1
*/
public class Test_SCOTreeSet extends Test_SCO_Base {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SCOTreeSet.class);
    }

    /** */
    public void test() {
        insertObjects();
        runTreeSetTest(false, false);
    }

    /** */
    public void testRetainValues() {
        insertObjects();
        runTreeSetTest(false, true);
    }

    /** */
    public void testOptimistic() {
        insertObjects();
        runTreeSetTest(true, false);
    }

    /** */
    public void testOptimisticRetainValues() {
         insertObjects();
         runTreeSetTest(true, true);
    }

    /** */
    protected void runTreeSetTest(boolean optimistic, boolean retainValues) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug) 
                logger.debug("Running " + (optimistic?"optimistic":"datastore") + 
                             " transactions with retainValues = " + retainValues);
            tx.setOptimistic(optimistic);
            tx.setRetainValues(retainValues);

            ArrayList c = new ArrayList();
            Double p = new Double(123);
            c.add(p);
            p = new Double(1230);
            c.add(p);
            
            tx.begin();
            PCCollections pcCollections = 
                    (PCCollections) pm.getObjectById(oid_collections, true);
            tx.commit();
            
            tx.begin();
            TreeSet set = pcCollections.getSCOTreeSet();
            assertCollectionSize("TreeSet: ", set, 7);
            set.add(p);
            tx.commit();
            
            tx.begin();
            set = pcCollections.getSCOTreeSet();
            assertCollectionSize("TreeSet after add: ", set, 8);
            set.remove(p);
            tx.commit();
            
            tx.begin();
            set = pcCollections.getSCOTreeSet();
            assertCollectionSize("TreeSet after remove: ", set, 7);
            set.addAll(c);
            tx.commit();
            
            tx.begin();
            set = pcCollections.getSCOTreeSet();
            assertCollectionSize("TreeSet after addAll 2: ", set, 9);
            TreeSet h = (TreeSet) set.clone();
            h.add(p);
            assertNotIsDirty("Is dirty: ", pcCollections);
            tx.commit();
            
            tx.begin();
            set = pcCollections.getSCOTreeSet();
            assertCollectionSize("TreeSet after clone: ", set, 9);
            assertNullOwner("Owner of clone: ", (SCOCollection)h);
            set.removeAll(c);
            tx.commit();
            
            tx.begin();
            set = pcCollections.getSCOTreeSet();
            assertCollectionSize("TreeSet after removeAll 2: ", set, 7);
            set.addAll(c);
            tx.commit();
            
            tx.begin();
            set = pcCollections.getSCOTreeSet();
            assertCollectionSize("TreeSet after addAll 2: ", set, 9);
            set.retainAll(c);
            tx.commit();
            
            tx.begin();
            set = pcCollections.getSCOTreeSet();
            assertCollectionSize("TreeSet after retainAll 2: ", set, 2);
            set.clear();
            tx.commit();
            
            tx.begin();
            set = pcCollections.getSCOTreeSet();
            assertCollectionSize("TreeSet after clear: ", set, 0);
            pcCollections.setSCOTreeSet(new TreeSet());
            tx.commit();
            
            tx.begin();
            set = pcCollections.getSCOTreeSet();
            assertCollectionSize("TreeSet restored: ", set, 7);
            tx.commit(); tx = null;
            pm.close(); pm = null;
            
            // The following block tests the fix for bug 5048297:
            // the common.sco.TreeSet missed to redefined some methods that
            // need to thaw the map before performing the operation.
            // The tests fetch the object using a new pm to make sure the sco
            // is not reused from a previous tx.
            
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            pcCollections = 
                    (PCCollections) pm.getObjectById(oid_collections, true);
            set = pcCollections.getSCOTreeSet();
            Set headSet = set.headSet(new Double(1230));
            assertCollectionSize("TreeSet.headSet().size(): ", headSet, 6); 
            tx.commit(); tx = null;
            pm.close(); pm = null;
            
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            pcCollections = 
                    (PCCollections) pm.getObjectById(oid_collections, true);
            set = pcCollections.getSCOTreeSet();
            Object first = set.first();
            if (debug) logger.debug("TreeSet.first(): " + first); 
            assertEquals("TreeSet.first(): ", new Double(-123.0), first);
            tx.commit(); tx = null;
            pm.close(); pm = null;
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    // We override this from Test_ActivateClass and insert our own objects
    protected void insertObjects() {
        insertAllTypes();
    }
}
