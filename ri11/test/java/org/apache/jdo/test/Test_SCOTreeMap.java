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

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCCollections;
import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.sco.SCOMap;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that SCO TreeMap correctly performs all update operations
* in both datastore and optimistic transactions.
*
* @author Marina Vatkina
* @version 1.0.1
*/
public class Test_SCOTreeMap extends Test_SCO_Base {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SCOTreeMap.class);
    }

   /** */
    public void test() {
        insertObjects();
        runTreeMapTest(false, false);
    }

    /** */
    public void testRetainValues() {
        insertObjects();
        runTreeMapTest(false, true);
    }

    /** */
    public void testOptimitic() {
        insertObjects();
        runTreeMapTest(true, false);
    }

    /** */
    public void testOptimisticRetainValues() {
         insertObjects();
         runTreeMapTest(true, true);
    }

    /** */
    protected void runTreeMapTest(boolean optimistic, boolean retainValues) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            if (debug)
                logger.debug("Running " + (optimistic?"optimistic":"datastore") + 
                             " transactions with retainValues = " + retainValues);
            tx.setOptimistic(optimistic);
            tx.setRetainValues(retainValues);

            Map c = new HashMap();
            PCPoint p = new PCPoint(1, 1);
            Double s = new Double(123);
            c.put(s, p);
            p = new PCPoint(2, 2);
            s = new Double(1230);
            c.put(s, p);

            tx.begin();
            PCCollections pcCollections = (PCCollections) pm.getObjectById(oid_collections, true);
            tx.commit();
            
            TreeMap map = null;
            tx.begin();
            map = pcCollections.getSCOTreeMap();
            assertMapSize("TreeMap: ", map, 9);
            map.put(s, p);
            tx.commit();
            
            tx.begin();
            map = pcCollections.getSCOTreeMap();
            assertMapSize("TreeMap after put: ", map, 10);
            map.remove(s);
            tx.commit();
            
            tx.begin();
            map = pcCollections.getSCOTreeMap();
            assertMapSize("TreeMap after remove: ", map, 9);
            map.putAll(c);
            tx.commit();
            
            tx.begin();
            map = pcCollections.getSCOTreeMap();
            assertMapSize("TreeMap after putAll 2: ", map, 11);
            TreeMap h = (TreeMap) map.clone();
            h.put(s, p);
            assertNotIsDirty("Is dirty: ", pcCollections);
            tx.commit();
            
            tx.begin();
            map = pcCollections.getSCOTreeMap();
            assertMapSize("TreeMap after clone: ", map, 11);
            assertNullOwner("Owner of clone: ", (SCOMap)h);
            map.clear();
            tx.commit();
            
            tx.begin();
            map = pcCollections.getSCOTreeMap();
            assertMapSize("TreeMap after clear: ", map, 0);
            pcCollections.setSCOTreeMap(new TreeMap());
            map = pcCollections.getSCOTreeMap();
            PCPoint pcPoint = new PCPoint(42, 99);
            map.put(new Double(300000), pcPoint);
            
            tx.commit();
            
            tx.begin();
            map = pcCollections.getSCOTreeMap();
            assertMapSize("TreeMap restored: ", map, 9);
            tx.commit(); tx = null;
            pm.close(); pm = null;
            
            // The following block tests the fix for bug 5048297:
            // the common.sco.TreeMap missed to redefined some methods that
            // need to thaw the map before performing the operation.
            // The tests fetch the object using a new pm to make sure the sco
            // is not reused from a previous tx.
            
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            pcCollections = (PCCollections) pm.getObjectById(oid_collections, true);
            map = pcCollections.getSCOTreeMap();
            Map tailMap = map.tailMap(new Double(200000));
            // the size should be 1
            assertMapSize("TreeMap.tailMap().size(): ", tailMap, 1); 
            tx.commit(); tx = null;
            pm.close(); pm = null;
            
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            pcCollections = (PCCollections) pm.getObjectById(oid_collections, true);
            map = pcCollections.getSCOTreeMap();
            Object firstKey = map.firstKey();
            if (debug) logger.debug("TreeMap.firstKey: " + firstKey);
            assertEquals("TreeMap.firstKey: ", new Double(-123.0), firstKey);
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
