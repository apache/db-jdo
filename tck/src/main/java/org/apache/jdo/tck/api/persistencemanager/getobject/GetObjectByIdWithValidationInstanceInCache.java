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
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Get Object By Id With Validation Instance in Cache <br>
 * <B>Keywords:</B> identity cache <br>
 * <B>Assertion IDs:</B> A12.5.6-7 <br>
 * <B>Assertion Description: </B> If PersistenceManager.getObjectById is called with a value of true
 * for the second parameter named validate, and there is already a transactional instance in the
 * cache with the same JDO identity as the oid parameter, then this method returns it. There is no
 * change made to the state of the returned instance.
 */
public class GetObjectByIdWithValidationInstanceInCache extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.6-7 (GetObjectByIdWithValidationInstanceInCache) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetObjectByIdWithValidationInstanceInCache.class);
  }

  /** */
  public void testGetObjectByIdWithValidationInstanceInCache() {
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      PCPoint p1 = new PCPoint(1, 3);
      pm.makePersistent(p1);
      Object oid = pm.getObjectId(p1);
      tx.commit();
      int curr = currentState(p1);

      tx.begin();
      Object obj = pm.getObjectById(oid, false);
      int curr2 = currentState(obj);

      if (obj != p1) {
        fail(
            ASSERTION_FAILED,
            "pm.getObjectById(oid, false) should find existing instance in pm cache");
      }

      if (curr2 != curr) {
        fail(
            ASSERTION_FAILED,
            "State changed in the returned instance, expected state: "
                + curr
                + " Actual state: "
                + curr2);
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    pm.close();
    pm = null;
  }
}
