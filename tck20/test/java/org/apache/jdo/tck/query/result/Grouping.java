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

package org.apache.jdo.tck.query.result;

import java.util.Arrays;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Grouping.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.10-1.
 *<BR>
 *<B>Assertion Description: </B>
 * When grouping is specified, each result expression must be one of: 
 * an expression contained in the grouping expression; 
 * or, an aggregate expression evaluated once per group. 
 * The query groups all elements where all expressions 
 * specified in setGrouping have the same values. 
 * The query result consists of one element per group.
 */
public class Grouping extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.10-1 (Grouping) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "department, SUM(salary)",
        /*INTO*/        null, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    "department",
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null)
    };
    
    /** 
     * The array of invalid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] INVALID_QUERIES = {
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "department, salary",
        /*INTO*/        null, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    "department",
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null)
    };
    
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        Arrays.asList(new Object[] {
            new Object[] {getTransientCompanyModelInstance("dept1"), new Double(30000.0)},
            new Object[] {getTransientCompanyModelInstance("dept2"), new Double(45000.0)}})
    };
        
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(Grouping.class);
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
