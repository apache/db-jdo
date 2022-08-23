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

package org.apache.jdo.tck.api.persistencemanager;

import java.util.Collection;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> DeletePersistent Has No Effect On Deleted Instances <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A12.5.7-10. <br>
 * <B>Assertion Description: </B> PersistenceManager.deletePersistent and deletePersistentAll have
 * no effect on parameter instances already deleted in the transaction.
 */
public class DeletePersistentHasNoEffectOnDeletedInstances extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-10 (DeletePersistentHasNoEffectOnDeletedInstances) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(DeletePersistentHasNoEffectOnDeletedInstances.class);
  }

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;

  /** */
  public void testDeletePersistentHasNoEffectOnDeletedInstances() {
    pm = getPM();
    createObjects(pm);
    runTestDeletePersistent(pm);
    runTestDeletePersistentAll1(pm);
    runTestDeletePersistentAll2(pm);
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

      pm.makePersistent(p1);
      pm.makePersistent(p2);
      pm.makePersistent(p3);
      pm.makePersistent(p4);
      pm.makePersistent(p5);

      tx.commit();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistent (Object pc) */
  private void runTestDeletePersistent(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      pm.deletePersistent(p1);
      pm.deletePersistent(p1);
      pm.deletePersistent(p1);
      pm.deletePersistent(p1);
      tx.commit();
      if (debug) logger.debug(" \nPASSED in testDeletePersistent()");
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistentAll (Collection pcs) */
  private void runTestDeletePersistentAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx = pm.currentTransaction();

      Collection col1 = new java.util.HashSet();
      col1.add(p2);
      col1.add(p3);

      tx.begin();
      pm.deletePersistentAll(col1);
      pm.deletePersistentAll(col1);
      pm.deletePersistentAll(col1);
      pm.deletePersistentAll(col1);
      tx.commit();
      if (debug) logger.debug(" \nPASSED in testDeletePersistentAll1()");
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /* test makePersistentAll (Object[] o) */
  private void runTestDeletePersistentAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      Collection col1 = new java.util.HashSet();
      col1.add(p4);
      col1.add(p5);

      Object[] obj1 = col1.toArray();

      tx.begin();
      pm.deletePersistentAll(obj1);
      pm.deletePersistentAll(obj1);
      pm.deletePersistentAll(obj1);
      pm.deletePersistentAll(obj1);
      tx.commit();
      if (debug) logger.debug(" \nPASSED in testDeletePersistentAll2()");
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }
}
