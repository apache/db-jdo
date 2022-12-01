/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.query.result;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.company.QDentalInsurance;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Default Unique. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.11-2. <br>
 * <B>Assertion Description: </B> The default Unique setting is true for aggregate results without a
 * grouping expression, and false otherwise.
 */
public class DefaultUnique extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.11-2 (DefaultUnique) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(DefaultUnique.class);
  }

  /** */
  public void testThis() {
    List<Employee> expected =
        getTransientCompanyModelInstancesAsList(
            Employee.class, "emp1", "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);

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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testAggregateNoGrouping0() {
    Object expected = Long.valueOf(5);

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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testAggregateNoGrouping1() {
    Object expected = Double.valueOf("99.997");

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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testAggregateNoGrouping2() {
    Object expected = new BigDecimal("2000.99");

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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testAggregateNoGrouping3() {
    Object expected = new BigDecimal("2500000.99");

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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testAggregateNoGrouping4() {
    Object expected = new BigDecimal("2552001.98");

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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testAggregateGrouping0() {
    Object expected = Arrays.asList(Long.valueOf(3), Long.valueOf(2));

    JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
    QEmployee cand = QEmployee.candidate();
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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testAggregateGrouping1() {
    Object expected = Arrays.asList(Double.valueOf("99.996"), Double.valueOf("99.9985"));

    JDOQLTypedQuery<DentalInsurance> query = getPM().newJDOQLTypedQuery(DentalInsurance.class);
    QDentalInsurance cand = QDentalInsurance.candidate();
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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testAggregateGrouping2() {
    Object expected = Arrays.asList(new BigDecimal("99.995"), new BigDecimal("99.998"));

    JDOQLTypedQuery<DentalInsurance> query = getPM().newJDOQLTypedQuery(DentalInsurance.class);
    QDentalInsurance cand = QDentalInsurance.candidate();
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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testAggregateGrouping3() {
    Object expected = Arrays.asList(new BigDecimal("99.997"), new BigDecimal("99.999"));

    JDOQLTypedQuery<DentalInsurance> query = getPM().newJDOQLTypedQuery(DentalInsurance.class);
    QDentalInsurance cand = QDentalInsurance.candidate();
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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testAggregateGrouping4() {
    Object expected = Arrays.asList(new BigDecimal("299.988"), new BigDecimal("199.997"));

    JDOQLTypedQuery<DentalInsurance> query = getPM().newJDOQLTypedQuery(DentalInsurance.class);
    QDentalInsurance cand = QDentalInsurance.candidate();
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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
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
