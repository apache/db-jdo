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

package org.apache.jdo.tck.query.jdoql.methods;

import java.util.Collections;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOQLTypedQuery;

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
 * <li> isEmpty()
 * <li> size()
 * </ul>
 */
public class SupportedMapMethods extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-46 (SupportedMapMethods) failed: ";
            
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
        Object expected = getTransientCompanyModelInstancesAsList("emp1");

        JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
        QPerson cand = QPerson.candidate();
        query.filter(cand.phoneNumbers.get("home").eq("1111"));

        QueryElementHolder holder = new QueryElementHolder(
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }
    
    /** */
    public void testContainsKey() {
        Object expected = getTransientCompanyModelInstancesAsList(
                "emp1", "emp2", "emp3", "emp4", "emp5");

        JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
        QPerson cand = QPerson.candidate();
        query.filter(cand.phoneNumbers.containsKey("home"));

        QueryElementHolder holder = new QueryElementHolder(
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testContainsValue() {
        Object expected = getTransientCompanyModelInstancesAsList("emp1");

        JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
        QPerson cand = QPerson.candidate();
        query.filter(cand.phoneNumbers.containsValue("1111"));

        QueryElementHolder holder = new QueryElementHolder(
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
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testIsEmpty() {
        Object expected = Collections.emptyList();

        JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
        QPerson cand = QPerson.candidate();
        query.filter(cand.phoneNumbers.isEmpty());

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Person.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "phoneNumbers.isEmpty()",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testSize() {
        Object expected = getTransientCompanyModelInstancesAsList(
                "emp1", "emp2", "emp3", "emp4", "emp5");

        JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
        QPerson cand = QPerson.candidate();
        query.filter(cand.phoneNumbers.size().eq(2));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Person.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "phoneNumbers.size() == 2",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
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
