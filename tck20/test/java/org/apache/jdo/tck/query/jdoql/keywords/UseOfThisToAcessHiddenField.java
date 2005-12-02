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
 
package org.apache.jdo.tck.query.jdoql.keywords;

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
 *<B>Title:</B> Use of this to Access Hidden Field
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.4-4.
 *<BR>
 *<B>Assertion Description: </B> A hidden field may be accessed using the
 *<code>'this'</code> qualifier: <code>this.fieldName</code>.
 */

public class UseOfThisToAcessHiddenField extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.4-4 (UseOfThisToAcessHiddenField) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(UseOfThisToAcessHiddenField.class);
    }

    /** */
    public void testPositve() {
        PersistenceManager pm = getPM();

        runTestUseOfThisToAcessHiddenField01(pm);
        runTestUseOfThisToAcessHiddenField02(pm);
    }

    /** */
    void runTestUseOfThisToAcessHiddenField01(PersistenceManager pm) {
        if (debug) 
            logger.debug("\nExecuting test UseOfThisToAcessHiddenField01() ...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters("Integer x");
            query.setFilter("this.x == x");
            Object results = query.execute(new java.lang.Integer(2));

            // check query result
            List expected = new ArrayList();
            Object p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
            if (debug)
                logger.debug("Test UseOfThisToAcessHiddenField01(): Passed");

            tx.commit();
            tx = null;
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** */
    void runTestUseOfThisToAcessHiddenField02(PersistenceManager pm) {
        if (debug) 
            logger.debug("\nExecuting test UseOfThisToAcessHiddenField02() ...");

        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            Query query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters("Integer y");
            query.setFilter("this.y == y");
            Object results = query.execute(new java.lang.Integer(3));

            // check query result
            List expected = new ArrayList();
            Object p4 = new PCPoint(3, 3);
            expected.add(p4);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, results, expected);
            if (debug)
                logger.debug("Test UseOfThisToAcessHiddenField02(): Passed");

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
        loadAndPersistPCPoints(getPM());
        addTearDownClass(PCPoint.class);
    }
}

