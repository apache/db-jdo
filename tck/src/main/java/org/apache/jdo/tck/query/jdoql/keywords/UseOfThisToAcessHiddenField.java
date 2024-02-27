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

package org.apache.jdo.tck.query.jdoql.keywords;

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
 * <B>Title:</B> Use of this to Access Hidden Field <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.4-4. <br>
 * <B>Assertion Description: </B> A hidden field may be accessed using the <code>'this'</code>
 * qualifier: <code>this.fieldName</code>.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UseOfThisToAcessHiddenField extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.4-4 (UseOfThisToAcessHiddenField) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestUseOfThisToAcessHiddenField01() {
    if (debug) logger.debug("\nExecuting test UseOfThisToAcessHiddenField01() ...");

    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("Integer x");
      query.setFilter("this.x == x");
      query.setParameters(Integer.valueOf(2));
      List<PCPoint> results = query.executeList();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(2));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "this.x == x", results, expected);
      if (debug) logger.debug("Test UseOfThisToAcessHiddenField01(): Passed");

      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void runTestUseOfThisToAcessHiddenField02() {
    if (debug) logger.debug("\nExecuting test UseOfThisToAcessHiddenField02() ...");

    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("Integer y");
      query.setFilter("this.y == y");
      query.setParameters(Integer.valueOf(3));
      List<PCPoint> results = query.executeList();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(getTransientPCPoint(3));
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "this.y == y", results, expected);
      if (debug) logger.debug("Test UseOfThisToAcessHiddenField02(): Passed");

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
