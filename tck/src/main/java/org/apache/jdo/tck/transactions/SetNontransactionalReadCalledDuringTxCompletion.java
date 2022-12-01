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

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.transaction.Synchronization;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Set Nontransactional Read Called During TX Completion <br>
 * <B>Keywords:</B> transactions <br>
 * <B>Assertion ID:</B> A13.4.2-1. <br>
 * <B>Assertion Description: </B> If the setNontransactionalRead method of the Transaction interface
 * is called during commit or rollback processing (within the beforeCompletion and afterCompletion
 * synchronization methods), a JDOUserException is thrown.
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version
 * Azita Kamangar   10/18/01     1.0
 */
public class SetNontransactionalReadCalledDuringTxCompletion extends JDO_Test
    implements Synchronization {

  private Transaction tx;

  private boolean nonTransactionalReadFlag;

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A13.4.2-1 (SetNontransactionalReadCalledDuringTxCompletion) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SetNontransactionalReadCalledDuringTxCompletion.class);
  }

  /** */
  public void beforeCompletion() {
    if (debug) logger.debug("beforeCompletion.");
    try {
      tx.setNontransactionalRead(nonTransactionalReadFlag);
      fail(
          ASSERTION_FAILED,
          "tx.setNontransactionalRead called in beforeCompletion should throw JDOUserException.");
    } catch (JDOUserException ex) {
      // expected exception
      if (debug) logger.debug("caught expected exception " + ex);
    } catch (Exception ex) {
      fail(
          ASSERTION_FAILED,
          "tx.setNontransactionalRead called in beforeCompletion throws unexpected exception: "
              + ex);
    }
  }

  /**
   * This method is called by the transaction manager after the transaction is committed or rolled
   * back.
   *
   * @param status The status of the transaction completion.
   */
  public void afterCompletion(int status) {
    if (debug) logger.debug("afterCompletion.");
    try {
      tx.setNontransactionalRead(nonTransactionalReadFlag);
    } catch (JDOUserException ex) {
      // TBD: need to remove this catch block as soon as the JDORI is
      // fixed see 'Issue 61: Transaction.isActive issues'
      if (debug) logger.debug("caught exception " + ex);
    } catch (Exception ex) {
      fail(
          ASSERTION_FAILED,
          "tx.setNontransactionalRead called in afterCompletion throws unexpected exception: "
              + ex);
    }
  }

  /** */
  public void test() {
    pm = getPM();

    runTestSetNontransactionalReadCalledDuringTxCompletion(pm);

    pm.close();
    pm = null;
  }

  /**
   * Test transactions.setNonteansactionalRead()
   *
   * @param pm the PersistenceManager
   */
  public void runTestSetNontransactionalReadCalledDuringTxCompletion(PersistenceManager pm) {
    tx = pm.currentTransaction();
    try {
      tx.setSynchronization(this);

      nonTransactionalReadFlag = false;
      tx.begin();
      tx.commit();

      if (isNontransactionalReadSupported()) {
        nonTransactionalReadFlag = true;
        tx.begin();
        tx.commit();
      }

      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
