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


package org.apache.jdo.tck.models.embedded;

import javax.jdo.PersistenceManager;
import java.util.Date;
import java.util.Set;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.Address;
import org.apache.jdo.tck.pc.company.Department;

/**
 *<B>Title:</B> Embedded Objects Track Their Changes
 *<BR>
 *<B>Keywords:</B> embedded lifecycle
 *<BR>
 *<B>Assertion ID:</B> A6.3-1.
 *<BR>
 *<B>Assertion Description: </B>
Second Class Objects track changes made to themselves and notify their owning 
First Class Object that they have changed, and the change is reflected as a
change to that First Class Object (e.g. the owning instance changes state from
persistent-clean to persistent-dirty).

 */

public class SecondClassObjectsTrackTheirChanges extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A6.3-1 (SecondClassObjectsTrackTheirChanges) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SecondClassObjectsTrackTheirChanges.class);
    }
    
    /** This tests that persistence-capable instances track changes or notify their owning instance that they are dirty */
    public void testPCInstance() {
        pm = getPM();
		pm.currentTransaction().begin();
		Company comp = getPersistentNewInstance(0);
		pm.currentTransaction().commit(); // obj should transition to hollow
		testHollowInstance(comp);
		pm.currentTransaction().begin();
		makePersistentCleanInstance(comp);

		Address addr = comp.getAddress();
		addr.setStreet("200 Orange Street"); // comp or addr should transition to persistent-dirty
		int currComp = currentState(comp);
		int currAddr = currentState(addr);
		if ((currComp != PERSISTENT_DIRTY) && (currAddr != PERSISTENT_DIRTY)){
		    fail(ASSERTION_FAILED,
				"Unable to create persistent-dirty instance " +
		    	"from persistent-clean instance via changing Address instance, state of Company instance is " + states[currComp] + " and state of Address instance is " + states[currAddr]);
		}
    }

    /** This tests that mutable system class instances track changes or notify their owning instance that they are dirty */
    public void testMutableSystemClass() {
        pm = getPM();
        pm.currentTransaction().begin();
        Company comp = getPersistentNewInstance(1);
        pm.currentTransaction().commit(); // obj should transition to hollow
        testHollowInstance(comp);
        pm.currentTransaction().begin();
        makePersistentCleanInstance(comp);

		Set depts = comp.getDepartments();
		comp.addDepartment(new Department(0,"HR",comp)); // comp or depts should transition to persistent-dirty
		int currComp = currentState(comp);
		int currDepts = currentState(depts);
		if ((currComp != PERSISTENT_DIRTY) && (currDepts != PERSISTENT_DIRTY)){
		    fail(ASSERTION_FAILED,
				"Unable to create persistent-dirty instance " +
		    	"from persistent-clean instance via changing Departments instance, state of Company instance is " + states[currComp] + " and state of Departments instance is " + states[currDepts]);
		}
    }

	public Company getPersistentNewInstance(long companyid)
	{
		Company obj = new Company(companyid, "MyCompany", new Date(), new Address(0,"","","","",""));
	    pm.makePersistent(obj); // obj should transition to persistent-new
	    int curr = currentState(obj);
	    if( curr != PERSISTENT_NEW ){
	        fail(ASSERTION_FAILED,
				"Unable to create persistent-new instance " +
	        	"from transient instance via makePersistent(), state is " + states[curr]);
	    }
	    return obj;
	}

	public void testHollowInstance(Company obj)
    {
		int curr = currentState(obj);
		if( curr != HOLLOW ){
		    fail(ASSERTION_FAILED,
				"Unable to create hollow instance " +
		    	"from persistent-new instance via commit(), state is " + states[curr]);
		}
    }

	public void makePersistentCleanInstance(Company obj)
	{
		pm.makeTransactional(obj); // obj should transition to persistent-clean
		int curr = currentState(obj);
		if( curr != PERSISTENT_CLEAN ){
		    fail(ASSERTION_FAILED,
				"Unable to create persistent-clean instance " +
		    	"from hollow instance via makeTransactional(obj), state is " + states[curr]);
		}
	}
}
