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
 *<B>Title:</B> Parentheses Mark Operator Precedence
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-37.
 *<BR>
 *<B>Assertion Description: </B> Parentheses can be used to explicitly mark operator precedence.

 */

public class ParenthesesMarkOperatorPrecedence extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-37 (ParenthesesMarkOperatorPrecedence) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ParenthesesMarkOperatorPrecedence.class);
    }

    /** */
    public void testPositive() {
        PersistenceManager pm = getPM();

        runTestParenthesesMarkOperatorPrecedence01(pm);
        runTestParenthesesMarkOperatorPrecedence02(pm);
        runTestParenthesesMarkOperatorPrecedence03(pm);
        runTestParenthesesMarkOperatorPrecedence04(pm);
        runTestParenthesesMarkOperatorPrecedence05(pm);
        runTestParenthesesMarkOperatorPrecedence06(pm);
        runTestParenthesesMarkOperatorPrecedence07(pm);
        runTestParenthesesMarkOperatorPrecedence08(pm);
        runTestParenthesesMarkOperatorPrecedence09(pm);
    }

    /** */
    void runTestParenthesesMarkOperatorPrecedence01(PersistenceManager pm) {
        if(debug) 
            logger.debug("\nExecuting Test ParenthesesMarkOperatorPrecedence01...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter("x == (1 + 1)");
            Object results = query.execute();

            // check query result
            List expected = new ArrayList();
            Object p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == (1 + 1)",
                    results, expected);
            if(debug) 
                logger.debug("Test ParenthesesMarkOperatorPrecedence01 - Passed");

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    void runTestParenthesesMarkOperatorPrecedence02(PersistenceManager pm) {
        if(debug) 
            logger.debug("\nExecuting Test ParenthesesMarkOperatorPrecedence01...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter("x == (1 + 1) * 2");
            Object results = query.execute();

            // check query result
            List expected = new ArrayList();
            Object p5 = new PCPoint(4, 4);
            expected.add(p5);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == (1 + 1) * 2",
                    results, expected);
            if(debug) 
                logger.debug("Test ParenthesesMarkOperatorPrecedence02 - Passed");
            
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    void runTestParenthesesMarkOperatorPrecedence03(PersistenceManager pm) {
        if(debug)
            logger.debug("\nExecuting Test ParenthesesMarkOperatorPrecedence03...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter("x == (9 - 1) * (3 + 5) / 32");
            Object results = query.execute();

            // check query result
            List expected = new ArrayList();
            Object p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, 
                    "x == (9 - 1) * (3 + 5) / 32",
                    results, expected);
            if(debug)
                logger.debug("Test ParenthesesMarkOperatorPrecedence03 - Passed");
            
            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    void runTestParenthesesMarkOperatorPrecedence04(PersistenceManager pm) {
        if(debug)
            logger.debug("\nExecuting Test ParenthesesMarkOperatorPrecedence04...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter("x == 2 && y == 2");
            Object results = query.execute();

            // check query result
            List expected = new ArrayList();
            Object p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, "x == 2 && y == 2",
                    results, expected);
            if(debug)
                logger.debug("Test ParenthesesMarkOperatorPrecedence04 - Passed");

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    void runTestParenthesesMarkOperatorPrecedence05(PersistenceManager pm) {
        if(debug)
            logger.debug("\nExecuting Test ParenthesesMarkOperatorPrecedence05...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter("x == (1 + 1) || y == (1002 - 1000)");
        
            Object results = query.execute();

            // check query result
            List expected = new ArrayList();
            Object p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, 
                    "x == (1 + 1) || y == (1002 - 1000)", results, expected);
            if(debug)
                logger.debug("Test ParenthesesMarkOperatorPrecedence05 - Passed");

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    void runTestParenthesesMarkOperatorPrecedence06(PersistenceManager pm) {
        if(debug)
            logger.debug("\nExecuting Test ParenthesesMarkOperatorPrecedence06...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter("x == (1 * 2) && true");
            Object results = query.execute();

            // check query result
            List expected = new ArrayList();
            Object p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, 
                    "x == (1 * 2) && true", results, expected);
            if(debug)
                logger.debug("Test ParenthesesMarkOperatorPrecedence06 - Passed");

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    void runTestParenthesesMarkOperatorPrecedence07(PersistenceManager pm) {
        if(debug) logger.debug("\nExecuting Test ParenthesesMarkOperatorPrecedence07...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter("x == (10000 / 5000) || false");
            Object results = query.execute();

            // check query result
            List expected = new ArrayList();
            Object p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, 
                    "x == (10000 / 5000) || false", results, expected);
            if(debug) logger.debug("Test ParenthesesMarkOperatorPrecedence07 - Passed");

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    void runTestParenthesesMarkOperatorPrecedence08(PersistenceManager pm) {
        if(debug)
            logger.debug("\nExecuting Test ParenthesesMarkOperatorPrecedence08...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter("(x == 2) == true");
            Object results = query.execute();

            // check query result
            List expected = new ArrayList();
            Object p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, "(x == 2) == true",
                    results, expected);
            if(debug) logger.debug("Test ParenthesesMarkOperatorPrecedence08 - Passed");

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
   }

    /** */
    void runTestParenthesesMarkOperatorPrecedence09(PersistenceManager pm) {
        if(debug)
            logger.debug("\nExecuting Test ParenthesesMarkOperatorPrecedence09...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.setFilter("(x == ((21 - 1/1)/10 + 1)) | (false && true)");
            Object results = query.execute();

            // check query result
            List expected = new ArrayList();
            Object p4 = new PCPoint(3, 3);
            expected.add(p4);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, 
                    "(x == ((21 - 1/1)/10 + 1)) | (false && true)",
                    results, expected);
            if(debug)
                logger.debug("Test ParenthesesMarkOperatorPrecedence09 - Passed");

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

