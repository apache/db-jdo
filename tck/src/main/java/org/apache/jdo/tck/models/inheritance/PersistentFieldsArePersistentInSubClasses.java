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

package org.apache.jdo.tck.models.inheritance;

import java.util.Iterator;
import javax.jdo.Extent;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.inheritance.AllPersist4;
import org.apache.jdo.tck.pc.inheritance.Constants;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Persistent Fields Are Persistent in SubClasses <br>
 * <B>Keywords:</B> inheritance <br>
 * <B>Assertion ID:</B> A6.5-4. <br>
 * <B>Assertion Description: </B> Fields identified as persistent in persistence-capable classes
 * will be persistent in subclasses.
 */
public class PersistentFieldsArePersistentInSubClasses extends TestParts {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A6.5-4 (PersistentFieldsArePersistentInSubClasses) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(PersistentFieldsArePersistentInSubClasses.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(AllPersist4.class);
  }

  /** */
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
      AllPersist4 refa =
          new AllPersist4(
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

      refa.secondObj =
          new AllPersist4(
              Constants.intA_V[2],
              Constants.doubleB_V[2],
              Constants.intB_V[2],
              Constants.charC_V[2],
              Constants.booleanD_V[2],
              Constants.floatE_V[2],
              Constants.shortF_V[2],
              Constants.shortG_V[2],
              Constants.intH_V[2]);
      TestParts.secondObj_V[1] = refa.secondObj;
      refa.thirdObj =
          new AllPersist4(
              Constants.intA_V[3],
              Constants.doubleB_V[3],
              Constants.intB_V[3],
              Constants.charC_V[3],
              Constants.booleanD_V[3],
              Constants.floatE_V[3],
              Constants.shortF_V[3],
              Constants.shortG_V[3],
              Constants.intH_V[3]);
      TestParts.thirdObj_V[1] = refa.thirdObj;
      pm.makePersistent(refa.thirdObj);
      Object objPtrB = pm.getObjectId(refa.thirdObj);
      refa.fourthObj =
          new AllPersist4(
              Constants.intA_V[4],
              Constants.doubleB_V[4],
              Constants.intB_V[4],
              Constants.charC_V[4],
              Constants.booleanD_V[4],
              Constants.floatE_V[4],
              Constants.shortF_V[4],
              Constants.shortG_V[4],
              Constants.intH_V[4]);
      TestParts.fourthObj_V[1] = refa.fourthObj;
      t.commit();

      t.begin();
      AllPersist4 a = null;
      AllPersist4 b = null;

      try { // retrieve object created in previous transaction & store in value array for later
        // comparison
        TestParts.thirdObj_V[1] = (AllPersist4) pm.getObjectById(objPtrB, true);
      } catch (JDOUserException e) {
        // could not locate persistent object created in previous transaction
        fail(ASSERTION_FAILED, "JDOUserException " + e + " could not reference thirdObj.");
      }

      try { // retrieve object created in previous transaction
        a = (AllPersist4) pm.getObjectById(objPtrA, true);
        checkPersistentAreCorrect(
            ASSERTION_FAILED,
            persistentAfterCommit,
            1,
            a.doubleB,
            a.intB,
            a.shortF,
            a.thirdObj,
            a.intH);

        // verify referenced persistent object contains correct values
        b = a.thirdObj;
        if (b != null) { // if previous error caused b to be null, then these tests cannot be
          // performed.
          checkPersistentAreCorrect(
              ASSERTION_FAILED,
              persistentAfterCommit,
              3,
              b.doubleB,
              b.intB,
              b.shortF,
              b.thirdObj,
              b.intH);
        }
      } catch (JDOUserException e) {
        // could not locate persistent object created in previous transaction
        fail(
            ASSERTION_FAILED,
            "JDOUserException " + e + " could not reference previously created object.");
      }

      // set in new values
      a.intA = Constants.intA_V[5];
      a.charC = Constants.charC_V[5];
      a.booleanD = Constants.booleanD_V[5];
      a.shortG = Constants.shortG_V[5];
      a.fourthObj =
          new AllPersist4(
              Constants.intA_V[6],
              Constants.doubleB_V[6],
              Constants.intB_V[6],
              Constants.charC_V[6],
              Constants.booleanD_V[6],
              Constants.floatE_V[6],
              Constants.shortF_V[6],
              Constants.shortG_V[6],
              Constants.intH_V[6]);
      TestParts.fourthObj_V[5] = a.fourthObj;
      a.floatE = Constants.floatE_V[5];
      a.secondObj = null;
      a.doubleB = Constants.doubleB_V[5];
      a.intB = Constants.intB_V[5];
      a.shortF = Constants.shortF_V[5];
      a.thirdObj = null;
      a.intH = Constants.intH_V[5];

      b.intA = Constants.intA_V[7];
      b.charC = Constants.charC_V[7];
      b.booleanD = Constants.booleanD_V[7];
      b.shortG = Constants.shortG_V[7];
      b.fourthObj = null;
      b.floatE = Constants.floatE_V[7];
      b.secondObj = null;
      b.doubleB = Constants.doubleB_V[7];
      b.intB = Constants.intB_V[7];
      b.shortF = Constants.shortF_V[7];
      b.thirdObj = null;
      b.intH = Constants.intH_V[7];

      // create new objects and make persistent
      AllPersist4 c =
          new AllPersist4(
              Constants.intA_V[8],
              Constants.doubleB_V[8],
              Constants.intB_V[8],
              Constants.charC_V[8],
              Constants.booleanD_V[8],
              Constants.floatE_V[8],
              Constants.shortF_V[8],
              Constants.shortG_V[8],
              Constants.intH_V[8]);
      AllPersist4 d =
          new AllPersist4(
              Constants.intA_V[9],
              Constants.doubleB_V[9],
              Constants.intB_V[9],
              Constants.charC_V[9],
              Constants.booleanD_V[9],
              Constants.floatE_V[9],
              Constants.shortF_V[9],
              Constants.shortG_V[9],
              Constants.intH_V[9]);
      c.thirdObj = d;
      c.fourthObj = d;
      TestParts.thirdObj_V[8] = d;
      TestParts.fourthObj_V[8] = d;
      pm.makePersistent(c);

      // change values of newly persistent object
      c.intA = Constants.intA_V[10];
      c.charC = Constants.charC_V[10];
      c.booleanD = Constants.booleanD_V[10];
      c.shortG = Constants.shortG_V[10];
      c.fourthObj = null;
      c.floatE = Constants.floatE_V[10];
      c.secondObj = null;
      c.doubleB = Constants.doubleB_V[10];
      c.intB = Constants.intB_V[10];
      c.shortF = Constants.shortF_V[10];
      c.thirdObj = null;
      c.intH = Constants.intH_V[10];

      t.rollback();

      // verify objects revert back to transient after rollback
      checkPersistentAreCorrect(
          ASSERTION_FAILED,
          transientAfterRollback,
          8,
          c.doubleB,
          c.intB,
          c.shortF,
          c.thirdObj,
          c.intH);

      t.begin();

      // verify rollback lost all persistent changes.
      try { // retrieve object created in previous transaction & store in value array for later
        // comparison
        TestParts.thirdObj_V[1] = (AllPersist4) pm.getObjectById(objPtrB, true);
      } catch (JDOUserException e) {
        // could not locate persistent object created in previous transaction
        fail(ASSERTION_FAILED, "JDOUserException " + e + " could not reference thirdObj.");
      }

      try { // retrieve object created in previous transaction
        a = (AllPersist4) pm.getObjectById(objPtrA, true);
        checkPersistentAreCorrect(
            ASSERTION_FAILED,
            persistentAfterRollback,
            1,
            a.doubleB,
            a.intB,
            a.shortF,
            a.thirdObj,
            a.intH);
        b = a.thirdObj;
        if (b != null) { // if previous error caused b to be null, then these tests cannot be
          // performed.
          checkPersistentAreCorrect(
              ASSERTION_FAILED,
              persistentAfterRollback,
              3,
              b.doubleB,
              b.intB,
              b.shortF,
              b.thirdObj,
              b.intH);
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
    AllPersist4 a = new AllPersist4(0, 0.0, 0, '0', false, 0.0f, (short) 0, (short) 0, 0);
    pm.makePersistent(a); // guarantee the class is registered; this will be removed
    Extent e = pm.getExtent(AllPersist4.class, true);
    Iterator i = e.iterator();
    while (i.hasNext()) {
      pm.deletePersistent(i.next());
    }
  }
}
