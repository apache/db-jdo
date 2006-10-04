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

import java.util.Iterator;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Project;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

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
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        // contains 
        new QueryElementHolder(
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
        /*TO*/          null),
        // contains 
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      null,
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       "!team.contains(null)",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),
    };
    
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        // contains 
        getTransientCompanyModelInstancesAsList(new String[]{}),
        // contains 
        getTransientCompanyModelInstancesAsList(new String[]{
                "emp1", "emp2", "emp3", "emp4", "emp5"})
    };
    
    /** Parameters of valid queries. */
    private Object[][] parameters = {
        // contains 
        {new Project(999l, "TestProject", null)},
        // contains 
        null
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NullCollectionsAndContainsMethod.class);
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
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
        getPM().currentTransaction().begin();
        Employee emp1 = (Employee) getPersistentCompanyModelInstance("emp1");
        for (Iterator itr = emp1.getProjects().iterator(); itr.hasNext();) {
            ((Project) itr.next()).removeMember(emp1); 
        }
        emp1.setProjects(null);
        getPM().currentTransaction().commit();
    }
}
