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

package org.apache.jdo.tck.query.jdoql;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Namespace of Type Names Separate From Fields, Variables, Parameters
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.4-1.
 *<BR>
 *<B>Assertion Description: </B>
Type names have their own namespace that is separate 
from the namespace for fields, variables and parameters.

 */

public class SeparateNamespaceForTypeNames extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.4-1 (SeparateNamespaceForTypeNames) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        // query having a parameter with the same name as a type
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null,
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "department == Department",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  "Department Department",
        /*IMPORTS*/     "import org.apache.jdo.tck.pc.company.Department",
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
        // query having a parameter with the same name as a type
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null,
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "employees.contains(Employee) && Employee.firstname == \"emp1First\"",
        /*VARIABLES*/   "Employee Employee",
        /*PARAMETERS*/  null,
        /*IMPORTS*/     "import org.apache.jdo.tck.pc.company.Employee",
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
    };
    
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        // query having a parameter with the same name as a type
        getTransientCompanyModelInstancesAsList(new String[]{"emp1", "emp2", "emp3"}),
        // query having a parameter with the same name as a type
        getTransientCompanyModelInstancesAsList(new String[]{"dept1"})
    };
    
    /** Parameters of valid queries. */
    private Object[][] parameters = {
        // query having a parameter with the same name as a type
        {getPersistentCompanyModelInstance("dept1")},
        // query having a parameter with the same name as a type
        null
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SeparateNamespaceForTypeNames.class);
    }

    /** */
    public void testPositive() {
        for (int i = 0; i < VALID_QUERIES.length; i++) {
            executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[i], 
                    parameters[i], expectedResult[i]);
            executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[i], 
                    parameters[i], expectedResult[i]);
        }
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadAndPersistCompanyModel(getPM());
        addTearDownClass(CompanyModelReader.getTearDownClasses());
    }
}
