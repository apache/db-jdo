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

package org.apache.jdo.tck.query.jdoql;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.QDepartment;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;
import java.util.List;

/**
 *<B>Title:</B> Navigation Through a Null-Valued Field
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-9.
 *<BR>
 *<B>Assertion Description: </B>
 * Navigation through a null-valued field, which would throw
 * <code>NullPointerException</code>, is treated as if the filter expression 
 * returned <code>false</code> for the evaluation of the current set of variable
 * values. Other values for variables might still qualify the candidate instance
 * for inclusion in the result set.
 */

public class NavigationThroughANullValuedField extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-9 (NavigationThroughANullValuedField) failed: ";
    
    /** */
    public static final String NAVIGATION_TEST_COMPANY_TESTDATA = 
        "org/apache/jdo/tck/pc/company/companyForNavigationTests.xml";

    /**
     * Returns the name of the company test data resource.
     * @return name of the company test data resource. 
     */
    @Override
    protected String getCompanyTestDataResource() {
        return NAVIGATION_TEST_COMPANY_TESTDATA;
    }
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NavigationThroughANullValuedField.class);
    }
    
    public void testPositive1() {
        // navigation through reference relationship field
        // the relationship medicalInsurance is not set for emp2 and emp3 =>
        // they should not be part of the result
        List<Employee> expected = getTransientCompanyModelInstancesAsList(Employee.class, "emp1");

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.filter(cand.medicalInsurance.carrier.eq("Carrier1"));

        QueryElementHolder<Employee> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "this.medicalInsurance.carrier == \"Carrier1\"",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/   query,
                /*paramValues*/  null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);

    }
    public void testPositive2() {
        // navigation through reference relationship field
        // emp5 and emp6 have have emp4 as manager
        List<Employee> expected = getTransientCompanyModelInstancesAsList(Employee.class, "emp5", "emp6");

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.filter(cand.manager.lastname.eq("emp4Last"));

        QueryElementHolder<Employee> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "this.manager.lastname == \"emp4Last\"",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/   query,
                /*paramValues*/  null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }
    public void testPositive3() {
        // multiple navigation through reference relationship field
        List<Employee> expected =
                getTransientCompanyModelInstancesAsList(Employee.class, "emp2", "emp3", "emp10");

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.filter(cand.manager.manager.lastname.eq("emp0Last"));

        QueryElementHolder<Employee> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "this.manager.manager.lastname == \"emp0Last\"",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/   query,
                /*paramValues*/  null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }
    public void testPositive4() {
        // navigation through collection relationship field
        // employees emp2 and emp3 do not have a medicalInsurance, but emp1
        // matches the filter such that dept1 qualifies for inclusion in the
        // result set.
        List<Department> expected = getTransientCompanyModelInstancesAsList(Department.class, "dept1");

        JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
        QDepartment cand = QDepartment.candidate();
        QEmployee e = QEmployee.variable("e");
        query.filter(cand.employees.contains(e).and(e.medicalInsurance.carrier.eq("Carrier1")));

        QueryElementHolder<Department> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Department.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "employees.contains(e) && e.medicalInsurance.carrier == \"Carrier1\"",
                /*VARIABLES*/   "Employee e",
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/   query,
                /*paramValues*/  null);

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
