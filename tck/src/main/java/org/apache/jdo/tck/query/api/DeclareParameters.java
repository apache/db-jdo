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
import java.util.List;
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
 * <B>Title:</B> Declare Parameters <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6-10. <br>
 * <B>Assertion Description: </B> <code>Query.declareParameters(String parameters)</code> binds the
 * parameter statements to the query instance. This method defines the parameter types and names
 * which will be used by a subsequent <code>execute</code> method.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeclareParameters extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6-10 (DeclareParameters) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestDeclareParameters01() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("Integer param");
      query.setFilter("x == param");
      Object results = query.execute(Integer.valueOf(2));

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(2));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == param", results, expected);
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestDeclareParameters02() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("Integer param1, Integer param2");
      query.setFilter("x == param1 && y == param2");
      Object results = query.execute(Integer.valueOf(2), Integer.valueOf(2));

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(2));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(
          ASSERTION_FAILED, "x == param1 && y == param2", results, expected);
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestDeclareParameters03() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("int a, int b");
      query.setFilter("x == a && y == b");
      Object results = query.execute(Integer.valueOf(1), Integer.valueOf(1));

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(1));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == a && y == b", results, expected);
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

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PCPoint.class);
    loadAndPersistPCPoints(getPM());
  }
}
