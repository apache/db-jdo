/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.api.persistencemanager.getobject;

import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Get Object By Id No Validation, Instance In Cache <br>
 * <B>Keywords:</B> identity cache <br>
 * <B>Assertion IDs:</B> A12.5.6-2 <br>
 * <B>Assertion Description: </B> If PersistenceManager.getObjectById is called with a value of
 * false for the second parameter named validate, and there is already an instance in the cache with
 * the same JDO identity as the oid parameter, then this method returns it.
 */
public class GetObjectByIdNoValidationInstanceInCache extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.6-2 (GetObjectByIdNoValidationInstanceInCache) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetObjectByIdNoValidationInstanceInCache.class);
  }

  /** */
  public void testGetObjectByIdNoValidationInstanceInCache() {
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      PCPoint p1 = new PCPoint(1, 3);
      pm.makePersistent(p1);
      int curr = currentState(p1);

      Object oid = pm.getObjectId(p1);
      tx.commit();

      tx.begin();
      Object obj = pm.getObjectById(oid, false);

      if (p1 != obj)
        fail(
            ASSERTION_FAILED,
            "pm.getObjectById(oid, false) should find existing instance in pm cache");
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    pm.close();
    pm = null;
  }
}
