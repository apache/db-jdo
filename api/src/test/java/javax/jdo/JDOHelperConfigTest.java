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
package javax.jdo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import javax.jdo.util.BatchTestRunner;
import junit.framework.TestSuite;

/** Tests class javax.jdo.JDOHelper for META-INF/jdoconfig.xml compliance. */
public class JDOHelperConfigTest extends AbstractJDOConfigTest {

  public static void main(String args[]) {
    BatchTestRunner.run(JDOHelperConfigTest.class);
  }

  /**
   * {@inheritDoc}
   *
   * @return {@inheritDoc}
   */
  public static TestSuite suite() {
    return new TestSuite(JDOHelperConfigTest.class);
  }

  /**
   * Builds up a {@link java.util.Map Map} object that contains key parameter values specific to a
   * named test. All of the properties needed to run a particular test are loaded into this object.
   *
   * @param testVariant the name of the test to include in the {@link java.util.Map Map} values.
   * @param listenerCount the number of life cycle listener class names to add to this map. The
   *     listener names will begin with the value stored in {@link
   *     javax.jdo.JDOConstants#PROPERTY_INSTANCE_LIFECYCLE_LISTENER
   *     PROPERTY_INSTANCE_LIFECYCLE_LISTENER}.
   * @param vendorSpecificPropertyCount the number of properties named of the form
   *     <pre>
   * &quot;property.&quot; + testVariant + &quot;.name&quot;
   * </pre>
   *     that are added to the map.
   * @param excludeName if true the property specified by {@link
   *     javax.jdo.JDOConstants#PROPERTY_NAME PROPERTY_NAME} is not added to the map.
   * @param excludePUName if true the property specified by {@link
   *     javax.jdo.JDOConstants#PROPERTY_PERSISTENCE_UNIT_NAME PROPERTY_PERSISTENCE_UNIT_NAME} is
   *     not added to the map.
   * @return a new {@link java.util.Map Map} object populated with properties that can be used in
   *     this test suite.
   */
  protected Map<String, String> prepareInitialExpectedMap(
      String testVariant,
      int listenerCount,
      int vendorSpecificPropertyCount,
      boolean excludeName,
      boolean excludePUName) {
    Map<String, String> expected = new HashMap<>();

    if (!excludeName) {
      expected.put(JDOConstants.PROPERTY_NAME, JDOConstants.PMF_ATTRIBUTE_NAME + "." + testVariant);
    }
    if (!excludePUName) {
      expected.put(
          JDOConstants.PROPERTY_PERSISTENCE_UNIT_NAME,
          JDOConstants.PMF_ATTRIBUTE_PERSISTENCE_UNIT_NAME + "." + testVariant);
    }

    expected.put(
        JDOConstants.PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS,
        JDOConstants.PMF_ATTRIBUTE_CLASS + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_CONNECTION_DRIVER_NAME,
        JDOConstants.PMF_ATTRIBUTE_CONNECTION_DRIVER_NAME + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_CONNECTION_FACTORY_NAME,
        JDOConstants.PMF_ATTRIBUTE_CONNECTION_FACTORY_NAME + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_CONNECTION_FACTORY2_NAME,
        JDOConstants.PMF_ATTRIBUTE_CONNECTION_FACTORY2_NAME + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_CONNECTION_PASSWORD,
        JDOConstants.PMF_ATTRIBUTE_CONNECTION_PASSWORD + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_CONNECTION_URL,
        JDOConstants.PMF_ATTRIBUTE_CONNECTION_URL + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_CONNECTION_USER_NAME,
        JDOConstants.PMF_ATTRIBUTE_CONNECTION_USER_NAME + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_IGNORE_CACHE,
        JDOConstants.PMF_ATTRIBUTE_IGNORE_CACHE + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_MAPPING, JDOConstants.PMF_ATTRIBUTE_MAPPING + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_MULTITHREADED,
        JDOConstants.PMF_ATTRIBUTE_MULTITHREADED + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_NONTRANSACTIONAL_READ,
        JDOConstants.PMF_ATTRIBUTE_NONTRANSACTIONAL_READ + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_NONTRANSACTIONAL_WRITE,
        JDOConstants.PMF_ATTRIBUTE_NONTRANSACTIONAL_WRITE + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_OPTIMISTIC,
        JDOConstants.PMF_ATTRIBUTE_OPTIMISTIC + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_RESTORE_VALUES,
        JDOConstants.PMF_ATTRIBUTE_RESTORE_VALUES + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_RETAIN_VALUES,
        JDOConstants.PMF_ATTRIBUTE_RETAIN_VALUES + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_DETACH_ALL_ON_COMMIT,
        JDOConstants.PMF_ATTRIBUTE_DETACH_ALL_ON_COMMIT + "." + testVariant);
    expected.put(
        JDOConstants.PROPERTY_SERVER_TIME_ZONE_ID,
        JDOConstants.PMF_ATTRIBUTE_SERVER_TIME_ZONE_ID + "." + testVariant);

    // listeners
    for (int i = 0; i < listenerCount; i++) {
      expected.put(
          JDOConstants.PROPERTY_PREFIX_INSTANCE_LIFECYCLE_LISTENER
              + "listener."
              + testVariant
              + ".listener"
              + i,
          "classes." + testVariant + ".classes" + i);
    }

    // vendor-specific properties
    for (int i = 0; i < vendorSpecificPropertyCount; i++) {
      expected.put(
          "property." + testVariant + ".name" + i, "property." + testVariant + ".value" + i);
    }

    return expected;
  }

  /**
   * Performs a test specified by <tt>testVariantName</tt>, by building up a property map and
   * executing the test according to the property values. With this version of
   * <tt>doPositiveTest</tt> the property name ( {@link javax.jdo.JDOConstants#PROPERTY_NAME
   * PROPERTY_NAME}) and the {@link javax.jdo.JDOConstants#PROPERTY_PERSISTENCE_UNIT_NAME
   * PERSISTENCE_UNIT_NAME}) are included in the property map that is used to run the test.
   *
   * @param classpaths class paths to add to the class loader that runs the test that specify where
   *     <tt>jdoconfig.xml</tt> can be found.
   * @param testVariantName the name of the test.
   * @param listenerCount number of listeners utilized in the test.
   * @param vendorSpecificPropertyCount number of vendor properties used in the test.
   * @param checkEqualProperties if true the test's properties are tested.
   * @throws java.io.IOException if an {@java.io.IOException IOException} occurs during class
   *     loading or any part of the test.
   */
  protected void doPositiveTest(
      String[] classpaths,
      String testVariantName,
      int listenerCount,
      int vendorSpecificPropertyCount,
      boolean checkEqualProperties)
      throws IOException {

    doPositiveTest(
        classpaths,
        testVariantName,
        listenerCount,
        vendorSpecificPropertyCount,
        checkEqualProperties,
        false,
        false);
  }

  /**
   * Performs a test specified by <tt>testVariantName</tt>, by building up a property map and
   * executing the test according to the property values. An assertion exeception is thrown if the
   * test being run has a negative (non-true) result. With this version of <tt>doPositiveTest</tt>
   * the property name ({@link javax.jdo.JDOConstants#PROPERTY_NAME PROPERTY_NAME}) and the {@link
   * javax.jdo.JDOConstants#PROPERTY_PERSISTENCE_UNIT_NAME PROPERTY_PERSISTENCE_UNIT_NAME}) are
   * included in the property map that is used to run the test.
   *
   * @param classpaths class paths to add to the class loader that runs the test that specify where
   *     <tt>jdoconfig.xml</tt> can be found.
   * @param testVariantName the name of the test.
   * @param listenerCount number of listeners utilized in the test.
   * @param vendorSpecificPropertyCount number of vendor properties used in the test.
   * @param checkEqualProperties if true the test's properties are tested.
   * @param excludeName if true the property specified by {@link
   *     javax.jdo.JDOConstants#PROPERTY_NAME PROPERTY_NAME} is not added to the map.
   * @param excludePUName if true the property specified by {@link
   *     javax.jdo.JDOConstants#PROPERTY_PERSISTENCE_UNIT_NAME PROPERTY_PERSISTENCE_UNIT_NAME} is
   *     not added to the map.
   * @throws java.io.IOException if an {@java.io.IOException IOException} occurs during class
   *     loading or any part of the test.
   */
  protected void doPositiveTest(
      String[] classpaths,
      String testVariantName,
      int listenerCount,
      int vendorSpecificPropertyCount,
      boolean checkEqualProperties,
      boolean excludeName,
      boolean excludePUName)
      throws IOException {

    URLClassLoader loader = new JDOConfigTestClassLoader(getClass().getClassLoader(), classpaths);
    Map<String, String> expected =
        prepareInitialExpectedMap(
            testVariantName,
            listenerCount,
            vendorSpecificPropertyCount,
            excludeName,
            excludePUName);
    String name = testVariantName == null ? null : expected.get(JDOConstants.PROPERTY_NAME);
    Map<Object, Object> actual = JDOHelper.getPropertiesFromJdoconfig(name, loader);

    assertNotNull("No properties found", actual);
    if (checkEqualProperties) {
      assertEqualProperties(expected, actual);
    }
  }

  public void testPositive00_PMF0_BasicPMFConfigUsingOnlyStandardAttributesAndListeners()
      throws IOException {
    doPositiveTest(
        new String[] {JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"}, "positive00.pmf0", 2, 0, true);
  }

  public void
      testPositive00_PMF1_BasicPMFConfigUsingOnlyPropertyElementsWithStandardJavaxDotJDOProperties()
          throws IOException {
    doPositiveTest(
        new String[] {JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"}, "positive00.pmf1", 2, 0, true);
  }

  public void testPositive00_PMF2_NestedPropertyElementsWithOnlyStandardAttributeNames()
      throws IOException {
    doPositiveTest(
        new String[] {JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"}, "positive00.pmf2", 2, 0, true);
  }

  public void testPositive00_PMF3_StandardAttributesPlusNonstandardPropertiesInPropertyElements()
      throws IOException {
    doPositiveTest(
        new String[] {JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"}, "positive00.pmf3", 2, 2, true);
  }

  public void testPositive00_PMF4_StandardAttributesPlusNonstandardAttributes() throws IOException {
    doPositiveTest(
        new String[] {JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"}, "positive00.pmf4", 0, 2, true);
  }

  public void testPositive01_DuplicatePUsInDifferentConfigFilesButNotRequested()
      throws IOException {
    String[] classpaths =
        new String[] {
          JDOCONFIG_CLASSPATH_PREFIX + "/Positive01/1a",
          JDOCONFIG_CLASSPATH_PREFIX + "/Positive01/1b"
        };
    URLClassLoader loader = new JDOConfigTestClassLoader(getClass().getClassLoader(), classpaths);
    Map<Object, Object> actual =
        JDOHelper.getPropertiesFromJdoconfig(
            JDOConstants.ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, loader);
  }

  public void testPositive02_GetAnonymousPMFWithNoProperties() throws IOException {

    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Positive02/");

    Map<Object, Object> properties =
        JDOHelper.getPropertiesFromJdoconfig(
            JDOConstants.ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, loader);
    assertNotNull("Anonymous PMF with no properties returned null", properties);
    assertTrue("Anonymous PMF with no properties had properties", properties.size() == 0);
  }

  public void testPositive03_PMF0_PMFClassNameViaServicesLookup() throws IOException {
    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Positive03/");

    String expected = "class.positive03.pmf0";
    String actual = getPMFClassNameViaServiceLookup(loader);

    assertNotNull("No PMF name found via services lookup", actual);
    assertEquals(expected, actual);
  }

  public void testPositive04_PMF0_PMFClassNameViaServicesLookup() throws IOException {

    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Positive04/");

    String expected = "class.positive04.pmf0";
    String actual = getPMFClassNameViaServiceLookup(loader);

    assertNotNull("No PMF name found via services lookup", actual);
    assertEquals(expected, actual);
  }

  public void testPositive05_PMF0_PMFClassNameViaServicesLookup() throws IOException {

    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Positive05/");

    String expected = "class.positive05.pmf0";
    String actual = getPMFClassNameViaServiceLookup(loader);

    assertNotNull("No PMF name found via services lookup", actual);
    assertEquals(expected, actual);
  }

  public void testPositive06_PMF0_GetAnonymousPMFProperties() throws IOException {

    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Positive06/");

    Map<String, String> expected = prepareInitialExpectedMap("positive06.pmf0", 2, 0, true, true);
    Map<Object, Object> actual =
        JDOHelper.getPropertiesFromJdoconfig(
            JDOConstants.ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, loader);

    assertNotNull("No properties found", actual);
    assertEqualProperties(expected, actual);
  }

  public void testPositive07_PMF0_GetAnonymousPMFPropertiesWithPUName() throws IOException {

    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Positive07/");

    Map<String, String> expected = prepareInitialExpectedMap("positive07.pmf0", 2, 0, true, false);
    Map<Object, Object> actual =
        JDOHelper.getPropertiesFromJdoconfig(
            JDOConstants.ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, loader);

    assertNotNull("No properties found", actual);
    assertEqualProperties(expected, actual);
  }

  public void testNegative00_EmptyJDOConfigXML() throws IOException {
    try {
      URLClassLoader loader =
          new JDOConfigTestClassLoader(
              getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Negative0/");
      JDOHelper.getPersistenceManagerFactory(loader);
      fail("JDOHelper failed to throw JDOFatalUserException");
    } catch (JDOFatalUserException x) {
      // sunny day
    }
  }

  public void testNegative01_NoPersistenceUnitsDefined() throws IOException {
    try {
      URLClassLoader loader =
          new JDOConfigTestClassLoader(
              getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Negative01/");
      JDOHelper.getPersistenceManagerFactory(loader);
      fail("JDOHelper failed to throw JDOFatalUserException");
    } catch (JDOFatalUserException x) {
      // joy, sweet joy
    }
  }

  public void testNegative02_DuplicateAnonymousPersistenceUnitsInSameConfig() throws IOException {
    try {
      URLClassLoader loader =
          new JDOConfigTestClassLoader(
              getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Negative02/");
      JDOHelper.getPersistenceManagerFactory(loader);
      fail("JDOHelper failed to throw JDOFatalUserException");
    } catch (JDOFatalUserException x) {
      // the cockles of my heart warmeth
    }
  }

  public void testNegative03_DuplicateNamedPersistenceUnitsInSameConfig() throws IOException {
    try {
      URLClassLoader loader =
          new JDOConfigTestClassLoader(
              getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Negative03/");
      JDOHelper.getPersistenceManagerFactory("name.negative03", loader);
      fail("JDOHelper failed to throw JDOFatalUserException");
    } catch (JDOFatalUserException x) {
      // warm fuzzies
    }
  }

  public void testNegative04_DuplicatePUNamePropertyInAttributeAndElement() throws IOException {
    try {
      URLClassLoader loader =
          new JDOConfigTestClassLoader(
              getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Negative04/");
      JDOHelper.getPersistenceManagerFactory("name.negative04.value0", loader);
      fail("JDOHelper failed to throw JDOFatalUserException");
    } catch (JDOFatalUserException x) {
      // no cold pricklies
    }
  }

  public void testNegative05_DuplicatePropertyInAttributeAndElement() throws IOException {
    try {
      URLClassLoader loader =
          new JDOConfigTestClassLoader(
              getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Negative05/");
      JDOHelper.getPersistenceManagerFactory(loader);
      fail("JDOHelper failed to throw JDOFatalUserException");
    } catch (JDOFatalUserException x) {
      // party!
    }
  }

  public void testNegative06_DuplicatePUInDifferentConfigFiles() throws IOException {
    try {
      URLClassLoader loader =
          new JDOConfigTestClassLoader(
              getClass().getClassLoader(),
              JDOCONFIG_CLASSPATH_PREFIX + "/Negative06/6a/",
              JDOCONFIG_CLASSPATH_PREFIX + "/Negative06/6b/");
      JDOHelper.getPersistenceManagerFactory("name.negative06", loader);
      fail("JDOHelper failed to throw JDOFatalUserException");
    } catch (JDOFatalUserException x) {
      // clear blue sky
    }
  }

  public void testNegative07_EmptyServicesFile() throws IOException {
    JDOConfigTestClassLoader testLoader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Negative07/");
    String shouldBeNull = getPMFClassNameViaServiceLookup(testLoader);
    assertNull(shouldBeNull);
  }

  public void testNegative08_NoResourcesFound() {
    String resource = "" + RANDOM.nextLong();
    InputStream in = getClass().getClassLoader().getResourceAsStream(resource);
    assertNull(in);

    // resource pretty much guaranteed not to exist
    try {
      JDOHelper.getPersistenceManagerFactory(resource);
      fail("JDOHelper failed to throw JDOFatalUserException");
    } catch (JDOFatalUserException x) {
      // happy path
    }
  }

  public void testNegative08_ServicesFileWithOnlyComments() throws IOException {
    JDOConfigTestClassLoader testLoader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Negative08/");
    String shouldBeNull = getPMFClassNameViaServiceLookup(testLoader);
    assertNull(shouldBeNull);
  }

  public void testNegative09_MultipleInvalidClassesInDifferentServicesFiles() throws IOException {

    // no class name in Negative09/jdoconfig.xml
    // 9a and 9b include services/javax.jdo.PersistenceManagerFactory
    // with bad implementations
    try {
      URLClassLoader loader =
          new JDOConfigTestClassLoader(
              getClass().getClassLoader(),
              JDOCONFIG_CLASSPATH_PREFIX + "/Negative09/9a/",
              JDOCONFIG_CLASSPATH_PREFIX + "/Negative09/9b/",
              TEST_CLASSPATH,
              API_CLASSPATH);
      JDOHelper.getPersistenceManagerFactory("name.negative09", loader);
      fail("JDOHelper failed to throw JDOFatalUserException");
    } catch (JDOFatalException x) {
      Throwable[] nestedExceptions = x.getNestedExceptions();
      if (nestedExceptions.length != 2) {
        appendMessage(
            "JDOHelper.getPersistenceManagerFactory wrong number of "
                + "nested exceptions. Expected 2, got "
                + nestedExceptions.length
                + "\n"
                + x);
      }
      for (int i = 0; i < nestedExceptions.length; ++i) {
        Throwable exception = nestedExceptions[i];
        if (!(exception instanceof JDOFatalException)) {
          appendMessage(
              "Nested exception "
                  + exception.getClass().getName()
                  + " is not a JDOFatalException.");
        }
      }
    }
    failOnError();
  }
}
