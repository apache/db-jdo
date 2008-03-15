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
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B>RelationshipManyToManyNoRelationships
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

public class RelationshipManyToManyNoRelationships extends AbstractRelationshipTest {
    
    String testMethod = null;
    protected String ASSERTION_FAILED =
        "Assertion A15-3.14 (RelationshipManyToManyNoRelationships) failed: ";
    
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
        BatchTestRunner.run(RelationshipManyToManyNoRelationships.class);
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
                "expected emp.getProjects()to be empty",
                emp1.getProjects().isEmpty());
            assertTrue(testMethod +
                ": Test aborted, precondition is false; " +
                "expected ins.getMembers() to be empty",
                proj1.getMembers().isEmpty());
        }
    }
    
    /** */
    public void testAddFromMappedSide() {
        testMethod = "testAddFromMappedSide";
        if (isTestToBePerformed) {
            
            // Set relationship
            Set emps = new HashSet();
            emps.add(emp1);
            proj1.setMembers(emps);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(emp1.getProjects().contains(proj1),
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
                emp1.getProjects().contains(proj1),
                ASSERTION_FAILED + testMethod,
                "In new transaction, postcondition is false; " +
                "other side of relationship is not set.");
            pm.currentTransaction().commit();
            
            failOnError();
        }
    }
    
    /** */
    public void testAddFromMappedbySide() {
        testMethod = "testAddFromMappedbySide";
        if (isTestToBePerformed) {
            
            // Set relationship
            Set projs = new HashSet();
            projs.add(proj1);
            emp1.setProjects(projs);
            pm.flush();
            
            // Postcondition
            deferredAssertTrue(proj1.getMembers().contains(emp1),
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
                proj1.getMembers().contains(emp1),
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
            Set members = new HashSet();
            Employee empNew = new FullTimeEmployee(100, "Jerry", "Valentine",
                "Brown", new Date(500L), new Date(10000L), 125000);
            pm.makePersistent(empNew);
            members.add(empNew);
            proj1.setMembers(members);
            Object empNewOid = pm.getObjectId(empNew);
            pm.flush();
            
            assertFalse(testMethod + ": Test aborted, precondition is false; " +
                "expected empNewOid to be non-null", empNewOid == null);
            
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
    public void testReplaceFromMappedbySide() {
        testMethod = "testReplaceFromMappedbySide";
        if (isTestToBePerformed) {
            
            // Set relationship
            Set projects = new HashSet();
            Project projNew = new Project(99L, "Skunkworks",
                new BigDecimal(10000.35));
            pm.makePersistent(projNew);
            projects.add(projNew);
            emp1.setProjects(projects);
            Object projNewOid = pm.getObjectId((Object)projNew);
            pm.flush();
            
            assertFalse(testMethod + ": Test aborted, precondition is false; " +
                "expected projNewOid to be non-null", projNewOid == null);
            
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

}
