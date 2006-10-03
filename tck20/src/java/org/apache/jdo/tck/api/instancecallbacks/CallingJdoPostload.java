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
 

package org.apache.jdo.tck.api.instancecallbacks;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOUserException;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.instancecallbacks.InstanceCallbackNonPersistFdsClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Calling InstanceCallbacks Postload Method
 *<BR>
 *<B>Keywords:</B> instancecallbacks
 *<BR>
 *<B>Assertion ID:</B> A10.1-1.
 *<BR>
 *<B>Assertion Description: </B>
The <code>jdoPostLoad()</code> method of a class implementing
<code>InstanceCallbacks</code> is called after the default fetch
group values are loaded from the <code>StateManager</code> into the instance.

 */


/*
 *  Create a persistent object and commit().
 *
 *  Retrieve object in the next transaction and verify its state is hollow.
 *
 *  Reference a persistent attribute and verify:
 *     the call to jdoPostLoad() occurred just before this reference--and after the object was retrieved (via getObjectById()),
 *     jdoPostLoad() was called exactly once and
 *     once the attribute has been referenced, the state of the object is persistent-clean
 *
 *  Verify that the values of the persistent attributes available in jdoPostLoad() are the values actually in the object (the
 *    values have already been restored be the time jdoPostLoad() is called).
 */
public class CallingJdoPostload extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A10.1-1 (CallingJdoPostload) failed";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CallingJdoPostload.class);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(InstanceCallbackNonPersistFdsClass.class);
    }
    
    /** */
    public void test() throws Exception
    {
        pm = getPM();
        Transaction t = pm.currentTransaction();
        t.setRetainValues(false);

        InstanceCallbackNonPersistFdsClass.initializeStaticsForTest();

        t.begin();
        // create instance        
        InstanceCallbackNonPersistFdsClass obj1 = new InstanceCallbackNonPersistFdsClass(2.2f, 13);
        pm.makePersistent(obj1);
        Object objPtr1 = pm.getObjectId (obj1);
        obj1.setNonPersist(1, (char)2, 3.3, (short)4);
        t.commit(); 

        t.begin();
        // set applicationStep before and after getObjectById() to be sure when jdoPostLoad() gets called.
        InstanceCallbackNonPersistFdsClass.applicationStep = InstanceCallbackNonPersistFdsClass.beforeGetObjectById;
        try {
            obj1 = (InstanceCallbackNonPersistFdsClass)pm.getObjectById(objPtr1, true);  // jdoPreLoad() called
        } catch (JDOUserException e) {
            // could not locate persistent object created in previous transaction
            fail(ASSERTION_FAILED, "CallingJdoPostload:  Could not locate persistent object obj1 created in previous transaction " + e);
            return;
        } catch (JDODataStoreException e) {
            // could not locate persistent object created in previous transaction
            fail(ASSERTION_FAILED, "CallingJdoPostload:  Could not locate persistent object obj1 created in previous transaction " + e);
            return;
        }
        
        int objectState = currentState(obj1);        
        // expect state to be hollow or persistent-clean
        if(objectState != HOLLOW && objectState != PERSISTENT_CLEAN) {
            fail(ASSERTION_FAILED, "State of object was not hollow or persistent clean after accessed via getObjectById((), state is " + states[objectState]);
        }
        
        InstanceCallbackNonPersistFdsClass.applicationStep = InstanceCallbackNonPersistFdsClass.afterGetObjectById;
        int getIntValue = obj1.intValue;  // reference attribute in default fetch group        
        objectState = currentState(obj1);
        
        // expect jdoPostLoad() was called
        if(!InstanceCallbackNonPersistFdsClass.postloadCalled) {
            fail(ASSERTION_FAILED, "jdoPostLoad() never called");
        }
        
        // only check other information if jdoPostLoad() called
        if(InstanceCallbackNonPersistFdsClass.postloadCalled) {
            // expect jdoPostLoad() was called only once
            if(InstanceCallbackNonPersistFdsClass.postloadCalledMultipleTimes) {
                fail(ASSERTION_FAILED, "jdoPostLoad was called more than once for the same loaded object");
            }

            // verify the value of intValue was available in jdoPostLoad()
            if(InstanceCallbackNonPersistFdsClass.savedIntValue != getIntValue) {
                fail(ASSERTION_FAILED, 
                     "intValue available in jdoPostLoad is incorrect.  It was " + 
                     InstanceCallbackNonPersistFdsClass.savedIntValue +
                     ", it should be " + getIntValue);
            }

            // verify the value of floatValue was available in jdoPostLoad()
            if(InstanceCallbackNonPersistFdsClass.savedFloatValue != obj1.floatValue) {
                fail(ASSERTION_FAILED, 
                     "floatValue available in jdoPostLoad is incorrect.  It was " + 
                     InstanceCallbackNonPersistFdsClass.savedFloatValue +
                     ", it should be " + obj1.floatValue);
            }
        }
        
        t.rollback();
        pm.close(); 
        pm = null;
    }
}
