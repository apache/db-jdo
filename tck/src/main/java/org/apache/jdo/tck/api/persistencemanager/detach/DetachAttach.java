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

import org.apache.jdo.tck.pc.shoppingcart.Cart;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Test DetachAttach <br>
 * <B>Keywords:</B> makePersistent detach <br>
 * <B>Assertion IDs:</B> <br>
 * <B>Assertion Description: </B> For a detached instance, they locate or create a persistent
 * instance with the same JDO identity as the detached instance
 */
public class DetachAttach extends DetachTest {

  private static final String ASSERTION_FAILED = "Assertion A12.6.8 (DetachAttach) failed: ";

  /** */
  @Test
  public void testAttachClean() {
    Cart detachedCart = createDetachedInstance(ASSERTION_FAILED);
    pm.currentTransaction().begin();
    Cart attachedCart = pm.makePersistent(detachedCart);
    checkCartValues(ASSERTION_FAILED + "after attach," + NL, attachedCart, true);
    pm.currentTransaction().commit();
    failOnError();
  }
}
