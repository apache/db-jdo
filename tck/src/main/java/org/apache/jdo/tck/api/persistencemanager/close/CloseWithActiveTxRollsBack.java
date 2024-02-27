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

package org.apache.jdo.tck.api.persistencemanager.close;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Close Throws Exception <br>
 * <B>Keywords:</B> exception <br>
 * <B>Assertion IDs:</B> A12.6-2. <br>
 * <B>Assertion Description: </B> In a non-managed environment, if the current transaction is
 * active, close will roll back the transaction.
 */
public class CloseWithActiveTxRollsBack extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.6-2 (CloseWithActiveTxRollsBack) failed: ";

  /** */
  @Test
  public void test() {
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    tx.begin();

    Object id = null;
    try {
      PCPoint pc = new PCPoint(101, 200);
      pm.makePersistent(pc);
      id = pm.getObjectId(pc);

      // This should rollback the transaction
      pm.close();

      Assertions.assertFalse(tx.isActive());
      Assertions.assertTrue(pm.isClosed());
    } finally {
      pm = null;
      PersistenceManager pm2 = getPM();
      pm2.currentTransaction().begin();
      try {
        try {
          pm2.getObjectById(id);
          fail(
              "Object was persisted despite closing the PM. Active transaction should have rolled back");
        } catch (JDOObjectNotFoundException onfe) {
          // Expected, since rolled back
        }
      } finally {
        if (pm2.currentTransaction().isActive()) {
          pm2.currentTransaction().rollback();
        }
        pm2.close();
        pm = null;
      }
    }
  }
}
