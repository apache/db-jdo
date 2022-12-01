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

package org.apache.jdo.tck.query.jdoql.methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jdo.JDOHelper;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.query.Expression;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.pc.mylib.QVersionedPCPoint;
import org.apache.jdo.tck.pc.mylib.VersionedPCPoint;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Supported JDOHelper methods. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-49. <br>
 * <B>Assertion Description: </B> Supported JDOHelper methods:
 *
 * <ul>
 *   <li>JDOHelper.getObjectId(Object)
 *   <li>JDOHelper.getVersion(Object)
 * </ul>
 */
public class SupportedJDOHelperMethods extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-49 (SupportedJDOHelperMethods) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SupportedJDOHelperMethods.class);
  }

  /** */
  public void testGetObjectById1() {
    Class<?> oidClass = getPM().getObjectIdClass(Person.class);
    List<Person> expectedResult = getExpectedResult(true, Person.class);

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.result(false, cand.jdoObjectId());

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ "JDOHelper.getObjectId(this)",
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ null,
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
  }

  /** */
  public void testGetObjectById2() {
    List<Person> expectedResult = getExpectedResult(false, Person.class, "personid == 1");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    Expression<Object> oid = query.parameter("oid", Object.class);
    query.filter(cand.jdoObjectId().eq(oid));

    // The query above returns a collection of size 1. The collection element is a
    // pc instance whose oid is the parameter of the query below.
    Map<String, Object> paramValues = new HashMap<>();
    paramValues.put("oid", JDOHelper.getObjectId(expectedResult.get(0)));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "JDOHelper.getObjectId(this) == oid",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ "Object oid",
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ paramValues);

    executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expectedResult);
  }

  /** Test for JDOHelper.getVersion() in queries. */
  public void testGetVersion() {
    // create some sample data
    pm.currentTransaction().begin();
    VersionedPCPoint pt1 = new VersionedPCPoint(1, 2);
    pm.makePersistent(pt1);
    pm.currentTransaction().commit();
    Object id = pm.getObjectId(pt1);

    try {
      // query 1
      List<Long> expectedResult1 = Arrays.asList(Long.valueOf(1));

      JDOQLTypedQuery<VersionedPCPoint> query = getPM().newJDOQLTypedQuery(VersionedPCPoint.class);
      QVersionedPCPoint cand = QVersionedPCPoint.candidate();
      query.result(false, cand.jdoVersion());

      QueryElementHolder<VersionedPCPoint> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "JDOHelper.getVersion(this)",
              /*INTO*/ null,
              /*FROM*/ VersionedPCPoint.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, holder, expectedResult1);
      executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult1);
      // executeJDOQLTypedQuery(ASSERTION_FAILED, holder, Long.class, expectedResult1);

      // query 2
      List<VersionedPCPoint> expectedResult2 =
          getExpectedResult(false, VersionedPCPoint.class, "x == 1");

      query = getPM().newJDOQLTypedQuery(VersionedPCPoint.class);
      cand = QVersionedPCPoint.candidate();
      Expression<Object> ver = query.parameter("ver", Object.class);
      query.filter(cand.jdoVersion().eq(ver));

      Map<String, Object> paramValues = new HashMap<>();
      paramValues.put("ver", Long.valueOf(1));

      holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ VersionedPCPoint.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "JDOHelper.getVersion(this) == ver",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ "Long ver",
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ paramValues);

      executeAPIQuery(ASSERTION_FAILED, holder, expectedResult2);
      executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult2);
      executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expectedResult2);

    } finally {
      pm.currentTransaction().begin();
      pm.deletePersistent(pm.getObjectById(id));
      pm.currentTransaction().commit();
    }
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
  }

  /** */
  private <T> List<T> getExpectedResult(boolean oidsWanted, Class<T> candidateClass) {
    return getExpectedResult(oidsWanted, candidateClass, null);
  }

  /** */
  @SuppressWarnings("unchecked")
  private <T> List<T> getExpectedResult(
      boolean oidsWanted, Class<T> candidateClass, String filter) {
    List<T> expectedResult;
    PersistenceManager pm = getPM();
    Transaction transaction = pm.currentTransaction();
    transaction.begin();
    try {
      Query<T> query =
          filter == null ? pm.newQuery(candidateClass) : pm.newQuery(candidateClass, filter);
      try {
        List<T> result = query.executeList();
        if (oidsWanted) {
          expectedResult = new ArrayList<>();
          for (T t : result) {
            expectedResult.add((T) JDOHelper.getObjectId(t));
          }
        } else {
          expectedResult = new ArrayList<>(result);
        }
      } finally {
        query.closeAll();
      }
    } finally {
      if (transaction.isActive()) {
        transaction.rollback();
      }
    }
    return expectedResult;
  }
}
