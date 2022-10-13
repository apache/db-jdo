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
import java.util.HashSet;
import javax.jdo.JDOUserException;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> DeletePersistent Fails If Instance Is Transient <br>
 * <B>Keywords:</B> exception <br>
 * <B>Assertion IDs:</B> A12.5.7-12. <br>
 * <B>Assertion Description: </B> PersistenceManager.deletePersistent and deletePersistentAll will
 * throw a JDOUserException if the parameter instance is transient.
 */
public class DeletePersistentFailsIfInstanceIsTransient extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-12 (DeletePersistentFailsIfInstanceIsTransient) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(DeletePersistentFailsIfInstanceIsTransient.class);
  }

  /** */
  public void testDeletePersistentFailsIfInstanceIsTransient() {
    pm = getPM();
    runTestDeletePersistent();
    runTestDeletePersistentAll1();
    runTestDeletePersistentAll2();
    pm.close();
    pm = null;
  }

  /* test deletePersistent (Object pc) */
  private void runTestDeletePersistent() {
    Transaction tx = pm.currentTransaction();
    try {

      tx = pm.currentTransaction();
      tx.begin();
      PCPoint p1 = new PCPoint(3, 3);
      try {
        pm.deletePersistent(p1);
        fail(
            ASSERTION_FAILED,
            "pm.deletePersistent(Object) with transient instance should throw exception");
      } catch (JDOUserException ex) {
        // expected exception
      }
      tx.rollback();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /* test deletePersistentAll (Collection pcs) */
  private void runTestDeletePersistentAll1() {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      PCPoint p1 = new PCPoint(3, 3);
      PCPoint p2 = new PCPoint(4, 4);

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p1);
      col1.add(p2);

      try {
        pm.deletePersistentAll(col1);
        fail(
            ASSERTION_FAILED,
            "pm.deletePersistent(Collection) with transient instance(s) should throw exception");
      } catch (JDOUserException ex) {
        // expected exception
      }
      tx.rollback();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }

  /* test deletePersistentAll (Object[] o) */
  private void runTestDeletePersistentAll2() {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      PCPoint p1 = new PCPoint(3, 3);
      PCPoint p2 = new PCPoint(4, 4);

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p1);
      col1.add(p2);

      try {
        pm.deletePersistentAll(col1.toArray());
        fail(
            ASSERTION_FAILED,
            "pm.deletePersistent(Object[]) with transient instance(s) should throw exception");
      } catch (JDOUserException ex) {
        // expected exception
      }
      tx.rollback();
    } finally {
      if (tx.isActive()) tx.rollback();
    }
  }
}
