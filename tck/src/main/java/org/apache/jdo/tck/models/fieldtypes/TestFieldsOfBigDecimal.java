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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.FieldsOfBigDecimal;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Support of field type BigDecimal.
 *<BR>
 *<B>Keywords:</B> model
 *<BR>
 *<B>Assertion ID:</B> A6.4.3-19.
 *<BR>
 *<B>Assertion Description: </B>
 JDO implementations must support fields of the immutable object class
 <code>java.math.BigDecimal</code>, and may choose to support them as
 Second Class Objects or First Class Objects.
 */

public class TestFieldsOfBigDecimal extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A6.4.3-19 (TestFieldsOfBigDecimal) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(TestFieldsOfBigDecimal.class);
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(FieldsOfBigDecimal.class);
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
    	int i, n;
        BigDecimal firstValue = new BigDecimal("20079.0237");
        BigDecimal secondValue = new BigDecimal("8907489.658");
        tx.begin();
        FieldsOfBigDecimal pi = new FieldsOfBigDecimal();
        pi.identifier = 1;
        pm.makePersistent(pi);
        Object oid = pm.getObjectId(pi);
        n = pi.getLength();
        // Provide initial set of values
        for( i = 0; i < n; ++i){
            pi.set( i, firstValue);
        }
        tx.commit();
        // cache will be flushed
        pi = null;
        System.gc();

        tx.begin();

        pi = (FieldsOfBigDecimal) pm.getObjectById(oid, true);
        checkValues(oid, firstValue);

        // Provide new set of values
        for( i = 0; i < n; ++i){
            pi.set(i, secondValue);
        }
        tx.commit();
        // cache will be flushed
        pi = null;
        System.gc();

        tx.begin();
        // check new values
        checkValues(oid, secondValue);
        tx.commit();
    }

    /** */
    private void checkValues(Object oid, BigDecimal startValue){
        int i;
        BigDecimal value;
        FieldsOfBigDecimal pi = (FieldsOfBigDecimal) pm.getObjectById(oid, true);
        int n = pi.getLength();
        for( i = 0, value = startValue; i < n; ++i){
            if( !FieldsOfBigDecimal.isPersistent[i] ) continue;
            BigDecimal val = pi.get(i);
            if(val.compareTo(value) != 0){
                fail(ASSERTION_FAILED,
                     "Incorrect value for " + FieldsOfBigDecimal.fieldSpecs[i] +
                     ", expected value " + value.toString() +
                     ", value is " + val.toString());
            }
        }
    }
}
