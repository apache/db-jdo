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
 
package org.apache.jdo.tck.query.jdoql.parameters;


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
 *<B>Title:</B> Parameter Declared with Same Name as Field of Candidate
 Class
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.4-2.
 *<BR>
 *<B>Assertion Description: </B> A field of the candidate class of a
 *<code>Query</code> can be hidden if a parameter is declared with the same name.
 */

public class ParameterDeclaredWithSameNameAsFieldOfCandidateClass extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.4-2 (ParameterDeclaredWithSameNameAsFieldOfCandidateClass) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ParameterDeclaredWithSameNameAsFieldOfCandidateClass.class);
    }

    /** */
    public void testPositve() {
        PersistenceManager pm = getPM();

        runTestParameterDeclaredWithSameNameAsFieldOfCandidateClass01(pm);
        runTestParameterDeclaredWithSameNameAsFieldOfCandidateClass02(pm);
    }

    /** */
    void runTestParameterDeclaredWithSameNameAsFieldOfCandidateClass01(
        PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters("Integer x");
            query.setFilter("x == x");
            Object results = query.execute(new java.lang.Integer(2));

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
            checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == x", 
                    results, expected);
            if (debug) 
                logger.debug("\nTest ParameterDeclaredWithSameNameAsFieldOfCandidateClass - Passed");

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    void runTestParameterDeclaredWithSameNameAsFieldOfCandidateClass02(
        PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters("Integer y");
            query.setFilter("y == y");
            Object results = query.execute(new java.lang.Integer(2));

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
            checkQueryResultWithoutOrder(ASSERTION_FAILED, "y == y",
                    results, expected);
            if (debug) 
                logger.debug("\nTest ParameterDeclaredWithSameNameAsFieldOfCandidateClass - Passed");
            
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

