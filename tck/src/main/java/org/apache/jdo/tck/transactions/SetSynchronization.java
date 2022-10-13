/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import javax.transaction.Synchronization;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Set Synchronization <br>
 * <B>Keywords:</B> transactions <br>
 * <B>Assertion ID:</B> A13.4.3-1. <br>
 * <B>Assertion Description: </B> A call to Transaction.setSynchronization registers a
 * Synchronization instance with the Transaction for transaction completion notifications. Any
 * Synchronization instance already registered will be replaced.
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version
 * Azita Kamangar   10/11/01     1.0
 */
public class SetSynchronization extends JDO_Test implements Synchronization {

  private Transaction tx;

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A13.4.3-1 (SetSynchronization) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SetSynchronization.class);
  }

  /** */
  public void beforeCompletion() {
    try {
      if (debug) logger.debug("before Complition isActive returns :" + tx.isActive());
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
    try {
      if (debug) logger.debug("after Complition isActive returns :" + tx.isActive());
    } catch (Exception ex) {
      fail(
          ASSERTION_FAILED,
          "tx.isActive called in afterCompletion throws unexpected exception: " + ex);
    }
  }

  /** */
  public void test() {
    pm = getPM();

    runTestSetSynchronization(pm);

    pm.close();
    pm = null;
  }

  /**
   * @param pm the PersistenceManager
   */
  void runTestSetSynchronization(PersistenceManager pm) {
    tx = pm.currentTransaction();
    try {
      tx.begin();
      Synchronization orig = tx.getSynchronization();
      tx.setSynchronization(this);
      Synchronization current = tx.getSynchronization();
      if (current == orig) {
        fail(
            ASSERTION_FAILED,
            "tx.setSynchronization does not replace Synchronization instances registered with the transaction.");
      }
      if (current != this) {
        fail(
            ASSERTION_FAILED,
            "unexpected sxnchronization isntance, expected " + this + ", got " + current);
      }

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
