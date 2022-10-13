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

package org.apache.jdo.tck.api.persistencemanager;

import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Same Transaction Instance For All Calls To Current Transaction <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion ID:</B> A12.5.2-2. <br>
 * <B>Assertion Description: </B> The identical <code>Transaction</code> instance will be returned
 * by all <code>currentTransaction</code> calls to the same <code>PersistenceManager</code> until
 * <code>close</code>.
 */
public class SameTransactionInstanceForAllCallsToCurrentTransaction extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.2-2 (SameTransactionInstanceForAllCallsToCurrentTransaction) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SameTransactionInstanceForAllCallsToCurrentTransaction.class);
  }

  /** */
  public void test() {
    pm = getPM();

    Transaction tx1 = getPM().currentTransaction();
    tx1.begin();
    Transaction tx2 = getPM().currentTransaction();
    tx1.commit();
    Transaction tx3 = getPM().currentTransaction();

    if (tx1 != tx2) {
      fail(ASSERTION_FAILED, "tx1 before begin different from tx2 after begin");
    }
    if (tx2 != tx3) {
      fail(ASSERTION_FAILED, "tx2 after begin different from tx3 after commit");
    }

    if (!pm.isClosed()) {
      if (pm.currentTransaction().isActive()) {
        pm.currentTransaction().rollback();
      }
      pm.close();
    }
    pm = null;
  }
}
