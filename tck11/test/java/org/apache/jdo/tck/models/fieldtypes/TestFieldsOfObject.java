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
import org.apache.jdo.tck.pc.fieldtypes.FieldsOfObject;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Support of field type Object.
 *<BR>
 *<B>Keywords:</B> model
 *<BR>
 *<B>Assertion ID:</B> A6.4.3-31.
 *<BR>
 *<B>Assertion Description: </B>
JDO implementations must support fields of <code>Object</code> class type
as First Class Objects. The implementation is permitted, but is not required,
to allow any class to be assigned to the field. If an implementation restricts
instances to be assigned to the field, a <code>ClassCastException</code>
must be thrown at the time of any incorrect assignment.
 */


public class TestFieldsOfObject extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A6.4.3-31 (TestFieldsOfObject) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(TestFieldsOfObject.class);
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
            Object firstValue = new String("Hello");
            Object secondValue = new Integer("420");
            tx.begin();
            FieldsOfObject pi = new FieldsOfObject();
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
            pi = (FieldsOfObject) pm.getObjectById(oid, true);
            checkValues(oid, firstValue); // check if persistent fields have values set

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
            pi = (FieldsOfObject) pm.getObjectById(oid, true);
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
    private void checkValues(Object oid, Object startValue){
        int i;
        FieldsOfObject pi = (FieldsOfObject) pm.getObjectById(oid, true);
        int n = pi.getLength();
        for( i = 0; i < n; ++i){
            if( !FieldsOfObject.isPersistent[i] ) continue;
            Object val = pi.get(i);
            if(!val.equals(startValue) ){
                fail(ASSERTION_FAILED,
                        "Incorrect value for " + FieldsOfObject.fieldSpecs[i] +
                        ", expected value " + startValue.toString() +
                        ", value is " + val.toString());
            }
        }
    }
}
