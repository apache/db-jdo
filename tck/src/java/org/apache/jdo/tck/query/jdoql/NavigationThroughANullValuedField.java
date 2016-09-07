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
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

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
    protected String getCompanyTestDataResource() {
        return NAVIGATION_TEST_COMPANY_TESTDATA;
    }

    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        // navigation through reference relationship field
        // the relationship medicalInsurance is not set for emp2 and emp3 =>
        // they should not be part of the result
        new QueryElementHolder(
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
        /*TO*/          null),

        // navigation through reference relationship field
        // emp5 and emp6 have have emp4 as manager
        new QueryElementHolder(
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
        /*TO*/          null),

        // multiple navigation through reference relationship field
        new QueryElementHolder(
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
        /*TO*/          null),

        // navigation through collection relationship field
        // employees emp2 and emp3 do not have a medicalInsurance, but emp1
        // matches the filter such that dept1 qualifies for inclusion in the 
        // result set.
        new QueryElementHolder(
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
        /*TO*/          null)
    };
        
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        // navigation through reference relationship field
        // the relationship medicalInsurance is not set for emp2 and emp3 =>
        // they should not be part of the result
        getTransientCompanyModelInstancesAsList(new String[]{"emp1"}),
        // navigation through reference relationship field
        // emp5 and emp6 have have emp4 as manager
        getTransientCompanyModelInstancesAsList(new String[]{"emp5", "emp6"}),
        // multiple navigation through reference relationship field
        getTransientCompanyModelInstancesAsList(new String[]{"emp2", "emp3", "emp10"}),
        // navigation through collection relationship field
        // employees emp2 and emp3 do not have a medicalInsurance, but emp1
        // matches the filter such that dept1 qualifies for inclusion in the 
        // result set.
        getTransientCompanyModelInstancesAsList(new String[]{"dept1"})
    };
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NavigationThroughANullValuedField.class);
    }
    
    /** */
    public void testPositive() {
        for (int i = 0; i < VALID_QUERIES.length; i++) {
            executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[i], 
                    expectedResult[i]);
            executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[i], 
                    expectedResult[i]);
        }
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
