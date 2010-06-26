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
 
package org.apache.jdo.tck.query.result;

import java.math.BigInteger;
import java.util.Arrays;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Test using methods in the result expression
 *<BR>
 *<B>Keywords:</B> query result
 *<BR>
 *<B>Assertion ID:</B>
 *<BR>
 *<B>Assertion Description: </B> 
 */
public class MethodsInResult extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion (MethodsInResult) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        // collection.size()
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "this.employees.size()",
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    "this.name ascending",
        /*FROM*/        null,
        /*TO*/          null),
        
        // map.size()
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "this.phoneNumbers.size()",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    "this.lastname ascending, this.firstname ascending",
        /*FROM*/        null,
        /*TO*/          null),

        // MAX(collection.size())
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "MAX(this.employees.size())",
        /*INTO*/        null, 
        /*FROM*/        Department.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    null,
        /*FROM*/        null,
        /*TO*/          null),

        // map.get()
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "this.phoneNumbers.get('home')",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    "this.lastname ascending, this.firstname ascending",
        /*FROM*/        null,
        /*TO*/          null),

        // String.substring()
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "this.firstname.substring(1,4)",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    "this.lastname ascending, this.firstname ascending",
        /*FROM*/        null,
        /*TO*/          null),

        // String.indexOf()
        new QueryElementHolder(
        /*UNIQUE*/      null,
        /*RESULT*/      "this.firstname.indexOf('First')",
        /*INTO*/        null, 
        /*FROM*/        Employee.class,
        /*EXCLUDE*/     null,
        /*WHERE*/       null,
        /*VARIABLES*/   null,
        /*PARAMETERS*/  null,
        /*IMPORTS*/     null,
        /*GROUP BY*/    null,
        /*ORDER BY*/    "this.lastname ascending, this.firstname ascending",
        /*FROM*/        null,
        /*TO*/          null)    
    };

    /** 
     * The expected results of valid queries.
     */
    private Object[] expectedResult = {
        // collection.size()
        Arrays.asList(new Object[] { new Integer(3), new Integer(2) }),
        // map.size()
        Arrays.asList(new Object[] { new Integer(2), new Integer(2), new Integer(2), new Integer(2), new Integer(2) }),
        // MAX(collection.size())
        new Integer(3),
        // map.get()
        Arrays.asList(new Object[] { "1111", "2222", "3333", "3343", "3363" }),
        // String.substring()
        Arrays.asList(new Object[] { "mp1", "mp2", "mp3", "mp4", "mp5" }),
        // String.indexOf()
        //Arrays.asList(new Object[] { new BigInteger("4"), new BigInteger("4"), new BigInteger("4"), new BigInteger("4"), new BigInteger("4") }),
        Arrays.asList(new Object[] { new Integer(4), new Integer(4), new Integer(4), new Integer(4), new Integer(4) }),
    };
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MethodsInResult.class);
    }

    public void testCollectionSizeInResult() throws Exception {
        int index = 0;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    public void testMapSizeInResult() throws Exception {
        int index = 1;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    public void testMaxAndSizeInResult() throws Exception {
        int index = 2;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    public void testMapGetInResult() throws Exception {
        int index = 3;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    public void testSubstringInResult() throws Exception {
        int index = 4;
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult[index]);
    }

    public void testIndexOfInResult() throws Exception {
        int index = 5;
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
