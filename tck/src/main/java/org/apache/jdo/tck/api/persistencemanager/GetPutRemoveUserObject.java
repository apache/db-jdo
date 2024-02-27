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

package org.apache.jdo.tck.api.persistencemanager;

import org.apache.jdo.tck.JDO_Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Test GetPutRemoveUserObject <br>
 * <B>Keywords:</B> getUserObject putUserObject removeUserObject <br>
 * <B>Assertion IDs:</B> A12.11-1 <br>
 * <B>Assertion Description: </B> Object putUserObject(Object key, Object value); This method models
 * the put method of Map. The current value associated with the key is returned and replaced by the
 * parameter value. If the parameter value is null, the implementation may remove the entry from the
 * table of managed key/value pairs Object removeUserObject(Object key); This method models the
 * remove method of Map. The current value associated with the key is returned and removed. Object
 * getUserObject(Object key); This method models the get method of Map. The current value associated
 * with the key is returned. If the key is not found in the table, null is returned.]..]
 */
public class GetPutRemoveUserObject extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.11-1 (GetPutRemoveUserObject) failed: ";

  /** First key */
  private static final String KEY1 = "jdo.tck.key1";

  /** Second key */
  private static final String KEY2 = "jdo.tck.key2";

  /** Non-existent key */
  private static final String KEY_DOES_NOT_EXIST = "jdo.tck.keyDoesNotExist";

  /** */
  @Test
  public void testGetNonexistentKey() {
    getPM();
    Assertions.assertNull(
        pm.getUserObject(KEY_DOES_NOT_EXIST),
        ASSERTION_FAILED + "Non-null value returned from key that does not exist.");
  }

  public void testPutKey() {
    getPM();
    UserObject obj1p = new UserObject();
    UserObject obj2p = new UserObject();
    UserObject obj3p = new UserObject();
    UserObject obj4p = new UserObject();
    Object obj1pv = pm.putUserObject(KEY1, obj1p);
    Object obj2pv = pm.putUserObject(KEY2, obj2p);
    Object obj1rv = pm.putUserObject(KEY1, obj3p);
    Object obj2rv = pm.putUserObject(KEY2, obj4p);
    Assertions.assertNull(
        obj1pv,
        ASSERTION_FAILED + "putUserObject expected null on first put" + "; actual: " + obj1pv);
    Assertions.assertSame(
        obj1p,
        obj1rv,
        ASSERTION_FAILED
            + "putUserObject expected replaced object returned: "
            + obj1p
            + "; actual: "
            + obj1rv);
    Assertions.assertNull(
        obj2pv,
        ASSERTION_FAILED + "putUserObject expected null on first put" + "; actual: " + obj2pv);
    Assertions.assertSame(
        obj2p,
        obj2rv,
        ASSERTION_FAILED
            + "putUserObject expected replaced object returned: "
            + obj2p
            + "; actual: "
            + obj2rv);
  }

  public void testGetKey() {
    getPM();
    UserObject obj1p = new UserObject();
    UserObject obj2p = new UserObject();
    Object obj1pv = pm.putUserObject(KEY1, obj1p);
    Object obj2pv = pm.putUserObject(KEY2, obj2p);
    Object obj1g = pm.getUserObject(KEY1);
    Object obj2g = pm.getUserObject(KEY2);
    pm.putUserObject(KEY1, null);
    pm.putUserObject(KEY2, null);
    Assertions.assertNull(
        obj1pv, ASSERTION_FAILED + "putUserObject expected null" + "; actual: " + obj1pv);
    Assertions.assertSame(
        obj1p, obj1g, ASSERTION_FAILED + "getUserObject expected: " + obj1p + "; actual: " + obj1g);
    Assertions.assertNull(
        obj2pv, ASSERTION_FAILED + "putUserObject expected null" + "; actual: " + obj2pv);
    Assertions.assertSame(
        obj2p, obj2g, ASSERTION_FAILED + "getUserObject expected: " + obj2p + "; actual: " + obj2g);
  }

  public void testRemoveKey() {
    getPM();
    UserObject obj1p = new UserObject();
    UserObject obj2p = new UserObject();
    pm.putUserObject(KEY1, obj1p);
    pm.putUserObject(KEY2, obj2p);
    Object obj1r = pm.removeUserObject(KEY1);
    Object obj1rr = pm.removeUserObject(KEY1);
    Object obj2r = pm.removeUserObject(KEY2);
    Object obj2rr = pm.removeUserObject(KEY2);
    Assertions.assertSame(
        obj1p,
        obj1r,
        ASSERTION_FAILED + "removeUserObject(KEY1) expected: " + obj1p + "; actual: " + obj1r);
    Assertions.assertNull(
        obj1rr, ASSERTION_FAILED + "getUserObject(KEY1) expected null: " + "; actual: " + obj1rr);
    Assertions.assertSame(
        obj2p,
        obj2r,
        ASSERTION_FAILED + "removeUserObject(KEY2) returned: " + obj2p + "; actual: " + obj2r);
    Assertions.assertNull(
        obj2rr, ASSERTION_FAILED + "getUserObject(KEY2) expected null: " + "; actual: " + obj2rr);
  }

  private class UserObject {
    // No implementation needed for this class. It is only usedwith == comparisons.
  }
}
