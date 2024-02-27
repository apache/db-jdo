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

package org.apache.jdo.tck.query.jdoql.parameters;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Each parameter named in the parameter declaration must be bound to a value when
 * executing the query. <br>
 * <B>Keywords:</B> query parameter check <br>
 * <B>Assertion ID:</B> A14.3-3. <br>
 * <B>Assertion Description: </B> Each parameter named in the parameter declaration must be bound to
 * a value when executing the query.
 */
public class BoundParameterCheck extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.3-3 (BoundParameterCheck) failed: ";

  /** */
  @Test
  public void test() {
    pm = getPM();
    checkQueryParameters(pm);
    pm.close();
    pm = null;
  }

  /** */
  void checkQueryParameters(PersistenceManager pm) {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<PCPoint> query = pm.newQuery(PCPoint.class);
      query.setCandidates(pm.getExtent(PCPoint.class, false));
      query.declareParameters("int a");
      query.setFilter("x == 0");
      try {
        Object results = query.execute();
        fail(
            ASSERTION_FAILED,
            "Query.execute with unbound parameter should throw JDOUserExecption.");
      } catch (JDOUserException ex) {
        // expected exception
        if (debug) {
          logger.debug("expected exception: " + ex);
        }
      }
      tx.rollback();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
