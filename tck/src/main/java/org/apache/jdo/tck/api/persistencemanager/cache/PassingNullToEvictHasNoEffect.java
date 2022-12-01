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

package org.apache.jdo.tck.api.persistencemanager.cache;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> PassingNullToEvictHasNoEffect <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A12.5-7 <br>
 * <B>Assertion Description: </B> Passing a null value to PersistenceManager.evict will have no
 * effect. A NullPointerException should NOT be thrown.
 */
public class PassingNullToEvictHasNoEffect extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5-7 (PassingNullToEvictHasNoEffect) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(PassingNullToEvictHasNoEffect.class);
  }

  /** */
  public void test() {
    pm = getPM();

    runTestPassingNullToEvictHasNoEffect(pm);

    pm.close();
    pm = null;
  }

  /**
   * test evict (object pc)
   *
   * @param pm the PersistenceManager
   */
  public void runTestPassingNullToEvictHasNoEffect(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      pm.evict(null);
      if (debug) logger.debug(" \nPASSED in testPassingNullToEvictHasNoEffect()");
      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
