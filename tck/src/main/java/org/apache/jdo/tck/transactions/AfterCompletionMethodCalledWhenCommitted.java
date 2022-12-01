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
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> After Completion Method Called When Committed <br>
 * <B>Keywords:</B> transactions <br>
 * <B>Assertion ID:</B> A13.4.3-6. <br>
 * <B>Assertion Description: </B> The afterCompletionmethod of the Synchronization instance
 * registered with a Transaction will be called during the behavior specified for the transaction
 * completion method commit with a value of Status.STATUS_COMMITTE
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version
 * Azita Kamangar   10/11/01     1.0
 */
public class AfterCompletionMethodCalledWhenCommitted extends JDO_Test implements Synchronization {

  private boolean afterCompletionCalled;
  private Transaction tx;

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A13.4.3-6 (AfterCompletionMethodCalledWhenCommitted) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(AfterCompletionMethodCalledWhenCommitted.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PCPoint.class);
  }

  /** */
  public void test() {
    pm = getPM();

    runTestAfterCompletionMethodCalledWhenCommitted(pm);

    pm.close();
    pm = null;
  }

  /** */
  public void beforeCompletion() {
    try {
      if (debug) logger.debug("before Complition isActive returns" + tx.isActive());
    } catch (Exception ex) {
      fail(
          ASSERTION_FAILED,
          "tx.isActive called in beforeCompletion throws unexpected exception: " + ex);
    }
  }

  /**
   * This method is called by the transaction manager after the transaction is committed or rolled
   * back.
   *
   * @param status The status of the transaction completion.
   */
  public void afterCompletion(int status) {
    if (status == javax.transaction.Status.STATUS_COMMITTED) {
      afterCompletionCalled = true;
    } else {
      fail(
          ASSERTION_FAILED,
          "afterCompletion: incorrect status, expected "
              + Status.STATUS_COMMITTED
              + ", got "
              + status);
    }
  }

  /** test transactions.setSynchronization() */
  void runTestAfterCompletionMethodCalledWhenCommitted(PersistenceManager pm) {
    tx = pm.currentTransaction();
    try {
      tx.begin();
      PCPoint p1 = new PCPoint(1, 3);
      pm.makePersistent(p1);

      tx.setSynchronization(this);
      afterCompletionCalled = false;
      tx.commit();
      tx = null;

      if (!afterCompletionCalled) {
        fail(ASSERTION_FAILED, "commit didn't invoke afterCompletion method");
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
