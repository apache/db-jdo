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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Before Completion Method Called <br>
 * <B>Keywords:</B> transactions <br>
 * <B>Assertion ID:</B> A13.4.3-4. <br>
 * <B>Assertion Description: </B> The beforeCompletion method of the Synchronization instance
 * registered with a Transaction will be called during the behavior specified for the transaction
 * completion method commit.
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version
 * Azita Kamangar   10/15/01     1.0
 */
public class BeforeCompletionMethodCalled extends JDO_Test implements Synchronization {

  private boolean beforeCompletionCalled;

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A13.4.3-4 (BeforeCompletionMethodCalled) failed: ";

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PCPoint.class);
  }

  /** */
  @Test
  public void test() {
    pm = getPM();

    runTestBeforeCompletionMethodCalled(pm);

    pm.close();
    pm = null;
  }

  /** */
  public void beforeCompletion() {
    beforeCompletionCalled = true;
    if (debug) logger.debug("beforeCompletion called ");
  }

  /**
   * This method is called by the transaction manager after the transaction is committed or rolled
   * back.
   *
   * @param status The status of the transaction completion.
   */
  public void afterCompletion(int status) {
    if (status != javax.transaction.Status.STATUS_COMMITTED) {
      fail(
          ASSERTION_FAILED,
          "afterCompletion: incorrect status, expected "
              + Status.STATUS_COMMITTED
              + ", got "
              + status);
    }
  }

  /** test transactions.setSynchronization() */
  void runTestBeforeCompletionMethodCalled(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      PCPoint p1 = new PCPoint(1, 3);
      pm.makePersistent(p1);

      tx.setSynchronization(this);
      beforeCompletionCalled = false;
      tx.commit();
      tx = null;

      if (!beforeCompletionCalled) {
        fail(ASSERTION_FAILED, "commit didn't invoke beforeCompletion method");
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
