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

import org.apache.jdo.tck.JDO_Test;

import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Address;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> GetExtentWithInstancesMadePersistentViaReachability
 *<BR>
 *<B>Keywords:</B> inheritance extent
 *<BR>
 *<B>Assertion ID:</B> A12.5.4-1.
 *<BR>
 *<B>Assertion Description: </B>
If an instance of a class or interface that has a managed extent is made persistent via reachability, the instance is put into the extent implicitly.
 */

public class GetExtentWithInstancesMadePersistentViaReachability extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.4-3 (GetExtentWithInstancesMadePersistentViaReachability) failed: ";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetExtentWithInstancesMadePersistentViaReachability.class);
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
        pm.makePersistent(comp);
        //Add transient departments
        comp.addDepartment(new Department(1L, "Department 1"));
        comp.addDepartment(new Department(2L, "Department 2"));
        comp.addDepartment(new Department(3L, "Department 3"));
        tx.commit(); //Now the transient departments should be made persistent via reachability
    }

    /** */
    private void runTest(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Extent e = pm.getExtent(Department.class, false);

        boolean foundDep1 = false;
        boolean foundDep2 = false;
        boolean foundDep3 = false;

        for (Iterator i = e.iterator(); i.hasNext();) {
            Department dep = (Department) i.next();
            if (dep.getName().equals("Department 1"))
                foundDep1=true;
            else if (dep.getName().equals("Department 2"))
                foundDep2=true;
            else if (dep.getName().equals("Department 3"))
                foundDep3=true;		
        }

        if (!foundDep1) {
            fail(ASSERTION_FAILED,
                "Extent of class " + Department.class.getName() + " does not include instance with deptid 1L");
        }
        if (!foundDep2) {
            fail(ASSERTION_FAILED,
                "Extent of class " + Department.class.getName() + " does not include instance with deptid 2L");
        }
        if (!foundDep3) {
            fail(ASSERTION_FAILED,
                "Extent of class " + Department.class.getName() + " does not include instance with deptid 3L");
        }

        tx.commit();
    }
}
