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
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Shape of Result.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.12-2.
 *<BR>
 *<B>Assertion Description: </B>
 * Table 6: Shape of Result (C is the candidate class)
 */
public class ShapeOfResult extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.12-2 (ShapeOfResult) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        // result: null
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
        /*FROM*/        null,
        /*TO*/          null),
        
        // result: this AS C
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "this AS Person",
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
        
        // result: null, unique: true
        new QueryElementHolder(
        /*UNIQUE*/      Boolean.TRUE,
        /*RESULT*/      null,
        /*INTO*/        null, 
        /*FROM*/        Person.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "personid == 1",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // result: this AS C, unique: true
        new QueryElementHolder(
        /*UNIQUE*/      Boolean.TRUE,
        /*RESULT*/      "this AS Person",
        /*INTO*/        null, 
        /*FROM*/        Person.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "personid == 1",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // result: expression of type T
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "firstname",
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
        
        // result: expression of type T, unique: true
        new QueryElementHolder(
        /*UNIQUE*/      Boolean.TRUE,
        /*RESULT*/      "firstname",
        /*INTO*/        null, 
        /*FROM*/        Person.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "personid == 1",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // result: multiple expressions of type T
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "firstname, lastname",
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
        
        // result: multiple expressions of type T, unique: true
        new QueryElementHolder(
        /*UNIQUE*/      Boolean.TRUE,
        /*RESULT*/      "firstname, lastname",
        /*INTO*/        null, 
        /*FROM*/        Person.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "personid == 1",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // result: multiple expressions of type T, result class
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "firstname, lastname",
        /*INTO*/        FullName.class, 
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
        
        // result: multiple expressions of type T, result class, unique: true
        new QueryElementHolder(
        /*UNIQUE*/      Boolean.TRUE,
        /*RESULT*/      "firstname, lastname",
        /*INTO*/        FullName.class, 
        /*FROM*/        Person.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "personid == 1",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
    };
    
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        // result: null
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp1", "emp2", "emp3", "emp4", "emp5"}),
        // result: this AS C
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp1", "emp2", "emp3", "emp4", "emp5"}),
        // result: null, unique: true
        getTransientCompanyModelInstance("emp1"),
        // result: this AS C, unique: true
        getTransientCompanyModelInstance("emp1"),
        // result: expression of type T
        Arrays.asList(new Object[]{"emp1First", "emp2First", 
                "emp3First", "emp4First", "emp5First"}),
        // result: expression of type T, unique: true
        "emp1First",
        // result: multiple expressions of type T
        Arrays.asList(new Object[]{
                new Object[]{"emp1First", "emp1Last"},
                new Object[]{"emp2First", "emp2Last"},
                new Object[]{"emp3First", "emp3Last"},
                new Object[]{"emp4First", "emp4Last"},
                new Object[]{"emp5First", "emp5Last"}}),
        // result: multiple expressions of type T, unique: true
        new Object[]{"emp1First", "emp1Last"},
        // result: multiple expressions of type T, result class
        Arrays.asList(new Object[]{
                new FullName("emp1First", "emp1Last"),
                new FullName("emp2First", "emp2Last"),
                new FullName("emp3First", "emp3Last"),
                new FullName("emp4First", "emp4Last"),
                new FullName("emp5First", "emp5Last")}),
        // result: multiple expressions of type T, result class, unique: true
        new FullName("emp1First", "emp1Last")
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ShapeOfResult.class);
    }
    
    /** */
    public void testNoResult() {
        int index = 0;
        execute(index, expectedResult[index]);
    }

    /** */
    public void testThisAsC() {
        int index = 1;
        execute(index, expectedResult[index]);
    }

    /** */
    public void testNoResultUnique() {
        int index = 2;
        execute(index, expectedResult[index]);
    }

    /** */
    public void testThisAsCUnique() {
        int index = 3;
        execute(index, expectedResult[index]);
    }

    /** */
    public void testSingleExpression() {
        int index = 4;
        execute(index, expectedResult[index]);
    }

    /** */
    public void testSingleExpressionUnique() {
        int index = 5;
        execute(index, expectedResult[index]);
    }

    /** */
    public void testMultipleExpressions() {
        int index = 6;
        execute(index, expectedResult[index]);
    }

    /** */
    public void testMultipleExpressionsUnique() {
        int index = 7;
        execute(index, expectedResult[index]);
    }

    /** */
    public void testMultipleExpressionsResultClass() {
        int index = 8;
        execute(index, expectedResult[index]);
    }

    /** */
    public void testMultipleExpressionResultClassUnique() {
        int index = 9;
        execute(index, expectedResult[index]);
    }

    /** */
    private void execute(int index, Object expected) {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], expected);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expected);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
