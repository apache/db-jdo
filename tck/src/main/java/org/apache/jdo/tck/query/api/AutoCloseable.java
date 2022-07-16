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

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.jdo.JDOUserException;
import javax.jdo.Query;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.pc.mylib.PCPoint;

/**
 *<B>Title:</B> AutoCloseable
 *<BR>
 *<B>Keywords:</B> query close
 *<BR>
 *<B>Assertion IDs:</B> A14.6.7-3.
 *<BR>
 *<B>Assertion Description: </B>
 * In a non-managed environment, if the query is created with try-with-resources
 * all results of execute(...) methods on this query instance are automatically 
 * closed at the end of that block and all resources associated with it are released.
 */
public class AutoCloseable extends QueryTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.7-3 (AutoCloseable) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(AutoCloseable.class);
    }

    /**
     * This methods creates a query instance with try-with-resources and 
     * checks that an iterator for the query result is not accessible after the block.
     */
    public void testTryWithResource() {

        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();

            Query<PCPoint> query = null;
            List<PCPoint> queryResult = null;
            Iterator<PCPoint> iterator = null;
            try (Query<PCPoint> query1 = pm.newQuery(PCPoint.class)) {
                query = query1;
                queryResult = query1.executeList();
                iterator = queryResult.iterator();
                if (!iterator.hasNext()) {
                    fail(ASSERTION_FAILED, "(1) Iterator of open query result should have elements.");
                }
            } catch (Exception ex) {
                fail(ASSERTION_FAILED, "(2) Unexpected exception " + ex);
            }

            // check iterator retrieved in try-with-resource block
            if (iterator.hasNext()) {
                fail(ASSERTION_FAILED,
                        "(3) Iterator of closed query result should return false on hasNext().");
            }
            try {
                PCPoint next = iterator.next();
                fail(ASSERTION_FAILED,
                        "(4) Iterator of closed query result should throw NoSuchElementException on next().");
            } catch (NoSuchElementException ex) {
                // expected exception
            }

            // create new Iterator and check its behaviour
            Iterator<PCPoint> iterator2 = queryResult.iterator();
            if (iterator2.hasNext()) {
                fail(ASSERTION_FAILED,
                        "(5) Iterator of closed query result should return false on hasNext().");
            }
            try {
                PCPoint next = iterator2.next();
                fail(ASSERTION_FAILED,
                        "(6) Iterator of closed query result should throw NoSuchElementException on next().");
            } catch (NoSuchElementException ex) {
                // expected exception
            }

            // check query result itself
            try {
                int size = queryResult.size();
                fail(ASSERTION_FAILED, "(7) closed query result should not be accessible.");
            } catch (JDOUserException ex) {
                // expected exception when accessing closed query result
            }
            try {
                PCPoint elem = queryResult.get(0);
                fail(ASSERTION_FAILED, "(8) closed query result should not be accessible.");
            } catch (JDOUserException ex) {
                // expected exception when accessing closed query result
            }
            try {
                boolean empty = queryResult.isEmpty();
                fail(ASSERTION_FAILED, "(9) closed query result should not be accessible.");
            } catch (JDOUserException ex) {
                // expected exception when accessing closed query result
            }
            // Check query instance is still usable
            queryResult = query.executeList();
            if (queryResult.isEmpty()) {
                fail(ASSERTION_FAILED,
                        "(10) query instance should be usable and execution should return a non empty result.");
            }

            tx.commit();
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * This methods creates a query instance with try-with-resources and 
     * checks that an iterator for the query result is not accessible after the block,
     * if the block is ended with an exception. 
     */
    public void testTryWithResourceThrowingException() {

        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();

        try {
            tx.begin();

            Query<PCPoint> query = null;
            List<PCPoint> queryResult = null;
            Iterator<PCPoint> iterator = null;
            try (Query<PCPoint> query1 = pm.newQuery(PCPoint.class)) {
                query = query1;
                queryResult = query1.executeList();
                iterator = queryResult.iterator();
                if (!iterator.hasNext()) {
                    fail(ASSERTION_FAILED, "(1) Iterator of open query result should have elements.");
                }
                throw new DummyException();
            } catch (DummyException ex) {
                // expected exception
            } catch (Exception ex) {
                fail(ASSERTION_FAILED, "(2) Unexpected exception " + ex);
            }

            // check iterator retrieved in try-with-resource block
            if (iterator.hasNext()) {
                fail(ASSERTION_FAILED,
                        "(3) Iterator of closed query result should return false on hasNext().");
            }
            try {
                PCPoint next = iterator.next();
                fail(ASSERTION_FAILED,
                        "(4) Iterator of closed query result should throw NoSuchElementException on next().");
            } catch (NoSuchElementException ex) {
                // expected exception
            }

            // create new Iterator and check its behaviour
            Iterator<PCPoint> iterator2 = queryResult.iterator();
            if (iterator2.hasNext()) {
                fail(ASSERTION_FAILED,
                        "(5) Iterator of closed query result should return false on hasNext().");
            }
            try {
                PCPoint next = iterator2.next();
                fail(ASSERTION_FAILED,
                        "(6) Iterator of closed query result should throw NoSuchElementException on next().");
            } catch (NoSuchElementException ex) {
                // expected exception
            }

            // check query result itself
            try {
                int size = queryResult.size();
                fail(ASSERTION_FAILED, "(7) closed query result should not be accessible.");
            } catch (JDOUserException ex) {
                // expected exception when accessing closed query result
            }
            try {
                PCPoint elem = queryResult.get(0);
                fail(ASSERTION_FAILED, "(8) closed query result should not be accessible.");
            } catch (JDOUserException ex) {
                // expected exception when accessing closed query result
            }
            try {
                boolean empty = queryResult.isEmpty();
                fail(ASSERTION_FAILED, "(9) closed query result should not be accessible.");
            } catch (JDOUserException ex) {
                // expected exception when accessing closed query result
            }

            // Check query instance is still usable
            queryResult = query.executeList();
            if (queryResult.isEmpty()) {
                fail(ASSERTION_FAILED,
                        "(10) query instance should be usable and execution should return a non empty result.");
            }

            tx.commit();
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
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

    /**
     * DummyException used in method testTryWithResourceThrowingException.
     */
    private static final class DummyException extends Exception {}
}
