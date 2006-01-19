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

package org.apache.jdo.tck.query.jdoql.methods;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
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
 * <li> Math.abs(numeric)
 * <li> Math.sqrt(numeric)
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
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(MylibReader.getTearDownClasses());
        loadAndPersistMylib(getPM());
    }

}
