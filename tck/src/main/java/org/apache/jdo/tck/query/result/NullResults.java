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
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Null Results. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.9-7. <br>
 * <B>Assertion Description: </B> If the returned value from a query specifying a result is null,
 * this indicates that the expression specified as the result was null.
 */
public class NullResults extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.6.9-7 (NullResults) failed: ";

  /** */
  @Test
  public void testUnique() {
    Object expected = null;

    JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
    QEmployee cand = QEmployee.candidate();
    query.filter(cand.lastname.eq("emp2Last"));
    query.result(false, cand.manager);

    QueryElementHolder<Employee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ Boolean.TRUE,
            /*RESULT*/ "manager",
            /*INTO*/ null,
            /*FROM*/ Employee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "lastname == 'emp2Last'",
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
  @Test
  public void testNavigation() {
    Object expected = Arrays.asList(new Object[] {null});

    JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
    QEmployee cand = QEmployee.candidate();
    query.filter(cand.lastname.eq("emp2Last"));
    query.result(false, cand.manager);

    QueryElementHolder<Employee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ "manager",
            /*INTO*/ null,
            /*FROM*/ Employee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "lastname == 'emp2Last'",
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
  @Test
  public void testDistinctNavigation() {
    List<Employee> expected = getTransientCompanyModelInstancesAsList(Employee.class, "emp2", null);

    JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
    QEmployee cand = QEmployee.candidate();
    query.result(true, cand.manager);

    QueryElementHolder<Employee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ "DISTINCT manager",
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
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
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
