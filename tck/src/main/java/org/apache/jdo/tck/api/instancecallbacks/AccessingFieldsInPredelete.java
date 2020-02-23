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

import java.util.Date;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOUserException;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.instancecallbacks.InstanceCallbackClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Accessing Fields In Predelete
 *<BR>
 *<B>Keywords:</B> instancecallbacks
 *<BR>
 *<B>Assertion ID:</B> A10.4-2.
 *<BR>
 *<B>Assertion Description: </B>
 Access to field values within a call to <code>jdoPreDelete()</code>
 of a class implementing <code>InstanceCallbacks</code> are valid.

 */

/*
 *   Define a PersistenceCapable class containing some primative types, 
 *      a Date attribute, a String attribute,
 *      one reference to an object of the same class and one
 *      Set collection attribute containing two instances of objects of this class.
 *
 *   Create an object of this class (along with the referenced object and two members in the Set) and commit.
 *   Delete the object.
 *   Within jdoPreDelete(), capture the values of attribues and delete the referenced object
 *       and one of the two Set members.
 *
 *  In the next transaction, verify the data captured from the attriubtes was what was expected and
 *  that all the objects that should have been deleted have been deleted and the
 *  Set member that was not deleted is still present.
 *  Also verify that jdoPreDelete() is called on each of these objects deleted within the first jdoPreDelete().
 */

public class AccessingFieldsInPredelete extends TestParts {
    
    private static final String ASSERTION_FAILED = 
        "Assertion A10.4-2 (AccessingFieldsInPredelete) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(AccessingFieldsInPredelete.class);
    }
    
    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(InstanceCallbackClass.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
        Transaction t = pm.currentTransaction();
        
        InstanceCallbackClass.initializeStaticsForTest();

        InstanceCallbackClass.performPreDeleteTests = true;
        t.begin();
        Date createTime = new Date();
        InstanceCallbackClass secondaryObj = new InstanceCallbackClass("secondaryObj", createTime, 2, 2.0, (short)-1, '2', null);
        InstanceCallbackClass primaryObj = new InstanceCallbackClass("primaryObj", createTime, 1, 1.0, (short)3, '1', secondaryObj);
        InstanceCallbackClass childA = new InstanceCallbackClass("childA", createTime, 3, 3.0, (short)-2, '3', null);
        InstanceCallbackClass childB = new InstanceCallbackClass("childB", createTime, 4, 4.0, (short)-3, '4', null);
        pm.makePersistent(primaryObj);
        pm.makePersistent(secondaryObj);
        pm.makePersistent(childA);
        pm.makePersistent(childB);
        primaryObj.addChild(childA);
        primaryObj.addChild(childB);
        Object secondaryObjId = pm.getObjectId(secondaryObj);
        Object primaryObjId = pm.getObjectId(primaryObj);
        Object childAId = pm.getObjectId(childA);
        Object childBId = pm.getObjectId(childB);
        t.commit();
        
        t.begin();
        primaryObj = (InstanceCallbackClass)pm.getObjectById(primaryObjId, true);
        pm.deletePersistent(primaryObj);
        t.commit();
        
        t.begin();
        try {
            primaryObj = (InstanceCallbackClass)pm.getObjectById(primaryObjId, true);
            fail(ASSERTION_FAILED, "primaryObj deleted but getObjectById() on its Id succeeded.");
        } catch (JDOUserException e) {
            // expected one of these exceptions
        } catch (JDODataStoreException e) {
            // expected one of these exceptions
        }
        
        // check that jdoPreDelete() provided proper access to the attributes in primaryObj
        checkFieldValues(ASSERTION_FAILED, "jdoPreDelete attribute access:  ", 1, "primaryObj", createTime, 1.0, (short)3, '1');
        checkInstances(ASSERTION_FAILED, "jdoPreDelete instance access:  ", 1, "secondaryObj", 2, 7);
        checkPMAccess(ASSERTION_FAILED, "jdoPreDelete PersistenceManager access:  ", 1, true);
        
        // check that secondaryObj had jdoPreDelete() called on it and provided proper access to its attributes.
        checkFieldValues(ASSERTION_FAILED, "jdoPreDelete attribute access:  ", 2,  "secondaryObj", createTime, 2.0, (short)-1, '2');
        checkInstances(ASSERTION_FAILED, "jdoPreDelete instance access:  ", 2, null, 0, 0);
        checkPMAccess(ASSERTION_FAILED, "jdoPreDelete PersistenceManager access:  ", 2, true);

        // check that secondaryObj was deleted.
        try {
            secondaryObj = (InstanceCallbackClass)pm.getObjectById(secondaryObjId, true);
            fail(ASSERTION_FAILED, "secondaryObj should have been deleted but getObjectById() on its Id succeeded.");
        } catch (JDOUserException e) {
            // expected one of these exceptions
        } catch (JDODataStoreException e) {
            // expected one of these exceptions
        }
        
        // check that first added member of Set had jdoPreDelete() called on it and provided proper access to its attributes.
        checkFieldValues(ASSERTION_FAILED, "jdoPreDelete attribute access:  ", 3, "childA", createTime, 3.0, (short)-2, '3');
        checkInstances(ASSERTION_FAILED, "jdoPreDelete instance access:  ", 3, null, 0, 0);
        checkPMAccess(ASSERTION_FAILED, "jdoPreDelete PersistenceManager access:  ", 3, true);
        
        // verify first added member of Set was actaully deleted.
        try {
            childA = (InstanceCallbackClass)pm.getObjectById(childAId, true);
            fail(ASSERTION_FAILED, "First added member of Set primaryObj.children should have been deleted but getObjectById() on its Id succeeded.");
        } catch (JDOUserException e) {
            // expected one of these exceptions
        } catch (JDODataStoreException e) {
            // expected one of these exceptions
        }
        
        // check that the second added member of Set did not have jdoPreDelete() called on it and it was not deleted.
        if(InstanceCallbackClass.processedIndex[4] == true) {
            fail(ASSERTION_FAILED, "jdoPreDelete() called on childB--it was not deleted.");
        }
        
        try {
            childB = (InstanceCallbackClass)pm.getObjectById(childBId, true);
            if( childB == null ){
                if (debug) logger.debug("childB object is null");
            } else {
                if( childB.name == null )
                    if (debug) logger.debug("childB.name is null");
            }
            if(!childB.name.equals("childB")) {
                fail(ASSERTION_FAILED, "childB.name should be \"childB\".  Instead its value is \"" + childB.name + "\".");                
            }
        } catch (JDOUserException e) {
            fail(ASSERTION_FAILED, "Second added member of Set primaryObj.children should exist but getObjectById() got JDOUserException " + e);
        } catch (JDODataStoreException e) {
            fail(ASSERTION_FAILED, "Second added member of Set primaryObj.children should exist but getObjectById() got JDODataStoreException " + e);
        }
        t.rollback();
        pm.close(); 
        pm = null;
    }
}

