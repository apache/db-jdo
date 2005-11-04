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

package org.apache.jdo.tck.query.jdoql.parameters;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Implicit parameters.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.3-3.
 *<BR>
 *<B>Assertion Description: </B>
 * Parameters implicitly declared (in the result, filter, grouping, ordering, 
 * or range) are identified by prepending a ":" to the parameter 
 * everywhere it appears. All parameter types can be determined 
 * by one of the following techniques:
 */
public class ImplicitParameters extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.3-3 (ImplicitParameters) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "this, :param", 
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
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null, 
        /*INTO*/        null, 
        /*FROM*/        Person.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "firstname == :param",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "department.name", 
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    "department.name HAVING COUNT(this) >= :min",
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null, 
        /*INTO*/        null, 
        /*FROM*/        Person.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        ":zero",
        /*TO*/          ":five")
    };
    
    /** The expected results of valid queries. */
    private static Object[][] expectedResult = {
        {"emp1", "emp2", "emp3", "emp4", "emp5"},
        {"emp1"},
        {"Development"}, /* Note: this is not a bean name! */
        {"emp1", "emp2", "emp3", "emp4", "emp5"}
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ImplicitParameters.class);
    }
    
    /** */
    public void testResult() {
        int index = 0;
        Object[] pcInstances = 
            getCompanyModelInstances(toStringArray(expectedResult[index]));
        Object[] expectedResultValues = new Object[pcInstances.length];
        String parameter = "parameterInResult";
        for (int i = 0; i < expectedResultValues.length; i++) {
            expectedResultValues[i] = new Object[] {pcInstances[i], parameter};
        }
        executeQuery(index, new Object[] {parameter}, expectedResultValues);
    }
    
    /** */
    public void testFilter() {
        int index = 1;
        Object[] expectedResultValues = 
            getCompanyModelInstances(toStringArray(expectedResult[index]));
        executeQuery(index, new Object[] {"emp1First"}, expectedResultValues);
    }
    
    /** */
    public void testGrouping() {
        int index = 2;
        executeQuery(index, new Object[] {new Long(3)}, expectedResult[index]);
    }
    
    /** */
    public void testRange() {
        int index = 3;
        Object[] expectedResultValues = 
            getCompanyModelInstances(toStringArray(expectedResult[index]));
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                new Object[] {new Long(0), new Long(5)}, expectedResultValues);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadCompanyModel(getPM(), COMPANY_TESTDATA);
        addTearDownClass(CompanyModelReader.getTearDownClasses());
    }

    /** */
    private void executeQuery(int index, Object[] parameters, Object[] expectedResultValues) {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                parameters, expectedResultValues);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                parameters, expectedResultValues);
    }
    
    /** */
    private String[] toStringArray(Object[] array) {
        String[] result = new String[array.length];
        System.arraycopy(array, 0, result, 0, result.length);
        return result;
    }
}
