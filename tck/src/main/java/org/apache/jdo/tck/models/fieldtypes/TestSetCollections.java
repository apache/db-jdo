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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.SetCollections;
import org.apache.jdo.tck.pc.fieldtypes.SimpleClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Support of field type Set.
 *<BR>
 *<B>Keywords:</B> model
 *<BR>
 *<B>Assertion ID:</B> A6.4.3-34.
 *<BR>
 *<B>Assertion Description: </B>
JDO implementations must support fields of the interface type
<code>java.util.Set</code>, and may choose to support them
as Second Class Objects or First Class Objects.
 */

public class TestSetCollections extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A6.4.3-34 (TestSetCollections) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(TestSetCollections.class);
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(SetCollections.class);
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
    void runTest(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        SetCollections expectedValue = new SetCollections();

        // turn on datastore transactions
        tx.setOptimistic(false);
        tx.begin();
        SetCollections pi = new SetCollections();
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
        pi = (SetCollections) pm.getObjectById(oid, true);
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
    private void setValues(SetCollections collect, int order)
    {
        Collection<?> value;
        int n = collect.getLength();
        for (int i = 0; i < n; ++i) {
            String valueType = TestUtil.getFieldSpecs(
                    SetCollections.fieldSpecs[i]);
            value = TestUtil.makeNewVectorInstance(
                    valueType, order);
            Set<?> lvalue = new HashSet<>(value);
            collect.set(i, lvalue);
            if (debug)
                logger.debug("Set " + i + "th value to: " + value);
        }
    }

    /** */
    private void checkValues(Object oid, SetCollections expectedValue)
    {
        int i;
        StringBuffer sbuf = new StringBuffer();
        SetCollections pi = (SetCollections)
                pm.getObjectById(oid, true);
        int n = pi.getLength();
        for (i = 0; i < n; ++i) {
            Set<?> expected = expectedValue.get(i);
            Set<?> actual = pi.get(i);
            if (actual.size() != expected.size()) {
                sbuf.append("\nFor element " + i + ", expected size = " +
                        expected.size() + ", actual size = " + actual.size()
                        + " . ");
            }
            else if (! expected.equals(actual)) {
                if (TestUtil.getFieldSpecs(SetCollections.fieldSpecs[i]
                            ).equals("BigDecimal")) {
                    // sort values for comparison
                    TreeSet<?> expectedTS = new TreeSet<>(expected);
                    TreeSet<?> actualTS = new TreeSet<>(actual);
                    Iterator<?> expectedIter = expectedTS.iterator();
                    Iterator<?> actualIter = actualTS.iterator();
                    for (int j=0; j < expectedTS.size(); j++) {
                        BigDecimal bigDecExpected =
                                (BigDecimal)(expectedIter.next());
                        BigDecimal bigDecActual =
                                (BigDecimal)(actualIter.next());
                                bigDecActual.setScale(bigDecExpected.scale());
                        if ((bigDecExpected.compareTo(bigDecActual)) != 0)  {
                            sbuf.append("\nFor element " + i + "(" + j +
                                    "), expected = " + bigDecExpected +
                                    ", actual = " + bigDecActual);
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
