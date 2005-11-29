/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
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
 *<B>Assertion ID:</B> A14.6.2-44.
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
        "Assertion A14.6.2-36 (SupportedCollectionMethods) failed: ";
    
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
        getCompanyModelInstancesAsList(new String[]{"dept1"}),
        // contains(PARAMETER)
        getCompanyModelInstancesAsList(new String[]{"dept1"}),
        // !isEmpty
        getCompanyModelInstancesAsList(new String[]{"dept1", "dept2"}),
        // isEmpty
        getCompanyModelInstancesAsList(new String[]{
                "emp1", "emp3", "emp4", "emp5"}),
        // size
        getCompanyModelInstancesAsList(new String[]{"dept1"})
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
        Object[] parameters = new Object[]{
                getParameter(Employee.class, "personid == 1", true)};
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
        loadCompanyModel(getPM(), COMPANY_TESTDATA);
        addTearDownClass(CompanyModelReader.getTearDownClasses());
    }
    
    /** */
    private Object getParameter(
            Class candidateClass, String filter, boolean unique) {
        Object result;
        PersistenceManager pm = getPM();
        Transaction transaction = pm.currentTransaction();
        transaction.begin();
        try {
            Query query = filter == null ? pm.newQuery(candidateClass) :
                pm.newQuery(candidateClass, filter);
            if (unique) {
                query.setUnique(unique);
            }
            try {
                result = query.execute();
            } finally {
                query.closeAll();
            }
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return result;
    }
}
