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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.FieldsOfPrimitivelong;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Support of field type long.
 *<BR>
 *<B>Keywords:</B> model
 *<BR>
 *<B>Assertion ID:</B> A6.4.3-5.
 *<BR>
 *<B>Assertion Description: </B>
JDO implementations must support fields of the primitive type <code>long</code>.
 */


public class TestFieldsOfPrimitivelong extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A6.4.3-5 (TestFieldsOfPrimitivelong) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(TestFieldsOfPrimitivelong.class);
    }   

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(FieldsOfPrimitivelong.class);
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
        try { 
            int i, n;
            long value;
            tx.begin();
            FieldsOfPrimitivelong pi = new FieldsOfPrimitivelong();
            pi.identifier = 1;
            pm.makePersistent(pi);
            Object oid = pm.getObjectId(pi);
            n = pi.getLength();
            // Provide initial set of values
            for( i = 0, value = 10; i < n; ++i){
                pi.set( i, value);
            }
            tx.commit();
            // cache will be flushed
            pi = null;
            System.gc();

            tx.begin();

            pi = (FieldsOfPrimitivelong) pm.getObjectById(oid, true);
            checkValues(oid, 10); // check if persistent fields have values set

            // Provide new set of values
            for( i = 0, value = 67000; i < n; ++i){
                pi.set(i, value);
            }
            tx.commit();
            // cache will be flushed
            pi = null;
            System.gc();

            tx.begin();
            // check new values
            checkValues(oid, 67000);
            pi = (FieldsOfPrimitivelong) pm.getObjectById(oid, true);
            pm.deletePersistent(pi);
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void checkValues(Object oid, long startValue){
        int i;
        long value;
        FieldsOfPrimitivelong pi = (FieldsOfPrimitivelong) pm.getObjectById(oid, true);
        int n = pi.getLength();
        for( i = 0; i < n; ++i){
            if( !FieldsOfPrimitivelong.isPersistent[i] ) continue;
            long val = pi.get(i);
            if( val != startValue ){
                fail(ASSERTION_FAILED,
                        "Incorrect value for " + FieldsOfPrimitivelong.fieldSpecs[i] +
                        ", expected value " + startValue +
                        ", value is " + val);
            }
        }
    }
}
