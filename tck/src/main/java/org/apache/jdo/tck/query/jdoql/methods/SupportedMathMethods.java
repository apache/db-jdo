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

package org.apache.jdo.tck.query.jdoql.methods;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.pc.mylib.QPrimitiveTypes;
import org.apache.jdo.tck.pc.query.MathSample;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Supported Math methods.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.6.2-48.
 *<BR>
 *<B>Assertion Description: </B>
 * Supported Math methods:
 * <ul>
 * <li>Math.abs(numeric)</li>
 * <li>Math.sqrt(numeric)</li>
 * <li>Math.sin(numeric)</li>
 * <li>Math.cos(numeric)</li>
 * <li>Math.tan(numeric)</li>
 * <li>Math.asin(numeric)</li>
 * <li>Math.acos(numeric)</li>
 * <li>Math.atan(numeric)</li>
 * </ul>
 */
public class SupportedMathMethods extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-48 (SupportedMathMethods) failed: ";

    /** */
    private Object oidOfMath1;

    /** */
    private Object oidOfMath2;

    /** */
    private Object oidOfMath3;
     
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SupportedMathMethods.class);
    }

    /** */
    public void testAbs0() {
        Object expected = getTransientMylibInstancesAsList(
                "primitiveTypesPositive", "primitiveTypesNegative");

        JDOQLTypedQuery<PrimitiveTypes> query = getPM().newJDOQLTypedQuery(PrimitiveTypes.class);
        QPrimitiveTypes cand = QPrimitiveTypes.candidate();
        query.filter(cand.intNotNull.abs().eq(4));

        QueryElementHolder<PrimitiveTypes> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        PrimitiveTypes.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "Math.abs(intNotNull) == 4",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testAbs1() {
        Object expected = getTransientMylibInstancesAsList(
                "primitiveTypesPositive", "primitiveTypesNegative");

        JDOQLTypedQuery<PrimitiveTypes> query = getPM().newJDOQLTypedQuery(PrimitiveTypes.class);
        QPrimitiveTypes cand = QPrimitiveTypes.candidate();
        query.filter(cand.intNull.abs().eq(4));

        QueryElementHolder<PrimitiveTypes> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        PrimitiveTypes.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "Math.abs(intNull) == 4",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testAbs2() {
        Object expected = getTransientMylibInstancesAsList(
                "primitiveTypesPositive", "primitiveTypesNegative");

        JDOQLTypedQuery<PrimitiveTypes> query = getPM().newJDOQLTypedQuery(PrimitiveTypes.class);
        QPrimitiveTypes cand = QPrimitiveTypes.candidate();
        query.filter(cand.longNotNull.abs().eq(4L));

        QueryElementHolder<PrimitiveTypes> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        PrimitiveTypes.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "Math.abs(longNotNull) == 4",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testAbs3() {
        Object expected = getTransientMylibInstancesAsList(
                "primitiveTypesPositive", "primitiveTypesNegative");

        JDOQLTypedQuery<PrimitiveTypes> query = getPM().newJDOQLTypedQuery(PrimitiveTypes.class);
        QPrimitiveTypes cand = QPrimitiveTypes.candidate();
        query.filter(cand.longNull.abs().eq(4L));

        QueryElementHolder<PrimitiveTypes> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        PrimitiveTypes.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "Math.abs(longNull) == 4",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testAbs4() {
        Object expected = getTransientMylibInstancesAsList(
                "primitiveTypesPositive", "primitiveTypesNegative");

        JDOQLTypedQuery<PrimitiveTypes> query = getPM().newJDOQLTypedQuery(PrimitiveTypes.class);
        QPrimitiveTypes cand = QPrimitiveTypes.candidate();
        query.filter(cand.floatNotNull.abs().lt(4.1f).
                and(cand.floatNotNull.abs().gt(3.9f)));

        QueryElementHolder<PrimitiveTypes> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        PrimitiveTypes.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "Math.abs(floatNotNull) < 4.1 &&" +
                "Math.abs(floatNotNull) > 3.9",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testAbs5() {
        Object expected = getTransientMylibInstancesAsList(
                "primitiveTypesPositive", "primitiveTypesNegative");

        JDOQLTypedQuery<PrimitiveTypes> query = getPM().newJDOQLTypedQuery(PrimitiveTypes.class);
        QPrimitiveTypes cand = QPrimitiveTypes.candidate();
        query.filter(cand.floatNull.abs().lt(4.1f).
                and(cand.floatNull.abs().gt(3.9f)));

        QueryElementHolder<PrimitiveTypes> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        PrimitiveTypes.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "Math.abs(floatNull) < 4.1 &&" +
                "Math.abs(floatNull) > 3.9",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testAbs6() {
        Object expected = getTransientMylibInstancesAsList(
                "primitiveTypesPositive", "primitiveTypesNegative");

        JDOQLTypedQuery<PrimitiveTypes> query = getPM().newJDOQLTypedQuery(PrimitiveTypes.class);
        QPrimitiveTypes cand = QPrimitiveTypes.candidate();
        query.filter(cand.doubleNotNull.abs().lt(4.1).
                and(cand.doubleNotNull.abs().gt(3.9)));

        QueryElementHolder<PrimitiveTypes> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        PrimitiveTypes.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "Math.abs(doubleNotNull) < 4.1 &&" +
                "Math.abs(doubleNotNull) > 3.9",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testAbs7() {
        Object expected = getTransientMylibInstancesAsList(
                "primitiveTypesPositive", "primitiveTypesNegative");

        JDOQLTypedQuery<PrimitiveTypes> query = getPM().newJDOQLTypedQuery(PrimitiveTypes.class);
        QPrimitiveTypes cand = QPrimitiveTypes.candidate();
        query.filter(cand.doubleNull.abs().lt(4.1).
                and(cand.doubleNull.abs().gt(3.9)));

        QueryElementHolder<PrimitiveTypes> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        PrimitiveTypes.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "Math.abs(doubleNull) < 4.1 &&" +
                "Math.abs(doubleNull) > 3.9",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testSqrt0() {
        Object expected = getTransientMylibInstancesAsList("primitiveTypesPositive");

        JDOQLTypedQuery<PrimitiveTypes> query = getPM().newJDOQLTypedQuery(PrimitiveTypes.class);
        QPrimitiveTypes cand = QPrimitiveTypes.candidate();
        query.filter(cand.doubleNotNull.gt(0d).and(cand.doubleNotNull.sqrt().lt(2.1).
                and(cand.doubleNotNull.sqrt().gt(1.9))));

        QueryElementHolder<PrimitiveTypes> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        PrimitiveTypes.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "doubleNotNull > 0 && " +
                "Math.sqrt(doubleNotNull) < 2.1 && " +
                "Math.sqrt(doubleNotNull) > 1.9",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testSqrt1() {
        Object expected = getTransientMylibInstancesAsList("primitiveTypesPositive");

        JDOQLTypedQuery<PrimitiveTypes> query = getPM().newJDOQLTypedQuery(PrimitiveTypes.class);
        QPrimitiveTypes cand = QPrimitiveTypes.candidate();
        query.filter(cand.doubleNull.gt(0d).and(cand.doubleNull.sqrt().lt(2.1).
                and(cand.doubleNull.sqrt().gt(1.9))));

        QueryElementHolder<PrimitiveTypes> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        PrimitiveTypes.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "doubleNull > 0 && " +
                "Math.sqrt(doubleNull) < 2.1 && " +
                "Math.sqrt(doubleNull) > 1.9",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testSqrt2() {
        Object expected = getTransientMylibInstancesAsList("primitiveTypesPositive");

        JDOQLTypedQuery<PrimitiveTypes> query = getPM().newJDOQLTypedQuery(PrimitiveTypes.class);
        QPrimitiveTypes cand = QPrimitiveTypes.candidate();
        query.filter(cand.intNotNull.gt(0).and(cand.intNotNull.sqrt().lt(2.1).
                and(cand.intNotNull.sqrt().gt(1.9))));

        QueryElementHolder<PrimitiveTypes> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        PrimitiveTypes.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "intNotNull > 0 && " +
                "Math.sqrt(intNotNull) < 2.1 && " +
                "Math.sqrt(intNotNull) > 1.9",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** */
    public void testSqrt3() {
        Object expected = getTransientMylibInstancesAsList("primitiveTypesPositive");

        JDOQLTypedQuery<PrimitiveTypes> query = getPM().newJDOQLTypedQuery(PrimitiveTypes.class);
        QPrimitiveTypes cand = QPrimitiveTypes.candidate();
        query.filter(cand.intNull.gt(0).and(cand.intNull.sqrt().lt(2.1).
                and(cand.intNull.sqrt().gt(1.9))));

        QueryElementHolder<PrimitiveTypes> holder = new QueryElementHolder<>(
                /*UNIQUE*/      null,
                /*RESULT*/      null,
                /*INTO*/        null,
                /*FROM*/        PrimitiveTypes.class,
                /*EXCLUDE*/     null,
                /*WHERE*/       "intNull > 0 && " +
                "Math.sqrt(intNull) < 2.1 && " +
                "Math.sqrt(intNull) > 1.9",
                /*VARIABLES*/   null,
                /*PARAMETERS*/  null,
                /*IMPORTS*/     null,
                /*GROUP BY*/    null,
                /*ORDER BY*/    null,
                /*FROM*/        null,
                /*TO*/          null,
                /*JDOQLTyped*/  query,
                /*paramValues*/ null);

        executeAPIQuery(ASSERTION_FAILED, holder, expected);
        executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
        executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
    }

    /** 
     * Tests for Math.sin()
     */
    @SuppressWarnings("unchecked")
	public void testSin() {
    	insertMathSampleData(getPM());

        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "Math.sin(angle) < 0.02 && Math.sin(angle) > -0.02";
            Collection<MathSample> expectedResult = new ArrayList<>();
            expectedResult.add((MathSample)pm.getObjectById(oidOfMath1));
            expectedResult.add((MathSample)pm.getObjectById(oidOfMath3));
            Query<MathSample> q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            List<MathSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.sin(angle) < 1.02 && Math.sin(angle) > 0.98";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath2));
            q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.sin(angle) < -0.98 && Math.sin(angle) > -1.02";
            expectedResult.clear();
            q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** 
     * Tests for Math.cos()
     */
    @SuppressWarnings("unchecked")
	public void testCos() {
    	insertMathSampleData(getPM());

        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "Math.cos(angle) < 0.02 && Math.cos(angle) > -0.02";
            Collection<MathSample> expectedResult = new ArrayList<>();
            expectedResult.add((MathSample)pm.getObjectById(oidOfMath2));
            Query<MathSample> q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            List<MathSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.cos(angle) < -0.98 && Math.cos(angle) > -1.02";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath1));
            q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.cos(angle) < 1.02 && Math.cos(angle) > 0.98";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath3));
            q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** 
     * Tests for Math.tan()
     */
    @SuppressWarnings("unchecked")
	public void testTan() {
    	insertMathSampleData(getPM());

        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "Math.tan(angle) < 0.02 && Math.tan(angle) > -0.02";
            Collection<MathSample> expectedResult = new ArrayList<>();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath1));
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath3));
            Query<MathSample> q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            List<MathSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** 
     * Tests for Math.asin()
     */
    @SuppressWarnings("unchecked")
	public void testArcSin() {
    	insertMathSampleData(getPM());

        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "Math.asin(trigValue) < 0.1 && Math.asin(trigValue) > -0.1";
            List<MathSample> expectedResult = new ArrayList<>();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath1));
            Query<MathSample> q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            List<MathSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.asin(trigValue) < 1.5714 && Math.asin(trigValue) > 1.570";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath2));
            q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.asin(trigValue) < -1.570 && Math.asin(trigValue) > -1.5714";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath3));
            q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** 
     * Tests for Math.acos()
     */
    @SuppressWarnings("unchecked")
	public void testArcCos() {
    	insertMathSampleData(getPM());

        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "Math.acos(trigValue) < 1.5714 && Math.acos(trigValue) > 1.570";
            Collection<MathSample> expectedResult = new ArrayList<>();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath1));
            Query<MathSample> q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            List<MathSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.acos(trigValue) < 0.1 && Math.acos(trigValue) > -0.1";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath2));
            q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.acos(trigValue) < 3.147 && Math.acos(trigValue) > 3.14";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath3));
            q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** 
     * Tests for Math.atan()
     */
    @SuppressWarnings("unchecked")
	public void testArcTan() {
    	insertMathSampleData(getPM());

        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "Math.atan(trigValue) < 0.5 && Math.atan(trigValue) > -0.5";
            Collection<MathSample> expectedResult = new ArrayList<>();
            expectedResult.add((MathSample)pm.getObjectById(oidOfMath1));
            Query<MathSample> q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            List<MathSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** 
     * Tests for Math.ceil()
     */
    @SuppressWarnings("unchecked")
	public void testCeil() {
    	insertMathSampleData(getPM());

        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "Math.ceil(doubleValue) == 5";
            Collection<MathSample> expectedResult = new ArrayList<>();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath1));
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath2));
            Query<MathSample> q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            List<MathSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.ceil(floatValue) == 3";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath3));
            q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.ceil(intValue) == 5";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath1));
            q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** 
     * Tests for Math.floor()
     */
    @SuppressWarnings("unchecked")
	public void testFloor() {
    	insertMathSampleData(getPM());

        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "Math.floor(doubleValue) == 4";
            Collection<MathSample> expectedResult = new ArrayList<>();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath1));
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath2));
            Query<MathSample> q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            List<MathSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.floor(floatValue) == 2";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath3));
            q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.floor(intValue) == 4";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath2));
            q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** 
     * Tests for Math.exp()
     */
    @SuppressWarnings("unchecked")
	public void testExp() {
    	insertMathSampleData(getPM());

        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "Math.exp(doubleValue) < 85.63 && Math.exp(doubleValue) > 85.62";
            Collection<MathSample> expectedResult = new ArrayList<>();
            expectedResult.add(pm.getObjectById(MathSample.class, oidOfMath2));
            Query<MathSample> q =  pm.newQuery(MathSample.class);
            q.setClass(MathSample.class);
            q.setFilter(filter);
            List<MathSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            tx.commit();
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }

    /** 
     * Tests for Math.log()
     */
    @SuppressWarnings("unchecked")
	public void testLog() {
    	insertMathSampleData(getPM());

        PersistenceManager pm  = getPM();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            String filter = "Math.log(doubleValue) < 1.4935 && Math.log(doubleValue) > 1.491";
            Collection<MathSample> expectedResult = new ArrayList<>();
            expectedResult.add((MathSample)pm.getObjectById(oidOfMath2));
            Query<MathSample> q =  pm.newQuery(MathSample.class);
            q.setFilter(filter);
            List<MathSample> results = q.executeList();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

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
        addTearDownClass(MylibReader.getTearDownClasses());
        addTearDownClass(MathSample.class);
        loadAndPersistMylib(getPM());
    }

    /** */
	private void insertMathSampleData(PersistenceManager pm) {
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();

            // Sample 1 : angle=PI (180 degrees)
            MathSample ms1 = new MathSample();
            ms1.setId(1);
            ms1.setAngle(new BigDecimal(Math.PI));
            ms1.setTrigValue(new BigDecimal(0.0));
            ms1.setDoubleValue(Double.valueOf(4.55));
            ms1.setFloatValue(Float.valueOf(4.55f));
            ms1.setIntValue(Integer.valueOf(5));
            pm.makePersistent(ms1);

            // Sample 2 : angle=PI/2 (90 degrees)
            MathSample ms2 = new MathSample();
            ms2.setId(2);
            ms2.setAngle(new BigDecimal(Math.PI/2.0));
            ms2.setTrigValue(new BigDecimal(1.0));
            ms2.setDoubleValue(Double.valueOf(4.45));
            ms2.setFloatValue(Float.valueOf((float)4.45));
            ms2.setIntValue(Integer.valueOf(4));
            pm.makePersistent(ms2);

            // Sample 3 : angle=0 (0 degrees)
            MathSample ms3 = new MathSample();
            ms3.setId(3);
            ms3.setAngle(new BigDecimal(0));
            ms3.setTrigValue(new BigDecimal(-1.0));
            ms3.setDoubleValue(Double.valueOf(2.49));
            ms3.setFloatValue(Float.valueOf((float)2.49));
            ms3.setIntValue(Integer.valueOf(3));
            pm.makePersistent(ms3);

            tx.commit();
            oidOfMath1 = pm.getObjectId(ms1);
            oidOfMath2 = pm.getObjectId(ms2);
            oidOfMath3 = pm.getObjectId(ms3);
            tx = null;
        } 
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
        }
    }
}
