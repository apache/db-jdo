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

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.pc.company.IProject;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B>RelationshipManyToManyAllRelationships
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

public class RelationshipManyToManyAllRelationships
        extends AbstractRelationshipTest {
    
    String testMethod = null;
    protected String ASSERTION_FAILED =
            "Assertion A15-3.14 (RelationshipManyToManyAllRelationships) failed: ";

            Object emp1Oid = null;
            Object proj1Oid = null;
            Employee emp1 = null;
            Project proj1 = null;
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(RelationshipManyToManyAllRelationships.class);
    }
    
    /**
     * @see AbstractRelationshipTest#localSetUp()
     */
    protected void localSetUp() {
        super.localSetUp();
        if (isTestToBePerformed) {
             
            getPM();
            pm.currentTransaction().begin();
            
            emp1Oid = getOidByName("emp1");
            proj1Oid = getOidByName("proj1");
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            proj1 = (Project)pm.getObjectById(proj1Oid);
            
            // Preconditions
            assertTrue(testMethod +
                    ": Test aborted, precondition is false; " +
                    "expected emp.getProjects()to be contain proj1",
                    emp1.getProjects().contains(proj1));
            assertTrue(testMethod +
                    ": Test aborted, precondition is false; " +
                    "expected proj.getMembers() to contain emp1",
                    proj1.getMembers().contains(emp1));
        }
    }
    
    /** */
    public void testSetToNullFromMappedSide() {
        testMethod = "testSetToNullFromMappedSide";
        if (isTestToBePerformed) {
            
            // Set relationship
            proj1.setMembers(null);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(!emp1.getProjects().contains(proj1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            proj1 = (Project)pm.getObjectById(proj1Oid);
            deferredAssertTrue(
                    !emp1.getProjects().contains(proj1),
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
            emp1.setProjects(null);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(!proj1.getMembers().contains(emp1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            proj1 = (Project)pm.getObjectById(proj1Oid);
            deferredAssertTrue(
                    !proj1.getMembers().contains(emp1),
                    ASSERTION_FAILED + testMethod,
                    "In new transaction, postcondition is false; " +
                    "other side of relationship is not set.");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }

    /** */
    public void testReplaceFromMappedSide() {
        testMethod = "testReplaceFromMappedSide";
        if (isTestToBePerformed) {
            
            // Set relationship
            IEmployee empNew = new FullTimeEmployee(100, "Jerry", "Valentine",
                    "Brown", new Date(500L), new Date(10000L), 125000);
            pm.makePersistent(empNew);
            Set members = new HashSet();
            members.add(empNew);
            proj1.setMembers(members);
            Object empNewOid = pm.getObjectId((Object)empNew);
            pm.flush();

            // Postcondition

            deferredAssertTrue(empNew.getProjects().contains(proj1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            empNew = (IEmployee)pm.getObjectById(empNewOid);
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            proj1 = (Project)pm.getObjectById(proj1Oid);
            deferredAssertTrue(empNew.getProjects().contains(proj1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set in new pm");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
    
    /** */
    public void testReplaceFromMappedbySide() {
        testMethod = "testReplaceFromMappedbySide";
        if (isTestToBePerformed) {
            
            // Set relationship
            IProject projNew = new Project(99L, "Skunkworks",
                    new BigDecimal(10000.35));
            pm.makePersistent(projNew);
            Set projects = new HashSet();
            projects.add(projNew);
            emp1.setProjects(projects);
            Object projNewOid = pm.getObjectId((Object)projNew);
            pm.flush();

            // Postcondition
             deferredAssertTrue(projNew.getMembers().contains(emp1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            projNew = (Project)pm.getObjectById(projNewOid);
            proj1 = (Project)pm.getObjectById(proj1Oid);
             deferredAssertTrue(projNew.getMembers().contains(emp1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set in new pm");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }

    /** */
    public void testAddNewFromMappedSide() {
        testMethod = "testAddNewFromMappedSide";
        if (isTestToBePerformed) {
            
            // Set relationship
            Employee empNew = new FullTimeEmployee(100, "Jerry", "Valentine",
                    "Brown", new Date(500L), new Date(10000L), 125000);
            pm.makePersistent(empNew);
            proj1.addMember(empNew);
            Object empNewOid = pm.getObjectId((Object)empNew);
            pm.flush();

            // Postcondition

            deferredAssertTrue(empNew.getProjects().contains(proj1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            empNew = (Employee)pm.getObjectById(empNewOid);
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            proj1 = (Project)pm.getObjectById(proj1Oid);
            deferredAssertTrue(empNew.getProjects().contains(proj1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set in new pm");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
    
    /** */
    public void testAddNewFromMappedbySide() {
        testMethod = "testAddNewFromMappedbySide";
        if (isTestToBePerformed) {
            
            // Set relationship
            Project projNew = new Project(99L, "Skunkworks",
                    new BigDecimal(10000.35));
            pm.makePersistent(projNew);
            emp1.addProject(projNew);
            Object projNewOid = pm.getObjectId((Object)projNew);
            pm.flush();

            // Postcondition
             deferredAssertTrue(projNew.getMembers().contains(emp1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            projNew = (Project)pm.getObjectById(projNewOid);
            proj1 = (Project)pm.getObjectById(proj1Oid);
             deferredAssertTrue(projNew.getMembers().contains(emp1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set in new pm");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
    /** */
    public void testAddExistingFromMappedSide() {
        testMethod = "testAddExistingFromMappedSide";
        if (isTestToBePerformed) {
            
            // Set relationship
            Object emp4Oid = getOidByName("emp4");
            Employee emp4 = (Employee)pm.getObjectById(emp4Oid);
            proj1.addMember(emp4);
            pm.flush();

            // Postcondition

            deferredAssertTrue(emp4.getProjects().contains(proj1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp4 = (Employee)pm.getObjectById(emp4Oid);
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            proj1 = (Project)pm.getObjectById(proj1Oid);
            deferredAssertTrue(emp4.getProjects().contains(proj1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set in new pm");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
    
    /** */
    public void testAddExistingFromMappedbySide() {
        testMethod = "testAddExistingFromMappedbySide";
        if (isTestToBePerformed) {
            
            // Set relationship
            Object proj2Oid = getOidByName("proj2");
            Project proj2 = (Project)pm.getObjectById(proj2Oid);
            emp1.addProject(proj2);
            pm.flush();

            // Postcondition
             deferredAssertTrue(proj2.getMembers().contains(emp1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            proj2 = (Project)pm.getObjectById(proj2Oid);
            proj1 = (Project)pm.getObjectById(proj1Oid);
             deferredAssertTrue(proj2.getMembers().contains(emp1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set in new pm");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }

    /** */
    public void testRemoveFromMappedSide() {
        testMethod = "testRemoveFromMappedSide";
        if (isTestToBePerformed) {
            
            // Set relationship
            proj1.removeMember(emp1);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(!emp1.getProjects().contains(proj1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            proj1 = (Project)pm.getObjectById(proj1Oid);
            deferredAssertTrue(
                    !emp1.getProjects().contains(proj1),
                    ASSERTION_FAILED + testMethod,
                    "In new transaction, postcondition is false; " +
                    "other side of relationship is not set.");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
    
    /** */
    public void testRemoveFromMappedbySide() {
        testMethod = "testRemoveFromMappedbySide";
        if (isTestToBePerformed) {
                        
            // Set relationship
            emp1.removeProject(proj1);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(!proj1.getMembers().contains(emp1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            proj1 = (Project)pm.getObjectById(proj1Oid);
            deferredAssertTrue(
                    !proj1.getMembers().contains(emp1),
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
            // Set relationship
            pm.deletePersistent(proj1);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(!emp1.getProjects().contains(proj1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            deferredAssertTrue(
                    !emp1.getProjects().contains(proj1),
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
            pm.deletePersistent(emp1);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(!proj1.getMembers().contains(emp1),
                    ASSERTION_FAILED + testMethod,
                    "Postcondition is false; "
                    + "other side of relationship not set on flush");
            pm.currentTransaction().commit();
            cleanupPM();
            getPM();
            
            pm.currentTransaction().begin();
            proj1 = (Project)pm.getObjectById(proj1Oid);
            deferredAssertTrue(
                    !proj1.getMembers().contains(emp1),
                    ASSERTION_FAILED + testMethod,
                    "In new transaction, postcondition is false; " +
                    "other side of relationship is not set.");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
}
