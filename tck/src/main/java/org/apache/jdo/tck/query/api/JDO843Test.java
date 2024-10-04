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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.query.NumericExpression;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.*;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JDO843Test extends QueryTest {

  private static final String SINGLE_STRING_QUERY_15 =
          "select into org.apache.jdo.tck.query.api.SampleReadQueries$EmpWrapper "
                  + "from org.apache.jdo.tck.pc.company.FullTimeEmployee where salary > :sal";

  private static final String SINGLE_STRING_QUERY_16 =
          "select into org.apache.jdo.tck.query.api.SampleReadQueries$EmpInfo "
                  + "from org.apache.jdo.tck.pc.company.FullTimeEmployee where salary > :sal";

  /** */
  private static final String ASSERTION_FAILED = "Assertion (SampleQueries) failed: ";

  /** */
  private static final String SAMPLE_QUERIES_TEST_COMPANY_TESTDATA =
      "org/apache/jdo/tck/pc/company/companyForSampleQueriesTest.xml";

  /**
   * Projection of "this" to User-defined Result Class with Matching Field.
   *
   * <p>This query selects instances of Employee who make more than the parameter salary and stores
   * the result in a user-defined class. Since the default is "distinct this as FullTimeEmployee",
   * the field must be named FullTimeEmployee and be of type FullTimeEmployee.
   */
  @Test
  public void testQuery15fFailure() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<SampleReadQueries.EmpWrapper> expected = testQuery15Helper();
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        // Using the following candidate method returns a query result wrapping null as FullTimeEmployee instance
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        NumericExpression<Double> sal = q.numericParameter("sal", Double.class);
        q.result(true, cand.as("FullTimeEmployee")).filter(cand.salary.gt(sal));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("sal", 30000.);
        q.setParameters(paramValues);
        List<SampleReadQueries.EmpWrapper> infos = q.executeResultList(SampleReadQueries.EmpWrapper.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_15, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  @Test
  public void testQuery15fSuccess() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<SampleReadQueries.EmpWrapper> expected = testQuery15Helper();
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        // Using the following candidate method returns the expected query result
        QFullTimeEmployee cand = QFullTimeEmployee.candidate();
        NumericExpression<Double> sal = q.numericParameter("sal", Double.class);
        q.result(true, cand.as("FullTimeEmployee")).filter(cand.salary.gt(sal));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("sal", 30000.);
        q.setParameters(paramValues);
        List<SampleReadQueries.EmpWrapper> infos = q.executeResultList(SampleReadQueries.EmpWrapper.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_15, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /**
   * Projection of "this" to User-defined Result Class with Matching Method
   *
   * <p>This query selects instances of FullTimeEmployee who make more than the parameter salary and
   * stores the result in a user-defined class.
   */
  @Test
  public void testQuery16fFailure() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<SampleReadQueries.EmpInfo> expected = testQuery16Helper();
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        // Using the following candidate method returns a query result wrapping null as FullTimeEmployee instance
        QFullTimeEmployee cand = QFullTimeEmployee.candidate("this");
        NumericExpression<Double> sal = q.numericParameter("sal", Double.class);
        q.result(true, cand.as("FullTimeEmployee")).filter(cand.salary.gt(sal));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("sal", 30000.);
        q.setParameters(paramValues);
        List<SampleReadQueries.EmpInfo> infos = q.executeResultList(SampleReadQueries.EmpInfo.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_16, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  @Test
  public void testQuery16fSuccess() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<SampleReadQueries.EmpInfo> expected = testQuery16Helper();
      try (JDOQLTypedQuery<FullTimeEmployee> q = pm.newJDOQLTypedQuery(FullTimeEmployee.class)) {
        // Using the following candidate method returns the expected query result
        QFullTimeEmployee cand = QFullTimeEmployee.candidate();
        NumericExpression<Double> sal = q.numericParameter("sal", Double.class);
        q.result(true, cand.as("FullTimeEmployee")).filter(cand.salary.gt(sal));
        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("sal", 30000.);
        q.setParameters(paramValues);
        List<SampleReadQueries.EmpInfo> infos = q.executeResultList(SampleReadQueries.EmpInfo.class);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, SINGLE_STRING_QUERY_16, infos, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  private List<SampleReadQueries.EmpWrapper> testQuery15Helper() {
    SampleReadQueries.EmpWrapper wrapper1 = new SampleReadQueries.EmpWrapper();
    wrapper1.FullTimeEmployee = getTransientCompanyModelInstance(FullTimeEmployee.class, "emp1");
    SampleReadQueries.EmpWrapper wrapper2 = new SampleReadQueries.EmpWrapper();
    wrapper2.FullTimeEmployee = getTransientCompanyModelInstance(FullTimeEmployee.class, "emp2");
    SampleReadQueries.EmpWrapper wrapper3 = new SampleReadQueries.EmpWrapper();
    wrapper3.FullTimeEmployee = getTransientCompanyModelInstance(FullTimeEmployee.class, "emp5");
    return Arrays.asList(wrapper1, wrapper2, wrapper3);
  }

  private List<SampleReadQueries.EmpInfo> testQuery16Helper() {
    SampleReadQueries.EmpInfo info1 = new SampleReadQueries.EmpInfo();
    info1.setFullTimeEmployee(getTransientCompanyModelInstance(FullTimeEmployee.class, "emp1"));
    SampleReadQueries.EmpInfo info2 = new SampleReadQueries.EmpInfo();
    info2.setFullTimeEmployee(getTransientCompanyModelInstance(FullTimeEmployee.class, "emp2"));
    SampleReadQueries.EmpInfo info3 = new SampleReadQueries.EmpInfo();
    info3.setFullTimeEmployee(getTransientCompanyModelInstance(FullTimeEmployee.class, "emp5"));
    return Arrays.asList(info1, info2, info3);
  }

  @BeforeAll
  @Override
  protected void setUp() {
    super.setUp();
  }

  @AfterAll
  @Override
  protected void tearDown() {
    super.tearDown();
  }

  /**
   * @see JDO_Test#localSetUp()
   */
  protected void localSetUp() {
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
  }

  /**
   * Returns the name of the company test data resource.
   *
   * @return name of the company test data resource.
   */
  protected String getCompanyTestDataResource() {
    return SAMPLE_QUERIES_TEST_COMPANY_TESTDATA;
  }
}
