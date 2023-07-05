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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/** Tests class javax.jdo.JDOHelper for META-INF/jdoconfig.xml compliance. */
class JDOHelperConfigTest extends AbstractJDOConfigTest implements Constants {

  /**
   * Builds up a {@link java.util.Map Map} object that contains key parameter values specific to a
   * named test. All of the properties needed to run a particular test are loaded into this object.
   *
   * @param testVariant the name of the test to include in the {@link java.util.Map Map} values.
   * @param listenerCount the number of life cycle listener class names to add to this map. The
   *     listener names will begin with the value stored in {@link
   *     javax.jdo.Constants.PROPERTY_INSTANCE_LIFECYCLE_LISTENER
   *     PROPERTY_INSTANCE_LIFECYCLE_LISTENER}.
   * @param vendorSpecificPropertyCount the number of properties named of the form
   *     <pre>
   * &quot;property.&quot; + testVariant + &quot;.name&quot;
   * </pre>
   *     that are added to the map.
   * @param excludeName if true the property specified by {@link javax.jdo.Constants.PROPERTY_NAME
   *     PROPERTY_NAME} is not added to the map.
   * @param excludePUName if true the property specified by {@link
   *     javax.jdo.Constants.PROPERTY_PERSISTENCE_UNIT_NAME PROPERTY_PERSISTENCE_UNIT_NAME} is not
   *     added to the map.
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
      expected.put(Constants.PROPERTY_NAME, Constants.PMF_ATTRIBUTE_NAME + "." + testVariant);
    }
    if (!excludePUName) {
      expected.put(
          Constants.PROPERTY_PERSISTENCE_UNIT_NAME,
          Constants.PMF_ATTRIBUTE_PERSISTENCE_UNIT_NAME + "." + testVariant);
    }

    expected.put(
        Constants.PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS,
        Constants.PMF_ATTRIBUTE_CLASS + "." + testVariant);
    expected.put(
        Constants.PROPERTY_CONNECTION_DRIVER_NAME,
        Constants.PMF_ATTRIBUTE_CONNECTION_DRIVER_NAME + "." + testVariant);
    expected.put(
        Constants.PROPERTY_CONNECTION_FACTORY_NAME,
        Constants.PMF_ATTRIBUTE_CONNECTION_FACTORY_NAME + "." + testVariant);
    expected.put(
        Constants.PROPERTY_CONNECTION_FACTORY2_NAME,
        Constants.PMF_ATTRIBUTE_CONNECTION_FACTORY2_NAME + "." + testVariant);
    expected.put(
        Constants.PROPERTY_CONNECTION_PASSWORD,
        Constants.PMF_ATTRIBUTE_CONNECTION_PASSWORD + "." + testVariant);
    expected.put(
        Constants.PROPERTY_CONNECTION_URL,
        Constants.PMF_ATTRIBUTE_CONNECTION_URL + "." + testVariant);
    expected.put(
        Constants.PROPERTY_CONNECTION_USER_NAME,
        Constants.PMF_ATTRIBUTE_CONNECTION_USER_NAME + "." + testVariant);
    expected.put(
        Constants.PROPERTY_IGNORE_CACHE, Constants.PMF_ATTRIBUTE_IGNORE_CACHE + "." + testVariant);
    expected.put(Constants.PROPERTY_MAPPING, Constants.PMF_ATTRIBUTE_MAPPING + "." + testVariant);
    expected.put(
        Constants.PROPERTY_MULTITHREADED,
        Constants.PMF_ATTRIBUTE_MULTITHREADED + "." + testVariant);
    expected.put(
        Constants.PROPERTY_NONTRANSACTIONAL_READ,
        Constants.PMF_ATTRIBUTE_NONTRANSACTIONAL_READ + "." + testVariant);
    expected.put(
        Constants.PROPERTY_NONTRANSACTIONAL_WRITE,
        Constants.PMF_ATTRIBUTE_NONTRANSACTIONAL_WRITE + "." + testVariant);
    expected.put(
        Constants.PROPERTY_OPTIMISTIC, Constants.PMF_ATTRIBUTE_OPTIMISTIC + "." + testVariant);
    expected.put(
        Constants.PROPERTY_RESTORE_VALUES,
        Constants.PMF_ATTRIBUTE_RESTORE_VALUES + "." + testVariant);
    expected.put(
        Constants.PROPERTY_RETAIN_VALUES,
        Constants.PMF_ATTRIBUTE_RETAIN_VALUES + "." + testVariant);
    expected.put(
        Constants.PROPERTY_DETACH_ALL_ON_COMMIT,
        Constants.PMF_ATTRIBUTE_DETACH_ALL_ON_COMMIT + "." + testVariant);
    expected.put(
        Constants.PROPERTY_SERVER_TIME_ZONE_ID,
        Constants.PMF_ATTRIBUTE_SERVER_TIME_ZONE_ID + "." + testVariant);

    // listeners
    for (int i = 0; i < listenerCount; i++) {
      expected.put(
          Constants.PROPERTY_PREFIX_INSTANCE_LIFECYCLE_LISTENER
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
   * <tt>doPositiveTest</tt> the property name ( {@link javax.jdo.Constants.PROPERTY_NAME
   * PROPERTY_NAME}) and the {@link javax.jdo.Constants.PERSISTENCE_UNIT_NAME
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
   * the property name ({@link javax.jdo.Constants.PROPERTY_NAME PROPERTY_NAME}) and the {@link
   * javax.jdo.Constants.PERSISTENCE_UNIT_NAME PERSISTENCE_UNIT_NAME}) are included in the property
   * map that is used to run the test.
   *
   * @param classpaths class paths to add to the class loader that runs the test that specify where
   *     <tt>jdoconfig.xml</tt> can be found.
   * @param testVariantName the name of the test.
   * @param listenerCount number of listeners utilized in the test.
   * @param vendorSpecificPropertyCount number of vendor properties used in the test.
   * @param checkEqualProperties if true the test's properties are tested.
   * @param excludeName if true the property specified by {@link javax.jdo.Constants.PROPERTY_NAME
   *     PROPERTY_NAME} is not added to the map.
   * @param excludePUName if true the property specified by {@link
   *     javax.jdo.Constants.PROPERTY_PERSISTENCE_UNIT_NAME PROPERTY_PERSISTENCE_UNIT_NAME} is not
   *     added to the map.
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
    String name = testVariantName == null ? null : expected.get(Constants.PROPERTY_NAME);
    Map<Object, Object> actual = JDOHelper.getPropertiesFromJdoconfig(name, loader);

    assertNotNull(actual, "No properties found");
    if (checkEqualProperties) {
      assertEqualProperties(expected, actual);
    }
  }

  @Test
  void testPositive00_PMF0_BasicPMFConfigUsingOnlyStandardAttributesAndListeners()
      throws IOException {
    doPositiveTest(
        new String[] {JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"}, "positive00.pmf0", 2, 0, true);
  }

  @Test
  void
      testPositive00_PMF1_BasicPMFConfigUsingOnlyPropertyElementsWithStandardJavaxDotJDOProperties()
          throws IOException {
    doPositiveTest(
        new String[] {JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"}, "positive00.pmf1", 2, 0, true);
  }

  @Test
  void testPositive00_PMF2_NestedPropertyElementsWithOnlyStandardAttributeNames()
      throws IOException {
    doPositiveTest(
        new String[] {JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"}, "positive00.pmf2", 2, 0, true);
  }

  @Test
  void testPositive00_PMF3_StandardAttributesPlusNonstandardPropertiesInPropertyElements()
      throws IOException {
    doPositiveTest(
        new String[] {JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"}, "positive00.pmf3", 2, 2, true);
  }

  @Test
  void testPositive00_PMF4_StandardAttributesPlusNonstandardAttributes() throws IOException {
    doPositiveTest(
        new String[] {JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"}, "positive00.pmf4", 0, 2, true);
  }

  @Test
  void testPositive01_DuplicatePUsInDifferentConfigFilesButNotRequested() throws IOException {
    String[] classpaths =
        new String[] {
          JDOCONFIG_CLASSPATH_PREFIX + "/Positive01/1a",
          JDOCONFIG_CLASSPATH_PREFIX + "/Positive01/1b"
        };
    URLClassLoader loader = new JDOConfigTestClassLoader(getClass().getClassLoader(), classpaths);
    Map<Object, Object> actual =
        JDOHelper.getPropertiesFromJdoconfig(
            Constants.ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, loader);
    assertNotNull(actual, "Anonymous PMF with no properties returned null");
    assertEquals(0, actual.size(), "Anonymous PMF with no properties had properties");
  }

  @Test
  void testPositive02_GetAnonymousPMFWithNoProperties() throws IOException {

    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Positive02/");

    Map<Object, Object> properties =
        JDOHelper.getPropertiesFromJdoconfig(
            Constants.ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, loader);
    assertNotNull(properties, "Anonymous PMF with no properties returned null");
    assertEquals(0, properties.size(), "Anonymous PMF with no properties had properties");
  }

  @ParameterizedTest
  @CsvSource({
    "/Positive03/,class.positive03.pmf0",
    "/Positive04/,class.positive04.pmf0",
    "/Positive05/,class.positive05.pmf0"
  })
  void testPositive_PMF0_PMFClassNameViaServicesLookup(String path, String expected)
      throws IOException {
    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + path);
    String actual = getPMFClassNameViaServiceLookup(loader);

    assertNotNull(actual, "No PMF name found via services lookup");
    assertEquals(expected, actual);
  }

  @Test
  void testPositive06_PMF0_GetAnonymousPMFProperties() throws IOException {

    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Positive06/");

    Map<String, String> expected = prepareInitialExpectedMap("positive06.pmf0", 2, 0, true, true);
    Map<Object, Object> actual =
        JDOHelper.getPropertiesFromJdoconfig(
            Constants.ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, loader);

    assertNotNull(actual, "No properties found");
    assertEqualProperties(expected, actual);
  }

  @Test
  void testPositive07_PMF0_GetAnonymousPMFPropertiesWithPUName() throws IOException {

    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Positive07/");

    Map<String, String> expected = prepareInitialExpectedMap("positive07.pmf0", 2, 0, true, false);
    Map<Object, Object> actual =
        JDOHelper.getPropertiesFromJdoconfig(
            Constants.ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, loader);

    assertNotNull(actual, "No properties found");
    assertEqualProperties(expected, actual);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "/Negative0/", // EmptyJDOConfigXML
        "/Negative01/", // NoPersistenceUnitsDefined
        "/Negative02/", // DuplicateAnonymousPersistenceUnitsInSameConfig
        "/Negative05/" // DuplicatePropertyInAttributeAndElement
      })
  void testNegative(String path) throws IOException {
    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + path);
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory(loader),
        "JDOHelper failed to throw JDOFatalUserException");
  }

  @Test
  void testNegative03_DuplicateNamedPersistenceUnitsInSameConfig() throws IOException {
    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Negative03/");
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory("name.negative03", loader),
        "JDOHelper failed to throw JDOFatalUserException");
  }

  @Test
  void testNegative04_DuplicatePUNamePropertyInAttributeAndElement() throws IOException {
    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Negative04/");
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory("name.negative04.value0", loader),
        "JDOHelper failed to throw JDOFatalUserException");
  }

  @Test
  void testNegative06_DuplicatePUInDifferentConfigFiles() throws IOException {
    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(),
            JDOCONFIG_CLASSPATH_PREFIX + "/Negative06/6a/",
            JDOCONFIG_CLASSPATH_PREFIX + "/Negative06/6b/");
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory("name.negative06", loader),
        "JDOHelper failed to throw JDOFatalUserException");
  }

  @Test
  void testNegative07_EmptyServicesFile() throws IOException {
    JDOConfigTestClassLoader testLoader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Negative07/");
    String shouldBeNull = getPMFClassNameViaServiceLookup(testLoader);
    assertNull(shouldBeNull);
  }

  @Test
  void testNegative08_NoResourcesFound() {
    String resource = "" + RANDOM.nextLong();
    InputStream in = getClass().getClassLoader().getResourceAsStream(resource);
    assertNull(in);

    // resource pretty much guaranteed not to exist
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory(resource),
        "JDOHelper failed to throw JDOFatalUserException");
  }

  @Test
  void testNegative08_ServicesFileWithOnlyComments() throws IOException {
    JDOConfigTestClassLoader testLoader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(), JDOCONFIG_CLASSPATH_PREFIX + "/Negative08/");
    String shouldBeNull = getPMFClassNameViaServiceLookup(testLoader);
    assertNull(shouldBeNull);
  }

  @Test
  void testNegative09_MultipleInvalidClassesInDifferentServicesFiles() throws IOException {

    // no class name in Negative09/jdoconfig.xml
    // 9a and 9b include services/javax.jdo.PersistenceManagerFactory
    // with bad implementations
    URLClassLoader loader =
        new JDOConfigTestClassLoader(
            getClass().getClassLoader(),
            JDOCONFIG_CLASSPATH_PREFIX + "/Negative09/9a/",
            JDOCONFIG_CLASSPATH_PREFIX + "/Negative09/9b/",
            TEST_CLASSPATH,
            API_CLASSPATH);
    JDOFatalException x =
        assertThrows(
            JDOFatalException.class,
            () -> JDOHelper.getPersistenceManagerFactory("name.negative09", loader),
            "JDOHelper failed to throw JDOFatalUserException");

    Throwable[] nestedExceptions = x.getNestedExceptions();
    if (nestedExceptions.length != 2) {
      appendMessage(
          "JDOHelper.getPersistenceManagerFactory wrong number of "
              + "nested exceptions. Expected 2, got "
              + nestedExceptions.length
              + "\n"
              + x);
    }
    for (Throwable exception : nestedExceptions) {
      if (!(exception instanceof JDOFatalException)) {
        appendMessage(
            "Nested exception " + exception.getClass().getName() + " is not a JDOFatalException.");
      }
    }
    failOnError();
  }
}
