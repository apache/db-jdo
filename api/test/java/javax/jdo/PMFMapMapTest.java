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
import java.net.MalformedURLException;

import javax.jdo.util.AbstractTest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import junit.framework.TestSuite;

import javax.jdo.util.BatchTestRunner;

/**
 * 
 * Tests class javax.jdo.JDOHelper for calls to the impl's static method
 *  getPersistenceManagerFactory(Map overrides, Map props).
 * 
 */
public class PMFMapMapTest extends AbstractTest implements Constants {

    static String expectedDriverName = "Jane Doe";
    static String expectedDriverName4NamedPMF = "Larry";
    static String expectedDriverNameWithOverrides = "Gerard Manley Hopkins";
    static String PMFName = "BookSearch";
    static String resourceDir = "Pmfmapmap01";
    static String propsDir = "Pmfmapmap02";
    static String pmfServiceClass = "javax.jdo.PMFService";
    static String propertiesFile = "propsfile.props";
    PersistenceManagerFactory pmf;
    Properties props;
    Properties overrides;
    URLClassLoader resourceClassLoader;
    ClassLoader saveContextClassLoader;

    public static void main(String args[]) {
        BatchTestRunner.run(PMFMapMapTest.class);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    public static TestSuite suite() {
        return new TestSuite(PMFMapMapTest.class);
    }

    public void setup() {
    }

    public void teardown() {
    }

    void setupResourceClassLoader(String dir) {
        try {
            URL url = new URL(
                    "file://" + JDOCONFIG_CLASSPATH_PREFIX + "/" + dir + "/");
            resourceClassLoader = new URLClassLoader(new URL[]{url},
                    getClass().getClassLoader());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * A class path prefix used in the various tests where the class path
     * needs to be set.
     */
    protected static String JDOCONFIG_CLASSPATH_PREFIX = initJDOConfigClasspathPrefix();

    /**
     * Returns the JDO configuration class path prefix's default value, which is
     * the project base directory suffixed by the path to the configuration
     * directory (<tt>test/schema/jdoconfig</tt>).
     *
     * @return the default class path prefix used by this test suite.
     *
     */
    protected static String initJDOConfigClasspathPrefix() {
        return initBasedir() + "test/schema/jdoconfig";
    }

    /**
     * Returns the base directory for this project.  This base directory
     * is used to build up the other class paths defined in this test suite.
     * The value returned is the value returned by
     * <code>System.getProperty("basedir")</code>.
     * A trailing slash is appended to the path if it doesn't exist.
     *
     * @return the default base directory of the project.
     */
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

    /* static PersistenceManagerFactory getPersistenceManagerFactory()
    Get the anonymous PersistenceManagerFactory configured via the
    standard configuration file resource "META-INF/jdoconfig.xml",
    using the current thread's context class loader to locate the
    configuration file resource(s).
     */
    public void testJDOConfigXML() {

        setupResourceClassLoader(resourceDir);
        Thread.currentThread().setContextClassLoader(resourceClassLoader);

        try {
            pmf = JDOHelper.getPersistenceManagerFactory();
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverName.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverName + "\"");
        }
    }

    /*
    static PersistenceManagerFactory getPersistenceManagerFactory
    (java.lang.ClassLoader pmfClassLoader)
    Get the anonymous PersistenceManagerFactory configured via the
    standard configuration file resource "META-INF/jdoconfig.xml",
    using the given class loader.
     */
    public void testJDOConfigXMLWithLoader() throws IOException {

        setupResourceClassLoader(resourceDir);

        try {
            pmf = JDOHelper.getPersistenceManagerFactory(resourceClassLoader);
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverName.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverName + "\"");
        }
    }

    /*
    static PersistenceManagerFactory getPersistenceManagerFactory(
    java.io.File propsFile)
    Returns a PersistenceManagerFactory configured based on the properties
    stored in the file at propsFile.
     */
    public void testPropsFile() {

        setupResourceClassLoader(propsDir);
        Thread.currentThread().setContextClassLoader(resourceClassLoader);

        try {
            pmf = JDOHelper.getPersistenceManagerFactory(propertiesFile);
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverName.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverName + "\"");
        }
    }


    /*
    static PersistenceManagerFactory getPersistenceManagerFactory(
    java.io.File propsFile, java.lang.ClassLoader loader)
    Returns a PersistenceManagerFactory configured based on the properties
    stored in the file at propsFile.
     */
    public void testPropsFileAndLoader() {

        setupResourceClassLoader(propsDir);

        try {
            pmf = JDOHelper.getPersistenceManagerFactory(propertiesFile,
                    resourceClassLoader);
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverName.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverName + "\"");
        }
    }

    /*
    static PersistenceManagerFactory getPersistenceManagerFactory(
    java.io.InputStream stream)
    Returns a PersistenceManagerFactory configured based on the Properties
    stored in the input stream at stream.
     */
    public void testInputStream() {
        props = new Properties();
        props.setProperty(PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS,
                pmfServiceClass);
        props.setProperty(PROPERTY_CONNECTION_DRIVER_NAME,
                expectedDriverName);

        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        try {
            props.store(outstream, "");
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
        InputStream byteArrayInputStream =
                new ByteArrayInputStream(outstream.toByteArray());

        setupResourceClassLoader(resourceDir);
        Thread.currentThread().setContextClassLoader(resourceClassLoader);

        try {
            pmf = JDOHelper.getPersistenceManagerFactory(byteArrayInputStream);
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverName.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverName + "\"");
        }
    }

    /*
    static PersistenceManagerFactory getPersistenceManagerFactory(
    java.io.InputStream stream, java.lang.ClassLoader loader)
    Returns a PersistenceManagerFactory configured based on the Properties
    stored in the input stream at stream.
     */
    public void testInputStreamWithLoader() {
        props = new Properties();
        props.setProperty(PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS,
                pmfServiceClass);
        props.setProperty(PROPERTY_CONNECTION_DRIVER_NAME,
                expectedDriverName);

        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        try {
            props.store(outstream, "");
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
        InputStream byteArrayInputStream =
                new ByteArrayInputStream(outstream.toByteArray());

        setupResourceClassLoader(resourceDir);

        try {
            pmf = JDOHelper.getPersistenceManagerFactory(byteArrayInputStream,
                    resourceClassLoader);
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverName.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverName + "\"");
        }
    }

    /*
    static PersistenceManagerFactory getPersistenceManagerFactory(
    java.util.Map<?,?> props)
    Get a PersistenceManagerFactory based on a Properties instance,
    using the current thread's context class loader to locate
    the PersistenceManagerFactory class.
     */
    public void testProperties() {
        props = new Properties();
        props.setProperty(PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS,
                pmfServiceClass);
        props.setProperty(PROPERTY_CONNECTION_DRIVER_NAME,
                expectedDriverName);

        setupResourceClassLoader(resourceDir);
        Thread.currentThread().setContextClassLoader(resourceClassLoader);
        try {
            pmf = JDOHelper.getPersistenceManagerFactory(props);
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverName.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverName + "\"");
        }
    }

    /*
    static PersistenceManagerFactory getPersistenceManagerFactory(
    java.util.Map<?,?> props, java.lang.ClassLoader pmfClassLoader)
    Get a PersistenceManagerFactory based on a Map and a class loader.
     */
    public void testPropertiesAndLoader() {
        props = new Properties();
        props.setProperty(PROPERTY_PERSISTENCE_MANAGER_FACTORY_CLASS,
                pmfServiceClass);
        props.setProperty(PROPERTY_CONNECTION_DRIVER_NAME,
                expectedDriverName);

        setupResourceClassLoader(resourceDir);
        try {
            pmf = JDOHelper.getPersistenceManagerFactory(props,
                    resourceClassLoader);
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverName.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverName + "\"");
        }
    }

    /*
    static PersistenceManagerFactory getPersistenceManagerFactory
    (java.util.Map<?,?> overrides, java.lang.String name)
    Returns a named PersistenceManagerFactory or persistence unit.
     */
    public void testNamedPMFWithOverrides() {
        overrides = new Properties();
        overrides.setProperty(PROPERTY_CONNECTION_DRIVER_NAME,
                expectedDriverNameWithOverrides);

        setupResourceClassLoader(resourceDir);
        Thread.currentThread().setContextClassLoader(resourceClassLoader);

        try {
            pmf = JDOHelper.getPersistenceManagerFactory(overrides, PMFName);
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverNameWithOverrides.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverNameWithOverrides + "\"");
        }
    }

    /*
    static PersistenceManagerFactory getPersistenceManagerFactory(
    java.util.Map<?,?> overrides, java.lang.String name,
    java.lang.ClassLoader resourceLoader)
    Returns a named PersistenceManagerFactory or persistence unit.
     */
    public void testNamedPMFWithOverridesAndLoader() {
        overrides = new Properties();
        overrides.setProperty(PROPERTY_CONNECTION_DRIVER_NAME,
                expectedDriverNameWithOverrides);

        setupResourceClassLoader(resourceDir);

        try {
            pmf = JDOHelper.getPersistenceManagerFactory(overrides, PMFName,
                    resourceClassLoader);
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass." + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverNameWithOverrides.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverNameWithOverrides + "\"");
        }
    }

    /*
    static PersistenceManagerFactory getPersistenceManagerFactory(
    java.util.Map<?,?> overrides, java.lang.String name,
    java.lang.ClassLoader resourceLoader, java.lang.ClassLoader pmfLoader)
    Returns a PersistenceManagerFactory configured based on the properties
    stored in the resource at name, or, if not found,
    returns a PersistenceManagerFactory with the given name or,
    if not found, returns a javax.persistence.EntityManagerFactory
    cast to a PersistenceManagerFactory.
     */
    public void testNamedPMFWithOverridesAndTwoLoaders() {
        overrides = new Properties();
        overrides.setProperty(PROPERTY_CONNECTION_DRIVER_NAME,
                expectedDriverNameWithOverrides);

        setupResourceClassLoader(resourceDir);

        try {
            pmf = JDOHelper.getPersistenceManagerFactory(overrides, PMFName,
                    resourceClassLoader,
                    Thread.currentThread().getContextClassLoader());
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass. " + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverNameWithOverrides.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverNameWithOverrides + "\"");
        }
    }

    /*
    static PersistenceManagerFactory getPersistenceManagerFactory(
    java.lang.String name)
    Returns a named PersistenceManagerFactory or persistence unit.
     */
    public void testNamedPMF() {

        setupResourceClassLoader(resourceDir);
        Thread.currentThread().setContextClassLoader(resourceClassLoader);

        try {
            pmf = JDOHelper.getPersistenceManagerFactory(PMFName);
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass. " + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverName4NamedPMF.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverName4NamedPMF + "\"");
        }
    }

    /*
    static PersistenceManagerFactory getPersistenceManagerFactory
    (java.lang.String name, java.lang.ClassLoader loader)
    Returns a named PersistenceManagerFactory or persistence unit.
     */
    public void testNamedPMFWithLoader() {

        setupResourceClassLoader(resourceDir);

        try {
            pmf = JDOHelper.getPersistenceManagerFactory(PMFName,
                    resourceClassLoader);
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass. " + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverName4NamedPMF.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverName4NamedPMF + "\"");
        }
    }

    /*
    static PersistenceManagerFactory getPersistenceManagerFactory(
    java.lang.String name, java.lang.ClassLoader resourceLoader,
    java.lang.ClassLoader pmfLoader)
    Returns a named PersistenceManagerFactory or persistence unit.
     */
    public void testNamedPMFWithTwoLoaders() {

        setupResourceClassLoader(resourceDir);

        try {
            pmf = JDOHelper.getPersistenceManagerFactory(PMFName,
                    resourceClassLoader,
                    Thread.currentThread().getContextClassLoader());
        } catch (JDOFatalUserException ex) {
            fail("Failed to find PersistenceManagerFactoryClass. " + ex.getMessage());
        }

        String driverName = pmf.getConnectionDriverName();
        if (!expectedDriverName4NamedPMF.equals(driverName)) {
            fail("Bad ConnectionDriverName(): " + driverName + ".  Expected: \""
                    + expectedDriverName4NamedPMF + "\"");
        }
    }
}
