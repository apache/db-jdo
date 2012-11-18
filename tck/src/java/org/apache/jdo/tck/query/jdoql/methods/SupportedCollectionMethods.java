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

package org.apache.jdo.tck.query.jdoql.methods;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B>Supported collection methods
 *<BR>
 *<B>Keywords:</B> query collection 
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-45.
 *<BR>
 *<B>Assertion Description: </B>
 * Supported collection methods:
 * <UL>
 * <LI>isEmpty</LI>
 * <LI>contains</LI>
 * <LI>size</LI>
 * </UL>
 */

public class SupportedCollectionMethods extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-45 (SupportedCollectionMethods) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        // contains(VARIABLE)
        new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null, 
                /*INTO*/        null, 
                /*FROM*/        Department.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "employees.contains(e) && e.personid == 1",
                /*VARIABLES*/   "Employee e",
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null),
        // contains(PARAMETER)
        new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null, 
                /*INTO*/        null, 
                /*FROM*/        Department.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "employees.contains(e)",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  "Employee e",
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null),
        // !isEmpty
        new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null, 
                /*INTO*/        null, 
                /*FROM*/        Department.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "!employees.isEmpty()",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null),
        // isEmpty
        new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null, 
                /*INTO*/        null, 
                /*FROM*/        Employee.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "team.isEmpty()",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null),
        // size
        new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null, 
                /*INTO*/        null, 
                /*FROM*/        Department.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "employees.size() == 3",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null)
    };

    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        // contains(VARIABLE)
        getTransientCompanyModelInstancesAsList(new String[]{"dept1"}),
        // contains(PARAMETER)
        getTransientCompanyModelInstancesAsList(new String[]{"dept1"}),
        // !isEmpty
        getTransientCompanyModelInstancesAsList(new String[]{"dept1", "dept2"}),
        // isEmpty
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp1", "emp3", "emp4", "emp5"}),
        // size
        getTransientCompanyModelInstancesAsList(new String[]{"dept1"})
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SupportedCollectionMethods.class);
    }
    
    /** */
    public void testContains() {
        int index = 0;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        
        index++;
        getPM().currentTransaction().begin();
        Object[] parameters = new Object[]{getPersistentCompanyModelInstance("emp1")};
        getPM().currentTransaction().commit(); 
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                parameters, expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                parameters, expectedResult[index]);
    }

    /** */
    public void testIsEmpty() {
        for (int index = 2; index < 4; index++) {
            executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                    expectedResult[index]);
            executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                    expectedResult[index]);
        }
    }

    /** */
    public void testSize() {
        int index = 4;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }

}
