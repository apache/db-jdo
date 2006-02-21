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


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
 *<B>Title:</B> New Query From Existing Serialized Query
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.5-2.
 *<BR>
 *<B>Assertion Description: </B> <code>PersistenceManager.newQuery(Object query)</code> constructs a <code>Query</code> instance from another query, where the parameter is a serialized/re-stored <code>Query</code> instance from the same JDO vendor but a different execution environment.  Any of the elements Class, Filter, Import declarations, Variable declarations, Parameter declarations, and Ordering from the parameter <code>Query</code> are copied to the new <code>Query</code> instance, but a candidate <code>Collection</code> or <code>Extent</code> element is discarded.
 */

public class NewQueryFromRestoredSerializedQuery extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.5-2 (NewQueryFromRestoredSerializedQuery) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NewQueryFromRestoredSerializedQuery.class);
    }

    /** */
    public void testPositive() throws Exception {
        PersistenceManager pm = getPM();
        if (debug)
            logger.debug("\nExecuting test NewQueryFromRestoredSerializedQuery01() ...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter("x == 3");
            query.compile();

            ObjectOutputStream oos = null;
            try {
                if (debug) 
                    logger.debug("Attempting to serialize Query object.");
                oos = new ObjectOutputStream( new FileOutputStream(SERIALZED_QUERY));
                oos.writeObject(query);
                if (debug) logger.debug("Query object serialized.");
            } 
            finally {
                if (oos != null) {
                    try { oos.flush();} catch(Exception ex) {}
                    try { oos.close();} catch(Exception ex) {}
                }
            }

            ObjectInputStream in = 
                new ObjectInputStream(new FileInputStream(SERIALZED_QUERY));
            Query query1 = (Query)in.readObject();
            
            // init and execute query
            query = pm.newQuery(query1);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            Object results = query.execute();

            // check query result
            List expected = new ArrayList();
            Object p4 = new PCPoint(3, 3);
            expected.add(p4);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == 3", 
                    results, expected);
            if (debug)
                logger.debug("Test NewQueryFromRestoredSerializedQuery01(): Passed");

            tx.commit();
            tx = null;
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
        loadAndPersistPCPoints(getPM());
    }
}

