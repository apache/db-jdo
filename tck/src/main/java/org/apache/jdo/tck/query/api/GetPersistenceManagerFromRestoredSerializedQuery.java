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
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

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
   * @throws Exception exception
   */
  @Test
  public void test() throws Exception {
    pm = getPM();

    // initDatabase(pm, PCPoint.class);
    runTestGetPersistenceManagerFromRestoredSerializedQuery(pm);

    pm.close();
    pm = null;
  }

  /** */
  void serializeQuery(PersistenceManager pm) throws IOException {
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
          oos.close();
        } catch (Exception ignored) {
        }
      }
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  void runTestGetPersistenceManagerFromRestoredSerializedQuery(PersistenceManager pm)
      throws IOException, ClassNotFoundException {
    if (debug)
      logger.debug("\nExecuting test GetPersistenceManagerFromRestoredSerializedQuery() ...");

    Query<PCPoint> restoredQuery = null;
    ObjectInputStream ois = null;
    PersistenceManager pm1 = null;

    serializeQuery(pm);

    try {
      if (debug) logger.debug("Attempting to de-serialize Query object.");
      ois = new ObjectInputStream(new FileInputStream(SERIALZED_QUERY));
      restoredQuery = (Query<PCPoint>) ois.readObject();
      if (restoredQuery == null) {
        fail(ASSERTION_FAILED, "Deserialzed query is null");
      }
      if (debug) logger.debug("Query object restored.");
    } finally {
      if (ois != null) {
        try {
          ois.close();
        } catch (Exception ignored) {
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
