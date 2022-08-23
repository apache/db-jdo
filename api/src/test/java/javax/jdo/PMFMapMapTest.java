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
package javax.jdo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.jdo.util.BatchTestRunner;
import junit.framework.TestSuite;

/**
 * Tests class javax.jdo.JDOHelper for calls to the impl's static method
 * getPersistenceManagerFactory(Map overrides, Map props).
 */
public class PMFMapMapTest extends AbstractJDOConfigTest {

  private static final String EXPECTED_FACTORY2_NAME = "Jane Doe";
  private static final String EXPECTED_FACTORY2_NAME_4_NAMED_PMF = "Larry";
  private static final String EXPECTED_FACTORY2_NAME_WITH_OVERRIDE = "Gerard Manley Hopkins";
  private static final String PMF_NAME = "BookSearch";
  private static final String RESOURCE_DIR = "/Pmfmapmap01/";
  private static final String PROPS_DIR = "/Pmfmapmap02/";
  private static final String PMF_SERVICE_CLASS = "javax.jdo.stub.StubPMF";
  private static final String PROPERTIES_FILE = "propsfile.props";

  public static void main(String args[]) {
    BatchTestRunner.run(PMFMapMapTest.class);
  }

  /**
   * {@inheritDoc}
   *
   * @return {@inheritDoc}
   */
  public static TestSuite suite() {
    return new TestSuite(PMFMapMapTest.class);
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory() Get the
   * anonymous PersistenceManagerFactory configured via the standard
   * configuration file resource "META-INF/jdoconfig.xml", using the current
   * thread's context class loader to locate the configuration file
   * resource(s).
   */
  public void testJDOConfigXML() throws IOException {

    ClassLoader resourceClassLoader = createResourceClassLoader(RESOURCE_DIR);
    ClassLoader saveContextClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(resourceClassLoader);

    try {
      PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory();
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
    } finally {
      Thread.currentThread().setContextClassLoader(saveContextClassLoader);
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory
   * (java.lang.ClassLoader pmfClassLoader) Get the anonymous
   * PersistenceManagerFactory configured via the standard configuration file
   * resource "META-INF/jdoconfig.xml", using the given class loader.
   */
  public void testJDOConfigXMLWithLoader() throws IOException {

    ClassLoader resourceClassLoader = createResourceClassLoader(RESOURCE_DIR);

    try {
      PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(resourceClassLoader);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory(
   * java.io.File propsFile) Returns a PersistenceManagerFactory configured
   * based on the properties stored in the file at propsFile.
   */
  public void testPropsFile() throws IOException {

    ClassLoader resourceClassLoader = createResourceClassLoader(PROPS_DIR);
    ClassLoader saveContextClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(resourceClassLoader);

    try {
      PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(PROPERTIES_FILE);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
    } finally {
      Thread.currentThread().setContextClassLoader(saveContextClassLoader);
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory(
   * java.io.File propsFile, java.lang.ClassLoader loader) Returns a
   * PersistenceManagerFactory configured based on the properties stored in
   * the file at propsFile.
   */
  public void testPropsFileAndLoader() throws IOException {

    ClassLoader resourceClassLoader = createResourceClassLoader(PROPS_DIR);

    try {
      PersistenceManagerFactory pmf =
          JDOHelper.getPersistenceManagerFactory(PROPERTIES_FILE, resourceClassLoader);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory(
   * java.io.InputStream stream) Returns a PersistenceManagerFactory
   * configured based on the Properties stored in the input stream at stream.
   */
  public void testInputStream() throws IOException {
    Properties props = new Properties();
    props.setProperty(Constants.PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS, PMF_SERVICE_CLASS);
    props.setProperty(Constants.PROPERTY_CONNECTION_FACTORY2_NAME, EXPECTED_FACTORY2_NAME);

    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
    try {
      props.store(outstream, "");
    } catch (IOException ex) {
      fail(ex.getMessage());
    }
    InputStream byteArrayInputStream = new ByteArrayInputStream(outstream.toByteArray());

    ClassLoader resourceClassLoader = createResourceClassLoader(RESOURCE_DIR);
    ClassLoader saveContextClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(resourceClassLoader);

    try {
      PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(byteArrayInputStream);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
    } finally {
      Thread.currentThread().setContextClassLoader(saveContextClassLoader);
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory(
   * java.io.InputStream stream, java.lang.ClassLoader loader) Returns a
   * PersistenceManagerFactory configured based on the Properties stored in
   * the input stream at stream.
   */
  public void testInputStreamWithLoader() throws IOException {
    Properties props = new Properties();
    props.setProperty(Constants.PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS, PMF_SERVICE_CLASS);
    props.setProperty(Constants.PROPERTY_CONNECTION_FACTORY2_NAME, EXPECTED_FACTORY2_NAME);

    ByteArrayOutputStream outstream = new ByteArrayOutputStream();
    try {
      props.store(outstream, "");
    } catch (IOException ex) {
      fail(ex.getMessage());
    }
    InputStream byteArrayInputStream = new ByteArrayInputStream(outstream.toByteArray());

    ClassLoader resourceClassLoader = createResourceClassLoader(RESOURCE_DIR);

    try {
      PersistenceManagerFactory pmf =
          JDOHelper.getPersistenceManagerFactory(byteArrayInputStream, resourceClassLoader);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory(
   * java.util.Map<?,?> props) Get a PersistenceManagerFactory based on a
   * Properties instance, using the current thread's context class loader to
   * locate the PersistenceManagerFactory class.
   */
  public void testProperties() throws IOException {
    Properties props = new Properties();
    props.setProperty(Constants.PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS, PMF_SERVICE_CLASS);
    props.setProperty(Constants.PROPERTY_CONNECTION_FACTORY2_NAME, EXPECTED_FACTORY2_NAME);

    ClassLoader resourceClassLoader = createResourceClassLoader(RESOURCE_DIR);
    ClassLoader saveContextClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(resourceClassLoader);
    try {
      PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(props);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
    } finally {
      Thread.currentThread().setContextClassLoader(saveContextClassLoader);
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory(
   * java.util.Map<?,?> props, java.lang.ClassLoader pmfClassLoader) Get a
   * PersistenceManagerFactory based on a Map and a class loader.
   */
  public void testPropertiesAndLoader() throws IOException {
    Properties props = new Properties();
    props.setProperty(Constants.PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS, PMF_SERVICE_CLASS);
    props.setProperty(Constants.PROPERTY_CONNECTION_FACTORY2_NAME, EXPECTED_FACTORY2_NAME);

    ClassLoader resourceClassLoader = createResourceClassLoader(RESOURCE_DIR);
    try {
      PersistenceManagerFactory pmf =
          JDOHelper.getPersistenceManagerFactory(props, resourceClassLoader);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory
   * (java.util.Map<?,?> overrides, java.lang.String name) Returns a named
   * PersistenceManagerFactory or persistence unit.
   */
  public void testNamedPMFWithOverrides() throws IOException {
    Properties overrides = new Properties();
    overrides.setProperty(
        Constants.PROPERTY_CONNECTION_FACTORY2_NAME, EXPECTED_FACTORY2_NAME_WITH_OVERRIDE);

    ClassLoader resourceClassLoader = createResourceClassLoader(RESOURCE_DIR);
    ClassLoader saveContextClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(resourceClassLoader);

    try {
      PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(overrides, PMF_NAME);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME_WITH_OVERRIDE);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
    } finally {
      Thread.currentThread().setContextClassLoader(saveContextClassLoader);
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory(
   * java.util.Map<?,?> overrides, java.lang.String name,
   * java.lang.ClassLoader resourceLoader) Returns a named
   * PersistenceManagerFactory or persistence unit.
   */
  public void testNamedPMFWithOverridesAndLoader() throws IOException {
    Properties overrides = new Properties();
    overrides.setProperty(
        Constants.PROPERTY_CONNECTION_FACTORY2_NAME, EXPECTED_FACTORY2_NAME_WITH_OVERRIDE);

    ClassLoader resourceClassLoader = createResourceClassLoader(RESOURCE_DIR);

    try {
      PersistenceManagerFactory pmf =
          JDOHelper.getPersistenceManagerFactory(overrides, PMF_NAME, resourceClassLoader);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME_WITH_OVERRIDE);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory(
   * java.util.Map<?,?> overrides, java.lang.String name,
   * java.lang.ClassLoader resourceLoader, java.lang.ClassLoader pmfLoader)
   * Returns a PersistenceManagerFactory configured based on the properties
   * stored in the resource at name, or, if not found, returns a
   * PersistenceManagerFactory with the given name or, if not found, returns a
   * javax.persistence.EntityManagerFactory cast to a
   * PersistenceManagerFactory.
   */
  public void testNamedPMFWithOverridesAndTwoLoaders() throws IOException {
    Properties overrides = new Properties();
    overrides.setProperty(
        Constants.PROPERTY_CONNECTION_FACTORY2_NAME, EXPECTED_FACTORY2_NAME_WITH_OVERRIDE);

    ClassLoader resourceClassLoader = createResourceClassLoader(RESOURCE_DIR);
    ClassLoader pmfLoader = getClass().getClassLoader();

    try {
      PersistenceManagerFactory pmf =
          JDOHelper.getPersistenceManagerFactory(
              overrides, PMF_NAME, resourceClassLoader, pmfLoader);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME_WITH_OVERRIDE);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass. " + ex.getMessage());
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory(
   * java.lang.String name) Returns a named PersistenceManagerFactory or
   * persistence unit.
   */
  public void testNamedPMF() throws IOException {

    ClassLoader resourceClassLoader = createResourceClassLoader(RESOURCE_DIR);
    ClassLoader saveContextClassLoader = Thread.currentThread().getContextClassLoader();
    Thread.currentThread().setContextClassLoader(resourceClassLoader);

    try {
      PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory(PMF_NAME);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME_4_NAMED_PMF);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass. " + ex.getMessage());
    } finally {
      Thread.currentThread().setContextClassLoader(saveContextClassLoader);
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory
   * (java.lang.String name, java.lang.ClassLoader loader) Returns a named
   * PersistenceManagerFactory or persistence unit.
   */
  public void testNamedPMFWithLoader() throws IOException {

    ClassLoader resourceClassLoader = createResourceClassLoader(RESOURCE_DIR);

    try {
      PersistenceManagerFactory pmf =
          JDOHelper.getPersistenceManagerFactory(PMF_NAME, resourceClassLoader);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME_4_NAMED_PMF);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass. " + ex.getMessage());
    }
  }

  /*
   * static PersistenceManagerFactory getPersistenceManagerFactory(
   * java.lang.String name, java.lang.ClassLoader resourceLoader,
   * java.lang.ClassLoader pmfLoader) Returns a named
   * PersistenceManagerFactory or persistence unit.
   */
  public void testNamedPMFWithTwoLoaders() throws IOException {

    ClassLoader resourceClassLoader = createResourceClassLoader(RESOURCE_DIR);
    ClassLoader pmfLoader = getClass().getClassLoader();

    try {
      PersistenceManagerFactory pmf =
          JDOHelper.getPersistenceManagerFactory(PMF_NAME, resourceClassLoader, pmfLoader);
      checkConnectionFactory2Name(pmf, EXPECTED_FACTORY2_NAME_4_NAMED_PMF);
    } catch (JDOFatalUserException ex) {
      fail("Failed to find PersistenceManagerFactoryClass. " + ex.getMessage());
    }
  }

  private ClassLoader createResourceClassLoader(String dir) throws IOException {
    switch (dir.charAt(dir.length() - 1)) {
      case '\\':
        dir = dir.substring(0, dir.length() - 1) + '/';
        break;
      case '/':
        break;
      default:
        if (new File(dir).isDirectory()) {
          dir += '/';
        }
    }
    return new JDOConfigTestClassLoader(
        getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + dir);
  }

  private void checkConnectionFactory2Name(PersistenceManagerFactory pmf, String expectedName) {
    String factory2Name = pmf.getConnectionFactory2Name();
    if (!expectedName.equals(factory2Name)) {
      fail(
          "Bad ConnectionFactory2Name(): "
              + factory2Name
              + ".  Expected: \""
              + expectedName
              + "\"");
    }
  }
}
