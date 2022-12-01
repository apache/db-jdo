/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.query.jdoql;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Query Result Passed to Another Query <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.1-4. <br>
 * <B>Assertion Description: </B> The collection returned by <code>Query.execute</code> can be
 * passed to another <code>Query</code>.
 */
public class QueryResultPassedToAnotherQuery extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.1-4 (QueryResultPassedToAnotherQuery) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(QueryResultPassedToAnotherQuery.class);
  }

  List<PCPoint> resultCln;

  /** */
  public void testPositive() {
    PersistenceManager pm = getPM();

    setResultCollection(pm);
    runTestQueryResultPassedToAnotherQuery01(pm);
    runTestQueryResultPassedToAnotherQuery02(pm);
    runTestQueryResultPassedToAnotherQuery03(pm);

    pm.close();
    pm = null;
  }

  /** */
  void setResultCollection(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      resultCln = query.executeList();
      // Create a new collection for the result collection.
      // This ensures that the result collection may be iterated
      // outside of the scope of the current transaction.
      resultCln = new ArrayList<>(resultCln);

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  void runTestQueryResultPassedToAnotherQuery01(PersistenceManager pm) {
    if (debug) logger.debug("\nExecuting test QueryResultPassedToAnotherQuery01()...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(resultCln);
      query.setFilter("x == 1");
      Object results = query.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      PCPoint p2 = new PCPoint(1, 1);
      expected.add(p2);
      expected = getFromInserted(expected);
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == 1", results, expected);
      if (debug) logger.debug("Test QueryResultPassedToAnotherQuery01: Passed");

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  void runTestQueryResultPassedToAnotherQuery02(PersistenceManager pm) {
    if (debug) logger.debug("\nExecuting test QueryResultPassedToAnotherQuery02()...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class, resultCln);
      query.setFilter("x == 1");
      List<PCPoint> results = query.executeList();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      PCPoint p2 = new PCPoint(1, 1);
      expected.add(p2);
      expected = getFromInserted(expected);
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == 1", results, expected);
      if (debug) logger.debug("Test QueryResultPassedToAnotherQuery02: Passed");

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  void runTestQueryResultPassedToAnotherQuery03(PersistenceManager pm) {
    if (debug) logger.debug("\nExecuting test QueryResultPassedToAnotherQuery03()...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class, resultCln, "x == 1");
      List<PCPoint> results = query.executeList();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      PCPoint p2 = new PCPoint(1, 1);
      expected.add(p2);
      expected = getFromInserted(expected);
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == 1", results, expected);
      if (debug) logger.debug("Test QueryResultPassedToAnotherQuery03: Passed");

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
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
