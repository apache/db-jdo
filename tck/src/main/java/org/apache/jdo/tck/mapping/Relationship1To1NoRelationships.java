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
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.MedicalInsurance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B>Relationship1To1NoRelationships <br>
 * <B>Keywords:</B> mapping, managed relationships <br>
 * <B>Assertion ID:</B> A15.3-14 <br>
 * <B>Assertion Description: Regardless of which side changes the relationship, flush (whether done
 * as part of commit or explicitly by the user) will modify the datastore to reflect the change and
 * will update the memory model for consistency...</B>
 */
public class Relationship1To1NoRelationships extends AbstractRelationshipTest {

  String testMethod = null;
  private static final String ASSERTION_FAILED =
      "Assertion A15-3.14 (Relationship1To1NoRelationships) failed: ";

  Object emp1Oid = null;
  Object emp2Oid = null;
  Object medIns1Oid = null;
  Object medIns2Oid = null;
  Employee emp1 = null;
  Employee emp2 = null;
  MedicalInsurance medIns1 = null;
  MedicalInsurance medIns2 = null;

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
      medIns1Oid = getOidByName("medicalIns1");
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      medIns1 = (MedicalInsurance) pm.getObjectById(medIns1Oid);

      // Preconditions
      Assertions.assertTrue(
          emp1.getMedicalInsurance() == null,
          ASSERTION_FAILED
              + ": Test aborted, precondition is false; "
              + "expected emp.getMedicalInsurance()to be null");
      Assertions.assertTrue(
          medIns1.getEmployee() == null,
          ASSERTION_FAILED
              + ": Test aborted, precondition is false; "
              + "expected ins.getEmployee() to be null");
    }
  }

  /** */
  @Test
  public void testSetToExistingFromMappedSide() {
    testMethod = "testSetToExistingFromMappedSide";
    if (isTestToBePerformed) {

      // Set relationship
      medIns1.setEmployee(emp1);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          emp1.getMedicalInsurance() == medIns1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      medIns1 = (MedicalInsurance) pm.getObjectById(medIns1Oid);
      deferredAssertTrue(
          emp1.getMedicalInsurance() == medIns1,
          ASSERTION_FAILED + testMethod,
          "In new transaction, postcondition is false; "
              + "other side of relationship is not set.");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  @Test
  public void testSetToExistingFromMappedBySide() {
    testMethod = "testSetToExistingFromMappedBySide";
    if (isTestToBePerformed) {

      // Set relationship
      emp1.setMedicalInsurance(medIns1);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          medIns1.getEmployee() == emp1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      medIns1 = (MedicalInsurance) pm.getObjectById(medIns1Oid);
      deferredAssertTrue(
          medIns1.getEmployee() == emp1,
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
      Employee empNew =
          new FullTimeEmployee(99, "Matthew", "", "Adams", new Date(0L), new Date(10000L), 125000);
      pm.makePersistent(empNew);
      medIns1.setEmployee(empNew);
      Object empNewOid = pm.getObjectId(empNew);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          empNew.getMedicalInsurance() == medIns1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");

      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      empNew = (Employee) pm.getObjectById(empNewOid);
      medIns1 = (MedicalInsurance) pm.getObjectById(medIns1Oid);
      deferredAssertTrue(
          empNew.getMedicalInsurance() == medIns1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set in new pm");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  @Test
  public void testSetToNewFromMappedBySide() {
    testMethod = "testSetToNewFromMappedBySide";
    if (isTestToBePerformed) {

      // Set relationship
      MedicalInsurance medInsNew = new MedicalInsurance(99L, "Ameriblast", "B");
      pm.makePersistent(medInsNew);
      emp1.setMedicalInsurance(medInsNew);
      Object medInsNewOid = pm.getObjectId(medInsNew);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          medInsNew.getEmployee() == emp1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush.");

      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (Employee) pm.getObjectById(emp1Oid);
      medInsNew = (MedicalInsurance) pm.getObjectById(medInsNewOid);
      medIns1 = (MedicalInsurance) pm.getObjectById(medIns1Oid);
      deferredAssertTrue(
          medInsNew.getEmployee() == emp1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set in new pm");
      pm.currentTransaction().commit();

      failOnError();
    }
  }
}
