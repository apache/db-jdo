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
import java.util.List;
import java.util.Vector;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.CollectionCollections;
import org.apache.jdo.tck.pc.fieldtypes.SimpleClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Support of field type Collection.
 *<BR>
 *<B>Keywords:</B> model
 *<BR>
 *<B>Assertion ID:</B> A6.4.3-33.
 *<BR>
 *<B>Assertion Description: </B>
 JDO implementations must support fields of the interface type
 <code>java.util.Collection</code>, and may choose to support them
 as Second Class Objects or First Class Objects.
 */


public class TestCollectionCollections extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A6.4.3-33 (TestCollectionCollections) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(TestCollectionCollections.class);
    }   

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CollectionCollections.class);
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
        Transaction tx = pm.currentTransaction();
        CollectionCollections expectedValue = new CollectionCollections();

        // turn on datastore transactions
        tx.setOptimistic(false);
        tx.begin();
        CollectionCollections pi = new CollectionCollections();
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
        pi = (CollectionCollections) pm.getObjectById(oid, true);
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
    private void setValues(CollectionCollections colcol, int order)
    {
        Vector value;
        int n = colcol.getLength();
        for (int i = 0; i < n; ++i) {
            String valueType = TestUtil.getFieldSpecs(
                    CollectionCollections.fieldSpecs[i]);
            value = TestUtil.makeNewVectorInstance(valueType, order);
            colcol.set(i, value);
            if (debug)
                logger.debug("Set " + i + "th value to: " + value.toString());
        }
    }

    /** */
    private void checkValues(Object oid, CollectionCollections expectedValue)
    {
        StringBuffer sbuf = new StringBuffer();
        CollectionCollections pi = (CollectionCollections)
                pm.getObjectById(oid, true);
        int n = pi.getLength();
        for (int i = 0; i < n; ++i) {
            Collection expected = expectedValue.get(i);
            Collection actual = pi.get(i);
            if (actual.size() != expected.size()) {
                sbuf.append("\nFor element " + i + ", expected size = " +
                        expected.size() + ", actual size = " + actual.size()
                        + " . ");
            }
            else if (! expected.equals(actual)) {
                if (TestUtil.getFieldSpecs(CollectionCollections.fieldSpecs[i]
                            ).equals("BigDecimal")) {
            	    if (debug) {
                	logger.debug("Field is " + i + " Class name is "
                          +  actual.getClass().getName()
			              + "   isInstance of Vector is "
			              + actual.getClass().isInstance((Object)new Vector()));
		            }
                    List expectedL = (List)expected;
                    List actualL = (List)actual;
                    for (int j = 0; j < actualL.size(); ++j) {
                        BigDecimal bigDecCompareWith =
                            (BigDecimal)expectedL.get(j);
                        BigDecimal bigDecVal = (BigDecimal)actualL.get(j);
                        if ((bigDecCompareWith.compareTo(bigDecVal) != 0)) {
                            sbuf.append("\nFor element " + i + "(" + j +
                                    "), expected = " + expected +
                                    ", actual = " + actualL + " . ");
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
                 "Expected and observed do not match!!" + sbuf.toString());
        }
    }
}
