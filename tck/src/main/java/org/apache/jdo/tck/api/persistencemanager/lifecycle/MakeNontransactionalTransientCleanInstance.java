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
 * <B>Title:</B> MakeNontransactionalTransientCleanInstance <br>
 * <B>Keywords:</B> lifecycle <br>
 * <B>Assertion IDs:</B> A12.5.7-26 <br>
 * <B>Assertion Description: </B> PersistenceManager.makeNontransactional and
 * makeNontransactionalAll makes a transient-clean instance nontransactional and causes a state
 * transition to transient. After the method completes, the instance does not observe transaction
 * boundaries.
 */
public class MakeNontransactionalTransientCleanInstance extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-26 (MakeNontransactionalTransientCleanInstance) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MakeNontransactionalTransientCleanInstance.class);
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
    runTestMakeNontransactionalTransientCleanInstance(pm);
    runTestMakeNontransactionalTransientCleanInstanceAll1(pm);
    runTestMakeNontransactionalTransientCleanInstanceAll2(pm);

    pm.close();
    pm = null;
  }

  /** */
  private void createTcleanObjects(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx = pm.currentTransaction();
      tx.begin();
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
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestMakeNontransactionalTransientCleanInstance(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in testMakeNontransactionalTransientCleanInstance()");
      int curr;
      tx.begin();

      if (testState(p1, TRANSIENT_CLEAN, "transient clean")) {
        pm.makeNontransactional(p1);

        if (testState(p1, TRANSIENT, "transient")) {
          // expected result
        } else {
          fail(
              ASSERTION_FAILED,
              "expected transient instance after pm.makeNontransactional, instance is "
                  + getStateOfInstance(p1));
        }
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestMakeNontransactionalTransientCleanInstanceAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in testMakeNontransactionalTransientCleanInstanceAll1()");
      int curr;
      tx.begin();

      if (testState(p2, TRANSIENT_CLEAN, "transient clean")
          && testState(p3, TRANSIENT_CLEAN, "transient clean")
          && testState(p4, TRANSIENT_CLEAN, "transient clean")) {

        pm.makeNontransactionalAll(col1);
        for (PCPoint p : col1) {
          if (testState(p, TRANSIENT, "transient")) {
            // expected result
          } else {
            fail(
                ASSERTION_FAILED,
                "expected transient instance after pm.makeNontransactionalAll, instance is "
                    + getStateOfInstance(p));
          }
        }
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestMakeNontransactionalTransientCleanInstanceAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in testMakeNontransactionalTransientCleanInstanceAll2()");
      int curr;
      tx.begin();

      Object[] objArray = col2.toArray();

      if (testState(p5, TRANSIENT_CLEAN, "transient clean")
          && testState(p6, TRANSIENT_CLEAN, "transient clean")
          && testState(p7, TRANSIENT_CLEAN, "transient clean")) {

        pm.makeNontransactionalAll(objArray);

        for (Object o : objArray) {
          PCPoint p = (PCPoint) o;
          if (testState(p, TRANSIENT, "transient")) {
            // expected result
          } else {
            fail(
                ASSERTION_FAILED,
                "expected transient instance after pm.makeNontransactionalAll, instance is "
                    + getStateOfInstance(p));
          }
        }
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
