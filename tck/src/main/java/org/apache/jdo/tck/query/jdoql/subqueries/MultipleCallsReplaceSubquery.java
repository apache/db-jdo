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
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Multiple Calls of addSubquery Replaces Previous Instance
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-51.
 *<BR>
 *<B>Assertion Description: </B> 
 * If the same value of variableDeclaration is used to add multiple subqueries,
 * the subquery replaces the previous subquery for the same named variable.
 */
public class MultipleCallsReplaceSubquery extends SubqueriesTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-51 (MultipleCallsReplaceSubquery) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MultipleCallsReplaceSubquery.class);
    }

    /** */
    public void testPositive() {
        PersistenceManager pm = getPM();
        List expectedResult = getTransientCompanyModelInstancesAsList(
            new String[]{"emp1","emp2","emp4","emp5","emp6","emp7","emp10"});

        // select employees who work more than the average of all employees
        String singleStringJDOQL = 
            "SELECT FROM " + Employee.class.getName() + " WHERE this.weeklyhours > " + 
            "(SELECT AVG(e.weeklyhours) FROM " + Employee.class.getName() + " e)";

        // API query
        // Query returning the weeklyhours of employee with id 1
        Query tmp = pm.newQuery(Employee.class);
        tmp.setResult("this.weeklyhours");
        tmp.setFilter("this.id == 1");
        // Query returning the avg of weeklyhours of all employees
        Query sub = pm.newQuery(Employee.class);
        sub.setResult("avg(this.weeklyhours)");
        Query apiQuery = pm.newQuery(Employee.class);
        apiQuery.setFilter("this.weeklyhours> averageWeeklyhours");
        apiQuery.addSubquery(tmp, "double averageWeeklyhours", null);
        // second call of addSubquery using the same variable
        // should replace the previous setting, so apiQuery should
        // represent the query of singleStringJDOQL
        apiQuery.addSubquery(sub, "double averageWeeklyhours", null);
        executeJDOQuery(ASSERTION_FAILED, apiQuery, singleStringJDOQL, 
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
