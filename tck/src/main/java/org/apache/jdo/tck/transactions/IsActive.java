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

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Is Active <br>
 * <B>Keywords:</B> transactions <br>
 * <B>Assertion ID:</B> A13.4.1-2. <br>
 * <B>Assertion Description: </B> The transactions.isActive method tells whether there is an active
 * transaction. There will be an active transaction if the begin method has been executed but
 * neither commit nor rollback has been executed.
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version
 * Azita Kamangar   10/09/01     1.0
 */
public class IsActive extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A13.4.1-2 (IsActive) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(IsActive.class);
  }

  /** */
  public void test() {
    pm = getPM();

    runTestIsActive(pm);

    pm.close();
    pm = null;
  }

  /**
   * test transactions.isActive()
   *
   * @param pm the PersistenceManager
   */
  void runTestIsActive(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      if (!tx.isActive()) {
        fail(ASSERTION_FAILED, "tx.isActive returns false after tx.begin");
      }
      tx.commit();

      if (tx.isActive()) {
        fail(ASSERTION_FAILED, "tx.isActive returns true after tx.commit");
      }

      tx.begin();
      tx.rollback();
      if (tx.isActive()) {
        fail(ASSERTION_FAILED, "tx.isActive returns true after tx.rollback");
      }

      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
