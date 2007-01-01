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

package org.apache.jdo.tck.query.sql;

import java.text.MessageFormat;
import java.util.Arrays;

import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Shape of Result.
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.7-4.
 *<BR>
 *<B>Assertion Description: </B>
 * Table 7: Shape of Result of SQL Query
 */
public class ShapeOfResult extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.7-4 (ShapeOfResult) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ShapeOfResult.class);
    }
    
    /** The array of valid SQL queries. */
    private static final String[] VALID_SQL_QUERIES = {
        // candidate class
        "SELECT * FROM {0}.PrimitiveTypes",
        // candidate class, unique
        "SELECT * FROM {0}.PrimitiveTypes where id = 1",
        // single column
        "SELECT firstname FROM {0}.persons",
        // single column, unique
        "SELECT firstname FROM {0}.persons WHERE personid = 1",
        // mutiple columns
        "SELECT firstname, lastname FROM {0}.persons",
        // mutiple columns, unique
        "SELECT firstname, lastname FROM {0}.persons WHERE personid = 1",
        // result class
        "SELECT firstname, lastname FROM {0}.persons",
        // result class, unique
        "SELECT firstname, lastname FROM {0}.persons WHERE personid = 1"
    };
    
    /** 
     * The expected results of valid SQL queries.
     */
    private Object[] expectedResult = {
        // candidate class
        getTransientMylibInstancesAsList(new String[]{
                "primitiveTypesPositive", "primitiveTypesNegative",
                "primitiveTypesCharacterStringLiterals"}),
        // candidate class, unique
        getTransientMylibInstance("primitiveTypesPositive"),
        // single column
        Arrays.asList(new Object[]{"emp1First", "emp2First", "emp3First", 
            "emp4First", "emp5First"}),
        // single column, unique
        "emp1First",
        // mutiple columns
        Arrays.asList(new Object[]{
            new Object[]{"emp1First", "emp1Last"},
            new Object[]{"emp2First", "emp2Last"},
            new Object[]{"emp3First", "emp3Last"},
            new Object[]{"emp4First", "emp4Last"},
            new Object[]{"emp5First", "emp5Last"}}),
        // mutiple columns, unique
        new Object[]{"emp1First", "emp1Last"},
        // result class
        Arrays.asList(new Object[]{
            new FullName("emp1First", "emp1Last"),
            new FullName("emp2First", "emp2Last"),
            new FullName("emp3First", "emp3Last"),
            new FullName("emp4First", "emp4Last"),
            new FullName("emp5First", "emp5Last")}),
        // result class, unique
        new FullName("emp1First", "emp1Last")
    };
    
    /** */
    public void testCanidateClass() {
        if (isSQLSupported()) {
            int index = 0;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    PrimitiveTypes.class, null, null, 
                    expectedResult[index], false);
            index++;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    PrimitiveTypes.class, null, null, 
                    expectedResult[index], true);
        }
    }

    /** */
    public void testSingleColumn() {
        if (isSQLSupported()) {
            int index = 2;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    null, null, null, expectedResult[index], false);
            index++;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    null, null, null, expectedResult[index], true);
        }
    }
    
    /** */
    public void testMultipleColumn() {
        if (isSQLSupported()) {
            int index = 4;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    null, null, null, expectedResult[index], false);
            index++;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    null, null, null, expectedResult[index], true);
        }
    }
    
    /** */
    public void testResultClass() {
        if (isSQLSupported()) {
            int index = 6;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    null, FullName.class, null, expectedResult[index], false);
            index++;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    null, FullName.class, null, expectedResult[index], true);
        }
    }
    
    /** */
    public void testNegative() {
        if (isSQLSupported()) {
            String schema = getPMFProperty("javax.jdo.mapping.Schema");
            String sql = MessageFormat.format(
                    "SELECT stringNull FROM {0}.PrimitiveTypes", 
                    new Object[]{schema});
            Query query = getPM().newQuery("javax.jdo.query.SQL", sql);
            query.setClass(PrimitiveTypes.class);
            compile(ASSERTION_FAILED, query, sql, false);
        }
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        addTearDownClass(MylibReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
        loadAndPersistMylib(getPM());
    }
}
