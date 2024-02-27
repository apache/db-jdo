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
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> MakePersistent Fails If Instance Managed By Another PersistenceManager <br>
 * <B>Keywords:</B> exception <br>
 * <B>Assertion IDs:</B> A12.5.7-8. <br>
 * <B>Assertion Description: </B> PersistenceManager.makePersistent and makePersistentAll will throw
 * a JDOUserException if the parameter instance is managed by a different PersistenceManager.
 */
public class MakePersistentFailsIfInstanceManagedByAnotherPersistenceManager
    extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-8 (MakePersistentFailsIfInstanceManagedByAnotherPersistenceManager) failed: ";

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;

  private Collection<PCPoint> pcPointCol;

  private PCPoint[] pcPointArray;

  /** */
  @Test
  public void testMakePersistentFailsIfInstanceManagedByAnotherPersistenceManager() {
    pm = getPM();

    PersistenceManager pm2 = getPMF().getPersistenceManager();
    try {
      createObjects(pm2);

      /* positive tests */
      runTestMakePersistent(pm);
      runTestMakePersistentAll1(pm);
      runTestMakePersistentAll2(pm);
    } finally {
      cleanupPM(pm2);
      pm2 = null;
      cleanupPM(pm);
      pm = null;
    }
  }

  /** */
  private void createObjects(PersistenceManager pm) {
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

      pcPointCol = new HashSet<>();
      pcPointCol.add(p2);
      pcPointCol.add(p3);

      pcPointArray = new PCPoint[2];
      pcPointArray[0] = p4;
      pcPointArray[0] = p5;

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistent (Object pc) */
  private void runTestMakePersistent(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      try {
        pm.makePersistent(p1);
        fail(
            ASSERTION_FAILED,
            "pm.makePersistent should throw JDOUserException if instance is already made persistence by different pm.");
      } catch (JDOUserException ex) {
        // expected exception
      }
      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistentAll (Collection pcs) */
  private void runTestMakePersistentAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      try {
        pm.makePersistentAll(pcPointCol);
        fail(
            ASSERTION_FAILED,
            "pm.makePersistentAll(Collection) should throw JDOUserException if instance is already made persistence by different pm.");
      } catch (JDOUserException ex) {
        // expected exception
      }
      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistentAll (Object[] o) */
  private void runTestMakePersistentAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      try {
        pm.makePersistentAll(pcPointArray);
        fail(
            ASSERTION_FAILED,
            "pm.makePersistentAll(Object[]) should throw JDOUserException if instance is already made persistence by different pm.");
      } catch (JDOUserException ex) {
        // expected exception
      }
      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
