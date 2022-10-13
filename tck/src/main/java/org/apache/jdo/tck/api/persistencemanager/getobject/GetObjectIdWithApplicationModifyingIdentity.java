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

package org.apache.jdo.tck.api.persistencemanager.getobject;

import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Get ObjectId With Application Modifying Identity <br>
 * <B>Keywords:</B> identity applicationidentity <br>
 * <B>Assertion ID:</B> A12.5.6-11. <br>
 * <B>Assertion Description: </B> If the object identity is being changed in the transaction, by the
 * application modifying one or more of the application key fields, then the method <code>
 * PersistenceManager.getObjectId</code> returns the identity as of the beginning of the
 * transaction. The value returned by <code>getObjectId</code> will be different following <code>
 * afterCompletion</code> processing for successful transactions.
 */
public class GetObjectIdWithApplicationModifyingIdentity extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.6-11 (GetObjectIdWithApplicationModifyingIdentity) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetObjectIdWithApplicationModifyingIdentity.class);
  }

  /** */
  public void testGetObjectByIdWithApplicationModifyingIdentity() {
    if (!isChangeApplicationIdentitySupported()) {
      if (debug) logger.debug("Implementation does not support chaning application identity");
      return;
    }

    if (debug) logger.debug("\nSTART GetObjectIdWithApplicationModifyingIdentity");
    pm = getPM();
    Object oid = createPCPointInstance(pm);
    PCPoint p1 = (PCPoint) pm.getObjectById(oid, false);
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      p1.setX(10000); // this is the primary key
      Object oid2 = pm.getObjectId(p1); // should be the same
      tx.commit();

      Object oid3 = pm.getObjectId(p1); // should be same as oid3
      if (!oid.equals(oid2)) {
        fail(ASSERTION_FAILED, "Oid before is different from oid after modifying pk");
      }

      if (oid.equals(oid3)) {
        fail(ASSERTION_FAILED, "Oid before is the same as oid after modifying pk");
      }
    } finally {
      if (debug) logger.debug("END GetObjectIdWithApplicationModifyingIdentity");
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    pm.close();
    pm = null;
  }
}
