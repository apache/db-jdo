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
package org.apache.jdo.tck.query.api;

import java.util.Arrays;

import javax.jdo.Query;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> SaveAsNamedQuery.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6-22.
 *<BR>
 *<B>Assertion Description: </B>
 * The method "saveAsNamedQuery" allows a query to be saved as a named query for later execution.
 */
public class SaveAsNamedQuery extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6-22 (SaveAsNamedQuery) failed: ";
    
    private static final String SINGLE_STRING_QUERY =
        "SELECT firstname, lastname FROM org.apache.jdo.tck.pc.company.Person";

    /**
     * The expected results of valid queries.
     */
    private final Object[] expectedResult = {
        Arrays.asList(new Object[] {
                new FullName("emp1First", "emp1Last"), 
                new FullName("emp2First", "emp2Last"),
                new FullName("emp3First", "emp3Last"),
                new FullName("emp4First", "emp4Last"),
                new FullName("emp5First", "emp5Last")})
    };

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SaveAsNamedQuery.class);
    }

    /** */
    @SuppressWarnings("unchecked")
    public void testSave() {
        int index = 0;
        Query<Person> query = getPM().newQuery(SINGLE_STRING_QUERY);
        query.setResultClass(FullName.class);
        query.setRange(0, 5);
        query.setIgnoreCache(true);
        executeJDOQuery(ASSERTION_FAILED, query, SINGLE_STRING_QUERY, false, null, expectedResult[index], true);

        // Save query under this name
        String savedName = "MySavedName";
        query.saveAsNamedQuery(savedName);
        query.closeAll();

        // Retrieve via the name, and execute
        Query<Person> namedQuery = getPM().newNamedQuery(Person.class, savedName);
        assertNotNull(namedQuery);
        executeJDOQuery(ASSERTION_FAILED, namedQuery, SINGLE_STRING_QUERY, false, null, expectedResult[index], true);
        namedQuery.closeAll();
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
