/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.Person;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> ExecuteWithMap <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.7-5. <br>
 * <B>Assertion Description: </B> If the parameter list is a Map, then the keys of the Map must be
 * instances of Integer whose intValue is 1..n. The value in the Map corresponding to the key whose
 * intValue is 1 is bound to the first ? in the SQL statement, and so forth.
 */
@SuppressWarnings("unchecked")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExecuteWithMap extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.7-5 (ExecuteWithMap)";

  /** The array of valid SQL queries. */
  private static final String[] VALID_SQL_QUERIES = {
    "SELECT * FROM {0}.PrimitiveTypes WHERE intNotNull = ? " + "OR stringNull = ?",
    "SELECT * FROM {0}.persons WHERE FIRSTNAME = ? AND LASTNAME = ?"
        + " AND MIDDLENAME = ? AND CITY = ?",
    "SELECT * FROM {0}.persons WHERE FIRSTNAME = ? AND LASTNAME = ?"
        + " AND MIDDLENAME = ? AND CITY = ?",
    "SELECT * FROM {0}.persons WHERE FIRSTNAME = ? AND LASTNAME = ?"
        + " AND MIDDLENAME = ? AND CITY = ?"
  };

  private static final String INVALID_SQL_QUERY =
      "SELECT * FROM {0}.persons WHERE FIRSTNAME = ? "
          + "AND LASTNAME = ? AND MIDDLENAME = ? AND CITY = ? "
          + "AND FUNDINGDEPT = ?";

  /** The expected results of valid SQL queries. */
  private final Object[] expectedResult = {
    getTransientMylibInstancesAsList(
        new String[] {"primitiveTypesPositive", "primitiveTypesCharacterStringLiterals"}),
    getTransientCompanyModelInstancesAsList(Employee.class, "emp2"),
    getTransientCompanyModelInstancesAsList(Employee.class, "emp2"),
    getTransientCompanyModelInstancesAsList(Employee.class, "emp2")
  };

  /** Maps of parameter values */
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

    hm4 = (HashMap<Object, Object>) hm2.clone();
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

    illegalMapStringKeys.put("1dog", "emp2First");
    illegalMapStringKeys.put("2dog", "emp2Last");
    illegalMapStringKeys.put("3dog", "emp2Middle");
    illegalMapStringKeys.put("4dog", "New York");
  }

  @SuppressWarnings("rawtypes")
  private static final Map<Object, Object>[] parameterMap = new Map[] {hm1, hm2, hm3, hm4};

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetClass0() {
    if (isSQLSupported()) {
      final int index = 0;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            PrimitiveTypes.class,
            null,
            true,
            parameterMap[index],
            expectedResult[index],
            false);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetClass1() {
    if (isSQLSupported()) {
      final int index = 1;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            Person.class,
            null,
            true,
            parameterMap[index],
            expectedResult[index],
            false);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetClass2() {
    if (isSQLSupported()) {
      final int index = 2;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            Person.class,
            null,
            true,
            parameterMap[index],
            expectedResult[index],
            false);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSetClass3() {
    if (isSQLSupported()) {
      final int index = 3;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            Person.class,
            null,
            true,
            parameterMap[index],
            expectedResult[index],
            false);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegative0() {
    if (isSQLSupported()) {
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            INVALID_SQL_QUERY,
            Person.class,
            null,
            false,
            illegalMapMissingKeyTwo,
            null,
            false);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegative1() {
    if (isSQLSupported()) {
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            INVALID_SQL_QUERY,
            Person.class,
            null,
            false,
            illegalMapStartsWithZero,
            null,
            false);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegative2() {
    if (isSQLSupported()) {
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            INVALID_SQL_QUERY,
            Person.class,
            null,
            false,
            illegalMapStringKeys,
            null,
            false);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  @BeforeAll
  @Override
  protected void setUp() {
    super.setUp();
  }

  @AfterAll
  @Override
  protected void tearDown() {
    super.tearDown();
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
