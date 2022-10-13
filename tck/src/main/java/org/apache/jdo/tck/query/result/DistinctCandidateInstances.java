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

package org.apache.jdo.tck.query.result;

import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Distinct Candidate Instances. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.9-2. <br>
 * <B>Assertion Description: </B> Queries against an extent always consider only distinct candidate
 * instances, regardless of whether distinct is specified. Queries against a collection might
 * contain duplicate candidate instances; the distinct keyword removes duplicates from the candidate
 * collection in this case.
 */
public class DistinctCandidateInstances extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.9-2 (DistintCandidateInstances) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(DistinctCandidateInstances.class);
  }

  /** */
  public void testExtentQueries0() {
    if (isUnconstrainedVariablesSupported()) {
      List<Employee> expected =
          getTransientCompanyModelInstancesAsList(Employee.class, "emp1", "emp1");

      JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
      query.variable("p", Person.class);

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ null,
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ "Project p",
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
  }

  /** */
  public void testExtentQueries1() {
    if (isUnconstrainedVariablesSupported()) {
      List<Employee> expected = getTransientCompanyModelInstancesAsList(Employee.class, "emp1");

      JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
      QEmployee cand = QEmployee.candidate();
      query.result(true, cand);
      query.variable("p", Person.class);

      QueryElementHolder<Employee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "DISTINCT",
              /*INTO*/ null,
              /*FROM*/ Employee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ "Project p",
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
  }

  /** */
  public void testCollectionQueries() {
    String singleStringQuery = "SELECT FROM " + Person.class.getName();
    String singleStringDistinctQuery = "SELECT DISTINCT FROM " + Person.class.getName();

    List<Person> candidates =
        getPersistentCompanyModelInstancesAsList(Person.class, "emp1", "emp1");
    Query<Person> query = pm.newQuery(Person.class);
    query.setCandidates(candidates);
    query.setResult("this");
    executeJDOQuery(
        ASSERTION_FAILED,
        query,
        singleStringQuery,
        false,
        null,
        getTransientCompanyModelInstancesAsList(Person.class, "emp1", "emp1"),
        true);

    query.setResult("DISTINCT this");
    executeJDOQuery(
        ASSERTION_FAILED,
        query,
        singleStringDistinctQuery,
        false,
        null,
        getTransientCompanyModelInstancesAsList(Person.class, "emp1"),
        true);
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
