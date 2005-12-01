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

package org.apache.jdo.tck.query.delete;

import java.util.HashMap;
import java.util.Map;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Delete Persistent All.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.8-1, A14.8-2
 *<BR>
 *<B>Assertion Description: </B>
 * These methods delete the instances of affected classes 
 * that pass the filter, and all dependent instances. 
 * Affected classes are the candidate class and 
 * its persistence-capable subclasses.
 * 
 * The number of instances of affected classes that were deleted is returned. 
 * Embedded instances and dependent instances are not counted 
 * in the return value.
 */
public class DeletePersistentAll extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.8-1 (DeletePersistentAll) failed: ";
    
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
        /*WHERE*/       null,
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
        /*WHERE*/       "firstname == param",
        /*VARIABLES*/   null,
        /*PARAMETERS*/  "String param",
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null)
    };
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(DeletePersistentAll.class);
    }
    
    /** */
    public void testNoParametersAPI() {
        deletePersistentAllByAPIQuery(ASSERTION_FAILED, 
                VALID_QUERIES[0], null, 5);
    }
    
    /** */
    public void testNoParametersSingleString() {
        deletePersistentAllBySingleStringQuery(ASSERTION_FAILED, 
                VALID_QUERIES[0], null, 5);
    }
    
    /** */
    public void testObjectArrayParametersAPI() {
        Object[] parameters = new Object[] {"emp1First"};
        deletePersistentAllByAPIQuery(ASSERTION_FAILED, 
                VALID_QUERIES[1], parameters, 1);
    }
    
    /** */
    public void testObjectArrayParametersSingleString() {
        Object[] parameters = new Object[] {"emp1First"};
        deletePersistentAllBySingleStringQuery(ASSERTION_FAILED, 
                VALID_QUERIES[1], parameters, 1);
    }
    
    /** */
    public void testMapParametersAPI() {
        Map parameters = new HashMap();
        parameters.put("param", "emp1First");
        deletePersistentAllByAPIQuery(ASSERTION_FAILED, 
                VALID_QUERIES[1], parameters, 1);
    }
    
    /** */
    public void testMapParametersSingleString() {
        Map parameters = new HashMap();
        parameters.put("param", "emp1First");
        deletePersistentAllBySingleStringQuery(ASSERTION_FAILED, 
                VALID_QUERIES[1], parameters, 1);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadCompanyModel(getPM(), COMPANY_TESTDATA);
        addTearDownClass(CompanyModelReader.getTearDownClasses());
    }
}
