/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B>RelationshipManyToManyNoRelationships <br>
 * <B>Keywords:</B> mapping, managed relationships <br>
 * <B>Assertion ID:</B> A15.3-14 <br>
 * <B>Assertion Description: Regardless of which side changes the relationship, flush (whether done
 * as part of commit or explicitly by the user) will modify the datastore to reflect the change and
 * will update the memory model for consistency...</B>
 */
public class RelationshipManyToManyNoRelationships extends AbstractRelationshipTest {

  String testMethod = null;
  private static final String ASSERTION_FAILED =
      "Assertion A15-3.14 (RelationshipManyToManyNoRelationships) failed: ";

  Object emp1Oid = null;
  Object proj1Oid = null;
  Employee emp1 = null;
  Project proj1 = null;

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
      proj1Oid = getOidByName("proj1");
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      proj1 = (Project) pm.getObjectById(proj1Oid);

      // Preconditions
      Assertions.assertTrue(
          emp1.getProjects().isEmpty(),
          testMethod
              + ": Test aborted, precondition is false; "
              + "expected emp.getProjects()to be empty");
      Assertions.assertTrue(
          proj1.getMembers().isEmpty(),
          testMethod
              + ": Test aborted, precondition is false; "
              + "expected ins.getMembers() to be empty");
    }
  }

  /** */
  @Test
  public void testAddFromMappedSide() {
    testMethod = "testAddFromMappedSide";
    if (isTestToBePerformed) {

      // Set relationship
      Set<IEmployee> emps = new HashSet<>();
      emps.add(emp1);
      proj1.setMembers(emps);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          emp1.getProjects().contains(proj1),
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      proj1 = (Project) pm.getObjectById(proj1Oid);
      deferredAssertTrue(
          emp1.getProjects().contains(proj1),
          ASSERTION_FAILED + testMethod,
          "In new transaction, postcondition is false; "
              + "other side of relationship is not set.");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  @Test
  public void testAddFromMappedbySide() {
    testMethod = "testAddFromMappedbySide";
    if (isTestToBePerformed) {

      // Set relationship
      Set<IProject> projs = new HashSet<>();
      projs.add(proj1);
      emp1.setProjects(projs);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          proj1.getMembers().contains(emp1),
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      proj1 = (Project) pm.getObjectById(proj1Oid);
      deferredAssertTrue(
          proj1.getMembers().contains(emp1),
          ASSERTION_FAILED + testMethod,
          "In new transaction, postcondition is false; "
              + "other side of relationship is not set.");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  @Test
  public void testReplaceFromMappedSide() {
    testMethod = "testReplaceFromMappedSide";
    if (isTestToBePerformed) {

      // Set relationship
      Set<IEmployee> members = new HashSet<>();
      Employee empNew =
          new FullTimeEmployee(
              100, "Jerry", "Valentine", "Brown", new Date(500L), new Date(10000L), 125000);
      pm.makePersistent(empNew);
      members.add(empNew);
      proj1.setMembers(members);
      Object empNewOid = pm.getObjectId(empNew);
      pm.flush();

      Assertions.assertFalse(
          empNewOid == null,
          testMethod
              + ": Test aborted, precondition is false; "
              + "expected empNewOid to be non-null");

      // Postcondition

      deferredAssertTrue(
          empNew.getProjects().contains(proj1),
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      empNew = (Employee) pm.getObjectById(empNewOid);
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      proj1 = (Project) pm.getObjectById(proj1Oid);
      deferredAssertTrue(
          empNew.getProjects().contains(proj1),
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set in new pm");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  @Test
  public void testReplaceFromMappedbySide() {
    testMethod = "testReplaceFromMappedbySide";
    if (isTestToBePerformed) {

      // Set relationship
      Set<IProject> projects = new HashSet<>();
      Project projNew = new Project(99L, "Skunkworks", new BigDecimal(10000.35));
      pm.makePersistent(projNew);
      projects.add(projNew);
      emp1.setProjects(projects);
      Object projNewOid = pm.getObjectId(projNew);
      pm.flush();

      Assertions.assertFalse(
          projNewOid == null,
          testMethod
              + ": Test aborted, precondition is false; "
              + "expected projNewOid to be non-null");

      // Postcondition
      deferredAssertTrue(
          projNew.getMembers().contains(emp1),
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      projNew = (Project) pm.getObjectById(projNewOid);
      proj1 = (Project) pm.getObjectById(proj1Oid);
      deferredAssertTrue(
          projNew.getMembers().contains(emp1),
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set in new pm");
      pm.currentTransaction().commit();

      failOnError();
    }
  }
}
