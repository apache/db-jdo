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

import java.util.Date;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.instancecallbacks.InstanceCallbackClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Access Other Instances In Prestore Method <br>
 * <B>Keywords:</B> instancecallbacks <br>
 * <B>Assertion ID:</B> A10.2-3. <br>
 * <B>Assertion Description: </B> The context in which a call is made to <code>jdoPreStore()</code>
 * allows access to the <code>PersistenceManager</code> and other persistent JDO instances.
 */

/*
 *   Within jdoPreStore(), locate the PersistenceManager (JDOHelper.getPersistenceManager()) and use it
 *   to call currentTransaction().  Use the Transaction to call isActive() and verify active.  These operations
 *   are done to help verify that we had access to a usable PersistenceManager.
 *
 *   Create objects.
 *   Within jdoPreStore(), capture information about objects referenced.
 *   This includes a String, Date and Collection attribute.
 *   After commit() verify jdoPreStore() called and found what expected.
 */

public class AccessOtherInstancesInPrestore extends TestParts {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A10.2-3 (AccessOtherInstancesInPrestore) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(AccessOtherInstancesInPrestore.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  protected void localSetUp() {
    addTearDownClass(InstanceCallbackClass.class);
  }

  /** */
  public void test() {
    pm = getPM();
    Transaction t = pm.currentTransaction();

    InstanceCallbackClass.initializeStaticsForTest();

    InstanceCallbackClass.performPreStoreTests = true;
    t.begin();
    //  Create primaryObj which has a collection (children) of two objects and references another
    // (nextObj).
    //  None of the other objects have a non-empty collection.  The also all have nextObj set to
    // null.
    Date createTime = new Date();
    InstanceCallbackClass secondaryObj =
        new InstanceCallbackClass("secondaryObj", createTime, 2, 2.0, (short) -1, '2', null);
    InstanceCallbackClass primaryObj =
        new InstanceCallbackClass("primaryObj", createTime, 1, 1.0, (short) 3, '1', secondaryObj);
    InstanceCallbackClass childA =
        new InstanceCallbackClass("childA", createTime, 3, 3.0, (short) -2, '3', null);
    InstanceCallbackClass childB =
        new InstanceCallbackClass("childB", createTime, 4, 4.0, (short) -3, '4', null);
    pm.makePersistent(primaryObj);
    primaryObj.addChild(childA);
    primaryObj.addChild(childB);
    t.commit();

    // check that jdoPreStore provides access to instances for primaryObj
    checkInstances(ASSERTION_FAILED, "jdoStore instance access:  ", 1, "secondaryObj", 2, 7);
    // check that jdoPreStore provides access to PersistenceManager for primaryObj
    checkPMAccess(ASSERTION_FAILED, "jdoPreDelete PersistenceManager access:  ", 1, true);

    // check that jdoPreStore provides access to instances for secondaryObj
    checkInstances(ASSERTION_FAILED, "jdoStore instance access:  ", 2, null, 0, 0);
    // check that jdoPreStore provides access to PersistenceManager for secondaryObj
    checkPMAccess(ASSERTION_FAILED, "jdoPreDelete PersistenceManager access:  ", 2, true);

    // There is no need to check childA or childB.  They are no different than secondaryObj.

    pm.close();
    pm = null;
  }
}
