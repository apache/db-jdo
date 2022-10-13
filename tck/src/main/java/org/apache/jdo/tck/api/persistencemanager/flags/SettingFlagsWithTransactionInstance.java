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

package org.apache.jdo.tck.api.persistencemanager.flags;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Setting Flags With Transaction instance <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion ID:</B> A12.5.2-3. <br>
 * <B>Assertion Description: </B> Even if the Transaction instance returned by
 * PersistenceManager.currentTransaction cannot be used for transaction completion (due to external
 * transaction management), it still can be used to set flags.
 */
public class SettingFlagsWithTransactionInstance extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.2-3 (SettingFlagsWithTransactionInstance) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SettingFlagsWithTransactionInstance.class);
  }

  /** */
  public void test() {
    pm = getPM();

    runTestSettingFlagsWithTransactionInstance(pm);

    pm.close();
    pm = null;
  }

  /** */
  private void runTestSettingFlagsWithTransactionInstance(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    tx.setNontransactionalRead(false);
    tx.setNontransactionalWrite(false);
    tx.setRetainValues(false);
    tx.setOptimistic(false);
  }
}
