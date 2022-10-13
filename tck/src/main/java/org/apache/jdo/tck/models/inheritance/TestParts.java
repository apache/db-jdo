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

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.inheritance.Constants;

/**
 * The test data is in arrays, making it easy to change values. The Inheritance test classes refer
 * to the data via array index and the checkXXXX() routines carry an index value--indicating the
 * expected value. If those attributes are references and should be other than null, as is the case
 * with thirdObj, then the test code inserts the reference into the data array.
 *
 * <p>Each test class follows the same pattern: 1. Persistent and transient object are created in
 * the first transaction. 2. A second transaction is started, the first persistent object retrieved
 * and checking is done on its attribute values. 3. A rollback is performed and value comparisions
 * are made on the transient instances that were created. 4. A third transation is started and the
 * first persistent object again retrieved and checking is done on its attribute values.
 */
public abstract class TestParts extends JDO_Test {

  //                                     0     1     2     3     4     5     6     7     8     9
  // 10    11
  static final Object[] secondObj_V = {
    null, null, null, null, null, null, null, null, null, null, null, null
  };
  static final Object[] thirdObj_V = {
    null, null, null, null, null, null, null, null, null, null, null, null
  };
  static final Object[] fourthObj_V = {
    null, null, null, null, null, null, null, null, null, null, null, null
  };

  static final String PERSISTENT_AFTER_COMMIT = "Read object back after committed to Database.  ";
  static final String PERSISTENT_AFTER_ROLLBACK = "Read object back after rollback.  ";
  static final String TRANSIENT_AFTER_ROLLBACK = "Object reverted to transient after rollback.  ";

  /** */
  void checkPersistentAreCorrect(
      String assertion,
      String title,
      int index,
      double doubleB,
      int intB,
      short shortF,
      Object thirdObj,
      int intH) {
    if (doubleB != Constants.doubleB_V[index]) {
      fail(
          assertion,
          title
              + "Persistent attribute doubleB is "
              + doubleB
              + ", it should be "
              + Constants.doubleB_V[index]);
    }
    if (intB != Constants.intB_V[index]) {
      fail(
          assertion,
          title
              + "Persistent attribute intB is "
              + intB
              + ", it should be "
              + Constants.intB_V[index]);
    }
    if (shortF != Constants.shortF_V[index]) {
      fail(
          assertion,
          title
              + "Persistent attribute shortF is "
              + shortF
              + ", it should be "
              + Constants.shortF_V[index]);
    }
    if (thirdObj != TestParts.thirdObj_V[index]) {
      fail(
          assertion,
          title
              + "Persistent attribute thirdObj is "
              + thirdObj
              + ", it should be "
              + TestParts.thirdObj_V[index]);
    }
    if (intH != Constants.intH_V[index]) {
      fail(
          assertion,
          title
              + "Persistent attribute intH is "
              + intH
              + ", it should be "
              + Constants.intH_V[index]);
    }
  }

  /** */
  void checkTransactionalAreCorrect(
      String assertion, String title, int index, float floatE, Object secondObj) {

    if (floatE != Constants.floatE_V[index]) {
      fail(
          assertion,
          title
              + "Transactional attribute floatE is "
              + floatE
              + ", it should be "
              + Constants.floatE_V[index]);
    }
    if (secondObj != TestParts.secondObj_V[index]) {
      fail(
          assertion,
          title
              + "Transactional attribute secondObj is "
              + secondObj
              + ", it should be "
              + TestParts.secondObj_V[index]);
    }
  }

  /** */
  void checkNonpersistentAreCorrect(
      String assertion,
      String title,
      int index,
      int intA,
      char charC,
      boolean booleanD,
      short shortG,
      Object fourthObj) {
    if (intA != Constants.intA_V[index]) {
      fail(
          assertion,
          title
              + "In non-persistent class, attribute intA is "
              + intA
              + ", it should be "
              + Constants.intA_V[index]);
    }
    if (charC != Constants.charC_V[index]) {
      fail(
          assertion,
          title
              + "In non-persistent class, attribute charC is "
              + charC
              + ", it should be "
              + Constants.charC_V[index]);
    }
    if (booleanD != Constants.booleanD_V[index]) {
      fail(
          assertion,
          title
              + "In non-persistent class, attribute booleanD is "
              + booleanD
              + ", it should be "
              + Constants.booleanD_V[index]);
    }
    if (shortG != Constants.shortG_V[index]) {
      fail(
          assertion,
          title
              + "In non-persistent class, attribute shortG is "
              + shortG
              + ", it should be "
              + Constants.shortG_V[index]);
    }
    if (fourthObj != TestParts.fourthObj_V[index]) {
      fail(
          assertion,
          title
              + "In non-persistent class, attribute fourthObj is "
              + fourthObj
              + ", it should be "
              + TestParts.fourthObj_V[index]);
    }
  }
}
