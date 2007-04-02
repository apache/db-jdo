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
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Random;
import java.io.InputStream;
import java.io.IOException;

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
        = "test/schema/jdoconfig.xml";

    protected static Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * Tests JDOHelper.getPersistenceUnitProperties using file
     * Positive0-jdoconfig.xml and PU name
     * "persistence-unit-name.positive0.pmf0"
     */
    public void testGetNamedPMFProperties_positive0_pmf0() throws IOException {

        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive0");

        ClassLoader loader = getClass().getClassLoader();

        Map expected = prepareInitialExpectedMap("positive0.pmf0", 2);
        String name = (String) expected.get(PROPERTY_PERSISTENCE_UNIT_NAME);

        Map actual = JDOHelper.getPersistenceUnitProperties(name, loader);

        assertNotNull("No properties found", actual);
        assertEqualProperties(expected, actual);
    }

    /**
     * Tests JDOHelper.getPersistenceUnitProperties using file
     * Positive0-jdoconfig.xml and PU name
     * "persistence-unit-name.positive0.pmf1"
     */
    public void testGetNamedPMFProperties_positive0_pmf1() throws IOException  {
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive0");

        ClassLoader loader = getClass().getClassLoader();

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
    public void testGetNamedPMFProperties_positive0_pmf2() throws IOException  {
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive0");

        ClassLoader loader = getClass().getClassLoader();

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
    public void testGetNamedPMFProperties_positive0_pmf3() throws IOException  {
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive0");

        ClassLoader loader = getClass().getClassLoader();

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
    public void testGetNamedPMFProperties_positive0_pmf4() throws IOException  {
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive0");

        ClassLoader loader = getClass().getClassLoader();

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
            PMF_ATTRIBUTE_ServerTimeZoneID + "." + testVariant);

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
    public void testDuplicatePUsInDifferentConfigFilesButNotRequested_positive1() throws IOException {

        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive1/1a");
        ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Positive1/1b");

        ClassLoader loader = getClass().getClassLoader();

        Map props = JDOHelper.getPersistenceUnitProperties(null);
        assertNotNull(props);
    }

    public void testNegative_NoResourcesFound() {
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

    public void testNegative0_EmptyJDOConfigXML() throws IOException  {
        try {
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative0");

            JDOHelper.getPersistenceManagerFactory();
            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }
    
    public void testNegative1_NoPersistenceUnitsDefined() throws IOException  {
        try {
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative1");

            JDOHelper.getPersistenceManagerFactory();
            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }

    public void testNegative2_DuplicateAnonymousPersistenceUnitsInSameConfig()
        throws IOException
    {
        try {
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative2");

            JDOHelper.getPersistenceManagerFactory();
            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }

    public void testNegative3_DuplicateNamedPersistenceUnitsInSameConfig()
        throws IOException
    {
        try {
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative3");

            JDOHelper.getPersistenceManagerFactory(
                "persistence-unit-name.negative3");

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }

    public void testNegative4_DuplicatePUNamePropertyInAttributeAndElement()
        throws IOException
    {
        try {
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative4");

            JDOHelper.getPersistenceManagerFactory(
                "persistence-unit-name.negative4.value0");

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }

    public void testNegative5_DuplicatePropertyInAttributeAndElement()
        throws IOException
    {
        try {
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative5");

            JDOHelper.getPersistenceManagerFactory();

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }

    public void testNegative6_DuplicatePUInDifferentConfigFiles()
        throws IOException
    {
        try {
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative6/6a");
            ClasspathHelper.addFile(JDOCONFIG_CLASSPATH_PREFIX + "/Negative6/6b");

            JDOHelper.getPersistenceManagerFactory("persistence-unit-name.negative6");

            fail("JDOHelper failed to throw JDOFatalUserException");
        }
        catch (JDOFatalUserException x) {
            // happy path
        }
    }
}

