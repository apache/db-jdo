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

import java.util.Arrays;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.QFullTimeEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Grouping. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.10-1. <br>
 * <B>Assertion Description: </B> When grouping is specified, each result expression must be one of:
 * an expression contained in the grouping expression; or, an aggregate expression evaluated once
 * per group. The query groups all elements where all expressions specified in setGrouping have the
 * same values. The query result consists of one element per group.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Grouping extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.10-1 (Grouping) failed: ";

  /**
   * The array of invalid queries which may be executed as single string queries and as API queries.
   */
  private static final QueryElementHolder<?>[] INVALID_QUERIES = {
    new QueryElementHolder<>(
        /*UNIQUE*/ null,
        /*RESULT*/ "department, salary",
        /*INTO*/ null,
        /*FROM*/ FullTimeEmployee.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ "department",
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null)
  };

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testPositive() {
    Object expected =
        Arrays.asList(
            new Object[] {
              new Object[] {
                getTransientCompanyModelInstance(Department.class, "dept1"), Double.valueOf(30000.0)
              },
              new Object[] {
                getTransientCompanyModelInstance(Department.class, "dept2"), Double.valueOf(45000.0)
              }
            });
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<FullTimeEmployee> query = pm.newJDOQLTypedQuery(FullTimeEmployee.class);
      QFullTimeEmployee cand = QFullTimeEmployee.candidate();
      query.result(false, cand.department, cand.salary.sum());
      query.groupBy(cand.department);

      QueryElementHolder<FullTimeEmployee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "department, SUM(salary)",
              /*INTO*/ null,
              /*FROM*/ FullTimeEmployee.class,
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
