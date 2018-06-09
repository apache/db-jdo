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
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Use of If Else expression in filter
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.x
 *<BR>
 *<B>Assertion Description: </B>
 */

public class IfElseInFilter extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.x (IfElseInFilter) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        // simple If/Else using literals
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null,
        /*INTO*/        null, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "this.salary > (IF (this.department.name == 'Development') 15000 ELSE 25000)",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    "this.personid",
        /*FROM*/        null,
        /*TO*/          null),
        // simple If/Else using relationships
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null,
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "(IF (this.manager == null) this.mentor.department.deptid ELSE this.manager.department.deptid) == this.department.deptid",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    "this.personid",
        /*FROM*/        null,
        /*TO*/          null),
        // multiple If/Else with distinct conditions
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null,
        /*INTO*/        null, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "(IF (0.0 <= this.salary && this.salary < 10000.1) 1 ELSE IF (10000.1 <= this.salary && this.salary < 20000.1) 2 ELSE IF (20000.1 <= this.salary && this.salary < 30000.1) 3 ELSE 4) == 2",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    "this.personid",
        /*FROM*/        null,
        /*TO*/          null),
        // multiple If/Else with overlapping conditions
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null,
        /*INTO*/        null, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "(IF (this.salary < 10000.1) 1 ELSE IF (this.salary < 20000.1) 2 ELSE IF (this.salary < 30000.1) 3 ELSE 4) == 2",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    "this.personid",
        /*FROM*/        null,
        /*TO*/          null)
    };

   /** 
     * The array of invalid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] INVALID_QUERIES = {
        // Invalid type of condition expression 
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null,
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "(IF (this.firstname) 0 ELSE 1) == 0",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        // missing ELSE
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null,
        /*INTO*/        null, 
        /*FROM*/        DentalInsurance.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "(IF (this.employee == null) 15000) == 15000",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        // type of THEN expr must be the same as type of ELSE expr
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null,
        /*INTO*/        null, 
        /*FROM*/        DentalInsurance.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "(IF (this.employee == null) 'Michael' ELSE this.employee) == 'Michael'",
        /*VARIABLES*/   null,
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
        getTransientCompanyModelInstancesAsList(new String[]{"emp1", "emp5"}),
        getTransientCompanyModelInstancesAsList(new String[]{"emp1", "emp2", "emp3"}),
        getTransientCompanyModelInstancesAsList(new String[]{"emp1"}),
        getTransientCompanyModelInstancesAsList(new String[]{"emp1"})
    };
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(IfElseInFilter.class);
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

    /** */
    public void testNegative() {
        for (int i = 0; i < INVALID_QUERIES.length; i++) {
            compileAPIQuery(ASSERTION_FAILED, INVALID_QUERIES[i], false);
            compileSingleStringQuery(ASSERTION_FAILED, INVALID_QUERIES[i], 
                    false);
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
