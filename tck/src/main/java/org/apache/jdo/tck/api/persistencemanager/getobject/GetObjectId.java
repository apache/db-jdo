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

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;

/**
 * <B>Title:</B> Get Objectid <br>
 * <B>Keywords:</B> identity <br>
 * <B>Assertion ID:</B> A12.5.6-10 <br>
 * <B>Assertion Description: </B> The PersistenceManager.getObjectId method returns an ObjectId
 * instance that re presents the object identity of the specified JDO Instance. Test: The method g
 * etObjectById returns the exact same object, evaluating to true when == is used .
 */
public class GetObjectId extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A12.5.6-10 (GetObjectId) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetObjectId.class);
  }

  /** */
  public void testGetObjectId() {
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      PCPoint p1 = new PCPoint(8, 8);

      tx = pm.currentTransaction();
      tx.begin();
      pm.makePersistent(p1);
      Object oid = pm.getObjectId(p1);
      tx.commit();

      tx.begin();
      Object obj = pm.getObjectById(oid, false);
      if (obj != p1) {
        fail(
            ASSERTION_FAILED,
            "m.getObjectById(oid, false) should find existing instance in pm cache.");
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
