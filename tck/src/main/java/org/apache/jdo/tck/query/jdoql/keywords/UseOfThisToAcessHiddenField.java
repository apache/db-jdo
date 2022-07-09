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
 
package org.apache.jdo.tck.query.jdoql.keywords;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

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

            Query<PCPoint> query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters("Integer x");
            query.setFilter("this.x == x");
            query.setParameters(Integer.valueOf(2));
            List<PCPoint> results = query.executeList();

            // check query result
            List<PCPoint> expected = new ArrayList<>();
            PCPoint p3 = new PCPoint(2, 2);
            expected.add(p3);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, "this.x == x",
                    results, expected);
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

            Query<PCPoint> query = pm.newQuery();
            query.setClass(PCPoint.class);
            query.setCandidates(pm.getExtent(PCPoint.class, false));
            query.declareParameters("Integer y");
            query.setFilter("this.y == y");
            query.setParameters(Integer.valueOf(3));
            List<PCPoint> results = query.executeList();

            // check query result
            List<PCPoint> expected = new ArrayList<>();
            PCPoint p4 = new PCPoint(3, 3);
            expected.add(p4);
            expected = getFromInserted(expected);
            printOutput(results, expected);
            checkQueryResultWithoutOrder(ASSERTION_FAILED, "this.y == y",
                    results, expected);
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
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(PCPoint.class);
        loadAndPersistPCPoints(getPM());
    }
}

