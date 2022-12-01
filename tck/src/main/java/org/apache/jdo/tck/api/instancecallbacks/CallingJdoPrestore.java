/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.api.instancecallbacks;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOUserException;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.instancecallbacks.InstanceCallbackClass;
import org.apache.jdo.tck.pc.instancecallbacks.InstanceCallbackNonPersistFdsClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Calling InstanceCallbacks Prestore Method <br>
 * <B>Keywords:</B> instancecallbacks <br>
 * <B>Assertion ID:</B> A10.2-1. <br>
 * <B>Assertion Description: </B> The <code>jdoPreStore()</code> method of a class implementing
 * <code>InstanceCallbacks</code> is called before the values are stored from the instance to the
 * <code>StateManager</code>.
 */

/*
 *  Use non-managed and transactional values to construct a persistent attribute value to store in database.
 *   Check to see this constructed persistent attribute got stored.
 *
 *  jdoPreStore() should have been called when obj1 and obj2 were created.
 *  jdoPreStore() should have been called again when obj2 was modified.
 *  jdoPreStore() should not be called a second time on obj1 because it was not modified.

 /*
 This test is accomplished in three transactions.
     1)  object1 and object2 are created.  At commit, we expect jdoPreStore()
         to be called on each and calculate values of persistent attributes from non-presistent attributes.
     2)  Retrieve object1 and object2 from the database and verify the persistent attributes
         have values calculated in the jdoPreStore()s called in the previous transaction.
         Change non-persistent fields in each but only change a persistent field in one of them.
     3)  Retrieve object1 and object2 from the database and verify that again jdoPreStore()
         was called for each in the previous transaction.
*/

public class CallingJdoPrestore extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A10.2-1 (CallingJdoPrestore) failed";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(CallingJdoPrestore.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(InstanceCallbackNonPersistFdsClass.class);
  }

  /** */
  public void test() {
    pm = getPM();
    Transaction t = pm.currentTransaction();

    InstanceCallbackClass.initializeStaticsForTest();

    InstanceCallbackClass.performPreStoreTests = true;
    t.begin();
    int expectedIntValue1, expectedIntValue2;
    float expectedFloatValue1, expectedFloatValue2;

    int origIntValue1 = 10, origIntValue2 = 12;
    float origFloatValue1 = 2.5f, origFloatValue2 = -3.0f;

    // create two instances

    InstanceCallbackNonPersistFdsClass obj1 =
        new InstanceCallbackNonPersistFdsClass(origFloatValue1, origIntValue1);
    pm.makePersistent(obj1);
    Object objPtr1 = pm.getObjectId(obj1);
    obj1.setNonPersist(5, (char) 10, .25, (short) 100);
    expectedIntValue1 = obj1.calcIntValue();
    expectedFloatValue1 = obj1.calcFloatValue();

    InstanceCallbackNonPersistFdsClass obj2 =
        new InstanceCallbackNonPersistFdsClass(origFloatValue2, origIntValue2);
    pm.makePersistent(obj2);
    Object objPtr2 = pm.getObjectId(obj2);
    obj2.setNonPersist(2, (char) 125, .1, (short) 750);
    expectedIntValue2 = obj2.calcIntValue();
    expectedFloatValue2 = obj2.calcFloatValue();

    t.commit();

    t.begin();
    // read both objects back in to determine persistent state is as expected.
    try {
      obj1 = (InstanceCallbackNonPersistFdsClass) pm.getObjectById(objPtr1, true);
      checkValues(
          "Object1 created and read back in:  ", origIntValue1, expectedIntValue1, obj1.intValue);
      checkValues(
          "Object1 created and read back in:  ",
          origFloatValue1,
          expectedFloatValue1,
          obj1.floatValue);
      origIntValue1 = obj1.intValue;
      origFloatValue1 = obj1.floatValue;
      obj1.setNonManaged(-1, (char) 62);
      expectedIntValue1 = obj1.calcIntValue();
      expectedFloatValue1 = obj1.calcFloatValue();
    } catch (JDOUserException | JDODataStoreException e) {
      // could not locate persistent object created in previous transaction
      fail(
          ASSERTION_FAILED,
          "CallingJdoPrestore: Failed to find object obj1 created in previous transaction, got "
              + e);
      return;
    }

    try {
      obj2 = (InstanceCallbackNonPersistFdsClass) pm.getObjectById(objPtr2, true);
      checkValues(
          "Object2 created and read back in:  ", origIntValue2, expectedIntValue2, obj2.intValue);
      checkValues(
          "Object2 created and read back in:  ",
          origFloatValue2,
          expectedFloatValue2,
          obj2.floatValue);
      origIntValue2 = obj2.intValue;
      origFloatValue2 = obj2.floatValue;
      obj2.setNonPersist(12, (char) 30, 5.0, (short) 137);
      expectedIntValue2 = obj2.calcIntValue();
      expectedFloatValue2 = obj2.calcFloatValue();

      // change obj2 making it persistent dirty (leave obj1 persistent clean).
      obj2.incrementIntValue();
    } catch (JDOUserException | JDODataStoreException e) {
      // could not locate persistent object created in previous transaction
      fail(
          ASSERTION_FAILED,
          "CallingJdoPrestore: Failed to find object obj2 created in previous transaction, got "
              + e);
      return;
    }

    t.commit();

    t.begin();
    // read both objects back in to determine persistent state is as expected.
    try {
      // obj1 not made dirty, so jdoPreStore() should not have been called.
      obj1 = (InstanceCallbackNonPersistFdsClass) pm.getObjectById(objPtr1, true);
      checkValues(
          "Object1, did not expect jdoPreStore to be called:  ",
          origIntValue1,
          origIntValue1,
          obj1.intValue);
      checkValues(
          "Object1, did not expect jdoPreStore to be called:  ",
          origFloatValue1,
          origFloatValue1,
          obj1.floatValue);
    } catch (JDOUserException | JDODataStoreException e) {
      // could not locate persistent object created in previous transaction
      fail(
          ASSERTION_FAILED,
          "CallingJdoPrestore: Failed to find object obj1 created in previous transaction, got "
              + e);
      return;
    }

    try {
      obj2 = (InstanceCallbackNonPersistFdsClass) pm.getObjectById(objPtr2, true);
      checkValues(
          "Object2, expected jdoPreStore() to be called:  ",
          origIntValue2,
          expectedIntValue2,
          obj2.intValue);
      checkValues(
          "Object2, expected jdoPreStore() to be called:  ",
          origFloatValue2,
          expectedFloatValue2,
          obj2.floatValue);
    } catch (JDOUserException | JDODataStoreException e) {
      // could not locate persistent object created in previous transaction
      fail(
          ASSERTION_FAILED,
          "CallingJdoPrestore: Failed to find object obj2 created in previous transaction, got "
              + e);
      return;
    }
    t.rollback();
    pm.close();
    pm = null;
  }

  /** */
  void checkValues(String title, int origIntValue, int expectedIntValue, int intValue) {
    if (expectedIntValue != intValue) {
      fail(
          ASSERTION_FAILED,
          title
              + "Original persistent intValue is "
              + origIntValue
              + ", expected "
              + expectedIntValue
              + ".  Instead intValue is "
              + intValue);
    }
  }

  /** */
  void checkValues(String title, float origFloatValue, float expectedFloatValue, float floatValue) {
    if (expectedFloatValue != floatValue) {
      fail(
          ASSERTION_FAILED,
          title
              + "Original persistent floatValue is "
              + origFloatValue
              + ", expected "
              + expectedFloatValue
              + ".  Instead floatVal is "
              + floatValue);
    }
  }
}
