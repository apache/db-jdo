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

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.QDepartment;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;
import java.util.List;

/**
 *<B>Title:</B> Element Returned in Query Result
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-2.
 *<BR>
 *<B>Assertion Description: </B>
An element of the candidate collection is returned in the result if:
<UL>
<LI>it is assignment compatible to the candidate <code>Class</code> of the
<code>Query</code>; and</LI>
<LI>for all variables there exists a value for which the filter expression
evaluates to <code>true</code>.
The user may denote uniqueness in the filter expression
by explicitly declaring an expression
(for example, <code>e1 != e2</code>).</LI>
</UL>
 */

public class DenoteUniquenessInFilter extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-2 (DenoteUniquenessInFilter) failed: ";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DenoteUniquenessInFilter.class);
    }

    /** */
    @SuppressWarnings("unchecked")
    public void testPositive0() {
        // Uniqueness not specified.
        // emp1 qualifies for both contains clause => result is dept1
        List<Department> expected = getTransientCompanyModelInstancesAsList(Department.class, "dept1");

        JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
        QDepartment cand = QDepartment.candidate();
        QEmployee e1 = QEmployee.variable("e1");
        QEmployee e2 = QEmployee.variable("e2");
        query.filter(cand.employees.contains(e1).and(e1.personid.eq(1l).and(
                cand.employees.contains(e2).and(e2.weeklyhours.eq(40d)))));

        QueryElementHolder<Department> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Department.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "employees.contains(e1) && (e1.personid == 1 && " +
                "(employees.contains(e2) && (e2.weeklyhours == 40)))",
                /*VARIABLES*/   "Employee e1; Employee e2",
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/   query,
                /*paramValues*/  null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    @SuppressWarnings("unchecked")
    public void testPositive1() {
        // Uniqueness specified.
        // Only emp3 qualifies for both contains clause.
        // Condition e1 != e2 violated => result is empty
        List<Department> expected = getTransientCompanyModelInstancesAsList(Department.class);

        JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
        QDepartment cand = QDepartment.candidate();
        QEmployee e1 = QEmployee.variable("e1");
        QEmployee e2 = QEmployee.variable("e2");
        query.filter(cand.employees.contains(e1).and(e1.personid.eq(3l).and(
                cand.employees.contains(e2).and(e2.weeklyhours.eq(19d).and(e1.ne(e2))))));

        QueryElementHolder<Department> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Department.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "employees.contains(e1) && (e1.personid == 3 && " +
                "(employees.contains(e2) && (e2.weeklyhours == 19 && " +
                "e1 != e2)))",
                /*VARIABLES*/   "Employee e1; Employee e2",
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/   query,
                /*paramValues*/  null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    @SuppressWarnings("unchecked")
    public void testPositive2() {
        // Uniqueness specified.
        // Only emp1 matches the first contains clause.
        // emp1 and emp2 match the second contains clause.
        // Thus, there are two different values for e1 and e2
        // satifying the entire filter => result is dept1
        List<Department> expected = getTransientCompanyModelInstancesAsList(Department.class, "dept1");

        JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
        QDepartment cand = QDepartment.candidate();
        QEmployee e1 = QEmployee.variable("e1");
        QEmployee e2 = QEmployee.variable("e2");
        query.filter(cand.employees.contains(e1).and(e1.personid.eq(1l).and(
                cand.employees.contains(e2).and(e2.weeklyhours.eq(40d).and(e1.ne(e2))))));

        QueryElementHolder<Department> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Department.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "employees.contains(e1) && (e1.personid == 1 && " +
                "(employees.contains(e2) && (e2.weeklyhours == 40 && " +
                "e1 != e2)))",
                /*VARIABLES*/   "Employee e1; Employee e2",
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/   query,
                /*paramValues*/  null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
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
