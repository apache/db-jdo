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

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Supported Map methods.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-46.
 *<BR>
 *<B>Assertion Description: </B>
 * Supported Map methods:
 * <ul>
 * <li> get(Object)
 * <li> containsKey(Object)
 * <li> containsValue(Object)
 * </ul>
 */
public class SupportedMapMethods extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-46 (SupportedMapMethods) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null, 
                /*INTO*/        null, 
                /*FROM*/        Person.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "phoneNumbers.get('home') == '1111'",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null),
        new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null, 
                /*INTO*/        null, 
                /*FROM*/        Person.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "phoneNumbers.containsKey('home')",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null),
        new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null, 
                /*INTO*/        null, 
                /*FROM*/        Person.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "phoneNumbers.containsValue('1111')",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null)
    };

    /** The expected results of valid queries. */
    private static String[][] expectedResult = {
        {"emp1"},
        {"emp1", "emp2", "emp3", "emp4", "emp5"},
        {"emp1"}
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SupportedMapMethods.class);
    }
    
    /** */
    public void testGet() {
        int index = 0;
        executeQuery(index);
    }
    
    /** */
    public void testContainsKey() {
        int index = 1;
        executeQuery(index);
    }

    /** */
    public void testContainsValue() {
        int index = 2;
        executeQuery(index);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadCompanyModel(getPM(), COMPANY_TESTDATA);
        addTearDownClass(CompanyModelReader.getTearDownClasses());
    }

    /** */
    private void executeQuery(int index) {
        Object[] expectedResultValues = 
            getCompanyModelInstances(expectedResult[index]);
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResultValues);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResultValues);
    }

}
