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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.LongString;
import org.apache.jdo.tck.query.result.classes.MissingNoArgsConstructor;
import org.apache.jdo.tck.query.result.classes.NoFieldsNoMethods;
import org.apache.jdo.tck.query.result.classes.PublicLongField;
import org.apache.jdo.tck.query.result.classes.PublicPutMethod;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.ConversionHelper;

/**
 *<B>Title:</B> Result Class Requirements.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.12-1.
 *<BR>
 *<B>Assertion Description: </B>
 * The result class may be one of the java.lang classes ...
 */
public class ResultClassRequirements extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.12-1 (ResultClassRequirements) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        // Long
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "personid",
        /*INTO*/        Long.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // Double
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "salary",
        /*INTO*/        Double.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // BigDecimal
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "budget",
        /*INTO*/        BigDecimal.class, 
        /*FROM*/        Project.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // java.util.Date
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "hiredate",
        /*INTO*/        java.util.Date.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // Map
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "phoneNumbers",
        /*INTO*/        Map.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // user defined result class
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "personid AS l, lastname AS s",
        /*INTO*/        LongString.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // constructor
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "new LongString(personid, lastname)",
        /*INTO*/        null, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // public fields
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "personid AS l",
        /*INTO*/        PublicLongField.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // public put method
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "personid, lastname",
        /*INTO*/        PublicPutMethod.class, 
        /*FROM*/        FullTimeEmployee.class,
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
     * The array of invalid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] INVALID_QUERIES = {
        // TCK class, invalid property
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "personid, lastname",
        /*INTO*/        LongString.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // JDK class
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "personid, lastname",
        /*INTO*/        Long.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // JDK class, non assignment compatible
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "lastname",
        /*INTO*/        Long.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // TCK class, salary field is not assignment compatible
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "personid AS l, salary AS s",
        /*INTO*/        LongString.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // TCK class, non existing constructor
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "new LongString(personid)",
        /*INTO*/        null, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // TCK class, no no-args constructor
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "personid",
        /*INTO*/        MissingNoArgsConstructor.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // TCK class, no no-args constructor
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "personid",
        /*INTO*/        NoFieldsNoMethods.class, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
    };
        
    // Two dimensional arrays to be converted to maps 
    // in the expected result.
    private static Object[][] phoneNumbers1 = 
        {{"home", "1111"}, 
         {"work", "123456-1"}};
    private static Object[][] phoneNumbers2 = 
        {{"home", "2222"}, 
         {"work", "123456-2"}};
    private static Object[][] phoneNumbers5 = 
        {{"home", "3363"}, 
         {"work", "126456-3"}};
    private static Object[][] publicPutMethod1 =
        {{"personid", new Long(1)}, {"lastname", "emp1Last"}};
    private static Object[][] publicPutMethod2 =
        {{"personid", new Long(2)}, {"lastname", "emp2Last"}};
    private static Object[][] publicPutMethod5 =
        {{"personid", new Long(5)}, {"lastname", "emp5Last"}};
    
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        // Long
        Arrays.asList(new Object[]{new Long(1), new Long(2), new Long(5)}),
        // Double
        Arrays.asList(new Object[]{
                new Double(20000.0), new Double(10000.0), new Double(45000.0)}),
        // BigDecimal
        Arrays.asList(new Object[]{new BigDecimal("2500000.99"), 
                new BigDecimal("50000.00"), new BigDecimal("2000.99")}),
        // java.util.Date
        Arrays.asList(new Object[]{
                CompanyModelReader.stringToUtilDate("1/Jan/1999"), 
                CompanyModelReader.stringToUtilDate("1/Jul/2003"), 
                CompanyModelReader.stringToUtilDate("15/Aug/1998")}),
        // Map
        Arrays.asList(new Object[]{
                ConversionHelper.arrayToMap(phoneNumbers1),
                ConversionHelper.arrayToMap(phoneNumbers2),
                ConversionHelper.arrayToMap(phoneNumbers5)}),
        // user defined result class
        Arrays.asList(new Object[]{
                new LongString(1, "emp1Last"), 
                new LongString(2, "emp2Last"), 
                new LongString(5, "emp5Last")}),
        // constructor
        Arrays.asList(new Object[]{
                new LongString(1, "emp1Last"), 
                new LongString(2, "emp2Last"), 
                new LongString(5, "emp5Last")}),
        // public fields
        Arrays.asList(new Object[]{
                new PublicLongField(1), 
                new PublicLongField(2), 
                new PublicLongField(5)}),
        // public put method
        Arrays.asList(new Object[]{
                new PublicPutMethod(ConversionHelper.arrayToMap(publicPutMethod1)),
                new PublicPutMethod(ConversionHelper.arrayToMap(publicPutMethod2)),
                new PublicPutMethod(ConversionHelper.arrayToMap(publicPutMethod5))})
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ResultClassRequirements.class);
    }
    
    /** */
    public void testLong() {
        int index = 0;
        executeQuery(index);
    }

    /** */
    public void testDouble() {
        int index = 1;
        executeQuery(index);
    }

    /** */
    public void testBigDecimal() {
        int index = 2;
        executeQuery(index);
    }

    /** */
    public void testDate() {
        int index = 3;
        executeQuery(index);
    }

    /** */
    public void testMap() {
        int index = 4;
        executeQuery(index);
    }

    /** */
    public void testUserDefinedResultClass() {
        int index = 5;
        executeQuery(index);
    }

    /** */
    public void testConstructor() {
        int index = 6;
        executeQuery(index);
    }

    /** */
    public void testFields() {
        int index = 7;
        executeQuery(index);
    }

    /** */
    public void testPut() {
        int index = 8;
        executeQuery(index);
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
    private void executeQuery(int index) {
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadCompanyModel(getPM(), COMPANY_TESTDATA);
        addTearDownClass(CompanyModelReader.getTearDownClasses());
    }
}
