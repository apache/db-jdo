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
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> MakeTransient <br>
 * <B>Keywords:</B> transient <br>
 * <B>Assertion IDs:</B> A12.5.7-13 <br>
 * <B>Assertion Description: </B> PersistenceManager.makeTransient and makeTransientAll make a
 * persistent instance transient, so it is not associated with the PersistenceManager instance. It
 * does not affect the persistent state in the data store.
 */
public class MakeTransient extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A12.5.7-13 (MakeTransient) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MakeTransient.class);
  }

  private PCPoint p1 = null;
  private PCPoint p2 = null;
  private PCPoint p3 = null;
  private PCPoint p4 = null;
  private PCPoint p5 = null;

  /** */
  public void testMakeTransient() {
    pm = getPM();

    createPersistentObjects(pm);
    runTestMakeTransient1(pm);
    runTestMakeTransientAll1(pm);
    runTestMakeTransientAll2(pm);

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
  private void runTestMakeTransient1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      pm.makeTransient(p1);
      tx.commit();
      tx = null;

      if (!testState(p1, TRANSIENT, "transient")) {
        fail("expected TRANSIENT instance, instance " + p1 + " is " + getStateOfInstance(p1));
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makeTransientAll (Collection pcs) */
  private void runTestMakeTransientAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p2);
      col1.add(p3);

      pm.makeTransientAll(col1);
      tx.commit();
      tx = null;

      for (PCPoint p : col1) {
        if (!testState(p, TRANSIENT, "transient")) {
          fail("expected TRANSIENT instance, instance " + p1 + " is " + getStateOfInstance(p));
        }
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makeTransientAll (Object[] o) */
  private void runTestMakeTransientAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      int NUM_OBJS = 2;
      tx.begin();

      Collection<PCPoint> col1 = new HashSet<>();
      col1.add(p4);
      col1.add(p5);
      Object[] obj1 = col1.toArray();

      pm.makeTransientAll(obj1);
      tx.commit();
      tx = null;

      for (int i = 0; i < NUM_OBJS; ++i) {
        PCPoint p = (PCPoint) obj1[i];
        if (!testState(p, TRANSIENT, "transient")) {
          fail("expected TRANSIENT instance, instance " + p1 + " is " + getStateOfInstance(p));
        }
      }
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
