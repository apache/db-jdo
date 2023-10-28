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
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> New Query <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion IDs:</B> A14.5-10. <br>
 * <B>Assertion Description: </B> Construct a new query instance with the candidate Extent and
 * filter specified; the candidate class is taken from the Extent.
 */
public class NewQueryWithExtentAndFilter extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.5-1 (NewQueryWithExtentAndFilter) failed: ";

  /** */
  @Test
  public void testPositive() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    tx.begin();
    try {
      Query<PCPoint> query = pm.newQuery(pm.getExtent(PCPoint.class, false), "x == 1");
      Object results = query.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      PCPoint pcp1 = new PCPoint(1, 1);
      expected.add(pcp1);
      expected = getFromInserted(expected);
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == 1", results, expected);

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
