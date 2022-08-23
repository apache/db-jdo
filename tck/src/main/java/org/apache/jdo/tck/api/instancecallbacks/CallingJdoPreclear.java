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

package org.apache.jdo.tck.api.instancecallbacks;

import java.util.Calendar;
import java.util.Date;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOUserException;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.instancecallbacks.InstanceCallbackClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Calling Jdo Preclear <br>
 * <B>Keywords:</B> instancecallbacks <br>
 * <B>Assertion ID:</B> A10.3-1. <br>
 * <B>Assertion Description: </B> The <code>jdoPreClear()</code> method of a class implementing
 * <code>InstanceCallbacks</code> is called before the values in the instance are cleared. (This
 * happens during the state transition to hollow.)
 */

/*
 *   Create a new object, reference an existing object and
 *   modify an existing object and then commit.
 *
 *   Check that each instance contains the values set into
 *   the persistent attributes prior to commit.
 */

public class CallingJdoPreclear extends TestParts {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A10.3-1 (CallingJdoPreclear) failed";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(CallingJdoPreclear.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(InstanceCallbackClass.class);
  }

  /** */
  public void test() {
    pm = getPM();
    Transaction t = pm.currentTransaction();
    t.setRetainValues(false); // instances transit'n to hollow after commit

    InstanceCallbackClass.initializeStaticsForTest();

    t.begin();
    Calendar cal = Calendar.getInstance();
    cal.set(1999, 1, 15, 12, 0);
    Date createTime = cal.getTime();
    cal.set(2002, 1, 15, 12, 0);
    Date laterDate = cal.getTime();
    InstanceCallbackClass secondaryObj =
        new InstanceCallbackClass("secondaryObj", createTime, 2, 2.2, (short) -20, '2', null);
    InstanceCallbackClass primaryObj =
        new InstanceCallbackClass("primaryObj", laterDate, 1, 1.1, (short) -10, '1', secondaryObj);
    pm.makePersistent(primaryObj);
    pm.makePersistent(secondaryObj);
    Object secondaryObjId = pm.getObjectId(secondaryObj);
    Object primaryObjId = pm.getObjectId(primaryObj);
    t.commit();

    InstanceCallbackClass.performPreClearTests = true;
    t.setOptimistic(false);
    t.begin();
    try {
      primaryObj = (InstanceCallbackClass) pm.getObjectById(primaryObjId, true);
      pm.retrieve(primaryObj); // load fields (make it persistent-clean)

    } catch (JDOUserException e) {
      fail(
          ASSERTION_FAILED,
          "Failed to find primaryObj created in "
              + "previous transaction.  Got JDOUserException "
              + e);
      return;
    } catch (JDODataStoreException e) {
      fail(
          ASSERTION_FAILED,
          "Failed to find primaryObj created in "
              + "previous transaction.  Got JDODataStoreException "
              + e);
      return;
    }

    secondaryObj = primaryObj.nextObj;
    if (secondaryObj == null) {
      fail(
          ASSERTION_FAILED,
          "Failed to find secondaryObj created in "
              + "previous transaction using reference from primaryObj.");
      return;
    }
    pm.retrieve(secondaryObj);

    // primaryObj contains one child;  secondaryObj contains none
    primaryObj.addChild(secondaryObj); // primaryObj is now dirty

    cal.set(2005, 6, 28, 0, 0);
    Date stillLaterDate = cal.getTime();
    InstanceCallbackClass ternaryObj =
        new InstanceCallbackClass("ternaryObj", stillLaterDate, 3, 3.3, (short) -30, '3', null);
    pm.makePersistent(ternaryObj);
    ternaryObj.addChild(primaryObj);
    t.commit();

    // verify attributes in what was persistent-clean object--secondaryObj
    checkFieldValues(
        ASSERTION_FAILED,
        "jdoPreClear attribute access:  ",
        2,
        "secondaryObj",
        createTime,
        2.2,
        (short) -20,
        '2');

    // verify attributes in what was persistent-dirty object--primaryObj
    checkFieldValues(
        ASSERTION_FAILED,
        "jdoPreClear attribute access:  ",
        1,
        "primaryObj",
        laterDate,
        1.1,
        (short) -10,
        '1');

    // verify attributes in what was persistent-new object--ternaryObj
    checkFieldValues(
        ASSERTION_FAILED,
        "jdoPreClear attribute access:  ",
        3,
        "ternaryObj",
        stillLaterDate,
        3.3,
        (short) -30,
        '3');
    pm.close();
    pm = null;
    InstanceCallbackClass.performPreClearTests = false;
  }
}
