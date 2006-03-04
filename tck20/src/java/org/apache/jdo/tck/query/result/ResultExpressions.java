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
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Result Expressions.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.9-5.
 *<BR>
 *<B>Assertion Description: </B>
 * The result expressions include: ... 
 * The result expression can be explicitly cast using the (cast) operator.
 */
public class ResultExpressions extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.9-5 (ResultExpressions) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        // this
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "this",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // field
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "personid",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // variable.field
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "p.projid",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "projects.contains(p) && personid == 1",
        /*VARIABLES*/   "Project p",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // variable
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "p",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "projects.contains(p) && personid == 1",
        /*VARIABLES*/   "Project p",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // COUNT(this)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "COUNT(this)",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // COUNT(variable)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "COUNT(p)",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "projects.contains(p) && personid == 1",
        /*VARIABLES*/   "Project p",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // SUM
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "SUM(personid)",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // MIN
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MIN(personid)",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // MAX
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MAX(personid)",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // AVG
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "AVG(personid)",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // field expression
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "personid + 1",
        /*INTO*/        null, 
        /*FROM*/        Person.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // navigational expression this
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "this.personid",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),

        // navigational expression variable
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "p.projid",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "projects.contains(p) && personid == 1",
        /*VARIABLES*/   "Project p",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),

        // navigational expression parameter
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "p.projid",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "personid == 1",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  "Project p",
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // navigational expression field
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "department.deptid",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "personid == 1",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // parameter
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "p",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "personid == 1",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  "Project p",
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // cast
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "(FullTimeEmployee)manager",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "personid == 1",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null)
    };
    
    /** 
     * The array of invalid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] INVALID_QUERIES = {
        // unknown field x
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "x",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        // field salary is declared in a subclass of the candidate class
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "salary",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        // project collection field
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "employees",
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        // project map field
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "phoneNumbers",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
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
        // this
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp1", "emp2", "emp3", "emp4", "emp5"}),
        // field
        Arrays.asList(new Object[]{new Long(1), new Long(2), 
                new Long(3), new Long(4), new Long(5)}),
        // variable.field
        Arrays.asList(new Object[]{new Long(1)}),
        // variable
        getTransientCompanyModelInstancesAsList(new String[]{"proj1"}),
        // COUNT(this)
        new Long(5),
        // COUNT(variable)
        new Long(1),
        // SUM
        new Long(1+2+3+4+5),
        // MIN
        new Long(1),
        // MAX
        new Long(5),
        // AVG
        new Long(3),
        // field expression
        Arrays.asList(new Object[]{new Long(2), new Long(3), 
                new Long(4), new Long(5), new Long(6)}),
        // navigational expression this
        Arrays.asList(new Object[]{new Long(1), new Long(2), 
                new Long(3), new Long(4), new Long(5)}),
        // navigational expression variable
        Arrays.asList(new Object[]{new Long(1)}),
        // navigational expression parameter
        Arrays.asList(new Object[]{new Long(1)}),
        // navigational expression field
        Arrays.asList(new Object[]{new Long(1)}),
        // parameter
        getTransientCompanyModelInstancesAsList(new String[]{"proj1"}),
        // cast
        getTransientCompanyModelInstancesAsList(new String[]{"emp2"})
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ResultExpressions.class);
    }
    
    /** */
    public void testThis() {
        int index = 0;
        executeQuery(index, null);
    }

    /** */
    public void testField() {
        int index = 1;
        executeQuery(index, null);
    }

    /** */
    public void testVariableField() {
        int index = 2;
        executeQuery(index, null);
    }

    /** */
    public void testVariable() {
        int index = 3;
        executeQuery(index, null);
    }

    /** */
    public void testCountThis() {
        int index = 4;
        executeQuery(index, null);
    }

    /** */
    public void testCountVariable() {
        int index = 5;
        executeQuery(index, null);
    }

    /** */
    public void testSum() {
        int index = 6;
        executeQuery(index, null);
    }

    /** */
    public void testMin() {
        int index = 7;
        executeQuery(index, null);
    }

    /** */
    public void testMax() {
        int index = 8;
        executeQuery(index, null);
    }

    /** */
    public void testAvg() {
        int index = 9;
        executeQuery(index, null);
    }

    /** */
    public void testFieldExpression() {
        int index = 10;
        executeQuery(index, null);
    }

    /** */
    public void testNavigationalExpressionThis() {
        int index = 11;
        executeQuery(index, null);
    }

    /** */
    public void testNavigationalExpressionVariable() {
        int index = 12;
        executeQuery(index, null);
    }

    /** */
    public void testNavigationalExpressionParameter() {
        int index = 13;
        Object[] parameters = getPersistentCompanyModelInstances(new String[]{"proj1"});
        executeQuery(index, parameters);
    }

    /** */
    public void testNavigationalExpressionField() {
        int index = 14;
        executeQuery(index, null);
    }

    /** */
    public void testParameter() {
        int index = 15;
        Object[] parameters = getPersistentCompanyModelInstances(new String[]{"proj1"});
        executeQuery(index, parameters);
    }

    /** */
    public void testCast() {
        int index = 16;
        executeQuery(index, null);
    }

    /** */
    public void testNegative() {
        for (int i = 0; i < INVALID_QUERIES.length; i++) {
            compileAPIQuery(ASSERTION_FAILED, INVALID_QUERIES[i], false);
            compileSingleStringQuery(ASSERTION_FAILED, INVALID_QUERIES[i], 
                    false);
        }
    }

    /** */
    private void executeQuery(int index, Object[] parameters) {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                parameters, expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                parameters, expectedResult[index]);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
