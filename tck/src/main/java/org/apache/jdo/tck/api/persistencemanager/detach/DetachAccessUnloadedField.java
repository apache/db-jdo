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

import javax.jdo.FetchPlan;
import javax.jdo.JDODetachedFieldAccessException;
import org.apache.jdo.tck.pc.shoppingcart.CartEntry;
import org.apache.jdo.tck.pc.shoppingcart.Product;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Test DetachCopy <br>
 * <B>Keywords:</B> detachCopy detach <br>
 * <B>Assertion IDs:</B> A12.6.8-21 <br>
 * <B>Assertion Description: </B> While detached, any field access to a field that was not loaded
 * throws JDODetachedFieldAccessException.
 */
public class DetachAccessUnloadedField extends DetachTest {

  private static final String ASSERTION_FAILED = "Assertion A12.6.8-21 (DetachCopy) failed: ";

  /**
   * Positive test for field access of detached instance Field is in fetch group, loaded at
   * detachment with DETACH_LOAD_FIELDS true
   */
  @Test
  public void testDetachCopyFieldAccessOk() {
    // datastore transaction, retainValues=false
    getPM().currentTransaction().begin();
    // sets fetch group cart, entries, product
    setCartFetchGroups();
    // DETACH_LOAD_FIELDS true by default
    CartEntry cartEntryDetached = pm.detachCopy(cartEntry1);
    try {
      Product prod = cartEntryDetached.getProduct();
    } catch (JDODetachedFieldAccessException jdoex) {
      fail("Unexpected exception caught: " + jdoex.getMessage());
    }
    pm.currentTransaction().commit();
  }

  /**
   * Positive test for access of loaded field of detached instance All fields are in the fetch plan
   * detachment options = 0, fields loaded by access of an instance
   */
  @Test
  public void testDetachCopyAccessLoadedField() {
    // datastore transaction, retainValues=false
    getPM().currentTransaction().begin();
    // sets fetch group cart, entries, product
    setCartFetchGroups();
    // loads Product field
    int quantity = cartEntry1.getQuantity();

    pm.getFetchPlan().setDetachmentOptions(0);
    CartEntry cartEntryDetached = pm.detachCopy(cartEntry1);
    try {
      Product prodDetached = cartEntryDetached.getProduct();
    } catch (JDODetachedFieldAccessException jdoex) {
      fail("Unexpected exception caught: " + jdoex.getMessage());
    } finally {
      pm.currentTransaction().commit();
    }
  }

  /**
   * Negative test for access of unloaded field of detached instance with DETACH_UNLOAD_FIELDS set
   */
  @Test
  public void testDetachCopyAccessUnloadedField() {
    // datastore transaction, retainValues=false
    getPM().currentTransaction().begin();
    // sets fetch group cart, entries, product
    setCartFetchGroups();
    Product prod = cartEntry1.getProduct();
    pm.getFetchPlan().removeGroup(CARTENTRY_PRODUCT);
    pm.getFetchPlan().setDetachmentOptions(FetchPlan.DETACH_UNLOAD_FIELDS);
    CartEntry cartEntryDetached = pm.detachCopy(cartEntry1);
    try {
      Product prodDetached = cartEntryDetached.getProduct();
      fail("Expected exception on access of unloaded field, " + "but none thrown.");
    } catch (JDODetachedFieldAccessException jdoex) {
      // expect exception
    } finally {
      pm.currentTransaction().commit();
    }
  }

  /** Negative test for access of field of detached instance where field was not loaded */
  @Test
  public void testDetachCopyAccessFieldNotInFetchPlan() {
    // datastore transaction, retainValues=false
    getPM().currentTransaction().begin();
    FetchPlan fp = pm.getFetchPlan();
    // Product field not in Fetch Plan
    fp.addGroup(CART_ENTRIES);
    fp.addGroup(CARTENTRY_CART);

    CartEntry cartEntryDetached = pm.detachCopy(cartEntry1);
    try {
      Product prod = cartEntryDetached.getProduct();
      fail(
          "Expected exception on access of field not in fetch plan"
              + " and not loaded, but none thrown.");
    } catch (JDODetachedFieldAccessException jdoex) {
      // expect exception
    } finally {
      pm.currentTransaction().commit();
    }
  }
}
