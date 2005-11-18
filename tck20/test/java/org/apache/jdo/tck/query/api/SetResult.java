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

package org.apache.jdo.tck.query.api;

import java.util.Arrays;

import javax.jdo.JDOUserException;
import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Set Result.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6-16.
 *<BR>
 *<B>Assertion Description: </B>
 * void setResult (String result); Specify the results of the query 
 * if not instances of the candidate class.
 */
public class SetResult extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6-16 (SetResult) failed: ";
    
    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        Arrays.asList(new Object[] {
                "emp1Last", "emp2Last", "emp3Last", "emp4Last", "emp5Last"})
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetResult.class);
    }
    
    /** */
    public void testPositive() {
        int index = 0;
        Query query = getPM().newQuery(Person.class);
        query.setResult("lastname");
        String singleStringQuery = "SELECT lastname FROM Person";
        executeJDOQuery(ASSERTION_FAILED, query, singleStringQuery, 
                false, null, expectedResult[index], true);
    }
    
    /** */
    public void testNegative() {
        Query query = getPM().newQuery(Person.class);
        try { 
            query.setResult("noname");
            query.compile();
            fail(ASSERTION_FAILED + "Compilation for query " +
                    "'SELECT noname FROM Person' " +
                    "succeeded, though the result clause is invalid.");
        } catch (JDOUserException e) {
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
