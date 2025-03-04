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
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.query.IfThenElseExpression;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.company.QDentalInsurance;
import org.apache.jdo.tck.pc.company.QProject;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> IfElseResult. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> . <br>
 * <B>Assertion Description: </B>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IfElseResult extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion (IfElseResult) failed: ";

  /**
   * The array of invalid queries which may be executed as single string queries and as API queries.
   */
  private static final QueryElementHolder<?>[] INVALID_QUERIES = {
    // Invalid type of condition expression
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ "IF (this.firstname) 0 ELSE 1",
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
        /*TO*/ null),
    // missing ELSE
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ "IF (this.employee == null) 0",
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
        /*TO*/ null),
    // type of THEN expr must be the same as type of ELSE expr
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ "IF (this.employee == null) 'Michael' ELSE this.employee",
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
        /*TO*/ null)
  };

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive0() {
    Object expected =
        Arrays.asList("emp1Last", "emp2Last", "emp3Last", "emp4Last", "emp5Last", "No employee");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<DentalInsurance> query = pm.newJDOQLTypedQuery(DentalInsurance.class);
      QDentalInsurance cand = QDentalInsurance.candidate("this");
      query.result(
          false,
          query.ifThenElse(
              cand.employee.eq((Employee) null), "No employee", cand.employee.lastname));
      query.orderBy(cand.insid.asc());

      QueryElementHolder<DentalInsurance> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "IF (this.employee == null) 'No employee' ELSE this.employee.lastname",
              /*INTO*/ null,
              /*FROM*/ DentalInsurance.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ "this.insid ascending",
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
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive1() {
    Object expected =
        Arrays.asList(
            new BigDecimal("3000001.188"), new BigDecimal("55000"), new BigDecimal("2201.089"));
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Project> query = pm.newJDOQLTypedQuery(Project.class);
      QProject cand = QProject.candidate("this");
      query.result(
          false,
          query.ifThenElse(
              BigDecimal.class,
              cand.members.size().gt(2),
              cand.budget.mul(new BigDecimal("1.2")),
              cand.budget.mul(new BigDecimal("1.1"))));
      query.orderBy(cand.projid.asc());

      QueryElementHolder<Project> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "IF (this.members.size() > 2) this.budget * 1.2 ELSE this.budget * 1.1",
              /*INTO*/ null,
              /*FROM*/ Project.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ "this.projid ascending",
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
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive2() {
    Object expected = Arrays.asList("No reviewer", "Reviewer team", "Single reviewer");
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<Project> query = pm.newJDOQLTypedQuery(Project.class);
      QProject cand = QProject.candidate("this");
      IfThenElseExpression<String> ifThenElse =
          query
              .ifThen(cand.reviewers.isEmpty(), "No reviewer")
              .ifThen(cand.reviewers.size().eq(1), "Single reviewer")
              .elseEnd("Reviewer team");
      query.result(false, ifThenElse);
      query.orderBy(cand.projid.asc());

      QueryElementHolder<Project> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "IF (this.reviewers.isEmpty()) 'No reviewer' "
                  + "ELSE IF (this.reviewers.size() == 1) 'Single reviewer' ELSE 'Reviewer team'",
              /*INTO*/ null,
              /*FROM*/ Project.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ "this.projid ascending",
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
  public void testNegative() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      for (QueryElementHolder<?> invalidQuery : INVALID_QUERIES) {
        compileAPIQuery(ASSERTION_FAILED, pm, invalidQuery, false);
        compileSingleStringQuery(ASSERTION_FAILED, pm, invalidQuery, false);
      }
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
