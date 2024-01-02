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

package org.apache.jdo.tck.query.jdoql.subqueries;

import java.util.List;
import javax.jdo.JDOQLTypedQuery;
import javax.jdo.JDOQLTypedSubquery;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.query.NumericExpression;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.MeetingRoom;
import org.apache.jdo.tck.pc.company.QEmployee;
import org.apache.jdo.tck.pc.company.QMeetingRoom;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

/**
 * <B>Title:</B> Correlated Subqueries Without Parameters <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-56 <br>
 * <B>Assertion Description: </B> A correlated subquery is a subquery which contains references to
 * expressions in the outer query. If the correlation can be expressed as a restriction of the
 * candidate collection of the subquery, no parameters are needed.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CorrelatedSubqueries extends SubqueriesTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-56 (CorrelatedSubqueries) failed: ";

  // Select employees who work more than the average of their department employees.
  private static final String SINGLE_STRING_QUERY_COLLECTION_SUBQUERY =
      "SELECT FROM "
          + Employee.class.getName()
          + " WHERE this.weeklyhours > (SELECT AVG(e.weeklyhours) FROM this.department.employees e)";

  // Select employees who work in a department having a meeting room with the maximum roomid.
  private static final String SINGLE_STRING_QUERY_LIST_SUBQUERY =
      "SELECT FROM "
          + Employee.class.getName()
          + " WHERE (SELECT max(r.roomid) FROM this.department.meetingRooms r) == 3";

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testCollectionApiQuery() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Employee> expected =
          getTransientCompanyModelInstancesAsList(
              Employee.class, "emp1", "emp2", "emp4", "emp6", "emp7", "emp10");

      // API query
      try (Query<Employee> apiQuery = pm.newQuery(Employee.class)) {
        Query<Employee> sub = pm.newQuery(Employee.class);
        sub.setResult("avg(this.weeklyhours)");
        apiQuery.setFilter("this.weeklyhours> averageWeeklyhours");
        apiQuery.addSubquery(sub, "double averageWeeklyhours", "this.department.employees");
        List<Employee> emps = apiQuery.executeList();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_COLLECTION_SUBQUERY, emps, expected);

        // API query against memory model
        List<Employee> allEmployees = pm.newQuery(Employee.class).executeList();
        apiQuery.setCandidates(allEmployees);
        emps = apiQuery.executeList();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_COLLECTION_SUBQUERY, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testCollectionSingleStringQuery() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Employee> expected =
          getTransientCompanyModelInstancesAsList(
              Employee.class, "emp1", "emp2", "emp4", "emp6", "emp7", "emp10");
      // single String JDOQL
      try (Query<Employee> singleStringQuery =
          pm.newQuery(SINGLE_STRING_QUERY_COLLECTION_SUBQUERY)) {
        List<Employee> emps = singleStringQuery.executeList();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_COLLECTION_SUBQUERY, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testCollectionJDOQLTypedQuery() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Employee> expected =
          getTransientCompanyModelInstancesAsList(
              Employee.class, "emp1", "emp2", "emp4", "emp6", "emp7", "emp10");
      try (JDOQLTypedQuery<Employee> q = pm.newJDOQLTypedQuery(Employee.class)) {
        QEmployee cand = QEmployee.candidate();
        JDOQLTypedSubquery<Employee> subquery =
            q.subquery(cand.department.employees, Employee.class, "e");
        QEmployee candsub = QEmployee.candidate("e");
        q.filter(cand.weeklyhours.gt(subquery.selectUnique(candsub.weeklyhours.avg())));
        List<Employee> emps = q.executeList();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_COLLECTION_SUBQUERY, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testListApiQuery() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Employee> expected =
          getTransientCompanyModelInstancesAsList(Employee.class, "emp7", "emp8", "emp9", "emp10");

      // API query
      try (Query<Employee> apiQuery = pm.newQuery(Employee.class)) {
        Query<MeetingRoom> sub = pm.newQuery(MeetingRoom.class);
        sub.setResult("max(roomid)");
        apiQuery.setFilter("maxNumber == 3");
        apiQuery.addSubquery(sub, "long maxNumber", "this.department.meetingRooms");
        List<Employee> emps = apiQuery.executeList();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_LIST_SUBQUERY, emps, expected);

        // API query against memory model
        List<Employee> allEmployees = pm.newQuery(Employee.class).executeList();
        apiQuery.setCandidates(allEmployees);
        emps = apiQuery.executeList();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_LIST_SUBQUERY, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testListSingleStringQuery() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Employee> expected =
          getTransientCompanyModelInstancesAsList(Employee.class, "emp7", "emp8", "emp9", "emp10");
      // single String JDOQL
      try (Query<Employee> singleStringQuery = pm.newQuery(SINGLE_STRING_QUERY_LIST_SUBQUERY)) {
        List<Employee> emps = singleStringQuery.executeList();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_LIST_SUBQUERY, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
    } finally {
      cleanupPM(pm);
    }
  }

  /** */
  @SuppressWarnings("unchecked")
  @Test
  @Execution(ExecutionMode.CONCURRENT)
  public void testListJDOQLTypedQuery() {
    PersistenceManager pm = getPMF().getPersistenceManager();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      List<Employee> expected =
          getTransientCompanyModelInstancesAsList(Employee.class, "emp7", "emp8", "emp9", "emp10");
      try (JDOQLTypedQuery<Employee> q = pm.newJDOQLTypedQuery(Employee.class)) {
        QEmployee cand = QEmployee.candidate();
        JDOQLTypedSubquery<MeetingRoom> subquery =
            q.subquery(cand.department.meetingRooms, MeetingRoom.class, "r");
        QMeetingRoom candsub = QMeetingRoom.candidate("r");
        q.filter(subquery.selectUnique((NumericExpression<Long>) candsub.roomid.max()).eq(3l));
        List<Employee> emps = q.executeList();
        checkQueryResultWithoutOrder(
            ASSERTION_FAILED, SINGLE_STRING_QUERY_COLLECTION_SUBQUERY, emps, expected);
      } catch (Exception ex) {
        fail(ASSERTION_FAILED, ex.getLocalizedMessage());
      }
      tx.commit();
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
