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

package org.apache.jdo.tck.query.result;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.company.QDentalInsurance;
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
 * <B>Title:</B> Default Unique. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.11-2. <br>
 * <B>Assertion Description: </B> The default Unique setting is true for aggregate results without a
 * grouping expression, and false otherwise.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DefaultUnique extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.11-2 (DefaultUnique) failed: ";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testThis() {
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(
            Employee.class, "emp1", "emp2", "emp3", "emp4", "emp5");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
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
  public void testAggregateNoGrouping0() {
    Object expected = Long.valueOf(5);
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "COUNT(department)",
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ null,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testAggregateNoGrouping1() {
    Object expected = Double.valueOf("99.997");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      QueryElementHolder<DentalInsurance> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "avg(lifetimeOrthoBenefit)",
              /*INTO*/ null,
              /*FROM*/ DentalInsurance.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ null,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testAggregateNoGrouping2() {
    Object expected = new BigDecimal("2000.99");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      QueryElementHolder<Project> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "MIN(budget)",
              /*INTO*/ null,
              /*FROM*/ Project.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ null,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testAggregateNoGrouping3() {
    Object expected = new BigDecimal("2500000.99");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      QueryElementHolder<Project> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "MAX(budget)",
              /*INTO*/ null,
              /*FROM*/ Project.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ null,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testAggregateNoGrouping4() {
    Object expected = new BigDecimal("2552001.98");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      QueryElementHolder<Project> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "SUM (budget)",
              /*INTO*/ null,
              /*FROM*/ Project.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ null,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testAggregateGrouping0() {
    Object expected = Arrays.asList(Long.valueOf(3), Long.valueOf(2));
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Employee> query = pm.newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate("this");
      query.result(false, cand.department.count());
      query.groupBy(cand.department);

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "count(department)",
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ "department",
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testAggregateGrouping1() {
    Object expected = Arrays.asList(Double.valueOf("99.996"), Double.valueOf("99.9985"));
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<DentalInsurance> query = pm.newJDOQLTypedQuery(DentalInsurance.class);
      QDentalInsurance cand = QDentalInsurance.candidate("this");
      query.filter(cand.employee.ne((Employee) null));
      query.result(false, cand.lifetimeOrthoBenefit.avg());
      query.groupBy(cand.employee.department);

      QueryElementHolder<DentalInsurance> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "AVG(lifetimeOrthoBenefit)",
              /*INTO*/ null,
              /*FROM*/ DentalInsurance.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "employee != null",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ "employee.department",
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testAggregateGrouping2() {
    Object expected = Arrays.asList(new BigDecimal("99.995"), new BigDecimal("99.998"));
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<DentalInsurance> query = pm.newJDOQLTypedQuery(DentalInsurance.class);
      QDentalInsurance cand = QDentalInsurance.candidate("this");
      query.filter(cand.employee.ne((Employee) null));
      query.result(false, cand.lifetimeOrthoBenefit.min());
      query.groupBy(cand.employee.department);

      QueryElementHolder<DentalInsurance> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "min(lifetimeOrthoBenefit)",
              /*INTO*/ null,
              /*FROM*/ DentalInsurance.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "employee != null",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ "employee.department",
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testAggregateGrouping3() {
    Object expected = Arrays.asList(new BigDecimal("99.997"), new BigDecimal("99.999"));
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<DentalInsurance> query = pm.newJDOQLTypedQuery(DentalInsurance.class);
      QDentalInsurance cand = QDentalInsurance.candidate("this");
      query.filter(cand.employee.ne((Employee) null));
      query.result(false, cand.lifetimeOrthoBenefit.max());
      query.groupBy(cand.employee.department);

      QueryElementHolder<DentalInsurance> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "MAX(lifetimeOrthoBenefit)",
              /*INTO*/ null,
              /*FROM*/ DentalInsurance.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "employee != null",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ "employee.department",
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, null, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testAggregateGrouping4() {
    Object expected = Arrays.asList(new BigDecimal("299.988"), new BigDecimal("199.997"));
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<DentalInsurance> query = pm.newJDOQLTypedQuery(DentalInsurance.class);
      QDentalInsurance cand = QDentalInsurance.candidate("this");
      query.filter(cand.employee.ne((Employee) null));
      query.result(false, cand.lifetimeOrthoBenefit.sum());
      query.groupBy(cand.employee.department);

      QueryElementHolder<DentalInsurance> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "sum(lifetimeOrthoBenefit)",
              /*INTO*/ null,
              /*FROM*/ DentalInsurance.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ "employee != null",
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ "employee.department",
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, null, true, expected);
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
