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
 

package org.apache.jdo.tck.api.instancecallbacks;

import javax.jdo.JDOException;
import javax.jdo.JDOUserException;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.instancecallbacks.InstanceCallbackClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Calling JDO Predelete
 *<BR>
 *<B>Keywords:</B> instancecallbacks
 *<BR>
 *<B>Assertion ID:</B> A10.4-1.
 *<BR>
 *<B>Assertion Description: </B>
<code>jdoPreDelete()</code> is called during the execution of
<code>deletePersistent</code> before the state transition to persistent-deleted
or persistent-new-deleted.

 */

/*
 *    Use the same PersistenceCapable class and objects as in the test AccessingFieldsInPredelete (but the
 *      referenced object and Set need not be created).
 *
 *    Delete one such new persistent, one persistent clean and one persistent dirty object.
 *
 *    For each of these objects:
 *       determine jdoPredelete is called after deletePersistent is called
 *       determine jdoPredelete is called before statements after deletePersistent is called.
 *       determine that in jdoPredelete, the state of the object is persistent-new, persistent-clean, or persistent-dirty
 *         (appropriately).
 *       determine that the state of the object after return from deletePersistent is persistent-deleted or
 *         persitent-new-deleted (appropriately).
 *
 */
public class CallingJdoPredelete extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
         "Assertion A10.4-1 (CallingJdoPredelete) failed";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CallingJdoPredelete.class);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(InstanceCallbackClass.class);
    }
    
    /** */
    public void test() throws Exception
    {
        pm = getPM();
        Transaction t = pm.currentTransaction();
        
        InstanceCallbackClass.initializeStaticsForTest();

        InstanceCallbackClass.performPreDeleteTests = true; // enabling PreDelete tests
        t.begin();
        InstanceCallbackClass a = new InstanceCallbackClass(null, null, 1, 1.0, (short)-1, '1', null);
        InstanceCallbackClass b = new InstanceCallbackClass(null, null, 2, 2.0, (short)-1, '2', null);
        pm.makePersistent(a);
        pm.makePersistent(b);
        Object aId  = pm.getObjectId(a);
        Object bId  = pm.getObjectId(b);
        t.commit();
        
        t.begin();
        try {
            a = (InstanceCallbackClass)pm.getObjectById(aId, true);
        } catch (JDOUserException e) {
            fail(ASSERTION_FAILED, "CallingJdoPredelete: Failed to find object a created in previous transaction, got JDOUserException " + e);
            return;
        }
        try {
            b = (InstanceCallbackClass)pm.getObjectById(bId, true);
        } catch (JDOException e) {
            fail(ASSERTION_FAILED, "CallingJdoPredelete: Failed to find object b created in previous transaction, got JDOUserException " + e);
            return;
        }
        
        int getfld = a.intValue;    // cause instance to be persistent-clean  
        int getfld2 = a.childToDelete;
        b.childToDelete++;          // make second object dirty
        InstanceCallbackClass c = new InstanceCallbackClass(null, null, 3, 3.0, (short)-1, '3', null);
        pm.makePersistent(c);       // create persistent-new object
        
        InstanceCallbackClass.preDeleteCalled = false;
        pm.deletePersistent(a);
        if(!InstanceCallbackClass.preDeleteCalled) {
            fail(ASSERTION_FAILED, "jdoPreDelete not called when PERSISTENT_CLEAN object deleted.");
        } else {
            if(InstanceCallbackClass.objectState == PERSISTENT_DELETED) {
                fail(ASSERTION_FAILED, "Error, state was persistent-deleted in jdoPredelete callback");
            }
        }
        
        InstanceCallbackClass.preDeleteCalled = false;
        pm.deletePersistent(b);
        if(!InstanceCallbackClass.preDeleteCalled) {
            fail(ASSERTION_FAILED, "jdoPreDelete not called when PERSISTENT_DIRTY object deleted.");
        } else {
            if(InstanceCallbackClass.objectState == PERSISTENT_DELETED) {
                fail(ASSERTION_FAILED, "Error, state was persistent-deleted in jdoPredelete callback");
            }
        }
        
        InstanceCallbackClass.preDeleteCalled = false;
        pm.deletePersistent(c);
        if(!InstanceCallbackClass.preDeleteCalled) {
            fail(ASSERTION_FAILED,"jdoPreDelete not called when PERSISTENT_NEW object deleted.");
        } else {
            if(InstanceCallbackClass.objectState == PERSISTENT_NEW_DELETED) {
                fail(ASSERTION_FAILED, "Error, state was persistent-new-deleted in jdoPredelete callback");
            }
        }
        t.rollback();
        pm.close();
        pm = null;
    }
}
