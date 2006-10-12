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
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
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
 */
public class VariableInResult extends QueryTest {

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
        /*RESULT*/      "distinct e",
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "employees.contains(e)",
        /*VARIABLES*/   "Employee e",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "distinct p.projid, p.name",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "projects.contains(p) & p.name == 'orange'",
        /*VARIABLES*/   "Project p",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "p.projid, p.name",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "projects.contains(p) & p.name == 'orange'",
        /*VARIABLES*/   "Project p",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "e",
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "employees.contains(e)",
        /*VARIABLES*/   "Employee e",
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
        /*WHERE*/       "employees.contains(e) && e.projects.contains(p)" +
                "&& p.name == 'orange'",
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
        // SELECT e FROM Department WHERE deptid==2 & employees.contains(e) VARIABLES Employee e 
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "e",
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "deptid == 2 & employees.contains(e)",
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
    private Object emp1 = getTransientCompanyModelInstance("emp1");
    private Object emp2 = getTransientCompanyModelInstance("emp2");
    private Object emp3 = getTransientCompanyModelInstance("emp3");
    private Object emp4 = getTransientCompanyModelInstance("emp4");
    private Object emp5 = getTransientCompanyModelInstance("emp5");
    private Object proj1 = getTransientCompanyModelInstance("proj1");
    private Object proj2 = getTransientCompanyModelInstance("proj2");
    private Object proj3 = getTransientCompanyModelInstance("proj3");

    private Object[] expectedResult = {
        getTransientCompanyModelInstancesAsList(
                new String[]{"emp1","emp2","emp3","emp4","emp5"}),
        // Note: "orange" is not a bean name!
        Arrays.asList(new Object[]{new Object[]{new Long(1), "orange"}}),
        Arrays.asList(new Object[]{
            new Object[]{new Long(1), "orange"}, 
            new Object[]{new Long(1), "orange"},
            new Object[]{new Long(1), "orange"}}),
        getTransientCompanyModelInstancesAsList(
                new String[]{"emp1","emp2","emp3","emp4","emp5"}),
        new Object[] {
                    new Object[] {emp1, proj1},
                    new Object[] {emp2, proj1},
                    new Object[] {emp3, proj1}
        },
        new Object[] {
                    new Object[] {emp1, proj1},
                    new Object[] {emp2, proj1},
                    new Object[] {emp3, proj1},
                    new Object[] {emp2, proj2},
                    new Object[] {emp3, proj2},
                    new Object[] {emp4, proj3},
                    new Object[] {emp5, proj3}
        },
        getTransientCompanyModelInstancesAsList(
                new String[]{"emp4","emp5"})
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
    public void testDistinctNoNavigation() {
        int index = 0;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /** */
    public void testDistinctNavigation() {
        int index = 1;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /** */
    public void testNavigation() {
        int index = 2;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /** */
    public void testNoNavigation() {
        int index = 3;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /** */
    public void testMultipleProjectionWithConstraints() {
        int index = 4;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /** */
    public void testMultipleProjection() {
        int index = 5;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /** */
    public void testProjectionWithConstraints() {
        int index = 6;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
