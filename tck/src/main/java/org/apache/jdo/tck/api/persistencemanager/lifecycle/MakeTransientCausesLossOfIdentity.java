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
import java.util.HashSet;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> MakeTransientCausesLossOfIdentity <br>
 * <B>Keywords:</B> transient identity <br>
 * <B>Assertion IDs:</B> A12.5.7-14 <br>
 * <B>Assertion Description: </B> PersistenceManager.makeTransient and makeTransientAll transition
 * an instance to transient, causing it to lose its JDO identity.
 */
public class MakeTransientCausesLossOfIdentity extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-14 (MakeTransientCausesLossOfIdentity) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MakeTransientCausesLossOfIdentity.class);
  }

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;

  /** */
  public void testMakeTransientCausesLossOfIdentity() {
    pm = getPM();

    createPersistentObjects(pm);
    runTestMakeTransientCausesLossOfIdentity1(pm);
    runTestMakeTransientCausesLossOfIdentityAll1(pm);
    runTestMakeTransientCausesLossOfIdentityAll2(pm);

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
  private void runTestMakeTransientCausesLossOfIdentity1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx = pm.currentTransaction();
      tx.begin();
      pm.makeTransient(p1);
      tx.commit();

      Object oid = pm.getObjectId(p1);
      if (oid != null) {
        fail(
            ASSERTION_FAILED,
            "pm.getObjectId(p1) returned non null ObjectId " + oid + " for transient instance");
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makeTransientAll (Collection pcs) */
  private void runTestMakeTransientCausesLossOfIdentityAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx = pm.currentTransaction();
      tx.begin();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p2);
      col1.add(p3);

      pm.makeTransientAll(col1);
      tx.commit();

      Object oid1 = pm.getObjectId(p2);
      if (oid1 != null) {
        fail(
            ASSERTION_FAILED,
            "pm.getObjectId(p2) returned non null ObjectId " + oid1 + " for transient instance");
      }
      Object oid2 = pm.getObjectId(p3);
      if (oid2 != null) {
        fail(
            ASSERTION_FAILED,
            "pm.getObjectId(p3) returned non null ObjectId " + oid2 + " for transient instance");
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makeTransientAll (Object[] o) */
  private void runTestMakeTransientCausesLossOfIdentityAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx = pm.currentTransaction();
      tx.begin();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p4);
      col1.add(p5);

      Object[] obj1 = col1.toArray();

      pm.makeTransientAll(obj1);
      tx.commit();

      Object oid1 = pm.getObjectId(p4);
      if (oid1 != null) {
        fail(
            ASSERTION_FAILED,
            "pm.getObjectId(p4) returned non null ObjectId " + oid1 + " for transient instance");
      }
      Object oid2 = pm.getObjectId(p5);
      if (oid2 != null) {
        fail(
            ASSERTION_FAILED,
            "pm.getObjectId(p5) returned non null ObjectId " + oid2 + " for transient instance");
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
