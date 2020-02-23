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

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.QDepartment;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;

/**
 *<B>Title:</B> NullPointerException in Result Expression.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.9-4.
 *<BR>
 *<B>Assertion Description: </B>
 * If any result is a navigational expression, 
 * and a non-terminal field or variable has a null value for a particular set 
 * of conditions (the result calculation would throw NullPointerException), 
 * then the result is null for that result expression.
 */
public class NPEInResultExpr extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.9-4 (NPEInResultExpr) failed: ";
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NPEInResultExpr.class);
    }
    
    /** */
    public void testPositive0() {
        Object expected = Arrays.asList("emp2Last", null, "emp2Last", "emp2Last", "emp2Last");

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.result(false, cand.manager.lastname);

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "manager.lastname",
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
    }

    /** */
    public void testPositive1() {
        Object expected = Arrays.asList("emp2Last", null, "emp2Last", "emp2Last", "emp2Last");

        JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
        QDepartment cand = QDepartment.candidate();
        QEmployee e = QEmployee.variable("e");
        query.filter(cand.employees.contains(e));
        query.result(false, e.manager.lastname);

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "e.manager.lastname",
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
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
