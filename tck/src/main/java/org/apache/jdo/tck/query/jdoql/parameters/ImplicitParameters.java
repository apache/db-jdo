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

package org.apache.jdo.tck.query.jdoql.parameters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Implicit parameters. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.3-3. <br>
 * <B>Assertion Description: </B> Parameters implicitly declared (in the result, filter, grouping,
 * ordering, or range) are identified by prepending a ":" to the parameter everywhere it appears.
 * All parameter types can be determined by one of the following techniques:
 */
public class ImplicitParameters extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.3-3 (ImplicitParameters) failed: ";

  private static final String PARAMETER = "parameterInResult";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(ImplicitParameters.class);
  }

  /** */
  public void testResult() {
    Object expected =
        getExpectedResultOfFirstQuery(
            getTransientCompanyModelInstancesAsList(
                new String[] {"emp1", "emp2", "emp3", "emp4", "emp5"}));

    Map<String, Object> paramValues = new HashMap<>();
    paramValues.put("param", PARAMETER);

    QueryElementHolder holder =
        new QueryElementHolder(
            /*UNIQUE*/ null,
            /*RESULT*/ "this, :param",
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
            /*JDOQLTyped*/ null,
            /*paramValues*/ paramValues);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    // TBD executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testFilter() {
    Object expected = getTransientCompanyModelInstancesAsList(new String[] {"emp1"});

    Map<String, Object> paramValues = new HashMap<>();
    paramValues.put("param", "emp1First");

    QueryElementHolder holder =
        new QueryElementHolder(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname == :param",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ null,
            /*paramValues*/ paramValues);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    // TBD executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testGrouping() {
    Object expected = /* Note: "Development" is not a bean name! */ Arrays.asList("Development");

    Map<String, Object> paramValues = new HashMap<>();
    paramValues.put("minValue", Long.valueOf(3));

    // Import Department twice
    QueryElementHolder holder =
        new QueryElementHolder(
            /*UNIQUE*/ null,
            /*RESULT*/ "department.name",
            /*INTO*/ null,
            /*FROM*/ Employee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ null,
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ "department.name HAVING COUNT(this) >= :minValue",
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ null,
            /*paramValues*/ paramValues);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    // TBD executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testRange() {
    Object expected =
        getTransientCompanyModelInstancesAsList(
            new String[] {"emp1", "emp2", "emp3", "emp4", "emp5"});

    Map<String, Object> paramValues = new HashMap<>();
    paramValues.put("zero", Long.valueOf(0));
    paramValues.put("five", Long.valueOf(5));

    // Import Department twice
    QueryElementHolder holder =
        new QueryElementHolder(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ null,
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ ":zero",
            /*TO*/ ":five",
            /*JDOQLTyped*/ null,
            /*paramValues*/ paramValues);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    // TBD executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
  }

  private List getExpectedResultOfFirstQuery(List instances) {
    Object[] expectedResult = new Object[instances.size()];
    for (int i = 0; i < expectedResult.length; i++) {
      expectedResult[i] = new Object[] {instances.get(i), PARAMETER};
    }
    return Arrays.asList(expectedResult);
  }
}
