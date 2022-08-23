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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Get PersistenceManager From Restored Serialized Query <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6-3. <br>
 * <B>Assertion Description: </B> If a <code>Query</code> instance has been restored from a
 * serialized form, <code>Query.getPersistenceManager()</code> returns <code>null</code>.
 */
public class GetPersistenceManagerFromRestoredSerializedQuery extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6-3 (GetPersistenceManagerFromRestoredSerializedQuery) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetPersistenceManagerFromRestoredSerializedQuery.class);
  }

  /**
   * @throws Exception exception
   */
  public void test() throws Exception {
    pm = getPM();

    // initDatabase(pm, PCPoint.class);
    runTestGetPersistenceManagerFromRestoredSerializedQuery(pm);

    pm.close();
    pm = null;
  }

  /** */
  void serializeQuery(PersistenceManager pm) throws Exception {
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
          oos.close();
        } catch (Exception ex) {
        }
      }
    }
  }

  /** */
  void runTestGetPersistenceManagerFromRestoredSerializedQuery(PersistenceManager pm)
      throws Exception {
    if (debug)
      logger.debug("\nExecuting test GetPersistenceManagerFromRestoredSerializedQuery() ...");

    Query restoredQuery = null;
    ObjectInputStream ois = null;
    PersistenceManager pm1 = null;

    serializeQuery(pm);

    try {
      if (debug) logger.debug("Attempting to de-serialize Query object.");
      ois = new ObjectInputStream(new FileInputStream(SERIALZED_QUERY));
      restoredQuery = (Query) ois.readObject();
      if (restoredQuery == null) {
        fail(ASSERTION_FAILED, "Deserialzed query is null");
      }
      if (debug) logger.debug("Query object restored.");
    } finally {
      if (ois != null) {
        try {
          ois.close();
        } catch (Exception ex) {
        }
      }
    }

    pm1 = restoredQuery.getPersistenceManager();
    if (pm1 == null) {
      if (debug) logger.debug("Test GetPersistenceManagerFromRestoredSerializedQuery(): Passed");
    } else {
      fail(ASSERTION_FAILED, "Deserialzed query instance should not have a pm associated");
    }
  }
}
