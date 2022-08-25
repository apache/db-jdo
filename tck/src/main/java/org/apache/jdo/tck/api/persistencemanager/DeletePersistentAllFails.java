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
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.Point;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> DeletePersistentAll Fails <br>
 * <B>Keywords:</B> exception <br>
 * <B>Assertion IDs:</B> A12.5.7-2. <br>
 * <B>Assertion Description: </B> If a collection or array of instances is passed to
 * PersistenceManager.deletePersistentAll, and one or more of the instances fail to complete the
 * required operation, then all instances will be attempted, and a JDOUserException will be thrown
 * which contains nested exceptions, each of which contains one of the failing instances. The
 * succeeding instances will transition to the specified life cycle state, and the failing instances
 * will remain in their current state.
 */
public class DeletePersistentAllFails extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-2 (DeletePersistentAllFails) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(DeletePersistentAllFails.class);
  }

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;

  /** */
  public void testDeletePersistentAllFails() {
    pm = getPM();
    createObjects(pm);
    runTestDeletePersistentAllFails1(pm);
    runTestDeletePersistentAllFails2(pm);
    pm.close();
    pm = null;
  }

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

  /* test deletePersistentAll (Collection pcs) */
  private void runTestDeletePersistentAllFails1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Point np3 = new Point(5, 5);

      Collection col1 = new java.util.HashSet();
      col1.add(p1);
      col1.add(p2);
      col1.add(np3);

      try {
        pm.deletePersistentAll(col1);
        fail(
            ASSERTION_FAILED,
            "pm.deletePersistentAll(Collection) with non persistent instances should throw an exception");
      } catch (JDOUserException ex) {
        // expected exception
      }
      tx.rollback();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /* test deletePersistentAll (Object[] o) */
  private void runTestDeletePersistentAllFails2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Point np3 = new Point(5, 5);

      Collection col1 = new java.util.HashSet();
      col1.add(p3);
      col1.add(p4);
      col1.add(np3);

      Object[] obj1 = col1.toArray();

      try {
        pm.deletePersistentAll(obj1);
        fail(
            ASSERTION_FAILED,
            "pm.deletePersistentAll(Object[]) with non persistent instances should throw an exception");
      } catch (JDOUserException ex) {
        // expected exception
      }
      tx.rollback();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }
}
