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

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
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
 * </ul>
 */
public class SupportedMathMethods extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.6.2-48 (SupportedMathMethods) failed: ";
    
    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     * These queries are used to test Math.abs.
     */
    private static final QueryElementHolder[] VALID_QUERIES_ABS = {
        new QueryElementHolder(
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
                /*TO*/          null),
        new QueryElementHolder(
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
                /*TO*/          null),
        new QueryElementHolder(
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
                /*TO*/          null),
        new QueryElementHolder(
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
                /*TO*/          null),
        new QueryElementHolder(
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
                /*TO*/          null),
        new QueryElementHolder(
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
                /*TO*/          null),
        new QueryElementHolder(
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
                /*TO*/          null),
        new QueryElementHolder(
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
                /*TO*/          null)
    };

    /** 
     * The array of valid queries which may be executed as 
     * single string queries and as API queries.
     * These queries are used to test Math.sqrt.
     */
    private static final QueryElementHolder[] VALID_QUERIES_SQRT = {
        new QueryElementHolder(
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
                /*TO*/          null),
        new QueryElementHolder(
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
                /*TO*/          null),
        new QueryElementHolder(
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
                /*TO*/          null),
        new QueryElementHolder(
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
                /*TO*/          null),
    };

    /** */
    private Object oidOfMath1;

    /** */
    private Object oidOfMath2;

    /** */
    private Object oidOfMath3;

    /** 
     * The expected results of valid queries testing Math.abs.
     */
    private Object[] expectedResultABS = {
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive", "primitiveTypesNegative"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive", "primitiveTypesNegative"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive", "primitiveTypesNegative"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive", "primitiveTypesNegative"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive", "primitiveTypesNegative"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive", "primitiveTypesNegative"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive", "primitiveTypesNegative"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive", "primitiveTypesNegative"})
    };
        
    /** The expected results of valid queries testing Math.sqrt. */
    private Object[] expectedResultSQRT = {
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive"}),
            getTransientMylibInstancesAsList(new String[]{
                    "primitiveTypesPositive"})
    };
            
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SupportedMathMethods.class);
    }

    /** */
    public void testAbs() {
        for (int i = 0; i < VALID_QUERIES_ABS.length; i++) {
            executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES_ABS[i], 
                    expectedResultABS[i]);
            executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES_ABS[i], 
                    expectedResultABS[i]);
        }
    }

    /** */
    public void testSqrt() {
        for (int i = 0; i < VALID_QUERIES_SQRT.length; i++) {
            executeAPIQuery(ASSERTION_FAILED, VALID_QUERIES_SQRT[i], 
                    expectedResultSQRT[i]);
            executeSingleStringQuery(ASSERTION_FAILED, VALID_QUERIES_SQRT[i], 
                    expectedResultSQRT[i]);
        }
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
            Collection expectedResult = new ArrayList();
            expectedResult.add(pm.getObjectById(oidOfMath1));
            expectedResult.add(pm.getObjectById(oidOfMath3));
            Query q =  pm.newQuery();
            q.setClass(MathSample.class);
            q.setFilter(filter);
            Collection results = (Collection)q.execute();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.sin(angle) < 1.02 && Math.sin(angle) > 0.98";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(oidOfMath2));
            q =  pm.newQuery();
            q.setClass(MathSample.class);
            q.setFilter(filter);
            results = (Collection)q.execute();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.sin(angle) < -0.98 && Math.sin(angle) > -1.02";
            expectedResult.clear();
            q =  pm.newQuery();
            q.setClass(MathSample.class);
            q.setFilter(filter);
            results = (Collection)q.execute();
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
            Collection expectedResult = new ArrayList();
            expectedResult.add(pm.getObjectById(oidOfMath2));
            Query q =  pm.newQuery();
            q.setClass(MathSample.class);
            q.setFilter(filter);
            Collection results = (Collection)q.execute();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.cos(angle) < -0.98 && Math.cos(angle) > -1.02";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(oidOfMath1));
            q =  pm.newQuery();
            q.setClass(MathSample.class);
            q.setFilter(filter);
            results = (Collection)q.execute();
            checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

            filter = "Math.cos(angle) < 1.02 && Math.cos(angle) > 0.98";
            expectedResult.clear();
            expectedResult.add(pm.getObjectById(oidOfMath3));
            q =  pm.newQuery();
            q.setClass(MathSample.class);
            q.setFilter(filter);
            results = (Collection)q.execute();
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
            Collection expectedResult = new ArrayList();
            expectedResult.add(pm.getObjectById(oidOfMath1));
            expectedResult.add(pm.getObjectById(oidOfMath3));
            Query q =  pm.newQuery();
            q.setClass(MathSample.class);
            q.setFilter(filter);
            Collection results = (Collection)q.execute();
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
     * @see JDO_Test#localSetUp()
     */
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
            pm.makePersistent(ms1);

            // Sample 2 : angle=PI/2 (90 degrees)
            MathSample ms2 = new MathSample();
            ms2.setId(2);
            ms2.setAngle(new BigDecimal(Math.PI/2.0));
            pm.makePersistent(ms2);

            // Sample 3 : angle=0 (0 degrees)
            MathSample ms3 = new MathSample();
            ms3.setId(3);
            ms3.setAngle(new BigDecimal(0));
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
