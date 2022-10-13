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
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.pc.company.IMedicalInsurance;
import org.apache.jdo.tck.pc.company.MedicalInsurance;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>Relationship1To1AllRelationships <br>
 * <B>Keywords:</B> mapping, managed relationships <br>
 * <B>Assertion ID:</B> A15.3-14 <br>
 * <B>Assertion Description: Regardless of which side changes the relationship, flush (whether done
 * as part of commit or explicitly by the user) will modify the datastore to reflect the change and
 * will update the memory model for consistency...</B>
 */
public class Relationship1To1AllRelationships extends AbstractRelationshipTest {

  String testMethod = null;
  private static final String ASSERTION_FAILED =
      "Assertion A15-3.14 (Relationship1To1AllRelationships) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(Relationship1To1AllRelationships.class);
  }

  Object emp1Oid = null;
  Object emp2Oid = null;
  Object medIns1Oid = null;
  Object medIns2Oid = null;
  IEmployee emp1 = null;
  IEmployee emp2 = null;
  IMedicalInsurance medIns1 = null;
  IMedicalInsurance medIns2 = null;

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
      emp2Oid = getOidByName("emp2");
      medIns1Oid = getOidByName("medicalIns1");
      medIns2Oid = getOidByName("medicalIns2");
      emp1 = (IEmployee) pm.getObjectById(emp1Oid);
      emp2 = (IEmployee) pm.getObjectById(emp2Oid);
      medIns1 = (IMedicalInsurance) pm.getObjectById(medIns1Oid);
      medIns2 = (IMedicalInsurance) pm.getObjectById(medIns2Oid);

      // Preconditions
      assertTrue(
          ASSERTION_FAILED
              + testMethod
              + ": Test aborted, precondition is false; "
              + "expected emp.getMedicalInsurance()to be medicalIns1",
          emp1.getMedicalInsurance() == medIns1);
      assertTrue(
          ASSERTION_FAILED
              + testMethod
              + ": Test aborted, precondition is false; "
              + "expected ins.getEmployee() to be emp1",
          medIns1.getEmployee() == emp1);
      assertTrue(
          ASSERTION_FAILED
              + testMethod
              + ": Test aborted, precondition is false; "
              + "expected emp.getMedicalInsurance()to be medicalIns1",
          emp2.getMedicalInsurance() == medIns2);
      assertTrue(
          ASSERTION_FAILED
              + testMethod
              + ": Test aborted, precondition is false; "
              + "expected ins.getEmployee() to be emp1",
          medIns2.getEmployee() == emp2);
    }
  }

  /** */
  public void testSetToExistingFromMappedSide() {
    testMethod = "testSetToExistingFromMappedSide";
    if (isTestToBePerformed) {

      // Set relationship
      medIns1.setEmployee(emp2);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          emp2.getMedicalInsurance() == medIns1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      deferredAssertTrue(
          emp1.getMedicalInsurance() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (emp1) not nulled on flush");
      deferredAssertTrue(
          medIns2.getEmployee() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (medIns2) not nulled on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (IEmployee) pm.getObjectById(emp1Oid);
      emp2 = (IEmployee) pm.getObjectById(emp2Oid);
      medIns1 = (IMedicalInsurance) pm.getObjectById(medIns1Oid);
      medIns2 = (IMedicalInsurance) pm.getObjectById(medIns2Oid);
      deferredAssertTrue(
          emp2.getMedicalInsurance() == medIns1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set in new pm");
      deferredAssertTrue(
          emp1.getMedicalInsurance() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (emp1) not nulled in new pm");
      deferredAssertTrue(
          medIns2.getEmployee() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (medIns2) not nulled in new pm");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  public void testSetToExistingFromMappedBySide() {
    testMethod = "testSetToExistingFromMappedBySide";
    if (isTestToBePerformed) {

      // Set relationship
      emp1.setMedicalInsurance(medIns2);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          medIns2.getEmployee() == emp1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush.");
      deferredAssertTrue(
          medIns1.getEmployee() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (medIns1) not nulled on flush.");
      deferredAssertTrue(
          emp2.getMedicalInsurance() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (emp2) not nulled on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (IEmployee) pm.getObjectById(emp1Oid);
      emp2 = (IEmployee) pm.getObjectById(emp2Oid);
      medIns1 = (IMedicalInsurance) pm.getObjectById(medIns1Oid);
      medIns2 = (IMedicalInsurance) pm.getObjectById(medIns2Oid);
      deferredAssertTrue(
          medIns2.getEmployee() == emp1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set in new pm.");
      deferredAssertTrue(
          medIns1.getEmployee() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (medIns1) not nulled in new pm.");
      deferredAssertTrue(
          emp2.getMedicalInsurance() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (emp2) not nulled in new pm.");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  public void testSetToNullFromMappedSide() {
    testMethod = "testSetToNullFromMappedSide";
    if (isTestToBePerformed) {

      // Set relationship
      medIns1.setEmployee(null);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          emp1.getMedicalInsurance() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (IEmployee) pm.getObjectById(emp1Oid);
      deferredAssertTrue(
          emp1.getMedicalInsurance() == null,
          ASSERTION_FAILED + testMethod,
          "In new transaction, postcondition is false; "
              + "other side of relationship is not set.");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  public void testSetToNullFromMappedBySide() {
    testMethod = "testSetToNullFromMappedBySide";
    if (isTestToBePerformed) {

      // Set relationship
      emp1.setMedicalInsurance(null);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          medIns1.getEmployee() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      // emp1 = (IEmployee)pm.getObjectById(emp1Oid);
      medIns1 = (IMedicalInsurance) pm.getObjectById(medIns1Oid);
      deferredAssertTrue(
          medIns1.getEmployee() == null,
          ASSERTION_FAILED + testMethod,
          "In new transaction, postcondition is false; "
              + "other side of relationship is not set.");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  public void testSetToNewFromMappedSide() {
    testMethod = "testSetToNewFromMappedSide";
    if (isTestToBePerformed) {

      // Set relationship
      IEmployee empNew =
          new FullTimeEmployee(99, "Matthew", "", "Adams", new Date(0L), new Date(10000L), 125000);
      pm.makePersistent(empNew);
      medIns1.setEmployee(empNew);
      Object empNewOid = pm.getObjectId(empNew);
      pm.flush();

      assertFalse(
          testMethod
              + ": Test aborted, precondition is false; "
              + "expected empNewOid to be non-null",
          empNewOid == null);

      // Postcondition
      deferredAssertTrue(
          empNew.getMedicalInsurance() == medIns1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      deferredAssertTrue(
          emp1.getMedicalInsurance() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (emp1) not nulled on flush");

      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (IEmployee) pm.getObjectById(emp1Oid);
      empNew = (IEmployee) pm.getObjectById(empNewOid);
      medIns1 = (IMedicalInsurance) pm.getObjectById(medIns1Oid);
      deferredAssertTrue(
          empNew.getMedicalInsurance() == medIns1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set in new pm");
      deferredAssertTrue(
          emp1.getMedicalInsurance() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (emp1) not nulled in new pm");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  public void testSetToNewFromMappedBySide() {
    testMethod = "testSetToNewFromMappedBySide";
    if (isTestToBePerformed) {

      // Set relationship
      IMedicalInsurance medInsNew = new MedicalInsurance(99L, "Ameriblast", "B");
      pm.makePersistent(medInsNew);
      emp1.setMedicalInsurance(medInsNew);
      Object medInsNewOid = pm.getObjectId(medInsNew);
      pm.flush();

      assertFalse(
          testMethod
              + ": Test aborted, precondition is false; "
              + "expected medInsNewOid to be non-null",
          medInsNewOid == null);

      // Postcondition
      deferredAssertTrue(
          medInsNew.getEmployee() == emp1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush.");
      deferredAssertTrue(
          medIns1.getEmployee() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (medIns1) not nulled on flush.");

      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (IEmployee) pm.getObjectById(emp1Oid);
      medInsNew = (IMedicalInsurance) pm.getObjectById(medInsNewOid);
      medIns1 = (IMedicalInsurance) pm.getObjectById(medIns1Oid);
      deferredAssertTrue(
          medInsNew.getEmployee() == emp1,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set in new pm");
      deferredAssertTrue(
          medIns1.getEmployee() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "previous relationship (medIns1) not nulled in new pm");
      pm.currentTransaction().commit();

      failOnError();
    }
  }
  /** */
  public void testDeleteFromMappedSide() {
    testMethod = "testDeleteFromMappedSide";
    if (isTestToBePerformed) {
      // Set relationship
      pm.deletePersistent(medIns1);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          emp1.getMedicalInsurance() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      emp1 = (IEmployee) pm.getObjectById(emp1Oid);
      deferredAssertTrue(
          emp1.getMedicalInsurance() == null,
          ASSERTION_FAILED + testMethod,
          "In new transaction, postcondition is false; "
              + "other side of relationship is not set.");
      pm.currentTransaction().commit();

      failOnError();
    }
  }

  /** */
  public void testDeleteFromMappedBySide() {
    testMethod = "testDeleteFromMappedBySide";
    if (isTestToBePerformed) {
      // Set relationship
      pm.deletePersistent(emp1);
      pm.flush();

      // Postcondition
      deferredAssertTrue(
          medIns1.getEmployee() == null,
          ASSERTION_FAILED + testMethod,
          "Postcondition is false; " + "other side of relationship not set on flush");
      pm.currentTransaction().commit();
      cleanupPM();
      getPM();

      pm.currentTransaction().begin();
      medIns1 = (IMedicalInsurance) pm.getObjectById(medIns1Oid);
      deferredAssertTrue(
          medIns1.getEmployee() == null,
          ASSERTION_FAILED + testMethod,
          "In new transaction, postcondition is false; "
              + "other side of relationship is not set.");
      pm.currentTransaction().commit();

      failOnError();
    }
  }
}
