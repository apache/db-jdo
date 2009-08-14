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

import javax.jdo.JDOQueryTimeoutException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.pc.mylib.PCPoint2;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> QueryTimeout
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.1-7
 *<BR>
 *<B>Assertion Description: </B>
If a non-null timeout value is specified, a running query will be interrupted if
it exceeds the execution time of the timeout value. The thread executing the query
will throw JDOQueryTimeoutException. Specifying a timeout value of 0 indicates 
that there is no timeout for this query (a setting of 0 overrides the default 
specified in the Persistence-Manager or the PersistenceManagerFactory).
 */

public class QueryTimeout extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.1-7 (QueryTimeout) failed: ";

    /** Single String JDOQL Query. */
    private static String SSJDOQL = 
        "select avg (this.x + point2.y) " +
        "from PCPoint " +
        "where this.y >= 0 && point2.x >= 0 " + 
        "variables PCPoint2 point2 " + 
        "import org.apache.jdo.tck.pc.mylib.PCPoint; " + 
        "import org.apache.jdo.tck.pc.mylib.PCPoint2; ";

    /** Timeout value. */
    private static Integer TIMEOUT_MILLIS = new Integer(10);

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(QueryTimeout.class);
    }

    /** Method to test setting query timeout. */
    public void testTimeout() throws Exception {
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        
        try {
            tx.begin();
            Query query = pm.newQuery(SSJDOQL);
            query.setTimeoutMillis(TIMEOUT_MILLIS);
            Object result = query.execute();
            tx.commit();
            tx = null;
            fail(ASSERTION_FAILED,
                 "Query.execute should result in a JDOQueryTimeoutException.");
        }
        catch (JDOQueryTimeoutException ex) {
            // expected exception
            if (debug) {
                logger.debug("caught expected exception " + ex);
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** Method to test setting query timeout to 0. */
    public void testZeroTimeout() throws Exception {
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        
        try {
            tx.begin();
            Query query = pm.newQuery(SSJDOQL);
            query.setTimeoutMillis(0);
            Object result = query.execute();
            tx.commit();
            tx = null;
        }
        catch (JDOQueryTimeoutException ex) {
            // setting the timeout to 0 should not result in an exception
            fail(ASSERTION_FAILED,
                 "Query.execute should not result in a JDOQueryTimeoutException.");
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

