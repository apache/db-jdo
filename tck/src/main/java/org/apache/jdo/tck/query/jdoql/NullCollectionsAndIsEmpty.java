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
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;

/**
 *<B>Title:</B> Handling of Null Collections and isEmpty in Queries
 *<BR>
 *<B>Keywords:</B> query nullcollection
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-34.
 *<BR>
 *<B>Assertion Description: </B>
 * <code>null</code>-valued fields of <code>Collection</code> types are treated
 * as if they were empty and <code>isEmpty</code> returns <code>true</code>.
 */

public class NullCollectionsAndIsEmpty extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-34 (NullCollectionsAndIsEmpty) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NullCollectionsAndIsEmpty.class);
    }
    
    /** */
    public void testPositive() {
        // isEmpty
        Object expected = getTransientCompanyModelInstancesAsList(new String[]{"emp1"});

        JDOQLTypedQuery<Employee> query = getPM().newJDOQLTypedQuery(Employee.class);
        QEmployee cand = QEmployee.candidate();
        query.filter(cand.personid.eq(1L).and(cand.projects.isEmpty()));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "personid == 1 && projects.isEmpty()",
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
