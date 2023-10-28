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

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> New Query with Candidate Class and Extent <br>
 * <B>Keywords:</B> query extent <br>
 * <B>Assertion ID:</B> A14.5-6. <br>
 * <B>Assertion Description: </B> <code>PersistenceManager.newQuery(Class cls, Extent cln)</code>
 * constructs a query instance with the candidate class and candidate <code>Extent</code> specified.
 */
public class NewQueryWithCandidateClassAndExtent extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.5-6 (NewQueryWithCandidateClassAndExtent) failed: ";

  /** */
  @Test
  public void testPositive() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      Extent<PCPoint> extent = pm.getExtent(PCPoint.class, true);
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(extent);
      Object results = query.execute();

      // check query result
      printOutput(results, inserted);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, results, inserted);

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
