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

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Set Optimistic <br>
 * <B>Keywords:</B> transactions <br>
 * <B>Assertion ID:</B> A13.4.2-14. <br>
 * <B>Assertion Description: </B> A call to Transaction.setOptimistic causes the optimistic setting
 * passed to replace the optimistic setting currently active, if the Optimistic optional feature is
 * supported.
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version
 * Azita Kamangar   10/09/01     1.0
 */
public class SetOptimistic extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A13.4.2-14 (SetOptimistic) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SetOptimistic.class);
  }

  /** */
  public void test() {
    pm = getPM();

    runTestSetOptimistic(pm);
    runTestSetOptimisticToInverse(pm);

    pm.close();
    pm = null;
  }

  /**
   * @param pm the PersistenceManager
   */
  public void runTestSetOptimistic(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (isOptimisticSupported()) {
        tx.setOptimistic(true);
        tx.begin();
        if (!tx.getOptimistic()) {
          fail(
              ASSERTION_FAILED, "tx.getOptimistic() returns false after setting the flag to true.");
        }
        tx.commit();
        if (!tx.getOptimistic()) {
          fail(
              ASSERTION_FAILED, "tx.getOptimistic() returns false after setting the flag to true.");
        }
      }

      tx.setOptimistic(false);
      tx.begin();
      if (tx.getOptimistic()) {
        fail(ASSERTION_FAILED, "tx.getOptimistic() returns true after setting the flag to false.");
      }
      tx.commit();
      if (tx.getOptimistic()) {
        fail(ASSERTION_FAILED, "tx.getOptimistic() returns true after setting the flag to false.");
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  void runTestSetOptimisticToInverse(PersistenceManager pm) {
    if (!isOptimisticSupported()) {
      if (debug) logger.debug("Optimistic not supported.");
      return;
    }

    Transaction tx = pm.currentTransaction();
    boolean orig = tx.getOptimistic();
    tx.setOptimistic(!orig);
    if (tx.getOptimistic() == orig) {
      fail(
          ASSERTION_FAILED,
          "changing the optimistic flag by calling tx.setOptimistic does not have a effect.");
    }
    if ((tx != null) && tx.isActive()) {
      tx.rollback();
    }
  }
}
