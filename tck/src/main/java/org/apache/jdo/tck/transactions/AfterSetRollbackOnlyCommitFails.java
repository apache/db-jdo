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

import javax.jdo.JDOFatalDataStoreException;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Test AfterSetRollbackOnlyCommitFails <br>
 * <B>Keywords:</B> rollback commit setRollbackOnly <br>
 * <B>Assertion IDs:</B> A13.4.5-1 <br>
 * <B>Assertion Description: </B> Once a transaction has been marked for rollback via
 * setRollbackOnly, the commit method will always fail with JDOFatalDataStoreException.
 */
public class AfterSetRollbackOnlyCommitFails extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A13.4.5-1 (AfterSetRollbackOnlyCommitFails) failed: ";

  /** */
  @Test
  public void test() {
    getPM();
    Transaction tx = pm.currentTransaction();
    tx.begin();
    tx.setRollbackOnly();
    try {
      tx.commit();
      fail(ASSERTION_FAILED, "Failed to catch expected JDOFatalDataStoreException.");
    } catch (JDOFatalDataStoreException ex) {
      // Good catch!
    }
  }
}
