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
 
package org.apache.jdo.tck.query;


import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> New Query
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion IDs:</B> A14.5-10.
 *<BR>
 *<B>Assertion Description: </B>
Construct a new query instance with the candidate Extent and filter 
specified; the candidate class is taken from the Extent.
 */

public class NewQueryWithExtentAndFilter extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.5-1 (NewQueryWithExtentAndFilter) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NewQueryWithExtentAndFilter.class);
    }

    /** */
    public void test() {
        pm = getPM();
        
        initDatabase(pm, PCPoint.class);
        runTestNewQuery(pm);

        pm.close();
        pm = null;
    }

    /** */
    void runTestNewQuery(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        try {
            Query query = pm.newQuery(pm.getExtent(PCPoint.class, false), "x == 1");
            Object results = query.execute();

            // check query result
            List expected = new ArrayList();
            Object pcp1 = new PCPoint(1, 1);
            expected.add(pcp1);
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
}

