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

import java.util.Collection;

import javax.jdo.FetchPlan;
import javax.jdo.Query;

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

    private static final String FETCH_GROUP_1 = "fetchGroup1";
    private static final String FETCH_GROUP_2 = "fetchGroup2";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetFetchPlan.class);
    }

    /** */
    private Query<PCClass> createQuery() {
        Query<PCClass> query = getPM().newQuery(PCClass.class, "true");
        query.getFetchPlan().setGroup(FETCH_GROUP_1);
        return query;
    }

    /** */
    public void testFetchGroup1() {
        // localSetUp closes the PM
        Query<PCClass> query = createQuery();
        checkSameFetchPlanInstances(query);
        checkFetchGroup1(query);
        cleanupPM();
    }

    public void testFetchGroup2() {
        // localSetUp closes the PM
        Query<PCClass> query = createQuery();
        checkFetchGroup2(query);
        checkFetchGroup1(query);
    }

    private void checkSameFetchPlanInstances(Query<PCClass> query) {
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
    private void checkFetchGroup1(Query<PCClass> query) {
        FetchPlan fetchplan = query.getFetchPlan();
        Collection<String> fetchgroups = fetchplan.getGroups();
        assertTrue("FetchPlan should include fetchGroup1 and not fetchGroup2",
                fetchgroups.contains(FETCH_GROUP_1) && 
                !fetchgroups.contains(FETCH_GROUP_2));
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
    private void checkFetchGroup2(Query<PCClass> query) {
        FetchPlan fetchplan = query.getFetchPlan();
        fetchplan.addGroup(FETCH_GROUP_2);
        Collection<String> fetchgroups = fetchplan.getGroups();
        try {
            assertTrue("FetchPlan should include fetchGroup1 and fetchGroup2",
                       fetchgroups.contains(FETCH_GROUP_1) && 
                       fetchgroups.contains(FETCH_GROUP_2));
        } finally {
            query.getFetchPlan().removeGroup(FETCH_GROUP_2);
        }
    }
}
