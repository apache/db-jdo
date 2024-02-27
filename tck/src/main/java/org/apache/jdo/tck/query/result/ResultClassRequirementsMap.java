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

package org.apache.jdo.tck.query.result;

import java.util.Arrays;
import java.util.Map;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.QFullTimeEmployee;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.query.result.classes.PublicPutMethod;
import org.apache.jdo.tck.util.ConversionHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * <B>Title:</B> Result Class Requirements. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.12-1. <br>
 * <B>Assertion Description: </B> The result class may be one of the java.lang classes ...
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ResultClassRequirementsMap extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.12-1 (ResultClassRequirements) failed: ";

  // Two dimensional arrays to be converted to maps
  // in the expected result.
  private static final Object[][] emp1Map = {
    {"id", Long.valueOf(1)},
    {"name", "emp1Last"}
  };
  private static final Object[][] emp2Map = {
    {"id", Long.valueOf(2)},
    {"name", "emp2Last"}
  };
  private static final Object[][] emp5Map = {
    {"id", Long.valueOf(5)},
    {"name", "emp5Last"}
  };
  private static final Object[][] publicPutMethod1 = {
    {"personid", Long.valueOf(1)}, {"lastname", "emp1Last"}
  };
  private static final Object[][] publicPutMethod2 = {
    {"personid", Long.valueOf(2)}, {"lastname", "emp2Last"}
  };
  private static final Object[][] publicPutMethod5 = {
    {"personid", Long.valueOf(5)}, {"lastname", "emp5Last"}
  };

  /** */
  @Test
  // @Execution(ExecutionMode.CONCURRENT)
  // ToDo: Wrong query result when exceuted in parallel
  // query: SELECT personid AS id, lastname AS name INTO java.util.Map
  //        FROM org.apache.jdo.tck.pc.company.FullTimeEmployee
  // expected: java.util.ArrayList of size 3
  //         [{name=emp1Last, id=1}, {name=emp2Last, id=2}, {name=emp5Last, id=5}]
  // got:      java.util.ArrayList of size 3
  //         [{personid=5, lastname=emp5Last}, {personid=2, lastname=emp2Last},
  //          {personid=1, lastname=emp1Last}]
  public void testMap() {
    Object expected =
        Arrays.asList(
            ConversionHelper.arrayToMap(emp1Map),
            ConversionHelper.arrayToMap(emp2Map),
            ConversionHelper.arrayToMap(emp5Map));
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<FullTimeEmployee> query = pm.newJDOQLTypedQuery(FullTimeEmployee.class);
      QFullTimeEmployee cand = QFullTimeEmployee.candidate();
      // JDOQLTypedQuery API: Map Result
      query.result(false, cand.personid.as("id"), cand.lastname.as("name"));

      QueryElementHolder<FullTimeEmployee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "personid AS id, lastname AS name",
              /*INTO*/ Map.class,
              /*FROM*/ FullTimeEmployee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, Map.class, true, expected);
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  // @Execution(ExecutionMode.CONCURRENT)
  // ToDo: Wrong query result when exceuted in parallel
  // query: SELECT personid, lastname INTO org.apache.jdo.tck.query.result.classes.PublicPutMethod
  //        FROM org.apache.jdo.tck.pc.company.FullTimeEmployee
  // expected: java.util.ArrayList of size 3
  //         [org.apache.jdo.tck.query.result.classes.PublicPutMethod(
  //             {personid=1, lastname=emp1Last}),
  //          org.apache.jdo.tck.query.result.classes.PublicPutMethod(
  //             {personid=2, lastname=emp2Last}),
  //          org.apache.jdo.tck.query.result.classes.PublicPutMethod(
  //             {personid=5, lastname=emp5Last})]
  // got:      java.util.ArrayList of size 3
  //         [org.apache.jdo.tck.query.result.classes.PublicPutMethod({name=emp5Last, id=5}),
  //          org.apache.jdo.tck.query.result.classes.PublicPutMethod({name=emp2Last, id=2}),
  //          org.apache.jdo.tck.query.result.classes.PublicPutMethod({name=emp1Last, id=1})]
  public void testPut() {
    Object expected =
        Arrays.asList(
            new PublicPutMethod(ConversionHelper.arrayToMap(publicPutMethod1)),
            new PublicPutMethod(ConversionHelper.arrayToMap(publicPutMethod2)),
            new PublicPutMethod(ConversionHelper.arrayToMap(publicPutMethod5)));
    PersistenceManager pm = getPMF().getPersistenceManager();
    try {
      JDOQLTypedQuery<FullTimeEmployee> query = pm.newJDOQLTypedQuery(FullTimeEmployee.class);
      QFullTimeEmployee cand = QFullTimeEmployee.candidate();
      // JDOQLTypedQuery API: constructor
      query.result(false, cand.personid.as("personid"), cand.lastname.as("lastname"));

      QueryElementHolder<FullTimeEmployee> holder =
          new QueryElementHolder<>(
              /*UNIQUE*/ null,
              /*RESULT*/ "personid, lastname",
              /*INTO*/ PublicPutMethod.class,
              /*FROM*/ FullTimeEmployee.class,
              /*EXCLUDE*/ null,
              /*WHERE*/ null,
              /*VARIABLES*/ null,
              /*PARAMETERS*/ null,
              /*IMPORTS*/ null,
              /*GROUP BY*/ null,
              /*ORDER BY*/ null,
              /*FROM*/ null,
              /*TO*/ null,
              /*JDOQLTyped*/ query,
              /*paramValues*/ null);

      executeAPIQuery(ASSERTION_FAILED, pm, holder, expected);
      executeSingleStringQuery(ASSERTION_FAILED, pm, holder, expected);
      executeJDOQLTypedQuery(ASSERTION_FAILED, pm, holder, PublicPutMethod.class, true, expected);
    } finally {
      cleanupPM(pm);
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
    loadAndPersistCompanyModel(getPM());
  }
}
