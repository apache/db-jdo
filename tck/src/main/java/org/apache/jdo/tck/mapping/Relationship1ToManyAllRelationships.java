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

package org.apache.jdo.tck.mapping;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.jdo.JDOHelper;

import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.IDepartment;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B>Relationship1ToManyAllRelationships
 *<BR>
 *<B>Keywords:</B> mapping, managed relationships
 *<BR>
 *<B>Assertion ID:</B> A15.3-14
 *<BR>
 *<B>Assertion Description: Regardless of which side changes the relationship,
 * flush (whether done as part of commit or explicitly by the user) will modify
 * the datastore to reflect the change and will update the memory model
 * for consistency...</B>
 */

public class Relationship1ToManyAllRelationships extends AbstractRelationshipTest {
    
    String testMethod = null;
    protected String ASSERTION_FAILED =
        "Assertion A15-3.14 (Relationship1ToManyAllRelationships) failed: ";
    
    Object emp1Oid = null;
    Object dept1Oid = null;
    Object dept2Oid = null;
    Employee emp1 = null;
    Department dept1 = null;
    Department dept2 = null;
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(Relationship1ToManyAllRelationships.class);
    }
    
    /**
     * @see AbstractRelationshipTest#localSetUp()
     */
    @Override
    protected void localSetUp() {
        super.localSetUp();
        if (isTestToBePerformed) {
            getPM();
            pm.currentTransaction().begin();
            
            emp1Oid = getOidByName("emp1");
            dept1Oid = getOidByName("dept1");
            dept2Oid = getOidByName("dept2");
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            dept1 = (Department)pm.getObjectById(dept1Oid);
            dept2 = (Department)pm.getObjectById(dept2Oid);
            
            // Preconditions
            assertTrue(ASSERTION_FAILED +
                ": Test aborted, precondition is false; " +
                "expected emp.getDepartment()to be dept1",
                emp1.getDepartment() == dept1);
            assertTrue(ASSERTION_FAILED +
                ": Test aborted, precondition is false; " +
                "expected dept.getEmployees() to contain emp1",
                dept1.getEmployees().contains(emp1));
        }
    }
    
    /** */
    public void testSetToExistingFromMappedSide() {
        testMethod = "testSetToExistingFromMappedSide";
        if (isTestToBePerformed) {
            
            // Set relationship
            emp1.setDepartment(dept2);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(dept2.getEmployees().contains(emp1),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "other side of relationship not set on flush");
            deferredAssertTrue(!dept1.getEmployees().contains(emp1),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; reference emp1 not removed "
                + "from previous relationship (dept1.employees)");
            
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            dept1 = (Department)pm.getObjectById(dept1Oid);
            dept2 = (Department)pm.getObjectById(dept2Oid);
            deferredAssertTrue(dept2.getEmployees().contains(emp1),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "other side of relationship not set in new pm");
            deferredAssertTrue(!dept1.getEmployees().contains(emp1),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; reference emp1 not removed "
                + "from previous relationship (dept1.employees)");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
    
        
    /** */
    public void testAddExistingFromMappedbySide() {
        testMethod = "testAddExistingFromMappedbySide";
        if (isTestToBePerformed) {
            
            // Set relationship
            Object emp4Oid = getOidByName("emp4");
            Employee emp4 = (Employee)pm.getObjectById(emp4Oid);
            Object dept2Oid = getOidByName("dept2");
            Department dept2 = (Department)pm.getObjectById(dept2Oid);

            dept1.addEmployee(emp4);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(emp4.getDepartment() == dept1,
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "other side of relationship not set on flush");
            deferredAssertTrue(!dept2.getEmployees().contains(emp4),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "previous relationship not unset on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp4 = (Employee)pm.getObjectById(emp4Oid);
            dept1 = (Department)pm.getObjectById(dept1Oid);
            dept2 = (Department)pm.getObjectById(dept2Oid);
            deferredAssertTrue(emp4.getDepartment() == dept1,
                ASSERTION_FAILED + testMethod,
                "In new transaction, postcondition is false; " +
                "other side of relationship is not set.");
            deferredAssertTrue(!dept2.getEmployees().contains(emp4),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "previous relationship not unset on flush");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }

    /** */
    public void testReplaceFromMappedbySide() {
        testMethod = "testReplaceFromMappedbySide";
        if (isTestToBePerformed) {
            
            // Set relationship
            Object emp4Oid = getOidByName("emp4");
            Employee emp4 = (Employee)pm.getObjectById(emp4Oid);
            Object dept2Oid = getOidByName("dept2");
            Department dept2 = (Department)pm.getObjectById(dept2Oid);

            Set<IEmployee> emps = new HashSet<>();
            emps.add(emp4);
            dept1.setEmployees(emps);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(emp4.getDepartment() == dept1,
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "other side of relationship not set on flush");
            deferredAssertTrue(!dept2.getEmployees().contains(emp4),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "previous relationship not unset on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp4 = (Employee)pm.getObjectById(emp4Oid);
            dept1 = (Department)pm.getObjectById(dept1Oid);
            dept2 = (Department)pm.getObjectById(dept2Oid);
            deferredAssertTrue(emp4.getDepartment() == dept1,
                ASSERTION_FAILED + testMethod,
                "In new transaction, postcondition is false; " +
                "other side of relationship is not set.");
            deferredAssertTrue(!dept2.getEmployees().contains(emp4),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "previous relationship not unset on flush");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }

    /** */
    public void testAddNewFromMappedbySide() {
        testMethod = "testAddNewFromMappedbySide";
        if (isTestToBePerformed) {
            
            // Set relationship
            Employee empNew = new FullTimeEmployee(101, "Jenny", "Merriwether",
                "White", new Date(500L), new Date(10000L), 135000);
            pm.makePersistent(empNew);
            Object empNewOid = pm.getObjectId(empNew);

            dept1.addEmployee(empNew);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(empNew.getDepartment() == dept1,
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            empNew = (Employee)pm.getObjectById(empNewOid);
            dept1 = (Department)pm.getObjectById(dept1Oid);
            deferredAssertTrue(empNew.getDepartment() == dept1,
                ASSERTION_FAILED + testMethod,
                "In new transaction, postcondition is false; " +
                "other side of relationship is not set.");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }

    /** */
    public void testSetToNullFromMappedSide() {
        testMethod = "testSetToNullFromMappedSide";
        if (isTestToBePerformed) {
            // Set relationship
            emp1.setDepartment(null);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(!dept1.getEmployees().contains(emp1),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            dept1 = (Department)pm.getObjectById(dept1Oid);
            deferredAssertTrue(
                !dept1.getEmployees().contains(emp1),
                ASSERTION_FAILED + testMethod,
                "In new transaction, postcondition is false; " +
                "other side of relationship is not set.");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
    
    /** */
    public void testSetToNullFromMappedbySide() {
        testMethod = "testSetToNullFromMappedbySide";
        if (isTestToBePerformed) {
            
            // Set relationship
            dept1.setEmployees(null);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(emp1.getDepartment() == null,
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            deferredAssertTrue(
                emp1.getDepartment() == null,
                ASSERTION_FAILED + testMethod,
                "In new transaction, postcondition is false; " +
                "other side of relationship is not set.");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
    
    /** */
    public void testSetToNewFromMappedSide() {
        testMethod = "testSetToNewFromMappedSide";
        if (isTestToBePerformed) {
            
            // Set relationship
            IDepartment deptNew = new Department(99L, "The New Department");
            pm.makePersistent(deptNew);
            emp1.setDepartment(deptNew);
            Object deptNewOid = pm.getObjectId(deptNew);
            pm.flush();
            
            assertFalse(testMethod + ": Test aborted, precondition is false; " +
                "expected deptNewOid to be non-null", deptNewOid == null);
            
            // Postcondition
            deferredAssertTrue(deptNew.getEmployees().contains(emp1),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "other side of relationship not set on flush");
            deferredAssertTrue(!dept1.getEmployees().contains(emp1),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "previous relationship (dept1) not nulled on flush");
            
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            deptNew = (Department)pm.getObjectById(deptNewOid);
            dept1 = (Department)pm.getObjectById(dept1Oid);
            deferredAssertTrue(deptNew.getEmployees().contains(emp1),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "other side of relationship not set in new pm");
            deferredAssertTrue(!dept1.getEmployees().contains(emp1),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "previous relationship (dept1) not nulled in new pm");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
    
    /** */
    public void testRemoveFromMappedbySide() {
        testMethod = "testRemoveFromMappedbySide";
        if (isTestToBePerformed) {
            
            // Set relationship
            dept1.removeEmployee(emp1);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(emp1.getDepartment() == null,
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            deferredAssertTrue(
                emp1.getDepartment() == null,
                ASSERTION_FAILED + testMethod,
                "In new transaction, postcondition is false; " +
                "other side of relationship is not set.");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
    
    /** */
    public void testDeleteFromMappedSide() {
        testMethod = "testDeleteFromMappedSide";
        if (isTestToBePerformed) {
            // remember id
            long emp1Id = emp1.getPersonid();
            // Set relationship
            pm.deletePersistent(emp1);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(!containsEmployee(dept1.getEmployees(), emp1Id),
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            dept1 = (Department)pm.getObjectById(dept1Oid);
            deferredAssertTrue(
                !containsEmployee(dept1.getEmployees(), emp1Id),
                ASSERTION_FAILED + testMethod,
                "In new transaction, postcondition is false; " +
                "other side of relationship is not set.");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
    
    /** */
    public void testDeleteFromMappedbySide() {
        testMethod = "testDeleteFromMappedbySide";
        if (isTestToBePerformed) {
            
            // Set relationship
            pm.deletePersistent(dept1);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(emp1.getDepartment() == null,
                ASSERTION_FAILED + testMethod,
                "Postcondition is false; "
                + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            deferredAssertTrue(
                emp1.getDepartment() == null,
                ASSERTION_FAILED + testMethod,
                "In new transaction, postcondition is false; " +
                "other side of relationship is not set.");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
}
