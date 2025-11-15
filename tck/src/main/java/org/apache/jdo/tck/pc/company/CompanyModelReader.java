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

package org.apache.jdo.tck.pc.company;

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
  private final CompanyFactory companyFactory;

  /**
   * Create a CompanyModelReader for the specified resourceName.
   *
   * @param resourceName the name of the resource
   */
  public CompanyModelReader(String resourceName) {
    companyFactory = CompanyFactoryRegistry.getInstance();
    getDataSource(resourceName).init(companyFactory, this);
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

  // Convenience methods

  /**
   * Convenience method returning an Address instance for the specified name. The method returns
   * <code>null</code> if there is no Address bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Address bean.
   */
  public IAddress getAddress(String name) {
    return getBean(name, Address.class);
  }

  /**
   * Convenience method returning a MeetingRoom instance for the specified name. The method returns
   * <code>null</code> if there is no MeetingRoom bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no MeetingRoom bean.
   */
  public IMeetingRoom getMeetingRoom(String name) {
    return getBean(name, MeetingRoom.class);
  }

  /**
   * Convenience method returning a Company instance for the specified name. The method returns
   * <code>null</code> if there is no Company bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Company bean.
   */
  public ICompany getCompany(String name) {
    return getBean(name, Company.class);
  }

  /**
   * Convenience method returning a DentalInsurance instance for the specified name. The method
   * returns <code>null</code> if there is no DentalInsurance bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no DentalInsurance bean.
   */
  public IDentalInsurance getDentalInsurance(String name) {
    return getBean(name, DentalInsurance.class);
  }

  /**
   * Convenience method returning a Department instance for the specified name. The method returns
   * <code>null</code> if there is no Department bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Department bean.
   */
  public IDepartment getDepartment(String name) {
    return getBean(name, Department.class);
  }

  /**
   * Convenience method returning an Employee instance for the specified name. The method returns
   * <code>null</code> if there is no Employee bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Employee bean.
   */
  public IEmployee getEmployee(String name) {
    return getBean(name, Employee.class);
  }

  /**
   * Convenience method returning a FullTimeEmployee instance for the specified name. The method
   * returns <code>null</code> if there is no FullTimeEmployee bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no FullTimeEmployee bean.
   */
  public IFullTimeEmployee getFullTimeEmployee(String name) {
    return getBean(name, FullTimeEmployee.class);
  }

  /**
   * Convenience method returning an Insurance instance for the specified name. The method returns
   * <code>null</code> if there is no Insurance bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Insurance bean.
   */
  public IInsurance getInsurance(String name) {
    return getBean(name, Insurance.class);
  }

  /**
   * Convenience method returning a MedicalInsurance instance for the specified name. The method
   * returns <code>null</code> if there is no MedicalInsurance bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no MedicalInsurance bean.
   */
  public IMedicalInsurance getMedicalInsurance(String name) {
    return getBean(name, MedicalInsurance.class);
  }

  /**
   * Convenience method returning a PartTimeEmployee instance for the specified name. The method
   * returns <code>null</code> if there is no PartTimeEmployee bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no PartTimeEmployee bean.
   */
  public IPartTimeEmployee getPartTimeEmployee(String name) {
    return getBean(name, PartTimeEmployee.class);
  }

  /**
   * Convenience method returning a Person instance for the specified name. The method returns
   * <code>null</code> if there is no Person bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Person bean.
   */
  public IPerson getPerson(String name) {
    return getBean(name, Person.class);
  }

  /**
   * Convenience method returning a Project instance for the specified name. The method returns
   * <code>null</code> if there is no Project bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Project bean.
   */
  public IProject getProject(String name) {
    return getBean(name, Project.class);
  }
}
