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

import java.util.HashMap;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCCollections;
import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.sco.SCOMap;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that SCO HashMap correctly performs all update operations
* in both datastore and optimistic transactions.
*
* @author Marina Vatkina
* @version 1.0.1
*/
public class Test_SCOHashMap extends Test_SCO_Base {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SCOHashMap.class);
    }

    /** */
    public void test() {
        insertObjects();
        runHashMapTest(false, false);
    }

    /** */
    public void testRetainValues() {
        insertObjects();
        runHashMapTest(false, true);
    }

    /** */
    public void testOptimistic() {
        insertObjects();
        runHashMapTest(true, false);
    }

    /** */
    public void testOptimisticRetainValues() {
        insertObjects();
        runHashMapTest(true, true);
    }

    /** */
    protected void runHashMapTest(boolean optimistic, boolean retainValues) {
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
            String s = "abc";
            c.put(s, p);
            p = new PCPoint(2, 2);
            s = "xyz";
            c.put(s, p);
            
            tx.begin();
            PCCollections pcCollections =
                (PCCollections) pm.getObjectById(oid_collections, true);
            tx.commit();
            
            tx.begin();
            HashMap map = null;
            map = pcCollections.getSCOHashMap();
            assertMapSize("HashMap: ", map, 9);
            map.put(s, p);
            tx.commit();
            
            tx.begin();
            map = pcCollections.getSCOHashMap();
            assertMapSize("HashMap after put: ", map, 10);
            map.remove(s);
            tx.commit();

            tx.begin();
            map = pcCollections.getSCOHashMap();
            assertMapSize("HashMap after remove: ", map, 9);
            map.putAll(c);
            tx.commit();

            tx.begin();
            map = pcCollections.getSCOHashMap();
            assertMapSize("HashMap after putAll 2: ", map, 11);
            HashMap h = (HashMap) map.clone();
            h.put(s, p);
            assertNotIsDirty("Is dirty: ", pcCollections);
            tx.commit();

            tx.begin();
            map = pcCollections.getSCOHashMap();
            assertMapSize("HashMap after clone: ", map, 11);
            assertNullOwner("Owner of clone: ",(SCOMap)h);
            map.clear();
            tx.commit();

            tx.begin();
            map = pcCollections.getSCOHashMap();
            assertMapSize("HashMap after clear: ", map, 0);
            pcCollections.setSCOHashMap(new HashMap());
            PCPoint pcPoint = new PCPoint(42, 99);
            pcCollections.getSCOHashMap().put("hello", pcPoint);

            tx.commit();

            tx.begin();
            map = pcCollections.getSCOHashMap();
            assertMapSize("HashMap restored: ", map, 9);
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
