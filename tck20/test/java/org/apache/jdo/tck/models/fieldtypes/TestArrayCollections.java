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
import java.util.Hashtable;
import java.util.Vector;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.fieldtypes.ArrayCollections;
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
                logger.debug("JDO Implementation does not support optional feature Array");
            return;
        }

        Transaction tx = pm.currentTransaction();
        try {
            int i, n;
            FirstSetOfTestValuesForCollection firstSetOfValues =
                new FirstSetOfTestValuesForCollection();
            SecondSetOfTestValuesForCollection secondSetOfValues =
                new SecondSetOfTestValuesForCollection();

            // turn on datastore transactions
            tx.setOptimistic(false);
            tx.begin();
            ArrayCollections pi = new ArrayCollections();
            pi.identifier = 1;
            pm.makePersistent(pi);
            Object oid = pm.getObjectId(pi);
            n = pi.getLength();
            // Provide initial set of values
            for(i = 0; i < n; ++i){
                String valueType = TestUtil.getFieldSpecs(
                    ArrayCollections.fieldSpecs[i]);
                //create an array of initial values for each value type
                Object[] firstValueArray = null;
                Vector firstValueVector =  (Vector)firstSetOfValues.get(valueType);
                firstValueArray = (Object[])java.lang.reflect.Array.newInstance(firstValueVector.get(0).getClass(),
                                                                                firstValueVector.size());
                for (int j=0; j<firstValueVector.size(); j++) {
                    firstValueArray[j] = firstValueVector.get(j);
                }

                //set the initial set of values
                pi.set( i, firstValueArray);
            }
            tx.commit();
            // cache will be flushed
            pi = null;
            System.gc();

            tx.begin();

            checkValues(oid, firstSetOfValues); // check if persistent fields have values set
            pi = (ArrayCollections) pm.getObjectById(oid, true);

            // Provide new set of values
            for( i = 0; i < n; ++i){
                String valueType = TestUtil.getFieldSpecs(ArrayCollections.fieldSpecs[i]);
                //create an array of second set of values for each value type
                Object[] secondValueArray = null;
                Vector secondValueVector =  (Vector)secondSetOfValues.get(valueType);
                secondValueArray = (Object[])java.lang.reflect.Array.newInstance(secondValueVector.get(0).getClass(),
                                                                                 secondValueVector.size());
                for (int j=0; j<secondValueVector.size(); j++) {
                    secondValueArray[j] = secondValueVector.get(j);
                }
                pi.set( i, secondValueArray);
            }
            tx.commit();
            // cache will be flushed
            pi = null;
            System.gc();

            tx.begin();
            // check new values
            checkValues(oid, secondSetOfValues);
            pi = (ArrayCollections) pm.getObjectById(oid, true);
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
        ArrayCollections pi = (ArrayCollections) pm.getObjectById(oid, true);
        int n = pi.getLength();
        for (i = 0; i < n; ++i) {
            String valueType = TestUtil.getFieldSpecs(ArrayCollections.fieldSpecs[i]);
            //build the compareWith array
            Object[] compareWith = null;
            Vector compareWithVector =  (Vector)startValue.get(valueType);
            compareWith = (Object[])java.lang.reflect.Array.newInstance(compareWithVector.get(0).getClass(),
                                                                        compareWithVector.size());
            for (int j=0; j<compareWithVector.size(); j++) {
                compareWith[j] = compareWithVector.get(j);
            }

            Object[] val = pi.get(i);

            if(!Arrays.equals(val, compareWith)){
                if (debug) {
                    String message1 = compareWith==null?"compareWith was null!!!":
                        compareWith.toString();
                    String message2 = val==null?"val was null!!!":
                        val.toString();
                    logger.debug("compareWith: " + message1);
                    logger.debug("val: "+ message2);
                }
                fail(ASSERTION_FAILED,
                     "Incorrect value for " + ArrayCollections.fieldSpecs[i]);
            }
        }
    }
}
