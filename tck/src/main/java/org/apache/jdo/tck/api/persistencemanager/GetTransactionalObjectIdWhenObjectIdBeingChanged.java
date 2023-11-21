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
 * <B>Title:</B> Get Transactional ObjectId When ObjectId Being Changed <br>
 * <B>Keywords:</B> identity applicationidentity <br>
 * <B>Assertion ID:</B> A12.5.6-15. <br>
 * <B>Assertion Description: </B> If the object identity is being changed in the transaction, by the
 * application modifying one or more of the application key fields, then <code>
 * PersistenceManager.getTransactionalObjectId</code> returns the current identity in the
 * transaction.
 */
public class GetTransactionalObjectIdWhenObjectIdBeingChanged extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.6-15 (GetTransactionalObjectIdWhenObjectIdBeingChanged) failed: ";

  /** */
  @Test
  public void testGetObjectByIdWhenObjectIdBeingChanged() {
    if (!isChangeApplicationIdentitySupported()) {
      if (debug) logger.debug("Implementation does not support chaning application identity");
      return;
    }

    if (debug) logger.debug("\nSTART GetTransactionalObjectIdWhenObjectIdBeingChanged");
    pm = getPM();
    Object oid = createPCPointInstance(pm);
    PCPoint p1 = (PCPoint) pm.getObjectById(oid, false);
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      p1.setX(10000); // this is the primary key
      Object oid2 = pm.getTransactionalObjectId(p1); // should be different
      tx.commit();
      Object oid3 = pm.getObjectId(p1); // should be same as oid2
      if (oid.equals(oid2)) {
        fail(ASSERTION_FAILED, "Oid before is the same as transactional oid after modifying pk");
      }

      if (!oid2.equals(oid3)) {
        fail(ASSERTION_FAILED, "Oid after is different from transactional oid after modifying pk");
      }
    } finally {
      if (debug) logger.debug("END GetTransactionalObjectIdWhenObjectIdBeingChanged");
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    pm.close();
    pm = null;
  }
}
