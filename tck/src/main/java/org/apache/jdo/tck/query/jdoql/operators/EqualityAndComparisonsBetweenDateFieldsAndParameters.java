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

package org.apache.jdo.tck.query.jdoql.operators;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.query.DateTimeExpression;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Equality and Comparisons Between Date Fields and Parameters <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-4. <br>
 * <B>Assertion Description: </B> Equality and ordering comparisons of <code>Date</code> fields and
 * <code>Date</code> parameters are valid in a <code>Query</code> filter.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EqualityAndComparisonsBetweenDateFieldsAndParameters extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-4 (EqualityAndComparisonsBetweenDateFieldsAndParameters) failed: ";

  /** */
  private static final Date FIRST_OF_JAN_1999;

  static {
    // initialize static field FIRST_OF_JAN_1999
    Calendar cal = new GregorianCalendar();
    cal.set(1999, GregorianCalendar.JANUARY, 1, 0, 0, 0);
    cal.set(GregorianCalendar.MILLISECOND, 0);
    FIRST_OF_JAN_1999 = cal.getTime();
  }

  /** Parameters of valid queries. */
  private final Object[][] parameters = {
    // date field == date parameter
    {FIRST_OF_JAN_1999},
    // date field >= date parameter
    {FIRST_OF_JAN_1999},
    // date field >= date parameter
    {FIRST_OF_JAN_1999}
  };

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testFieldEqualsParameter() {
    List<Employee> expected = getTransientCompanyModelInstancesAsList(Employee.class, "emp1");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      DateTimeExpression param = query.datetimeParameter("param");
      query.filter(cand.hiredate.eq(param));

      Map<String, Object> paramValues = new HashMap<>();
      paramValues.put("param", FIRST_OF_JAN_1999);

      // date field == date parameter
      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "hiredate == param",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ "java.util.Date param",
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ paramValues);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testFieldGEParameter() {
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3", "emp4");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      DateTimeExpression param = query.datetimeParameter("param");
      query.filter(cand.hiredate.gteq(param));

      Map<String, Object> paramValues = new HashMap<>();
      paramValues.put("param", FIRST_OF_JAN_1999);

      // date field >= date parameter
      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "hiredate >= param",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ "java.util.Date param",
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ paramValues);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testParameterLTField() {
    List<Employee> expected = getTransientCompanyModelInstancesAsList(Employee.class);
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {

      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      DateTimeExpression param = query.datetimeParameter("param");
      query.filter(param.lt(cand.birthdate));

      Map<String, Object> paramValues = new HashMap<>();
      paramValues.put("param", FIRST_OF_JAN_1999);

      // Import Department twice
      QueryElementHolder<Employee> holder = // date parameter < date field
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "param < birthdate",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ "java.util.Date param",
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ paramValues);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
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
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
  }
}
