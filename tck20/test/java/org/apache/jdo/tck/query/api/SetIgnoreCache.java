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

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Set IgnoreCache
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6-12.
 *<BR>
 *<B>Assertion Description: </B> <code>Query.setIgnoreCache(boolean flag)</code> 
 *sets the IgnoreCache option for queries.
 */

public class SetIgnoreCache extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6-12 (SetIgnoreCache) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetIgnoreCache.class);
    }

    /** */
    public void test() {
        pm = getPM();

        runTestSetIgnoreCache01(pm);
        runTestSetIgnoreCache02(pm);

        pm.close();
        pm = null;
    }

    /** */
    void runTestSetIgnoreCache01(PersistenceManager pm)  {
        Transaction tx = pm.currentTransaction();
        tx.setOptimistic(false);
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));

            if (debug)
                logger.debug("Pessimistic: IgnoreCache - Setting value = true");
            query.setIgnoreCache(true);
            if(query.getIgnoreCache()) {
                if (debug)
                    logger.debug("Pessimistic: IgnoreCache - value = " +
                                 query.getIgnoreCache());
            } 
            else {
                fail(ASSERTION_FAILED,
                     "query.getIgnoreCache() returns false after setting the flag to true");
            }

            if (debug)
                logger.debug("Pessimistic: IgnoreCache - Setting value = false");
            query.setIgnoreCache(false);
            if(!query.getIgnoreCache()) {
                if (debug)
                    logger.debug("Pessimistic: IgnoreCache - value = " + 
                                 query.getIgnoreCache());
            } 
            else {
                fail(ASSERTION_FAILED,
                     "query.getIgnoreCache() returns true after setting the flag to false");
            }
            query.compile();

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    void runTestSetIgnoreCache02(PersistenceManager pm) {
        if (!isOptimisticSupported()) {
            if (debug) logger.debug("Optimistic tx not supported");
            return;
        }
        
        Transaction tx = pm.currentTransaction();
        try {
            tx.setOptimistic(true);
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));

            if (debug)
                logger.debug("Optimistic: IgnoreCache - Setting value = true");
            query.setIgnoreCache(true);
            if(query.getIgnoreCache()) {
                if (debug) 
                    logger.debug("Optimistic: IgnoreCache - value = " +
                                 query.getIgnoreCache());
            }
            else {
                fail(ASSERTION_FAILED,
                     "query.getIgnoreCache() returns false after setting the flag to true");
            }

            if (debug)
                logger.debug("Optimistic: IgnoreCache - Setting value = false");
            query.setIgnoreCache(false);
            if(!query.getIgnoreCache()) {
                if (debug) 
                    logger.debug("Optimistic: IgnoreCache - value = " +
                                 query.getIgnoreCache());
            } 
            else {
                fail(ASSERTION_FAILED,
                     "query.getIgnoreCache() returns true after setting the flag to false");
            }
            
            query.compile();
        
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}

