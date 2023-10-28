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
package org.apache.jdo.tck.api.jdohelper;

import javax.jdo.JDOHelper;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.detach.DetachTest;
import org.apache.jdo.tck.pc.shoppingcart.Cart;
import org.apache.jdo.tck.pc.shoppingcart.Undetachable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Test IsDetached <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A8.5.6-1 <br>
 * <B>Assertion Description: </B> Instances that have been detached return true. The method returns
 * false if the instance is transient or null or if its class is not detachable.
 */
public class IsDetached extends DetachTest {

  private static final String ASSERTION_FAILED =
      "Assertion A8.5.6-1 JDOHelper.isDetached(Object) failed: ";

  @Test
  public void testNullTransientAndUndetachableIsDetachedFalse() {
    pm = getPM();
    pm.currentTransaction().begin();

    Assertions.assertFalse(
        JDOHelper.isDetached(null), ASSERTION_FAILED + "null object is detached");
    Assertions.assertFalse(
        JDOHelper.isDetached(new Cart("bob")), ASSERTION_FAILED + "transient object is detached");
    Assertions.assertFalse(
        JDOHelper.isDetached(new Undetachable()),
        ASSERTION_FAILED + "object of class marked not detachabled is detached");

    pm.currentTransaction().commit();
  }

  @Test
  public void testDetachableIsDetachedTrue() {
    pm = getPM();
    Transaction txn = pm.currentTransaction();

    Cart c, detached;
    Object oid;
    txn.begin();
    {
      c = new Cart("bob");
      pm.makePersistent(c);
      oid = pm.getObjectId(c);
    }
    txn.commit();

    txn.begin();
    {
      c = (Cart) pm.getObjectById(oid);
      detached = pm.detachCopy(c);
      Assertions.assertTrue(JDOHelper.isDetached(detached));
    }
    txn.commit();
  }
}
