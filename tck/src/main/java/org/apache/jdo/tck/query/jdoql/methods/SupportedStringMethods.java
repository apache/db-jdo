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

package org.apache.jdo.tck.query.jdoql.methods;

import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QDepartment;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Supported String methods. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-47. <br>
 * <B>Assertion Description: </B> New supported String methods:
 *
 * <ul>
 *   <li>toLowerCase()
 *   <li>toUpperCase()
 *   <li>indexOf(String)
 *   <li>indexOf(String, int)
 *   <li>matches(String)
 *   <li>substring(int)
 *   <li>substring(int, int)
 *   <li>startsWith(String)
 *   <li>endsWith(String)
 *   <li>charAt(int)
 *   <li>startsWith(String, int)
 *   <li>length()
 *   <li>trim()
 * </ul>
 */
public class SupportedStringMethods extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-47 (SupportedStringMethods) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SupportedStringMethods.class);
  }

  /** */
  public void testToLowerCase() {
    List<Person> expected = getTransientCompanyModelInstancesAsList(Person.class, "emp1");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.toLowerCase().eq("emp1first"));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.toLowerCase() == 'emp1first'",
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
  public void testToUpperCase() {
    List<Person> expected = getTransientCompanyModelInstancesAsList(Person.class, "emp1");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.toUpperCase().eq("EMP1FIRST"));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.toUpperCase() == 'EMP1FIRST'",
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
  public void testIndexOfString() {
    List<Department> expected = getTransientCompanyModelInstancesAsList(Department.class, "dept1");

    JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
    QDepartment cand = QDepartment.candidate();
    query.filter(cand.name.indexOf("e").eq(1));

    QueryElementHolder<Department> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Department.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "name.indexOf('e') == 1",
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
  public void testIndexOfStringInt() {
    List<Department> expected = getTransientCompanyModelInstancesAsList(Department.class, "dept1");

    JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
    QDepartment cand = QDepartment.candidate();
    query.filter(cand.name.indexOf("e", 2).eq(3));

    QueryElementHolder<Department> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Department.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "name.indexOf('e', 2) == 3",
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
  public void testMatches1() {
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.matches(".*First"));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.matches('.*First')",
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
  public void testMatches2() {
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.matches("emp.First"));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.matches('emp.First')",
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
  public void testMatches3() {
    List<Person> expected = getTransientCompanyModelInstancesAsList(Person.class, "emp1");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.matches("(?i)EMP1FIRST"));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.matches('(?i)EMP1FIRST')",
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
  public void testSubstringInt() {
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.substring(4).eq("First"));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.substring(4) == 'First'",
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
  public void testSubstringIntInt() {
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.substring(4, 9).eq("First"));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.substring(4,9) == 'First'",
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
  public void testStartsWith() {
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.startsWith("emp"));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.startsWith('emp')",
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
  public void testEndsWith() {
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.endsWith("First"));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.endsWith('First')",
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
  public void testStartsWithIndexed() {
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.startsWith("mp", 1));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.startsWith('mp', 1)",
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
  public void testLength() {
    List<Person> expected =
        getTransientCompanyModelInstancesAsList(
            Person.class, "emp1", "emp2", "emp3", "emp4", "emp5");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.length().eq(9));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.length() == 9",
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
  public void testCharAt() {
    List<Person> expected = getTransientCompanyModelInstancesAsList(Person.class, "emp1");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.charAt(3).eq('1'));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.charAt(3) == '1'",
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
  public void testTrim() {
    List<Person> expected = getTransientCompanyModelInstancesAsList(Person.class, "emp1");

    JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
    QPerson cand = QPerson.candidate();
    query.filter(cand.firstname.trim().eq("emp1First"));

    QueryElementHolder<Person> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Person.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "firstname.trim() == 'emp1First'",
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

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(getPM());
  }
}
