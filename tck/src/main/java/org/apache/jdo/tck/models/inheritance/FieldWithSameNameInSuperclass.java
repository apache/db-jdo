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

package org.apache.jdo.tck.models.inheritance;

import javax.jdo.Extent;
import javax.jdo.JDOException;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.inheritance.Constants;
import org.apache.jdo.tck.pc.inheritance.FieldSameName4;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Field With the Same Name as a Field in a Superclass <br>
 * <B>Keywords:</B> inheritance <br>
 * <B>Assertion ID:</B> A6.5-7. <br>
 * <B>Assertion Description: </B> A class might define a new field with the same name as the field
 * declared in the superclass, and might define it to be different (persistent or not) from the
 * inherited field. But Java treats the declared field as a different field from the inherited
 * field, so there is no conflict.
 */
public class FieldWithSameNameInSuperclass extends TestParts {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A6.5-7 (FieldWithSameNameInSuperclass) failed: ";

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(FieldSameName4.class);
  }

  /** */
  @Test
  public void test() {
    pm = getPM();

    runTest(pm);

    pm.close();
    pm = null;
  }

  /** */
  void runTest(PersistenceManager pm) {
    Transaction t = pm.currentTransaction();
    try {
      t.setRestoreValues(true);

      t.begin();
      FieldSameName4 refa =
          new FieldSameName4(
              Constants.intA_V[1],
              Constants.doubleB_V[1],
              Constants.intB_V[1],
              Constants.charC_V[1],
              Constants.booleanD_V[1],
              Constants.floatE_V[1],
              Constants.shortF_V[1],
              Constants.shortG_V[1],
              Constants.intH_V[1]);
      pm.makePersistent(refa);
      Object objPtrA = pm.getObjectId(refa);

      refa.setSecondObj(
          new FieldSameName4(
              Constants.intA_V[2],
              Constants.doubleB_V[2],
              Constants.intB_V[2],
              Constants.charC_V[2],
              Constants.booleanD_V[2],
              Constants.floatE_V[2],
              Constants.shortF_V[2],
              Constants.shortG_V[2],
              Constants.intH_V[2]));
      TestParts.secondObj_V[1] = refa.getSecondObj();
      refa.setThirdObj(
          new FieldSameName4(
              Constants.intA_V[3],
              Constants.doubleB_V[3],
              Constants.intB_V[3],
              Constants.charC_V[3],
              Constants.booleanD_V[3],
              Constants.floatE_V[3],
              Constants.shortF_V[3],
              Constants.shortG_V[3],
              Constants.intH_V[3]));
      TestParts.thirdObj_V[1] = refa.getThirdObj();
      pm.makePersistent(TestParts.thirdObj_V[1]);
      Object objPtrB = pm.getObjectId(TestParts.thirdObj_V[1]);
      refa.setFourthObj(
          new FieldSameName4(
              Constants.intA_V[4],
              Constants.doubleB_V[4],
              Constants.intB_V[4],
              Constants.charC_V[4],
              Constants.booleanD_V[4],
              Constants.floatE_V[4],
              Constants.shortF_V[4],
              Constants.shortG_V[4],
              Constants.intH_V[4]));
      TestParts.fourthObj_V[1] = refa.getFourthObj();
      try {
        t.commit();
      } catch (JDOException e) {
        Object o = e.getFailedObject();
        String cname = o == null ? "null" : o.getClass().getName();
        fail(
            ASSERTION_FAILED,
            "Exception thrown, failed object class is " + cname + " exception is " + e);
      }

      t.begin();
      FieldSameName4 a = null;
      FieldSameName4 b = null;

      try { // retrieve object created in previous transaction & store in value array for later
        // comparison
        TestParts.thirdObj_V[1] = pm.getObjectById(objPtrB, true);
      } catch (JDOUserException e) {
        // could not locate persistent object created in previous
        // transaction
        fail(ASSERTION_FAILED, "JDOUserException " + e + " could not reference thirdObj.");
      }

      try { // retrieve object created in previous transaction
        a = (FieldSameName4) pm.getObjectById(objPtrA, true);
        checkPersistentAreCorrect(
            ASSERTION_FAILED,
            PERSISTENT_AFTER_COMMIT,
            1,
            a.getDoubleB(),
            a.getIntB(),
            a.getShortF(),
            a.getThirdObj(),
            a.getIntH());

        // verify referenced persistent object contains correct values
        b = a.getThirdObj();
        if (b != null) { // if previous error caused b to be null, then these tests cannot be
          // performed.
          checkPersistentAreCorrect(
              ASSERTION_FAILED,
              PERSISTENT_AFTER_COMMIT,
              3,
              b.getDoubleB(),
              b.getIntB(),
              b.getShortF(),
              b.getThirdObj(),
              b.getIntH());
        }
      } catch (JDOUserException e) {
        // could not locate persistent object created in previous transaction
        fail(
            ASSERTION_FAILED,
            "JDOUserException " + e + " could not reference previously created object.");
      }

      // set in new values
      a.setIntA(Constants.intA_V[5]);
      a.setCharC(Constants.charC_V[5]);
      a.setBooleanD(Constants.booleanD_V[5]);
      a.setShortG(Constants.shortG_V[5]);
      FieldSameName4 fourth =
          new FieldSameName4(
              Constants.intA_V[6],
              Constants.doubleB_V[6],
              Constants.intB_V[6],
              Constants.charC_V[6],
              Constants.booleanD_V[6],
              Constants.floatE_V[6],
              Constants.shortF_V[6],
              Constants.shortG_V[6],
              Constants.intH_V[6]);
      a.setFourthObj(fourth);
      a.setFloatE(Constants.floatE_V[5]);
      a.setSecondObj(null);
      a.setDoubleB(Constants.doubleB_V[5]);
      a.setIntB(Constants.intB_V[5]);
      a.setShortF(Constants.shortF_V[5]);
      a.setThirdObj(null);
      a.setIntH(Constants.intH_V[5]);

      b.setIntA(Constants.intA_V[7]);
      b.setCharC(Constants.charC_V[7]);
      b.setBooleanD(Constants.booleanD_V[7]);
      b.setShortG(Constants.shortG_V[7]);
      b.setFourthObj(null);
      b.setFloatE(Constants.floatE_V[7]);
      b.setSecondObj(null);
      b.setDoubleB(Constants.doubleB_V[7]);
      b.setIntB(Constants.intB_V[7]);
      b.setShortF(Constants.shortF_V[7]);
      b.setThirdObj(null);
      b.setIntH(Constants.intH_V[7]);

      // create new objects and make persistent
      FieldSameName4 c =
          new FieldSameName4(
              Constants.intA_V[8],
              Constants.doubleB_V[8],
              Constants.intB_V[8],
              Constants.charC_V[8],
              Constants.booleanD_V[8],
              Constants.floatE_V[8],
              Constants.shortF_V[8],
              Constants.shortG_V[8],
              Constants.intH_V[8]);
      FieldSameName4 d =
          new FieldSameName4(
              Constants.intA_V[9],
              Constants.doubleB_V[9],
              Constants.intB_V[9],
              Constants.charC_V[9],
              Constants.booleanD_V[9],
              Constants.floatE_V[9],
              Constants.shortF_V[9],
              Constants.shortG_V[9],
              Constants.intH_V[9]);
      c.setThirdObj(d);
      c.setFourthObj(d);
      TestParts.thirdObj_V[8] = d;
      TestParts.fourthObj_V[8] = d;
      pm.makePersistent(c);

      // change values of newly persistent object
      c.setIntA(Constants.intA_V[10]);
      c.setCharC(Constants.charC_V[10]);
      c.setBooleanD(Constants.booleanD_V[10]);
      c.setShortG(Constants.shortG_V[10]);
      c.setFourthObj(null);
      c.setFloatE(Constants.floatE_V[10]);
      c.setSecondObj(null);
      c.setDoubleB(Constants.doubleB_V[10]);
      c.setIntB(Constants.intB_V[10]);
      c.setShortF(Constants.shortF_V[10]);
      c.setThirdObj(null);
      c.setIntH(Constants.intH_V[10]);

      t.rollback();

      // verify objects revert back to transient after rollback
      checkPersistentAreCorrect(
          ASSERTION_FAILED,
          TRANSIENT_AFTER_ROLLBACK,
          8,
          c.getDoubleB(),
          c.getIntB(),
          c.getShortF(),
          c.getThirdObj(),
          c.getIntH());
      checkTransactionalAreCorrect(
          ASSERTION_FAILED, TRANSIENT_AFTER_ROLLBACK, 8, c.getFloatE(), c.getSecondObj());
      checkNonpersistentAreCorrect(
          ASSERTION_FAILED,
          TRANSIENT_AFTER_ROLLBACK,
          10,
          c.getIntA(),
          c.getCharC(),
          c.getBooleanD(),
          c.getShortG(),
          c.getFourthObj());

      t.begin();

      // verify rollback lost all persistent changes.
      try { // retrieve object created in previous transaction & store in value array for later
        // comparison
        TestParts.thirdObj_V[1] = pm.getObjectById(objPtrB, true);
      } catch (JDOUserException e) {
        // could not locate persistent object created in previous transaction
        fail(ASSERTION_FAILED, "JDOUserException " + e + " could not reference thirdObj.");
      }

      try { // retrieve object created in previous transaction
        a = (FieldSameName4) pm.getObjectById(objPtrA, true);
        checkPersistentAreCorrect(
            ASSERTION_FAILED,
            PERSISTENT_AFTER_ROLLBACK,
            1,
            a.getDoubleB(),
            a.getIntB(),
            a.getShortF(),
            a.getThirdObj(),
            a.getIntH());
        b = a.getThirdObj();
        if (b != null) { // if previous error caused b to be null, then these tests cannot be
          // performed.
          checkPersistentAreCorrect(
              ASSERTION_FAILED,
              PERSISTENT_AFTER_ROLLBACK,
              3,
              b.getDoubleB(),
              b.getIntB(),
              b.getShortF(),
              b.getThirdObj(),
              b.getIntH());
        }
      } catch (JDOUserException e) {
        // could not locate persistent object created in previous transaction
        fail(
            ASSERTION_FAILED,
            "JDOUserException " + e + " could not reference previously created object.");
      }

      t.rollback();
      t = null;
    } finally {
      if ((t != null) && t.isActive()) t.rollback();
    }
  }

  void removeAllInstances(PersistenceManager pm) {
    Extent<FieldSameName4> e = pm.getExtent(FieldSameName4.class, true);
    for (FieldSameName4 fieldSameName4 : e) {
      pm.deletePersistent(fieldSameName4);
    }
  }
}
