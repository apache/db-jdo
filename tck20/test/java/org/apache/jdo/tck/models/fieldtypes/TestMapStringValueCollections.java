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
                logger.debug("JDO Implementation does not support " +
                        "the optional feature Map");
            return;
        }
        if (!isMapSupported() && !isTreeMapSupported()) {
            fail(ASSERTION_FAILED,
                 "JDO Implementation supports Map, but neither " +
                 "Map nor TreeMap.");
        }

        Transaction tx = pm.currentTransaction();
        MapStringValueCollections expectedValue =
                new MapStringValueCollections();
        
        // turn on datastore transactions
        tx.setOptimistic(false);
        tx.begin();
        MapStringValueCollections pi = new MapStringValueCollections();
        pi.identifier = 1;
        pm.makePersistent(pi);
        Object oid = pm.getObjectId(pi);
        // Provide initial set of values
        setValues(pi, 1);
        tx.commit();

        // cache will be flushed
        pi = null;
        System.gc();

        tx.begin();
        setValues(expectedValue, 1);
        checkValues(oid, expectedValue);
        pi = (MapStringValueCollections) pm.getObjectById(oid, true);
        setValues(pi, 2);
        tx.commit();

        // cache will be flushed
        pi = null;
        System.gc();

        tx.begin();
        // check new values
        setValues(expectedValue, 2);
        checkValues(oid, expectedValue);
        tx.commit();
    }

    /** */
    private void setValues(MapStringValueCollections collect, int order)
    {
        int keyOrder = order;
        int valueOrder = (order == 1) ? 2 : 1; // why??
        int n = collect.getLength();
        for (int i = 0; i < n; ++i) {
            Vector fieldSpecs = TestUtil.getFieldSpecsForMap(
                    MapStringValueCollections.fieldSpecs[i]);
            Vector key = TestUtil.makeNewVectorInstance(
                    (String)fieldSpecs.get(0), keyOrder);
            Vector value = TestUtil.makeNewVectorInstance(
                    (String)fieldSpecs.get(1), valueOrder);

            HashMap map = new HashMap();
            for (int j = 0; j< key.size(); j++) {
                map.put(key.get(j), value.get(j));
            }
            collect.set(i, map);
            if (debug)
                logger.debug("Set " + i + "th value to: " + map.toString());
        }
    }

    /** */
    private void checkValues(Object oid,
            MapStringValueCollections expectedValue)
    {
        StringBuffer sbuf = new StringBuffer();
        MapStringValueCollections pi = (MapStringValueCollections)
                pm.getObjectById(oid, true);
        int n = pi.getLength();
        for (int i = 0; i < n; ++i) {
            Map compareWith = expectedValue.get(i);
            Map val = pi.get(i);
            if (val.size() != compareWith.size()) {
                sbuf.append("\nFor element " + i + ", expected size = " +
                        compareWith.size() + ", actual size = " + val.size()
                        + " . ");
                continue;
            }
            if (! val.equals(compareWith)) {
                sbuf.append("\nFor element " + i + ", expected = " +
                        compareWith + ", actual = " + val + " . ");
            }
        }
        if (sbuf.length() > 0) {
            fail(ASSERTION_FAILED,
                 "Expected and observed do not match!!" + sbuf.toString());
        }
    }
}
