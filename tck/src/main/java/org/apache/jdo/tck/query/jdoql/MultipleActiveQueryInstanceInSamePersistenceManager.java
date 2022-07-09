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

package org.apache.jdo.tck.query.jdoql;


import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Multiple Active Query Instances in Same PersistenceMana
ger
 *<BR>
 *<B>Keywords:</B> query concurrency
 *<BR>
 *<B>Assertion ID:</B> A14.3-1.
 *<BR>
 *<B>Assertion Description: </B> Multiple JDO <code>Query</code> instances might
 *be active simultaneously in the same JDO <code>PersistenceManager</code>
 *instance.
 */

public class MultipleActiveQueryInstanceInSamePersistenceManager extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.3-1 (MultipleActiveQueryInstanceInSamePersistenceManager) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(MultipleActiveQueryInstanceInSamePersistenceManager.class);
    }

    public void testPositive() {
        PersistenceManager pm = getPM();
        if (debug) 
            logger.debug("\nExecuting test MultipleActiveQueryInstanceInSamePersistenceManager()...");

        executeQueries(pm);

        if (debug)
            logger.debug("Test MultipleActiveQueryInstanceInSamePersistenceManager: Passed");
    }

    /** */
    void executeQueries(PersistenceManager pm) {
        // query selecting all point instances
        pm.currentTransaction().begin();
        Query<PCPoint> query = pm.newQuery();
        query.setClass(PCPoint.class);
        query.setCandidates(pm.getExtent(PCPoint.class, false));
        
        // query selecting point with x value 0
        Query<PCPoint> query2 = pm.newQuery();
        query2.setClass(PCPoint.class);
        query2.setCandidates(pm.getExtent(PCPoint.class, false));
        query2.setFilter("x == 0");
        
        // execute first query
        Object results = query.execute();
        
        // check query result of first query
        List<PCPoint> expected = new ArrayList<>();
        expected.add(new PCPoint(0, 0));
        expected.add(new PCPoint(1, 1));
        expected.add(new PCPoint(2, 2));
        expected.add(new PCPoint(3, 3));
        expected.add(new PCPoint(4, 4));
        expected = getFromInserted(expected);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == 0",
                results, expected);
        
        // execute second query
        Object results2 = query2.execute();
        
        // check query result of second query
        List<PCPoint> expected2 = new ArrayList<>();
        expected2.add(new PCPoint(0, 0));
        expected2 = getFromInserted(expected2);
        checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == 0",
                results2, expected2);
        pm.currentTransaction().commit();
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(PCPoint.class);
        loadAndPersistPCPoints(getPM());
    }
}

