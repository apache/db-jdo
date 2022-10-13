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

package org.apache.jdo.tck.api.persistencemanager.cache;

import java.util.Collection;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Passing Null to RefreshAll Throws Exception <br>
 * <B>Keywords:</B> cache <br>
 * <B>Assertion IDs:</B> A12.5-10. <br>
 * <B>Assertion Description: </B> Passing a null valued argument to refreshAll will throw a
 * NullPointerException.
 */
public class PassingNullToRefreshAllThrowsException extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5-10 (PassingNullToRefreshAllThrowsException) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(PassingNullToRefreshAllThrowsException.class);
  }

  /** */
  public void testPassingNullToRefreshAllThrowsException() {
    pm = getPM();

    runTestRefreshAll1(pm);
    runTestRefreshAll2(pm);

    pm.close();
    pm = null;
  }

  /**
   * test refreshAll (Collection pcs)
   *
   * @param pm the PersistenceManager
   */
  public void runTestRefreshAll1(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    if (debug) logger.debug("**  in runTestRefreshAll1() ");
    try {
      tx.begin();
      Collection<Object> col1 = null;
      try {
        pm.refreshAll(col1);
        fail(
            ASSERTION_FAILED,
            "pm.refreshAll should throw NullPointerException when called with argument null");
      } catch (NullPointerException ex) {
        // expected exception
      }
      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /* test refreshAll (Object[] objArray) */
  public void runTestRefreshAll2(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    if (debug) logger.debug("**  in runTestRefreshAll1() ");
    try {
      tx = pm.currentTransaction();
      tx.begin();
      Object[] arr = null;
      try {
        pm.refreshAll(arr);
        fail(
            ASSERTION_FAILED,
            "pm.refreshAll should throw NullPointerException when called with argument null");
      } catch (NullPointerException ex) {
        // expected exception
      }
      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
