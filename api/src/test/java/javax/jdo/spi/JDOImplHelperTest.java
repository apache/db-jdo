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

package javax.jdo.spi;

import java.util.Collection;
import java.util.Properties;
import javax.jdo.JDOConstants;
import javax.jdo.JDOUserException;
import javax.jdo.pc.PCPoint;
import javax.jdo.util.AbstractTest;
import javax.jdo.util.BatchTestRunner;

/**
 * Tests class javax.jdo.spi.JDOImplHelper.
 *
 * <p>Missing: testNewInstance + testNewObjectIdInstance Missing: tests for JDOImplHelper methods:
 * copyKeyFieldsToObjectId and copyKeyFieldsFromObjectId.
 */
public class JDOImplHelperTest extends AbstractTest {

  /** */
  private RegisterClassEvent event;

  /** */
  public static void main(String args[]) {
    BatchTestRunner.run(JDOImplHelperTest.class);
  }

  /** */
  public void setUp() {
    // make sure PCClass is loaded before any tests are run
    new PCPoint(1, Integer.valueOf(1));
  }

  /** */
  public void testGetFieldNames() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();
    String[] fieldNames = implHelper.getFieldNames(PCPoint.class);
    if (fieldNames == null) {
      fail("array of field names is null");
    }
    if (fieldNames.length != 2) {
      fail("Unexpected length of fieldNames; expected 2, got " + fieldNames.length);
    }
    if (!fieldNames[0].equals("x")) {
      fail("Unexpected field; expected x, got " + fieldNames[0]);
    }
    if (!fieldNames[1].equals("y")) {
      fail("Unexpected field; expected y, got " + fieldNames[1]);
    }
  }

  /** */
  public void testGetFieldTypes() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();
    Class<?>[] fieldTypes = implHelper.getFieldTypes(PCPoint.class);
    if (fieldTypes == null) {
      fail("array of field types is null");
    }
    if (fieldTypes.length != 2) {
      fail("Unexpected length of fieldTypes; expected 2, got " + fieldTypes.length);
    }
    if (fieldTypes[0] != int.class) {
      fail("Unexpected field type; expected int, got " + fieldTypes[0]);
    }
    if (fieldTypes[1] != Integer.class) {
      fail("Unexpected field type; expected Integer, got " + fieldTypes[1]);
    }
  }

  /** */
  public void testGetFieldFlags() {
    byte expected =
        (byte)
            (PersistenceCapable.CHECK_READ
                + PersistenceCapable.CHECK_WRITE
                + PersistenceCapable.SERIALIZABLE);

    JDOImplHelper implHelper = JDOImplHelper.getInstance();
    byte[] fieldFlags = implHelper.getFieldFlags(PCPoint.class);
    if (fieldFlags == null) {
      fail("array of field flags is null");
    }
    if (fieldFlags.length != 2) {
      fail("Unexpected length of fieldFlags; expected 2, got " + fieldFlags.length);
    }
    if (fieldFlags[0] != expected) {
      fail("Unexpected field flag; expected " + expected + ", got " + fieldFlags[0]);
    }
    if (fieldFlags[1] != expected) {
      fail("Unexpected field flag; expected " + expected + ", got " + fieldFlags[1]);
    }
  }

  /** */
  public void testGetPCSuperclass() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();
    Class<?> pcSuper = implHelper.getPersistenceCapableSuperclass(PCPoint.class);
    if (pcSuper != null) {
      fail("Wrong pc superclass of PCPoint; expected null, got " + pcSuper);
    }
  }

  /** */
  public void testNewInstance() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();
    PersistenceCapable pcpoint = implHelper.newInstance(PCPoint.class, null);
    assertTrue("instance created by newInstance is not a PCPoint", pcpoint instanceof PCPoint);

    pcpoint = implHelper.newInstance(PCPoint.class, null, null);
    assertTrue("instance created by newInstance is not a PCPoint", pcpoint instanceof PCPoint);
  }

  /** */
  public void testNewObjectIdInstance() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();
    Object oid = implHelper.newObjectIdInstance(PCPoint.class);
    // The ObjrectId od javax.jdo.pc.PCPoint is always null
    assertNull("Unexpected non-null ObjectId of PCPoint", oid);
  }

  /** */
  public void testClassRegistration() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();

    Collection<Class<?>> registeredClasses = implHelper.getRegisteredClasses();
    // test whether PCPoint is registered
    if (!registeredClasses.contains(PCPoint.class)) {
      fail("Missing registration of pc class PCPoint");
    }

    // Save registered meta data for restoring
    String[] fieldNames = implHelper.getFieldNames(PCPoint.class);
    Class<?>[] fieldTypes = implHelper.getFieldTypes(PCPoint.class);
    byte[] fieldFlags = implHelper.getFieldFlags(PCPoint.class);
    Class<?> pcSuperclass = implHelper.getPersistenceCapableSuperclass(PCPoint.class);

    // test unregisterClass with null parameter
    try {
      implHelper.unregisterClass(null);
      fail("Missing exception when calling unregisterClass(null)");
    } catch (NullPointerException ex) {
      // expected exception => OK
    }

    // test unregister PCPoint class
    implHelper.unregisterClass(PCPoint.class);
    registeredClasses = implHelper.getRegisteredClasses();
    if (registeredClasses.contains(PCPoint.class)) {
      fail("PCPoint still registered");
    }

    // register PCPoint again
    JDOImplHelper.registerClass(
        PCPoint.class, fieldNames, fieldTypes, fieldFlags, pcSuperclass, new PCPoint());
  }

  /** */
  public void testClassListenerRegistration() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();

    // add listener and check event
    event = null;
    RegisterClassListener listener = new SimpleListener();
    implHelper.addRegisterClassListener(listener);
    JDOImplHelper.registerClass(
        JDOImplHelperTest.class, new String[0], new Class[0], new byte[0], null, null);
    if (event == null) {
      fail("Missing event ");
    }

    // remove listener and check event
    event = null;
    implHelper.removeRegisterClassListener(listener);
    JDOImplHelper.registerClass(
        JDOImplHelperTest.class, new String[0], new Class[0], new byte[0], null, null);
    if (event != null) {
      fail("Unexpected event " + event);
    }
  }

  /** Test that an unknown standard property causes JDOUserException. */
  public void testUnknownStandardProperty() {
    Properties p = new Properties();
    p.setProperty("javax.jdo.unknown.standard.property", "value");

    JDOUserException x = null;

    try {
      JDOImplHelper.assertOnlyKnownStandardProperties(p);
      fail(
          "testUnknownStandardProperty should result in JDOUserException. "
              + "No exception was thrown.");
    } catch (JDOUserException thrown) {
      if (verbose) println("Caught expected exception " + thrown);
      x = thrown;
    }
    assertNull("should have had no nested exceptions", x.getNestedExceptions());
  }

  /** Test that unknown standard properties cause JDOUserException w/nested exceptions. */
  public void testUnknownStandardProperties() {
    Properties p = new Properties();
    p.setProperty("javax.jdo.unknown.standard.property.1", "value");
    p.setProperty("javax.jdo.unknown.standard.property.2", "value");

    JDOUserException x = null;

    try {
      JDOImplHelper.assertOnlyKnownStandardProperties(p);
      fail(
          "testUnknownStandardProperties should result in JDOUserException. "
              + "No exception was thrown.");
    } catch (JDOUserException thrown) {
      if (verbose) println("Caught expected exception " + thrown);
      x = thrown;
    }

    Throwable[] nesteds = x.getNestedExceptions();

    assertNotNull(nesteds);
    assertEquals("should have been 2 nested exceptions", 2, nesteds.length);
    for (int i = 0; i < nesteds.length; i++) {
      Throwable t = nesteds[i];
      assertTrue(
          "nested exception " + i + " should have been JDOUserException",
          t instanceof JDOUserException);
    }
  }

  /**
   * Test that unknown non-standard properties & well-formed listener properties don't cause
   * JDOUserException.
   */
  public void testUnknownNonStandardPropertiesAndListeners() {
    Properties p = new Properties();
    p.put("unknown.property", "value");
    p.put(new Object(), "value");
    p.put(JDOConstants.PROPERTY_PREFIX_INSTANCE_LIFECYCLE_LISTENER + "unknown.listener", "value");
    JDOImplHelper.assertOnlyKnownStandardProperties(p);
  }

  /** Test that all JDO standard properties don't cause JDOUserException. */
  public void testOnlyStandardProperties() {
    Properties props = new Properties();
    for (String p : JDOImplHelper.USER_CONFIGURABLE_STANDARD_PROPERTIES) {
      props.setProperty(p, p);
    }
    JDOImplHelper.assertOnlyKnownStandardProperties(props);
  }

  /** Test that an known standard property in mixed case succeeds. */
  public void testKnownStandardPropertyThatDiffersInCaseOnly() {
    Properties p = new Properties();
    p.setProperty("JaVaX.jDo.oPtIoN.CoNNectionDRiVerNamE", "value");
    p.setProperty("jAvAx.JdO.lIsTeNeR.InstaNceLifeCycleLisTener.foo.Bar", "");
    JDOImplHelper.assertOnlyKnownStandardProperties(p);
  }

  /** */
  class SimpleListener implements RegisterClassListener {

    /** */
    public void registerClass(RegisterClassEvent event) {
      JDOImplHelperTest.this.event = event;
    }
  }
}
