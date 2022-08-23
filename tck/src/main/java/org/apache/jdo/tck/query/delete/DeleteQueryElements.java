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

package org.apache.jdo.tck.query.delete;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.Insurance;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Delete Query Elements. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.8-3 <br>
 * <B>Assertion Description: </B> Query elements filter, parameters, imports, variables, and unique
 * are valid in queries used for delete. Elements result, result class, range, grouping, and
 * ordering are invalid. If any of these elements is set to its non-default value when one of the
 * deletePersistentAll methods is called, a JDOUserException is thrown and no instances are deleted.
 */
public class DeleteQueryElements extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.8-1 (DeleteQueryElements) failed: ";

  /**
   * The array of invalid queries which may be executed as single string queries and as API queries.
   */
  private static final QueryElementHolder[] INVALID_QUERIES = {
    // The query is invalid because it defines a result clause.
    new QueryElementHolder(
        /*UNIQUE*/ null,
        /*RESULT*/ "carrier",
        /*INTO*/ null,
        /*FROM*/ Insurance.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null),
    // The query is invalid because it defines a result class.
    new QueryElementHolder(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ String.class,
        /*FROM*/ Insurance.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null),
    // The query is invalid because it defines a result clause
    // and a result class.
    new QueryElementHolder(
        /*UNIQUE*/ null,
        /*RESULT*/ "carrier",
        /*INTO*/ String.class,
        /*FROM*/ Insurance.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null),
    // The query is invalid because it defines a grouping clause.
    new QueryElementHolder(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ Insurance.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ "carrier",
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null),
    // The query is invalid because it defines a result clause
    // and a grouping clause
    new QueryElementHolder(
        /*UNIQUE*/ null,
        /*RESULT*/ "carrier",
        /*INTO*/ null,
        /*FROM*/ Insurance.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ "carrier",
        /*ORDER BY*/ null,
        /*FROM*/ null,
        /*TO*/ null),
    // The query is invalid because it defines an ordering clause.
    new QueryElementHolder(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ Insurance.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ "carrier",
        /*FROM*/ null,
        /*TO*/ null),
    // The query is invalid because it defines a range clause.
    new QueryElementHolder(
        /*UNIQUE*/ null,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ Insurance.class,
        /*EXCLUDE*/ null,
        /*WHERE*/ null,
        /*VARIABLES*/ null,
        /*PARAMETERS*/ null,
        /*IMPORTS*/ null,
        /*GROUP BY*/ null,
        /*ORDER BY*/ null,
        /*FROM*/ "0",
        /*TO*/ "5"),
    // The query is valid but deletePersistentAll is expected
    // to throw a JDOUserException because it defines a
    // unique clause but it affects multiple rows in the database.
    new QueryElementHolder(
        /*UNIQUE*/ Boolean.TRUE,
        /*RESULT*/ null,
        /*INTO*/ null,
        /*FROM*/ Insurance.class,
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

  /** Parameters of valid queries. */
  private Object[][] parameters = {{new BigDecimal("2500000")}};

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(DeleteQueryElements.class);
  }

  /** */
  public void testAPI() {
    Map<String, Object> paramValues = new HashMap<>();
    paramValues.put("limit", new BigDecimal("2500000"));

    QueryElementHolder holder =
        new QueryElementHolder(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ DentalInsurance.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "((FullTimeEmployee)employee).salary > 10000 & "
                + "employee.projects.contains(p) & p.budget > limit",
            /*VARIABLES*/ "Project p",
            /*PARAMETERS*/ "BigDecimal limit",
            /*IMPORTS*/ "import org.apache.jdo.tck.pc.company.Project; "
                + "import java.math.BigDecimal;",
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ null,
            /*paramValues*/ paramValues);

    deletePersistentAllByAPIQuery(ASSERTION_FAILED, holder, 1);
  }

  /** */
  public void testSingleString() {
    Map<String, Object> paramValues = new HashMap<>();
    paramValues.put("limit", new BigDecimal("2500000"));

    QueryElementHolder holder =
        new QueryElementHolder(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ DentalInsurance.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "((FullTimeEmployee)employee).salary > 10000 & "
                + "employee.projects.contains(p) & p.budget > limit",
            /*VARIABLES*/ "Project p",
            /*PARAMETERS*/ "BigDecimal limit",
            /*IMPORTS*/ "import org.apache.jdo.tck.pc.company.Project; "
                + "import java.math.BigDecimal;",
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ null,
            /*paramValues*/ paramValues);

    deletePersistentAllBySingleStringQuery(ASSERTION_FAILED, holder, 1);
  }

  /** */
  public void testNegative() {
    for (int i = 0; i < INVALID_QUERIES.length; i++) {
      deletePersistentAllByAPIQuery(ASSERTION_FAILED, INVALID_QUERIES[i], -1);
      deletePersistentAllBySingleStringQuery(ASSERTION_FAILED, INVALID_QUERIES[i], -1);
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
