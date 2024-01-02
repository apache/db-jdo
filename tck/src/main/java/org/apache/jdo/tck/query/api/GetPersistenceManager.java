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

package org.apache.jdo.tck.query.api;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Get PersistenceManager <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6-2. <br>
 * <B>Assertion Description: </B> <code>Query.getPersistenceManager()</code> returns the associated
 * <code>PersistenceManager</code> instance.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetPersistenceManager extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6-2 (GetPersistenceManager) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestGetPersistenceManager01() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.setOptimistic(false);
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));

      PersistenceManager pm1 = query.getPersistenceManager();
      if (pm.equals(pm1)) {
        if (debug) logger.debug("Pessimistic: GetPersistenceManager Test: Passed.");
      } else {
        fail(
            ASSERTION_FAILED,
            "Pessimistic: query.getPersistenceManager() returned different pm than the one it was created from.");
      }

      query.compile();
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestGetPersistenceManager02() {
    if (!isOptimisticSupported()) return;

    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.setOptimistic(true);
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));

      PersistenceManager pm1 = query.getPersistenceManager();
      if (pm.equals(pm1)) {
        if (debug) logger.debug("Optimistic: GetPersistenceManager Test: Passed");
      } else {
        fail(
            ASSERTION_FAILED,
            "Optimistic: query.getPersistenceManager() returned different pm than the one it was created from.");
      }

      query.compile();
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  @BeforeAll
  @Override
  protected void setUp() {
    super.setUp();
  }

  @AfterAll
  @Override
  protected void tearDown() {
    super.tearDown();
  }
}
