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
import java.util.HashSet;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.Point;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>MakeTransientAll Fail <br>
 * <B>Keywords:</B> exception <br>
 * <B>Assertion IDs:</B> A12.5.7-3 <br>
 * <B>Assertion Description: </B> If a collection or array of instances is passed to
 * PersistenceManager.makeTransientAll, and one or more of the instances fail to complete the
 * required operation, then all instances will be attempted, and a JDOUserException will be thrown
 * which contains a nested exception array, each exception of which contains one of the failing
 * instances. The succeeding instances will transition to the specified life cycle state, and the
 * failing instances will remain in their current state.
 */
public class MakeTransientAllFails extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.7-3 (MakeTransientAllFails) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(MakeTransientAllFails.class);
  }

  /** */
  public void testMakeTransientAllFails() {
    pm = getPM();

    runTestMakeTransientAllFails1(pm);
    runTestMakeTransientAllFails2(pm);

    pm.close();
    pm = null;
  }

  /* test makeTansientAll (Collection pcs) */
  private void runTestMakeTransientAllFails1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      PCPoint np1 = new PCPoint(3, 3);
      PCPoint np2 = new PCPoint(4, 4);
      Point np3 = new Point(5, 5);

      Collection col1 = new HashSet();
      col1.add(np1);
      col1.add(np2);
      col1.add(np3);

      try {
        pm.makeTransientAll(col1);
        fail(
            ASSERTION_FAILED,
            "pm.makeTransientAll(Collection) should throw JDOUserException when called for a collection including an instance of a non-pc class");
      } catch (JDOUserException ex) {
        // expected exception
      }

      tx.rollback();
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test makeTransientAll (Object[] o) */
  private void runTestMakeTransientAllFails2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      PCPoint np1 = new PCPoint(3, 3);
      PCPoint np2 = new PCPoint(4, 4);
      Point np3 = new Point(5, 5);

      Collection col1 = new java.util.HashSet();
      col1.add(np1);
      col1.add(np2);
      col1.add(np3);

      Object[] obj1 = col1.toArray();

      try {
        pm.makeTransientAll(obj1);
        fail(
            ASSERTION_FAILED,
            "pm.makeTransientAll(Object[]) should throw JDOUserException when called for an array including an instance of a non-pc class");
      } catch (JDOUserException ex) {
        // expected exception
      }

      tx.rollback();
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
