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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import javax.jdo.FetchPlan;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCClass;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Get Fetch Plan. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6-21. <br>
 * <B>Assertion Description: </B> This method retrieves the fetch plan associated with the Query. It
 * always returns the identical instance for the same Query instance. Any change made to the fetch
 * plan affects subsequent query execution.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Get extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED_21 = "Assertion A14.6-21 (FetchPan) failed: ";

  /** */
  private static final String ASSERTION_FAILED_13 = "Assertion A14.6-13 (GetIgnoreCache) failed: ";

  /** */
  private static final String ASSERTION_FAILED_2 =
      "Assertion A14.6-2 (GetPersistenceManager) failed: ";

  /** */
  private static final String ASSERTION_FAILED_3 =
      "Assertion A14.6-3 (GetPersistenceManagerFromRestoredSerializedQuery) failed: ";

  private static final String FETCH_GROUP_1 = "fetchGroup1";
  private static final String FETCH_GROUP_2 = "fetchGroup2";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testFetchGroup1() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<PCClass> query = createQuery(pm);
      checkSameFetchPlanInstances(query);
      checkFetchGroup1(query);
    } finally {
      cleanupPM(pm);
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testFetchGroup2() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<PCClass> query = createQuery(pm);
      checkFetchGroup2(query);
      checkFetchGroup1(query);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  private Query<PCClass> createQuery(PersistenceManager pm) {
    Query<PCClass> query = pm.newQuery(PCClass.class, "true");
    query.getFetchPlan().setGroup(FETCH_GROUP_1);
    return query;
  }

  private void checkSameFetchPlanInstances(Query<PCClass> query) {
    FetchPlan fetchPlan1 = query.getFetchPlan();
    FetchPlan fetchPlan2 = query.getFetchPlan();
    if (fetchPlan1 != fetchPlan2) {
      fail(
          ASSERTION_FAILED_21
              + "Calling getFetchPlan twice on the same "
              + "query instance results in two different fetch plan instances.");
    }
  }

  /**
   * Checks if the given query loads fields assigned to fetchGroup1
   *
   * @param query the query
   */
  @SuppressWarnings("unchecked")
  private void checkFetchGroup1(Query<PCClass> query) {
    FetchPlan fetchplan = query.getFetchPlan();
    Collection<String> fetchgroups = fetchplan.getGroups();
    Assertions.assertTrue(
        fetchgroups.contains(FETCH_GROUP_1) && !fetchgroups.contains(FETCH_GROUP_2),
        "FetchPlan should include fetchGroup1 and not fetchGroup2");
  }

  /**
   * Checks if the given query loads fields assigned to "fetchGroup1" plus fetch group
   * "fetchGroup2". For this purpose, the method temporarily adds fetch group "fetchGroup2" to the
   * fetch plan of the given query instance. That fetch group loads field number2. Finally, that
   * fetch group is removed from the fetch plan again.
   *
   * @param query the query
   */
  @SuppressWarnings("unchecked")
  private void checkFetchGroup2(Query<PCClass> query) {
    FetchPlan fetchplan = query.getFetchPlan();
    fetchplan.addGroup(FETCH_GROUP_2);
    Collection<String> fetchgroups = fetchplan.getGroups();
    try {
      Assertions.assertTrue(
          fetchgroups.contains(FETCH_GROUP_1) && fetchgroups.contains(FETCH_GROUP_2),
          "FetchPlan should include fetchGroup1 and fetchGroup2");
    } finally {
      query.getFetchPlan().removeGroup(FETCH_GROUP_2);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testGetIgnoreCache01() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<?> query = pm.newQuery();

      // the ignoreCache flag of PM must default to the PMF setting
      if (pm.getIgnoreCache() == pmf.getIgnoreCache()) {
        if (debug) logger.debug("PM ignoreCache defaults to the PMF setting.");
      } else {
        fail(ASSERTION_FAILED_13, "PM ignoreCache does NOT default to the PMF setting.");
      }
      // the ignoreCache flag of Query must default to the setting in PM
      if (query.getIgnoreCache() == pm.getIgnoreCache()) {
        if (debug) logger.debug("Query ignoreCache defaults to the PM setting.");
      } else {
        fail(ASSERTION_FAILED_13, "Query ignoreCache does NOT default to the PM setting.");
      }

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testGetIgnoreCache02() {
    PersistenceManager pm = getPMF().getPersistenceManager();

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
        fail(ASSERTION_FAILED_13, "Query ignoreCache flag is NOT equal to new PM setting.");
      }

      // change the Query's ignoreCache flag
      query.setIgnoreCache(ignoreCacheDefault);
      if (query.getIgnoreCache() == ignoreCacheDefault) {
        if (debug) logger.debug("Query ignoreCache flag successfully changed");
      } else {
        fail(ASSERTION_FAILED_13, "Could NOT change the Query's ignoreCache flag.");
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testGetPersistenceManager01() {
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
            ASSERTION_FAILED_2,
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
  public void testGetPersistenceManager02() {
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
            ASSERTION_FAILED_2,
            "Optimistic: query.getPersistenceManager() returned different pm than the one it was created from.");
      }

      query.compile();
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testGetPersistenceManagerFromRestoredSerializedQuery()
      throws IOException, ClassNotFoundException {
    if (debug)
      logger.debug("\nExecuting test GetPersistenceManagerFromRestoredSerializedQuery() ...");

    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      Query<PCPoint> restoredQuery = null;
      ObjectInputStream ois = null;
      PersistenceManager pm1 = null;

      serializeQuery(pm);

      try {
        if (debug) logger.debug("Attempting to de-serialize Query object.");
        ois = new ObjectInputStream(new FileInputStream(SERIALZED_QUERY));
        restoredQuery = (Query<PCPoint>) ois.readObject();
        if (restoredQuery == null) {
          fail(ASSERTION_FAILED_3, "Deserialzed query is null");
        }
        if (debug) logger.debug("Query object restored.");
      } finally {
        if (ois != null) {
          try {
            ois.close();
          } catch (Exception ignored) {
          }
        }
      }

      pm1 = restoredQuery.getPersistenceManager();
      if (pm1 == null) {
        if (debug) logger.debug("Test GetPersistenceManagerFromRestoredSerializedQuery(): Passed");
      } else {
        fail(ASSERTION_FAILED_3, "Deserialzed query instance should not have a pm associated");
      }
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  void serializeQuery(PersistenceManager pm) throws IOException {
    Query<PCPoint> query = pm.newQuery(PCPoint.class);
    query.setCandidates(pm.getExtent(PCPoint.class, false));
    query.setFilter("x == 3");
    query.compile();

    ObjectOutputStream oos = null;
    try {
      if (debug) logger.debug("Attempting to serialize Query object.");
      oos = new ObjectOutputStream(new FileOutputStream(SERIALZED_QUERY));
      oos.writeObject(query);
      if (debug) logger.debug("Query object serialized.");
    } finally {
      if (oos != null) {
        try {
          oos.close();
        } catch (Exception ignored) {
        }
      }
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
