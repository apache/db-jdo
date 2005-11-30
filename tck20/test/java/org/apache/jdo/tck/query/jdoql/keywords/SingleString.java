/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.apache.jdo.tck.query.jdoql.keywords;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Single string query.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.13-1.
 *<BR>
 *<B>Assertion Description: </B>
 * The String version of Query represents all query elements 
 * using a single string. The string contains the following structure:
 */
public class SingleString extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.13-1 (SingleString) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        new QueryElementHolder(
        /*UNIQUE*/      Boolean.FALSE,
        /*RESULT*/      "firstname AS firstName, lastname AS lastName",
        /*INTO*/        FullName.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     Boolean.TRUE,
        /*WHERE*/       "salary > 1000 & projects.contains(project) & " +
                        "project.budget > limit",
        /*VARIABLES*/   "Project project",
        /*PARAMETERS*/  "BigDecimal limit",
        /*IMPORTS*/     "import org.apache.jdo.tck.pc.company.Project; " +
                        "import java.math.BigDecimal",
        /*GROUP BY*/    "firstname, lastname HAVING lastname.startsWith('emp')",
        /*ORDER BY*/    "personid ASCENDING",
        /*FROM*/        0,
        /*TO*/          3)
    };
    
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        Arrays.asList(new Object[]{
                new FullName("emp1First", "emp1Last"), 
                new FullName("emp2First", "emp2Last"),
                new FullName("emp5First", "emp5Last")})
    };
            
    /** Parameters of valid queries. */
    private static Object[][] parameters = {
        {new BigDecimal("2000")}
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SingleString.class);
    }
    
    /** */
    public void testPositive() {
        for (int i = 0; i < VALID_QUERIES.length; i++) {
            executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[i], 
                    parameters[i], expectedResult[i]);
            executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[i], 
                    parameters[i], expectedResult[i]);
        }
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadCompanyModel(getPM(), COMPANY_TESTDATA);
        addTearDownClass(CompanyModelReader.getTearDownClasses());
    }
}
