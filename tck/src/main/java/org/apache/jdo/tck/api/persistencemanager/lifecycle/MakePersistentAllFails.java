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
import org.apache.jdo.tck.pc.mylib.Point;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> MakePersistentAll Fails <br>
 * <B>Keywords:</B> exception <br>
 * <B>Assertion IDs:</B> A12.5.7-1. <br>
 * <B>Assertion Description: </B> If a collection or array of instances is passed to
 * PersistenceManager.makePersistentAll, and one or more of the instances fail to complete the
 * operation, then all instances will be attempted, and a JDOUserException will be thrown which
 * contains a nested exception array, each exception of which contains one of the failing instances.
 * The succeeding instances will transition to the specified life cycle state, and the failing
 * instances will remain in their current state.
 */
public class MakePersistentAllFails extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-1 (MakePersistentAllFails) failed: ";

  /** */
  @Test
  public void testMakePersistentAllFails() {
    pm = getPM();

    runTestMakePersistentAllFails1(pm);
    runTestMakePersistentAllFails2(pm);

    pm.close();
    pm = null;
  }

  /* test makePersistentAll (Collection pcs) */
  private void runTestMakePersistentAllFails1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      PCPoint np1 = new PCPoint(3, 3);
      PCPoint np2 = new PCPoint(4, 4);
      Point np3 = new Point(5, 5);

      Collection<Object> col1 = new HashSet<>();
      col1.add(np1);
      col1.add(np2);
      col1.add(np3);

      try {
        pm.makePersistentAll(col1);
        fail(
            ASSERTION_FAILED,
            "pm.makePersistentAll(Collection) should thro JDOUserException when called for collection including instance of non-pc class.");
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
  private void runTestMakePersistentAllFails2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      PCPoint np1 = new PCPoint(3, 3);
      PCPoint np2 = new PCPoint(4, 4);
      Point np3 = new Point(5, 5);

      Collection<Object> col1 = new HashSet<>();
      col1.add(np1);
      col1.add(np2);
      col1.add(np3);

      Object[] obj1 = col1.toArray();

      try {
        pm.makePersistentAll(obj1);
        fail(
            ASSERTION_FAILED,
            "pm.makePersistentAll(Object[]) should thro JDOUserException when called for array including instance of non-pc class ");
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
