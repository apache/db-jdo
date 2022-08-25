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


import javax.jdo.JDOFatalUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Execute Query when Persistence Manager is Closed
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.1-1.
 *<BR>
 *<B>Assertion Description: </B> If the <code>PersistenceManager</code> which constructed the <code>Query</code> is closed, then the <code>Query.execute</code> methods throw <code>JDOFatalUserException</code>.
 */

public class ExecuteQueryWhenPersistenceManagerIsClosed extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.1-1 (ExecuteQueryWhenPersistenceManagerIsClosed) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ExecuteQueryWhenPersistenceManagerIsClosed.class);
    }

    /** */
    public void test() {
        pm = getPM();
        
        runTestExecuteQueryWhenPersistenceManagerIsClosed(pm);
        
        //pm.close();
        pm = null;
    }

    /** */
    void runTestExecuteQueryWhenPersistenceManagerIsClosed(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter("x == 4");
            query.compile();
            Object results = query.execute();
            tx.commit();
            tx = null;
            pm.close();

            try {
                Object result = query.execute();
                fail(ASSERTION_FAILED,
                     "Query.execute after pm is closed should throw JDOUserException.");
            } 
            catch (JDOFatalUserException ex) {
                // expected exception
                if (debug) logger.debug("expected exception " + ex);
            }
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }        
    }
}

