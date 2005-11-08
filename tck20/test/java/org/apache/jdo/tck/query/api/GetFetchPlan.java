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
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetFetchPlan.class);
    }
    
    /** */
    public void testPositive() {
        Query query = getPM().newQuery(PCClass.class, "number1 == param");
        query.declareParameters("int param");
        
        checkSameFetchPlanInstances(query);
        checkDefaultFetchGroup(query);
        checkFetchGroup2(query);
        checkDefaultFetchGroup(query);
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
     * assigned to the default fetch group.
     * @param query the query
     */
    private void checkDefaultFetchGroup(Query query) {
        Transaction transaction = query.getPersistenceManager().
            currentTransaction();
        transaction.begin();
        Collection result = (Collection) query.execute(new Integer(10));
        if (result.size() != 1) {
            fail(ASSERTION_FAILED + "Query returned " + result.size() + 
                    " instances, expected size is " + 1);
        }
        PCClass pcClass = (PCClass) result.iterator().next();
        if (pcClass.getTransientNumber1() != 10) {
            fail(ASSERTION_FAILED + 
                    "Field PCClass.number1 is in the " +
                    "default fetch group and should have been loaded. " +
                    "The jdoPostLoad() callback has copied the field value " +
                    "to a transient field which has an unexpected value: " + 
                    pcClass.getTransientNumber1());
        }
        if (pcClass.getTransientNumber2() != 0) {
            fail(ASSERTION_FAILED + 
                    "Field PCClass.number2 is not in the " +
                    "default fetch group and should not have been loaded. " +  
                    "The jdoPostLoad() callback has copied the field value " +
                    "to a transient field which has an unexpected value: " + 
                    pcClass.getTransientNumber2());
        }
        transaction.commit();
    }
    
    /**
     * Checks if the given query loads fields assigned 
     * to the default fetch group plus fetch group "fetchGroup2".
     * For this purpose, the method temporarily adds fetch group "fetchGroup2"
     * to the fetch plan of the given query instance. 
     * That fetch group is assigned a different field 
     * than the default fetch group. 
     * Finally, that fetch group is removed from the fetch plan again.
     * @param query the query
     */
    private void checkFetchGroup2(Query query) {
        String fetchGoupName = "fetchGroup2";
        query.getFetchPlan().addGroup(fetchGoupName);
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
            if (pcClass.getTransientNumber1() != 20) {
                fail(ASSERTION_FAILED + 
                        "Field PCClass.number1 is in the " +
                        "default fetch group and should have been loaded. " +
                        "The jdoPostLoad() callback has copied the field value " +
                        "to a transient field which has an unexpected value: " + 
                        pcClass.getTransientNumber1());
            }
            if (pcClass.getTransientNumber2() != 20) {
                fail(ASSERTION_FAILED + 
                        "Field PCClass.number1 is in " +
                        "fetch group 2 and should have been loaded. " +
                        "The jdoPostLoad() callback has copied the field value " +
                        "to a transient field which has an unexpected value: " + 
                        pcClass.getTransientNumber2());
            }
            transaction.commit();
        } finally {
            query.getFetchPlan().removeGroup(fetchGoupName);
        }
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadMylib(getPM(), MYLIB_TESTDATA);
        addTearDownClass(MylibReader.getTearDownClasses());
    }
}
