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
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Get Persistence Manager <br>
 * <B>Keywords:</B> transactions <br>
 * <B>Assertion ID:</B> A13.4.1-1. <br>
 * <B>Assertion Description: </B> The transactions.getPersistenceManager method returns associated
 * persistence manager if the object parameter ia not null and implements persistenceCapable.
 * evaluating to true when == is used.
 */

/*
 * Revision History
 * ================
 * Author         :     Date   :    Version
 * Azita Kamangar   9/26/01      1.0
 */
public class GetPersistenceManager extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A13.4.1-1 (GetPersistenceManager) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetPersistenceManager.class);
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

    runTestGetPersistenceManager(pm);

    pm.close();
    pm = null;
  }

  /** test transactions.getPersistenceManager() */
  void runTestGetPersistenceManager(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      PCPoint p1 = new PCPoint(1, 3);
      pm.makePersistent(p1);
      PersistenceManager pm1 = tx.getPersistenceManager();
      tx.commit();
      tx = null;

      if (pm1 != pm)
        fail(
            ASSERTION_FAILED,
            "tx.getPersistenceManager() returned unexpected pm, expected " + pm + ", got " + pm1);
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
