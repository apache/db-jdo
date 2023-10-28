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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> New Query From Existing Serialized Query <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.5-2. <br>
 * <B>Assertion Description: </B> <code>PersistenceManager.newQuery(Object query)</code> constructs
 * a <code>Query</code> instance from another query, where the parameter is a serialized/re-stored
 * <code>Query</code> instance from the same JDO vendor but a different execution environment. Any
 * of the elements Class, Filter, Import declarations, Variable declarations, Parameter
 * declarations, and Ordering from the parameter <code>Query</code> are copied to the new <code>
 * Query</code> instance, but a candidate <code>Collection</code> or <code>Extent</code> element is
 * discarded.
 */
public class NewQueryFromRestoredSerializedQuery extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.5-2 (NewQueryFromRestoredSerializedQuery) failed: ";

  /**
   * @throws Exception exception
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testPositive() throws IOException, ClassNotFoundException {
    PersistenceManager pm = getPM();
    if (debug) logger.debug("\nExecuting test NewQueryFromRestoredSerializedQuery01() ...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.setFilter("x == 3");
      query.compile();

      ObjectOutputStream oos = null;
      try {
        if (debug) logger.debug("Attempting to serialize Query object.");
        oos = new ObjectOutputStream(new FileOutputStream(SERIALZED_QUERY));
        oos.writeObject(query);
        if (debug) logger.debug("Query object serialized.");
      } finally {
        if (oos != null) {
          try {
            oos.flush();
          } catch (Exception ignored) {
          }
          try {
            oos.close();
          } catch (Exception ignored) {
          }
        }
      }

      ObjectInputStream in = new ObjectInputStream(new FileInputStream(SERIALZED_QUERY));
      Query<PCPoint> query1 = (Query<PCPoint>) in.readObject();

      // init and execute query
      query = pm.newQuery(query1);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      Object results = query.execute();

      // check query result
      List<PCPoint> expected = new ArrayList<>();
      PCPoint p4 = new PCPoint(3, 3);
      expected.add(p4);
      expected = getFromInserted(expected);
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == 3", results, expected);
      if (debug) logger.debug("Test NewQueryFromRestoredSerializedQuery01(): Passed");

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
