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

package org.apache.jdo.tck.api.persistencemanager.getobject;

import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Get Object By Id No Validation Instance Not In Cache, No Tx <br>
 * <B>Keywords:</B> identity cache <br>
 * <B>Assertion IDs:</B> A12.5.6-4 <br>
 * <B>Assertion Description: </B> If PersistenceManager.getObjectById is called with a value of
 * false for the second parameter named validate, and there is not an instance already in the cache
 * with the same JDO identity as the oid parameter, and there is no transaction in progress, then
 * this method creates an instance with the specified JDO identity and returns it with a state of
 * hollow or persistent-nontransactional, at the choice of the implementation.
 */
public class GetObjectByIdNoValidationInstanceNotInCacheNoTx extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.6-4 (GetObjectByIdNoValidationInstanceNotInCacheNoTx) failed: ";

  /** */
  @Test
  public void testGetObjectByIdNoValidationInstanceNotInCacheNoTx() {
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      PCPoint p1 = new PCPoint(1, 3);
      pm.makePersistent(p1);
      Object oid = pm.getObjectId(p1);
      pm.evict(p1);
      tx.commit();

      /* get oid for p1, no transaction in progress */
      Object obj = pm.getObjectById(oid, false);

      int curr = currentState(p1);

      if (curr == HOLLOW || curr == PERSISTENT_NONTRANSACTIONAL || curr == PERSISTENT_CLEAN) {
        // expected result
      } else {
        fail(ASSERTION_FAILED, "returned state mismatched - " + " Actual state " + curr);
      }
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    pm.close();
    pm = null;
  }
}
