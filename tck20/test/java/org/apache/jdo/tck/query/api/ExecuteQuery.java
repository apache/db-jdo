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

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Execute Query
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.1-3.
 *<BR>
 *<B>Assertion Description: </B> The <code>Query.execute</code> methods execute
 * the query using the parameters and return the result, which is a collection
 * of instances that satisfy the boolean filter. Each parameter of the
 * <code>execute</code> method(s) is an object which is either the value of the
 * corresponding parameter or the wrapped value of a primitive parameter.
 */

public class ExecuteQuery extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.1-3 (ExecuteQuery) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ExecuteQuery.class);
    }

    /** */
    public void test() {
        pm = getPM();

        initDatabase(pm, PCPoint.class);
        runTestExecuteQuery01(pm);
        runTestExecuteQuery02(pm);
        runTestExecuteQuery03(pm);
        
        pm.close();
        pm = null;
    }

    /** */
    private void runTestExecuteQuery01(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter("x == 4");
            Object results = query.execute();

            // check query result
            List expected = new ArrayList();
            Object p5 = new PCPoint(4, 4);
            expected.add(p5);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
            tx.commit();
            tx = null;
            if (debug) logger.debug ("Test ExecuteQuery01 - Passed\n");
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestExecuteQuery02(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters("Integer param");
            query.setFilter("x == param");
            Object results = query.execute(new java.lang.Integer(2));

            // check query result
            List expected = new ArrayList();
            Object p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
            tx.commit();
            tx = null;
            if (debug) logger.debug("Test ExecuteQuery02 - Passed\n");
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    private void runTestExecuteQuery03(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters("Integer param1, Integer param2");
            query.setFilter("x == param1 && y == param2");
            Object results = query.execute(new Integer(2), new Integer(2));

            // check query result
            List expected = new ArrayList();
            Object p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
            tx.commit();
            tx = null;
            if (debug) logger.debug("Test ExecuteQuery03 - Passed\n");
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}

