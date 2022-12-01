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

package org.apache.jdo.tck.api.persistencemanager.cache;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint2;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> EvictAllWithNoParameters <br>
 * <B>Keywords:</B> cache <br>
 * <B>Assertion IDs:</B> A12.5.1-2 <br>
 * <B>Assertion Description: </B> If PersistenceManager.evict is called with no parameter, then all
 * referenced instances are evicted. For each instance evicted, it:
 *
 * <UL>
 *   <LI>calls the jdoPreClearmethod on each instance, if the class of the instance implements
 *       InstanceCallbacks
 *   <LI>clears persistent fields on each instance after the call to jdoPreClear()
 *   <LI>changes the state of instances to hollow or persistent-nontransactional (cannot distinguish
 *       between these two states) this is not directly testable.
 * </UL>
 */
public class EvictAllWithNoParameters extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.1-2 (EvictAllWithNoParameters) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(EvictAllWithNoParameters.class);
  }

  private PCPoint2 pnt1 = null;
  private PCPoint2 p1 = null;

  /** */
  public void testEvictAllWithNoParameters() {
    pm = getPM();
    createObjects(pm);
    runTestEvictAll(pm);
    pm.close();
    pm = null;
  }

  /**
   * @param pm the PersistenceManager
   */
  private void createObjects(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      pnt1 = new PCPoint2(1, 3);
      p1 = new PCPoint2(3, 5);
      pm.makePersistent(pnt1);
      pm.makePersistent(p1);
      tx.commit();

      // P-nontransactional instance
      // Check whether pmf supported optimitic tx
      tx.setOptimistic(isOptimisticSupported());
      tx.begin();
      pnt1.getX();
      tx.commit();

      // P-clean instance
      tx.setOptimistic(false);
      tx.begin();
      p1.getX();
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestEvictAll(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      pm.evictAll();

      if (!p1.wasClearCalled()) {
        fail(ASSERTION_FAILED, "missing call of p1.jdoPreClear during pm.evictAll");
      }
      if (!pnt1.wasClearCalled()) {
        fail(ASSERTION_FAILED, "missing call of pnt1.jdoPreClear during pm.evictAll");
      }

      if (testState(p1, HOLLOW, "hollow")
          || testState(p1, PERSISTENT_NONTRANSACTIONAL, "persistent_nontransaction")) {
        // expected result
      } else {
        fail(ASSERTION_FAILED, "p1 should be HOLLOW or P-NONTX after pm.evictAll.");
      }

      if (testState(pnt1, HOLLOW, "hollow")
          || testState(pnt1, PERSISTENT_NONTRANSACTIONAL, "persistent_nontransaction")) {
        // expected result
      } else {
        fail(ASSERTION_FAILED, "pnt1 should be HOLLOW or P-NONTX after pm.evictAll.");
      }
      tx.commit();
      if (debug) logger.debug(" \nPASSED in testEvictAll()");
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }
}
