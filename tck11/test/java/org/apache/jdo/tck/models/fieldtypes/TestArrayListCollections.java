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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.ArrayListCollections;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Support of field type ArrayList.
 *<BR>
 *<B>Keywords:</B> model
 *<BR>
 *<B>Assertion ID:</B> A6.4.3-23.
 *<BR>
 *<B>Assertion Description: </B>
 If the ArrayList optional feature is supported, then JDO implementation must
 support fields of the mutable object class <code>ArrayList</code>,
 supporting them as Second Class Objects or First Class Objects.
 */

public class TestArrayListCollections extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A6.4.3-23 (TestArrayListCollections) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(TestArrayListCollections.class);
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
        if (!isArrayListSupported()) {
            if (debug)
                logger.debug("JDO Implementation does not support optional feature ArrayList");
            return;
        }
        
        Transaction tx = pm.currentTransaction();
        try {
            int i, n;
            FirstSetOfTestValuesForCollection firstValue =
                new FirstSetOfTestValuesForCollection();
            SecondSetOfTestValuesForCollection secondValue =
                new SecondSetOfTestValuesForCollection();

            // turn on datastore transactions
            tx.setOptimistic(false);
            tx.begin();
            ArrayListCollections pi = new ArrayListCollections();
            pi.identifier = 1;
            pm.makePersistent(pi);
            Object oid = pm.getObjectId(pi);
            n = pi.getLength();
            // Provide initial set of values
            for(i = 0; i < n; ++i){
                String valueType = TestUtil.getFieldSpecs(ArrayListCollections.fieldSpecs[i]);
                pi.set( i, new ArrayList((Collection)firstValue.get(valueType)));
            }
            tx.commit();
            // cache will be flushed
            pi = null;
            System.gc();
            
            tx.begin();

            checkValues(oid, firstValue); // check if persistent fields have values set
            pi = (ArrayListCollections) pm.getObjectById(oid, true);

            // Provide new set of values
            for( i = 0; i < n; ++i){
                String valueType = TestUtil.getFieldSpecs(ArrayListCollections.fieldSpecs[i]);
                pi.set( i, new ArrayList((Collection)secondValue.get(valueType)));
            }
            tx.commit();
            // cache will be flushed
            pi = null;
            System.gc();
            
            tx.begin();
            // check new values
            checkValues(oid, secondValue);
            pi = (ArrayListCollections) pm.getObjectById(oid, true);
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
    private void checkValues(Object oid, Hashtable startValue)
    {
        int i;
        ArrayListCollections pi = (ArrayListCollections) pm.getObjectById(oid, true);
        int n = pi.getLength();
        for( i = 0; i < n; ++i){
            String valueType = TestUtil.getFieldSpecs(ArrayListCollections.fieldSpecs[i]);
            ArrayList compareWith = new ArrayList((Collection)startValue.get(valueType));
            
            ArrayList val = pi.get(i);
            
            if(!val.equals(compareWith)){
                fail(ASSERTION_FAILED,
                     "Incorrect value for " + ArrayListCollections.fieldSpecs[i]);
            }
        }
    }
}
