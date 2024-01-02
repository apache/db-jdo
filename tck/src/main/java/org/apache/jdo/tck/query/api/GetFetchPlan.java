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

import java.util.Collection;
import javax.jdo.FetchPlan;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.mylib.PCClass;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Assertions;
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
public class GetFetchPlan extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6-21 (FetchPan) failed: ";

  private static final String FETCH_GROUP_1 = "fetchGroup1";
  private static final String FETCH_GROUP_2 = "fetchGroup2";

  /** */
  private Query<PCClass> createQuery(PersistenceManager pm) {
    Query<PCClass> query = pm.newQuery(PCClass.class, "true");
    query.getFetchPlan().setGroup(FETCH_GROUP_1);
    return query;
  }

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

  private void checkSameFetchPlanInstances(Query<PCClass> query) {
    FetchPlan fetchPlan1 = query.getFetchPlan();
    FetchPlan fetchPlan2 = query.getFetchPlan();
    if (fetchPlan1 != fetchPlan2) {
      fail(
          ASSERTION_FAILED
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
}
