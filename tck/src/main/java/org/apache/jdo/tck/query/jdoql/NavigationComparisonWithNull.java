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

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.MedicalInsurance;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.pc.company.QMedicalInsurance;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *<B>Title:</B> Navigation Through a Reference and comparing a Relationship with null
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-13.
 *<BR>
 *<B>Assertion Description: </B>
 * Navigation through single-valued fields is specified by the Java language
 * syntax of <code>field_name.field_name....field_name</code>.
 */

public class NavigationComparisonWithNull extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-13 (NavigationComparisonWithNull) failed: ";

    /** */
    public static final String NAVIGATION_TEST_COMPANY_TESTDATA = 
        "org/apache/jdo/tck/pc/company/companyForNavigationTests.xml";

    /**
     * Returns the name of the company test data resource.
     * @return name of the company test data resource. 
     */
    protected String getCompanyTestDataResource() {
        return NAVIGATION_TEST_COMPANY_TESTDATA;
    }

    /**
     * Manager relationship:
     * emp0
     *   emp1
     *     emp2
     *     emp3
     *     emp10
     * emp4
     *   emp5
     *   emp6
     * emp7
     *   emp8
     *   emp9
     */
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NavigationThroughReferencesUsesDotOperator.class);
    }

    /**
     * this.manager == null
     */
    public void testPositive0() {
        // 0: simple manager check being null
        Object expected = getTransientCompanyModelInstancesAsList(
                new String[]{"emp0", "emp4", "emp7"});
        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.filter(cand.manager.eq((Employee)null));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "this.manager == null",
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

    /**
     * this.manager != null
     */
    public void testPositive1() {
        // 1: simple manager check being not null
        Object expected = getTransientCompanyModelInstancesAsList(
                new String[]{"emp1", "emp2", "emp3", "emp5", "emp6", "emp8", "emp9", "emp10"});
        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.filter(cand.manager.ne((Employee)null));
        
        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "this.manager != null",
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

    /**
     * !(this.manager == null)
     */
    public void testPositive2() {
        // 2: simple manager check being not null using not operator
        Object expected = getTransientCompanyModelInstancesAsList(
                new String[]{"emp1", "emp2", "emp3", "emp5", "emp6", "emp8", "emp9", "emp10"});
        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.filter(cand.manager.eq((Employee)null).not());

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "!(this.manager == null)",
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
        // DataNucleus: UnsupportedOperationException: Dont currently support operator NOT  in JDOQL conversion
        //executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /**
     * this.manager.manager == null
     * Disabled, because it currently fails on the RI.
     */
    public void testPositive3() {
        // 3: manager's manager check
        Object expected = getTransientCompanyModelInstancesAsList(
                new String[]{"emp0", "emp1", "emp4", "emp5", "emp6", "emp7", "emp8", "emp9"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.filter(cand.manager.manager.eq((Employee)null));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "this.manager.manager == null",
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
    
    /**
     * this.manager != null && this.manager.manager == null
     */
    public void testPositive4() {
       // 4: manager's manager check with extra check on first level manager
        Object expected = getTransientCompanyModelInstancesAsList(
                new String[]{"emp1", "emp5", "emp6", "emp8", "emp9"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.filter(cand.manager.ne((Employee)null).and(cand.manager.manager.eq((Employee)null)));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "this.manager != null && this.manager.manager == null",
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

    /**
     * this.manager.manager != null
     */
    public void testPositive5() {
        // 5 : manager's manager check not being null
        Object expected = getTransientCompanyModelInstancesAsList(
                new String[]{"emp2", "emp3", "emp10"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.filter(cand.manager.manager.ne((Employee)null));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "this.manager.manager != null",
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

    /**
     * !(this.manager.manager == null)
     */
    public void testPositive6() {
        // 6 : manager's manager check not being null using not operator
        Object expected = getTransientCompanyModelInstancesAsList(
            new String[]{"emp2", "emp3", "emp10"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        QEmployee e = QEmployee.variable("e");
        query.filter(cand.manager.manager.eq((Employee)null).not());

        QueryElementHolder holder  =new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "!(this.manager.manager == null)",
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
        // DataNucleus: UnsupportedOperationException: Dont currently support operator NOT  in JDOQL conversion
        //executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /**
     * this.employee.manager.manager == null
     * Disabled, because it currently fails on the RI.
     */
    public void testPositive7() {
        Object expected = getTransientCompanyModelInstancesAsList(
                new String[]{"medicalIns1", "medicalIns4", "medicalIns5", "medicalIns98"});

        JDOQLTypedQuery<MedicalInsurance> query = getPM().newJDOQLTypedQuery(MedicalInsurance.class);
        QMedicalInsurance cand = QMedicalInsurance.candidate();
        query.filter(cand.employee.manager.manager.eq((Employee)null));

        QueryElementHolder holder  = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        MedicalInsurance.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "this.employee.manager.manager == null",
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

    /**
     * this.employee != null && this.employee.manager != null && this.employee.manager.manager == null
     */
    public void testPositive8() {
        // 8 : multiple relationships 
        Object expected = getTransientCompanyModelInstancesAsList(
                new String[]{"medicalIns1", "medicalIns5"});

        JDOQLTypedQuery<MedicalInsurance> query = getPM().newJDOQLTypedQuery(MedicalInsurance.class);
        QMedicalInsurance cand = QMedicalInsurance.candidate();
        query.filter(cand.employee.ne((Employee)null).and(cand.employee.manager.ne((Employee)null))
                .and(cand.employee.manager.manager.eq((Employee)null)));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        MedicalInsurance.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "this.employee != null && this.employee.manager != null && " +
                                 "this.employee.manager.manager == null",
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

    /**
     * this.employee.manager.manager != null
     */
    public void testPositive9() {
        // 9 : multiple relationships 
        Object expected = getTransientCompanyModelInstancesAsList(
                new String[]{"medicalIns2", "medicalIns3"});

        JDOQLTypedQuery<MedicalInsurance> query = getPM().newJDOQLTypedQuery(MedicalInsurance.class);
        QMedicalInsurance cand = QMedicalInsurance.candidate();
        query.filter(cand.employee.manager.manager.ne((Employee)null));

        QueryElementHolder holder  = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        MedicalInsurance.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "this.employee.manager.manager != null",
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

    /**
     * !(this.employee.manager.manager == null)
     */
    public void testPositive10() {
        Object expected = getTransientCompanyModelInstancesAsList(
                new String[]{"medicalIns2", "medicalIns3"});

        JDOQLTypedQuery<MedicalInsurance> query = getPM().newJDOQLTypedQuery(MedicalInsurance.class);
        QMedicalInsurance cand = QMedicalInsurance.candidate();
        query.filter(cand.employee.manager.manager.eq((Employee)null).not());

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        MedicalInsurance.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "!(this.employee.manager.manager == null)",
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
        // DataNucleus: UnsupportedOperationException: Dont currently support operator NOT  in JDOQL conversion
        //executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
