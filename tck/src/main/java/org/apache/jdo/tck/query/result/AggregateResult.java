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
import javax.jdo.JDOQLTypedQuery;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.company.QDentalInsurance;
import org.apache.jdo.tck.pc.company.QFullTimeEmployee;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.pc.company.QProject;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Aggregate Result. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.9-6. <br>
 * <B>Assertion Description: </B> Count returns Long. Sum returns Long for integral types and the
 * field's type for other Number types (BigDecimal, BigInteger, Float, and Double). Sum is invalid
 * if applied to non-Number types. Avg, min, and max return the type of the expression.
 */
public class AggregateResult extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.9-6 (AggregateResult) failed: ";

  /**
   * The array of invalid queries which may be executed as single string queries and as API queries.
   */
  private static final QueryElementHolder<?>[] INVALID_QUERIES = {
    // SUM
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ "SUM(firstname)",
        /*INTO*/ null,
        /*FROM*/ FullTimeEmployee.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null)
  };

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(AggregateResult.class);
  }

  /** */
  public void testCount0() {
    // COUNT(this)
    Object expected = Long.valueOf(3);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    query.result(false, cand.count());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "COUNT(this)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testCount1() {
    // COUNT(this)
    Object expected = Long.valueOf(0);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    query.result(false, cand.count());
    query.filter(cand.personid.eq(0L));

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "COUNT(this)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "personid == 0",
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testCount2() {
    // COUNT(manager)
    Object expected = Long.valueOf(2);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    query.result(false, cand.manager.count());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "COUNT(manager)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testCount3() {
    // COUNT(manager.personid)
    Object expected = Long.valueOf(2);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    query.result(false, cand.manager.personid.count());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "COUNT(manager.personid)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testCount4() {
    // COUNT(DISTINCT manager)
    Object expected = Long.valueOf(1);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    query.result(false, cand.manager.countDistinct());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "COUNT(DISTINCT manager)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testSum0() {
    // SUM(long)
    Object expected = Long.valueOf(1L + 2 + 5);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    query.result(false, cand.personid.sum());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "SUM(personid)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testSum1() {
    // SUM(double)
    Object expected = Double.valueOf(20000.0 + 10000.0 + 45000.0);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    query.result(false, cand.salary.sum());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "SUM(salary)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testSum2() {
    // SUM(BigDecimal)
    Object expected =
        new BigDecimal("2500000.99").add(new BigDecimal("50000.00")).add(new BigDecimal("2000.99"));

    JDOQLTypedQuery<Project> query = getPM().newJDOQLTypedQuery(Project.class);
    QProject cand = QProject.candidate();
    query.result(false, cand.budget.sum());

    QueryElementHolder<Project> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "SUM(budget)",
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
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testSum3() {
    // SUM(budget)
    Object expected = null;

    JDOQLTypedQuery<Project> query = getPM().newJDOQLTypedQuery(Project.class);
    QProject cand = QProject.candidate();
    query.result(false, cand.budget.sum());
    query.filter(cand.projid.eq(0L));

    QueryElementHolder<Project> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "SUM(budget)",
            /*INTO*/ null,
            /*FROM*/ Project.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "projid == 0",
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testSum4() {
    // SUM(((FullTimeEmployee)manager).salary)
    Object expected = Double.valueOf(20000);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    // DataNucleus: java.lang.ClassCastException:
    // org.datanucleus.api.jdo.query.PersistableExpressionImpl
    // cannot be cast to org.apache.jdo.tck.pc.company.QFullTimeEmployee
    QFullTimeEmployee cast = (QFullTimeEmployee) cand.manager.cast(FullTimeEmployee.class);
    query.result(false, cast.salary.sum());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "SUM(((FullTimeEmployee)manager).salary)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testSum5() {
    // SUM(DISTINCT ((FullTimeEmployee)manager).salary)
    Object expected = Double.valueOf(10000);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    // DataNucleus: )java.lang.ClassCastException:
    // org.datanucleus.api.jdo.query.PersistableExpressionImpl
    // cannot be cast to org.apache.jdo.tck.pc.company.QFullTimeEmployee
    QFullTimeEmployee cast = (QFullTimeEmployee) cand.manager.cast(FullTimeEmployee.class);
    query.result(false, cast.salary.sumDistinct());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "SUM(DISTINCT ((FullTimeEmployee)manager).salary)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testMin0() {
    // MIN(long)
    Object expected = Long.valueOf(1);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    query.result(false, cand.personid.min());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "MIN(personid)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testMin1() {
    // MIN(double)
    Object expected = Double.valueOf(10000.0);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    query.result(false, cand.salary.min());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "MIN(salary)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testMin2() {
    // MIN(BigDecimal)
    Object expected = new BigDecimal("2000.99");

    JDOQLTypedQuery<Project> query = getPM().newJDOQLTypedQuery(Project.class);
    QProject cand = QProject.candidate();
    query.result(false, cand.budget.min());

    QueryElementHolder<Project> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
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
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testMin3() {
    // MIN(budget)
    Object expected = null;

    JDOQLTypedQuery<Project> query = getPM().newJDOQLTypedQuery(Project.class);
    QProject cand = QProject.candidate();
    query.result(false, cand.budget.min());
    query.filter(cand.projid.eq(0L));

    QueryElementHolder<Project> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "MIN(budget)",
            /*INTO*/ null,
            /*FROM*/ Project.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "projid == 0",
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testMin4() {
    // MIN(((FullTimeEmployee)manager).salary)
    Object expected = Double.valueOf(10000);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    // DataNucleus: ClassCastException: org.datanucleus.api.jdo.query.PersistableExpressionImpl
    // cannot be cast to org.apache.jdo.tck.pc.company.QFullTimeEmployee
    QFullTimeEmployee cast = (QFullTimeEmployee) cand.manager.cast(FullTimeEmployee.class);
    query.result(false, cast.salary.min());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "MIN(((FullTimeEmployee)manager).salary)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }
  /** */
  public void testMax0() {
    // MAX(long)
    Object expected = Long.valueOf(5);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    query.result(false, cand.personid.max());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "MAX(personid)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testMax1() {
    // MAX(double)
    Object expected = Double.valueOf(45000.0);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    query.result(false, cand.salary.max());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "MAX(salary)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testMax2() {
    // MAX(BigDecimal)
    Object expected = new BigDecimal("2500000.99");

    JDOQLTypedQuery<Project> query = getPM().newJDOQLTypedQuery(Project.class);
    QProject cand = QProject.candidate();
    query.result(false, cand.budget.max());

    QueryElementHolder<Project> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
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
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testMax3() {
    // MAX(budget)
    Object expected = null;

    JDOQLTypedQuery<Project> query = getPM().newJDOQLTypedQuery(Project.class);
    QProject cand = QProject.candidate();
    query.result(false, cand.budget.max());
    query.filter(cand.projid.eq(0L));

    QueryElementHolder<Project> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "MAX(budget)",
            /*INTO*/ null,
            /*FROM*/ Project.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "projid == 0",
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testMax4() {
    // MAX(((FullTimeEmployee)manager).salary)
    Object expected = Double.valueOf(10000);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    // DataNucleus: java.lang.ClassCastException:
    // org.datanucleus.api.jdo.query.PersistableExpressionImpl
    // cannot be cast to org.apache.jdo.tck.pc.company.QFullTimeEmployee
    QFullTimeEmployee cast = (QFullTimeEmployee) cand.manager.cast(FullTimeEmployee.class);
    query.result(false, cast.salary.max());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "MAX(((FullTimeEmployee)manager).salary)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testAvg0() {
    // AVG(long)
    Object expected = Double.valueOf(3);

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.result(false, cand.personid.avg());

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "AVG(personid)",
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

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testAvg1() {
    // AVG(double)
    Object expected = Double.valueOf(25000.0);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    query.result(false, cand.salary.avg());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "AVG(salary)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testAvg2() {
    // AVG(BigDecimal)
    Object expected = Double.valueOf("99.997");

    JDOQLTypedQuery<DentalInsurance> query = getPM().newJDOQLTypedQuery(DentalInsurance.class);
    QDentalInsurance cand = QDentalInsurance.candidate();
    query.result(false, cand.lifetimeOrthoBenefit.avg());

    QueryElementHolder<DentalInsurance> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "AVG(lifetimeOrthoBenefit)",
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
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testAvg3() {
    // AVG(lifetimeOrthoBenefit)
    Object expected = null;

    JDOQLTypedQuery<DentalInsurance> query = getPM().newJDOQLTypedQuery(DentalInsurance.class);
    QDentalInsurance cand = QDentalInsurance.candidate();
    query.result(false, cand.lifetimeOrthoBenefit.avg());
    query.filter(cand.insid.eq(0L));

    QueryElementHolder<DentalInsurance> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "AVG(lifetimeOrthoBenefit)",
            /*INTO*/ null,
            /*FROM*/ DentalInsurance.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "insid == 0",
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testAvg4() {
    // AVG(((FullTimeEmployee)manager).salary)
    Object expected = Double.valueOf(10000);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    // DataNucleus: java.lang.ClassCastException:
    // org.datanucleus.api.jdo.query.PersistableExpressionImpl
    // cannot be cast to org.apache.jdo.tck.pc.company.QFullTimeEmployee
    QFullTimeEmployee cast = (QFullTimeEmployee) cand.manager.cast(FullTimeEmployee.class);
    query.result(false, cast.salary.avg());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "AVG(((FullTimeEmployee)manager).salary)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  /** */
  public void testAvg5() {
    // AVG(DISTINCT ((FullTimeEmployee)manager).salary)
    Object expected = Double.valueOf(10000);

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    // DataNucleus: java.lang.ClassCastException:
    // org.datanucleus.api.jdo.query.PersistableExpressionImpl
    // cannot be cast to org.apache.jdo.tck.pc.company.QFullTimeEmployee
    QFullTimeEmployee cast = (QFullTimeEmployee) cand.manager.cast(FullTimeEmployee.class);
    query.result(false, cast.salary.avgDistinct());

    QueryElementHolder<FullTimeEmployee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "AVG(DISTINCT ((FullTimeEmployee)manager).salary)",
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
  }

  public void testNegative() {
    for (QueryElementHolder<?> invalidQuery : INVALID_QUERIES) {
      compileAPIQuery(ASSERTION_FAILED, invalidQuery, false);
      compileSingleStringQuery(ASSERTION_FAILED, invalidQuery, false);
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
}
