/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.transactions;

import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

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

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetRollbackOnlyReturnsFalseUntilSet.class);
  }

  /** */
  public void test() {
    getPM();
    Transaction tx = pm.currentTransaction();
    assertFalse(
        ASSERTION_FAILED + "getRollbackOnly returned true before transaction begin.",
        tx.getRollbackOnly());
    tx.begin();
    assertFalse(
        ASSERTION_FAILED + "getRollbackOnly returned true before setRollbackOnly.",
        tx.getRollbackOnly());
    tx.setRollbackOnly();
    assertTrue(
        ASSERTION_FAILED + "getRollbackOnly returned false after setRollbackOnly.",
        tx.getRollbackOnly());
    tx.rollback();
    assertFalse(
        ASSERTION_FAILED + "getRollbackOnly returned true after transaction rollback.",
        tx.getRollbackOnly());
  }
}
