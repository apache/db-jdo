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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.company.QFullTimeEmployee;
import org.apache.jdo.tck.pc.company.QProject;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.LongString;
import org.apache.jdo.tck.query.result.classes.MissingNoArgsConstructor;
import org.apache.jdo.tck.query.result.classes.NoFieldsNoMethods;
import org.apache.jdo.tck.query.result.classes.PublicLongField;
import org.apache.jdo.tck.query.result.classes.PublicPutMethod;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.ConversionHelper;

import javax.jdo.JDOQLTypedQuery;

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
     * The array of invalid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] INVALID_QUERIES = {
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
        /*RESULT*/      "this",
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
        /*IMPORTS*/     "import org.apache.jdo.tck.query.result.classes.LongString;",
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
    private static Object[][] emp1Map = 
        {{"id", new Long(1)}, 
         {"name", "emp1Last"}};
    private static Object[][] emp2Map = 
        {{"id", new Long(2)}, 
         {"name", "emp2Last"}};
    private static Object[][] emp5Map = 
        {{"id", new Long(5)}, 
         {"name", "emp5Last"}};
    private static Object[][] publicPutMethod1 =
        {{"personid", new Long(1)}, {"lastname", "emp1Last"}};
    private static Object[][] publicPutMethod2 =
        {{"personid", new Long(2)}, {"lastname", "emp2Last"}};
    private static Object[][] publicPutMethod5 =
        {{"personid", new Long(5)}, {"lastname", "emp5Last"}};
    
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
        Object expected = Arrays.asList(new Long(1), new Long(2), new Long(5));

        JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
        QFullTimeEmployee cand = QFullTimeEmployee.candidate();
        query.result(false, cand.personid);

        QueryElementHolder holder = new QueryElementHolder(
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, Long.class, expected);
    }

    /** */
    public void testDouble() {
        Object expected = Arrays.asList(new Double(20000.0), new Double(10000.0), new Double(45000.0));

        JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
        QFullTimeEmployee cand = QFullTimeEmployee.candidate();
        query.result(false, cand.salary);

        QueryElementHolder holder = new QueryElementHolder(
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, Double.class, expected);
    }

    /** */
    public void testBigDecimal() {
        Object expected = Arrays.asList(
                new BigDecimal("2500000.99"), new BigDecimal("50000.00"), new BigDecimal("2000.99"));

        JDOQLTypedQuery<Project> query = getPM().newJDOQLTypedQuery(Project.class);
        QProject cand = QProject.candidate();
        query.result(false, cand.budget);

        QueryElementHolder holder = new QueryElementHolder(
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, BigDecimal.class, expected);
    }

    /** */
    public void testDate() {
        Object expected = Arrays.asList(
                CompanyModelReader.stringToUtilDate("1/Jan/1999"),
                CompanyModelReader.stringToUtilDate("1/Jul/2003"),
                CompanyModelReader.stringToUtilDate("15/Aug/1998"));

        JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
        QFullTimeEmployee cand = QFullTimeEmployee.candidate();
        query.result(false, cand.hiredate);

        QueryElementHolder holder = new QueryElementHolder(
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, java.util.Date.class, expected);
    }

    /** */
    public void testMap() {
        Object expected = Arrays.asList(
                ConversionHelper.arrayToMap(emp1Map),
                ConversionHelper.arrayToMap(emp2Map),
                ConversionHelper.arrayToMap(emp5Map));

        JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
        QFullTimeEmployee cand = QFullTimeEmployee.candidate();
        // JDOQLTypedQuery API: Map Result
        query.result(false, cand.personid, cand.lastname);

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "personid AS id, lastname AS name",
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, Map.class, expected);
    }

    /** */
    public void testUserDefinedResultClass() {
        Object expected = Arrays.asList(
                new LongString(1, "emp1Last"),
                new LongString(2, "emp2Last"),
                new LongString(5, "emp5Last"));

        JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
        QFullTimeEmployee cand = QFullTimeEmployee.candidate();
        // JDOQLTypedQuery API: user defined class
        query.result(false, cand.personid, cand.lastname);

        QueryElementHolder holder = new QueryElementHolder(
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, LongString.class, expected);
    }

    /** */
    public void testConstructor() {
        Object expected = Arrays.asList(new Object[]{
                new LongString(1, "emp1Last"),
                new LongString(2, "emp2Last"),
                new LongString(5, "emp5Last")});

        JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
        QFullTimeEmployee cand = QFullTimeEmployee.candidate();
        // JDOQLTypedQuery API: constructor
        query.result(false, cand.personid, cand.lastname);

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "new LongString(personid, lastname)",
                /*INTO*/        null,
                /*FROM*/        FullTimeEmployee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       null,
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     "import org.apache.jdo.tck.query.result.classes.LongString;",
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, LongString.class, expected);
    }

    /** */
    public void testConstructorWithoutConstructorCall() {
        Object expected = Arrays.asList(new Object[]{
                new LongString(1, "emp1Last"),
                new LongString(2, "emp2Last"),
                new LongString(5, "emp5Last")});

        JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
        QFullTimeEmployee cand = QFullTimeEmployee.candidate();
        // JDOQLTypedQuery API: constructor
        query.result(false, cand.personid, cand.lastname);

        QueryElementHolder holder = new QueryElementHolder(
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, LongString.class, expected);
    }

    /** */
    public void testFields() {
        Object expected = Arrays.asList(new Object[]{
                new PublicLongField(1),
                new PublicLongField(2),
                new PublicLongField(5)});

        JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
        QFullTimeEmployee cand = QFullTimeEmployee.candidate();
        // JDOQLTypedQuery API:
        query.result(false, cand.personid);

        QueryElementHolder holder = new QueryElementHolder(
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, PublicLongField.class, expected);
    }

    /** */
    public void testPut() {
        Object expected = Arrays.asList(new Object[]{
                new PublicPutMethod(ConversionHelper.arrayToMap(publicPutMethod1)),
                new PublicPutMethod(ConversionHelper.arrayToMap(publicPutMethod2)),
                new PublicPutMethod(ConversionHelper.arrayToMap(publicPutMethod5))});

        JDOQLTypedQuery<FullTimeEmployee> query = getPM().newJDOQLTypedQuery(FullTimeEmployee.class);
        QFullTimeEmployee cand = QFullTimeEmployee.candidate();
        // JDOQLTypedQuery API: constructor
        query.result(false, cand.personid, cand.lastname);

        QueryElementHolder holder = new QueryElementHolder(
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, PublicPutMethod.class, expected);
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
