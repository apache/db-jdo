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

package org.apache.jdo.tck.query.sql;

import java.util.Arrays;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> New SQL Query.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.7-1.
 *<BR>
 *<B>Assertion Description: </B>
 * In this case, the factory method that takes the language string and 
 * Object is used: newQuery (String language, Object query). 
 * The language parameter is javax.jdo.query.SQL and 
 * the query parameter is the SQL query string.
 */
public class NewQuery extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.7-1 (NewQuery) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NewQuery.class);
    }
    
    /** The array of valid SQL queries. */
    private static final String[] VALID_SQL_QUERIES = {
        "SELECT firstname, lastname FROM {0}.persons WHERE personid = ?"
    };
    
    /** 
     * The expected results of valid SQL queries.
     */
    private Object[] expectedResult = {
        Arrays.asList(new Object[]{
                new Object[]{"emp1First", "emp1Last"}})
    };
            
    /** Parameters of valid SQL queries. */
    private static Object[][] parameters = {
        {Integer.valueOf(1)}
    };
            
    /** */
    public void testPositive() {
        if (isSQLSupported()) {
            for (int i = 0; i < VALID_SQL_QUERIES.length; i++) {
                executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[i],
                        null, null, true, parameters[i], expectedResult[i],
                        false);
            }
        }
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
    }
}
