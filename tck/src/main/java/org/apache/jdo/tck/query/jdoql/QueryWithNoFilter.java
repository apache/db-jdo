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


import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Query with no Filter
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-1.
 *<BR>
 *<B>Assertion Description: </B> If the <code>Query</code> filter is not
 *specified, then it defaults to <code>true</code>, which has the effect of
 *filtering the input <code>Collection</code> only for class type.
 */

public class QueryWithNoFilter extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-1 (QueryWithNoFilter) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(QueryWithNoFilter.class);
    }

    /** */
    public void testPositive() {
        PersistenceManager pm = getPM();
        if(debug) logger.debug("\nExecuting test QueryWithNoFilter() ...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query<PCPoint> query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            Object results = query.execute();

            // check query result
            List<PCPoint> expected = new ArrayList<>();
            PCPoint p1 = new PCPoint(0, 0);
            PCPoint p2 = new PCPoint(1, 1);
            PCPoint p3 = new PCPoint(2, 2);
            PCPoint p4 = new PCPoint(3, 3);
            PCPoint p5 = new PCPoint(4, 4);
            expected.add(p1);
            expected.add(p2);
            expected.add(p3);
            expected.add(p4);
            expected.add(p5);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);

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
