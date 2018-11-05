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
import java.math.BigDecimal;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.DentalInsurance;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.company.QDentalInsurance;
import org.apache.jdo.tck.pc.company.QProject;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.IfThenElseExpression;

/**
 *<B>Title:</B> IfElseResult.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> .
 *<BR>
 *<B>Assertion Description: </B>
 */
public class IfElseResult extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion (IfElseResult) failed: ";

    /** 
     * The array of invalid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] INVALID_QUERIES = {
        // Invalid type of condition expression 
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "IF (this.firstname) 0 ELSE 1",
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
        // missing ELSE  
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "IF (this.employee == null) 0",
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
        // type of THEN expr must be the same as type of ELSE expr
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "IF (this.employee == null) 'Michael' ELSE this.employee",
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
        /*TO*/          null)
    };
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NullResults.class);
    }
    
    /** */
    public void testPositive0() {
        Object expected = Arrays.asList("emp1Last", "emp2Last", "emp3Last", "emp4Last", "emp5Last", "No employee");

        JDOQLTypedQuery<DentalInsurance> query = getPM().newJDOQLTypedQuery(DentalInsurance.class);
        QDentalInsurance cand = QDentalInsurance.candidate();
        query.result(false,
                query.ifThenElse(cand.employee.eq((Employee)null), "No employee", cand.employee.lastname));
        query.orderBy(cand.insid.asc());

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "IF (this.employee == null) 'No employee' ELSE this.employee.lastname",
                /*INTO*/        null,
                /*FROM*/        DentalInsurance.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       null,
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    "this.insid ascending",
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
    }

    /** */
    public void testPositive1() {
        Object expected = Arrays.asList(
                new BigDecimal("3000001.188"), new BigDecimal("55000"), new BigDecimal("2201.089"));

        JDOQLTypedQuery<Project> query = getPM().newJDOQLTypedQuery(Project.class);
        QProject cand = QProject.candidate();
        query.result(false,
                query.ifThenElse(BigDecimal.class, cand.members.size().gt(2),
                        cand.budget.mul(new BigDecimal(1.2)), cand.budget.mul(new BigDecimal(1.1))));
        query.orderBy(cand.projid.asc());

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "IF (this.members.size() > 2) this.budget * 1.2 ELSE this.budget * 1.1",
                /*INTO*/        null,
                /*FROM*/        Project.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       null,
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    "this.projid ascending",
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
    }

    /** */
    public void testPositive2() {
        Object expected = Arrays.asList("No reviewer", "Reviewer team", "Single reviewer");

        JDOQLTypedQuery<Project> query = getPM().newJDOQLTypedQuery(Project.class);
        QProject cand = QProject.candidate();
        IfThenElseExpression<String> ifThenElse = query.ifThen(cand.reviewers.isEmpty(), "No reviewer").
                ifThen(cand.reviewers.size().eq(1), "Single reviewer").elseEnd("Reviewer team");
        query.result(false, ifThenElse);
        query.orderBy(cand.projid.asc());

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "IF (this.reviewers.isEmpty()) 'No reviewer' " +
                "ELSE IF (this.reviewers.size() == 1) 'Single reviewer' ELSE 'Reviewer team'",
                /*INTO*/        null,
                /*FROM*/        Project.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       null,
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    "this.projid ascending",
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
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
