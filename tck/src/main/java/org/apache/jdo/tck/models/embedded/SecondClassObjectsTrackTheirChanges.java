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


package org.apache.jdo.tck.models.embedded;

import java.util.Date;
import java.util.Set;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.Address;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.IDepartment;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> SecondClassObjectsTrackTheirChanges
 *<BR>
 *<B>Keywords:</B> embedded lifecycle
 *<BR>
 *<B>Assertion ID:</B> A14.6.9-9
 *<BR>
 *<B>Assertion Description: </B>
If an SCO field is in the result, the projected field is  not owned by any persistent instance, and modifying the SCO value has  no effect on any persistent instance. If an FCO field is in the  result, the projected field is a persistent instance, and  modifications made to the instance are are reflected as changes to  the datastore per transaction requirements.
 */

public class SecondClassObjectsTrackTheirChanges extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.9-9 (SecondClassObjectsTrackTheirChanges) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SecondClassObjectsTrackTheirChanges.class);
    }
    
    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(Company.class);
    }

    /** This tests that persistence-capable instances track changes
     * or notify their owning instance that they are dirty */
    public void testPCInstance() {
        pm = getPM();
        pm.currentTransaction().begin();
        Company comp = getPersistentNewInstance(0);
        pm.currentTransaction().commit(); // obj should transition to hollow
        testHollowInstance(comp);
        pm.currentTransaction().begin();
        makePersistentCleanInstance(comp);

        Address addr = (Address)comp.getAddress();
        // comp or addr should transition to persistent-dirty
        addr.setStreet("200 Orange Street");
        int currComp = currentState(comp);
        int currAddr = currentState(addr);
        if ((currComp != PERSISTENT_DIRTY) && (currAddr != PERSISTENT_DIRTY)){
            fail(ASSERTION_FAILED,
                "Unable to create persistent-dirty instance " +
                "from persistent-clean instance via changing Address instance, "
                + "state of Company instance is " + states[currComp] 
                + " and state of Address instance is " + states[currAddr]);
        }
    }

    /** This tests that mutable system class instances track changes
      * or notify their owning instance that they are dirty */
    public void testMutableSystemClass() {
        pm = getPM();
        pm.currentTransaction().begin();
        Company comp = getPersistentNewInstance(1);
        pm.currentTransaction().commit(); // obj should transition to hollow
        testHollowInstance(comp);
        pm.currentTransaction().begin();
        makePersistentCleanInstance(comp);

        Set<IDepartment> depts = comp.getDepartments();
        // comp or depts should transition to persistent-dirty
        comp.addDepartment(new Department(0, "HR", comp));
        int currComp = currentState(comp);
        int currDepts = currentState(depts);
        if ((currComp != PERSISTENT_DIRTY) && (currDepts != PERSISTENT_DIRTY)){
            fail(ASSERTION_FAILED,
                "Unable to create persistent-dirty instance "
                + "from persistent-clean instance via changing Departments "
                + "instance, state of Company instance is "
                + states[currComp] + " and state of Departments instance is "
                + states[currDepts]);
        }
    }

    public Company getPersistentNewInstance(long companyid) {
        Company obj = new Company(companyid, "MyCompany", new Date(),
                new Address(0,"","","","",""));
        pm.makePersistent(obj); // obj should transition to persistent-new
        int curr = currentState(obj);
        if( curr != PERSISTENT_NEW ){
            fail(ASSERTION_FAILED,
                "Unable to create persistent-new instance "
                + "from transient instance via makePersistent(), state is "
                + states[curr]);
        }
        return obj;
    }

    public void testHollowInstance(Company obj) {
        int curr = currentState(obj);
        if( curr != HOLLOW ){
            fail(ASSERTION_FAILED,
                "Unable to create hollow instance "
                + "from persistent-new instance via commit(), state is "
                + states[curr]);
        }
    }

    public void makePersistentCleanInstance(Company obj) {
        pm.makeTransactional(obj); // obj should transition to persistent-clean
        int curr = currentState(obj);
        if( curr != PERSISTENT_CLEAN ){
            fail(ASSERTION_FAILED,
                "Unable to create persistent-clean instance "
                + "from hollow instance via makeTransactional(obj), state is "
                + states[curr]);
        }
    }
}
