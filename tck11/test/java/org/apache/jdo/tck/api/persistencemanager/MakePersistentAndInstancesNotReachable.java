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

package org.apache.jdo.tck.api.persistencemanager;

import java.util.Iterator;
import java.util.Date;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Address;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> MakePersistent And Instances Not Reachable
 *<BR>
 *<B>Keywords:</B> 
 *<BR>
 *<B>Assertion ID:</B> A12.5.7-6C.
 *<BR>
 *<B>Assertion Description: </B>
When calling <code>PersistenceManager.makePersistent</code> or <code>makePersistentAll</code>, 
transient instances will become persistent and any transient instances reachable via persistent 
fields will become provisionally persistent. That is, they behave as persistent-new instances 
(return true to isPersistent, isNew, and isDirty).
But at commit time, the reachability algorithm is run again, 
and instances made provisionally persistent that are not 
currently reachable from persistent instances will revert to transient.
 */

public class MakePersistentAndInstancesNotReachable extends PersistenceManagerTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.7-6C (MakePersistentAndInstancesNotReachable) failed: ";
    
    private Department dep1 = null;
    private Department dep2 = null;
    private Department dep3 = null;

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MakePersistentAndInstancesNotReachable.class);
    }
    
    /** */
    public void test() {
        pm = getPM();
        createObjects(pm);
        runTest(pm);
  }

    /** */
    private void createObjects(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Company comp = new Company(1L, "Sun Microsystems", new Date(), new Address(0,"","","","",""));
        //Add transient departments
        dep1 = new Department(1L, "Department 1");
        dep2 = new Department(2L, "Department 1");
        dep3 = new Department(3L, "Department 1");
        comp.addDepartment(dep1);
        comp.addDepartment(dep2);
        comp.addDepartment(dep3);
        pm.makePersistent(comp); //Now the transient departments should be made provisionally persistent via reachability
        int curr = currentState(dep1);
        if( curr != PERSISTENT_NEW ){
            fail(ASSERTION_FAILED, "dep1 should be persistent-new, state is " + states[curr]);
        }
        curr = currentState(dep2);
        if( curr != PERSISTENT_NEW ){
            fail(ASSERTION_FAILED, "dep2 should be persistent-new, state is " + states[curr]);
        }
        curr = currentState(dep3);
        if( curr != PERSISTENT_NEW ){
            fail(ASSERTION_FAILED, "dep3 should be persistent-new, state is " + states[curr]);
        }
        //Remove departments
        comp.removeDepartment(dep1);
        comp.removeDepartment(dep2);
        comp.removeDepartment(dep3);
        tx.commit(); //Now the removed departments should be made transient again
    }

    /** */
    void runTest(PersistenceManager pm) {
        int curr = currentState(dep1);
        if( curr != TRANSIENT ){
            fail(ASSERTION_FAILED, "dep1 should be transient, state is " + states[curr]);
        }
        curr = currentState(dep2);
        if( curr != TRANSIENT ){
            fail(ASSERTION_FAILED, "dep2 should be transient, state is " + states[curr]);
        }
        curr = currentState(dep3);
        if( curr != TRANSIENT ){
            fail(ASSERTION_FAILED, "dep3 should be transient, state is " + states[curr]);
        }
    }
}
