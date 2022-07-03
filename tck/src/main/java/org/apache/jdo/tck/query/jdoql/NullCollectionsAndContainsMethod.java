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

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.Expression;
import java.util.HashMap;
import java.util.Map;

/**
 *<B>Title:</B> Null Collections and Contains Method
 *<BR>
 *<B>Keywords:</B> query nullcollection
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-35.
 *<BR>
 *<B>Assertion Description: </B>
 * <code>null</code>-valued fields of <code>Collection</code> types are treated
 * as if they were empty and all <code>contains</code> methods return
 * <code>false</code>.
 */

public class NullCollectionsAndContainsMethod extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-35 (NullCollectionsAndContainsMethod) failed: ";
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NullCollectionsAndContainsMethod.class);
    }

    /**
     *
     */
    public void testContains1() {
        Object expected = getTransientCompanyModelInstancesAsList(new String[]{});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        Expression<Project> empParam = query.parameter("p", Project.class);
        query.filter(cand.personid.eq(1L).and(cand.projects.contains(empParam)));

        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("p", getPersistentCompanyModelInstance("proj1"));

        // contains
        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "personid == 1 && projects.contains(p)",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  "org.apache.jdo.tck.pc.company.Project p",
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
    public void testContains2() {
        Object expected = getTransientCompanyModelInstancesAsList("emp2", "emp3");

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        Expression<Project> empParam = query.parameter("p", Project.class);
        query.filter(cand.projects.contains(empParam));

        Map<String, Object> paramValues = new HashMap<>();
        paramValues.put("p", getPersistentCompanyModelInstance("proj1"));

        // contains
        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "projects.contains(p)",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  "org.apache.jdo.tck.pc.company.Project p",
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
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
        getPM().currentTransaction().begin();
        Employee emp1 = (Employee) getPersistentCompanyModelInstance("emp1");
        emp1.setProjects(null);
        getPM().currentTransaction().commit();
    }
}
