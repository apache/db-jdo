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
 * <B>Title:</B> MakePersistent Has No Effect On Persistent Instances <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A12.5.7-7. <br>
 * <B>Assertion Description: </B> PersistenceManager.makePersistent and makePersistentAll have no
 * effect on parameter persistent instances already managed by the same PersistenceManager.
 */
public class MakePersistentHasNoEffectOnPersistentInstances extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-7 (MakePersistentHasNoEffectOnPersistentInstances) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MakePersistentHasNoEffectOnPersistentInstances.class);
  }

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;

  /** */
  public void testMakePersistentHasNoEffectOnPersistentInstances() {
    pm = getPM();

    createObjects(pm);

    runTestMakePersistent(pm);
    runTestMakePersistentAll1(pm);
    runTestMakePersistentAll2(pm);

    pm.close();
    pm = null;
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
      tx = pm.currentTransaction();
      tx.begin();
      pm.makePersistent(p1);
      pm.makePersistent(p1);
      pm.makePersistent(p1);
      pm.makePersistent(p1);
      tx.commit();
      tx = null;
      if (debug) logger.debug(" \nPASSED in runTestMakePersistent()");
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistentAll (Collection pcs) */
  private void runTestMakePersistentAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection col1 = new java.util.HashSet();
      col1.add(p2);
      col1.add(p3);

      pm.makePersistentAll(col1);
      pm.makePersistentAll(col1);
      pm.makePersistentAll(col1);
      pm.makePersistentAll(col1);
      tx.commit();
      tx = null;
      if (debug) logger.debug(" \nPASSED in runTestMakePersistentAll1()");
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistentAll (Object[] o) */
  private void runTestMakePersistentAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection col1 = new java.util.HashSet();
      col1.add(p1);
      col1.add(p2);

      Object[] obj1 = col1.toArray();

      pm.makePersistentAll(obj1);
      pm.makePersistentAll(obj1);
      pm.makePersistentAll(obj1);
      pm.makePersistentAll(obj1);
      tx.commit();
      tx = null;
      if (debug) logger.debug(" \nPASSED in runTestMakePersistentAll2()");
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
