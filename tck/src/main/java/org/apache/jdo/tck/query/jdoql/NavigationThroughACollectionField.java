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

package org.apache.jdo.tck.query.jdoql;

import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.QDepartment;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Navigation Through a Collection Field <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-10. <br>
 * <B>Assertion Description: </B> Navigation through multi-valued fields (<code>Collection</code>
 * types) is specified using a variable declaration and the <code>Collection.contains(Object o)
 * </code> method.
 */
public class NavigationThroughACollectionField extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-10 (NavigationThroughACollectionField) failed: ";

  /** */
  public static final String NAVIGATION_TEST_COMPANY_TESTDATA =
      "org.apache.jdo.tck.pc.company.data.NavigationTestData";

  /**
   * Returns the name of the company test data resource.
   *
   * @return name of the company test data resource.
   */
  protected String getCompanyTestDataResource() {
    return NAVIGATION_TEST_COMPANY_TESTDATA;
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  public void testPositive() {
    List<Department> expected = getTransientCompanyModelInstancesAsList(Department.class, "dept1");

    JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
    QDepartment cand = QDepartment.candidate("this");
    QEmployee e = QEmployee.variable("e");
    query.filter(cand.employees.contains(e).and(e.firstname.eq("emp1First")));

    QueryElementHolder<Department> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Department.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "employees.contains(e) && e.firstname == \"emp1First\"",
            /*VARIABLES*/ "Employee e",
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, expected);
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
