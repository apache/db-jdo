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

package org.apache.jdo.tck.transactions;

import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Test GetRollbackOnlyReturnsFalseUntilSet <br>
 * <B>Keywords:</B> commit setRollbackOnly getRollbackOnly <br>
 * <B>Assertion IDs:</B> A13.4.5-2 <br>
 * <B>Assertion Description: </B> When a transaction is not active, and after a transaction is
 * begun, getRollbackOnly will return false. Once setRollbackOnly has been called, it will return
 * true until commit or rollback is called.
 */
public class GetRollbackOnlyReturnsFalseUntilSet extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A13.4.5-2 (GetRollbackOnlyReturnsFalseUntilSet) failed: ";

  /** */
  @Test
  public void test() {
    getPM();
    Transaction tx = pm.currentTransaction();
    Assertions.assertFalse(
        tx.getRollbackOnly(),
        ASSERTION_FAILED + "getRollbackOnly returned true before transaction begin.");
    tx.begin();
    Assertions.assertFalse(
        tx.getRollbackOnly(),
        ASSERTION_FAILED + "getRollbackOnly returned true before setRollbackOnly.");
    tx.setRollbackOnly();
    Assertions.assertTrue(
        tx.getRollbackOnly(),
        ASSERTION_FAILED + "getRollbackOnly returned false after setRollbackOnly.");
    tx.rollback();
    Assertions.assertFalse(
        tx.getRollbackOnly(),
        ASSERTION_FAILED + "getRollbackOnly returned true after transaction rollback.");
  }
}
