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

package org.apache.jdo.tck.pc.companyListWithoutJoin;

import java.util.Date;
import java.util.Locale;

import org.apache.jdo.tck.util.ConversionHelper;
import org.apache.jdo.tck.util.DefaultListableInstanceFactory;
import org.apache.jdo.tck.util.JDOCustomDateEditor;

/**
 * Utility class to create a graph of company model instances from an xml representation.
 *
 * @author Michael Bouschen
 */
public class CompanyModelReader extends DefaultListableInstanceFactory {

  /** The company factory instance. */
  private CompanyFactory companyFactory;

  /**
   * Create a CompanyModelReader for the specified resourceName.
   *
   * @param resourceName the name of the resource
   */
  public CompanyModelReader(String resourceName) {
    // Use the class loader of the Company class to find the resource
    this(resourceName, Company.class.getClassLoader());
  }

  /**
   * Create a CompanyModelReader for the specified resourceName.
   *
   * @param resourceName the name of the resource
   * @param classLoader the ClassLoader for the lookup
   */
  public CompanyModelReader(String resourceName, ClassLoader classLoader) {
    companyFactory = CompanyFactoryRegistry.getInstance();
    getDataSource(resourceName).init(companyFactory, this);
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
