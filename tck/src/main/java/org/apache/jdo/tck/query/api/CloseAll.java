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
 * <B>Title:</B> Close All <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.7-2. <br>
 * <B>Assertion Description: </B> <code>Query.closeAll()</code> closes all results of <code>
 * execute(...)</code> methods on this <code>Query</code> instance, as in A14.6.7-1. The <code>Query
 * </code> instance is still valid and can still be used.
 */
public class CloseAll extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.7-2 (CloseAll) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(CloseAll.class);
  }

  /** */
  public void testPositive() {
    PersistenceManager pm = getPM();

    if (debug) logger.debug("\nExecuting test CloseAll()...");
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));

      List<PCPoint> results = query.executeList();
      Iterator<PCPoint> resIterator = results.iterator();

      query.closeAll();

      if (resIterator.hasNext()) {
        fail(
            ASSERTION_FAILED,
            "Iterator.hasNext() should return false after closing all query results.");
      }

      try {
        resIterator.next();
        fail(
            ASSERTION_FAILED,
            "Iterator.hasNext() should throw NoSuchElementException after closing all query results.");
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
