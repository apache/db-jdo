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

package org.apache.jdo.tck.api.persistencemanager.lifecycle;

import java.util.Collection;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> MakeTransientNotSubjectToRollback <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A12.5.7-17 <br>
 * <B>Assertion Description: </B> The effect of PersistenceManager.makeTransient or makeTransientAll
 * is immediate and not subject to rollback.
 */
public class MakeTransientNotSubjectToRollback extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-17 (MakeTransientNotSubjectToRollback) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MakeTransientNotSubjectToRollback.class);
  }

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;

  /** */
  public void testMakeTransientNotSubjectToRollback() {
    pm = getPM();

    createPersistentObjects(pm);
    runTestMakeTransientNotSubjectToRollback1(pm);
    runTestMakeTransientNotSubjectToRollbackAll1(pm);
    runTestMakeTransientNotSubjectToRollbackAll2(pm);

    pm.close();
    pm = null;
  }

  /** */
  private void createPersistentObjects(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      p1 = new PCPoint(1, 3);
      p2 = new PCPoint(2, 4);
      p3 = new PCPoint(3, 5);
      p4 = new PCPoint(4, 6);
      p5 = new PCPoint(5, 7);

      pm.makePersistent(p1);
      pm.makePersistent(p2);
      pm.makePersistent(p3);
      pm.makePersistent(p4);
      pm.makePersistent(p5);
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makeTransient (Object pc) */
  private void runTestMakeTransientNotSubjectToRollback1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      pm.makeTransient(p1);
      int curr1 = currentState(p1);
      tx.rollback();

      if (curr1 != currentState(p1)) {
        fail(
            ASSERTION_FAILED,
            "expected TRANSIENT instancew, instance " + p1 + " is " + getStateOfInstance(p1));
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makeTransientAll (Collection pcs) */
  private void runTestMakeTransientNotSubjectToRollbackAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection col1 = new java.util.HashSet();
      col1.add(p2);
      col1.add(p3);
      pm.makeTransientAll(col1);
      int p2_curr = currentState(p2);
      int p3_curr = currentState(p3);
      tx.rollback();

      if (p2_curr != currentState(p2)) {
        fail(
            ASSERTION_FAILED,
            "expected TRANSIENT instancew, instance " + p2 + " is " + getStateOfInstance(p2));
      }
      if (p3_curr != currentState(p3)) {
        fail(
            ASSERTION_FAILED,
            "expected TRANSIENT instancew, instance " + p3 + " is " + getStateOfInstance(p3));
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makeTransientAll (Object[] o) */
  private void runTestMakeTransientNotSubjectToRollbackAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection col1 = new java.util.HashSet();
      col1.add(p4);
      col1.add(p5);

      Object[] obj1 = col1.toArray();

      pm.makeTransientAll(obj1);
      int p4_curr = currentState(p4);
      int p5_curr = currentState(p5);
      tx.rollback();

      if (p4_curr != currentState(p4)) {
        fail(
            ASSERTION_FAILED,
            "expected TRANSIENT instancew, instance " + p4 + " is " + getStateOfInstance(p2));
      }
      if (p5_curr != currentState(p5)) {
        fail(
            ASSERTION_FAILED,
            "expected TRANSIENT instancew, instance " + p5 + " is " + getStateOfInstance(p5));
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
