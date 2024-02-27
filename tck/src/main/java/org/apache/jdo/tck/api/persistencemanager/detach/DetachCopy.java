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
package org.apache.jdo.tck.api.persistencemanager.detach;

import java.util.Collection;
import java.util.HashSet;
import org.apache.jdo.tck.pc.shoppingcart.Cart;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Test DetachCopy <br>
 * <B>Keywords:</B> detachCopy detach <br>
 * <B>Assertion IDs:</B> A12.6.8-13, A12.6.8-18, A12.6.8-19, A12.6.8-20 <br>
 * <B>Assertion Description: </B> For each instance in the parameter Collection, a corresponding
 * detached copy is returned. Each field in the persistent instance is handled based on its type and
 * whether the field is contained in the fetch group for the persistence-capable class. If there are
 * duplicates in the parameter Collection, the corresponding detached copy is used for each such
 * duplicate.
 */
public class DetachCopy extends DetachTest {

  private static final String ASSERTION_FAILED =
      "Assertion A12.6.8-13, A12.6.8-18, A12.6.8-19, A12.6.8-20" + " (DetachCopy) failed: ";

  /** */
  @Test
  public void testDetachCopy() {
    getPM().currentTransaction().begin();
    setCartFetchGroups();
    // pm.retrieveAll(cartClosure);
    Cart cartDetached = pm.detachCopy(cart1);
    checkCartValues(ASSERTION_FAILED + "after detachCopy," + NL, cartDetached);
    pm.currentTransaction().commit();
    failOnError();
  }

  /** */
  @Test
  public void testDetachCopyAllCollection() {
    getPM().currentTransaction().begin();
    setCartFetchGroups();
    // pm.retrieveAll(cartClosure);
    Collection<Cart> instances = new HashSet<>();
    instances.add(cart1);
    Collection<Cart> detached = pm.detachCopyAll(instances);
    Cart cartDetached = detached.iterator().next();
    checkCartValues(ASSERTION_FAILED + "after detachCopyAll(Collection)," + NL, cartDetached);
    pm.currentTransaction().commit();
    failOnError();
  }

  /** */
  @Test
  public void testDetachCopyAllArray() {
    getPM().currentTransaction().begin();
    setCartFetchGroups();
    // pm.retrieveAll(cartClosure);
    Object[] instances = new Object[] {cart1};
    Object[] detached = pm.detachCopyAll(instances);
    Cart cartDetached = (Cart) detached[0];
    checkCartValues(ASSERTION_FAILED + "after detachCopyAll(Object[])," + NL, cartDetached);
    pm.currentTransaction().commit();
    failOnError();
  }
}
