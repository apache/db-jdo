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

package org.apache.jdo.tck.api.persistencemanager.lifecycle;

import java.util.Collection;
import java.util.HashSet;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> MakeTransactional <br>
 * <B>Keywords:</B> transienttransactional lifecycle <br>
 * <B>Assertion IDs:</B> A12.5.7-20 <br>
 * <B>Assertion Description: </B> PersistenceManager.makeTransactional and makeTransactionalAll
 * makes a transient instance transactional and causes a state transition to transient-clean. After
 * the method completes, the instance observes transaction boundaries.
 */
public class MakeTransactional extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-20 (MakeTransactional) failed: ";

  private PCPoint p = null;
  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;

  /** */
  @Test
  public void testTransactionalInst() {
    pm = getPM();

    createObjects();
    testMakeTransactional(pm);
    testMakeTransactionalAll1(pm);
    testMakeTransactionalAll2(pm);

    pm.close();
    pm = null;
  }

  /** */
  private void createObjects() {
    p1 = new PCPoint(1, 3);
    p2 = new PCPoint(2, 4);
    p3 = new PCPoint(3, 5);
    p4 = new PCPoint(4, 6);
  }

  /**
   * @param pm the PersistenceManager
   */
  public void testMakeTransactional(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      pm.makeTransactional(p1);
      if (currentState(p1) != TRANSIENT_CLEAN) {
        fail(
            ASSERTION_FAILED,
            "Expected T-CLEAN instance, instance is " + getStateOfInstance(p1) + ".");
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * @param pm the PersistenceManager
   */
  public void testMakeTransactionalAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p1);
      col1.add(p2);
      col1.add(p3);
      col1.add(p4);

      pm.makeTransactionalAll(col1);
      for (PCPoint p : col1) {
        if (currentState(p) != TRANSIENT_CLEAN) {
          fail(
              ASSERTION_FAILED,
              "Expected T-CLEAN instance, instance " + p + " is " + getStateOfInstance(p1) + ".");
        }
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void testMakeTransactionalAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Object[] objArray = {p1, p2, p3, p4};
      pm.makeTransactionalAll(objArray);

      for (Object o : objArray) {
        p = (PCPoint) o;
        if (currentState(p) != TRANSIENT_CLEAN) {
          fail(
              ASSERTION_FAILED,
              "Expected T-CLEAN instance, instance " + p + " is " + getStateOfInstance(p1) + ".");
        }
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
