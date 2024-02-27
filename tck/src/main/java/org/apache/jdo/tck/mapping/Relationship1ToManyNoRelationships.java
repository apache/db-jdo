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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B>Relationship1ToManyNoRelationships <br>
 * <B>Keywords:</B> mapping, managed relationships <br>
 * <B>Assertion ID:</B> A15.3-14 <br>
 * <B>Assertion Description: Regardless of which side changes the relationship, flush (whether done
 * as part of commit or explicitly by the user) will modify the datastore to reflect the change and
 * will update the memory model for consistency...</B>
 */
public class Relationship1ToManyNoRelationships extends AbstractRelationshipTest {

  String testMethod = null;
  private static final String ASSERTION_FAILED =
      "Assertion A15-3.14 (Relationship1ToManyNoRelationships) failed: ";

  Object emp1Oid = null;
  Object dept1Oid = null;
  Object dept2Oid = null;
  Employee emp1 = null;
  Department dept1 = null;
  Department dept2 = null;

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
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      dept1 = (Department) pm.getObjectById(dept1Oid);
      dept2 = (Department) pm.getObjectById(dept2Oid);

      // Preconditions
      Assertions.assertTrue(
          emp1.getDepartment() == null,
          ASSERTION_FAILED
              + ": Test aborted, precondition is false; "
              + "expected emp.getDepartment()to be null");
      Assertions.assertTrue(
          dept1.getEmployees().isEmpty(),
          ASSERTION_FAILED
              + testMethod
              + ": Test aborted, precondition is false; "
              + "expected dept.getEmployees() to be empty");
    }
  }

  /** */
  @Test
  public void testSetToExistingFromMappedSide() {
    testMethod = "testSetToExistingFromMappedSide";
    if (isTestToBePerformed) {

      // Set relationship
      emp1.setDepartment(dept1);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          dept1.getEmployees().contains(emp1),
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      dept1 = (Department) pm.getObjectById(dept1Oid);
      deferredAssertTrue(
          dept1.getEmployees().contains(emp1),
          ASSERTION_FAILED + testMethod,
          "In new transaction, postcondition is false; "
              + "other side of relationship is not set.");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  @Test
  public void testAddExistingFromMappedbySide() {
    testMethod = "testSetToExistingFromMappedbySide";
    if (isTestToBePerformed) {

      // Set relationship
      Set<IEmployee> emps = new HashSet<>();
      emps.add(emp1);
      dept1.setEmployees(emps);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          emp1.getDepartment() == dept1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      dept1 = (Department) pm.getObjectById(dept1Oid);
      deferredAssertTrue(
          emp1.getDepartment() == dept1,
          ASSERTION_FAILED + testMethod,
          "In new transaction, postcondition is false; "
              + "other side of relationship is not set.");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  @Test
  public void testSetToNewFromMappedSide() {
    testMethod = "testSetToNewFromMappedSide";
    if (isTestToBePerformed) {

      // Set relationship
      Department deptNew = new Department(99L, "The New Department");
      emp1.setDepartment(deptNew);
      pm.makePersistent(deptNew);
      Object deptNewOid = pm.getObjectId(deptNew);
      pm.flush();

      Assertions.assertFalse(
          deptNewOid == null,
          testMethod
              + ": Test aborted, precondition is false; "
              + "expected deptNewOid to be non-null");

      // Postcondition
      deferredAssertTrue(
          deptNew.getEmployees().contains(emp1),
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      deferredAssertTrue(
          !dept1.getEmployees().contains(emp1),
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (dept1) not nulled on flush");

      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      deptNew = (Department) pm.getObjectById(deptNewOid);
      dept1 = (Department) pm.getObjectById(dept1Oid);
      deferredAssertTrue(
          deptNew.getEmployees().contains(emp1),
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set in new pm");
      deferredAssertTrue(
          !dept1.getEmployees().contains(emp1),
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (dept1) not nulled in new pm");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  @Test
  public void testSetToNewFromMappedbySide() {
    testMethod = "testSetToNewFromMappedbySide";
    if (isTestToBePerformed) {

      // Set relationship
      Set<IEmployee> emps = new HashSet<>();
      Employee empNew =
          new FullTimeEmployee(
              101, "Jenny", "Merriwether", "White", new Date(500L), new Date(10000L), 135000);
      pm.makePersistent(empNew);
      emps.add(empNew);
      dept1.setEmployees(emps);
      Object empNewOid = pm.getObjectId(empNew);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          empNew.getDepartment() == dept1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      deferredAssertTrue(
          emp1.getDepartment() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship not nulled on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      empNew = (Employee) pm.getObjectById(empNewOid);
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      dept1 = (Department) pm.getObjectById(dept1Oid);
      deferredAssertTrue(
          empNew.getDepartment() == dept1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set in new pm");
      deferredAssertTrue(
          emp1.getDepartment() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship not nulled in new pm");
      pm.currentTransaction().commit();

      failOnError();
    }
  }
}
