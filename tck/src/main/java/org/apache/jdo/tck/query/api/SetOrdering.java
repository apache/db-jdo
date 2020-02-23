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
import java.util.ListIterator;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Set Ordering
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6-11.
 *<BR>
 *<B>Assertion Description: </B>
<code>Query.setOrdering(String ordering)</code> binds the ordering statements
to the query instance.

 */

public class SetOrdering extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6-11 (SetOrdering) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetOrdering.class);
    }

    /** */
    public void testPositive() {
        PersistenceManager pm = getPM();

        runTestAscending(pm);
        runTestDescending(pm);
    }

    /** */
    void runTestAscending(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            //ascending
            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setOrdering("x ascending");
            Object results = query.execute();

            // check result
            printOutput(results, inserted);
            checkQueryResultWithOrder(ASSERTION_FAILED, results, inserted);

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
    

    /** */
    void runTestDescending(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        Class clazz = PCPoint.class;
        try {
            tx.begin();

            //descending
            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setOrdering("x descending");
            Object results = query.execute();
            
            // check result
            List expected = new ArrayList();
            ListIterator li = inserted.listIterator(inserted.size());
            // construct expected results by iterating inserted objects backwards
            while (li.hasPrevious()) {
                Object obj = li.previous();
                expected.add(obj);
            }
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithOrder(ASSERTION_FAILED, results, expected);

            tx.commit();
            tx = null;
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
