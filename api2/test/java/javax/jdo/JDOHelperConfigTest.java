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

import junit.framework.TestSuite;

import javax.jdo.util.AbstractTest;
import javax.jdo.util.BatchTestRunner;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Tests class javax.jdo.JDOHelper for META-INF/jdoconfig.xml compliance.
 */
public class JDOHelperConfigTest extends AbstractTest implements Constants {

    public static void main(String args[]) {
        BatchTestRunner.run(JDOHelperConfigTest.class);
    }

    public static TestSuite suite() {
        return new TestSuite(JDOHelperConfigTest.class);
    }

    protected static String JDOCONFIG_CLASSPATH_PREFIX
            = initJDOConfigClasspathPrefix();

    protected static String initJDOConfigClasspathPrefix() {
        return initBasedir() + "test/schema/jdoconfig";
    }

    protected static String TEST_CLASSPATH =
            initTestClasspath();

    protected static String initTestClasspath() {
        return initBasedir() + "target/test-classes/";
    }

    protected static String API_CLASSPATH =
            initAPIClasspath();

    protected static String initAPIClasspath() {
        return initBasedir() + "target/classes/";
    }

    protected static String initBasedir() {
        String basedir = System.getProperty("basedir");
        if (basedir != null) {
            if (!basedir.endsWith("/")) {
                basedir += "/";
            }
        } else {
            basedir = "";
        }
        return basedir;
    }

    protected static Random RANDOM = new Random(System.currentTimeMillis());

    protected Map prepareInitialExpectedMap(
            String testVariant,
            int listenerCount,
            int vendorSpecificPropertyCount,
            boolean excludeName,
            boolean excludePUName
    ) {
        Map expected = new HashMap();

        if (!excludeName) {
            expected.put(
                    PROPERTY_NAME,
                    PMF_ATTRIBUTE_NAME + "." + testVariant);
        }
        if (!excludePUName) {
            expected.put(
                    PROPERTY_PERSISTENCE_UNIT_NAME,
                    PMF_ATTRIBUTE_PERSISTENCE_UNIT_NAME + "." + testVariant);
        }

        expected.put(PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS,
                PMF_ATTRIBUTE_CLASS + "." + testVariant);
        expected.put(
                PROPERTY_CONNECTION_DRIVER_NAME,
                PMF_ATTRIBUTE_CONNECTION_DRIVER_NAME + "." + testVariant);
        expected.put(
                PROPERTY_CONNECTION_FACTORY_NAME,
                PMF_ATTRIBUTE_CONNECTION_FACTORY_NAME + "." + testVariant);
        expected.put(
                PROPERTY_CONNECTION_FACTORY2_NAME,
                PMF_ATTRIBUTE_CONNECTION_FACTORY2_NAME + "." + testVariant);
        expected.put(
                PROPERTY_CONNECTION_PASSWORD,
                PMF_ATTRIBUTE_CONNECTION_PASSWORD + "." + testVariant);
        expected.put(
                PROPERTY_CONNECTION_URL,
                PMF_ATTRIBUTE_CONNECTION_URL + "." + testVariant);
        expected.put(
                PROPERTY_CONNECTION_USER_NAME,
                PMF_ATTRIBUTE_CONNECTION_USER_NAME + "." + testVariant);
        expected.put(
                PROPERTY_IGNORE_CACHE,
                PMF_ATTRIBUTE_IGNORE_CACHE + "." + testVariant);
        expected.put(
                PROPERTY_MAPPING,
                PMF_ATTRIBUTE_MAPPING + "." + testVariant);
        expected.put(
                PROPERTY_MULTITHREADED,
                PMF_ATTRIBUTE_MULTITHREADED + "." + testVariant);
        expected.put(
                PROPERTY_NONTRANSACTIONAL_READ,
                PMF_ATTRIBUTE_NONTRANSACTIONAL_READ + "." + testVariant);
        expected.put(
                PROPERTY_NONTRANSACTIONAL_WRITE,
                PMF_ATTRIBUTE_NONTRANSACTIONAL_WRITE + "." + testVariant);
        expected.put(
                PROPERTY_OPTIMISTIC,
                PMF_ATTRIBUTE_OPTIMISTIC + "." + testVariant);
        expected.put(
                PROPERTY_RESTORE_VALUES,
                PMF_ATTRIBUTE_RESTORE_VALUES + "." + testVariant);
        expected.put(
                PROPERTY_RETAIN_VALUES,
                PMF_ATTRIBUTE_RETAIN_VALUES + "." + testVariant);
        expected.put(
                PROPERTY_DETACH_ALL_ON_COMMIT,
                PMF_ATTRIBUTE_DETACH_ALL_ON_COMMIT + "." + testVariant);
        expected.put(
                PROPERTY_SERVER_TIME_ZONE_ID,
                PMF_ATTRIBUTE_SERVER_TIME_ZONE_ID + "." + testVariant);

        // listeners
        for (int i = 0; i < listenerCount; i++) {
            expected.put(
                    PROPERTY_PREFIX_INSTANCE_LIFECYCLE_LISTENER +
                            "listener." + testVariant + ".listener" + i,
                    "classes." + testVariant + ".classes" + i
            );
        }

        // vendor-specific properties
        for (int i = 0; i < vendorSpecificPropertyCount; i++) {
            expected.put(
                    "property." + testVariant + ".name" + i,
                    "property." + testVariant + ".value" + i
            );
        }

        return expected;
    }

    static void assertEqualProperties(Map expected, Map actual) {
        Iterator i = expected.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            String key = (String) entry.getKey();
            String expectedValue = (String) entry.getValue();
            String actualValue = (String) actual.get(key);

            assertEquals(
                    "Actual property at key [" + key + "] with value [" +
                            actualValue + "] not equal to expected value [" +
                            expectedValue + "]",
                    expectedValue,
                    actualValue);
        }
    }

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

    protected void doPositiveTest(
            String[] classpaths,
            String testVariantName,
            int listenerCount,
            int vendorSpecificPropertyCount,
            boolean checkEqualProperties,
            boolean excludeName,
            boolean excludePUName)
            throws IOException {

        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());

        for (int i = 0; i < classpaths.length; i++) {
            ClasspathHelper.addFile(classpaths[i], loader);
        }

        Map expected = prepareInitialExpectedMap(
                testVariantName,
                listenerCount,
                vendorSpecificPropertyCount,
                excludeName,
                excludePUName);
        
        String name = testVariantName == null
                ? null
                : (String) expected.get(PROPERTY_NAME);

        Map actual = JDOHelper.getPropertiesFromJdoconfig(name, loader);

        assertNotNull("No properties found", actual);
        if (checkEqualProperties) {
            assertEqualProperties(expected, actual);
        }
    }

    public void testPositive00_PMF0_BasicPMFConfigUsingOnlyStandardAttributesAndListeners()
            throws IOException {
        doPositiveTest(
                new String[]{JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"},
                "positive00.pmf0",
                2,
                0,
                true);
    }

    public void testPositive00_PMF1_BasicPMFConfigUsingOnlyPropertyElementsWithStandardJavaxDotJDOProperties()
            throws IOException {
        doPositiveTest(
                new String[]{JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"},
                "positive00.pmf1",
                2,
                0,
                true);
    }

    public void testPositive00_PMF2_NestedPropertyElementsWithOnlyStandardAttributeNames()
            throws IOException {
        doPositiveTest(
                new String[]{JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"},
                "positive00.pmf2",
                2,
                0,
                true);
    }

    public void testPositive00_PMF3_StandardAttributesPlusNonstandardPropertiesInPropertyElements()
            throws IOException {
        doPositiveTest(
                new String[]{JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"},
                "positive00.pmf3",
                2,
                2,
                true);
    }

    public void testPositive00_PMF4_StandardAttributesPlusNonstandardAttributes()
            throws IOException {
        doPositiveTest(
                new String[]{JDOCONFIG_CLASSPATH_PREFIX + "/Positive00"},
                "positive00.pmf4",
                0,
                2,
                true);
    }

    public void testPositive01_DuplicatePUsInDifferentConfigFilesButNotRequested()
            throws IOException {

        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());

        String[] classpaths = new String[]{
            JDOCONFIG_CLASSPATH_PREFIX + "/Positive01/1a",
            JDOCONFIG_CLASSPATH_PREFIX + "/Positive01/1b"
        };
        for (int i = 0; i < classpaths.length; i++) {
            ClasspathHelper.addFile(classpaths[i], loader);
        }

        Map actual = JDOHelper.getPropertiesFromJdoconfig(
                ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, loader);
    }

    public void testPositive02_GetAnonymousPMFWithNoProperties()
            throws IOException {
        
        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());

            ClasspathHelper.addFile(
                    JDOCONFIG_CLASSPATH_PREFIX + "/Positive02", loader);

        Map properties = JDOHelper.getPropertiesFromJdoconfig(
                ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, loader);
        assertNotNull(
                "Anonymous PMF with no properties returned null", properties);
        assertTrue(
                "Anonymous PMF with no properties had properties",
                properties.size() == 0);
    }

    public void testPositive03_PMF0_PMFClassNameViaServicesLookup()
            throws IOException {

        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive03", loader);

        String expected = "class.positive03.pmf0";
        String actual = getPMFClassNameViaServiceLookup(loader);

        assertNotNull("No PMF name found via services lookup", actual);
        assertEquals(expected, actual);
    }

    public void testPositive04_PMF0_PMFClassNameViaServicesLookup()
            throws IOException {

        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive04", loader);

        String expected = "class.positive04.pmf0";
        String actual = getPMFClassNameViaServiceLookup(loader);

        assertNotNull("No PMF name found via services lookup", actual);
        assertEquals(expected, actual);
    }

    public void testPositive05_PMF0_PMFClassNameViaServicesLookup()
            throws IOException {

        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());
        ClasspathHelper.addFile(
                JDOCONFIG_CLASSPATH_PREFIX + "/Positive05", loader);

        String expected = "class.positive05.pmf0";
        String actual = getPMFClassNameViaServiceLookup(loader);

        assertNotNull("No PMF name found via services lookup", actual);
        assertEquals(expected, actual);
    }

    public void testPositive06_PMF0_GetAnonymousPMFProperties()
            throws IOException {

        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());

        ClasspathHelper.addFile(
                JDOCONFIG_CLASSPATH_PREFIX + "/Positive06", loader);

        Map expected = prepareInitialExpectedMap(
                "positive06.pmf0", 2, 0, true, true);

        Map actual = JDOHelper.getPropertiesFromJdoconfig(
                ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, loader);

        assertNotNull("No properties found", actual);
        assertEqualProperties(expected, actual);
    }

    public void testPositive07_PMF0_GetAnonymousPMFPropertiesWithPUName()
            throws IOException {

        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());

        ClasspathHelper.addFile(
                JDOCONFIG_CLASSPATH_PREFIX + "/Positive07", loader);

        Map expected = prepareInitialExpectedMap(
                "positive07.pmf0", 2, 0, true, false);

        Map actual = JDOHelper.getPropertiesFromJdoconfig(
                ANONYMOUS_PERSISTENCE_MANAGER_FACTORY_NAME, loader);

        assertNotNull("No properties found", actual);
        assertEqualProperties(expected, actual);
    }

    public void testNegative00_EmptyJDOConfigXML() throws IOException {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(
                    JDOCONFIG_CLASSPATH_PREFIX + "/Negative0", loader);

            JDOHelper.getPersistenceManagerFactory(loader);
            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // sunny day
        }
    }

    public void testNegative01_NoPersistenceUnitsDefined() throws IOException {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(
                    JDOCONFIG_CLASSPATH_PREFIX + "/Negative01", loader);

            JDOHelper.getPersistenceManagerFactory(loader);
            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // joy, sweet joy
        }
    }

    public void testNegative02_DuplicateAnonymousPersistenceUnitsInSameConfig()
            throws IOException {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(
                    JDOCONFIG_CLASSPATH_PREFIX + "/Negative02", loader);

            JDOHelper.getPersistenceManagerFactory(loader);
            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // the cockles of my heart warmeth
        }
    }

    public void testNegative03_DuplicateNamedPersistenceUnitsInSameConfig()
            throws IOException {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(
                    JDOCONFIG_CLASSPATH_PREFIX + "/Negative03", loader);

            JDOHelper.getPersistenceManagerFactory(
                    "name.negative03",
                    loader);

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // warm fuzzies
        }
    }

    public void testNegative04_DuplicatePUNamePropertyInAttributeAndElement()
            throws IOException {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(
                    JDOCONFIG_CLASSPATH_PREFIX + "/Negative04", loader);

            JDOHelper.getPersistenceManagerFactory(
                    "name.negative04.value0",
                    loader);

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // no cold pricklies
        }
    }

    public void testNegative05_DuplicatePropertyInAttributeAndElement()
            throws IOException {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(
                    JDOCONFIG_CLASSPATH_PREFIX + "/Negative05", loader);

            JDOHelper.getPersistenceManagerFactory(loader);

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // party!
        }
    }

    public void testNegative06_DuplicatePUInDifferentConfigFiles()
            throws IOException {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(
                    JDOCONFIG_CLASSPATH_PREFIX + "/Negative06/6a", loader);
            ClasspathHelper.addFile(
                    JDOCONFIG_CLASSPATH_PREFIX + "/Negative06/6b", loader);

            JDOHelper.getPersistenceManagerFactory(
                    "name.negative06",
                    loader);

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // clear blue sky
        }
    }

    
    public void testNegative07_EmptyServicesFile()
            throws IOException {
        JDOConfigTestClassLoader testLoader = new JDOConfigTestClassLoader(
                new String[]{JDOCONFIG_CLASSPATH_PREFIX},
                getClass().getClassLoader());
        ClasspathHelper.addFile(
                JDOCONFIG_CLASSPATH_PREFIX + "/Negative07", testLoader);
        String shouldBeNull =
               getPMFClassNameViaServiceLookup(testLoader);
        assertNull(shouldBeNull);
    }

    public void testNegative08_NoResourcesFound() {
        String resource = "" + RANDOM.nextLong();

        InputStream in =
                getClass().getClassLoader().getResourceAsStream(resource);
        assertNull(in);

        // resource pretty much guaranteed not to exist
        try {
            JDOHelper.getPersistenceManagerFactory(resource);
            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }

    public void testNegative08_ServicesFileWithOnlyComments()
            throws IOException {
        JDOConfigTestClassLoader testLoader = new JDOConfigTestClassLoader(
                new String[]{JDOCONFIG_CLASSPATH_PREFIX},
                getClass().getClassLoader());
        ClasspathHelper.addFile(
                JDOCONFIG_CLASSPATH_PREFIX + "/Negative08", testLoader);
        String shouldBeNull =
                getPMFClassNameViaServiceLookup(testLoader);
        assertNull(shouldBeNull);
    }
    
    public void testNegative09_MultipleInvalidClassesInDifferentServicesFiles()
            throws IOException {

        // no class name in Negative09/jdoconfig.xml
        // 9a and 9b include services/javax.jdo.PersistenceManagerFactory
        // with bad implementations
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(
                    JDOCONFIG_CLASSPATH_PREFIX + "/Negative09/9a", loader);
            ClasspathHelper.addFile(
                    JDOCONFIG_CLASSPATH_PREFIX + "/Negative09/9b", loader);
            ClasspathHelper.addFile(
                    TEST_CLASSPATH, loader);
            ClasspathHelper.addFile(
                    API_CLASSPATH, loader);

            JDOHelper.getPersistenceManagerFactory("name.negative09", loader);

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalException x) {

            Throwable[] nestedExceptions = x.getNestedExceptions();
            if (nestedExceptions.length != 2) {
                appendMessage(
                "JDOHelper.getPersistenceManagerFactory wrong number of " +
                "nested exceptions. Expected 2, got " + nestedExceptions.length +
                        "\n" + x);
            }
            for (int i = 0; i < nestedExceptions.length; ++i) {
                Throwable exception = nestedExceptions[i];
                if (!(exception instanceof JDOFatalException)) {
                    appendMessage("Nested exception " + 
                            exception.getClass().getName() + 
                            " is not a JDOFatalException.");
                }
            }
        }
        failOnError();
    }

    private String getPMFClassNameViaServiceLookup(ClassLoader loader) {
        try {
            Enumeration urls = JDOHelper.getResources(loader, 
                SERVICE_LOOKUP_PMF_RESOURCE_NAME);
            while (urls.hasMoreElements()) {
                // return the first one found
                return JDOHelper.getClassNameFromURL((URL)urls.nextElement());
            }
        } catch (Exception ex) {
            // ignore exceptions from i/o errors
        }
        return null;            
    }
}
