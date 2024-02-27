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
 * <B>Title:</B> MakeTransactionalWithinTransactionRolledback <br>
 * <B>Keywords:</B> transienttransactional <br>
 * <B>Assertion IDs:</B> A12.5.7-22 <br>
 * <B>Assertion Description: </B> If the transaction in which an instance is made transactional (by
 * calling PersistenceManager.makeTransactional or makeTransactionalAll) is rolled back, then the
 * transient instance takes its values as of the call to makeTransactional if the call was made
 * within the current transaction.
 */
public class MakeTransactionalWithinTransactionRolledback extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-22 (MakeTransactionalWithinTransactionRolledback) failed: ";

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;
  private PCPoint p6 = null;
  private PCPoint p7 = null;

  private final Collection<PCPoint> col1 = new HashSet<>();
  private final Collection<PCPoint> col2 = new HashSet<>();

  /** */
  @Test
  public void testTransactionalInst() {
    pm = getPM();
    pm.currentTransaction().setRestoreValues(true);

    createObjects();
    runTestMakeTransactionalWithinTransactionRolledback(pm);
    runTestMakeTransactionalWithinTransactionRolledbackAll1(pm);
    runTestMakeTransactionalWithinTransactionRolledbackAll2(pm);
    pm.close();
    pm = null;
  }

  /** */
  private void createObjects() {
    p1 = new PCPoint(1, 3);
    p2 = new PCPoint(2, 4);
    p3 = new PCPoint(3, 5);
    p4 = new PCPoint(4, 6);
    p5 = new PCPoint(5, 7);

    col1.add(p2);
    col1.add(p3);

    col2.add(p4);
    col2.add(p5);
  }

  /**
   * @param pm the PersistenceManager
   */
  public void runTestMakeTransactionalWithinTransactionRolledback(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in runTestMakeTransactionalWithinTransactionRolledback()");

      p1.setY(Integer.valueOf(100));
      tx.begin();
      p1.setY(Integer.valueOf(200));
      pm.makeTransactional(p1);
      p1.setY(Integer.valueOf(300));
      tx.rollback();

      if (p1.getY().intValue() != 200) {
        fail(
            ASSERTION_FAILED,
            "wrong value of p1.y, expected: 200, actual: " + p1.getY().intValue());
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * @param pm the PersistenceManager
   */
  private void runTestMakeTransactionalWithinTransactionRolledbackAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in runTestMakeTransactionalWithinTransactionRolledbackAll1()");
      p2.setY(Integer.valueOf(100));
      p3.setY(Integer.valueOf(100));
      tx.begin();
      p2.setY(Integer.valueOf(200));
      p3.setY(Integer.valueOf(200));
      pm.makeTransactionalAll(col1);
      p2.setY(Integer.valueOf(300));
      p3.setY(Integer.valueOf(300));
      tx.rollback();

      if (p2.getY().intValue() != 200) {
        fail(
            ASSERTION_FAILED, "wrong value of p2.y expected: 200, actual: " + p2.getY().intValue());
      }

      if (p3.getY().intValue() != 200) {
        fail(
            ASSERTION_FAILED,
            "wrong value of p3.y, expected: 200, actual: " + p3.getY().intValue());
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * @param pm the PersistenceManager
   */
  private void runTestMakeTransactionalWithinTransactionRolledbackAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in runTestMakeTransactionalWithinTransactionRolledbackAll2()");

      p4.setY(Integer.valueOf(100));
      p5.setY(Integer.valueOf(100));
      tx.begin();
      p4.setY(Integer.valueOf(200));
      p5.setY(Integer.valueOf(200));
      Object[] objArray = col2.toArray();
      pm.makeTransactionalAll(objArray);
      p4.setY(Integer.valueOf(300));
      p5.setY(Integer.valueOf(300));
      tx.rollback();

      if (p4.getY().intValue() != 200) {
        fail(ASSERTION_FAILED, "wrong value of p4, expected: 200, actual: " + p4.getY().intValue());
      }

      if (p5.getY().intValue() != 200) {
        fail(
            ASSERTION_FAILED,
            "wrong value of p5.y, expected: 200, actual: " + p5.getY().intValue());
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
