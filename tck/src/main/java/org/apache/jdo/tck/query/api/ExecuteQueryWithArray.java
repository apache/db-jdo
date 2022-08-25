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


import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Execute Query with Array
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.1-6.
 *<BR>
 *<B>Assertion Description: </B> The <code>executeWithArray</code> method is
 *similar to the <code>execute</code> method, but takes its parameters from an
 *array instance. The array contains <code>Object</code>s, in which the 
 *positional <code>Object</code> is the value to use in the query for that
 *parameter. Unlike <code>execute</code>, there is no limit on the number of
 *parameters.
 */

public class ExecuteQueryWithArray extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.1-6 (ExecuteQueryWithArray) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ExecuteQueryWithArray.class);
    }

    /** */
    public void testPositive() {
        PersistenceManager pm = getPM();

        runTestExecuteQueryWithArray01(pm);
        runTestExecuteQueryWithArray02(pm);
    }

    /** */
    private void runTestExecuteQueryWithArray01(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            
            tx.begin();

            Query<PCPoint> query = pm.newQuery(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters("Integer param");
            query.setFilter("x == param");
        
            Object[] actualParams = { Integer.valueOf(2) };
            Object results = query.executeWithArray(actualParams);

            // check query result
            List<PCPoint> expected = new ArrayList<>();
            PCPoint p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == param", results, expected);
            
            tx.commit();
            tx = null;
            if (debug) logger.debug ("Test ExecuteQueryWithArray01 - Passed\n");
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestExecuteQueryWithArray02(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query<PCPoint> query = pm.newQuery(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters("Integer param1, Integer param2");
            query.setFilter("x == param1 && y == param2");
            
            Object[] actualParams = { Integer.valueOf(2), Integer.valueOf(2) };
            Object results = query.executeWithArray(actualParams);

            // check query result
            List<PCPoint> expected = new ArrayList<>();
            PCPoint p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED,
                    "x == param1 && y == param2", 
                    results, expected);
            tx.commit();
            tx = null;
            if (debug) logger.debug ("Test ExecuteQueryWithArray02 - Passed\n");
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
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

