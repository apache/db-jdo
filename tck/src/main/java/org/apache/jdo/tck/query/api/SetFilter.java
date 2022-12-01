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

package org.apache.jdo.tck.query.api;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Set Filter <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6-7. <br>
 * <B>Assertion Description: </B> <code>Query.setFilter(String filter)</code> binds the query filter
 * to the query instance.
 */
public class SetFilter extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6-7 (SetFilter) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SetFilter.class);
  }

  /** */
  public void testPositive() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    Class<PCPoint> clazz = PCPoint.class;
    try {
      Extent<PCPoint> extent = pm.getExtent(clazz, true);
      tx.begin();
      Query<PCPoint> query = pm.newQuery(clazz);
      query.setClass(clazz);
      query.setCandidates(extent);
      query.setFilter("x == 2");
      Object results = query.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      expected.add(new PCPoint(2, 2));
      expected = getFromInserted(expected);
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == 2", results, expected);

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
