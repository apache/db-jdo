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
 * <B>Title:</B> make instance persistent <br>
 * <B>Keywords:</B> identity <br>
 * <B>Assertion IDs:</B> A12.5.7-6B <br>
 * <B>Assertion Description: </B> The method PersistenceManager.makePersistent and makePersistentAll
 * will assign an object identity to the instance and transitions it to persistent-new.
 */
public class MakePersistentAssignsObjectId extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-6B (MakePersistentAssignsObjectId) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MakePersistentAssignsObjectId.class);
  }

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;

  /** */
  public void testMakePersistentAssignsObjectId() {
    pm = getPM();

    createObjects();

    /* positive tests */
    runTestMakePersistentAssignsObjectId1(pm);
    runTestMakePersistentAssignsObjectId2(pm);
    runTestMakePersistentAssignsObjectId3(pm);

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
  }

  /* test makePersistent (Object pc) */
  private void runTestMakePersistentAssignsObjectId1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      pm.makePersistent(p1);

      if (!testState(p1, PERSISTENT_NEW, "persistent_new")) {
        fail(
            ASSERTION_FAILED,
            "expected P-NEW instance, instance is " + getStateOfInstance(p1) + ".");
      }
      if (pm.getObjectId(p1) == null) {
        fail(ASSERTION_FAILED, "pm.makePersistent should assign non-null oid.");
      }

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistentAll (Collection pcs) */
  private void runTestMakePersistentAssignsObjectId2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p2);
      col1.add(p3);

      pm.makePersistentAll(col1);

      for (PCPoint p : col1) {
        if (!testState(p, PERSISTENT_NEW, "persistent_new")) {
          fail(
              ASSERTION_FAILED,
              "expected P-NEW instance, instance is " + getStateOfInstance(p) + ".");
        }

        if (pm.getObjectId(p) == null) {
          fail(ASSERTION_FAILED, "pm.makePersistentAll should assign non-null oid.");
        }
      }

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistentAll (Object[] o) */
  private void runTestMakePersistentAssignsObjectId3(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      int NUM_OBJS = 2;
      tx.begin();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p4);
      col1.add(p5);

      Object[] obj1 = col1.toArray();

      pm.makePersistentAll(obj1);
      for (int i = 0; i < NUM_OBJS; i++) {
        PCPoint p = (PCPoint) obj1[i];
        if (!testState(p, PERSISTENT_NEW, "persistent_new")) {
          fail(
              ASSERTION_FAILED,
              "expected P-NEW instance, instance is " + getStateOfInstance(p) + ".");
        }

        if (pm.getObjectId(p) == null) {
          fail(ASSERTION_FAILED, "pm.makePersistentAll should assign non-null oid.");
        }
      }

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
