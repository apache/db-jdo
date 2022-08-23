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

import java.util.Collection;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint2;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> EvictingCollectionOfInstancesSideEffects <br>
 * <B>Keywords:</B> cache <br>
 * <B>Assertion IDs:</B> A12.5.1-4 <br>
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
public class EvictingCollectionOfInstancesSideEffects extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.1-4 (EvictingCollectionOfInstancesSideEffects) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(EvictingCollectionOfInstancesSideEffects.class);
  }

  private PCPoint2 pnt1 = null;
  private PCPoint2 pnt2 = null;
  private PCPoint2 p1 = null;
  private PCPoint2 p2 = null;

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PCPoint2.class);
    super.localSetUp();
  }

  /** */
  public void testEvictingCollectionOfInstancesSideEffects() {
    pm = getPM();
    createObjects(pm);
    runTestEvictAllCollection(pm);
    runTestEvictAllArray(pm);
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
      pnt1 = new PCPoint2(1, 2);
      pnt2 = new PCPoint2(2, 3);
      p1 = new PCPoint2(3, 1);
      p2 = new PCPoint2(4, 2);
      pm.makePersistent(pnt1);
      pm.makePersistent(pnt2);
      pm.makePersistent(p1);
      pm.makePersistent(p2);
      tx.commit();

      // P-nontransactional instance
      // Check whether pmf supported optimitic tx
      tx.setOptimistic(isOptimisticSupported());
      tx.begin();
      pnt1.getX();
      pnt2.getX();
      tx.commit();

      /* P-clean instance */
      tx.setOptimistic(false);
      tx.begin();
      p1.getX();
      p2.getX();
      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestEvictAllCollection(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection col1 = new java.util.HashSet();
      col1.add(pnt1);
      col1.add(p1);

      pm.evictAll(col1);

      if (!p1.wasClearCalled()) {
        fail(ASSERTION_FAILED, "missing call of p1.jdoPreClear during pm.evictAll(Collection)");
      }
      if (!pnt1.wasClearCalled()) {
        fail(ASSERTION_FAILED, "missing call of pnt1.jdoPreClear during pm.evictAll(Collection)");
      }

      if (testState(p1, HOLLOW, "hollow")
          || testState(p1, PERSISTENT_NONTRANSACTIONAL, "persistent_nontransactional")) {
        ; // expected result
      } else {
        fail(ASSERTION_FAILED, "p1 should be HOLLOW or P-NONTX after pm.evictAll(Collection).");
      }

      if (testState(pnt1, HOLLOW, "hollow")
          || testState(pnt1, PERSISTENT_NONTRANSACTIONAL, "persistent_nontransactional")) {
        ; // expected result
      } else {
        fail(ASSERTION_FAILED, "pnt1 should be HOLLOW or P-NONTX after pm.evictAll(Collection).");
      }

      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestEvictAllArray(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection col1 = new java.util.HashSet();
      col1.add(pnt2);
      col1.add(p2);

      pm.evictAll(col1.toArray());

      if (!p2.wasClearCalled()) {
        fail(ASSERTION_FAILED, "missing call of p2.jdoPreClear during pm.evictAll(Object[])");
      }
      if (!pnt2.wasClearCalled()) {
        fail(ASSERTION_FAILED, "missing call of pnt2.jdoPreClear during pm.evictAll(Object[])");
      }

      if (testState(p2, HOLLOW, "hollow")
          || testState(p2, PERSISTENT_NONTRANSACTIONAL, "persistent_nontransactional")) {
        ; // expected result
      } else {
        fail(ASSERTION_FAILED, "p2 should be HOLLOW or P-NONTX after pm.evictAll(Object[]).");
      }

      if (testState(pnt2, HOLLOW, "hollow")
          || testState(pnt2, PERSISTENT_NONTRANSACTIONAL, "persistent_nontransactional")) {
        ; // expected result
      } else {
        fail(ASSERTION_FAILED, "pnt2 should be HOLLOW or P-NONTX after pm.evictAll(Object[]).");
      }

      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }
}
