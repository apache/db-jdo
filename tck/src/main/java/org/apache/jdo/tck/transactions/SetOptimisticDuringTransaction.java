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

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Set Optimistic During Transaction <br>
 * <B>Keywords:</B> transactions <br>
 * <B>Assertion ID:</B> A13.4.2-15. <br>
 * <B>Assertion Description: </B> If Transaction.setOptimistic is called while there is an active
 * transaction, a JDOUserException is thrown.
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version
 * Azita Kamangar   10/09/01     1.0
 */
public class SetOptimisticDuringTransaction extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A13.4.2-15 (SetOptimisticDuringTransaction) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SetOptimisticDuringTransaction.class);
  }

  /** */
  public void test() {
    pm = getPM();

    runTestSetOptimisticDuringTransaction(pm);

    pm.close();
    pm = null;
  }

  /**
   * @param pm the PersistenceManager
   */
  void runTestSetOptimisticDuringTransaction(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      // try to call tx.setOptimistic(true)
      if (isOptimisticSupported()) {
        try {
          tx.setOptimistic(true);
          fail(
              ASSERTION_FAILED,
              "calling tx.setOptimistic in the context of an active transaction should throw a JDOUserException.");
        } catch (JDOUserException ex) {
          // expected exception
          if (debug) logger.debug("caught expected exception " + ex);
        }
      }
      // try to call tx.setOptimistic(false)
      try {
        tx.setOptimistic(false);
        fail(
            ASSERTION_FAILED,
            "calling tx.setOptimistic in the context of an active transaction should throw a JDOUserException.");
      } catch (JDOUserException ex) {
        // expected exception
        if (debug) logger.debug("caught expected exception " + ex);
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
