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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.FieldsOfPrimitiveint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Support of field type int.
 *<BR>
 *<B>Keywords:</B> model
 *<BR>
 *<B>Assertion ID:</B> A6.4.3-4.
 *<BR>
 *<B>Assertion Description: </B>
JDO implementations must support fields of the primitive type <code>int</code>.
 */


public class TestFieldsOfPrimitiveint extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A6.4.3-4 (TestFieldsOfPrimitiveint) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(TestFieldsOfPrimitiveint.class);
    }   

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(FieldsOfPrimitiveint.class);
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
        int i, n, value;
        tx.begin();
        FieldsOfPrimitiveint pi = new FieldsOfPrimitiveint();
        pi.identifier = 1;
        pm.makePersistent(pi);
        Object oid = pm.getObjectId(pi);
        n = pi.getLength();
        // Provide initial set of values
        for( i = 0, value = 0; i < n; ++i, ++value ){
            pi.set( i, value);            
        }
        tx.commit();
        // cache will be flushed        
        pi = null;
        System.gc();
    
        tx.begin();
        pi = (FieldsOfPrimitiveint) pm.getObjectById(oid, true);
        checkValues(oid, 0); // check if persistent fields have values set

        // Provide new set of values 
        for( i = 0, value = 1000; i < n; ++i, ++value ){
            pi.set(i, value);
        }
        tx.commit();
        // cache will be flushed        
        pi = null;
        System.gc();
    
        tx.begin();
        // check new values
        checkValues(oid, 1000);
        tx.commit();
    }
    
    /** */
    private void checkValues(Object oid, int startValue){
        int i, value;
        FieldsOfPrimitiveint pi = (FieldsOfPrimitiveint)
            pm.getObjectById(oid, true);
        int n = pi.getLength();
        for( i = 0, value = startValue; i < n; ++i, ++value){
            if( !FieldsOfPrimitiveint.isPersistent[i] ) continue;
            int val = pi.get(i);
            if( val != value ){
                fail(ASSERTION_FAILED,
                        "Incorrect value for " +
                        FieldsOfPrimitiveint.fieldSpecs[i] +
                        ", expected value " + startValue +
                        ", value is " + val);
            }
        }
    }
}
