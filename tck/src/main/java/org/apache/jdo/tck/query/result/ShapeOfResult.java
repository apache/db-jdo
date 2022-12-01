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
import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Shape of Result. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.12-2. <br>
 * <B>Assertion Description: </B> Table 6: Shape of Result (C is the candidate class)
 */
public class ShapeOfResult extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.12-2 (ShapeOfResult) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(ShapeOfResult.class);
  }

  /** */
  public void testNoResult() {
    // result: null
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
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
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testThisAsC() {
    // result: this AS C
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.result(false, cand);

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ "this AS Person",
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  public void testNoResultUnique() {
    // result: null, unique: true
    Object expected = getTransientCompanyModelInstance(Employee.class, "emp1");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.personid.eq(1l));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "personid == 1",
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
  public void testThisAsCUnique() {
    // result: this AS C, unique: true
    Object expected = getTransientCompanyModelInstance(Employee.class, "emp1");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.personid.eq(1l));
    query.result(false, cand);

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "this AS Person",
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "personid == 1",
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
  public void testSingleExpression() {
    // result: expression of type T
    Object expected =
        Arrays.asList("emp1First", "emp2First", "emp3First", "emp4First", "emp5First");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.result(false, cand.firstname);

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ "firstname",
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
  public void testSingleExpressionUnique() {
    // result: expression of type T, unique: true
    Object expected = "emp1First";

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.personid.eq(1l));
    query.result(false, cand.firstname);

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "firstname",
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "personid == 1",
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
  public void testMultipleExpressions() {
    // result: multiple expressions of type T
    Object expected =
        Arrays.asList(
            new Object[] {"emp1First", "emp1Last"},
            new Object[] {"emp2First", "emp2Last"},
            new Object[] {"emp3First", "emp3Last"},
            new Object[] {"emp4First", "emp4Last"},
            new Object[] {"emp5First", "emp5Last"});

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.result(false, cand.firstname, cand.lastname);

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ "firstname, lastname",
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
  public void testMultipleExpressionsUnique() {
    // result: multiple expressions of type T, unique: true
    Object expected = new Object[] {"emp1First", "emp1Last"};

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.personid.eq(1l));
    query.result(false, cand.firstname, cand.lastname);

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "firstname, lastname",
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "personid == 1",
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
  public void testMultipleExpressionsResultClass() {
    // result: multiple expressions of type T, result class
    Object expected =
        Arrays.asList(
            new FullName("emp1First", "emp1Last"),
            new FullName("emp2First", "emp2Last"),
            new FullName("emp3First", "emp3Last"),
            new FullName("emp4First", "emp4Last"),
            new FullName("emp5First", "emp5Last"));

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    // JDOQLTypedQuery API
    query.result(false, cand.firstname, cand.lastname);

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ "firstname, lastname",
            /*INTO*/ FullName.class,
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, FullName.class, true, expected);
  }

  /** */
  public void testMultipleExpressionResultClassUnique() {
    // result: multiple expressions of type T, result class, unique: true
    Object expected = new FullName("emp1First", "emp1Last");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.personid.eq(1l));
    // JDOQLTypedQuery API
    query.result(false, cand.firstname, cand.lastname);

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "firstname, lastname",
            /*INTO*/ FullName.class,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "personid == 1",
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
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, FullName.class, true, expected);
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
