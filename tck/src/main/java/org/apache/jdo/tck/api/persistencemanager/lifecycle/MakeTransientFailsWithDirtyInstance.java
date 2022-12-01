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
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>MakeTransientFailsWithDirtyInstance <br>
 * <B>Keywords:</B> exception <br>
 * <B>Assertion IDs:</B> A12.5.7-16 <br>
 * <B>Assertion Description: </B> If the instance passed to PersistenceManager.makeTransient or
 * makeTransientAll is dirty, a JDOUserException is thrown.
 */
public class MakeTransientFailsWithDirtyInstance extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-16 (MakeTransientFailsWithDirtyInstance) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MakeTransientFailsWithDirtyInstance.class);
  }

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;

  /** */
  public void testMakeTransientFailsWithDirtyInstance() {
    pm = getPM();

    createObjects(pm);
    runTestMakeTransient(pm);
    runTestMakeTransientAll1(pm);
    runTestMakeTransientAll2(pm);

    pm.close();
    pm = null;
  }

  /** */
  private void createObjects(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx = pm.currentTransaction();
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

  /* test makeTansient (Object pc) */
  private void runTestMakeTransient(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      // make instance dirty
      p1.setX(100);

      try {
        pm.makeTransient(p1);
        fail(
            ASSERTION_FAILED,
            "pm.makeTransient should throw JDOUserException when called for P-DIRTY instance.");
      } catch (JDOUserException ex) {
        // expected exception
      }
      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makeTansientAll (Collection pcs) */
  private void runTestMakeTransientAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {

      tx = pm.currentTransaction();
      tx.begin();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p2);
      col1.add(p3);

      p2.setX(200);
      p3.setX(201);

      try {
        pm.makeTransientAll(col1);
        fail(
            ASSERTION_FAILED,
            "pm.makeTransientAll(Collection) should throw JDOUserException when called for a collection including P-DIRTY instances.");
      } catch (JDOUserException ex) {
        // expected exception
      }
      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makeTransientAll (Object[] o) */
  private void runTestMakeTransientAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p4);
      col1.add(p5);

      p4.setX(300);
      p5.setX(301);

      Object[] obj1 = col1.toArray();

      try {
        pm.makeTransientAll(obj1);
        fail(
            ASSERTION_FAILED,
            "pm.makeTransientAll(Object[]) should throw JDOUserException when called for an array including P-DIRTY instances.");
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
