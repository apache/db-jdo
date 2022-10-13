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

package org.apache.jdo.tck.api.persistencemanager.cache;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Refresh All With No Parameters <br>
 * <B>Keywords:</B> cache <br>
 * <B>Assertion ID:</B> A12.5.1-6. <br>
 * <B>Assertion Description: </B> The <code>PersistenceManager.refreshAll</code> method with no
 * parameters causes all transactional instances to be refreshed. Note that this method will cause
 * loss of changes made to affected instances by the application due to refreshing the contents from
 * the data store. The JDO <code>PersistenceManager</code>:
 *
 * <UL>
 *   <LI>loads persistent values from the data store;
 *   <LI>loads persistent fields into the instance;
 *   <LI>calls the <code>jdoPostLoad</code> method on each persistent instance, if the class of the
 *       instance implements <code>InstanceCallbacks</code>; and
 *   <LI>changes the state of persistent-dirty instances to persistent-clean.
 * </UL>
 */
public class RefreshAllWithNoParameters extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.1-6 (RefreshAllWithNoParameters) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(RefreshAllWithNoParameters.class);
  }

  /** */
  public void test() {
    pm = getPM();

    runTestRefreshAll(pm);

    pm.close();
    pm = null;
  }

  /**
   * @param pm the PersistenceManager
   */
  private void runTestRefreshAll(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      PCPoint p = new PCPoint(100, 200);
      pm.makePersistent(p);
      tx.commit();

      tx.begin();
      p.setX(500);
      p.setY(Integer.valueOf(800));
      pm.refreshAll();
      int currentX = p.getX();
      Integer currentY = p.getY();
      if ((currentX != 100) || !currentY.equals(Integer.valueOf(200))) {
        fail(ASSERTION_FAILED, "RefreshAll expected 100, 200; got " + currentX + ", " + currentY);
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
