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

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Correlated Subqueries Without Parameters
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-56
 *<BR>
 *<B>Assertion Description: </B> 
 * A correlated subquery is a subquery which contains references to expressions
 * in the outer query. If the correlation can be expressed as a restriction of
 * the candidate collection of the subquery, no parameters are needed.
 */
public class CorrelatedSubqueries extends SubqueriesTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-56 (CorrelatedSubqueries) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CorrelatedSubqueries.class);
    }

    /** */
    public void testPositive() throws Exception {
        PersistenceManager pm = getPM();

        List expectedResult = getTransientCompanyModelInstancesAsList(
            new String[]{"emp1", "emp2", "emp4", "emp6", "emp7", "emp10"});

        // Select employees who work more than the average of 
        // their department employees. 
        String singleStringJDOQL = 
            "SELECT FROM " + Employee.class.getName() + " WHERE this.weeklyhours > " + 
            "(SELECT AVG(e.weeklyhours) FROM this.department.employees e)";

        // API query
        Query sub = pm.newQuery(Employee.class);
        sub.setResult("avg(this.weeklyhours)");
        Query apiQuery = pm.newQuery(Employee.class);
        apiQuery.setFilter("this.weeklyhours> averageWeeklyhours");
        apiQuery.addSubquery(sub, "double averageWeeklyhours", 
                          "this.department.employees"); 
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
