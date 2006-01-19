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

import java.util.Collection;

import javax.jdo.FetchPlan;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PCClass;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Get Fetch Plan.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6-21.
 *<BR>
 *<B>Assertion Description: </B>
 * This method retrieves the fetch plan associated with the Query. 
 * It always returns the identical instance for the same Query instance. 
 * Any change made to the fetch plan affects subsequent query execution.
 */
public class GetFetchPlan extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6-21 (FetchPan) failed: ";

    private String FETCH_GROUP_1 = "fetchGroup1";
    private String FETCH_GROUP_2 = "fetchGroup2";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetFetchPlan.class);
    }

    /** */
    private Query createQuery() {
        // initialize the PM with datastore transactions and no retain values
        getPM().currentTransaction().setOptimistic(false);
        getPM().currentTransaction().setRetainValues(false);
        Query query = getPM().newQuery(PCClass.class, "number1 == param");
        query.declareParameters("int param");
        query.getFetchPlan().setGroup(FETCH_GROUP_1);
        return query;
    }

    /** */
    public void testFetchGroup1() {
        // localSetUp closes the PM
        Query query = createQuery();
        checkSameFetchPlanInstances(query);
        checkFetchGroup1(query);
        cleanupPM();
    }

    public void testFetchGroup2() {
        // localSetUp closes the PM
        Query query = createQuery();
        checkFetchGroup2(query);
        checkFetchGroup1(query);
    }

    private void checkSameFetchPlanInstances(Query query) {
        FetchPlan fetchPlan1 = query.getFetchPlan();
        FetchPlan fetchPlan2 = query.getFetchPlan();
        if (fetchPlan1 != fetchPlan2) {
            fail(ASSERTION_FAILED + "Calling getFetchPlan twice on the same " +
            "query instance results in two different fetch plan instances.");
        }
    }
    
    /**
     * Checks if the given query loads fields
     * assigned to fetchGroup1
     * @param query the query
     */
    private void checkFetchGroup1(Query query) {
        FetchPlan fetchplan = query.getFetchPlan();
        Collection fetchgroups = fetchplan.getGroups();
        assertTrue("FetchPlan should include fetchGroup1 and not fetchGroup2",
                fetchgroups.contains(FETCH_GROUP_1) && 
                !fetchgroups.contains(FETCH_GROUP_2));
        Transaction transaction = query.getPersistenceManager().
            currentTransaction();
        transaction.begin();
        Collection result = (Collection) query.execute(new Integer(10));
        if (result.size() != 1) {
            fail(ASSERTION_FAILED + "Query returned " + result.size() + 
                    " instances, expected size is " + 1);
        }
        PCClass pcClass = (PCClass) result.iterator().next();
        int transient1 = pcClass.getTransientNumber1();
        int transient2 = pcClass.getTransientNumber2();
        boolean field1loaded = transient1 == 10;
        boolean field2loaded = transient2 == 10;
        transaction.commit();

        if (!field1loaded || field2loaded) {
            fail(ASSERTION_FAILED +
                    "\nUnexpected: TransientNumber1 = " + transient1 +
                    ", and TransientNumber2 = " + transient2 + ".\n" +
                    "Field number1 loaded = " + field1loaded + 
                    ", Field number2 loaded = " + field2loaded + ".\n" +
                    "With fetchGroup1 active, expect field number1 " +
                    " loaded and field number2 not loaded.");
        }
    }
    
    /**
     * Checks if the given query loads fields assigned 
     * to "fetchGroup1" plus fetch group "fetchGroup2".
     * For this purpose, the method temporarily adds fetch group "fetchGroup2"
     * to the fetch plan of the given query instance. 
     * That fetch group loads field number2. 
     * Finally, that fetch group is removed from the fetch plan again.
     * @param query the query
     */
    private void checkFetchGroup2(Query query) {
        FetchPlan fetchplan = query.getFetchPlan();
        fetchplan.addGroup(FETCH_GROUP_2);
        Collection fetchgroups = fetchplan.getGroups();
        assertTrue("FetchPlan should include fetchGroup1 and fetchGroup2",
                fetchgroups.contains(FETCH_GROUP_1) && 
                fetchgroups.contains(FETCH_GROUP_2));
        try {
            Transaction transaction = query.getPersistenceManager().
                currentTransaction();
            transaction.begin();
            Collection result = (Collection) query.execute(new Integer(20));
            if (result.size() != 1) {
                fail(ASSERTION_FAILED + "Query returned " + result.size() + 
                        " instances, expected size is " + 1);
            }
            PCClass pcClass = (PCClass) result.iterator().next();
            int transient1 = pcClass.getTransientNumber1();
            int transient2 = pcClass.getTransientNumber2();
            boolean field1loaded = transient1 == 20;
            boolean field2loaded = transient2 == 20;
            transaction.commit();

            if (!field1loaded || !field2loaded) {
                fail(ASSERTION_FAILED +
                        "\nUnexpected: TransientNumber1 = " + transient1 +
                        ", and TransientNumber2 = " + transient2 + ".\n" +
                        "Field number1 loaded = " + field1loaded + 
                        ", Field number2 loaded = " + field2loaded + ".\n" +
                        "With fetchGroup1 and fetchGroup2 active, expect" +
                        " field number1 loaded and field number2 loaded.");
            }
        } finally {
            query.getFetchPlan().removeGroup(FETCH_GROUP_2);
        }
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(MylibReader.getTearDownClasses());
        loadAndPersistMylib(getPM());
        cleanupPM();
    }
}
