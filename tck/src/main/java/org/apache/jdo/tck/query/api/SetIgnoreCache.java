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
 * <B>Title:</B> Set IgnoreCache <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6-12. <br>
 * <B>Assertion Description: </B> <code>Query.setIgnoreCache(boolean flag)</code> sets the
 * IgnoreCache option for queries.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SetIgnoreCache extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6-12 (SetIgnoreCache) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestSetIgnoreCache01() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    tx.setOptimistic(false);
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));

      if (debug) logger.debug("Pessimistic: IgnoreCache - Setting value = true");
      query.setIgnoreCache(true);
      if (query.getIgnoreCache()) {
        if (debug) logger.debug("Pessimistic: IgnoreCache - value = " + query.getIgnoreCache());
      } else {
        fail(
            ASSERTION_FAILED,
            "query.getIgnoreCache() returns false after setting the flag to true");
      }

      if (debug) logger.debug("Pessimistic: IgnoreCache - Setting value = false");
      query.setIgnoreCache(false);
      if (!query.getIgnoreCache()) {
        if (debug) logger.debug("Pessimistic: IgnoreCache - value = " + query.getIgnoreCache());
      } else {
        fail(
            ASSERTION_FAILED,
            "query.getIgnoreCache() returns true after setting the flag to false");
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
  public void runTestSetIgnoreCache02() {
    if (!isOptimisticSupported()) {
      if (debug) logger.debug("Optimistic tx not supported");
      return;
    }

    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.setOptimistic(true);
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));

      if (debug) logger.debug("Optimistic: IgnoreCache - Setting value = true");
      query.setIgnoreCache(true);
      if (query.getIgnoreCache()) {
        if (debug) logger.debug("Optimistic: IgnoreCache - value = " + query.getIgnoreCache());
      } else {
        fail(
            ASSERTION_FAILED,
            "query.getIgnoreCache() returns false after setting the flag to true");
      }

      if (debug) logger.debug("Optimistic: IgnoreCache - Setting value = false");
      query.setIgnoreCache(false);
      if (!query.getIgnoreCache()) {
        if (debug) logger.debug("Optimistic: IgnoreCache - value = " + query.getIgnoreCache());
      } else {
        fail(
            ASSERTION_FAILED,
            "query.getIgnoreCache() returns true after setting the flag to false");
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
