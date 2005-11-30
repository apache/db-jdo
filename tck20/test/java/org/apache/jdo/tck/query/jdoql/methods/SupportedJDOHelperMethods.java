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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Supported JDOHelper methods.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-49.
 *<BR>
 *<B>Assertion Description: </B>
 * Supported JDOHelper methods:
 * <ul>
 * <li> JDOHelper.getObjectId(Object)
 * </ul>
 */
public class SupportedJDOHelperMethods extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-49 (SupportedJDOHelperMethods) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     */
    private static final QueryElementHolder[] VALID_QUERIES = {
        new QueryElementHolder(
                /*UNIQUE*/      null,
                /*RESULT*/      "JDOHelper.getObjectId(this)", 
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
                /*WHERE*/       "JDOHelper.getObjectId(this) == oid",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  "Object oid",
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
        BatchTestRunner.run(SupportedJDOHelperMethods.class);
    }
    
    /** */
    public void testGetObjectById() {
        // query 1
        int index = 0;
        List expectedResult = getExpectedResult(true, Person.class);
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                expectedResult);

        // query 2
        index = 1;
        expectedResult = getExpectedResult(false, Person.class, "personid == 1");
        // The query above returns a collection of size 1.
        // The collection element is a pc instances 
        // whose oid is the parameter of the query below.
        Object[] parameters = new Object[]{
                JDOHelper.getObjectId(expectedResult.get(0))};
        executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                parameters, expectedResult);
        executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES[index], 
                parameters, expectedResult);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadCompanyModel(getPM(), COMPANY_TESTDATA);
        addTearDownClass(CompanyModelReader.getTearDownClasses());
    }

    /** */
    private List getExpectedResult(boolean oidsWanted, Class candidateClass) {
        return getExpectedResult(oidsWanted, candidateClass, null);
    }
    
    /** */
    private List getExpectedResult(boolean oidsWanted, 
            Class candidateClass, String filter) {
        List expectedResult;
        PersistenceManager pm = getPM();
        Transaction transaction = pm.currentTransaction();
        transaction.begin();
        try {
            Query query = filter == null ? pm.newQuery(candidateClass) :
                pm.newQuery(candidateClass, filter);
            try {
                Collection result = (Collection) query.execute();
                if (oidsWanted) {
                    expectedResult = new ArrayList();
                    for (Iterator i = result.iterator(); i.hasNext(); ) {
                        expectedResult.add(JDOHelper.getObjectId(i.next()));
                    }
                } else {
                    expectedResult = new ArrayList(result);
                }
            } finally {
                query.closeAll();
            }
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return expectedResult;
    }

}
