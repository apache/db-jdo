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

import java.util.Collection;
import java.util.Vector;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.ListCollections;
import org.apache.jdo.tck.transactions.Commit;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Support of field type List
 *<BR>
 *<B>Keywords:</B> model
 *<BR>
 *<B>Assertion ID:</B> A6.4.3-36.
 *<BR>
 *<B>Assertion Description: </B>
If the List optional feature is supported, then JDO implementations must
support fields of the interface type <code>List</code>,
supporting them as Second Class Objects or First Class Objects.
 */


public class TestListCollections extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A6.4.3-36 (Commit) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(Commit.class);
    }   

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(ListCollections.class);
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
        if (!isListSupported()) {
            if (debug)
                logger.debug("JDO Implementation does not support " +
                        "the optional feature List");
            return;
        }

        if (!isLinkedListSupported() && !isArrayListSupported()) {
            fail(ASSERTION_FAILED,
                     "JDO Implementation supports List, but neither " +
                     "ArrayList nor LinkedList.");
        }

        Transaction tx = pm.currentTransaction();
        ListCollections expectedValue = new ListCollections();
        
        // turn on datastore transactions
        tx.setOptimistic(false);
        tx.begin();
        ListCollections pi = new ListCollections();
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
        pi = (ListCollections) pm.getObjectById(oid, true);
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
    private void setValues(ListCollections collect, int order)
    {
        Vector value;
        int n = collect.getLength();
        for (int i = 0; i < n; ++i) {
            String valueType = TestUtil.getFieldSpecs(
                    ListCollections.fieldSpecs[i]);
            value = TestUtil.makeNewVectorInstance(valueType, order);
            collect.set( i, value);
            if (debug)
                logger.debug("Set " + i + "th value to: " + value.toString());
        }
    }

    /** */
    private void checkValues(Object oid, ListCollections expectedValue)
    {
        int i;
        StringBuffer sbuf = new StringBuffer();
        ListCollections pi = (ListCollections)
                pm.getObjectById(oid, true);
        int n = pi.getLength();
        for (i = 0; i < n; ++i) {
            Collection compareWith = expectedValue.get(i);
            Collection val = pi.get(i);
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
