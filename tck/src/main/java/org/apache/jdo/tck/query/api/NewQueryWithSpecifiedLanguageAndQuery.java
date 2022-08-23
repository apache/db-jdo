/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> New Query With Specified Language and Query <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.5-4. <br>
 * <B>Assertion Description: </B> <code>PersistenceManager.newQuery(String language, Object query)
 * </code> constructs a query instance using the specified language and the specified query . The
 * query instance returned will be of a class defined by the query language. The language parameter
 * for the JDO Query language as herein documented is &quot;javax.jdo.query.JDOQL&quot;.
 */
public class NewQueryWithSpecifiedLanguageAndQuery extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.5-4 (NewQueryWithSpecifiedLanguageAndQuery) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(NewQueryWithSpecifiedLanguageAndQuery.class);
  }

  /** */
  public void testPositive() {
    PersistenceManager pm = getPM();
    if (debug) logger.debug("\nExecuting test NewQueryWithSpecifiedLanguageAndQuery()...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query query = pm.newQuery();
      query.setClass(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));

      Query query1 = pm.newQuery("javax.jdo.query.JDOQL", query);
      query1.setCandidates(pm.getExtent(PCPoint.class, false));
      query1.compile();
      Object results = query1.execute();

      // check query result
      List expected = new ArrayList();
      Object p1 = new PCPoint(0, 0);
      Object p2 = new PCPoint(1, 1);
      Object p3 = new PCPoint(2, 2);
      Object p4 = new PCPoint(3, 3);
      Object p5 = new PCPoint(4, 4);
      expected.add(p1);
      expected.add(p2);
      expected.add(p3);
      expected.add(p4);
      expected.add(p5);
      expected = getFromInserted(expected);
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
      if (debug) logger.debug("Test NewQueryWithSpecifiedLanguageAndQuery() - Passed\n");

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
