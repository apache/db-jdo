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

package org.apache.jdo.tck.query.jdoql.operators;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.List;

/**
 *<B>Title:</B> Bitwise Binary Query Operators
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-22 and A14.6.2-23.
 *<BR>
 *<B>Assertion Description: </B>
 A JDO implementation including javax.jdo.query.JDOQL.bitwiseOperations in the result of
 PMF.supportedOptions() supports JDOQL queries that contain bitwise operations.
 Then the integral binary bitwise operators (<code>& and | and ^</code>) are supported
 for the following types:
 <UL>
 <LI><code>byte, short, int, long, Byte, Short Integer, Long</code></LI>
 </UL>
 */
public class BitwiseBinaryOperators extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED =
            "Assertion A14.6.2-22 and A14.6.2-23 (BitwiseBinaryOperators) failed: ";

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(BitwiseBinaryOperators.class);
    }

    /**
     * Testing bitwise AND
     */
    public void testBitwiseAndPositive() {
        if (isBitwiseOperationsSupported()) {
            PersistenceManager pm = getPM();
            Transaction tx = pm.currentTransaction();
            try {
                tx.begin();

                // Expected result
                List<PrimitiveTypes> expected = pm.newQuery(PrimitiveTypes.class, "4 <= id && id <= 7").executeList();

                runSimplePrimitiveTypesQuery("(byteNotNull & 4) != 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(byteNull & 4) != 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(shortNotNull & 4) != 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(shortNull & 4) != 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(intNotNull & 4) != 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(intNull & 4) != 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(longNotNull & 4) != 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(longNull & 4) != 0", pm, expected, ASSERTION_FAILED);

                tx.commit();
                tx = null;
            } finally {
                if ((tx != null) && tx.isActive())
                    tx.rollback();
            }
        }
    }

    /**
     * Testing bitwise OR
     */
    public void testBitwiseOrPositive() {
        if (isBitwiseOperationsSupported()) {
            PersistenceManager pm = getPM();
            Transaction tx = pm.currentTransaction();
            try {
                tx.begin();

                // Expected result
                List<PrimitiveTypes> expected = pm.newQuery(PrimitiveTypes.class, "4 <= id && id <= 7").executeList();

                runSimplePrimitiveTypesQuery("(byteNotNull | 3) == 7", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(byteNull | 3) == 7", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(shortNotNull | 3) == 7", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(shortNull | 3) == 7", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(intNotNull | 3) == 7", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(intNull | 3) == 7", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(longNotNull | 3) == 7", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(longNull | 3) == 7", pm, expected, ASSERTION_FAILED);

                tx.commit();
                tx = null;
            } finally {
                if ((tx != null) && tx.isActive())
                    tx.rollback();
            }
        }
    }
    
    /**
     * Testing bitwise XOR
     */
    public void testBitwiseXOrPositive() {
        if (isBitwiseOperationsSupported()) {
            PersistenceManager pm = getPM();
            Transaction tx = pm.currentTransaction();
            try {
                tx.begin();

                // Expected result
                List<PrimitiveTypes> expected = pm.newQuery(PrimitiveTypes.class, "id == 1").executeList();

                runSimplePrimitiveTypesQuery("(byteNotNull ^ 1) == 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(byteNull ^ 1) == 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(shortNotNull ^ 1) == 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(shortNull ^ 1) == 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(intNotNull ^ 1) == 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(intNull ^ 1) == 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(longNotNull ^ 1) == 0", pm, expected, ASSERTION_FAILED);
                runSimplePrimitiveTypesQuery("(longNull ^ 1) == 0", pm, expected, ASSERTION_FAILED);

                tx.commit();
                tx = null;
            } finally {
                if ((tx != null) && tx.isActive())
                    tx.rollback();
            }
        }
    }

    /**
     * Queries using bitwise AND that should result in a JDOException.
     */
    public void testBitwiseAndNegative() {
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.begin();

        try {
            Query q = pm.newQuery(PrimitiveTypes.class, "stringNull & stringNull == stringNull");
            Object result = q.execute();
            fail(ASSERTION_FAILED,
                    "Query using & operator for non-supported types should throw JDOUserException.");
        }
        catch (JDOUserException ex) {
            // expected exception
            if (debug) {
                logger.debug("expected exception: " + ex);
            }
        }

        if (!isBitwiseOperationsSupported()) {
            try {
                Query q = pm.newQuery(PrimitiveTypes.class, "(intNotNull & 4) > 0");
                Object result = q.execute();
                fail(ASSERTION_FAILED,
                        "Query using & operator for non-supported types should throw JDOUserException.");
            } catch (JDOUserException ex) {
                // expected exception
                if (debug) {
                    logger.debug("expected exception: " + ex);
                }
            }
        }

        tx.commit();
    }

    /**
     * Queries using bitwise AND that should result in a JDOException.
     */
    public void testBitwiseOrNegative() {
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.begin();

        try {
            Query q = pm.newQuery(PrimitiveTypes.class, "stringNull | stringNull == stringNull");
            Object result = q.execute();
            fail(ASSERTION_FAILED,
                    "Query using | operator for non-supported types should throw JDOUserException.");
        }
        catch (JDOUserException ex) {
            // expected exception
            if (debug) {
                logger.debug("expected exception: " + ex);
            }
        }

        if (!isBitwiseOperationsSupported()) {
            try {
                Query q = pm.newQuery(PrimitiveTypes.class, "(intNotNull | 3) == 7");
                Object result = q.execute();
                fail(ASSERTION_FAILED,
                        "Query using & operator for non-supported types should throw JDOUserException.");
            } catch (JDOUserException ex) {
                // expected exception
                if (debug) {
                    logger.debug("expected exception: " + ex);
                }
            }
        }

        tx.commit();
    }

    /**
     * Queries using bitwise AND that should result in a JDOException.
     */
    public void testBitwiseXOrNegative() {
        PersistenceManager pm = getPM();
        Transaction tx = pm.currentTransaction();
        tx.begin();

        try {
            Query q = pm.newQuery(PrimitiveTypes.class, "stringNull ^ stringNul == stringNull");
            Object result = q.execute();
            fail(ASSERTION_FAILED,
                    "Query using & operator for non-supported types should throw JDOUserException.");
        }
        catch (JDOUserException ex) {
            // expected exception
            if (debug) {
                logger.debug("expected exception: " + ex);
            }
        }

        if (!isBitwiseOperationsSupported()) {
            try {
                Query q = pm.newQuery(PrimitiveTypes.class, "(intNotNull ^ 1) == 0");
                Object result = q.execute();
                fail(ASSERTION_FAILED,
                        "Query using & operator for non-supported types should throw JDOUserException.");
            } catch (JDOUserException ex) {
                // expected exception
                if (debug) {
                    logger.debug("expected exception: " + ex);
                }
            }
        }

        tx.commit();
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(PrimitiveTypes.class);
        loadAndPersistPrimitiveTypes(getPM());
    }
}
