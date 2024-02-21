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

import java.text.MessageFormat;
import java.util.Arrays;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.pc.mylib.PrimitiveTypes;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.FullName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Shape of Result. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.7-4. <br>
 * <B>Assertion Description: </B> Table 7: Shape of Result of SQL Query
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShapeOfResult extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A14.7-4 (ShapeOfResult) failed: ";

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

  /** The expected results of valid SQL queries. */
  private final Object[] expectedResult = {
    // candidate class
    getTransientMylibInstancesAsList(
        "primitiveTypesPositive",
        "primitiveTypesNegative",
        "primitiveTypesCharacterStringLiterals"),
    // candidate class, unique
    getTransientMylibInstance("primitiveTypesPositive"),
    // single column
    Arrays.asList("emp1First", "emp2First", "emp3First", "emp4First", "emp5First"),
    // single column, unique
    "emp1First",
    // mutiple columns
    Arrays.asList(
        new Object[] {
          new Object[] {"emp1First", "emp1Last"},
          new Object[] {"emp2First", "emp2Last"},
          new Object[] {"emp3First", "emp3Last"},
          new Object[] {"emp4First", "emp4Last"},
          new Object[] {"emp5First", "emp5Last"}
        }),
    // mutiple columns, unique
    new Object[] {"emp1First", "emp1Last"},
    // result class
    Arrays.asList(
        new Object[] {
          new FullName("emp1First", "emp1Last"),
          new FullName("emp2First", "emp2Last"),
          new FullName("emp3First", "emp3Last"),
          new FullName("emp4First", "emp4Last"),
          new FullName("emp5First", "emp5Last")
        }),
    // result class, unique
    new FullName("emp1First", "emp1Last")
  };

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testCanidateClass0() {
    if (isSQLSupported()) {
      int index = 0;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            PrimitiveTypes.class,
            null,
            true,
            null,
            expectedResult[index],
            false);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testCanidateClass1() {
    if (isSQLSupported()) {
      int index = 1;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            PrimitiveTypes.class,
            null,
            true,
            null,
            expectedResult[index],
            true);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSingleColumn0() {
    if (isSQLSupported()) {
      int index = 2;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            null,
            null,
            true,
            null,
            expectedResult[index],
            false);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testSingleColumn1() {
    if (isSQLSupported()) {
      int index = 3;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            null,
            null,
            true,
            null,
            expectedResult[index],
            true);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMultipleColumn0() {
    if (isSQLSupported()) {
      int index = 4;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            null,
            null,
            true,
            null,
            expectedResult[index],
            false);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testMultipleColumn1() {
    if (isSQLSupported()) {
      int index = 5;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            null,
            null,
            true,
            null,
            expectedResult[index],
            true);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testResultClass0() {
    if (isSQLSupported()) {
      int index = 6;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            null,
            FullName.class,
            true,
            null,
            expectedResult[index],
            false);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testResultClass1() {
    if (isSQLSupported()) {
      int index = 7;
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        executeSQLQuery(
            ASSERTION_FAILED,
            pm,
            VALID_SQL_QUERIES[index],
            null,
            FullName.class,
            true,
            null,
            expectedResult[index],
            true);
      } finally {
        cleanupPM(pm);
      }
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testNegative() {
    if (isSQLSupported()) {
      String schema = getPMFProperty("javax.jdo.mapping.Schema");
      String sql = MessageFormat.format("SELECT stringNull FROM {0}.PrimitiveTypes", schema);
      PersistenceManager pm = getPMF().getPersistenceManager();
      try {
        Query<PrimitiveTypes> query = pm.newQuery("javax.jdo.query.SQL", sql);
        query.setClass(PrimitiveTypes.class);
        compile(ASSERTION_FAILED, pm, query, sql, false);
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
