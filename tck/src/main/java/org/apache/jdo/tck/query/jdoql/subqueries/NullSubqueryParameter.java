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

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Null Subquery Parameter.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-52.
 *<BR>
 *<B>Assertion Description: </B> 
 * If the subquery parameter is null, the variable is unset, effectively 
 * making the variable named in the variableDeclaration unbound.
 */
public class NullSubqueryParameter extends SubqueriesTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-52 (NullSubqueryParameter) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NullSubqueryParameter.class);
    }

    /** */
    public void testPositive() {
        PersistenceManager pm = getPM();

        List<IEmployee> expectedResult = getTransientCompanyModelInstancesAsList(
            "emp1", "emp2", "emp4", "emp6", "emp7", "emp10");

        String singleStringJDOQL = 
            "SELECT FROM " + Employee.class.getName() +
            " WHERE this.weeklyhours == emp.weeklyhours && " +
            "emp.firstname == 'emp1First' VARIABLES Employee emp";

        // API query
        Query<Employee> apiQuery = pm.newQuery(Employee.class);
        apiQuery.setFilter("this.weeklyhours == emp.weeklyhours && emp.firstname == 'emp1First'");
        // null subquery parameter
        apiQuery.addSubquery(null, Employee.class.getName() + " emp", null); 
        executeJDOQuery(ASSERTION_FAILED, apiQuery, singleStringJDOQL, 
                        false, null, expectedResult, true);

        // single String JDOQL
        Query<Employee> singleStringQuery = pm.newQuery(singleStringJDOQL);
        executeJDOQuery(ASSERTION_FAILED, singleStringQuery, singleStringJDOQL, 
                        false, null, expectedResult, true);
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
