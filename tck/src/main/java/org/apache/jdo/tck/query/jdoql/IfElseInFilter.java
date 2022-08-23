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

package org.apache.jdo.tck.query.jdoql;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.BooleanExpression;
import javax.jdo.query.IfThenElseExpression;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.pc.company.QFullTimeEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Use of If Else expression in filter <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.x <br>
 * <B>Assertion Description: </B>
 */
public class IfElseInFilter extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.x (IfElseInFilter) failed: ";

  /**
   * The array of invalid queries which may be executed as single string queries and as API queries.
   */
  private static final QueryElementHolder[] INVALID_QUERIES = {
    // Invalid type of condition expression
    new QueryElementHolder(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ Employee.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ "(IF (this.firstname) 0 ELSE 1) == 0",
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null),
    // missing ELSE
    new QueryElementHolder(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ DentalInsurance.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ "(IF (this.employee == null) 15000) == 15000",
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null),
    // type of THEN expr must be the same as type of ELSE expr
    new QueryElementHolder(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ DentalInsurance.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ "(IF (this.employee == null) 'Michael' ELSE this.employee) == 'Michael'",
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
    BatchTestRunner.run(IfElseInFilter.class);
  }

  /** */
  public void testPositive0() {
    // simple If/Else using literals
    Object expected = getTransientCompanyModelInstancesAsList(new String[] {"emp1", "emp5"});

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    IfThenElseExpression<Double> ifExpr =
        query.ifThenElse(cand.department.name.eq("Development"), 15000.0, 25000.0);
    query.filter(cand.salary.gt(ifExpr));
    query.orderBy(cand.personid.asc());

    QueryElementHolder holder =
        new QueryElementHolder(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "this.salary > (IF (this.department.name == 'Development') 15000 ELSE 25000)",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ "this.personid",
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }
  /** */
  public void testPositive1() {
    // simple If/Else using relationships
    Object expected =
        getTransientCompanyModelInstancesAsList(new String[] {"emp1", "emp2", "emp3"});

    JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
    QEmployee cand = QEmployee.candidate();
    IfThenElseExpression<Long> ifExpr =
        query.ifThenElse(
            Long.class,
            cand.manager.eq((Employee) null),
            cand.mentor.department.deptid,
            cand.manager.department.deptid);
    query.filter(ifExpr.eq(cand.department.deptid));
    query.orderBy(cand.personid.asc());

    QueryElementHolder holder =
        new QueryElementHolder(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Employee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "(IF (this.manager == null) this.mentor.department.deptid ELSE this.manager.department.deptid) == this.department.deptid",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ "this.personid",
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }
  /** */
  public void testPositive2() {
    // multiple If/Else with distinct conditions
    Object expected = getTransientCompanyModelInstancesAsList(new String[] {"emp1"});

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    BooleanExpression cond1 = cand.salary.gt(0.0).and(cand.salary.lt(10000.1));
    BooleanExpression cond2 = cand.salary.gt(10000.1).and(cand.salary.lt(20000.1));
    BooleanExpression cond3 = cand.salary.gt(20000.1).and(cand.salary.lt(30000.1));
    IfThenElseExpression<Integer> ifExpr =
        query.ifThen(cond1, 1).ifThen(cond2, 2).ifThen(cond3, 3).elseEnd(4);
    query.filter(ifExpr.eq(2));
    query.orderBy(cand.personid.asc());

    QueryElementHolder holder =
        new QueryElementHolder(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "(IF (0.0 <= this.salary && this.salary < 10000.1) 1 ELSE "
                + "IF (10000.1 <= this.salary && this.salary < 20000.1) 2 ELSE "
                + "IF (20000.1 <= this.salary && this.salary < 30000.1) 3 ELSE 4) == 2",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ "this.personid",
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }
  /** */
  public void testPositive3() {
    // multiple If/Else with overlapping conditions
    Object expected = getTransientCompanyModelInstancesAsList(new String[] {"emp1"});

    JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
    QFullTimeEmployee cand = QFullTimeEmployee.candidate();
    BooleanExpression cond1 = cand.salary.lt(10000.1);
    BooleanExpression cond2 = cand.salary.lt(20000.1);
    BooleanExpression cond3 = cand.salary.lt(30000.1);
    IfThenElseExpression<Integer> ifExpr =
        query.ifThen(cond1, 1).ifThen(cond2, 2).ifThen(cond3, 3).elseEnd(4);
    query.filter(ifExpr.eq(2));

    QueryElementHolder holder =
        new QueryElementHolder(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ FullTimeEmployee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "(IF (this.salary < 10000.1) 1 ELSE "
                + "IF (this.salary < 20000.1) 2 ELSE IF (this.salary < 30000.1) 3 ELSE 4) == 2",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ "this.personid",
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testNegative() {
    for (int i = 0; i < INVALID_QUERIES.length; i++) {
      compileAPIQuery(ASSERTION_FAILED, INVALID_QUERIES[i], false);
      compileSingleStringQuery(ASSERTION_FAILED, INVALID_QUERIES[i], false);
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
