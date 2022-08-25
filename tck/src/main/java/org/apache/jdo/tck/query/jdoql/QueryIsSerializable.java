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

package org.apache.jdo.tck.query.jdoql;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Query is Serializable <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.3-7 A14.6-1. <br>
 * <B>Assertion Description: </B> The class implementing the <code>Query</code> interface must be
 * serializable. The serialized fields include the candidate class, the filter, parameter
 * declarations, variable declarations, imports, and ordering specification.
 */
public class QueryIsSerializable extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.3-7 A14.6-1 (QueryIsSerializable) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(QueryIsSerializable.class);
  }

  /**
   * @throws Exception exception
   */
  public void testPositive() throws Exception {
    PersistenceManager pm = getPM();

    runTestQueryIsSerializable01(pm);
    runTestQueryIsSerializable02(pm);
    runTestQueryIsSerializable03(pm);
  }

  /** */
  void runTestQueryIsSerializable01(PersistenceManager pm) {
    if (debug) logger.debug("\nExecuting test QueryIsSerializable01() ...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query query = pm.newQuery();
      if (query instanceof Serializable) {
        if (debug) {
          logger.debug("Query extends serializable interface.");
        }
      } else {
        fail(ASSERTION_FAILED, "Query does not extends serializable interface.");
      }

      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  void runTestQueryIsSerializable02(PersistenceManager pm) throws Exception {
    if (debug) logger.debug("\nExecuting test QueryIsSerializable02() ...");

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query query = pm.newQuery();
      query.setClass(PCPoint.class);
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
          } catch (Exception ex) {
          }
          try {
            oos.close();
          } catch (Exception ex) {
          }
        }
      }

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** */
  void runTestQueryIsSerializable03(PersistenceManager pm) throws Exception {
    if (debug) logger.debug("\nExecuting test QueryIsSerializable03() ...");
    Object restoredQuery = null;
    ObjectInputStream ois = null;

    try {
      if (debug) logger.debug("Attempting to de-serialize Query object.");
      ois = new ObjectInputStream(new FileInputStream("query.ser"));
      restoredQuery = ois.readObject();
      if (debug) logger.debug("Query object restored.");
    } finally {
      if (ois != null) {
        try {
          ois.close();
        } catch (Exception ex) {
        }
      }
    }

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      Query query = pm.newQuery(restoredQuery);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.compile();
      Object results = query.execute();

      // check query result
      List expected = new ArrayList();
      Object p4 = new PCPoint(3, 3);
      expected.add(p4);
      expected = getFromInserted(expected);
      printOutput(results, expected);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
      if (debug) logger.debug("Test QueryIsSerializable03(): Passed");

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
