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

package org.apache.jdo.tck.lifecycle.nontransactional;

import java.util.Iterator;

import javax.jdo.PersistenceManager;
import javax.jdo.Extent;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.lifecycle.StateTransitionObj;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Modification of Nontransactional Persistent Instance Outside a Transaction
 *<BR>
 *<B>Keywords:</B> nontransactionalwrite
 *<BR>
 *<B>Assertion ID:</B> A5.6-9.
 *<BR>
 *<B>Assertion Description: </B>
 * With <code>NontransactionalWrite</code> set to <code>true</code>,
 * modification of nontransactional persistent instances is permitted outside
 * a transaction.  The changes do not participate in any subsequent transaction.
 */

public class ModificationOfNontransactionalInstanceOutsideTransaction extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A5.6-9 (ModificationOfNontransactionalInstanceOutsideTransaction) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ModificationOfNontransactionalInstanceOutsideTransaction.class);
    }
    
    /** 
     * The pc instance, set by method createAndModifyInstance. 
     */
    private StateTransitionObj object;

    /** 
     * The ObjectId of the pc instance, set by method
     * createAndModifyInstance. 
     */ 
    private Object oid;

    /**
     * The original value of the int field of the pc instance, 
     * set by method createAndModifyInstance. 
     */
    private int originalValue;

    /** */
    public void testDifferentPM() {
        if (!isNontransactionalWriteSupported()) {
            printUnsupportedOptionalFeatureNotTested(
                "NontransactionalWrite", "testDifferentPM");
        }
        else {
            pm = getPM();
            createAndModifyInstance();

            int value=object.readField();
            if (value!=999){
                fail(ASSERTION_FAILED,
                     "Unable to write value to field with setNontransactionalWrite==true");
            }

            // begin new transaction, read instance from datastore and 
            // verify that old value is still there
            PersistenceManager pm2 = null;
            try {
                pm2 = getPMF().getPersistenceManager();
                pm2.currentTransaction().begin();
                StateTransitionObj object2 =
                    (StateTransitionObj)pm2.getObjectById(oid,true);
                if (object2 == null){
                    fail(ASSERTION_FAILED, 
                         "Failed to read instance from datastore via pm.getObjectById(...)");
                }
                
                int value2 = object2.readField();
                if (value2 != originalValue){
                    fail(ASSERTION_FAILED, 
                         "Value has been changed with setNontransactionalWrite==true. " + 
                         "New value is " + value2 + " ... Old value was " + originalValue);
                }
                pm2.currentTransaction().commit();
            }
            finally {
                if ((pm2 != null) && !pm2.isClosed()) {
                    if (pm2.currentTransaction().isActive())
                        pm2.currentTransaction().rollback();
                    pm2.close();
                }
            }
        }
    }
    
    /** */
    public void testSameInstance() {
        if (!isNontransactionalWriteSupported()) {
            printUnsupportedOptionalFeatureNotTested(
                "NontransactionalWrite", "testDifferentPM");
        }
        else {
            pm = getPM();
            createAndModifyInstance();

            // use the object in a new datastore transaction
            pm.currentTransaction().setOptimistic(false);
            pm.currentTransaction().begin();
            int value = object.readField();
            if (value != originalValue){
                fail(ASSERTION_FAILED, 
                     "Value has been changed with setNontransactionalWrite==true. " + 
                     "New value is " + value + " ... Old value was " + originalValue);
            }
            pm.currentTransaction().commit();
        }
    }

    /** */
    public void testIterateExtent() {
        if (!isNontransactionalWriteSupported()) {
            printUnsupportedOptionalFeatureNotTested(
                "NontransactionalWrite", "testDifferentPM");
        }
        else {
            pm = getPM();
            createAndModifyInstance();

            // iterate the extent and use the object in a new datastore transaction
            pm.currentTransaction().setOptimistic(false);
            pm.currentTransaction().begin();
            Extent e = pm.getExtent(StateTransitionObj.class, false);
            for (Iterator i = e.iterator(); i.hasNext();) {
                StateTransitionObj next = (StateTransitionObj)i.next();
                if (oid.equals(pm.getObjectId(next))) {
                    // found instance
                    int value = next.readField();
                    if (value != originalValue){
                        fail(ASSERTION_FAILED, 
                             "Value has been changed with setNontransactionalWrite==true. " + 
                             "New value is " + value + " ... Old value was " + originalValue);
                    }
                }
            }
            pm.currentTransaction().commit();
        }
    }

    /** */
    private void createAndModifyInstance() {
        pm.currentTransaction().begin();
        // create and save the pc instance
        object = getPersistentNewInstance();
        // save the ObjectId
        oid = pm.getObjectId(object);
        // save the value of the int field
        originalValue = object.readField();
        pm.currentTransaction().commit();
        
        // change field outside of transaction
        pm.currentTransaction().setNontransactionalWrite(true);
        object.writeField(999);
    }

    /** */
    private StateTransitionObj getPersistentNewInstance() {
        StateTransitionObj obj = new StateTransitionObj();
        pm.makePersistent(obj); // obj should transition to PERSISTENT_NEW
        int curr = currentState(obj);
        if (curr != PERSISTENT_NEW) {
            fail(ASSERTION_FAILED,
                 "Unable to create persistent-new instance " +
                 "from transient instance via makePersistent(), state is " + 
                 getStateOfInstance(obj));
        }
        return obj;
    }
}
