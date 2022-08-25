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

import java.io.File;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jdo.pc.PCPoint;
import javax.jdo.util.AbstractTest;
import javax.jdo.util.BatchTestRunner;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Tests class javax.jdo.JDOHelper.
 * <p>
 * TBD: implementation of testMakeDirty, 
 * TBD: testing interrogative methods for persistent instances
 * TBD: getPMF for valid PMF class
 */
public class JDOHelperTest extends AbstractTest {
    
    /** */
    public static void main(String args[]) {
        BatchTestRunner.run(JDOHelperTest.class);
    }

    /** The purpose of this test is simply to call some of the
     * methods on a constructed instance of JDOHelper and verify that
     * they do not throw exceptions. It is not a functional test.
     * @since 2.1
     */
    public void testConstructor() {
        assertNull("getObjectId(null) returned non-null", 
                JDOHelper.getObjectId(null));
        assertNull("getPersistenceManager(null) returned non-null", 
                JDOHelper.getPersistenceManager(null));
        assertNull("getTransactionalObjectId(null) returned non-null", 
                JDOHelper.getTransactionalObjectId(null));
        assertNull("getVersion(null) returned non-null", 
                JDOHelper.getVersion(null));
        assertFalse("isDeleted(null) returned non-null", 
                JDOHelper.isDeleted(null));
        assertFalse("isDetached(null) returned non-null", 
                JDOHelper.isDetached(null));
        assertFalse("isDirty(null) returned non-null", 
                JDOHelper.isDirty(null));
        assertFalse("isNew(null) returned non-null", 
                JDOHelper.isNew(null));
        assertFalse("isPersistent(null) returned non-null", 
                JDOHelper.isPersistent(null));
        assertFalse("isTransactional(null) returned non-null", 
                JDOHelper.isTransactional(null));
    }

    /** The purpose of this test is simply to call some of the
     * methods on the static instance of JDOHelper and verify that
     * they do not throw exceptions. It is not a functional test.
     * @since 2.1
     */
    public void testGetInstance() {
        assertNull("getObjectId(null) returned non-null", 
                JDOHelper.getObjectId(null));
        assertNull("getPersistenceManager(null) returned non-null", 
                JDOHelper.getPersistenceManager(null));
        assertNull("getTransactionalObjectId(null) returned non-null", 
                JDOHelper.getTransactionalObjectId(null));
        assertNull("getVersion(null) returned non-null", 
                JDOHelper.getVersion(null));
        assertFalse("isDeleted(null) returned non-null", 
                JDOHelper.isDeleted(null));
        assertFalse("isDetached(null) returned non-null", 
                JDOHelper.isDetached(null));
        assertFalse("isDirty(null) returned non-null", 
                JDOHelper.isDirty(null));
        assertFalse("isNew(null) returned non-null", 
                JDOHelper.isNew(null));
        assertFalse("isPersistent(null) returned non-null", 
                JDOHelper.isPersistent(null));
        assertFalse("isTransactional(null) returned non-null", 
                JDOHelper.isTransactional(null));
    }

    /** */
    public void testGetPM() {
        PCPoint p = new PCPoint(1, Integer.valueOf(1));
        if (JDOHelper.getPersistenceManager(p) != null) {
            fail("JDOHelper.getPersistenceManager should return null pm for non-persistent instance");
        }

        // TBD: test for persistent instance
    }

    /** */
    public void testMakeDirty() {
        // TBD: test JDOHelper.makeDirty(pc, fieldName);
    }

    /** */
    public void testGetObjectId() {
        PCPoint p = new PCPoint(1, Integer.valueOf(1));
        if (JDOHelper.getObjectId(p) != null) {
            fail("JDOHelper.getObjectId should return null ObjectId for non-persistent instance");
        }

        // TBD test JDOHelper.getObjectId(pc) for persistent instance
    }

    /** */
    public void testGetTransactionObjectId() {
        PCPoint p = new PCPoint(1, Integer.valueOf(1));
        if (JDOHelper.getObjectId(p) != null) {
            fail("JDOHelper.getTransactionalObjectId should return null ObjectId for non-persistent instance");
        }

        // TBD test JDOHelper.getTransactionalObjectId(pc) for persistent instance
    }

    /** */
    public void testIsDirty() {
        PCPoint p = new PCPoint(1, Integer.valueOf(1));
        if (JDOHelper.isDirty(p)) {
            fail("JDOHelper.isDirty should return false for non-persistent instance");
        }

        // TBD test JDOHelper.isDirty(pc) for persistent instance
    }

    /** */
    public void testIsTransactional() {
        PCPoint p = new PCPoint(1, Integer.valueOf(1));
        if (JDOHelper.isTransactional(p)) {
            fail("JDOHelper.isTransactional should return false for non-persistent instance");
        }

        // TBD test JDOHelper.isTransactional(pc) for persistent instance
    }

    /** */
    public void testIsPersistent() {
        PCPoint p = new PCPoint(1, Integer.valueOf(1));
        if (JDOHelper.isPersistent(p)) {
            fail("JDOHelper.isPersistent should return false for non-persistent instance");
        }

        // TBD test JDOHelper.isPersistent(pc) for persistent instance
    }

    /** */
    public void testIsNew() {
        PCPoint p = new PCPoint(1, Integer.valueOf(1));
        if (JDOHelper.isNew(p)) {
            fail("JDOHelper.isNew should return false for non-persistent instance");
        }

        // TBD test JDOHelper.isNew(pc) for persistent instance
    }


    /** */
    public void testIsDeleted() {
        PCPoint p = new PCPoint(1, Integer.valueOf(1));
        if (JDOHelper.isDeleted(p)) {
            fail("JDOHelper.isDeleted should return false for non-persistent instance");
        }

        // TBD test JDOHelper.isDeleted(pc) for persistent instance
    }
    
    /** Test null String resource with no class loader.
     */
    public void testGetPMFNullResource() {
        try {
            JDOHelper.getPersistenceManagerFactory((String)null);
            fail("Null resource name should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test null String resource with good class loader.
     */
    public void testGetPMFNullResourceGoodClassLoader() {
        try {
            JDOHelper.getPersistenceManagerFactory((String)null, this.getClass().getClassLoader());
            fail("Null resource name should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test bad String resource with no class loader.
     */
    public void testGetPMFBadResource() {
        try {
            JDOHelper.getPersistenceManagerFactory("Whatever");
            fail("Null resource name should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test null String resource with good class loader.
     */
    public void testGetPMFBadResourceGoodClassLoader() {
        try {
            JDOHelper.getPersistenceManagerFactory("Whatever", this.getClass().getClassLoader());
            fail("Null resource name should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test null File resource with no class loader.
     */
    public void testGetPMFNullFile() {
        try {
            JDOHelper.getPersistenceManagerFactory((File)null);
            fail("Null file should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test null File resource with good class loader.
     */
    public void testGetPMFNullFileGoodClassLoader() {
        try {
            JDOHelper.getPersistenceManagerFactory((File)null, this.getClass().getClassLoader());
            fail("Null file should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test bad File resource with no class loader.
     */
    public void testGetPMFBadFile() {
        try {
            JDOHelper.getPersistenceManagerFactory(new File("Whatever"));
            fail("Null file should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test bad File resource with good class loader.
     */
    public void testGetPMFBadFileGoodClassLoader() {
        try {
            JDOHelper.getPersistenceManagerFactory(new File("Whatever"), this.getClass().getClassLoader());
            fail("Null file should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test null JNDI resource name with no class loader.
     */
    public void testGetPMFNullJNDI() {
        try {
            JDOHelper.getPersistenceManagerFactory(null, getInitialContext());
            fail("Null JNDI resource name should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test null JNDI resource name with good class loader.
     */
    public void testGetPMFNullJNDIGoodClassLoader() {
        try {
            JDOHelper.getPersistenceManagerFactory(null, getInitialContext(), this.getClass().getClassLoader());
            fail("Null JNDI resource name should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test bad JNDI resource name with no class loader.
     */
    public void testGetPMFBadJNDI() {
        try {
            JDOHelper.getPersistenceManagerFactory("Whatever", getInitialContext());
            fail("Bad JNDI resource name should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test bad JNDI resource name with good class loader.
     */
    public void testGetPMFBadJNDIGoodClassLoader() {
        try {
            JDOHelper.getPersistenceManagerFactory("Whatever", getInitialContext(), this.getClass().getClassLoader());
            fail("Bad JNDI resource name should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test null stream with no class loader.
     */
    public void testGetPMFNullStream() {
        try {
            JDOHelper.getPersistenceManagerFactory((InputStream)null);
            fail("Null JNDI resource name should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test null stream with good class loader.
     */
    public void testGetPMFNullStreamGoodClassLoader() {
        try {
            JDOHelper.getPersistenceManagerFactory((InputStream)null, this.getClass().getClassLoader());
            fail("Null JNDI resource name should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test null ClassLoader.
     */
    public void testGetPMFNullClassLoader() {
        try {
            JDOHelper.getPersistenceManagerFactory("Whatever", (ClassLoader)null);
            fail("Null ClassLoader should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test both null ClassLoaders.
     */
    public void testGetPMFBothNullClassLoader() {
        try {
            JDOHelper.getPersistenceManagerFactory("Whatever", (ClassLoader)null, null);
            fail("Null ClassLoader should result in JDOFatalUserException");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test missing property javax.jdo.PersistenceManagerFactoryClass.
     */
    public void testGetPMFNoClassNameProperty() {
        try {
            JDOHelper.getPersistenceManagerFactory(new Properties());
            fail("Missing property PersistenceManagerFactoryClass should result in JDOFatalUserException ");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test bad PMF class does not exist.
     */
    public void testBadPMFClassNotFound() {
        Properties props = new Properties();
        props.put("javax.jdo.PersistenceManagerFactoryClass", "ThisClassDoesNotExist");
        try {
            JDOHelper.getPersistenceManagerFactory(props);
            fail("Bad PersistenceManagerFactoryClass should result in JDOFatalUserException ");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test bad PMF class no method getPersistenceManagerFactory(Properties).
     */
    public void testBadPMFNoGetPMFPropertiesMethod() {
        Properties props = new Properties();
        props.put("javax.jdo.PersistenceManagerFactoryClass", 
                "javax.jdo.JDOHelperTest$BadPMFNoGetPMFMethod");
        try {
            JDOHelper.getPersistenceManagerFactory(props);
            fail("Bad PersistenceManagerFactory should result in JDOFatalInternalException ");
        }
        catch (JDOFatalInternalException ex) {
            if (ex.getCause() instanceof NoSuchMethodException) {
                if (verbose)
                    println("Caught expected exception " + ex);
            } else {
                fail("Bad PersistenceManagerFactory should result in " +
                        "JDOFatalInternalException with nested " +
                        "NoSuchMethodException. " +
                        "Actual nested exception was " + ex);
            }
        }
    }

    /** Test bad PMF class no method getPersistenceManagerFactory(Map).
     */
    public void testBadPMFNoGetPMFMapMethod() {
        Map<String,String> props = new HashMap<>();
        props.put("javax.jdo.PersistenceManagerFactoryClass", 
                "javax.jdo.JDOHelperTest$BadPMFNoGetPMFMethod");
        try {
            JDOHelper.getPersistenceManagerFactory(props);
            fail("Bad PersistenceManagerFactory should result in JDOFatalInternalException ");
        }
        catch (JDOFatalInternalException ex) {
            if (ex.getCause() instanceof NoSuchMethodException) {
                if (verbose)
                    println("Caught expected exception " + ex);
            } else {
                fail("Bad PersistenceManagerFactory should result in " +
                        "JDOFatalInternalException with nested " +
                        "NoSuchMethodException. " +
                        "Actual nested exception was " + ex);
            }
        }
    }

    /** Test bad PMF class non-static getPMF method.
     */
    public void testBadPMFNonStaticGetPMFMethod() {
        Properties props = new Properties();
        props.put("javax.jdo.PersistenceManagerFactoryClass", 
                "javax.jdo.JDOHelperTest$BadPMFNonStaticGetPMFMethod");
        try {
            JDOHelper.getPersistenceManagerFactory(props);
            fail("Bad PersistenceManagerFactoryClass should result in JDOFatalInternalException ");
        }
        catch (JDOFatalInternalException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test bad PMF class doesn't implement PMF.
     */
    public void testBadPMFWrongReturnType() {
        Properties props = new Properties();
        props.put("javax.jdo.PersistenceManagerFactoryClass", 
                "javax.jdo.JDOHelperTest$BadPMFWrongReturnType");
        try {
            JDOHelper.getPersistenceManagerFactory(props);
            fail("Bad PersistenceManagerFactoryClass should result in JDOFatalInternalException ");
        }
        catch (JDOFatalInternalException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test bad PMF class getPersistenceManagerFactory throws Exception.
     */
    public void testBadPMFGetPMFMethodThrowsJDOException() {
        Properties props = new Properties();
        props.put("javax.jdo.PersistenceManagerFactoryClass", 
                "javax.jdo.JDOHelperTest$BadPMFGetPMFMethodThrowsJDOException");
        try {
            JDOHelper.getPersistenceManagerFactory(props);
            fail("BadPMFGetPMFMethodThrowsJDOException.GetPersistenceManagerFactory " +
                    "should result in JDOUnsupportedOptionException. " +
                    "No exception was thrown.");
        }
        catch (JDOUnsupportedOptionException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }

    /** Test bad PMF class getPersistenceManagerFactory returns null.
     */
    public void testBadPMFGetPMFMethodReturnsNull() {
        Properties props = new Properties();
        props.put("javax.jdo.PersistenceManagerFactoryClass", 
                "javax.jdo.JDOHelperTest$BadPMFGetPMFMethodReturnsNull");
        try {
            JDOHelper.getPersistenceManagerFactory(props);
            fail("BadPMFGetPMFMethodReturnsNull.GetPersistenceManagerFactory " +
                    "should result in JDOFatalInternalException. " +
                    "No exception was thrown.");
        }
        catch (JDOFatalInternalException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }
    }
    
    /** Test that an unknown standard property cause JDOUserException.
     */
    public void testUnknownStandardProperty() {
        Properties p = new Properties();
        p.setProperty("javax.jdo.unknown.standard.property", "value");
        try {
            JDOHelper.getPersistenceManagerFactory(p);
            fail("testUnknownStandardProperties should result in JDOUserException. "
                    + "No exception was thrown.");
        } catch (JDOUserException x) {
            if (verbose)
                println("Caught expected exception " + x);
        }
    }

    /**
     * Test that unknown standard properties cause JDOUserException w/nested
     * exceptions.
     */
    public void testUnknownStandardProperties() {
        Properties p = new Properties();
        p.setProperty("javax.jdo.unknown.standard.property.1", "value");
        p.setProperty("javax.jdo.unknown.standard.property.2", "value");

        JDOUserException x = null;

        try {
            JDOHelper.getPersistenceManagerFactory(p);
            fail("testUnknownStandardProperties should result in JDOUserException. "
                    + "No exception was thrown.");
        } catch (JDOUserException thrown) {
            if (verbose)
                println("Caught expected exception " + thrown);
            x = thrown;
        }

        Throwable[] nesteds = x.getNestedExceptions();

        assertNotNull(nesteds);
        assertEquals("should have been 2 nested exceptions", 2, nesteds.length);
        for (int i = 0; i < nesteds.length; i++) {
            Throwable t = nesteds[i];
            assertTrue("nested exception " + i
                    + " should have been JDOUserException",
                    t instanceof JDOUserException);
        }
    }
    
    private Context getInitialContext() {
        try {
            return new InitialContext();
        } catch (NamingException ne) {
            fail("Could not get Initial Context");
            return null;
        }
    }
    
    public static class BadPMFNoGetPMFMethod {
    }

    public static class BadPMFNonStaticGetPMFMethod {
        public PersistenceManagerFactory
                getPersistenceManagerFactory(Map<?, ?> props) {
            return null;
        }
    }
        
    public static class BadPMFWrongReturnType {
        public static BadPMFWrongReturnType 
                getPersistenceManagerFactory(Map<?, ?> props) {
            return new BadPMFWrongReturnType();
        }
    }
    
    public static class BadPMFGetPMFMethodThrowsJDOException {
        public static PersistenceManagerFactory
                getPersistenceManagerFactory(Map<?, ?> props) {
            throw new JDOUnsupportedOptionException(
                    "GetPMF method throws JDOUnsupportedOptionException");
        }
    }

    public static class BadPMFGetPMFMethodThrowsJDOFatalInternalException {
        public static PersistenceManagerFactory
                getPersistenceManagerFactory(Map<?, ?> props) {
            throw new JDOFatalInternalException(
                    "GetPMF method throws JDOFatalInternalException");
        }
    }

    public static class BadPMFGetPMFMethodReturnsNull {
        public static PersistenceManagerFactory
                getPersistenceManagerFactory(Map<?, ?> props) {
            return null;
        }
    }
}
