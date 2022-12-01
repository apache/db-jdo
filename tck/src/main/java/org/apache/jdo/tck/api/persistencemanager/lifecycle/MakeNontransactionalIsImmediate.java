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
 * <B>Assertion IDs:</B> A12.5.7-29 <br>
 * <B>Assertion Description: </B> The effect of PersistenceManager.makeNontransactional or
 * makeNontransactionalAll is immediate and not subject to rollback.
 */
public class MakeNontransactionalIsImmediate extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-29 (MakeNontransactionalIsImmediate) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MakeNontransactionalIsImmediate.class);
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

    createTcleanObjects();
    runTestMakeNontransactionalIsImmediate(pm);
    runTestMakeNontransactionalIsImmediateAll1(pm);
    runTestMakeNontransactionalIsImmediateAll2(pm);

    pm.close();
    pm = null;
  }

  /** */
  private void createTcleanObjects() {
    Transaction tx = pm.currentTransaction();
    try {
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
  private void runTestMakeNontransactionalIsImmediate(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in runTestMakeNontransactionalIsImmediate()");
      int curr;
      tx.begin();

      if (testState(p1, TRANSIENT_CLEAN, "transient clean")) {
        pm.makeNontransactional(p1);
        if (!testState(p1, TRANSIENT, "transient")) {
          fail(
              ASSERTION_FAILED,
              "transient clean instance shoud become transient immediately after pm.makeNontransactional");
        }
      }
      tx.rollback();
      tx = null;

      if (!testState(p1, TRANSIENT, "transient")) {
        fail(ASSERTION_FAILED, "tx.rollback should rollback effect of pm.makeNontransactional");
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestMakeNontransactionalIsImmediateAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in runTestMakeNontransactionalIsImmediateAll1()");
      int curr;
      tx.begin();

      if (testState(p2, TRANSIENT_CLEAN, "transient clean")
          && testState(p3, TRANSIENT_CLEAN, "transient clean")
          && testState(p4, TRANSIENT_CLEAN, "transient clean")) {

        pm.makeNontransactionalAll(col1);
        if (!navigateAndTestTransientCol(col1)) {
          fail(
              ASSERTION_FAILED,
              "transient clean instances shoud become transient immediately after pm.makeNontransactionalAll(Collection)");
        }
      }

      tx.rollback();

      tx.begin();
      if (!navigateAndTestTransientCol(col1)) {
        fail(
            ASSERTION_FAILED,
            "tx.rollback should rollback effect of pm.makeNontransactionalAll(Collection)");
      }
      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestMakeNontransactionalIsImmediateAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in runTestMakeNontransactionalIsImmediateAll2()");
      int curr;
      tx.begin();
      Object[] objArray = col2.toArray();

      if (testState(p5, TRANSIENT_CLEAN, "transient clean")
          && testState(p6, TRANSIENT_CLEAN, "transient clean")
          && testState(p7, TRANSIENT_CLEAN, "transient clean")) {

        pm.makeNontransactionalAll(objArray);
        if (!navigateAndTestTransientArray(objArray)) {
          fail(
              ASSERTION_FAILED,
              "transient clean instances shoud become transient immediately after pm.makeNontransactionalAll(Object[])");
        }
      }
      tx.rollback();

      tx.begin();
      if (!navigateAndTestTransientArray(objArray)) {
        fail(
            ASSERTION_FAILED,
            "tx.rollback should rollback effect of pm.makeNontransactionalAll(Object[])");
      }
      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  private boolean navigateAndTestTransientCol(Collection<PCPoint> col) {
    boolean noError = true;
    for (PCPoint p : col1) {
      if (!testState(p, TRANSIENT, "transient")) {
        noError = false;
      }
    }
    return noError;
  }

  /** */
  private boolean navigateAndTestTransientArray(Object[] objArray) {
    boolean noError = true;
    for (Object o : objArray) {
      PCPoint p = (PCPoint) o;
      if (!testState(p, TRANSIENT, "transient")) {
        noError = false;
      }
    }
    return noError;
  }
}
