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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Properties;
import javax.jdo.Constants;
import javax.jdo.JDOUserException;
import javax.jdo.pc.PCPoint;
import javax.jdo.util.AbstractTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests class javax.jdo.spi.JDOImplHelper.
 *
 * <p>Missing: testNewInstance + testNewObjectIdInstance Missing: tests for JDOImplHelper methods:
 * copyKeyFieldsToObjectId and copyKeyFieldsFromObjectId.
 */
class JDOImplHelperTest extends AbstractTest {

  /** */
  private RegisterClassEvent event;

  /** */
  @BeforeEach
  void setUp() {
    // make sure PCClass is loaded before any tests are run
    new PCPoint(1, Integer.valueOf(1));
  }

  /** */
  @Test
  void testGetFieldNames() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();
    String[] fieldNames = implHelper.getFieldNames(PCPoint.class);
    assertNotNull(fieldNames, "array of field names is null");
    assertEquals(2, fieldNames.length, "Unexpected length of fieldNames");
    assertEquals("x", fieldNames[0], "Unexpected field");
    assertEquals("y", fieldNames[1], "Unexpected field");
  }

  /** */
  @Test
  void testGetFieldTypes() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();
    Class<?>[] fieldTypes = implHelper.getFieldTypes(PCPoint.class);
    assertNotNull(fieldTypes, "array of field types is null");
    assertEquals(2, fieldTypes.length, "Unexpected length of fieldTypes");
    assertEquals(int.class, fieldTypes[0], "Unexpected field type");
    assertEquals(Integer.class, fieldTypes[1], "Unexpected field type");
  }

  /** */
  @Test
  void testGetFieldFlags() {
    byte expected =
        (byte)
            (PersistenceCapable.CHECK_READ
                + PersistenceCapable.CHECK_WRITE
                + PersistenceCapable.SERIALIZABLE);

    JDOImplHelper implHelper = JDOImplHelper.getInstance();
    byte[] fieldFlags = implHelper.getFieldFlags(PCPoint.class);
    assertNotNull(fieldFlags, "array of field flags is null");
    assertEquals(2, fieldFlags.length, "Unexpected length of fieldFlags");
    assertEquals(expected, fieldFlags[0], "Unexpected field flag");
    assertEquals(expected, fieldFlags[1], "Unexpected field flag");
  }

  /** */
  @Test
  void testGetPCSuperclass() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();
    Class<?> pcSuper = implHelper.getPersistenceCapableSuperclass(PCPoint.class);
    assertNull(pcSuper, "Wrong pc superclass of PCPoint");
  }

  /** */
  @Test
  void testNewInstance() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();
    PersistenceCapable pcpoint = implHelper.newInstance(PCPoint.class, null);
    assertTrue(pcpoint instanceof PCPoint, "instance created by newInstance is not a PCPoint");

    pcpoint = implHelper.newInstance(PCPoint.class, null, null);
    assertTrue(pcpoint instanceof PCPoint, "instance created by newInstance is not a PCPoint");
  }

  /** */
  @Test
  void testNewObjectIdInstance() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();
    Object oid = implHelper.newObjectIdInstance(PCPoint.class);
    // The ObjectId od javax.jdo.pc.PCPoint is always null
    assertNull(oid, "Unexpected non-null ObjectId of PCPoint");
  }

  /** */
  @Test
  void testClassRegistration() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();

    Collection<Class<?>> registeredClasses = implHelper.getRegisteredClasses();

    // test whether PCPoint is registered
    assertTrue(
        registeredClasses.contains(PCPoint.class), "Missing registration of pc class PCPoint");

    // Save registered meta data for restoring
    String[] fieldNames = implHelper.getFieldNames(PCPoint.class);
    Class<?>[] fieldTypes = implHelper.getFieldTypes(PCPoint.class);
    byte[] fieldFlags = implHelper.getFieldFlags(PCPoint.class);
    Class<?> pcSuperclass = implHelper.getPersistenceCapableSuperclass(PCPoint.class);

    // test unregisterClass with null parameter
    assertThrows(
        NullPointerException.class,
        () -> implHelper.unregisterClass(null),
        "Missing exception when calling unregisterClass(null)");

    // test unregister PCPoint class
    implHelper.unregisterClass(PCPoint.class);
    registeredClasses = implHelper.getRegisteredClasses();
    assertFalse(registeredClasses.contains(PCPoint.class), "PCPoint still registered");

    // register PCPoint again
    JDOImplHelper.registerClass(
        PCPoint.class, fieldNames, fieldTypes, fieldFlags, pcSuperclass, new PCPoint());
  }

  /** */
  @Test
  void testClassListenerRegistration() {
    JDOImplHelper implHelper = JDOImplHelper.getInstance();

    // add listener and check event
    event = null;
    RegisterClassListener listener = new SimpleListener();
    implHelper.addRegisterClassListener(listener);
    JDOImplHelper.registerClass(
        JDOImplHelperTest.class, new String[0], new Class[0], new byte[0], null, null);
    assertNotNull(event, "Missing event ");

    // remove listener and check event
    event = null;
    implHelper.removeRegisterClassListener(listener);
    JDOImplHelper.registerClass(
        JDOImplHelperTest.class, new String[0], new Class[0], new byte[0], null, null);
    assertNull(event, "Unexpected event ");
  }

  /** Test that an unknown standard property causes JDOUserException. */
  @Test
  void testUnknownStandardProperty() {
    Properties p = new Properties();
    p.setProperty("javax.jdo.unknown.standard.property", "value");

    JDOUserException thrown =
        assertThrows(
            JDOUserException.class,
            () -> JDOImplHelper.assertOnlyKnownStandardProperties(p),
            "testUnknownStandardProperty should result in JDOUserException. No exception was thrown.");
    assertNull(thrown.getNestedExceptions(), "should have had no nested exceptions");
  }

  /** Test that unknown standard properties cause JDOUserException w/nested exceptions. */
  @Test
  void testUnknownStandardProperties() {
    Properties p = new Properties();
    p.setProperty("javax.jdo.unknown.standard.property.1", "value");
    p.setProperty("javax.jdo.unknown.standard.property.2", "value");

    JDOUserException thrown =
        assertThrows(
            JDOUserException.class,
            () -> JDOImplHelper.assertOnlyKnownStandardProperties(p),
            "testUnknownStandardProperties should result in JDOUserException. No exception was thrown.");

    Throwable[] nesteds = thrown.getNestedExceptions();

    assertNotNull(nesteds);
    assertEquals(2, nesteds.length, "should have been 2 nested exceptions");
    for (int i = 0; i < nesteds.length; i++) {
      Throwable t = nesteds[i];
      assertTrue(
          t instanceof JDOUserException,
          "nested exception " + i + " should have been JDOUserException");
    }
  }

  /**
   * Test that unknown non-standard properties & well-formed listener properties don't cause
   * JDOUserException.
   */
  @Test
  void testUnknownNonStandardPropertiesAndListeners() {
    Properties p = new Properties();
    p.put("unknown.property", "value");
    p.put(new Object(), "value");
    p.put(Constants.PROPERTY_PREFIX_INSTANCE_LIFECYCLE_LISTENER + "unknown.listener", "value");
    JDOImplHelper.assertOnlyKnownStandardProperties(p);
  }

  /** Test that all JDO standard properties don't cause JDOUserException. */
  @Test
  void testOnlyStandardProperties() {
    Properties props = new Properties();
    for (String p : JDOImplHelper.USER_CONFIGURABLE_STANDARD_PROPERTIES) {
      props.setProperty(p, p);
    }
    JDOImplHelper.assertOnlyKnownStandardProperties(props);
  }

  /** Test that an known standard property in mixed case succeeds. */
  @Test
  void testKnownStandardPropertyThatDiffersInCaseOnly() {
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
