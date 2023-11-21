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

package org.apache.jdo.tck.api.persistencemanager;

import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Get Transactional ObjectId With No Transaction <br>
 * <B>Keywords:</B> identity <br>
 * <B>Assertion ID:</B> A12.5.6-16 <br>
 * <B>Assertion Description: </B> If there is no transaction in progress, or if none of the key
 * fields is being modified, then PersistenceManager.getTransactionalObjectId has the same behavior
 * as getObjectId.
 */
public class GetTransactionalObjectIdWithNoTransaction extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.6-16 (GetTransactionalObjectIdWithNoTransaction) failed: ";

  /** */
  @Test
  public void testGetTransactionalObjectIdWithNoTransaction() {
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      PCPoint p1 = new PCPoint();

      tx.begin();
      pm.makePersistent(p1);
      Object oid = pm.getObjectId(p1);
      tx.commit();

      Object toid = pm.getTransactionalObjectId(p1);
      if (!toid.equals(oid)) {
        fail(
            ASSERTION_FAILED,
            "pm.getTransactionalObjectId(p1) returned unexpected ObjectId, expected "
                + oid
                + ", got "
                + toid);
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    pm.close();
    pm = null;
  }
}
