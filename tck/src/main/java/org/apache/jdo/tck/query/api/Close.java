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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Close <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.7-1. <br>
 * <B>Assertion Description: </B> <code>Query.close(Object)</code> closes the result of one <code>
 * execute(...)</code> method, and releases resources associated with it. After this method
 * completes, the query result can no longer be used, for example to iterate the returned elements.
 * Any elements returned previously by iteration of the results remain in their current state. Any
 * iterators acquired from the queryResult will return <code>false</code> to <code>hasNext()</code>
 * and will throw <code>NoSuchElementException</code> to <code>next(</code>).
 */
public class Close extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.7-1 (Close) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(Close.class);
  }

  /** */
  public void testPositive() {
    PersistenceManager pm = getPM();

    if (debug) logger.debug("\nExecuting test Close()...");
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      List<PCPoint> results = query.executeList();

      // check query result
      List<PCPoint> expected = new ArrayList<>();

      PCPoint p1 = new PCPoint(0, 0);
      PCPoint p2 = new PCPoint(1, 1);
      PCPoint p3 = new PCPoint(2, 2);
      PCPoint p4 = new PCPoint(3, 3);
      PCPoint p5 = new PCPoint(4, 4);

      expected.add(p1);
      expected.add(p2);
      expected.add(p3);
      expected.add(p4);
      expected.add(p5);

      expected = getFromInserted(expected);

      // printOutput(results);

      checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
      if (debug)
        logger.debug("Test Close: Results are as expected and accessible before query is closed");

      Iterator<PCPoint> resIterator = results.iterator();
      query.close(results);

      if (resIterator.hasNext()) {
        fail(
            ASSERTION_FAILED,
            "Iterator.hasNext() should return false after closing the query result.");
      }
      try {
        resIterator.next();
        fail(
            ASSERTION_FAILED,
            "Iterator.hasNext() should throw NoSuchElementException after closing the query result.");
      } catch (NoSuchElementException ex) {
        // expected exception
      }

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
