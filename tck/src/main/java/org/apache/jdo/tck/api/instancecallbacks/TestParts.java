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
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.instancecallbacks.InstanceCallbackClass;

public abstract class TestParts extends JDO_Test {

  /** */
  public TestParts() {}

  /** */
  void checkInstances(
      String assertion,
      String label,
      int intValue,
      String capturedNextObjName,
      int numberOfChildren,
      int sumOfChildrenIntValue) {
    if (InstanceCallbackClass.processedIndex[intValue] != true) {
      fail(assertion, label + "Callback never made on object with intValue = " + intValue);
    }
    if (capturedNextObjName != null
        && InstanceCallbackClass.capturedNextObjName[intValue] == null) {
      fail(
          assertion,
          label
              + "nextObj attribute for object with intValue = "
              + intValue
              + " should not have been null.");
    } else if (capturedNextObjName == null
        && InstanceCallbackClass.capturedNextObjName[intValue] != null) {
      fail(
          assertion,
          label
              + "nextObj attribute for object with intValue = "
              + intValue
              + " should  have been null.");
    } else if (capturedNextObjName != null
        && !InstanceCallbackClass.capturedNextObjName[intValue].equals(capturedNextObjName)) {
      fail(
          assertion,
          label
              + "nextObj.name attribute for object with intValue = "
              + intValue
              + " should  have been \""
              + capturedNextObjName
              + "\".  It was \""
              + InstanceCallbackClass.capturedNextObjName[intValue]
              + "\" instead.");
    }

    if (InstanceCallbackClass.numberOfChildren[intValue] != numberOfChildren) {
      fail(
          assertion,
          label
              + "Number of instances in attribute children for object with intValue = "
              + intValue
              + " should  have been "
              + numberOfChildren
              + ".  It was "
              + InstanceCallbackClass.numberOfChildren[intValue]
              + " instead.");
    }

    if (InstanceCallbackClass.sumOfChildrenIntValue[intValue] != sumOfChildrenIntValue) {
      fail(
          assertion,
          label
              + "Sum of intValue of instances in attribute children for object with intValue = "
              + intValue
              + " should have been "
              + sumOfChildrenIntValue
              + ".  It was "
              + InstanceCallbackClass.sumOfChildrenIntValue[intValue]
              + " instead.");
    }
  }

  /** */
  void checkPMAccess(String assertion, String label, int intValue, boolean transactionActive) {
    if (InstanceCallbackClass.processedIndex[intValue] != true) {
      fail(assertion, label + "Callback never made on object with intValue = " + intValue);
      return;
    }
    // Only  verify isActive() returned true for the object if transactionActive is true
    if (transactionActive && InstanceCallbackClass.transactionActive[intValue] != true) {
      fail(assertion, label + "PersistenceManager.currentTransaction.isAcive() returned false");
    }
  }

  /** The attributes are: label, name, date, intValue, doubleValue, childToDelete, charValue */
  void checkFieldValues(
      String assertion,
      String label,
      int intValue,
      String name,
      Date timeStamp,
      double doubleValue,
      short childToDelete,
      char charValue) {
    if (InstanceCallbackClass.processedIndex[intValue] != true) {
      fail(assertion, label + "Callback never made on object with intValue = " + intValue);
      return;
    }

    if (!InstanceCallbackClass.capturedName[intValue].equals(name)) {
      fail(
          assertion,
          label
              + "name attribute for object with intValue = "
              + intValue
              + " should be \""
              + name
              + "\".  It was \""
              + InstanceCallbackClass.capturedName[intValue]
              + "\" instead.");
    }

    if (!InstanceCallbackClass.capturedTimeStamp[intValue].equals(timeStamp)) {
      fail(
          assertion,
          label
              + "timeStamp attribute for object with intValue = "
              + intValue
              + " should be "
              + timeStamp
              + ".  It was "
              + InstanceCallbackClass.capturedTimeStamp[intValue]
              + " instead.");
    }

    if (InstanceCallbackClass.capturedDoubleValue[intValue] != doubleValue) {
      fail(
          assertion,
          label
              + "doubleValue attribute for object with intValue = "
              + intValue
              + " should be "
              + doubleValue
              + ".  It was "
              + InstanceCallbackClass.capturedDoubleValue[intValue]
              + " instead.");
    }

    if (InstanceCallbackClass.capturedCharValue[intValue] != charValue) {
      fail(
          assertion,
          label
              + "charValue attribute for object with intValue = "
              + intValue
              + " should be "
              + charValue
              + ".  It was "
              + InstanceCallbackClass.capturedCharValue[intValue]
              + " instead.");
    }

    if (InstanceCallbackClass.capturedChildToDelete[intValue] != childToDelete) {
      fail(
          assertion,
          label
              + "childToDelete attribute for object with intValue = "
              + intValue
              + " should be "
              + childToDelete
              + ".  It was "
              + InstanceCallbackClass.capturedChildToDelete[intValue]
              + " instead.");
    }
  }

  /** Touch fields to guarantee that they are loaded into the instance */
  double touchFields(InstanceCallbackClass o) {
    // make a checksum from the fields and return it; this cannot be optimized out...
    double rc = o.doubleValue;
    rc += o.intValue;
    rc += o.charValue;
    rc += o.childToDelete;
    rc += o.name.length();
    rc += o.timeStamp.getTime();
    return rc;
  }
}
