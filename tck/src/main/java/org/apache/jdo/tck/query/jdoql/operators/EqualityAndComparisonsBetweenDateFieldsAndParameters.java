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

package org.apache.jdo.tck.query.jdoql.operators;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.DateTimeExpression;

/**
 *<B>Title:</B> Equality and Comparisons Between Date Fields and Parameters
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-4.
 *<BR>
 *<B>Assertion Description: </B>
Equality and ordering comparisons of <code>Date</code> fields and
<code>Date</code> parameters are valid in a <code>Query</code> filter.
 */

public class EqualityAndComparisonsBetweenDateFieldsAndParameters 
    extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-4 (EqualityAndComparisonsBetweenDateFieldsAndParameters) failed: ";
    
    /** */
    private static final Date FIRST_OF_JAN_1999;
    static {
        // initialize static field FIRST_OF_JAN_1999
        Calendar cal = new GregorianCalendar();
        cal.set(1999, GregorianCalendar.JANUARY, 1, 0, 0, 0);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        FIRST_OF_JAN_1999 = cal.getTime();
    }
    
    /** Parameters of valid queries. */
    private Object[][] parameters = {
        // date field == date parameter
        {FIRST_OF_JAN_1999},
        // date field >= date parameter
        {FIRST_OF_JAN_1999},
        // date field >= date parameter
        {FIRST_OF_JAN_1999}
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(EqualityAndComparisonsBetweenDateFieldsAndParameters.class);
    }

    /**
     *
     */
    public void testFieldEqualsParameter() {
        Object expected = getTransientCompanyModelInstancesAsList(new String[]{"emp1"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        DateTimeExpression param = query.datetimeParameter("param");
        query.filter(cand.hiredate.eq(param));

        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("param", FIRST_OF_JAN_1999);

        // date field == date parameter
        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "hiredate == param",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  "java.util.Date param",
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ paramValues);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /**
     *
     */
    public void testFieldGEParameter() {
        Object expected = getTransientCompanyModelInstancesAsList(new String[]{
                "emp1", "emp2", "emp3", "emp4"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        DateTimeExpression param = query.datetimeParameter("param");
        query.filter(cand.hiredate.gteq(param));

        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("param", FIRST_OF_JAN_1999);

        // date field >= date parameter
        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "hiredate >= param",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  "java.util.Date param",
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ paramValues);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /**
     *
     */
    public void testParameterLTField() {
        Object expected = getTransientCompanyModelInstancesAsList(new String[]{});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        DateTimeExpression param = query.datetimeParameter("param");
        query.filter(param.lt(cand.birthdate));

        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("param", FIRST_OF_JAN_1999);

        // Import Department twice
        QueryElementHolder holder = // date parameter < date field
                new QueryElementHolder(
                        /*UNIQUE*/      null,
                        /*RESULT*/      null,
                        /*INTO*/        null,
                        /*FROM*/        Employee.class,
                        /*EXCLUDE*/     null,
                        /*WHERE*/       "param < birthdate",
                        /*VARIABLES*/   null,
                        /*PARAMETERS*/  "java.util.Date param",
                        /*IMPORTS*/     null,
                        /*GROUP BY*/    null,
                        /*ORDER BY*/    null,
                        /*FROM*/        null,
                        /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ paramValues);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
