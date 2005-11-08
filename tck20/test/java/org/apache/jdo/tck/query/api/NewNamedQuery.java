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

import javax.jdo.JDOException;
import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> New Named Query.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.5-12.
 *<BR>
 *<B>Assertion Description: </B>
 * Construct a new query instance 
 * with the given candidate class from a named query.
 */
public class NewNamedQuery extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.5-12 (NewNamedQuery) failed: ";
    
    /** The expected results of valid queries. */
    private static Object[][] expectedResult = {
        {new FullName("emp1First", "emp1Last"), 
            new FullName("emp2First", "emp2Last"),
            new FullName("emp3First", "emp3Last"), 
            new FullName("emp4First", "emp4Last"),
            new FullName("emp5First", "emp5Last")},
        {new FullName("emp1First", "emp1Last")}
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NewNamedQuery.class);
    }
    
    /** */
    public void testPositive() {
        int index = 0;
        executeNamedQuery(Person.class, "validNotUnique", 
                false, expectedResult[index]);

        index = 1;
        executeNamedQuery(Person.class, "validUnique", 
                true, expectedResult[index]);
    }

    /** */
    public void testNegative() {
        executeNamedQuery(Person.class, "invalidUnique", true, null);
    }

    private void executeNamedQuery(Class candidateClass, String namedQuery,
            boolean isUnique, Object[] expectedResultValues) {
        Query query = getPM().newNamedQuery(candidateClass, namedQuery); 
        execute(ASSERTION_FAILED, query, "Named query " + namedQuery,
                isUnique, false, null, expectedResultValues);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadCompanyModel(getPM(), COMPANY_TESTDATA);
        addTearDownClass(CompanyModelReader.getTearDownClasses());
    }
}
