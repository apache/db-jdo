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
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> New Query with Candidate Class, Collection, and Filter <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.5-9. <br>
 * <B>Assertion Description: </B> <code>
 * PersistenceManager.newQuery(Class cls, Collection cln, String filter)</code> constructs a query
 * instance with the candidate class, the candidate <code>Collection</code>, and filter specified.
 */
public class NewQueryWithCandidateClassCollectionFilter extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.5-9 (NewQueryWithCandidateClassCollectionFilter) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(NewQueryWithCandidateClassCollectionFilter.class);
  }

  /** */
  public void testPositive() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query query = pm.newQuery(PCPoint.class, inserted, "x ==2");
      Object results = query.execute();

      // check query result
      List expected = new ArrayList();
      PCPoint pc1 = new PCPoint(2, 2);
      expected.add(pc1);
      expected = getFromInserted(expected);
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "x ==2", results, expected);

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
