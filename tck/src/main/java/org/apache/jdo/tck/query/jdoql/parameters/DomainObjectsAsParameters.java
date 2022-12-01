/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.query.jdoql.parameters;

import java.util.ArrayList;
import java.util.Collection;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.company.CompanyModelReader;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.MedicalInsurance;
import org.apache.jdo.tck.query.QueryTest;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Domain Objects as Parameters. <br>
 * <B>Keywords:</B> query <br>
 * <B>Assertion ID:</B> A14.6.2-61 <br>
 * <B>Assertion Description: </B>
 */
public class DomainObjectsAsParameters extends QueryTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A14.6.2-61 (DomainObjectsAsParameters) failed: ";

  private Object oidDept1Copy;

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(DomainObjectsAsParameters.class);
  }

  /**
   * This methods runs a JDOQL query with an equal operator comparing a field with a parameter of a
   * domain object type.
   */
  @SuppressWarnings("unchecked")
  public void testParameterEqual() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      String filter = "this.department == d";
      Collection<Employee> expectedResult = new ArrayList<>();
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp1"));
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp2"));
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp3"));
      Query<Employee> q = pm.newQuery(Employee.class);
      q.declareParameters("org.apache.jdo.tck.pc.company.Department d");
      q.setFilter(filter);
      Collection<Employee> results =
          (Collection<Employee>)
              q.execute(getPersistentCompanyModelInstance(Department.class, "dept1"));
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * This methods runs a JDOQL query with an equal operator comparing a field with a parameter of a
   * domain object type. The actual parameter is a copy of the domain object referenced by the
   * employees, thus the expected result is empty.
   */
  @SuppressWarnings("unchecked")
  public void testParameterEqualCopy() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      String filter = "this.department == d";
      Collection<Employee> expectedResult = new ArrayList<>();
      Query<Employee> q = pm.newQuery(Employee.class);
      q.declareParameters("org.apache.jdo.tck.pc.company.Department d");
      q.setFilter(filter);
      Collection<Employee> results =
          (Collection<Employee>) q.execute(getPM().getObjectById(oidDept1Copy));
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * This methods runs a JDOQL query with a not equal operator comparing a field with a parameter of
   * a domain object type.
   */
  @SuppressWarnings("unchecked")
  public void testParameterNotEqual() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      String filter = "this.department != d";
      Collection<Employee> expectedResult = new ArrayList<>();
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp4"));
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp5"));
      Query<Employee> q = pm.newQuery(Employee.class);
      q.declareParameters("org.apache.jdo.tck.pc.company.Department d");
      q.setFilter(filter);
      Collection<Employee> results =
          (Collection<Employee>)
              q.execute(getPersistentCompanyModelInstance(Department.class, "dept1"));
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * This methods runs a JDOQL query with an equal operator comparing a field with a domain object
   * navigated from a parameter.
   */
  @SuppressWarnings("unchecked")
  public void testParameterNavigationToDomainObject() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      String filter = "this.department == e.department";
      Collection<Employee> expectedResult = new ArrayList<>();
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp1"));
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp2"));
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp3"));
      Query<Employee> q = pm.newQuery(Employee.class);
      q.declareParameters("org.apache.jdo.tck.pc.company.Employee e");
      q.setFilter(filter);
      Collection<Employee> results =
          (Collection<Employee>)
              q.execute(getPersistentCompanyModelInstance(Employee.class, "emp1"));
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * This methods runs a JDOQL query with an equal operator comparing a field with a domain object
   * navigated from a parameter.
   */
  @SuppressWarnings("unchecked")
  public void testParameterNavigationToPrimitiveField() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      String filter = "this.salary > e.salary";
      Collection<Employee> expectedResult = new ArrayList<>();
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp5"));
      Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class);
      q.declareParameters("org.apache.jdo.tck.pc.company.FullTimeEmployee e");
      q.setFilter(filter);
      Collection<FullTimeEmployee> results =
          (Collection<FullTimeEmployee>)
              q.execute(getPersistentCompanyModelInstance(Employee.class, "emp1"));
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * This methods runs a JDOQL query with an equal operator comparing a field with a domain object
   * navigated from a parameter.
   */
  @SuppressWarnings("unchecked")
  public void testDirtyParameterNavigationToPrimitiveField() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      FullTimeEmployee emp1 = getPersistentCompanyModelInstance(FullTimeEmployee.class, "emp1");
      emp1.setSalary(5000d);

      String filter = "this.salary > e.salary";
      Collection<Employee> expectedResult = new ArrayList<>();
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp2"));
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp5"));
      Query<FullTimeEmployee> q = pm.newQuery(FullTimeEmployee.class);
      q.declareParameters("org.apache.jdo.tck.pc.company.FullTimeEmployee e");
      q.setFilter(filter);
      Collection<FullTimeEmployee> results = (Collection<FullTimeEmployee>) q.execute(emp1);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** This methods runs a JDOQL query with a contains clause using a domain object parameter. */
  @SuppressWarnings("unchecked")
  public void testContainsParameter() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      String filter = "this.employees.contains(e)";
      Collection<Department> expectedResult = new ArrayList<>();
      expectedResult.add(getPersistentCompanyModelInstance(Department.class, "dept1"));
      Query<Department> q = pm.newQuery(Department.class);
      q.declareParameters("org.apache.jdo.tck.pc.company.Employee e");
      q.setFilter(filter);
      Collection<Department> results =
          (Collection<Department>)
              q.execute(getPersistentCompanyModelInstance(Employee.class, "emp1"));
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * This methods runs a JDOQL query with a contains clause using a navigated domain object
   * parameter.
   */
  @SuppressWarnings("unchecked")
  public void testContainsParameterNavigation() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      String filter = "this.employees.contains(ins.employee)";
      Collection<Department> expectedResult = new ArrayList<>();
      expectedResult.add(getPersistentCompanyModelInstance(Department.class, "dept1"));
      Query<Department> q = pm.newQuery(Department.class);
      q.declareParameters("org.apache.jdo.tck.pc.company.Insurance ins");
      q.setFilter(filter);
      Collection<Department> results =
          (Collection<Department>)
              q.execute(getPersistentCompanyModelInstance(MedicalInsurance.class, "medicalIns1"));
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /** This methods runs a JDOQL query with a contains clause using a collection parameter. */
  @SuppressWarnings("unchecked")
  public void testParameterCollection() {
    PersistenceManager pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();

      String filter = "insurances.contains(this.medicalInsurance)";
      Collection<Employee> expectedResult = new ArrayList<>();
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp1"));
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp3"));
      expectedResult.add(getPersistentCompanyModelInstance(Employee.class, "emp4"));
      Query<Employee> q = pm.newQuery(Employee.class);
      q.declareParameters("java.util.Collection insurances");
      q.setFilter(filter);
      Collection<MedicalInsurance> parameters = new ArrayList<>();
      parameters.add(getPersistentCompanyModelInstance(MedicalInsurance.class, "medicalIns1"));
      parameters.add(getPersistentCompanyModelInstance(MedicalInsurance.class, "medicalIns3"));
      parameters.add(getPersistentCompanyModelInstance(MedicalInsurance.class, "medicalIns4"));
      Collection<Employee> results = (Collection<Employee>) q.execute(parameters);
      checkQueryResultWithoutOrder(ASSERTION_FAILED, filter, results, expectedResult);

      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    PersistenceManager pm = getPM();
    addTearDownClass(CompanyModelReader.getTearDownClasses());
    loadAndPersistCompanyModel(pm);

    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Department dept1 = getPersistentCompanyModelInstance(Department.class, "dept1");
      Department dept1Copy =
          new Department(9999, dept1.getName(), dept1.getCompany(), dept1.getEmployeeOfTheMonth());
      pm.makePersistent(dept1Copy);
      oidDept1Copy = pm.getObjectId(dept1Copy);
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
  }
}
