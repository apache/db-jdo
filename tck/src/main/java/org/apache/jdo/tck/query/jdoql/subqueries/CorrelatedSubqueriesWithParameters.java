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
 
package org.apache.jdo.tck.query.jdoql.subqueries;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Support for subqueries in JDOQL
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-57.
 *<BR>
 *<B>Assertion Description: </B> 
 * If the correlation cannot be expressed as a restriction of the candidate 
 * collection, the correlation is expressed as one or more parameters in the 
 * subquery which are bound to expressions of the outer query.
 */
public class CorrelatedSubqueriesWithParameters extends SubqueriesTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-57 (CorrelatedSubqueriesWithParameters) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CorrelatedSubqueriesWithParameters.class);
    }

    /** */
    public void testPositive() throws Exception {
        PersistenceManager pm = getPM();
        runTestSubqueries01(pm);
        runTestSubqueries02(pm);
        runTestSubqueries03(pm);
    }

    /** 
     * Test subquery: 
     */
    void runTestSubqueries01(PersistenceManager pm) {
        List expectedResult = getTransientCompanyModelInstancesAsList(
            new String[]{"emp2", "emp6", "emp9"});
        
        // Select employees who work more than the average of the employees 
        // in their department having the same manager
        String singleStringJDOQL = 
            "SELECT FROM " + Employee.class.getName() +
            " WHERE this.weeklyhours > " +
            "(SELECT AVG(e.weeklyhours) FROM this.department.employees e " + 
            " WHERE e.manager == this.manager)";

        // API query
        Query sub = pm.newQuery(Employee.class);
        sub.setResult("avg(this.weeklyhours)");
        sub.setFilter("this.manager == :manager");
        Query apiQuery = pm.newQuery(Employee.class);
        apiQuery.setFilter("this.weeklyhours> averageWeeklyhours");
        apiQuery.addSubquery(sub, "double averageWeeklyhours", 
                             "this.department.employees", "this.manager"); 
        executeJDOQuery(ASSERTION_FAILED, apiQuery, singleStringJDOQL, 
                        false, null, expectedResult, true);

        // API query against memory model
        List allEmployees = getAllEmployees(pm);
        apiQuery.setCandidates(allEmployees);
        executeJDOQuery(ASSERTION_FAILED, apiQuery, singleStringJDOQL, 
                        false, null, expectedResult, true);

        // single String JDOQL
        Query singleStringQuery = pm.newQuery(singleStringJDOQL);
        executeJDOQuery(ASSERTION_FAILED, singleStringQuery, singleStringJDOQL, 
                        false, null, expectedResult, true);
    }

    /** */
    void runTestSubqueries02(PersistenceManager pm) {
        List expectedResult = getTransientCompanyModelInstancesAsList(
            new String[]{"emp2", "emp10"});

        // Select employees hired after a particular date who work more 
        // than the average of all employees of the same manager
        String singleStringJDOQL = 
            "SELECT FROM " + Employee.class.getName() +
            " WHERE this.hiredate > :hired && this.weeklyhours > " +
            "(SELECT AVG(e.weeklyhours) FROM " + Employee.class.getName() +
            " e WHERE e.manager == this.manager)"; 

        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(2002, Calendar.SEPTEMBER, 1, 0, 0, 0);
        Date hired = cal.getTime();

        // API query
        Query sub = pm.newQuery(Employee.class);
        sub.setResult("avg(this.weeklyhours)");
        sub.setFilter("this.manager == :manager");
        Query apiQuery = pm.newQuery(Employee.class);
        apiQuery.setFilter("this.hiredate > :hired && this.weeklyhours> averageWeeklyhours");
        apiQuery.addSubquery(sub, "double averageWeeklyhours", null, "this.manager"); 
        executeJDOQuery(ASSERTION_FAILED, apiQuery, singleStringJDOQL, 
                        false, new Object[] {hired}, expectedResult, true);

        // API query against memory model
        List allEmployees = getAllEmployees(pm);
        apiQuery.setCandidates(allEmployees);
        executeJDOQuery(ASSERTION_FAILED, apiQuery, singleStringJDOQL, 
                        false, new Object[]{hired} , expectedResult, true);

        // single String JDOQL
        Query singleStringQuery = pm.newQuery(singleStringJDOQL);
        executeJDOQuery(ASSERTION_FAILED, singleStringQuery, singleStringJDOQL, 
                        false, new Object[]{hired}, expectedResult, true);
    }

    /** */
    void runTestSubqueries03(PersistenceManager pm) {
        List expectedResult = getTransientCompanyModelInstancesAsList(
            new String[]{"emp2", "emp6", "emp9", "emp10"});

        // Select employees who work more than the average of all 
        // employees of the same manager
        String singleStringJDOQL = 
            "SELECT FROM " + Employee.class.getName() + " WHERE this.weeklyhours > " +
            "(SELECT AVG(e.weeklyhours) FROM " + Employee.class.getName() +
            " e WHERE e.manager == this.manager)";

        // API query
        Query sub = pm.newQuery(Employee.class);
        sub.setResult("avg(this.weeklyhours)");
        sub.setFilter("this.manager == :manager");
        Query apiQuery = pm.newQuery(Employee.class);
        apiQuery.setFilter("this.weeklyhours > averageWeeklyhours");
        apiQuery.addSubquery(sub, "double averageWeeklyhours", null, "this.manager"); 
        executeJDOQuery(ASSERTION_FAILED, apiQuery, singleStringJDOQL, 
                        false, null, expectedResult, true);

        // API query against memory model
        List allEmployees = getAllEmployees(pm);
        apiQuery.setCandidates(allEmployees);
        executeJDOQuery(ASSERTION_FAILED, apiQuery, singleStringJDOQL, 
                        false, null, expectedResult, true);

        // single String JDOQL
        Query singleStringQuery = pm.newQuery(singleStringJDOQL);
        executeJDOQuery(ASSERTION_FAILED, singleStringQuery, singleStringJDOQL, 
                        false, null, expectedResult, true);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }

}


