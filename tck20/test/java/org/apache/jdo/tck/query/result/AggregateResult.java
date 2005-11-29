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

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Aggregate Result.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.9-6.
 *<BR>
 *<B>Assertion Description: </B>
 * Count returns Long. Sum returns Long for integral types and 
 * the field's type for other Number types 
 * (BigDecimal, BigInteger, Float, and Double). 
 * Sum is invalid if applied to non-Number types. 
 * Avg, min, and max return the type of the expression.
 */
public class AggregateResult extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.9-6 (AggregateResult) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        // COUNT(this)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "COUNT(this)",
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
        
        // COUNT(this)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "COUNT(this)",
        /*INTO*/        null, 
        /*FROM*/        FullTimeEmployee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "personid == 0",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // COUNT(manager)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "COUNT(manager)",
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
        
        // COUNT(manager.personid)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "COUNT(manager.personid)",
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
        
        // COUNT(DISTINCT manager)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "COUNT(DISTINCT manager)",
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
        
        // SUM(long)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "SUM(personid)",
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
        
        // SUM(double)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "SUM(salary)",
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
        
        // SUM(BigDecimal)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "SUM(budget)",
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
        
        // SUM(budget)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "SUM(budget)",
        /*INTO*/        null, 
        /*FROM*/        Project.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "projid == 0",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // SUM(manager.salary)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "SUM(manager.salary)",
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
        
        // SUM(DISTINCT manager.salary)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "SUM(DISTINCT manager.salary)",
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
        
        // MIN(long)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MIN(personid)",
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
        
        // MIN(double)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MIN(salary)",
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
        
        // MIN(BigDecimal)
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
        
        // MIN(budget)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MIN(budget)",
        /*INTO*/        null, 
        /*FROM*/        Project.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "projid == 0",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // MIN(manager.salary)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MIN(manager.salary)",
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
        
        // MAX(long)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MAX(personid)",
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
        
        // MAX(double)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MAX(salary)",
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
        
        // MAX(BigDecimal)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MAX(budget)",
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
        
        // MAX(budget)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MAX(budget)",
        /*INTO*/        null, 
        /*FROM*/        Project.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "projid == 0",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // MAX(manager.salary)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MAX(manager.salary)",
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
        
        // AVG(long)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "AVG(personid)",
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
        
        // AVG(double)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "AVG(salary)",
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
        
        // AVG(BigDecimal)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "AVG(lifetimeOrthoBenefit)",
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

        // AVG(lifetimeOrthoBenefit)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "AVG(lifetimeOrthoBenefit)",
        /*INTO*/        null, 
        /*FROM*/        DentalInsurance.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "insid == 0",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        
        // AVG(manager.salary)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "AVG(manager.salary)",
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
        
        // AVG(DISTINCT manager.salary)
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "AVG(DISTINCT manager.salary)",
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
        /*TO*/          null)
        
    };
    
    /** 
     * The array of invalid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] INVALID_QUERIES = {
        // SUM
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "SUM(firstname)",
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
        /*TO*/          null)
    };
        
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        // COUNT(this)
        new Long(3),
        // COUNT(this)
        new Long(0),
        // COUNT(manager)
        new Long(2),
        // COUNT(manager.personid)
        new Long(2),
        // COUNT(DISTINCT manager)
        new Long(1),
        // SUM(long)
        new Long(1+2+5),
        // SUM(double)
        new Double(20000.0+10000.0+45000.0),
        // SUM(BigDecimal)
        new BigDecimal("2500000.99").add
            (new BigDecimal("50000.00")).add(new BigDecimal("2000.99")),
        // SUM(budget)
        null,
        // SUM(manager.salary)
        new Double(20000),
        // SUM(DISTINCT manager.salary)
        new Double(10000),
        // MIN(long)
        new Long(1),
        // MIN(double)
        new Double(10000.0),
        // MIN(BigDecimal)
        new BigDecimal("2000.99"),
        // MIN(budget)
        null,
        // MIN(manager.salary)
        new Double(10000),
        // MAX(long)
        new Long(5),
        // MAX(double)
        new Double(45000.0),
        // MAX(BigDecimal)
        new BigDecimal("2500000.99"),
        // MAX(budget)
        null,
        // MAX(manager.salary)
        new Double(10000),
        // AVG(long)
        new Long(3),
        // AVG(double)
        new Double(25000.0),
        // AVG(BigDecimal)
        new BigDecimal("99.999"),
        // AVG(lifetimeOrthoBenefit)
        null,
        // AVG(manager.salary)
        new Double(10000),
        // AVG(DISTINCT manager.salary)
        new Double(10000)
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(AggregateResult.class);
    }
    
    /** */
    public void testCount() {
        for(int i = 0; i < 5; i++) {
            executeQuery(i);
        }
    }

    /** */
    public void testSUM() {
        for(int i = 5; i < 11; i++) {
            executeQuery(i);
        }
    }

    /** */
    public void testMIN() {
        for(int i = 11; i < 16; i++) {
            executeQuery(i);
        }
    }

    /** */
    public void testMAX() {
        for(int i = 16; i < 21; i++) {
            executeQuery(i);
        }
    }

    /** */
    public void testAVG() {
        for(int i = 21; i < 27; i++) {
            executeQuery(i);
        }
    }

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
