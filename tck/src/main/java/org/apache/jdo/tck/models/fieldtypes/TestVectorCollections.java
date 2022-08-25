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
 
package org.apache.jdo.tck.models.fieldtypes;

import java.math.BigDecimal;

import java.util.Vector;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.VectorCollections;
import org.apache.jdo.tck.pc.fieldtypes.SimpleClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Support of field type Vector.
 *<BR>
 *<B>Keywords:</B> model
 *<BR>
 *<B>Assertion ID:</B> A6.4.3-29.
 *<BR>
 *<B>Assertion Description: </B>
If the Vector optional feature is supported, then JDO implementation
must support fields of the mutable object class <code>Vector</code>,
supporting them as Second Class Objects or First Class Objects.
 */


public class TestVectorCollections extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A6.4.3-29 (TestVectorCollections) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(TestVectorCollections.class);
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(VectorCollections.class);
        addTearDownClass(SimpleClass.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
         
        runTest(pm);
        
        pm.close();
        pm = null;
    }
    
    /** */
    void runTest(PersistenceManager pm) 
    {
        if (!isVectorSupported()) {
            if (debug)
                logger.debug("JDO Implementation does not support " +
                        "the optional feature Vector");
            return;
        }
        
        Transaction tx = pm.currentTransaction();
        VectorCollections expectedValue = new VectorCollections();

        // turn on datastore transactions
        tx.setOptimistic(false);
        tx.begin();
        VectorCollections pi = new VectorCollections();
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
        // check if persistent fields have values set
        checkValues(oid, expectedValue);
        pi = (VectorCollections) pm.getObjectById(oid, true);
        // Provide new set of values
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
    private void setValues(VectorCollections collect, int order)
    {
        Vector<?> value;
        int n = collect.getLength();
        for (int i = 0; i < n; ++i) {
            String valueType = TestUtil.getFieldSpecs(
                    VectorCollections.fieldSpecs[i]);
            value = TestUtil.makeNewVectorInstance(valueType, order);
            collect.set(i, value);
            if (debug)
                logger.debug("Set " + i + "th value to: " + value.toString());
        }
    }

    /** */
    private void checkValues(Object oid, VectorCollections expectedValue)
    {
        StringBuilder sbuf = new StringBuilder();
        VectorCollections pi = (VectorCollections)
                pm.getObjectById(oid, true);
        int n = pi.getLength();
        for (int i = 0; i < n; i++) {
            Vector<?> expected = expectedValue.get(i);
            Vector<?> actual = pi.get(i);
            if (actual.size() != expected.size()) {
                sbuf.append("\nFor element " + i + ", expected size = " +
                        expected.size() + ", actual size = " + actual.size()
                        + " . ");
            }
            else if (! expected.equals(actual)) {
                if (debug) {
                    logger.debug("Field number is " + i);
                    logger.debug("Persisted vector is " + actual);
                    logger.debug("Expected vector is " + expected);
                }
                if (TestUtil.getFieldSpecs(VectorCollections.fieldSpecs[i]
                            ).equals("BigDecimal")) {
                    for (int j = 0; j < actual.size(); j++) {
                        BigDecimal bigDecActual =
                                (BigDecimal)actual.elementAt(j);
                        BigDecimal bigDecExpected =
                            (BigDecimal)expected.elementAt(j);
                        if ((bigDecExpected.compareTo(bigDecActual) != 0)) {
                            sbuf.append("\nFor element " + i + "(" + j +
                                    "), expected = " + expected +
                                    ", actual = " + actual + " . ");
                        }
                    }
                }
                else {
                    sbuf.append("\nFor element " + i + ", expected = " +
                        expected + ", actual = " + actual + " . ");
                }
            }

        }
        if (sbuf.length() > 0) {
            fail(ASSERTION_FAILED,
                 "Expected and observed do not match!!" + sbuf);
        }
    }
}
