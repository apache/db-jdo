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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * <B>Title:</B> Execute Query <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.1-3. <br>
 * <B>Assertion Description: </B> The <code>Query.execute</code> methods execute the query using the
 * parameters and return the result, which is a collection of instances that satisfy the boolean
 * filter. Each parameter of the <code>execute</code> method(s) is an object which is either the
 * value of the corresponding parameter or the wrapped value of a primitive parameter.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Execute extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED_3 = "Assertion A14.6.1-3 (ExecuteQuery) failed: ";

  /** */
  private static final String ASSERTION_FAILED_6 =
      "Assertion A14.6.1-6 (ExecuteQueryWithArray) failed: ";

  /** */
  private static final String ASSERTION_FAILED_5 =
      "Assertion A14.6.1-5 (ExecuteQueryWithMap) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestExecuteQuery01() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.setFilter("x == 4");
      List<PCPoint> results = query.executeList();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(4));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_3, "x == 4", results, expected);
      tx.commit();
      if (debug) logger.debug("Test ExecuteQuery01 - Passed\n");
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestExecuteQuery02() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("Integer param");
      query.setFilter("x == param");
      query.setParameters(Integer.valueOf(2));
      List<PCPoint> results = query.executeList();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(2));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_3, "x == param", results, expected);
      tx.commit();
      if (debug) logger.debug("Test ExecuteQuery02 - Passed\n");
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestExecuteQuery03() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("Integer param1, Integer param2");
      query.setFilter("x == param1 && y == param2");
      query.setParameters(Integer.valueOf(2), Integer.valueOf(2));
      List<PCPoint> results = query.executeList();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(2));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(
          ASSERTION_FAILED_3, "x == param1 && y == param2", results, expected);
      tx.commit();
      if (debug) logger.debug("Test ExecuteQuery03 - Passed\n");
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestExecuteQueryWithArray01() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {

      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("Integer param");
      query.setFilter("x == param");

      Object[] actualParams = {Integer.valueOf(2)};
      Object results = query.executeWithArray(actualParams);

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(2));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_6, "x == param", results, expected);

      tx.commit();
      if (debug) logger.debug("Test ExecuteQueryWithArray01 - Passed\n");
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestExecuteQueryWithArray02() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("Integer param1, Integer param2");
      query.setFilter("x == param1 && y == param2");

      Object[] actualParams = {Integer.valueOf(2), Integer.valueOf(2)};
      Object results = query.executeWithArray(actualParams);

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(2));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(
          ASSERTION_FAILED_6, "x == param1 && y == param2", results, expected);
      tx.commit();
      if (debug) logger.debug("Test ExecuteQueryWithArray02 - Passed\n");
    } finally {
      cleanupPM(pm);
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestExecuteQueryWithMap01() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("Integer param");
      query.setFilter("x == param");

      Map<String, Object> actualParams = new HashMap<>();
      actualParams.put("param", Integer.valueOf(2));
      Object results = query.executeWithMap(actualParams);

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(2));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED_5, "x == param", results, expected);
      tx.commit();
      if (debug) logger.debug("Test ExecuteQueryWithMap01 - Passed\n");
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestExecuteQueryWithMap02() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("Integer param1, Integer param2");
      query.setFilter("x == param1 && y == param2");

      Map<String, Object> actualParams = new HashMap<>();
      actualParams.put("param1", Integer.valueOf(2));
      actualParams.put("param2", Integer.valueOf(2));
      Object results = query.executeWithMap(actualParams);

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(2));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(
          ASSERTION_FAILED_5, "x == param1 && y == param2", results, expected);
      tx.commit();
      if (debug) logger.debug("Test ExecuteQueryWithMap02 - Passed\n");
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

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PCPoint.class);
    loadAndPersistPCPoints(getPM());
  }
}
