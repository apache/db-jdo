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

package org.apache.jdo.tck.query.jdoql.methods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.Expression;
import javax.jdo.query.NumericExpression;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.MeetingRoom;
import org.apache.jdo.tck.pc.company.QDepartment;
import org.apache.jdo.tck.query.QueryElementHolder;
import org.apache.jdo.tck.query.QueryTest;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B>Supported list methods <br>
 * <B>Keywords:</B> query list <br>
 * <B>Assertion ID:</B> A14.6.2-58. <br>
 * <B>Assertion Description: </B> Supported list methods:
 *
 * <UL>
 *   <LI>get(int)
 * </UL>
 */
public class SupportedListMethods extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-58 (SupportedListMethods) failed: ";

  /** */
  @SuppressWarnings("unchecked")
  @Test
  public void testGetInFilter() {
    // get(PARAMETER) in filter
    List<Department> expected = getTransientCompanyModelInstancesAsList(Department.class, "dept1");

    JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
    QDepartment cand = QDepartment.candidate();
    Expression<MeetingRoom> roomParam = query.parameter("room1", MeetingRoom.class);
    NumericExpression<?> posParam = query.numericParameter("pos");
    query.filter(cand.meetingRooms.get(posParam).eq(roomParam));

    getPM().currentTransaction().begin();
    Map<String, Object> paramValues = new HashMap<>();
    paramValues.put("pos", Integer.valueOf(1));
    paramValues.put("room1", getPersistentCompanyModelInstance(MeetingRoom.class, "room2"));
    getPM().currentTransaction().commit();

    QueryElementHolder<Department> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ null,
            /*INTO*/ null,
            /*FROM*/ Department.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "meetingRooms.get(pos) == room1",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ "int pos, MeetingRoom room1",
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ paramValues);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, expected);
  }

  /** */
  @Test
  public void testGetInResult() {
    // get(LITERAL) in result
    List<MeetingRoom> expected =
        getTransientCompanyModelInstancesAsList(MeetingRoom.class, "room2");

    JDOQLTypedQuery<Department> query = getPM().newJDOQLTypedQuery(Department.class);
    QDepartment cand = QDepartment.candidate();
    query.result(false, cand.meetingRooms.get(1));
    query.filter(cand.deptid.eq(1L));

    QueryElementHolder<Department> holder =
        new QueryElementHolder<>(
            /*UNIQUE*/ null,
            /*RESULT*/ "meetingRooms.get(1)",
            /*INTO*/ null,
            /*FROM*/ Department.class,
            /*EXCLUDE*/ null,
            /*WHERE*/ "deptid == 1",
            /*VARIABLES*/ null,
            /*PARAMETERS*/ null,
            /*IMPORTS*/ null,
            /*GROUP BY*/ null,
            /*ORDER BY*/ null,
            /*FROM*/ null,
            /*TO*/ null,
            /*JDOQLTyped*/ query,
            /*paramValues*/ null);

    executeAPIQuery(ASSERTION_FAILED, holder, expected);
    executeSingleStringQuery(ASSERTION_FAILED, holder, expected);
    executeJDOQLTypedQuery(ASSERTION_FAILED, holder, null, true, expected);
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
