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

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.SetCollections;
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
        try {
            int i, n;
            FirstSetOfTestValuesForCollection firstValue = new FirstSetOfTestValuesForCollection();
            SecondSetOfTestValuesForCollection secondValue = new SecondSetOfTestValuesForCollection();

            int ret = 0;
            // turn on datastore transactions
            tx.setOptimistic(false);
            tx.begin();
            SetCollections pi = new SetCollections();
            pi.identifier = 1;
            pm.makePersistent(pi);
            Object oid = pm.getObjectId(pi);
            n = pi.getLength();
            // Provide initial set of values
            for(i = 0; i < n; ++i){
                String valueType = TestUtil.getFieldSpecs(SetCollections.fieldSpecs[i]);
                pi.set( i, new HashSet((Collection)firstValue.get(valueType)));
            }
            tx.commit();
            // cache will be flushed
            pi = null;
            System.gc();

            tx.begin();

            checkValues(oid, firstValue); // check if persistent fields have values set
            pi = (SetCollections) pm.getObjectById(oid, true);

            // Provide new set of values
            for( i = 0; i < n; ++i){
                String valueType = TestUtil.getFieldSpecs(SetCollections.fieldSpecs[i]);
                pi.set( i, new HashSet((Collection)secondValue.get(valueType)));
            }
            tx.commit();
            // cache will be flushed
            pi = null;
            System.gc();

            tx.begin();
            // check new values
            checkValues(oid, secondValue);
            pi = (SetCollections) pm.getObjectById(oid, true);
            pm.deletePersistent(pi);
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    private void checkValues(Object oid, Hashtable startValue)
    {
        int i;

        SetCollections pi = (SetCollections) pm.getObjectById(oid, true);
        int n = pi.getLength();
        for( i = 0; i < n; ++i){
            String valueType = TestUtil.getFieldSpecs(SetCollections.fieldSpecs[i]);
            Set compareWith = new HashSet((Collection)startValue.get(valueType));

            Set val = pi.get(i);

            if(!val.equals(compareWith)){
                fail(ASSERTION_FAILED,
                     "Incorrect value for " + SetCollections.fieldSpecs[i]);
            }
        }
    }

}
