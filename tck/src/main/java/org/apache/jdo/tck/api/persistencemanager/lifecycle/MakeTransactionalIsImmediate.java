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
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Make Nontransactional is immediate <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A12.5.7-25 <br>
 * <B>Assertion Description: </B> The effect of PersistenceManager.makeTransactional or
 * makeTransactionalAll is immediate and not subject to rollback.
 */
public class MakeTransactionalIsImmediate extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-25 (MakeTransactionalIsImmediate) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MakeTransactionalIsImmediate.class);
  }

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
  public void testTransactionalInst() {
    pm = getPM();

    createTcleanObjects(pm);
    runTestMakeTransactionalIsImmediate(pm);
    runTestMakeTransactionalIsImmediateAll1(pm);
    runTestMakeTransactionalIsImmediateAll2(pm);

    pm.close();
    pm = null;
  }

  /** */
  private void createTcleanObjects(PersistenceManager pm) {
    p1 = new PCPoint(1, 3);
    p2 = new PCPoint(2, 4);
    p3 = new PCPoint(3, 5);
    p4 = new PCPoint(4, 6);
    p5 = new PCPoint(5, 7);
    p6 = new PCPoint(6, 8);
    p7 = new PCPoint(7, 9);

    col1.add(p2);
    col1.add(p3);
    col1.add(p4);

    col2.add(p5);
    col2.add(p6);
    col2.add(p7);

    pm.makeTransactional(p1);
    pm.makeTransactional(p2);
    pm.makeTransactional(p3);
    pm.makeTransactional(p4);
    pm.makeTransactional(p5);
    pm.makeTransactional(p6);
    pm.makeTransactional(p7);
  }

  /** */
  private void runTestMakeTransactionalIsImmediate(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in runTestMakeTransactionalIsImmediate()");

      tx.begin();
      pm.makeTransactional(p1);
      tx.rollback();

      if (!testState(p1, TRANSIENT_CLEAN, "transient_clean")) {
        fail(
            ASSERTION_FAILED,
            "expected T-CLEAN instance, instance " + p1 + " is " + getStateOfInstance(p1));
      }
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestMakeTransactionalIsImmediateAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in runTestMakeTransactionalIsImmediateAll1()");
      tx = pm.currentTransaction();
      tx.begin();
      pm.makeTransactionalAll(col1);
      tx.rollback();

      for (PCPoint p : col1) {
        if (!testState(p, TRANSIENT_CLEAN, "transient_clean")) {
          fail(
              ASSERTION_FAILED,
              "expected T-CLEAN instance, instance " + p1 + " is " + getStateOfInstance(p));
        }
      }
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestMakeTransactionalIsImmediateAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in runTestMakeTransactionalIsImmediateAll2()");
      tx.begin();
      Object[] objArray = col2.toArray();
      pm.makeTransactionalAll(objArray);
      tx.rollback();

      for (Object o : objArray) {
        PCPoint p = (PCPoint) o;
        if (!testState(p, TRANSIENT_CLEAN, "transient_clean")) {
          fail(
              ASSERTION_FAILED,
              "expected T-CLEAN instance, instance " + p1 + " is " + getStateOfInstance(p));
        }
      }
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
