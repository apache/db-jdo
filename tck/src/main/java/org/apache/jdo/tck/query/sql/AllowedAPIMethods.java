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

package org.apache.jdo.tck.query.sql;

import java.text.MessageFormat;
import java.util.Arrays;
import javax.jdo.JDOUserException;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Allowed API Methods. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.7-2. <br>
 * <B>Assertion Description: </B> The only methods that can be used are setClass to establish the
 * candidate class, setUnique to declare that there is only one result row, and setResultClass to
 * establish the result class.
 */
public class AllowedAPIMethods extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.7-2 (AllowedAPIMethods) failed: ";

  /** The array of valid SQL queries. */
  private static final String[] VALID_SQL_QUERIES = {
    "SELECT * FROM {0}.PrimitiveTypes",
    "SELECT * FROM {0}.departments",
    "SELECT * FROM {0}.persons",
    "SELECT FIRSTNAME, LASTNAME FROM {0}.persons WHERE PERSONID = 1",
    "SELECT FIRSTNAME, LASTNAME FROM {0}.persons"
  };

  /** The expected results of valid SQL queries. */
  private final Object[] expectedResult = {
    getTransientMylibInstancesAsList(
        "primitiveTypesPositive",
        "primitiveTypesNegative",
        "primitiveTypesCharacterStringLiterals"),
    getTransientCompanyModelInstancesAsList(Department.class, "dept1", "dept2"),
    getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp2", "emp3", "emp4", "emp5"),
    new Object[] {"emp1First", "emp1Last"},
    Arrays.asList(
        new FullName("emp1First", "emp1Last"),
        new FullName("emp2First", "emp2Last"),
        new FullName("emp3First", "emp3Last"),
        new FullName("emp4First", "emp4Last"),
        new FullName("emp5First", "emp5Last"))
  };

  /** */
  @Test
  public void testSetClass() {
    if (isSQLSupported()) {
      int index = 0;
      executeSQLQuery(
          ASSERTION_FAILED,
          VALID_SQL_QUERIES[index],
          PrimitiveTypes.class,
          null,
          true,
          null,
          expectedResult[index],
          false);

      index = 1;
      executeSQLQuery(
          ASSERTION_FAILED,
          VALID_SQL_QUERIES[index],
          Department.class,
          null,
          true,
          null,
          expectedResult[index],
          false);

      index = 2;
      executeSQLQuery(
          ASSERTION_FAILED,
          VALID_SQL_QUERIES[index],
          Person.class,
          null,
          true,
          null,
          expectedResult[index],
          false);
    }
  }

  /** */
  @Test
  public void testSetUnique() {
    if (isSQLSupported()) {
      int index = 3;
      executeSQLQuery(
          ASSERTION_FAILED,
          VALID_SQL_QUERIES[index],
          null,
          null,
          true,
          null,
          expectedResult[index],
          true);
    }
  }

  /** */
  @Test
  public void testSetResultClass() {
    if (isSQLSupported()) {
      int index = 4;
      executeSQLQuery(
          ASSERTION_FAILED,
          VALID_SQL_QUERIES[index],
          null,
          FullName.class,
          true,
          null,
          expectedResult[index],
          false);
    }
  }

  /** */
  @Test
  public void testNegative() {
    if (isSQLSupported()) {
      String schema = getPMFProperty("javax.jdo.mapping.Schema");
      // Note that the SQL query below is valid.
      // The query is not executed.
      // Instead, the method uses a query instance
      // to check prohibited setters.
      String sql = "SELECT PERSONID FROM {0}.persons";
      sql = MessageFormat.format(sql, schema);
      Query<?> query = getPM().newQuery("javax.jdo.query.SQL", sql);
      checkProhibitedSetters(query);
    }
  }

  private void checkProhibitedSetters(Query<?> query) {
    checkSetResult(query);
    checkSetFilter(query);
    checkDeclareVariables(query);
    checkDeclareParameters(query);
    checkDeclareImports(query);
    checkSetGrouping(query);
    checkSetOrdering(query);
  }

  private void checkSetResult(Query<?> query) {
    try {
      query.setResult("firstname, lastname");
      methodFailed("setResult()");
    } catch (JDOUserException ignored) {
    }
  }

  private void checkSetFilter(Query<?> query) {
    try {
      query.setFilter("WHERE personid = 1");
      methodFailed("setFilter()");
    } catch (JDOUserException ignored) {
    }
  }

  private void checkDeclareVariables(Query<?> query) {
    try {
      query.declareVariables("Employee emp");
      methodFailed("declareVariables()");
    } catch (JDOUserException ignored) {
    }
  }

  private void checkDeclareParameters(Query<?> query) {
    try {
      query.declareParameters("Employee emp");
      methodFailed("declareParameters()");
    } catch (JDOUserException ignored) {
    }
  }

  private void checkDeclareImports(Query<?> query) {
    try {
      query.declareImports("import org.apache.jdo.tck.pc.company.Employee");
      methodFailed("declareImports()");
    } catch (JDOUserException ignored) {
    }
  }

  private void checkSetGrouping(Query<?> query) {
    try {
      query.setGrouping("firstname");
      methodFailed("setGrouping()");
    } catch (JDOUserException ignored) {
    }
  }

  private void checkSetOrdering(Query<?> query) {
    try {
      query.setOrdering("firstname ASCENDING");
      methodFailed("setOrdering()");
    } catch (JDOUserException ignored) {
    }
  }

  private void methodFailed(String method) {
    fail(ASSERTION_FAILED + method + " on a SQL query must throw JDOUserException.");
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    addTearDownClass(MylibReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
    loadAndPersistMylib(getPM());
  }
}
