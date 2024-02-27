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

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Get Object By Id With Validation, Instance in Cache, Not in Datastore <br>
 * <B>Keywords:</B> identity cache <br>
 * <B>Assertion IDs:</B> A12.5.6-8. <br>
 * <B>Assertion Description: </B> If PersistenceManager.getObjectById is called with a value of true
 * for the second parameter named validate, and there is an instance already in the cache with the
 * same JDO identity as the oid parameter, and the instance is not transactional, and the instance
 * does not exist in the datastore, then a JDODatastoreException is thrown.
 */
public class GetObjectByIdWithValidationInstanceInCacheNotInDatastore
    extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.6-8 (GetObjectByIdWithValidationInstanceInCacheNotInDatastore) failed: ";

  /* passing null paramameter to getObjectId */
  @Test
  public void testGetObjectByIdWithValidationInstanceInCacheNotInDatastore() {
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      PCPoint p1 = new PCPoint(1, 3);
      pm.makePersistent(p1);
      Object oid = pm.getObjectId(p1);
      pm.deletePersistent(p1);
      tx.commit();

      tx.begin();
      try {
        PCPoint p2 = (PCPoint) pm.getObjectById(oid, true);
        fail(
            ASSERTION_FAILED,
            "pm.getObjectById(oid, true) should throw JDOObjectNotFoundException, if oid refers to an non existing object");
      } catch (JDOObjectNotFoundException ex) {
        // expected exception
      }
      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    pm.close();
    pm = null;
  }
}
