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


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Execute Query with Map
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.1-5.
 *<BR>
 *<B>Assertion Description: </B> The <code>Query.executeWithMap</code> method is
 *similar to the <code>execute</code> method, but takes its parameters from a
 *<code>Map</code> instance. The <code>Map</code> contains key/value pairs,
 *in which the key is the declared parameter name, and the value is the value
 *to use in the query for that parameter. Unlike <code>execute</code>, there is
 *no limit on the number of parameters.
 */

public class ExecuteQueryWithMap extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.1-5 (ExecuteQueryWithMap) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ExecuteQueryWithMap.class);
    }

    /** */
    public void testPositive() {
        PersistenceManager pm = getPM();

        runTestExecuteQueryWithMap01(pm);
        runTestExecuteQueryWithMap02(pm);
    }

    /** */
    private void runTestExecuteQueryWithMap01(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters("Integer param");
            query.setFilter("x == param");
        
            Map actualParams = new java.util.HashMap();
            actualParams.put("param", new Integer(2) );
            Object results = query.executeWithMap(actualParams);

            // check query result
            List expected = new ArrayList();
            Object p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
            tx.commit();
            tx = null;
            if (debug) logger.debug("Test ExecuteQueryWithMap01 - Passed\n");
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestExecuteQueryWithMap02(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters("Integer param1, Integer param2");
            query.setFilter("x == param1 && y == param2");
        
            Map actualParams = new java.util.HashMap();
            actualParams.put("param1", new Integer(2) );
            actualParams.put("param2", new Integer(2) );
            Object results = query.executeWithMap(actualParams);

            // check query result
            List expected = new ArrayList();
            Object p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
            tx.commit();
            tx = null;
            if (debug) logger.debug("Test ExecuteQueryWithMap02 - Passed\n");
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        loadAndPersistPCPoints(getPM());
        addTearDownClass(PCPoint.class);
    }
}

