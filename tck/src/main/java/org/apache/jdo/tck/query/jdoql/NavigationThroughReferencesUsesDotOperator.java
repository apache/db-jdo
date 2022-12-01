/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.query.jdoql;

import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.MedicalInsurance;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.pc.company.QMedicalInsurance;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Navigation Through a References uses Dot Operator <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-13. <br>
 * <B>Assertion Description: </B> Navigation through single-valued fields is specified by the Java
 * language syntax of <code>field_name.field_name....field_name</code>.
 */
public class NavigationThroughReferencesUsesDotOperator extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-13 (NavigationThroughReferencesUsesDotOperator) failed: ";

  /** */
  public static final String NAVIGATION_TEST_COMPANY_TESTDATA =
      "org/apache/jdo/tck/pc/company/companyForNavigationTests.xml";

  /**
   * Returns the name of the company test data resource.
   *
   * @return name of the company test data resource.
   */
  @Override
  protected String getCompanyTestDataResource() {
    return NAVIGATION_TEST_COMPANY_TESTDATA;
  }

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(NavigationThroughReferencesUsesDotOperator.class);
  }

  public void testPositive0() {
    // navigation through one relationship
    List<Employee> expected = getTransientCompanyModelInstancesAsList(Employee.class, "emp1");

    JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
    QEmployee cand = QEmployee.candidate();
    query.filter(cand.medicalInsurance.carrier.eq("Carrier1"));

    QueryElementHolder<Employee> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Employee.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "medicalInsurance.carrier == \"Carrier1\"",
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

  public void testPositive1() {
    // navigation through multiple relationships
    List<MedicalInsurance> expected =
        getTransientCompanyModelInstancesAsList(
            MedicalInsurance.class,
            "medicalIns1",
            "medicalIns2",
            "medicalIns3",
            "medicalIns4",
            "medicalIns5");

    JDOQLTypedQuery<MedicalInsurance> query = getPM().newJDOQLTypedQuery(MedicalInsurance.class);
    QMedicalInsurance cand = QMedicalInsurance.candidate();
    query.filter(cand.employee.department.name.eq("Development"));

    QueryElementHolder<MedicalInsurance> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ MedicalInsurance.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "this.employee.department.name == \"Development\"",
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

  public void testPositive2() {
    // navigation through a self referencing relationship
    List<MedicalInsurance> expected =
        getTransientCompanyModelInstancesAsList(
            MedicalInsurance.class, "medicalIns2", "medicalIns3");

    JDOQLTypedQuery<MedicalInsurance> query = getPM().newJDOQLTypedQuery(MedicalInsurance.class);
    QMedicalInsurance cand = QMedicalInsurance.candidate();
    query.filter(cand.employee.manager.firstname.eq("emp1First"));

    QueryElementHolder<MedicalInsurance> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ MedicalInsurance.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "this.employee.manager.firstname == \"emp1First\"",
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

  public void testPositive3() {
    // navigation through a self referencing relationship multiple times
    List<MedicalInsurance> expected =
        getTransientCompanyModelInstancesAsList(
            MedicalInsurance.class, "medicalIns2", "medicalIns3");

    JDOQLTypedQuery<MedicalInsurance> query = getPM().newJDOQLTypedQuery(MedicalInsurance.class);
    QMedicalInsurance cand = QMedicalInsurance.candidate();
    query.filter(cand.employee.manager.manager.firstname.eq("emp0First"));

    QueryElementHolder<MedicalInsurance> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ MedicalInsurance.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "this.employee.manager.manager.firstname == \"emp0First\"",
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
