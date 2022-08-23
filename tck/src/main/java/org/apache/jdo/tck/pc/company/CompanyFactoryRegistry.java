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

package org.apache.jdo.tck.pc.company;

import java.lang.reflect.Constructor;

import java.math.BigDecimal;

import java.util.Date;

import javax.jdo.PersistenceManager;

/*
 * This is the registry for company factories. It is used for the
 * CompletenessTest to create instances from input xml test data files.
 * Factory instances that implement CompanyFactory interface are
 * registered (using the singleton pattern).
 * <P>Several registration methods are available. The default factory,
 * which creates instances by construction, is automatically
 * registered during class initialization. The default factory can
 * also be registered by using the no-args method registerFactory().
 * <P>Non-default factories can be registered using the registerFactory
 * method taking the factory as an argument. Non-default factories that
 * have a single constructor argument PersistenceManager can be
 * registered using either the method that explicitly names the class,
 * or with the method that takes the class name from a system property.
 */
public class CompanyFactoryRegistry {

  /** The system property for factory name */
  static final String FACTORY_PROPERTY_NAME = "jdo.tck.mapping.companyfactory";

  /** The factory name if the system property is not set. */
  static final String DEFAULT_FACTORY_CLASS_NAME =
      "org.apache.jdo.tck.pc.company.CompanyFactoryConcreteClass";

  /** The default factory class name */
  static final String FACTORY_CLASS_NAME;

  static {
    String prop = System.getProperty(FACTORY_PROPERTY_NAME);
    if ((prop == null) || (prop.length() == 0)) prop = DEFAULT_FACTORY_CLASS_NAME;
    FACTORY_CLASS_NAME = prop;
  }

  /**
   * This is the default company factory singleton. This is statically loaded regardless of the
   * setting of the system property.
   */
  static final CompanyFactory SINGLETON = new CompanyFactoryConcreteClass();

  /** This is the currently registered factory. */
  static CompanyFactory instance = SINGLETON;

  /** Creates a new instance of CompanyFactoryRegistry */
  private CompanyFactoryRegistry() {}

  /**
   * Get the currently registered factory.
   *
   * @return the factory
   */
  public static CompanyFactory getInstance() {
    return instance;
  }

  /** Register the default factory. */
  public static void registerFactory() {
    instance = SINGLETON;
  }

  /**
   * Register a factory using the default factory name from the system property. The factory must be
   * available in the current class path and have a public constructor taking a PersistenceManager
   * as a parameter.
   *
   * @param pm the PersistenceManager
   */
  public static void registerFactory(PersistenceManager pm) {
    registerFactory(FACTORY_CLASS_NAME, pm);
  }

  /**
   * Register a factory using the specified factoryName parameter. The factory class must be
   * loadable by the current context classloader and have a public constructor taking a
   * PersistenceManager as a parameter.
   *
   * @param factoryClassName the fully-qualified class name of the factory
   * @param pm the PersistenceManager
   */
  public static void registerFactory(String factoryClassName, PersistenceManager pm) {
    CompanyFactory factory = null;
    try {
      if (factoryClassName != null) {
        Class factoryClass = Class.forName(factoryClassName);
        Constructor ctor = factoryClass.getConstructor(new Class[] {PersistenceManager.class});
        factory = (CompanyFactory) ctor.newInstance(new Object[] {pm});
      }
      registerFactory(factory);
    } catch (Exception ex) {
      throw new RuntimeException("Unable to construct CompanyFactory " + factoryClassName, ex);
    }
  }

  /**
   * Register the factory.
   *
   * @param factory the factory
   */
  public static void registerFactory(CompanyFactory factory) {
    instance = factory != null ? factory : SINGLETON;
  }
}
