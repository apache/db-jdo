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
import java.util.HashSet;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOUserException;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.instancecallbacks.InstanceCallbackClass;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> No Access To Fields After Predelete <br>
 * <B>Keywords:</B> instancecallbacks <br>
 * <B>Assertion ID:</B> A10.4-3. <br>
 * <B>Assertion Description: </B> Access to field values after a call to <code>jdoPreDelete()</code>
 * of a class implementing <code>InstanceCallbacks</code> are disallowed.
 */

/*
 *  Delete a persistent object and new object.  While the objects are in state persitent-deleted (or persistent-new-deleted)
 *     access persistent fields (except do not reference primary key fields).
 *
 *  Verify the field access results in a JDOUserException.
 */

public class NoAccessToFieldsAfterPredelete extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A10.4-3 (NoAccessToFieldsAfterPredelete) failed";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(NoAccessToFieldsAfterPredelete.class);
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

    InstanceCallbackClass.initializeStaticsForTest();

    t.begin();
    // make intValue outside the range of zero to arraySize-1 to skip most jdoPreDelete() code.
    InstanceCallbackClass a =
        new InstanceCallbackClass(
            "object a", null, InstanceCallbackClass.arraySize, 6.0, (short) -1, '6', null);
    pm.makePersistent(a);
    Object aId = pm.getObjectId(a);
    t.commit();

    t.begin();
    // relocte object.
    try {
      a = (InstanceCallbackClass) pm.getObjectById(aId, true);
    } catch (JDOUserException e) {
      fail(
          ASSERTION_FAILED,
          "NoAccessToFieldsAfterPredelete:  Could not locate persistent object created in previous transaction, got "
              + e);
      return;
    } catch (JDODataStoreException e) {
      fail(
          ASSERTION_FAILED,
          "NoAccessToFieldsAfterPredelete:  Could not locate persistent object created in previous transaction, got "
              + e);
      return;
    }

    pm.deletePersistent(a);
    performAccessFieldTests("Object in state persistent-deleted:  ", a);

    InstanceCallbackClass b =
        new InstanceCallbackClass(
            "object b", null, InstanceCallbackClass.arraySize + 1, 7.0, (short) -1, '7', null);
    pm.makePersistent(b);
    pm.deletePersistent(b);
    performAccessFieldTests("Object in state persistent-new-deleted:  ", b);

    t.rollback();
    pm.close();
    pm = null;
  }

  /** */
  void performAccessFieldTests(String title, InstanceCallbackClass o) {
    try {
      short x1 = o.childToDelete;
      // http://issues.apache.org/jira/browse/JDO-413
      // fail(ASSERTION_FAILED, title + "Accessed persistent short field childToDelete--should have
      // gotten JDOUserException");
    } catch (JDOUserException e) {
      // expected
    }

    try {
      double x2 = o.doubleValue;
      // http://issues.apache.org/jira/browse/JDO-413
      // fail(ASSERTION_FAILED, title + "Accessed persistent double field doubleValue--should have
      // gotten JDOUserException");
    } catch (JDOUserException e) {
      // expected
    }

    try {
      char x3 = o.charValue;
      // http://issues.apache.org/jira/browse/JDO-413
      // fail(ASSERTION_FAILED, title + "Accessed persistent char field charValue--should have
      // gotten JDOUserException");
    } catch (JDOUserException e) {
      // expected
    }

    try {
      String x4 = o.name;
      // http://issues.apache.org/jira/browse/JDO-413
      // fail(ASSERTION_FAILED,title + "Accessed persistent String field name--should have gotten
      // JDOUserException");
    } catch (JDOUserException e) {
      // expected
    }

    try {
      Date x5 = o.timeStamp;
      // http://issues.apache.org/jira/browse/JDO-413
      // fail(ASSERTION_FAILED, title + "Accessed persistent Date field timeStamp--should have
      // gotten JDOUserException");
    } catch (JDOUserException e) {
      // expected
    }

    try {
      HashSet x6 = o.children;
      // http://issues.apache.org/jira/browse/JDO-413
      // fail(ASSERTION_FAILED, title + "Accessed persistent HashSet field Children--should have
      // gotten JDOUserException");
    } catch (JDOUserException e) {
      // expected
    }

    try {
      InstanceCallbackClass x7 = o.nextObj;
      // http://issues.apache.org/jira/browse/JDO-413
      // fail(ASSERTION_FAILED, title + "Accessed persistent InstanceCallbackClass reference field
      // nextObj--should have gotten JDOUserException");
    } catch (JDOUserException e) {
      // expected
    }
  }
}
