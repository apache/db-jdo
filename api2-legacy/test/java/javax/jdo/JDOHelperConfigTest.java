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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.net.URLClassLoader;

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

    protected static String JDOCONFIG_CLASSPATH_PREFIX = 
        System.getProperty("basedir") + "/test/schema/jdoconfig";

    protected static Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * Tests JDOHelper.getPersistenceUnitProperties using file
     * Positive0-jdoconfig.xml and PU name
     * "persistence-unit-name.positive0.pmf0"
     */
    public void testPositive00_PMF0_GetNamedPMFProperties() throws IOException {

        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive0", loader);

            Map expected = prepareInitialExpectedMap("positive0.pmf0", 2);
            String name = (String) expected.get(PROPERTY_PERSISTENCE_UNIT_NAME);

            Map actual = JDOHelper.getPersistenceUnitProperties(name, loader);

            assertNotNull("No properties found", actual);
            assertEqualProperties(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Tests JDOHelper.getPersistenceUnitProperties using file
     * Positive0-jdoconfig.xml and PU name
     * "persistence-unit-name.positive0.pmf1"
     */
    public void testPositive00_PMF1_GetNamedPMFProperties() throws IOException  {
        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive0", loader);

        Map expected = prepareInitialExpectedMap("positive0.pmf1", 2);
        String name = (String) expected.get(PROPERTY_PERSISTENCE_UNIT_NAME);

        Map actual = JDOHelper.getPersistenceUnitProperties(name, loader);

        assertNotNull("No properties found", actual);
        assertEqualProperties(expected, actual);
    }

    /**
     * Tests JDOHelper.getPersistenceUnitProperties using file
     * Positive0-jdoconfig.xml and PU name
     * "persistence-unit-name.positive0.pmf2"
     */
    public void testPositive00_PMF2_GetNamedPMFProperties() throws IOException  {
        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive0", loader);

        Map expected = prepareInitialExpectedMap("positive0.pmf2", 2);
        String name = (String) expected.get(PROPERTY_PERSISTENCE_UNIT_NAME);

        Map actual = JDOHelper.getPersistenceUnitProperties(name, loader);

        assertNotNull("No properties found", actual);
        assertEqualProperties(expected, actual);
    }

    /**
     * Tests JDOHelper.getPersistenceUnitProperties using file
     * Positive0-jdoconfig.xml and PU name
     * "persistence-unit-name.positive0.pmf3"
     */
    public void testPositive00_PMF3_GetNamedPMFProperties() throws IOException  {
        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive0", loader);

        Map expected = prepareInitialExpectedMap("positive0.pmf3", 2, 2);
        String name = (String) expected.get(PROPERTY_PERSISTENCE_UNIT_NAME);

        Map actual = JDOHelper.getPersistenceUnitProperties(name, loader);

        assertNotNull("No properties found", actual);
        assertEqualProperties(expected, actual);
    }

    /**
     * Tests JDOHelper.getPersistenceUnitProperties using file
     * Positive0-jdoconfig.xml and PU name
     * "persistence-unit-name.positive0.pmf4"
     */
    public void testPositive00_PMF4_GetNamedPMFProperties() throws IOException  {
        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive0", loader);

        Map expected = prepareInitialExpectedMap("positive0.pmf4", 0, 2);
        String name = (String) expected.get(PROPERTY_PERSISTENCE_UNIT_NAME);

        Map actual = JDOHelper.getPersistenceUnitProperties(name, loader);

        assertNotNull("No properties found", actual);
        assertEqualProperties(expected, actual);
    }

    public Map prepareInitialExpectedMap(String testVariant) {
        return prepareInitialExpectedMap(testVariant, 0, 0);
    }
    public Map prepareInitialExpectedMap(String testVariant, int numListeners) {
        return prepareInitialExpectedMap(testVariant, numListeners, 0);
    }
    public Map prepareInitialExpectedMap(
        String testVariant,
        int numListeners,
        int numProperties
    ) {
        Map expected = new HashMap();

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
            PROPERTY_PERSISTENCE_UNIT_NAME,
            PMF_ATTRIBUTE_PERSISTENCE_UNIT_NAME + "." + testVariant);
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
            PROPERTY_SERVER_TIMEZONE_ID,
            PMF_ATTRIBUTE_SERVER_TIME_ZONE_ID + "." + testVariant);

        // listeners
        for (int i = 0; i < numListeners; i++) {
            expected.put(
                PROPERTY_PREFIX_INSTANCE_LIFECYCLE_LISTENER +
                    "listener." + testVariant + ".listener" + i,
                "classes." + testVariant + ".classes" + i
            );
        }

        // properties
        for (int i = 0; i < numProperties; i++) {
            expected.put(
                "property." +  testVariant + ".name" + i,
                "property." +  testVariant + ".value" + i
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

    /**
     * Tests JDOHelper.getPersistenceUnitProperties using file
     * Positive0-jdoconfig.xml and PU name
     * "persistence-unit-name.positive0.pmf0"
     */
    public void testPositive01_DuplicatePUsInDifferentConfigFilesButNotRequested() throws IOException {

        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive1/1a", loader);
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive1/1b", loader);

        Map props = JDOHelper.getPersistenceUnitProperties(null, loader);
        assertNotNull(props);
    }

    /**
     * Tests JDOHelper.getPMFClassNameViaServiceLookup
     */
    public void testPositive03_PMF0_PMFClassNameViaServicesLookup() throws IOException {

        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive3", loader);

        String expected = "class.positive3.pmf0";
        String actual = JDOHelper.getPMFClassNameViaServiceLookup(loader);

        assertNotNull("No PMF name found via services lookup", actual);
        assertEquals(expected, actual);
    }

    /**
     * Tests JDOHelper.getPMFClassNameViaServiceLookup
     */
    public void testPositive04_PMF0_PMFClassNameViaServicesLookup() throws IOException {

        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive4", loader);

        String expected = "class.positive4.pmf0";
        String actual = JDOHelper.getPMFClassNameViaServiceLookup(loader);

        assertNotNull("No PMF name found via services lookup", actual);
        assertEquals(expected, actual);
    }

    /**
     * Tests JDOHelper.getPMFClassNameViaServiceLookup
     */
    public void testPositive05_PMF0_PMFClassNameViaServicesLookup() throws IOException {

        URLClassLoader loader = new JDOConfigTestClassLoader(
                JDOCONFIG_CLASSPATH_PREFIX,
                getClass().getClassLoader());
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive5", loader);

        String expected = "class.positive5.pmf0";
        String actual = JDOHelper.getPMFClassNameViaServiceLookup(loader);

        assertNotNull("No PMF name found via services lookup", actual);
        assertEquals(expected, actual);
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

    public void testNegative00_EmptyJDOConfigXML() throws IOException  {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative0", loader);

            JDOHelper.getPersistenceManagerFactory(loader);
            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }
    
    public void testNegative01_NoPersistenceUnitsDefined() throws IOException  {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative1", loader);

            JDOHelper.getPersistenceManagerFactory(loader);
            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }

    public void testNegative02_DuplicateAnonymousPersistenceUnitsInSameConfig()
        throws IOException
    {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative2", loader);

            JDOHelper.getPersistenceManagerFactory(loader);
            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }

    public void testNegative03_DuplicateNamedPersistenceUnitsInSameConfig()
        throws IOException
    {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative3", loader);

            JDOHelper.getPersistenceManagerFactory(
                "persistence-unit-name.negative3",
                loader);

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }

    public void testNegative04_DuplicatePUNamePropertyInAttributeAndElement()
        throws IOException
    {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative4", loader);

            JDOHelper.getPersistenceManagerFactory(
                "persistence-unit-name.negative4.value0",
                loader);

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }

    public void testNegative05_DuplicatePropertyInAttributeAndElement()
        throws IOException
    {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative5", loader);

            JDOHelper.getPersistenceManagerFactory(loader);

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }

    public void testNegative06_DuplicatePUInDifferentConfigFiles()
        throws IOException
    {
        try {
            URLClassLoader loader = new JDOConfigTestClassLoader(
                    JDOCONFIG_CLASSPATH_PREFIX,
                    getClass().getClassLoader());
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative6/6a", loader);
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative6/6b", loader);

            JDOHelper.getPersistenceManagerFactory(
                    "persistence-unit-name.negative6",
                    loader);

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }

    public void testNegative07_EmptyServicesFile()
        throws IOException
    {
        JDOConfigTestClassLoader testLoader = new JDOConfigTestClassLoader(new String[] {JDOCONFIG_CLASSPATH_PREFIX}, getClass().getClassLoader());
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative7", testLoader);
        String shouldBeNull = JDOHelper.getPMFClassNameViaServiceLookup(testLoader);
        assertNull(shouldBeNull);
    }

    public void testNegative08_ServicesFileWithOnlyComments()
        throws IOException
    {
        JDOConfigTestClassLoader testLoader = new JDOConfigTestClassLoader(new String[] {JDOCONFIG_CLASSPATH_PREFIX}, getClass().getClassLoader());
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative8", testLoader);
        String shouldBeNull = JDOHelper.getPMFClassNameViaServiceLookup(testLoader);
        assertNull(shouldBeNull);
    }
}
