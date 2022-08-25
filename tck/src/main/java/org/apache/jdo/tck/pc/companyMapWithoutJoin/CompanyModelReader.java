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

package org.apache.jdo.tck.pc.companyMapWithoutJoin;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.jdo.tck.util.ConversionHelper;
import org.apache.jdo.tck.util.JDOCustomDateEditor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * Utility class to create a graph of company model instances from an xml representation.
 *
 * @author Michael Bouschen
 */
public class CompanyModelReader extends DefaultListableBeanFactory {

  /** The name of the root list bean. */
  public static final String ROOT_LIST_NAME = "root";

  /** The bean-factory name in the xml input files. */
  public static final String BEAN_FACTORY_NAME = "companyFactory";

  /** The company factory instance. */
  private CompanyFactory companyFactory;

  /** Bean definition reader */
  private final XmlBeanDefinitionReader reader;

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
    super();
    configureFactory();
    this.reader = new XmlBeanDefinitionReader(this);
    this.reader.loadBeanDefinitions(new ClassPathResource(resourceName, classLoader));
  }

  /**
   * Returns a list of root objects. The method expects to find a bean called "root" of type list in
   * the xml and returns it.
   *
   * @return a list of root instances
   */
  public List getRootList() {
    return (List) getBean(ROOT_LIST_NAME);
  }

  /**
   * Configure the CompanyModelReader, e.g. register CustomEditor classes to convert the string
   * representation of a property into an instance of the right type.
   */
  private void configureFactory() {
    registerCustomEditor(Date.class, JDOCustomDateEditor.class);
    companyFactory = CompanyFactoryRegistry.getInstance();
    addSingleton(BEAN_FACTORY_NAME, companyFactory);
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
    return (Company) getBean(name, Company.class);
  }

  /**
   * Convenience method returning a Department instance for the specified name. The method returns
   * <code>null</code> if there is no Department bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Department bean.
   */
  public Department getDepartment(String name) {
    return (Department) getBean(name, Department.class);
  }

  /**
   * Convenience method returning an Employee instance for the specified name. The method returns
   * <code>null</code> if there is no Employee bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Employee bean.
   */
  public Employee getEmployee(String name) {
    return (Employee) getBean(name, Employee.class);
  }

  /**
   * Convenience method returning a FullTimeEmployee instance for the specified name. The method
   * returns <code>null</code> if there is no FullTimeEmployee bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no FullTimeEmployee bean.
   */
  public FullTimeEmployee getFullTimeEmployee(String name) {
    return (FullTimeEmployee) getBean(name, FullTimeEmployee.class);
  }

  /**
   * Convenience method returning a PartTimeEmployee instance for the specified name. The method
   * returns <code>null</code> if there is no PartTimeEmployee bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no PartTimeEmployee bean.
   */
  public PartTimeEmployee getPartTimeEmployee(String name) {
    return (PartTimeEmployee) getBean(name, PartTimeEmployee.class);
  }

  /**
   * Convenience method returning a Person instance for the specified name. The method returns
   * <code>null</code> if there is no Person bean with the specified name.
   *
   * @param name the name of the bean to return.
   * @return the instance of the bean or <code>null</code> if there no Person bean.
   */
  public Person getPerson(String name) {
    return (Person) getBean(name, Person.class);
  }

  /**
   * @return Returns the tearDownClasses.
   */
  public Class[] getTearDownClassesFromFactory() {
    return companyFactory.getTearDownClasses();
  }

  /**
   * @return Returns the tearDownClasses.
   */
  public static Class[] getTearDownClasses() {
    return CompanyFactoryConcreteClass.tearDownClasses;
  }

  public static Date stringToUtilDate(String value) {
    return ConversionHelper.toUtilDate(JDOCustomDateEditor.DATE_PATTERN, Locale.US, value);
  }
}
