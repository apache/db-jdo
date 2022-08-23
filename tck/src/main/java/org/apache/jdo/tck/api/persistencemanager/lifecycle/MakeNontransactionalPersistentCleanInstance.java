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
import java.util.Iterator;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;

/**
 * <B>Title:</B> Make Nontransactional a Persistent-Clean Instance <br>
 * <B>Keywords:</B> lifecycle <br>
 * <B>Assertion IDs:</B> A12.5.7-27 <br>
 * <B>Assertion Description: </B> PersistenceManager.makeNontransactional and
 * makeNontransactionalAll makes a persistent-clean instance nontransactional and causes a state
 * transition to persistent-nontransactional.
 */
public class MakeNontransactionalPersistentCleanInstance extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-27 (MakeNontransactionalPersistentCleanInstance) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MakeNontransactionalPersistentCleanInstance.class);
  }

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;
  private PCPoint p6 = null;
  private PCPoint p7 = null;

  private Collection col1 = new java.util.HashSet();
  private Collection col2 = new java.util.HashSet();

  /** */
  public void testTransactionalInstance() {
    pm = getPM();

    createObjects(pm);
    runTestMakeNontransactionalPersistentCleanInstance(pm);
    runTestMakeNontransactionalPersistentCleanInstanceAll1(pm);
    runTestMakeNontransactionalPersistentCleanInstanceAll2(pm);

    pm.close();
    pm = null;
  }

  /** */
  private void createObjects(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.setOptimistic(false);
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

      pm.makePersistent(p1);
      pm.makePersistent(p2);
      pm.makePersistent(p3);
      pm.makePersistent(p4);
      pm.makePersistent(p5);
      pm.makePersistent(p6);
      pm.makePersistent(p7);
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestMakeNontransactionalPersistentCleanInstance(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in testMakeNontransactionalPersistentCleanInstance()");
      int curr;
      tx.setOptimistic(false);
      tx.begin();

      if (makePersistentCleanInstance(p1)) {
        pm.makeNontransactional(p1);
        curr = currentState(p1);

        if (curr == HOLLOW || curr == PERSISTENT_NONTRANSACTIONAL) {
        } else {
          fail(
              ASSERTION_FAILED,
              "Excpected P-HOLLOW or P-NONTX instance, instance is " + getStateOfInstance(p1));
        }
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  private void runTestMakeNontransactionalPersistentCleanInstanceAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in testMakeNontransactionalPersistentCleanInstanceAll1()");
      int curr;

      tx.setOptimistic(false);
      tx.begin();

      if (makePersistentCleanInstance(p2)
          && makePersistentCleanInstance(p3)
          && makePersistentCleanInstance(p4)) {

        pm.makeNontransactionalAll(col1);
        Iterator iter = col1.iterator();
        while (iter.hasNext()) {
          PCPoint p = (PCPoint) iter.next();
          curr = currentState(p);
          if (curr == HOLLOW || curr == PERSISTENT_NONTRANSACTIONAL) {
          } else {
            fail(
                ASSERTION_FAILED,
                "Expected persistent-nontransactional or hollow; got " + getStateOfInstance(p));
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
  private void runTestMakeNontransactionalPersistentCleanInstanceAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      if (debug) logger.debug(" ** in testMakeNontransactionalPersistentCleanInstanceAll2()");
      int curr;
      if (tx.getOptimistic()) if (debug) logger.debug(" opt flag set to true");
      tx.begin();

      Object[] objArray = col2.toArray();

      if (makePersistentCleanInstance(p5)
          && makePersistentCleanInstance(p6)
          && makePersistentCleanInstance(p7)) {

        pm.makeNontransactionalAll(objArray);

        for (int i = 0; i < objArray.length; i++) {
          PCPoint p = (PCPoint) objArray[i];
          curr = currentState(p);
          if (curr == HOLLOW || curr == PERSISTENT_NONTRANSACTIONAL) {
          } else {
            fail(
                ASSERTION_FAILED,
                "Expected persistent-nontransactional or hollow; got " + getStateOfInstance(p));
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
  private boolean makePersistentCleanInstance(PCPoint obj) {
    int val = obj.getX();
    return (testState(obj, PERSISTENT_CLEAN, "persistent clean"));
  }
}
