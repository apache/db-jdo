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
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

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
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        // 0: simple manager check being nill
        new QueryElementHolder(
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
        /*TO*/          null),
        
        // 1: simple manager check being not null
        new QueryElementHolder(
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
        /*TO*/          null),
        
        // 2: simple manager check being not null using not operator
        new QueryElementHolder(
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
        /*TO*/          null),
        
        // 3: manager's manager check
        new QueryElementHolder(
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
        /*TO*/          null),
        
        // 4: manager's manager check with extra check on first level manager
        new QueryElementHolder(
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
        /*TO*/          null),
        
        // 5 : manager's manager check not being null
        new QueryElementHolder(
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
        /*TO*/          null),
        
        // 6 : manager's manager check not being null using not operator
        new QueryElementHolder(
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
        /*TO*/          null),
        
        // 7 : multiple relationships 
        new QueryElementHolder(
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
        /*TO*/          null),

        // 8 : multiple relationships 
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null,
        /*INTO*/        null,
        /*FROM*/        MedicalInsurance.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "this.employee != null && this.employee.manager != null && this.employee.manager.manager == null",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // 9 : multiple relationships 
        new QueryElementHolder(
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
        /*TO*/          null),
        
        // 10 : multiple relationships 
        new QueryElementHolder(
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
        /*TO*/          null)
    };

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
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        // 0 : this.manager == null
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp0", "emp4", "emp7"}),
        // 1 : this.manager != null
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp1", "emp2", "emp3", "emp5", "emp6", "emp8", "emp9", "emp10"}),
        // 2 : !(this.manager == null)
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp1", "emp2", "emp3", "emp5", "emp6", "emp8", "emp9", "emp10"}),
        
        // 3 : this.manager.manager == null
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp0", "emp1", "emp4", "emp5", "emp6", "emp7", "emp8", "emp9"}),
        // 4: this.manager != null && this.manager.manager == null
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp1", "emp5", "emp6", "emp8", "emp9"}),
        // 5 : this.manager.manager != null
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp2", "emp3", "emp10"}),
        // 6 : !(this.manager.manager == null)
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp2", "emp3", "emp10"}),

        // 7 : this.employee.manager.manager == null
        getTransientCompanyModelInstancesAsList(new String[]{
                "medicalIns1", "medicalIns4", "medicalIns5", "medicalIns98"}),
        // 8 : this.employee != null && this.employee.manager != null && this.employee.manager.manager == null
        getTransientCompanyModelInstancesAsList(new String[]{
                "medicalIns1",  "medicalIns5"}),
        // 9 : this.employee.manager.manager != null
        getTransientCompanyModelInstancesAsList(new String[]{
                "medicalIns2", "medicalIns3"}),
        // 10 : !(this.employee.manager.manager == null)
        getTransientCompanyModelInstancesAsList(new String[]{
                "medicalIns2", "medicalIns3"})
    };
    
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
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[0], 
                        expectedResult[0]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[0], 
                                 expectedResult[0]);
    }

    /**
     * this.manager != null
     */
    public void testPositive1() {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[1], 
                        expectedResult[1]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[1], 
                                 expectedResult[1]);
    }

    /**
     * !(this.manager == null)
     */
    public void testPositive2() {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[2], 
                        expectedResult[2]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[2], 
                                 expectedResult[2]);
    }

    /**
     * this.manager.manager == null
     * Disabled, because it currently fails on the RI.
     */
    public void testPositive3() {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[3], 
                        expectedResult[3]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[3], 
                                 expectedResult[3]);
    }
    
    /**
     * this.manager != null && this.manager.manager == null
     */
    public void testPositive4() {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[4], 
                        expectedResult[4]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[4], 
                                 expectedResult[4]);
    }

    /**
     * this.manager.manager != null
     */
    public void testPositive5() {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[5], 
                        expectedResult[5]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[5], 
                                 expectedResult[5]);
    }

    /**
     * !(this.manager.manager == null)
     */
    public void testPositive6() {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[6], 
                        expectedResult[6]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[6], 
                                 expectedResult[6]);
    }

    /**
     * this.employee.manager.manager == null
     * Disabled, because it currently fails on the RI.
     */
    public void testPositive7() {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[7], 
                        expectedResult[7]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[7], 
                                 expectedResult[7]);
    }

    /**
     * this.employee != null && this.employee.manager != null && this.employee.manager.manager == null
     */
    public void testPositive8() {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[8], 
                        expectedResult[8]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[8], 
                                 expectedResult[8]);
    }

    /**
     * this.employee.manager.manager != null
     */
    public void testPositive9() {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[9], 
                        expectedResult[9]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[9], 
                                 expectedResult[9]);
    }

    /**
     * !(this.employee.manager.manager == null)
     */
    public void testPositive10() {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[10], 
                        expectedResult[10]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[10], 
                                 expectedResult[10]);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
