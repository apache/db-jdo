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
 
package org.apache.jdo.tck.models.fieldtypes;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.MapStringValueCollections;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Support of field type Map, varying key type
 *<BR>
 *<B>Keywords:</B> model
 *<BR>
 *<B>Assertion ID:</B> A6.4.3-35.
 *<BR>
 *<B>Assertion Description: </B>
If the Map optional feature is supported, then JDO implementations
must support fields of the interface type <code>Map</code>,
supporting them as Second Class Objects or First Class Objects.
 */


public class TestMapStringValueCollections extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A6.4.3-35 (TestMapStringValueCollections) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(TestMapStringValueCollections.class);
    }   

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(MapStringValueCollections.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
         
        runTest(pm);
        
        pm.close();
        pm = null;
    }
    
    /** */
    void runTest(PersistenceManager pm) {
        if (!isMapSupported()) {
            if (debug)
                logger.debug("JDO Implementation does not support the optional feature Map");
            return;
        }
        if (!isHashMapSupported() && !isTreeMapSupported()) {
            fail(ASSERTION_FAILED,
                 "JDO Implementation supports Map, but neither HashMap nor TreeMap.");
        }

        Transaction tx = pm.currentTransaction();
        try {
            int i, j, n;
            FirstSetOfTestValuesForCollection firstValue = 
                new FirstSetOfTestValuesForCollection();
            SecondSetOfTestValuesForCollection secondValue = 
                new SecondSetOfTestValuesForCollection();

            int ret = 0;
            // turn on datastore transactions
            tx.setOptimistic(false);
            tx.begin();
            MapStringValueCollections pi = new MapStringValueCollections();
            pi.identifier = 1;
            pm.makePersistent(pi);
            Object oid = pm.getObjectId(pi);
            n = pi.getLength();
            // Provide initial set of values
            for(i = 0; i < n; ++i){
                Vector fieldSpecs = TestUtil.getFieldSpecsForMap(
                    MapStringValueCollections.fieldSpecs[i]);
                String fieldType = (String)fieldSpecs.get(0);
                String valueType = (String)fieldSpecs.get(1);
                Map map = new HashMap();
                Vector keys = (Vector) firstValue.get(fieldType);
                Vector values = (Vector) secondValue.get(valueType);

                for (j = 0; j< keys.size(); j++) {
                    map.put(keys.get(j), values.get(j));
                }

                pi.set(i, map);
            }
            tx.commit();
            // cache will be flushed
            pi = null;
            System.gc();

            tx.begin();

            Hashtable firstSet = new Hashtable();
            firstSet.put("keys", firstValue);
            firstSet.put("values", secondValue);

            checkValues(oid, firstSet); // check if persistent fields have values set
            pi = (MapStringValueCollections) pm.getObjectById(oid, true);

            // Provide new set of values     -- reverse the keys and values
            for(i = 0; i < n; ++i){
                Vector fieldSpecs = TestUtil.getFieldSpecsForMap(
                    MapStringValueCollections.fieldSpecs[i]);
                String fieldType = (String)fieldSpecs.get(0);
                String valueType = (String)fieldSpecs.get(1);
                Map map = new HashMap();
                Vector keys = (Vector) secondValue.get(fieldType);
                Vector values = (Vector) firstValue.get(valueType);

                for (j = 0; j< keys.size(); j++) {
                    map.put(keys.get(j), values.get(j));
                }
                pi.set(i, map);
            }

            tx.commit();
            // cache will be flushed
            pi = null;
            System.gc();

            tx.begin();
            // check new values
            Hashtable secondSet = new Hashtable();
            secondSet.put("keys", secondValue);
            secondSet.put("values", firstValue);

            checkValues(oid, secondSet);
            pi = (MapStringValueCollections) pm.getObjectById(oid, true);
            pm.deletePersistent(pi);
            tx.commit();
            tx = null;
        }
        catch (IndexOutOfBoundsException e) {
            logger.error(e.getMessage());
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    private void checkValues(Object oid, Hashtable startValue)
    {
        int i, j;

        MapStringValueCollections pi = 
            (MapStringValueCollections) pm.getObjectById(oid, true);
        int n = pi.getLength();

        Hashtable keySet = (Hashtable) startValue.get("keys");
        Hashtable valueSet = (Hashtable) startValue.get("values");

        for( i = 0; i < n; ++i){
            Vector fieldSpecs = TestUtil.getFieldSpecsForMap(
                MapStringValueCollections.fieldSpecs[i]);
            String fieldType = (String)fieldSpecs.get(0);
            String valueType = (String)fieldSpecs.get(1);
            Map compareWith = new HashMap();

            Vector keys = (Vector) keySet.get(fieldType);
            Vector values = (Vector) valueSet.get(valueType);
            int sz = keys.size();
            for (j = 0; j < sz; j++) {
                compareWith.put(keys.get(j), values.get(j));
            }

            Map val = pi.get(i);

            if(!val.equals(compareWith)){
                fail(ASSERTION_FAILED,
                     "Incorrect value for " + MapStringValueCollections.fieldSpecs[i]);
            }
        }
    }

}
