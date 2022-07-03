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

import java.util.HashMap;
import java.util.Map;

import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> ExecuteWithMap
 *<BR>
 *<B>Keywords:</B> query
 *<BR>
 *<B>Assertion ID:</B> A14.7-5.
 *<BR>
 *<B>Assertion Description: </B>
 * If the parameter list is a Map, then the keys of the Map
 * must be instances of Integer whose intValue is 1..n. 
 * The value in the Map corresponding to the key whose intValue is 1
 * is bound to the first ? in the SQL statement, and so forth. 
 */
public class ExecuteWithMap extends QueryTest {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A14.7-5 (ExecuteWithMap)";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ExecuteWithMap.class);
    }
    
    /** The array of valid SQL queries. */
    private static final String[] VALID_SQL_QUERIES = {
        "SELECT * FROM {0}.PrimitiveTypes WHERE intNotNull = ? "
            + "OR stringNull = ?",
        "SELECT * FROM {0}.persons WHERE FIRSTNAME = ? AND LASTNAME = ?"
            + " AND MIDDLENAME = ? AND CITY = ?",
        "SELECT * FROM {0}.persons WHERE FIRSTNAME = ? AND LASTNAME = ?"
            + " AND MIDDLENAME = ? AND CITY = ?",
        "SELECT * FROM {0}.persons WHERE FIRSTNAME = ? AND LASTNAME = ?"
            + " AND MIDDLENAME = ? AND CITY = ?"
    };
    
    /** 
     * The expected results of valid SQL queries.
     */
    private Object[] expectedResult = {
        getTransientMylibInstancesAsList(new String[]{
                "primitiveTypesPositive", 
                "primitiveTypesCharacterStringLiterals"}),
        getTransientCompanyModelInstancesAsList(new String[]{"emp2"}),
        getTransientCompanyModelInstancesAsList(new String[]{"emp2"}),
        getTransientCompanyModelInstancesAsList(new String[]{"emp2"})
    };

    /** 
     * Maps of parameter values
     */
    private static final HashMap<Object, Object> hm1 = new HashMap<>();
    private static final HashMap<Object, Object> hm2 = new HashMap<>();
    private static final HashMap<Object, Object> hm3;
    private static final HashMap<Object, Object> hm4;
    private static final HashMap<Object, Object> illegalMapMissingKeyTwo = new HashMap<>();
    private static final HashMap<Object, Object> illegalMapStartsWithZero = new HashMap<>();
    private static final HashMap<Object, Object> illegalMapStringKeys = new HashMap<>();
    static {
        // valid parameter values
        hm1.put(Integer.valueOf(1), Integer.valueOf(4));
        hm1.put(Integer.valueOf(2), "Even");

        hm2.put(Integer.valueOf(1), "emp2First");
        hm2.put(Integer.valueOf(2), "emp2Last");
        hm2.put(Integer.valueOf(3), "emp2Middle");
        hm2.put(Integer.valueOf(4), "New York");

        hm3 = (HashMap<Object, Object>) hm2.clone();
        // extra entry okay, should be ignored by impl
        hm3.put(Integer.valueOf(0), "emp2First");

        hm4 = (HashMap<Object, Object>)hm2.clone();
        // extra entry okay, should be ignored by impl
        hm4.put(Integer.valueOf(5), "New York");

        // invalid parameter values
        illegalMapMissingKeyTwo.put(Integer.valueOf(1), "emp2First");
        illegalMapMissingKeyTwo.put(Integer.valueOf(3), "emp2Last");
        illegalMapMissingKeyTwo.put(Integer.valueOf(4), "emp2Middle");
        illegalMapMissingKeyTwo.put(Integer.valueOf(5), "New York");

        illegalMapStartsWithZero.put(Integer.valueOf(0), "emp2First");
        illegalMapStartsWithZero.put(Integer.valueOf(1), "emp2Last");
        illegalMapStartsWithZero.put(Integer.valueOf(2), "emp2Middle");
        illegalMapStartsWithZero.put(Integer.valueOf(3), "New York");

        illegalMapStringKeys.put(new String("1dog"), "emp2First");
        illegalMapStringKeys.put(new String("2dog"), "emp2Last");
        illegalMapStringKeys.put(new String("3dog"), "emp2Middle");
        illegalMapStringKeys.put(new String("4dog"), "New York");
    };
    private static Map<Object, Object>[] parameterMap = new Map[]{hm1, hm2, hm3, hm4};
            
    /** */
    public void testSetClass() {
        if (isSQLSupported()) {
            int index = 0;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    PrimitiveTypes.class, null, true,
                    parameterMap[index], expectedResult[index], false);

            index = 1;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    Person.class, null, true,
                    parameterMap[index], expectedResult[index], false);

            index = 2;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    Person.class, null, true,
                    parameterMap[index], expectedResult[index], false);

            index = 3;
            executeSQLQuery(ASSERTION_FAILED, VALID_SQL_QUERIES[index],
                    Person.class, null, true,
                    parameterMap[index], expectedResult[index], false);
        }
    }

    /** */
    public void testNegative() {
        if (isSQLSupported()) {
            String query = "SELECT * FROM {0}.persons WHERE FIRSTNAME = ? "
                + "AND LASTNAME = ? AND MIDDLENAME = ? AND CITY = ? "
                + "AND FUNDINGDEPT = ?";
            String singleStringQuery = query;
            executeSQLQuery(ASSERTION_FAILED, query, Person.class, null,
                    false, illegalMapMissingKeyTwo, null, false); 
            executeSQLQuery(ASSERTION_FAILED, query, Person.class, null,
                    false, illegalMapStartsWithZero, null, false); 
            executeSQLQuery(ASSERTION_FAILED, query, Person.class, null,
                    false, illegalMapStringKeys, null, false); 
        }
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        addTearDownClass(CompanyModelReader.getTearDownClasses());
        addTearDownClass(MylibReader.getTearDownClasses());
        loadAndPersistCompanyModel(getPM());
        loadAndPersistMylib(getPM());
    }
}
