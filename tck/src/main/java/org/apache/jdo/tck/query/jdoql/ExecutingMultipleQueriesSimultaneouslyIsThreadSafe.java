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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.ThreadExceptionHandler;

/**
 *<B>Title:</B> Executing Multiple Queries Simultaneously is ThreadSafe
 *<BR>
 *<B>Keywords:</B> query concurrency
 *<BR>
 *<B>Assertion ID:</B> A14.3-2.
 *<BR>
 *<B>Assertion Description: </B>
Multiple queries might be executed simultaneously by different threads
(but the implementation might choose to execute them serially).
In either case, the execution must be thread safe.

 */

public class ExecutingMultipleQueriesSimultaneouslyIsThreadSafe
    extends MultipleActiveQueryInstanceInSamePersistenceManager {
    
    static int nrOfThreads = 20;
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.3-2 (ExecutingMultipleQueriesSimultaneouslyIsThreadSafe) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ExecutingMultipleQueriesSimultaneouslyIsThreadSafe.class);
    }

    /** */
    public void testPositive() {
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            if (debug) 
                logger.debug(getThreadName() + ": Starting " + 
                             nrOfThreads + " concurrent threads.");
            ThreadExceptionHandler group = new ThreadExceptionHandler();
            Thread[] threads = new Thread[nrOfThreads];
            for (int i = 0; i < nrOfThreads; i++) {
            	// Runnable r = new QueryExecuter(pm);
                Runnable r = 
                    new ExecutingMultipleQueriesSimultaneouslyIsThreadSafe().
                        getQueryExecuter(pm);
                Thread t = new Thread(group, r);
                t.setName("ThreadID-" + i);
                threads[i] = t;
                if (debug) logger.debug(getThreadName() + ": Running");
                t.start();
            }

            if (debug) 
                logger.debug(getThreadName() +
                             ": Waiting for threads to join...");
            for (int i = 0; i < nrOfThreads; i++) {
                try { threads[i].join(); } 
                catch (InterruptedException e) { }
            }
            if (debug) logger.debug(getThreadName() + ": All threads joined.");

            tx.commit();
            tx = null;
            
            // check unhandled exceptions
            Set uncaught = group.getAllUncaughtExceptions();
            if ((uncaught != null) && !uncaught.isEmpty()) {
                for (Iterator i = uncaught.iterator(); i.hasNext();) {
                    Map.Entry next = (Map.Entry)i.next();
                    Thread thread = (Thread)next.getKey();
                    Throwable problem = (Throwable)next.getValue();
                    if (debug) {
                        logger.debug("uncaught exception in thread " + thread +
                                     " stacktrace:");
                        problem.printStackTrace();
                    }
                    fail(ASSERTION_FAILED,
                         "Thread " + thread + ": uncaught exception " + problem);
                }
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    String getThreadName() {
        return "[" + Thread.currentThread().getName() + "]";
    }

    /** Will be removed. */
    Runnable getQueryExecuter(PersistenceManager pm) {
        return new QueryExecuter(pm);
    }

    /** Will be removed. */
    void executeQueries(PersistenceManager ignore) {
        pm = getPM();
        setInsertedObjects(pm);
        super.executeQueries(pm);
        pm.close();
        pm = null;
    }
    
    /** Will be removed. */
    void setInsertedObjects(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            Object results = query.execute();
            for (Iterator i=((Collection)results).iterator(); i.hasNext();) {
                inserted.add(i.next());
            }
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    class QueryExecuter implements Runnable {

        final PersistenceManager pm;

        /** */
        public QueryExecuter(PersistenceManager pm) {
            this.pm = pm;
        }
        
        /** */
        public void run() {
            ExecutingMultipleQueriesSimultaneouslyIsThreadSafe.this.executeQueries(pm);
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
