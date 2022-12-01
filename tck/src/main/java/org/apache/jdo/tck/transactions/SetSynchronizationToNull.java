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
 * <B>Title:</B> Set Synchronization To Null <br>
 * <B>Keywords:</B> transactions <br>
 * <B>Assertion ID:</B> A13.4.3-2. <br>
 * <B>Assertion Description: </B> If the parameter to Transaction.setSynchronization is null, then
 * no instance will be notified.
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version
 * Azita Kamangar   10/11/01     1.0
 */
public class SetSynchronizationToNull extends JDO_Test
    implements javax.transaction.Synchronization {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A13.4.3-2 (SetSynchronizationToNull) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SetSynchronizationToNull.class);
  }

  /** */
  public void beforeCompletion() {
    fail(
        ASSERTION_FAILED,
        "Instance should not be registered, thus this beforeCompletion methgod should not be called.");
  }

  /**
   * This method is called by the transaction manager after the transaction is committed or rolled
   * back.
   *
   * @param status The status of the transaction completion.
   */
  public void afterCompletion(int status) {
    fail(
        ASSERTION_FAILED,
        "Instance should not be registered, thus this afterCompletion methgod should not be called.");
  }

  /** */
  public void test() {
    pm = getPM();

    runTestSetSynchronizationToNull(pm);

    pm.close();
    pm = null;
  }

  /**
   * @param pm the PersistenceManager
   */
  void runTestSetSynchronizationToNull(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      tx.setSynchronization(this);
      tx.setSynchronization(null);
      if (tx.getSynchronization() != null) {
        fail(
            ASSERTION_FAILED,
            "tx.setSynchronization(null) should overwrite previous registered synchronization instance.");
      }

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
