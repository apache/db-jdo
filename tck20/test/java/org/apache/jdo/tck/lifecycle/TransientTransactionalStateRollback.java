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
 
package org.apache.jdo.tck.lifecycle;

import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.lifecycle.StateTransitionObj;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Test Transient Transactional Rollback
 *<BR>
 *<B>Keywords:</B> lifecycle transienttransactional rollback
 *<BR>
 *<B>Assertion IDs:</B> A5.1-4
 *<B>Assertion Description: </B>
 If <code>TransientTransactional</code> is supported, a transient transactional instance
 will have its state restored to its state prior to becoming transactional when
 its associated transaction aborts or on rollback.
 */

public class TransientTransactionalStateRollback extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A5.1-4 (TransientTransactionalStateRollback) failed: ";
    
    private static final int CLEAN_VALUE = 12;
    private static final int DIRTY_VALUE = 123;
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(TransientTransactionalStateRollback.class);
    }
    
    /** */
    public void test() {
        if (!isTransientTransactionalSupported()) {
            logger.debug("Transient transactional instances are not supported");
        }
        else {
            pm = getPM();
            
            // Get transient instance and read field value
            StateTransitionObj obj = getTransientInstance();

            int beforeValue=obj.readField();

            // Start transaction
            Transaction tx = pm.currentTransaction();
            tx.begin();

            // Get transient dirty instance
            makeTransientDirty(obj);

            // Rollback
            tx.rollback();
            int curr = currentState(obj);
            if( curr != TRANSIENT_CLEAN ){
                fail(ASSERTION_FAILED,
                     "StateTransition: Unable to create transient-clean instance " +
                     "from a transient-dirty instance via tx.rollback(), state is " + states[curr]);
            }

            // Check that field value has been rolled back
            int afterValue=obj.readField();
            if (beforeValue!=afterValue)
            {
                fail(ASSERTION_FAILED,
                     "Field value incorrect after rollback. Expected: "+beforeValue+" Found: "+afterValue);
            }
        }
    }

    protected StateTransitionObj getTransientInstance() {
        StateTransitionObj obj = new StateTransitionObj(CLEAN_VALUE);
        int curr = currentState(obj);
        if( curr != TRANSIENT ){
            fail(ASSERTION_FAILED,
                 "Unable to create transient instance, state is " + states[curr]);
        }
        return obj;
    }

    protected void makeTransientClean(StateTransitionObj obj) {
        if( obj == null ) return;
        pm.makeTransactional(obj);
        int curr = currentState(obj);
        if( curr != TRANSIENT_CLEAN ){
            fail(ASSERTION_FAILED,
                 "Unable to create transient-clean instance " +
                 "from a transient instance via makeTransactional(), state is " + states[curr]);
        }
    }

    protected void makeTransientDirty(StateTransitionObj obj) {
        if( obj == null ) return;
        makeTransientClean(obj);
        obj.writeField(DIRTY_VALUE);
        int curr = currentState(obj);
        if( curr != TRANSIENT_DIRTY ){
            fail(ASSERTION_FAILED,
                 "Unable to create transient-dirty instance " +
                 "from a transient-clean instance via modifying a field, state is " + states[curr]);
        }
    }
}
