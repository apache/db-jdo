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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.query.NumericExpression;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.QFullTimeEmployee;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> SampleModifyQueries <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion IDs:</B> <br>
 * <B>Assertion Description: </B> This test class runs the example modify queries from the JDO
 * specification.
 *
 * <p>There are up to four test methods per test case:
 *
 * <ul>
 *   <li>testQueryxxa: runtime constructed JDO query using deletePersistentAll taking parameters to
 *       run the query
 *   <li>testQueryxxb: runtime constructed JDO query using setNamedParameters to specify the
 *       parameter values and no-arg deletePersistentAll to run the query
 *   <li>testQueryxxc: runtime constructed JDO query using setParameters to specify the parameter
 *       values and no-arg deletePersistentAll to run the query
 *   <li>testQueryxxf: JDOQLTypedQuery version
 */
public class SampleModifyQueries extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion (SampleModifyQueries) failed: ";

  /** */
  private static final String SAMPLE_QUERIES_TEST_COMPANY_TESTDATA =
      "org/apache/jdo/tck/pc/company/companyForSampleQueriesTest.xml";

  /**
   * Deleting Multiple Instances.
   *
   * <p>This query deletes all Employees who make more than the parameter salary.
   */
  @Test
  public void testQuery21a() {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal");
      q.declareParameters("Double sal");
      q.deletePersistentAll(30000.);
      tx.commit();

      tx.begin();
      Query<FullTimeEmployee> allQuery = pm.newQuery(FullTimeEmployee.class);
      List<FullTimeEmployee> allFTE = allQuery.executeList();
      if (!allFTE.isEmpty()) {
        fail(
            ASSERTION_FAILED,
            "All FullTimeEmployee instances should have been deleted,"
                + " there are still "
                + allFTE.size()
                + " instances left.");
      }
      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }
  }

  /**
   * Deleting Multiple Instances.
   *
   * <p>This query deletes all Employees who make more than the parameter salary.
   */
  @Test
  public void testQuery21b() {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal");
      q.declareParameters("Double sal");
      Map<String, Object> paramValues = new HashMap<>();
      paramValues.put("sal", 30000.);
      q.setNamedParameters(paramValues);
      q.deletePersistentAll();
      tx.commit();

      tx.begin();
      Query<FullTimeEmployee> allQuery = pm.newQuery(FullTimeEmployee.class);
      List<FullTimeEmployee> allFTE = allQuery.executeList();
      if (!allFTE.isEmpty()) {
        fail(
            ASSERTION_FAILED,
            "All FullTimeEmployee instances should have been deleted,"
                + " there are still "
                + allFTE.size()
                + " instances left.");
      }
      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }
  }

  /**
   * Deleting Multiple Instances.
   *
   * <p>This query deletes all Employees who make more than the parameter salary.
   */
  @Test
  public void testQuery21c() {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class, "salary > sal");
      q.declareParameters("Double sal");
      q.setParameters(30000.);
      q.deletePersistentAll();
      tx.commit();

      tx.begin();
      Query<FullTimeEmployee> allQuery = pm.newQuery(FullTimeEmployee.class);
      List<FullTimeEmployee> allFTE = allQuery.executeList();
      if (!allFTE.isEmpty()) {
        fail(
            ASSERTION_FAILED,
            "All FullTimeEmployee instances should have been deleted,"
                + " there are still "
                + allFTE.size()
                + " instances left.");
      }
      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
    }
  }

  /**
   * Deleting Multiple Instances.
   *
   * <p>This query deletes all Employees who make more than the parameter salary.
   */
  @Test
  public void testQuery21f() {
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        NumericExpression<Double> sal = q.numericParameter("sal", Double.class);
        q.filter(cand.salary.gt(sal));
        q.setParameter("sal", 30000.);
        q.deletePersistentAll();
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();

      tx.begin();
      Query<FullTimeEmployee> allQuery = pm.newQuery(FullTimeEmployee.class);
      List<FullTimeEmployee> allFTE = allQuery.executeList();
      if (!allFTE.isEmpty()) {
        fail(
            ASSERTION_FAILED,
            "All FullTimeEmployee instances should have been deleted,"
                + " there are still "
                + allFTE.size()
                + " instances left.");
      }
      tx.commit();
    } finally {
      if (tx.isActive()) {
        tx.rollback();
      }
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

  /**
   * Returns the name of the company test data resource.
   *
   * @return name of the company test data resource.
   */
  @Override
  protected String getCompanyTestDataResource() {
    return SAMPLE_QUERIES_TEST_COMPANY_TESTDATA;
  }
}
