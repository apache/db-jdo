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

package org.apache.jdo.tck.query.jdoql.parameters;

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
 * <B>Title:</B> Parameter Declared with Same Name as Field of Candidate Class <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.4-2. <br>
 * <B>Assertion Description: </B> A field of the candidate class of a <code>Query</code> can be
 * hidden if a parameter is declared with the same name.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParameterDeclaredWithSameNameAsFieldOfCandidateClass extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.4-2 (ParameterDeclaredWithSameNameAsFieldOfCandidateClass) failed: ";

  /** */
  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestParameterDeclaredWithSameNameAsFieldOfCandidateClass01() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("Integer x");
      query.setFilter("x == x");
      Object results = query.execute(Integer.valueOf(2));

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(0));
      expected.add(getTransientPCPoint(1));
      expected.add(getTransientPCPoint(2));
      expected.add(getTransientPCPoint(3));
      expected.add(getTransientPCPoint(4));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == x", results, expected);
      if (debug)
        logger.debug("\nTest ParameterDeclaredWithSameNameAsFieldOfCandidateClass - Passed");

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestParameterDeclaredWithSameNameAsFieldOfCandidateClass02() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("Integer y");
      query.setFilter("y == y");
      Object results = query.execute(Integer.valueOf(2));

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(0));
      expected.add(getTransientPCPoint(1));
      expected.add(getTransientPCPoint(2));
      expected.add(getTransientPCPoint(3));
      expected.add(getTransientPCPoint(4));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "y == y", results, expected);
      if (debug)
        logger.debug("\nTest ParameterDeclaredWithSameNameAsFieldOfCandidateClass - Passed");

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
