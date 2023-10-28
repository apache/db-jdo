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

package org.apache.jdo.tck.api.persistencemanager.flags;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Set Multithreaded False <br>
 * <B>Keywords:</B> multithreaded <br>
 * <B>Assertion ID:</B> A12.7-2. <br>
 * <B>Assertion Description: </B> If PersistenceManager.setMultithreaded is called with a value of
 * false, a value of false will be returned when getMultithreaded is called.
 */
public class SetMultithreadedFalse extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.7-2 (SetMultithreadedFalse) failed: ";

  /** */
  @Test
  public void test() {
    pm = getPM();

    runTestSetMultithreadedFalse(pm);

    pm.close();
    pm = null;
  }

  /**
   * @param pm the PersistenceManager
   */
  private void runTestSetMultithreadedFalse(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      PCPoint p1 = new PCPoint();
      tx.begin();
      pm.setMultithreaded(false);
      if (pm.getMultithreaded()) {
        fail(
            ASSERTION_FAILED,
            "pm.getMultithreaded() should false true after setting the flag to false.");
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
