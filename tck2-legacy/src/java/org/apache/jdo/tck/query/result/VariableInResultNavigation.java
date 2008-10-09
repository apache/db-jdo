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

package org.apache.jdo.tck.query.result;

import java.util.Arrays;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Variable in Result.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.9-3.
 *<BR>
 *<B>Assertion Description: </B>
 * If a variable or a field of a variable is included in the result, 
 * either directly or via navigation through the variable, 
 * then the semantics of the contains clause that include the variable change. 
 * In this case, all values of the variable 
 * that satisfy the filter are included in the result.
 * Result expressions begin with either an instance of the candidate class 
 * (with an explicit or implicit "this") or an instance of a variable 
 * (using the variable name). The candidate tuples are the cartesian product 
 * of the candidate class and all variables used in the result. The result 
 * tuples are the tuples of the candidate class and all variables used 
 * in the result that satisfy the filter. 
 * The result is the collection of result expressions projected from the 
 * result tuples. 
 * 
 * This test differs from VariableInResult by extending the navigation
 * of variables. It navigates from the candidate Department class to include
 * fields in the corresponding Company. It navigates from the candidate
 * Company class to Department, Employee, and Project.
 */
public class VariableInResultNavigation extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.9-3 (VariableInResult) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "e, p",
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "employees.contains(e) && e.projects.contains(p) && p.name == 'orange'",
        /*VARIABLES*/   "Employee e; Project p",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "e, p",
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "employees.contains(e) && e.projects.contains(p)",
        /*VARIABLES*/   "Employee e; Project p",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "this, e, p",
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "employees.contains(e) && e.projects.contains(p)",
        /*VARIABLES*/   "Employee e; Project p",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "this, e, p",
        /*INTO*/        null, 
        /*FROM*/        Company.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "name == \"Sun Microsystems, Inc.\" && departments.contains(d) && d.employees.contains(e) && e.projects.contains(p)",
        /*VARIABLES*/   "Department d; Employee e; Project p",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "e, p",
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "deptid == 1 && employees.contains(e) && e.projects.contains(p)",
        /*VARIABLES*/   "Employee e; Project p",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "e, p",
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "company.name == \"Sun Microsystems, Inc.\" && employees.contains(e) && e.projects.contains(p)",
        /*VARIABLES*/   "Employee e; Project p",
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
    private Object emp1 = getTransientCompanyModelInstance("emp1");
    private Object emp2 = getTransientCompanyModelInstance("emp2");
    private Object emp3 = getTransientCompanyModelInstance("emp3");
    private Object emp4 = getTransientCompanyModelInstance("emp4");
    private Object emp5 = getTransientCompanyModelInstance("emp5");
    private Object proj1 = getTransientCompanyModelInstance("proj1");
    private Object proj2 = getTransientCompanyModelInstance("proj2");
    private Object proj3 = getTransientCompanyModelInstance("proj3");
    private Object dept1 = getTransientCompanyModelInstance("dept1");
    private Object dept2 = getTransientCompanyModelInstance("dept2");

    private Object[] expectedResult = {
        Arrays.asList(new Object[] {
            new Object[] {emp1, proj1},
            new Object[] {emp2, proj1},
            new Object[] {emp3, proj1}}),
        Arrays.asList(new Object[] {
            new Object[] {emp1, proj1},
            new Object[] {emp2, proj1},
            new Object[] {emp3, proj1},
            new Object[] {emp2, proj2},
            new Object[] {emp3, proj2},
            new Object[] {emp4, proj3},
            new Object[] {emp5, proj3}}),
        Arrays.asList(new Object[] {
            new Object[] {dept1, emp1, proj1},
            new Object[] {dept1, emp2, proj1},
            new Object[] {dept1, emp3, proj1},
            new Object[] {dept1, emp2, proj2},
            new Object[] {dept1, emp3, proj2},
            new Object[] {dept2, emp4, proj3},
            new Object[] {dept2, emp5, proj3}}),
        Arrays.asList(new Object[] {
            new Object[] {dept1, emp1, proj1},
            new Object[] {dept1, emp2, proj1},
            new Object[] {dept1, emp3, proj1},
            new Object[] {dept1, emp2, proj2},
            new Object[] {dept1, emp3, proj2},
            new Object[] {dept2, emp4, proj3},
            new Object[] {dept2, emp5, proj3}}),
        Arrays.asList(new Object[] {
            new Object[] {emp1, proj1},
            new Object[] {emp2, proj1},
            new Object[] {emp3, proj1},
            new Object[] {emp2, proj2},
            new Object[] {emp3, proj2}}),
        Arrays.asList(new Object[] {
            new Object[] {emp1, proj1},
            new Object[] {emp2, proj1},
            new Object[] {emp3, proj1},
            new Object[] {emp2, proj2},
            new Object[] {emp3, proj2},
            new Object[] {emp4, proj3},
            new Object[] {emp5, proj3}})
    };

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(VariableInResult.class);
    }
    
    /** */
    public void testNavigationWithConstraint() {
        int index = 0;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /** */
    public void testNavigationWithoutConstraint() {
        int index = 1;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /** */
    public void testNavigationWithThis() {
        int index = 2;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /** */
    public void testNavigationWithThisConstraint() {
        int index = 4;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /** */
    public void testNavigationWithCompanyConstraint() {
        int index = 5;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }
    /**
     * @see QueryTest#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
