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
 *<B>Title:</B> Set Candidate Collection
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6-5.
 *<BR>
 *<B>Assertion Description: </B> <code>Query.setCandidates(Collection
 *candidateCollection)</code> binds the candidate <code>Collection</code>
 *to the query instance.
 */

public class SetCandidateCollection extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6-5 (SetCandidateCollection) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetCandidateCollection.class);
    }

    /** */
    public void test() {
        pm = getPM();

        initDatabase(pm, PCPoint.class);
        runTestSetCandidateCollection(pm);

        pm.close();
        pm = null;
    }

    /** */
    void runTestSetCandidateCollection(PersistenceManager pm) {
        if (debug) logger.debug("\nExecuting test SetCandidateCollection()...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(inserted);
            Object results = query.execute();
        
            // check query result
            List expected = new ArrayList();
            Object p1 = new PCPoint(0, 0);
            Object p2 = new PCPoint(1, 1);
            Object p3 = new PCPoint(2, 2);
            Object p4 = new PCPoint(3, 3);
            Object p5 = new PCPoint(4, 4);
            expected.add(p1);
            expected.add(p2);
            expected.add(p3);
            expected.add(p4);
            expected.add(p5);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
            if (debug) logger.debug("Test SetCandidateCollection: Passed");

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}

