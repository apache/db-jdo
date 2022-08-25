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
import java.util.HashMap;
import java.util.Map;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.pc.company.QProject;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.Expression;

/**
 *<B>Title:</B> Result Expressions.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.9-5.
 *<BR>
 *<B>Assertion Description: </B>
 * The result expressions include: ... 
 * The result expression can be explicitly cast using the (cast) operator.
 */
public class ResultExpressions extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.9-5 (ResultExpressions) failed: ";
    
    /** 
     * The array of invalid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] INVALID_QUERIES = {
        // unknown field x
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "x",
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
        // field salary is declared in a subclass of the candidate class
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "salary",
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
        // project collection field
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "employees",
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        // project map field
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "phoneNumbers",
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
        /*TO*/          null)
    };


            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ResultExpressions.class);
    }
    
    /** */
    public void testThis() {
        Object expectedResult = getTransientCompanyModelInstancesAsList(new String[]{
                "emp1", "emp2", "emp3", "emp4", "emp5"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.result(false, cand);

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "this",
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

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expectedResult);
    }

    /** */
    public void testField() {
        Object expectedResult = Arrays.asList(
                Long.valueOf(1), Long.valueOf(2), Long.valueOf(3), Long.valueOf(4), Long.valueOf(5));

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.result(false, cand.personid);

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "personid",
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

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testVariableField() {
        Object expectedResult = Arrays.asList(new Object[]{Long.valueOf(1)});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        QProject p = QProject.variable("p");
        query.result(false, p.projid);
        query.filter(cand.projects.contains(p).and(cand.personid.eq(1l)));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "p.projid",
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "projects.contains(p) && personid == 1",
                /*VARIABLES*/   "Project p",
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testVariable() {
        Object expectedResult = getTransientCompanyModelInstancesAsList(new String[]{"proj1"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        QProject variable = QProject.variable("p");
        query.result(false, variable);
        query.filter(cand.projects.contains(variable).and(cand.personid.eq(1l)));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "p",
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "projects.contains(p) && personid == 1",
                /*VARIABLES*/   "Project p",
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testCountThis() {
        // COUNT(this)
        Object expectedResult = Long.valueOf(5);

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.result(false, cand.count());

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      Boolean.TRUE,
                /*RESULT*/      "COUNT(this)",
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

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testCountVariable() {
        // COUNT(variable)
        Object expectedResult = Long.valueOf(1);

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        QProject p = QProject.variable("p");
        query.result(false, p.count());
        query.filter(cand.projects.contains(p).and(cand.personid.eq(1l)));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      Boolean.TRUE,
                /*RESULT*/      "COUNT(p)",
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "projects.contains(p) && personid == 1",
                /*VARIABLES*/   "Project p",
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testSum() {
        // SUM
        Object expectedResult = Long.valueOf(1+2+3+4+5);

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.result(false, cand.personid.sum());

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      Boolean.TRUE,
                /*RESULT*/      "SUM(personid)",
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

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testMin() {
        // MIN
        Object expectedResult = Long.valueOf(1);

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.result(false, cand.personid.min());

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      Boolean.TRUE,
                /*RESULT*/      "MIN(personid)",
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

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testMax() {
        // MAX
        Object expectedResult = Long.valueOf(5);

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.result(false, cand.personid.max());

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      Boolean.TRUE,
                /*RESULT*/      "MAX(personid)",
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

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testAvg() {
        // AVG
        Object expectedResult = Double.valueOf(3);

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.result(false, cand.personid.avg());

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      Boolean.TRUE,
                /*RESULT*/      "AVG(personid)",
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

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testFieldExpression() {
        // field expression
        Object expectedResult = Arrays.asList(
                Long.valueOf(2), Long.valueOf(3), Long.valueOf(4), Long.valueOf(5), Long.valueOf(6));

        JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
        QPerson cand = QPerson.candidate();
        query.result(false, cand.personid.add(1));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "personid + 1",
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testNavigationalExpressionThis() {
        // navigational expression this
        Object expectedResult = Arrays.asList(
                Long.valueOf(1), Long.valueOf(2),Long.valueOf(3), Long.valueOf(4), Long.valueOf(5));

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.result(false, cand.personid);

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "this.personid",
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

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testNavigationalExpressionVariable() {
        // navigational expression variable
        Object expectedResult =
                Arrays.asList(new Object[]{"Development", "Human Resources"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        QEmployee e = QEmployee.variable("e");
        Expression<Employee> eExpr = query.variable("e", Employee.class);
        query.result(true, e.department.name);
        query.filter(cand.team.contains(eExpr).and(cand.personid.eq(2l)));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "distinct e.department.name",
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "team.contains(e) && personid == 2",
                /*VARIABLES*/   "Employee e",
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testNavigationalExpressionParameter() {
        // navigational expression parameter
        Object expectedResult = Arrays.asList(Long.valueOf(1));

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        QProject p = QProject.parameter("p");
        query.parameter("p", Project.class);
        query.result(false, p.projid);
        query.filter(cand.personid.eq(1l).and(p.projid.eq(cand.personid)));

        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("p", getPersistentCompanyModelInstance("proj1"));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "p.projid",
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "personid == 1 && p.projid == this.personid",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  "Project p",
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ paramValues);

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testNavigationalExpressionField() {
        // navigational expression field
        Object expectedResult = Arrays.asList(new Object[]{Long.valueOf(1)});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.result(false, cand.department.deptid);
        query.filter(cand.personid.eq(1l));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "department.deptid",
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "personid == 1",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testParameter() {
        // parameter
        Object expectedResult = getTransientCompanyModelInstancesAsList(new String[]{"proj1"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        QProject p = QProject.parameter("p");
        query.parameter("p", Project.class);
        query.result(false, p);
        query.filter(cand.personid.eq(1l).and(cand.personid.eq(p.projid)));

        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("p", getPersistentCompanyModelInstance("proj1"));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "p",
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "personid == 1 && personid == p.projid",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  "Project p",
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ paramValues);

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
    }

    /** */
    public void testCast() {
        // cast
        Object expectedResult = getTransientCompanyModelInstancesAsList(new String[]{"emp2"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.result(false, cand.manager.cast(FullTimeEmployee.class));
        query.filter(cand.personid.eq(1l));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "(FullTimeEmployee)manager",
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "personid == 1",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expectedResult);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expectedResult);
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
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
