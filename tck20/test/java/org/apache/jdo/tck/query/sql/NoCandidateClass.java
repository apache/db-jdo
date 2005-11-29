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

package org.apache.jdo.tck.query.sql;

import java.util.Arrays;

import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> No Candidate Class.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.7-3.
 *<BR>
 *<B>Assertion Description: </B>
 * SQL queries can be defined without a candidate class. 
 * These queries can be found by name using the factory method newNamedQuery, 
 * specifying the class as null, 
 * or can be constructed without a candidate class.
 */
public class NoCandidateClass extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.7-3 (CandidateClass) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NoCandidateClass.class);
    }
    
    /** The array of valid SQL queries. */
    private static final String[] VALID_SQL_QUERIES = {
        "SELECT firstname, lastname FROM {0}.persons"
    };
    
    /** 
     * The expected results of valid SQL queries.
     */
    private Object[] expectedResult = {
        Arrays.asList(new Object[]{
                new Object[]{"emp1First", "emp1Last"},
                new Object[]{"emp2First", "emp2Last"},
                new Object[]{"emp3First", "emp3Last"},
                new Object[]{"emp4First", "emp4Last"},
                new Object[]{"emp5First", "emp5Last"}})
    };
            
    /** */
    public void testNamedQuery() {
        if (isSQLSupported()) {
            int index = 0;
            Query query = getPM().newNamedQuery(null, "SQLQuery");
            executeJDOQuery(ASSERTION_FAILED, query, "Named SQL query", 
                    false, null, expectedResult[index], true);
        }
    }

    /** */
    public void testNoCandidateClass() {
        if (isSQLSupported()) {
            int index = 0;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    null, null, null, expectedResult[index], false);
        }
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadCompanyModel(getPM(), COMPANY_TESTDATA);
        addTearDownClass(CompanyModelReader.getTearDownClasses());
    }
}
