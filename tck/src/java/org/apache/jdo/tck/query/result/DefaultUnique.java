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

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Default Unique.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.11-2.
 *<BR>
 *<B>Assertion Description: </B>
 * The default Unique setting is true for aggregate results 
 * without a grouping expression, and false otherwise.
 */
public class DefaultUnique extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.11-2 (DefaultUnique) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null, 
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

        // aggregate queries

        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "COUNT(department)", 
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
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "avg(lifetimeOrthoBenefit)", 
        /*INTO*/        null, 
        /*FROM*/        DentalInsurance.class,
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
        /*RESULT*/      "MIN(budget)", 
        /*INTO*/        null, 
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
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "max (budget)", 
        /*INTO*/        null, 
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
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "SUM (budget)", 
        /*INTO*/        null, 
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

        // aggregate queries with grouping

        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "count(department)", 
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    "department",
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "AVG(lifetimeOrthoBenefit)", 
        /*INTO*/        null, 
        /*FROM*/        DentalInsurance.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "employee != null",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    "employee.department",
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "min(lifetimeOrthoBenefit)", 
        /*INTO*/        null, 
        /*FROM*/        DentalInsurance.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "employee != null",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    "employee.department",
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MAX(lifetimeOrthoBenefit)", 
        /*INTO*/        null, 
        /*FROM*/        DentalInsurance.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "employee != null",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    "employee.department",
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "sum(lifetimeOrthoBenefit)", 
        /*INTO*/        null, 
        /*FROM*/        DentalInsurance.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "employee != null",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    "employee.department",
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null)
    };
    
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp1", "emp2", "emp3", "emp4", "emp5"}),
        // results for aggregate queries
        new Long(5),
        new Double("99.997"),
        new BigDecimal("2000.99"),
        new BigDecimal("2500000.99"),
        new BigDecimal("2552001.98"),
        // results for aggregate queries with grouping
        Arrays.asList(new Object[]{new Long(3), new Long(2)}),
        Arrays.asList(new Object[]{new Double("99.996"), new Double("99.9985")}),
        Arrays.asList(new Object[]{new BigDecimal("99.995"), new BigDecimal("99.998")}),
        Arrays.asList(new Object[]{new BigDecimal("99.997"), new BigDecimal("99.999")}),
        Arrays.asList(new Object[]{new BigDecimal("299.988"), new BigDecimal("199.997")})
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DefaultUnique.class);
    }

    /** */
    public void testThis() {
        int index = 0;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /** */
    public void testAggregateNoGrouping() {
        final int firstIndex = 1;
        final int lastIndex = 5;
        for (int index = firstIndex; index <= lastIndex; index++) {
            executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                            expectedResult[index]);
            executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                                     expectedResult[index]);
        }
    }

    /** */
    public void testAggregateGrouping() {
        final int firstIndex = 6;
        final int lastIndex = 10;
        for (int index = firstIndex; index <= lastIndex; index++) {
            executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                            expectedResult[index]);
            executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                                     expectedResult[index]);
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
