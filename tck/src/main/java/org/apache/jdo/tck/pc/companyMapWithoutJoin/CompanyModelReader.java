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

package org.apache.jdo.tck.pc.companyMapWithoutJoin;

import java.util.*;
import java.util.stream.Collectors;
import org.apache.jdo.tck.pc.order.DefaultListableInstanceFactory;
import org.apache.jdo.tck.util.ConversionHelper;
import org.apache.jdo.tck.util.JDOCustomDateEditor;

/**
 * Utility class to create a graph of company model instances from an xml representation.
 *
 * @author Michael Bouschen
 */
public class CompanyModelReader extends DefaultListableInstanceFactory {

  private static final long serialVersionUID = 1L;

  /** The company factory instance. */
  private CompanyFactory companyFactory;

  /**
   * Create a CompanyModelReader for the specified resourceName.
   *
   * @param resourceName the name of the resource
   */
  public CompanyModelReader(String resourceName) {
    super();
    configureFactory();
    init();
  }

  private void init() {
    ICompany company1 =
        companyFactory.newCompany(1L, "Sun Microsystems, Inc.", new Date(1952, 4, 11, 0, 0, 0));
    super.register("company1", company1);

    IDepartment dept1 = companyFactory.newDepartment(1, "Development", company1);
    IDepartment dept2 = companyFactory.newDepartment(2, "Human Resources", company1);
    company1.setDepartments(toSet(dept1, dept2));

    IFullTimeEmployee emp1 =
        companyFactory.newFullTimeEmployee(
            1,
            "emp1First",
            "emp1Last",
            "emp1Middle",
            new Date(1970, 6, 10, 0, 0, 0),
            new Date(1999, 1, 1, 0, 0, 0),
            "role1",
            60000);

    IFullTimeEmployee emp2 =
        companyFactory.newFullTimeEmployee(
            2,
            "emp2First",
            "emp2Last",
            "emp2Middle",
            new Date(1975, 12, 22, 0, 0, 0),
            new Date(2003, 7, 1, 0, 0, 0),
            "role2",
            47000);

    IFullTimeEmployee emp3 =
        companyFactory.newFullTimeEmployee(
            3,
            "emp3First",
            "emp3Last",
            "emp3Middle",
            new Date(1972, 7, 5, 0, 0, 0),
            new Date(2002, 8, 15, 0, 0, 0),
            "role3",
            67.00);

    IFullTimeEmployee emp4 =
        companyFactory.newFullTimeEmployee(
            4,
            "emp4First",
            "emp4Last",
            "emp4Middle",
            new Date(1973, 7, 6, 0, 0, 0),
            new Date(2001, 4, 15, 0, 0, 0),
            "role4",
            37.00);

    IFullTimeEmployee emp5 =
        companyFactory.newFullTimeEmployee(
            5,
            "emp5First",
            "emp5Last",
            "emp5Middle",
            new Date(1962, 7, 5, 0, 0, 0),
            new Date(1998, 8, 15, 0, 0, 0),
            "role5",
            73000);

    emp1.setWeeklyhours(40);
    emp1.setDepartment(dept1);
    emp1.setFundingDept(dept2);
    emp1.setManager(emp2);
    emp1.setHradvisor(emp5);
    dept1.add(emp1); // TODO do this inside setDepartment

    emp2.setWeeklyhours(40);
    emp2.setDepartment(dept1);
    emp2.setFundingDept(dept1);
    emp2.setHradvisor(emp5);
    emp2.setTeam(toSet(emp1, emp3, emp4, emp5));
    dept1.add(emp2); // TODO do this inside setDepartment

    emp3.setWeeklyhours(19);
    emp3.setDepartment(dept1);
    emp3.setFundingDept(dept1);
    emp3.setManager(emp2);
    emp3.setHradvisor(emp5);
    dept1.add(emp3); // TODO do this inside setDepartment

    // emp4.setWeeklyhours(19);
    emp4.setDepartment(dept2);
    emp4.setFundingDept(dept2);
    emp4.setManager(emp2);
    emp4.setHradvisor(emp5);
    dept2.add(emp4); // TODO do this inside setDepartment

    // emp5.setWeeklyhours(19);
    emp5.setDepartment(dept2);
    emp5.setFundingDept(dept2);
    emp5.setManager(emp2);
    emp5.setHradvisees(toSet(emp1, emp2, emp3, emp4));
    dept2.add(emp5); // TODO do this inside setDepartment
  }

  private <T> Set<T> toSet(T... objs) {
    return Arrays.stream(objs).collect(Collectors.toSet());
  }

  /**
   * Configure the CompanyModelReader, e.g. register CustomEditor classes to convert the string
   * representation of a property into an instance of the right type.
   */
  private void configureFactory() {
    // registerCustomEditor(Date.class, JDOCustomDateEditor.class);
    companyFactory = CompanyFactoryRegistry.getInstance();
    // addSingleton(BEAN_FACTORY_NAME, companyFactory);
  }

  // Convenience methods

  /**
   * Convenience method returning a Company instance for the specified name. The method returns
   * <code>null</code> if there is no Company bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Company bean.
   */
  public Company getCompany(String name) {
    return getBean(name, Company.class);
  }

  /**
   * Convenience method returning a Department instance for the specified name. The method returns
   * <code>null</code> if there is no Department bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Department bean.
   */
  public Department getDepartment(String name) {
    return getBean(name, Department.class);
  }

  /**
   * Convenience method returning an Employee instance for the specified name. The method returns
   * <code>null</code> if there is no Employee bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Employee bean.
   */
  public Employee getEmployee(String name) {
    return getBean(name, Employee.class);
  }

  /**
   * Convenience method returning a FullTimeEmployee instance for the specified name. The method
   * returns <code>null</code> if there is no FullTimeEmployee bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no FullTimeEmployee bean.
   */
  public FullTimeEmployee getFullTimeEmployee(String name) {
    return getBean(name, FullTimeEmployee.class);
  }

  /**
   * Convenience method returning a PartTimeEmployee instance for the specified name. The method
   * returns <code>null</code> if there is no PartTimeEmployee bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no PartTimeEmployee bean.
   */
  public PartTimeEmployee getPartTimeEmployee(String name) {
    return getBean(name, PartTimeEmployee.class);
  }

  /**
   * Convenience method returning a Person instance for the specified name. The method returns
   * <code>null</code> if there is no Person bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Person bean.
   */
  public Person getPerson(String name) {
    return getBean(name, Person.class);
  }

  /**
   * @return Returns the tearDownClasses.
   */
  public Class<?>[] getTearDownClassesFromFactory() {
    return companyFactory.getTearDownClasses();
  }

  /**
   * @return Returns the tearDownClasses.
   */
  public static Class<?>[] getTearDownClasses() {
    return CompanyFactoryConcreteClass.tearDownClasses;
  }

  public static Date stringToUtilDate(String value) {
    return ConversionHelper.toUtilDate(JDOCustomDateEditor.DATE_PATTERN, Locale.US, value);
  }
}
