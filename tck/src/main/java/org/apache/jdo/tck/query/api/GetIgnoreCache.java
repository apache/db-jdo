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
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Get IgnoreCache <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6-13. <br>
 * <B>Assertion Description: </B> <code>Query.getIgnoreCache</code> returns the current setting of
 * the IgnoreCache option.
 */
public class GetIgnoreCache extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6-13 (GetIgnoreCache) failed: ";

  /** */
  @Test
  public void test() {
    pmf = getPMF();
    pm = getPM();

    runTestGetIgnoreCache01(pm);
    runTestGetIgnoreCache02(pm);

    pm.close();
    pm = null;
  }

  /** */
  void runTestGetIgnoreCache01(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<?> query = pm.newQuery();

      // the ignoreCache flag of PM must default to the PMF setting
      if (pm.getIgnoreCache() == pmf.getIgnoreCache()) {
        if (debug) logger.debug("PM ignoreCache defaults to the PMF setting.");
      } else {
        fail(ASSERTION_FAILED, "PM ignoreCache does NOT default to the PMF setting.");
      }
      // the ignoreCache flag of Query must default to the setting in PM
      if (query.getIgnoreCache() == pm.getIgnoreCache()) {
        if (debug) logger.debug("Query ignoreCache defaults to the PM setting.");
      } else {
        fail(ASSERTION_FAILED, "Query ignoreCache does NOT default to the PM setting.");
      }

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  void runTestGetIgnoreCache02(PersistenceManager pm) {

    boolean ignoreCacheDefault = pmf.getIgnoreCache();
    // set PM's ignoreCache to a different value
    pm.setIgnoreCache(!ignoreCacheDefault);

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<?> query = pm.newQuery();

      // Query.ignoreCache must be equal to the new value in PM
      if (query.getIgnoreCache() == !ignoreCacheDefault) {
        if (debug) logger.debug("Query ignoreCache flag is equal to new PM setting.");
      } else {
        fail(ASSERTION_FAILED, "Query ignoreCache flag is NOT equal to new PM setting.");
      }

      // change the Query's ignoreCache flag
      query.setIgnoreCache(ignoreCacheDefault);
      if (query.getIgnoreCache() == ignoreCacheDefault) {
        if (debug) logger.debug("Query ignoreCache flag successfully changed");
      } else {
        fail(ASSERTION_FAILED, "Could NOT change the Query's ignoreCache flag.");
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
