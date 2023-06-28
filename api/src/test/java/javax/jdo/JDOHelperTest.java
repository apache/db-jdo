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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.jdo.pc.PCPoint;
import javax.jdo.util.AbstractTest;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.junit.jupiter.api.Test;

/**
 * Tests class javax.jdo.JDOHelper.
 *
 * <p>TBD: implementation of testMakeDirty, TBD: testing interrogative methods for persistent
 * instances TBD: getPMF for valid PMF class
 */
class JDOHelperTest extends AbstractTest {

  /**
   * The purpose of this test is simply to call some of the methods on a constructed instance of
   * JDOHelper and verify that they do not throw exceptions. It is not a functional test.
   *
   * @since 2.1
   */
  @Test
  void testConstructor() {
    assertNull(JDOHelper.getObjectId(null), "getObjectId(null) returned non-null");
    assertNull(
        JDOHelper.getPersistenceManager(null), "getPersistenceManager(null) returned non-null");
    assertNull(
        JDOHelper.getTransactionalObjectId(null),
        "getTransactionalObjectId(null) returned non-null");
    assertNull(JDOHelper.getVersion(null), "getVersion(null) returned non-null");
    assertFalse(JDOHelper.isDeleted(null), "isDeleted(null) returned non-null");
    assertFalse(JDOHelper.isDetached(null), "isDetached(null) returned non-null");
    assertFalse(JDOHelper.isDirty(null), "isDirty(null) returned non-null");
    assertFalse(JDOHelper.isNew(null), "isNew(null) returned non-null");
    assertFalse(JDOHelper.isPersistent(null), "isPersistent(null) returned non-null");
    assertFalse(JDOHelper.isTransactional(null), "isTransactional(null) returned non-null");
  }

  /**
   * The purpose of this test is simply to call some of the methods on the static instance of
   * JDOHelper and verify that they do not throw exceptions. It is not a functional test.
   *
   * @since 2.1
   */
  @Test
  void testGetInstance() {
    assertNull(JDOHelper.getObjectId(null), "getObjectId(null) returned non-null");
    assertNull(
        JDOHelper.getPersistenceManager(null), "getPersistenceManager(null) returned non-null");
    assertNull(
        JDOHelper.getTransactionalObjectId(null),
        "getTransactionalObjectId(null) returned non-null");
    assertNull(JDOHelper.getVersion(null), "getVersion(null) returned non-null");
    assertFalse(JDOHelper.isDeleted(null), "isDeleted(null) returned non-null");
    assertFalse(JDOHelper.isDetached(null), "isDetached(null) returned non-null");
    assertFalse(JDOHelper.isDirty(null), "isDirty(null) returned non-null");
    assertFalse(JDOHelper.isNew(null), "isNew(null) returned non-null");
    assertFalse(JDOHelper.isPersistent(null), "isPersistent(null) returned non-null");
    assertFalse(JDOHelper.isTransactional(null), "isTransactional(null) returned non-null");
  }

  /** */
  @Test
  void testGetPM() {
    PCPoint p = new PCPoint(1, Integer.valueOf(1));
    assertNull(
        JDOHelper.getPersistenceManager(p),
        "JDOHelper.getPersistenceManager should return null pm for non-persistent instance");

    // TBD: test for persistent instance
  }

  /** */
  @Test
  void testMakeDirty() {
    PCPoint p = new PCPoint(1, Integer.valueOf(1));
    JDOHelper.makeDirty(p, "x");
    assertFalse(JDOHelper.isDirty(p), "JDOHelper.makeDirty should ignore non-persistent instance");

    // TBD: test for persistent instance
  }

  /** */
  @Test
  void testGetObjectId() {
    PCPoint p = new PCPoint(1, Integer.valueOf(1));
    assertNull(
        JDOHelper.getObjectId(p),
        "JDOHelper.getObjectId should return null ObjectId for non-persistent instance");

    // TBD test JDOHelper.getObjectId(pc) for persistent instance
  }

  /** */
  @Test
  void testGetTransactionObjectId() {
    PCPoint p = new PCPoint(1, Integer.valueOf(1));
    assertNull(
        JDOHelper.getObjectId(p),
        "JDOHelper.getTransactionalObjectId should return null ObjectId for non-persistent instance");

    // TBD test JDOHelper.getTransactionalObjectId(pc) for persistent instance
  }

  /** */
  @Test
  void testIsDirty() {
    PCPoint p = new PCPoint(1, Integer.valueOf(1));
    assertFalse(
        JDOHelper.isDirty(p), "JDOHelper.isDirty should return false for non-persistent instance");

    // TBD test JDOHelper.isDirty(pc) for persistent instance
  }

  /** */
  @Test
  void testIsTransactional() {
    PCPoint p = new PCPoint(1, Integer.valueOf(1));
    assertFalse(
        JDOHelper.isTransactional(p),
        "JDOHelper.isTransactional should return false for non-persistent instance");

    // TBD test JDOHelper.isTransactional(pc) for persistent instance
  }

  /** */
  @Test
  void testIsPersistent() {
    PCPoint p = new PCPoint(1, Integer.valueOf(1));
    assertFalse(
        JDOHelper.isPersistent(p),
        "JDOHelper.isPersistent should return false for non-persistent instance");

    // TBD test JDOHelper.isPersistent(pc) for persistent instance
  }

  /** */
  @Test
  void testIsNew() {
    PCPoint p = new PCPoint(1, Integer.valueOf(1));
    assertFalse(
        JDOHelper.isNew(p), "JDOHelper.isNew should return false for non-persistent instance");

    // TBD test JDOHelper.isNew(pc) for persistent instance
  }

  /** */
  @Test
  void testIsDeleted() {
    PCPoint p = new PCPoint(1, Integer.valueOf(1));
    assertFalse(
        JDOHelper.isDeleted(p),
        "JDOHelper.isDeleted should return false for non-persistent instance");

    // TBD test JDOHelper.isDeleted(pc) for persistent instance
  }

  /** Test null String resource with no class loader. */
  @Test
  void testGetPMFNullResource() {
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory((String) null),
        "Null resource name should result in JDOFatalUserException");
  }

  /** Test null String resource with good class loader. */
  @Test
  void testGetPMFNullResourceGoodClassLoader() {
    ClassLoader loader = this.getClass().getClassLoader();
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory((String) null, loader),
        "Null resource name should result in JDOFatalUserException");
  }

  /** Test bad String resource with no class loader. */
  @Test
  void testGetPMFBadResource() {
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory("Whatever"),
        "Null resource name should result in JDOFatalUserException");
  }

  /** Test null String resource with good class loader. */
  @Test
  void testGetPMFBadResourceGoodClassLoader() {
    ClassLoader loader = this.getClass().getClassLoader();
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory("Whatever", loader),
        "Null resource name should result in JDOFatalUserException");
  }

  /** Test null File resource with no class loader. */
  @Test
  void testGetPMFNullFile() {
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory((File) null),
        "Null file should result in JDOFatalUserException");
  }

  /** Test null File resource with good class loader. */
  @Test
  void testGetPMFNullFileGoodClassLoader() {
    ClassLoader loader = this.getClass().getClassLoader();
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory((File) null, loader),
        "Null file should result in JDOFatalUserException");
  }

  /** Test bad File resource with no class loader. */
  @Test
  void testGetPMFBadFile() {
    File file = new File("Whatever");
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory(file),
        "Null file should result in JDOFatalUserException");
  }

  /** Test bad File resource with good class loader. */
  @Test
  void testGetPMFBadFileGoodClassLoader() {
    File file = new File("Whatever");
    ClassLoader loader = this.getClass().getClassLoader();
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory(file, loader),
        "Null file should result in JDOFatalUserException");
  }

  /** Test null JNDI resource name with no class loader. */
  @Test
  void testGetPMFNullJNDI() {
    Context context = getInitialContext();
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory(null, context),
        "Null JNDI resource name should result in JDOFatalUserException");
  }

  /** Test null JNDI resource name with good class loader. */
  @Test
  void testGetPMFNullJNDIGoodClassLoader() {
    Context context = getInitialContext();
    ClassLoader loader = this.getClass().getClassLoader();
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory(null, context, loader),
        "Null JNDI resource name should result in JDOFatalUserException");
  }

  /** Test bad JNDI resource name with no class loader. */
  @Test
  void testGetPMFBadJNDI() {
    Context context = getInitialContext();
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory("Whatever", context),
        "Bad JNDI resource name should result in JDOFatalUserException");
  }

  /** Test bad JNDI resource name with good class loader. */
  @Test
  void testGetPMFBadJNDIGoodClassLoader() {
    Context context = getInitialContext();
    ClassLoader loader = this.getClass().getClassLoader();
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory("Whatever", context, loader),
        "Bad JNDI resource name should result in JDOFatalUserException");
  }

  /** Test null stream with no class loader. */
  @Test
  void testGetPMFNullStream() {
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory((InputStream) null),
        "Null JNDI resource name should result in JDOFatalUserException");
  }

  /** Test null stream with good class loader. */
  @Test
  void testGetPMFNullStreamGoodClassLoader() {
    ClassLoader loader = this.getClass().getClassLoader();
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory((InputStream) null, loader),
        "Null JNDI resource name should result in JDOFatalUserException");
  }

  /** Test null ClassLoader. */
  @Test
  void testGetPMFNullClassLoader() {
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory("Whatever", (ClassLoader) null),
        "Null ClassLoader should result in JDOFatalUserException");
  }

  /** Test both null ClassLoaders. */
  @Test
  void testGetPMFBothNullClassLoader() {
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory("Whatever", (ClassLoader) null, null),
        "Null ClassLoader should result in JDOFatalUserException");
  }

  /** Test missing property javax.jdo.PersistenceManagerFactoryClass. */
  @Test
  void testGetPMFNoClassNameProperty() {
    Properties props = new Properties();
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory(props),
        "Missing property PersistenceManagerFactoryClass should result in JDOFatalUserException ");
  }

  /** Test bad PMF class does not exist. */
  @Test
  void testBadPMFClassNotFound() {
    Properties props = new Properties();
    props.put("javax.jdo.PersistenceManagerFactoryClass", "ThisClassDoesNotExist");
    assertThrows(
        JDOFatalUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory(props),
        "Bad PersistenceManagerFactoryClass should result in JDOFatalUserException ");
  }

  /** Test bad PMF class no method getPersistenceManagerFactory(Properties). */
  @Test
  void testBadPMFNoGetPMFPropertiesMethod() {
    Properties props = new Properties();
    props.put(
        "javax.jdo.PersistenceManagerFactoryClass", "javax.jdo.JDOHelperTest$BadPMFNoGetPMFMethod");
    JDOFatalInternalException ex =
        assertThrows(
            JDOFatalInternalException.class,
            () -> JDOHelper.getPersistenceManagerFactory(props).getConnectionFactory(),
            "Bad PersistenceManagerFactory should result in JDOFatalInternalException ");
    if (ex.getCause() instanceof NoSuchMethodException) {
      if (verbose) println("Caught expected exception " + ex);
    } else {
      fail(
          "Bad PersistenceManagerFactory should result in "
              + "JDOFatalInternalException with nested "
              + "NoSuchMethodException. "
              + "Actual nested exception was "
              + ex);
    }
  }

  /** Test bad PMF class no method getPersistenceManagerFactory(Map). */
  @Test
  void testBadPMFNoGetPMFMapMethod() {
    Map<String, String> props = new HashMap<>();
    props.put(
        "javax.jdo.PersistenceManagerFactoryClass", "javax.jdo.JDOHelperTest$BadPMFNoGetPMFMethod");
    JDOFatalInternalException ex =
        assertThrows(
            JDOFatalInternalException.class,
            () -> JDOHelper.getPersistenceManagerFactory(props),
            "Bad PersistenceManagerFactory should result in JDOFatalInternalException ");
    if (ex.getCause() instanceof NoSuchMethodException) {
      if (verbose) println("Caught expected exception " + ex);
    } else {
      fail(
          "Bad PersistenceManagerFactory should result in "
              + "JDOFatalInternalException with nested "
              + "NoSuchMethodException. "
              + "Actual nested exception was "
              + ex);
    }
  }

  /** Test bad PMF class non-static getPMF method. */
  @Test
  void testBadPMFNonStaticGetPMFMethod() {
    Properties props = new Properties();
    props.put(
        "javax.jdo.PersistenceManagerFactoryClass",
        "javax.jdo.JDOHelperTest$BadPMFNonStaticGetPMFMethod");
    assertThrows(
        JDOFatalInternalException.class,
        () -> JDOHelper.getPersistenceManagerFactory(props),
        "Bad PersistenceManagerFactoryClass should result in JDOFatalInternalException ");
  }

  /** Test bad PMF class doesn't implement PMF. */
  @Test
  void testBadPMFWrongReturnType() {
    Properties props = new Properties();
    props.put(
        "javax.jdo.PersistenceManagerFactoryClass",
        "javax.jdo.JDOHelperTest$BadPMFWrongReturnType");
    assertThrows(
        JDOFatalInternalException.class,
        () -> JDOHelper.getPersistenceManagerFactory(props),
        "Bad PersistenceManagerFactoryClass should result in JDOFatalInternalException ");
  }

  /** Test bad PMF class getPersistenceManagerFactory throws Exception. */
  @Test
  void testBadPMFGetPMFMethodThrowsJDOException() {
    Properties props = new Properties();
    props.put(
        "javax.jdo.PersistenceManagerFactoryClass",
        "javax.jdo.JDOHelperTest$BadPMFGetPMFMethodThrowsJDOException");
    assertThrows(
        JDOUnsupportedOptionException.class,
        () -> JDOHelper.getPersistenceManagerFactory(props),
        "BadPMFGetPMFMethodThrowsJDOException.GetPersistenceManagerFactory "
            + "should result in JDOUnsupportedOptionException. "
            + "No exception was thrown.");
  }

  /** Test bad PMF class getPersistenceManagerFactory returns null. */
  @Test
  void testBadPMFGetPMFMethodReturnsNull() {
    Properties props = new Properties();
    props.put(
        "javax.jdo.PersistenceManagerFactoryClass",
        "javax.jdo.JDOHelperTest$BadPMFGetPMFMethodReturnsNull");
    assertThrows(
        JDOFatalInternalException.class,
        () -> JDOHelper.getPersistenceManagerFactory(props),
        "BadPMFGetPMFMethodReturnsNull.GetPersistenceManagerFactory "
            + "should result in JDOFatalInternalException. "
            + "No exception was thrown.");
  }

  /** Test that an unknown standard property cause JDOUserException. */
  @Test
  void testUnknownStandardProperty() {
    Properties p = new Properties();
    p.setProperty("javax.jdo.unknown.standard.property", "value");
    assertThrows(
        JDOUserException.class,
        () -> JDOHelper.getPersistenceManagerFactory(p),
        "testUnknownStandardProperties should result in JDOUserException. No exception was thrown.");
  }

  /** Test that unknown standard properties cause JDOUserException w/nested exceptions. */
  @Test
  void testUnknownStandardProperties() {
    Properties p = new Properties();
    p.setProperty("javax.jdo.unknown.standard.property.1", "value");
    p.setProperty("javax.jdo.unknown.standard.property.2", "value");

    JDOUserException x =
        assertThrows(
            JDOUserException.class,
            () -> JDOHelper.getPersistenceManagerFactory(p),
            "testUnknownStandardProperties should result in JDOUserException. "
                + "No exception was thrown.");

    Throwable[] nesteds = x.getNestedExceptions();

    assertNotNull(nesteds);
    assertEquals(2, nesteds.length, "should have been 2 nested exceptions");
    for (int i = 0; i < nesteds.length; i++) {
      Throwable t = nesteds[i];
      assertTrue(
          t instanceof JDOUserException,
          "nested exception " + i + " should have been JDOUserException");
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

  public static class BadPMFNoGetPMFMethod {}

  public static class BadPMFNonStaticGetPMFMethod {
    public PersistenceManagerFactory getPersistenceManagerFactory(Map<?, ?> props) {
      return null;
    }
  }

  public static class BadPMFWrongReturnType {
    public static BadPMFWrongReturnType getPersistenceManagerFactory(Map<?, ?> props) {
      return new BadPMFWrongReturnType();
    }
  }

  public static class BadPMFGetPMFMethodThrowsJDOException {
    public static PersistenceManagerFactory getPersistenceManagerFactory(Map<?, ?> props) {
      throw new JDOUnsupportedOptionException("GetPMF method throws JDOUnsupportedOptionException");
    }
  }

  public static class BadPMFGetPMFMethodThrowsJDOFatalInternalException {
    public static PersistenceManagerFactory getPersistenceManagerFactory(Map<?, ?> props) {
      throw new JDOFatalInternalException("GetPMF method throws JDOFatalInternalException");
    }
  }

  public static class BadPMFGetPMFMethodReturnsNull {
    public static PersistenceManagerFactory getPersistenceManagerFactory(Map<?, ?> props) {
      return null;
    }
  }
}
