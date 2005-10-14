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

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.ArrayCollections;
import org.apache.jdo.tck.pc.fieldtypes.SimpleClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Support of field type array.
 *<BR>
 *<B>Keywords:</B> model
 *<BR>
 *<B>Assertion ID:</B> A6.4.3-39.
 *<BR>
 *<B>Assertion Description: </B>
JDO implementations may optionally support fields of array types.
 */

public class TestArrayCollections extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion (TestArrayCollections) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(TestArrayCollections.class);
    }   

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(ArrayCollections.class);
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
        if (!isArraySupported()) {
            if (debug)
                logger.debug("JDO Implementation does not support" +
                        "optional feature Array");
            return;
        }

        Transaction tx = pm.currentTransaction();
        ArrayCollections expectedValue = new ArrayCollections();

            // turn on datastore transactions
            tx.setOptimistic(false);
            tx.begin();
            ArrayCollections pi = new ArrayCollections();
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
            pi = (ArrayCollections) pm.getObjectById(oid, true);

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
    private void setValues(ArrayCollections collect, int order)
    {
        Vector value;
        int n = collect.getLength();
        for (int i = 0; i < n; ++i) {
            String valueType = TestUtil.getFieldSpecs(
                    ArrayCollections.fieldSpecs[i]);
            Object[] valueArray = null;
            value = TestUtil.makeNewVectorInstance(valueType, order);
            value.copyInto(valueArray);
            collect.set(i, valueArray);
            if (debug)
                logger.debug("Set " + i + "th value to: "
                        + valueArray.toString());
        }
    }

    /** */
    private void checkValues(Object oid, ArrayCollections expectedValue)
    {
        StringBuffer sbuf = new StringBuffer();
        ArrayCollections pi = (ArrayCollections) pm.getObjectById(oid, true);
        int n = pi.getLength();
        for (int i = 0; i < n; ++i) {
            Object [] compareWith = expectedValue.get(i);
            Object [] val = pi.get(i);
            if (val.length != compareWith.length) {
                sbuf.append("\nFor element " + i + ", expected size = " +
                        compareWith.length + ", actual size = " + val.length
                        + " . ");
                continue;
            }
            if (! Arrays.equals(val, compareWith)) {
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
