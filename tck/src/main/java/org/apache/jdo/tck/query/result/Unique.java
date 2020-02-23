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

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.company.QPerson;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Unique.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.11-1.
 *<BR>
 *<B>Assertion Description: </B>
 * When the value of the Unique flag is true, 
 * then the result of a query is a single value, 
 * with null used to indicate that none of the instances 
 * in the candidates satisfied the filter. 
 * If more than one instance satisfies the filter, 
 * and the range is not limited to one result, 
 * then execute throws a JDOUserException.
 */
public class Unique extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.11-1 (Unique) failed: ";
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(Unique.class);
    }
    
    /** */
    public void testPositive0() {
        Object expected = getTransientCompanyModelInstance("emp1");

        JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
        QPerson cand = QPerson.candidate();
        query.filter(cand.personid.eq(1l));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      Boolean.TRUE,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Person.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "personid == 1",
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
    public void testPositive1() {
        Object expected = null;

        JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
        QPerson cand = QPerson.candidate();
        query.filter(cand.personid.eq(0l));

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      Boolean.TRUE,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Person.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "personid == 0",
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
    public void testPositive2() {
        Object expected = getTransientCompanyModelInstance("emp1");

        JDOQLTypedQuery<Person> query = getPM().newJDOQLTypedQuery(Person.class);
        QPerson cand = QPerson.candidate();
        query.orderBy(cand.personid.asc());

        QueryElementHolder holder = new QueryElementHolder(
                /*UNIQUE*/      Boolean.TRUE,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        Person.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       null,
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    "personid ASCENDING",
                /*FROM*/        "0",
                /*TO*/          "1",
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    public void testNegative() {
        String singleString = "SELECT UNIQUE FROM " + Person.class.getName();
        
        Query query = getPM().newQuery(Person.class);
        query.setUnique(true);
        executeJDOQuery(ASSERTION_FAILED, query, singleString, 
                false, null, null, false);
        
        query = getPM().newQuery(singleString);
        executeJDOQuery(ASSERTION_FAILED, query, singleString, 
                false, null, null, false);
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
