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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import org.apache.jdo.tck.pc.company.data.*;
import org.apache.jdo.tck.pc.mylib.MylibReader;
import org.apache.jdo.tck.util.ConversionHelper;
import org.apache.jdo.tck.util.DataSource;
import org.apache.jdo.tck.util.DefaultListableInstanceFactory;
import org.apache.jdo.tck.util.JDOCustomDateEditor;

/**
 * Utility class to create a graph of company model instances from an xml representation.
 *
 * @author Michael Bouschen
 */
public class CompanyModelReader extends DefaultListableInstanceFactory {

  private static final long serialVersionUID = 1L;

  public static final String QUERY_TEST = "org/apache/jdo/tck/pc/company/companyForQueryTests.xml";
  public static final String MYLIB_TEST = "org/apache/jdo/tck/pc/mylib/mylibForQueryTests.xml";
  public static final String SAMPLE_QUERIES_TEST =
      "org/apache/jdo/tck/pc/company/companyForSampleQueriesTest.xml";
  public static final String JDOQL_NAVIGATION_TESTS =
      "org/apache/jdo/tck/pc/company/companyForNavigationTests.xml";
  public static final String JDOQL_SUBQUERIES_TESTS =
      "org/apache/jdo/tck/pc/company/companyForSubqueriesTests.xml";

  public static final String RELATIONSHIPS_ALL =
      "org/apache/jdo/tck/pc/company/companyAllRelationships.xml";
  public static final String RELATIONSHIPS_1_1 =
      "org/apache/jdo/tck/pc/company/company1-1Relationships.xml";
  public static final String RELATIONSHIPS_1_M =
      "org/apache/jdo/tck/pc/company/company1-MRelationships.xml";
  public static final String EMBEDDED = "org/apache/jdo/tck/pc/company/companyEmbedded.xml";
  public static final String RELATIONSHIPS_M_M =
      "org/apache/jdo/tck/pc/company/companyM-MRelationships.xml";
  public static final String RELATIONSHIPS_NO =
      "org/apache/jdo/tck/pc/company/companyNoRelationships.xml";

  /** The company factory instance. */
  private CompanyFactory companyFactory;

  /** Bean definition reader */
  private final CompanyModelReaderOld reader;

  /**
   * Create a CompanyModelReader for the specified resourceName.
   *
   * @param resourceName the name of the resource
   */
  public CompanyModelReader(String resourceName) {
    // Use the class loader of the Company class to find the resource
    this(resourceName, Company.class.getClassLoader());
  }

  public CompanyModelReader(String resourceName, int dummy) {
    super();
    this.reset();
    configureFactory();
    reader = null;
    try {
      Class<DataSource<CompanyFactory>> cls =
          (Class<DataSource<CompanyFactory>>) Class.forName(resourceName);
      Constructor<DataSource<CompanyFactory>> cstr = cls.getConstructor();
      DataSource<CompanyFactory> ds = cstr.newInstance();
      ds.initMe(companyFactory, this);
    } catch (ClassNotFoundException
        | InvocationTargetException
        | NoSuchMethodException
        | InstantiationException
        | IllegalAccessException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Create a CompanyModelReader for the specified resourceName.
   *
   * @param resourceName the name of the resource
   * @param classLoader the ClassLoader for the lookup
   */
  public CompanyModelReader(String resourceName, ClassLoader classLoader) {
    super();
    this.reset();
    configureFactory();
    reader = null;

    switch (resourceName) {
      case RELATIONSHIPS_ALL:
        RelationshipsAllData.init(companyFactory, this);
        break;
      case RELATIONSHIPS_1_1:
        Relationships1_1Data.init(companyFactory, this);
        break;
      case RELATIONSHIPS_1_M:
        Relationships1_MData.init(companyFactory, this);
        break;
      case RELATIONSHIPS_M_M:
        RelationshipsM_MData.init(companyFactory, this);
        break;
      case RELATIONSHIPS_NO:
        RelationshipsNoData.init(companyFactory, this);
        break;
      case EMBEDDED:
        EmbeddedTestData.init(companyFactory, this);
        break;
      case JDOQL_NAVIGATION_TESTS:
        NavigationTestData.initNavigationTest(companyFactory, this);
        break;
      case JDOQL_SUBQUERIES_TESTS:
        SubqueryTestData.initSubqueryTest(companyFactory, this);
        break;
      case QUERY_TEST:
        QueryTestData.initQueryTest(companyFactory, this);
        break;
      case SAMPLE_QUERIES_TEST:
        SampleQueryTestData.initSampleQueryTest(companyFactory, this);
        break;
      case MYLIB_TEST:
        // TODO use companyModelFactory!
        MylibReader.init(this);
        break;
      default:
        //        this.reader = null;
        System.err.println("ERROR: Not registered: " + resourceName);
        // throw new IllegalArgumentException("Not registered: " + resourceName);
    }
  }

  public <T> T getBean(String name, Class<T> clazz) {
    if (reader != null) {
      return reader.getBean(name, clazz);
    }
    return super.getBean(name, clazz);
  }

  /**
   * Returns a list of root objects. The method expects to find a bean called "root" of type list in
   * the xml and returns it.
   *
   * @return a list of root instances
   */
  public List<Object> getRootList() {
    if (reader != null) {
      return reader.getRootList();
    }
    return super.getRootList();
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

  /**
   * @return Returns the tearDownClasses.
   */
  public Class<?>[] getTearDownClassesFromFactory() {
    if (reader != null) {
      return reader.getTearDownClassesFromFactory();
    }

    for (Class<?> c : companyFactory.getTearDownClasses()) {
      System.err.println("TearDownClass: " + c);
    }
    System.err.println("TearDownClass: " + Arrays.toString(companyFactory.getTearDownClasses()));
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
