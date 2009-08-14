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

import junit.framework.AssertionFailedError;

import javax.jdo.JDOFatalException;
import javax.jdo.JDOQueryInterruptedException;
import javax.jdo.JDOUnsupportedOptionException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PCPoint2;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.ThreadExceptionHandler;

/**
 *<B>Title:</B> QueryCancel
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.1-8
 *<BR>
 *<B>Assertion Description: </B>
These methods cancel a running query (or queries). The thread that has its query 
canceled will throw a JDOQueryInterruptedException.
If cancel is not supported (most likely due to the underlying implementation not 
supporting cancel) then JDOUnsupportedOptionException is thrown to the caller.
 */

public class QueryCancel extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.1-8 (QueryCancel) failed: ";

    /** Single String JDOQL Query to be canceled. */
    private static String SSJDOQL = 
        "select avg (this.x + point2.y) " +
        "from PCPoint " +
        "where this.y >= 0 && point2.x >= 0 " + 
        "variables PCPoint2 point2 " + 
        "import org.apache.jdo.tck.pc.mylib.PCPoint; " + 
        "import org.apache.jdo.tck.pc.mylib.PCPoint2; ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(QueryCancel.class);
    }

    /** */
    public void testCancel() throws Exception {
        PersistenceManager pm = getPM();
        Query query = pm.newQuery(SSJDOQL);

        // Thread executing the query
        ThreadExceptionHandler group = new ThreadExceptionHandler();
        QueryExecutor runnable = new QueryExecutor(pm, query);
        Thread t = new Thread(group, runnable, "Query Executor");
        t.start();

        try {
            // cancel query 
            query.cancel(t);
            if (!isQueryCancelSupported()) {
                fail(ASSERTION_FAILED,
                     "Query.cancel should throw a JDOQueryInterruptedException, " + 
                     "if query canceling is not supported ");
            }
        }
        catch (JDOUnsupportedOptionException ex) {
            if (isQueryCancelSupported()) {
                fail(ASSERTION_FAILED,
                     "Query.cancel should not result in a JDOQueryInterruptedException, " + 
                     "if query canceling is supported ");
            }
        }

        t.join();
        Throwable problem = group.getUncaughtException(t);
        if (problem != null) {
            if (problem instanceof AssertionFailedError)
                throw (AssertionFailedError)problem;
            else
                throw new JDOFatalException( "Thread " + t.getName()+ 
                                             " results in exception ", problem);
        }
    }

    /** */
    public void testCancelAll() throws Exception {
        PersistenceManager pm = getPM();
        Query query = pm.newQuery(SSJDOQL);

        // Thread executing the query
        ThreadExceptionHandler group = new ThreadExceptionHandler();
        QueryExecutor runnable = new QueryExecutor(pm, query);
        Thread t = new Thread(group, runnable, "Query Executor");
        t.start();

        try {
            // cancel query 
            query.cancelAll();
            if (!isQueryCancelSupported()) {
                fail(ASSERTION_FAILED,
                     "Query.cancel should throw a JDOQueryInterruptedException, " + 
                     "if query canceling is not supported ");
            }
        }
        catch (JDOUnsupportedOptionException ex) {
            if (isQueryCancelSupported()) {
                fail(ASSERTION_FAILED,
                     "Query.cancel should not result in a JDOQueryInterruptedException, " + 
                     "if query canceling is supported ");
            }
        }

        t.join();
        Throwable problem = group.getUncaughtException(t);
        if (problem != null) {
            if (problem instanceof AssertionFailedError)
                throw (AssertionFailedError)problem;
            else
                throw new JDOFatalException( "Thread " + t.getName()+ 
                                             " results in exception ", problem);
        }
    }

    /** Runnable class executing the query. */
    class QueryExecutor implements Runnable {

        PersistenceManager pm;
        Query query;
        
        QueryExecutor(PersistenceManager pm, Query query) {
            this.pm = pm;
            this.query = query;
        }

        public void run() {
            Transaction tx = pm.currentTransaction();
            try {
                tx.begin();
                Object result = query.execute();
                tx.commit();
                tx = null;
                if (isQueryCancelSupported()) {
                    fail(ASSERTION_FAILED,
                         "Query.execute should result in a JDOQueryInterruptedException, " +
                         "if query canceling is supported.");
                }
            }
            catch (JDOQueryInterruptedException ex) {
                if (!isQueryCancelSupported()) {
                    fail(ASSERTION_FAILED,
                         "Query.execute should not result in a JDOQueryInterruptedException, " + 
                         "if query canceling is not supported.");
                }
            }
            finally {
                if ((tx != null) && tx.isActive())
                    tx.rollback();
            }
        }
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(PCPoint.class);
        addTearDownClass(PCPoint2.class);

        // create PCPoint and PCPoint2 instances
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            for (int i = 0; i < 1000; i++) {
                PCPoint obj = new PCPoint(i, i);
                pm.makePersistent(obj);
            }
            for (int i = 0; i < 1000; i++) {
                PCPoint2 obj = new PCPoint2(i, i);
                pm.makePersistent(obj);
            }
            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}

