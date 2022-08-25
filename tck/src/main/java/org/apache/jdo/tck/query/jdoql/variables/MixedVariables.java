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

package org.apache.jdo.tck.query.jdoql.variables;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Mixed Variables.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.5-3.
 *<BR>
 *<B>Assertion Description: </B>
 * All variables must be explicitly declared, 
 * or all variables must be implicitly declared.
 */
public class MixedVariables extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.5-3 (MixedVariables) failed: ";
    
    /** 
     * The array of invalid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder<?>[] INVALID_QUERIES = {
            new QueryElementHolder<>(
                    /*UNIQUE*/      null,
                    /*RESULT*/      null,
                    /*INTO*/        null,
                    /*FROM*/        Employee.class,
                    /*EXCLUDE*/     null,
                    /*WHERE*/       "(team.contains(e) & e.firstname == 'emp1First') & " +
                    "(projects.contains(p) & p.name == 'orange')",
                    /*VARIABLES*/   "Employee e",
                    /*PARAMETERS*/  null,
                    /*IMPORTS*/     null,
                    /*GROUP BY*/    null,
                    /*ORDER BY*/    null,
                    /*FROM*/        null,
                    /*TO*/          null)
    };
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder<?>[] VALID_QUERIES = {
            new QueryElementHolder<>(
                    /*UNIQUE*/      null,
                    /*RESULT*/      null,
                    /*INTO*/        null,
                    /*FROM*/        Employee.class,
                    /*EXCLUDE*/     null,
                    /*WHERE*/       "(team.contains(e) & e.firstname == 'emp1First') & " +
                    "(projects.contains(p) & p.name == 'orange')",
                    /*VARIABLES*/   "Employee e; Project p",
                    /*PARAMETERS*/  null,
                    /*IMPORTS*/     null,
                    /*GROUP BY*/    null,
                    /*ORDER BY*/    null,
                    /*FROM*/        null,
                    /*TO*/          null),
            new QueryElementHolder<>(
                    /*UNIQUE*/      null,
                    /*RESULT*/      null,
                    /*INTO*/        null,
                    /*FROM*/        Employee.class,
                    /*EXCLUDE*/     null,
                    /*WHERE*/       "(team.contains(e) & e.firstname == 'emp1First') & " +
                    "(projects.contains(p) & p.name == 'orange')",
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
    private final Object[] expectedResult = {
        getTransientCompanyModelInstancesAsList(Employee.class, "emp2"),
        getTransientCompanyModelInstancesAsList(Employee.class, "emp2")
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MixedVariables.class);
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

    public void testNegative() {
        for (QueryElementHolder<?> invalidQuery : INVALID_QUERIES) {
            compileAPIQuery(ASSERTION_FAILED, invalidQuery, false);
            compileSingleStringQuery(ASSERTION_FAILED, invalidQuery,
                    false);
        }
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
