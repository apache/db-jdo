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

import java.util.Iterator;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOUserException;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.instancecallbacks.InstanceCallbackNonPersistFdsClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Modification of Nontransactional Nonpersistent Fields
 *<BR>
 *<B>Keywords:</B> instancecallbacks
 *<BR>
 *<B>Assertion ID:</B> A6.4.1-3.
 *<BR>
 *<B>Assertion Description: </B>
Nontransactional non-persistent fields may be modified during execution of
user-written callbacks defined in interface <code>InstanceCallbacks</code>
at specific points in the life cycle.

 */

/*
 *  Test jdoPostLoad() by setting values into the non-managed and transactional attriubes.
 *    This includes creating and populating a collection and initializing a Date.
 *  Verify those values are set once the object is referenced.
 *
 *  Test jdoPreClear(), jdoPreDelete() & jdoPreStore() by setting values into the nonmanaged and transactional attributes.
 *   This includes setting a collection and Date to null.
 *  Verify that no exceptions are thrown.
 */
public class ModificationOfNontransactionalNonpersistentFields extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A6.4.1-3 (ModificationOfNontransactionalNonpersistentFields) failed";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ModificationOfNontransactionalNonpersistentFields.class);
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(InstanceCallbackNonPersistFdsClass.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
        Transaction t = pm.currentTransaction(); 
        t.setRetainValues(false);

        InstanceCallbackNonPersistFdsClass.initializeStaticsForTest();
        
        t.begin();
        // create instance
        InstanceCallbackNonPersistFdsClass obj1 = new InstanceCallbackNonPersistFdsClass(1.1f, 1);
        pm.makePersistent(obj1);
        Object objPtr1 = pm.getObjectId (obj1);
        obj1.setNonPersist(1, (char)1, 0, (short)0);
        t.commit();  // jdoPreStore() and jdoPreClear() called 
        
        t.begin();
        try {
            obj1 = (InstanceCallbackNonPersistFdsClass)pm.getObjectById(objPtr1, true);  // jdoPostLoad() called
        } catch (JDOUserException e) {
            // could not locate persistent object created in previous transaction
            fail(ASSERTION_FAILED, "ModificationOfNontransactionalNonpersistentFields:  Could not locate persistent object obj1 created in previous transaction, got " + e);
            return;
        } catch (JDODataStoreException e) {
           // could not locate persistent object created in previous transaction
            fail(ASSERTION_FAILED, "ModificationOfNontransactionalNonpersistentFields:  Could not locate persistent object obj1 created in previous transaction, got " + e);
            return;
        }
        
        // check fields set in jdoPostLoad()
        if(obj1.i != -10) {
            fail(ASSERTION_FAILED, "jdoPostLoad:  Value incorrect, obj1.i != -10; it is " + obj1.i);
        }
        if(obj1.c != '2') {
            fail(ASSERTION_FAILED, "jdoPostLoad:  Value incorrect, obj1.c != '2'; it is " + obj1.c);
        }
        if(obj1.d != 30.0) {
            fail(ASSERTION_FAILED, "jdoPostLoad:  Value incorrect, obj1.d != 30.0; it is " + obj1.d);
        }
        if(obj1.s != 40) {
            fail(ASSERTION_FAILED, "jdoPostLoad:  Value incorrect, obj1.s != 40; it is " + obj1.s);
        }
        if(obj1.loadTime != InstanceCallbackNonPersistFdsClass.savedLoadTime)  {
            fail(ASSERTION_FAILED,
                 "jdoPostLoad:  Value incorrect, obj1.loadTime != " +
                 InstanceCallbackNonPersistFdsClass.savedLoadTime + 
                 "; it is " + obj1.loadTime);
        }
        boolean childrenValueIncorrect = false;
        if(obj1.children.size() != 3)  {
            childrenValueIncorrect = true;
        }else {
            if(!obj1.children.contains(InstanceCallbackNonPersistFdsClass.member1)) {
                childrenValueIncorrect = true;
            }
            if(!obj1.children.contains(InstanceCallbackNonPersistFdsClass.member2)) {
                childrenValueIncorrect = true;
            }
            if(!obj1.children.contains(InstanceCallbackNonPersistFdsClass.member3)) {
                childrenValueIncorrect = true;
            }
        }
        if(childrenValueIncorrect) {
            if (debug) {
                logger.debug("jdoPostLoad:  Value incorrect, obj1.children does not contain the correct String members");
                logger.debug("The members should be: " + InstanceCallbackNonPersistFdsClass.member1 + ", " +
                        InstanceCallbackNonPersistFdsClass.member2 + ", " + InstanceCallbackNonPersistFdsClass.member3);
                logger.debug("obj1.children contains " + obj1.children.size() + " members");
                if(obj1.children.size() != 0) {
                    logger.debug("Those members are:");
                    for(Iterator<String> i = obj1.children.iterator(); i.hasNext();) {
                        logger.debug(i.next());
                    }
                }
            }
            fail(ASSERTION_FAILED, "jdoPostLoad:  Value incorrect, obj1.children does not contain the correct String members");
        }
        
        pm.deletePersistent(obj1);  // jdoPreDelete() called
        t.commit();
        // check result of jdoPreStore(), jdoPreClear() and jdoPreDelete()
        if (!InstanceCallbackNonPersistFdsClass.preClearCalled) {
            fail(ASSERTION_FAILED, "jdoPreClear() never called on obj1");
        }
        
        if(!InstanceCallbackNonPersistFdsClass.preStoreCalled) {
            fail(ASSERTION_FAILED, "jdoPreStore() never called on obj1");
        }
        
        if(!InstanceCallbackNonPersistFdsClass.preDeleteCalled) {
            fail(ASSERTION_FAILED, "jdoPreDelete() never called on obj1");
        }
        
        if(InstanceCallbackNonPersistFdsClass.exceptions.size() != 0) {
            for(int index = 0; index < InstanceCallbackNonPersistFdsClass.exceptions.size(); index++) {
                fail(ASSERTION_FAILED, 
                     "" + InstanceCallbackNonPersistFdsClass.callbackCalled.get(index) + 
                     "operation " + 
                     InstanceCallbackNonPersistFdsClass.attributeOpCausingExceptions.get(index) + 
                     " resulted in exception " +
                     InstanceCallbackNonPersistFdsClass.exceptions.get(index));
            }
        }
        pm.close();
        pm = null;
    }
}
