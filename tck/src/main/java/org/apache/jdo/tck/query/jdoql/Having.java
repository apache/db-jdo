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

package org.apache.jdo.tck.query.jdoql;

import java.util.Arrays;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;

/**
 *<B>Title:</B> Having.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.10-2.
 *<BR>
 *<B>Assertion Description: </B>
 * When having is specified, the having expression consists of 
 * arithmetic and boolean expressions containing aggregate expressions.
 */
public class Having extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.10-2 (Having) failed: ";

    /** 
     * The array of invalid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder<?>[] INVALID_QUERIES = {
        // HAVING clause is not a boolean expression
            new QueryElementHolder<>(
                    /*UNIQUE*/      null,
                    /*RESULT*/      "department, AVG(weeklyhours)",
                    /*INTO*/        null,
                    /*FROM*/        Employee.class,
                    /*EXCLUDE*/     null,
                    /*WHERE*/       null,
                    /*VARIABLES*/   null,
                    /*PARAMETERS*/  null,
                    /*IMPORTS*/     null,
                    /*GROUP BY*/    "department HAVING firstname",
                    /*ORDER BY*/    null,
                    /*FROM*/        null,
                    /*TO*/          null),
        // HAVING clause is a non-aggregate expression using a non-grouping field
            new QueryElementHolder<>(
                    /*UNIQUE*/      null,
                    /*RESULT*/      "department, AVG(weeklyhours)",
                    /*INTO*/        null,
                    /*FROM*/        Employee.class,
                    /*EXCLUDE*/     null,
                    /*WHERE*/       null,
                    /*VARIABLES*/   null,
                    /*PARAMETERS*/  null,
                    /*IMPORTS*/     null,
                    /*GROUP BY*/    "department HAVING firstname == 'emp1First'",
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
        BatchTestRunner.run(Having.class);
    }

    /** */
    public void testPositive0() {
        Object expected = Arrays.asList(new Object[] {
                new Object[] {
                        getTransientCompanyModelInstance(Department.class, "dept1"),
                        Double.valueOf(33.0)},
                new Object[] {
                        getTransientCompanyModelInstance(Department.class, "dept2"),
                        Double.valueOf(0.0)}});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.groupBy(cand.department).having(cand.department.count().gt(0L));
        query.result(false, cand.department, cand.weeklyhours.avg());

        QueryElementHolder<Employee> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      "department, AVG(weeklyhours)",
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       null,
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    "department HAVING COUNT(department) > 0",
                /*ORDER BY*/    null,
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
        // HAVING clause uses field that isn't contained in the SELECT clause.
        Object expected = Arrays.asList(new Object[] {
                new Object[] {
                        getTransientCompanyModelInstance(Department.class, "dept1"),
                        Double.valueOf(33.0)},
                new Object[] {
                        getTransientCompanyModelInstance(Department.class, "dept2"),
                        Double.valueOf(0.0)}});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.groupBy(cand.department).having(cand.personid.count().gt(1L));
        query.result(false, cand.department, cand.weeklyhours.avg());

        QueryElementHolder<Employee> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      "department, AVG(weeklyhours)",
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       null,
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    "department HAVING COUNT(personid) > 1",
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null ,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
    }
    
    /** */
    public void testNegative() {
        for (QueryElementHolder<?> invalidQuery : INVALID_QUERIES) {
            compileAPIQuery(ASSERTION_FAILED, invalidQuery, false);
            compileSingleStringQuery(ASSERTION_FAILED, invalidQuery,
                    false);
        }
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
