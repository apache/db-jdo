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
package org.apache.jdo.tck.api.converter;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.converter.IPCPoint;
import org.apache.jdo.tck.pc.converter.PCPoint;
import org.apache.jdo.tck.pc.converter.PCPointAnnotated;
import org.apache.jdo.tck.pc.converter.PCPointProp;
import org.apache.jdo.tck.pc.converter.PCPointPropAnnotated;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.IntegerToStringConverter;

import javax.jdo.JDOHelper;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * <B>Title:</B>IntAttributeConverterTest <br>
 * <B>Keywords:</B> mapping <br>
 * <B>Assertion ID:</B> [not identified] <br>
 * <B>Assertion Description: </B> A IPCPoint instance has an int and an Integer field, that are
 * stored as strings in the datastore. The int / Integer fields are converted using an
 * AttributeConverter.
 */
public class IntAttributeConverterTest extends JDO_Test {

  private static final int MIN_X = 1;
  private static final int MIN_Y = 5;

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(IntAttributeConverterTest.class);
  }

  /**
   * @see JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PCPoint.class);
    addTearDownClass(PCPointAnnotated.class);
    addTearDownClass(PCPointProp.class);
    addTearDownClass(PCPointPropAnnotated.class);
  }

  /** Test method creating and storing a PCPoint instance. */
  public void testStorePCPointInstance() {
    runStoreIPCPointInstance(PCPoint.class);
  }

  /** Test method reading a PCPoint instance from the datastore. */
  public void testReadPCPointInstance() {
    runReadIPCPointInstance(PCPoint.class);
  }

  /** Test method modifying a PCPoint instance and storing in the datastore. */
  public void testModifyPCPointInstance() {
    runModifyIPCPointInstance(PCPoint.class);
  }

  /** Test method running a PCPoint query with a query parameter of type Point. */
  public void testPCPointQueryWithPointParam() throws Exception {
    runQueryWithIntParameter(PCPoint.class);
  }

  /** Test method running a PCPoint query with a query parameter of type String. */
  public void testPCPointQueryWithStringParam() throws Exception {
    runQueryWithStringParameter(PCPoint.class);
  }

  /** Test method creating and storing a PCPointAnnotated instance. */
  public void testStorePCPointAnnotatedInstance() {
    runStoreIPCPointInstance(PCPointAnnotated.class);
  }

  /** Test method reading a PCPointAnnotated instance from the datastore. */
  public void testReadPCPointAnnotatedInstance() {
    runReadIPCPointInstance(PCPointAnnotated.class);
  }

  /** Test method modifying a PCPointAnnotated instance and storing in the datastore. */
  public void testModifyPCPointAnnotatedInstance() {
    runModifyIPCPointInstance(PCPointAnnotated.class);
  }

  /** Test method running a PCPointAnnotated query with a query parameter of type String. */
  public void testPCPointAnnotatedQueryWithPointParam() throws Exception {
    runQueryWithIntParameter(PCPointAnnotated.class);
  }

  /** Test method running a PCPointAnnotated query with a query parameter of type Point. */
  public void testPCPointAnnotatedQueryWithStringParam() throws Exception {
    runQueryWithStringParameter(PCPointAnnotated.class);
  }

  /** Test method creating and storing a PCPoint instance. */
  public void testStorePCPointPropInstance() {
    runStoreIPCPointInstance(PCPointProp.class);
  }

  /** Test method reading a PCPoint instance from the datastore. */
  public void testReadPCPointPropInstance() {
    runReadIPCPointInstance(PCPointProp.class);
  }

  /** Test method modifying a PCPoint instance and storing in the datastore. */
  public void testModifyPCPointPropInstance() {
    runModifyIPCPointInstance(PCPointProp.class);
  }

  /** Test method running a PCPoint query with a query parameter of type Point. */
  public void testPCPointPropQueryWithPointParam() throws Exception {
    runQueryWithIntParameter(PCPointProp.class);
  }

  /** Test method running a PCPoint query with a query parameter of type String. */
  public void testPCPointPropQueryWithStringParam() throws Exception {
    runQueryWithStringParameter(PCPointProp.class);
  }

  /** Test method creating and storing a PCPointAnnotated instance. */
  public void testStorePCPointPropAnnotatedInstance() {
    runStoreIPCPointInstance(PCPointPropAnnotated.class);
  }

  /** Test method reading a PCPointAnnotated instance from the datastore. */
  public void testReadPCPointPropAnnotatedInstance() {
    runReadIPCPointInstance(PCPointPropAnnotated.class);
  }

  /** Test method modifying a PCPointAnnotated instance and storing in the datastore. */
  public void testModifyPCPointPropAnnotatedInstance() {
    runModifyIPCPointInstance(PCPointPropAnnotated.class);
  }

  /** Test method running a PCPointAnnotated query with a query parameter of type String. */
  public void testPCPointPropAnnotatedQueryWithPointParam() throws Exception {
    runQueryWithIntParameter(PCPointPropAnnotated.class);
  }

  /** Test method running a PCPointAnnotated query with a query parameter of type Point. */
  public void testPCPointPropAnnotatedQueryWithStringParam() throws Exception {
    runQueryWithStringParameter(PCPointPropAnnotated.class);
  }

  // Helper methods

  /**
   * Helper method creating a IPCPoint instance. It should call AttributeConverter method
   * convertToDatastore.
   */
  private <T extends IPCPoint> void runStoreIPCPointInstance(Class<T> pcPointClass) {
    int nrOfDbCalls = IntegerToStringConverter.getNrOfConvertToDatastoreCalls();
    int nrOfAttrCalls = IntegerToStringConverter.getNrOfConvertToAttributeCalls();

    // Create a persistent IPCPoint instance and store its oid
    // AttributeConverter method convertToDatastore is called when persisting instance
    createIPCPointInstances(pcPointClass, 1);

    // convertToDatastore should be called twice
    assertEquals(2, IntegerToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
    // convertToAttribute should not be called
    assertEquals(0, IntegerToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);
  }

  /**
   * Helper method reading a IPCPoint instance from the datastore. It should call AttributeConverter
   * method convertToAttribute.
   */
  private <T extends IPCPoint> void runReadIPCPointInstance(Class<T> pcPointClass) {
    IPCPoint point;
    Object oid;
    int nrOfDbCalls;
    int nrOfAttrCalls;

    // Create a persistent IPCPoint instance and store its oid
    oid = createIPCPointInstances(pcPointClass, 1);

    // Cleanup the 2nd-level cache and close the pm to make sure PCPoint instances are not cached
    pm.getPersistenceManagerFactory().getDataStoreCache().evictAll(false, pcPointClass);
    pm.close();
    pm = null;

    nrOfDbCalls = IntegerToStringConverter.getNrOfConvertToDatastoreCalls();
    nrOfAttrCalls = IntegerToStringConverter.getNrOfConvertToAttributeCalls();
    pm = getPM();
    pm.currentTransaction().begin();
    // Read the IPCPoint instance from the datastore, this should call convertToAttribute
    point = (IPCPoint) pm.getObjectById(oid);
    int x = point.getX();
    Integer y = point.getY();
    pm.currentTransaction().commit();

    // convertToDatastore should not be called
    assertEquals(0, IntegerToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
    // convertToAttribute should be called twice
    assertEquals(2, IntegerToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);
    // Check the values of the associated Point instances
    assertEquals(MIN_X, x);
    assertEquals(MIN_Y, y == null ? 0 : y.intValue());
  }

  /**
   * Helper method modifying a IPCPoint instance. It should call AttributeConverter method
   * convertToDatastore.
   */
  private <T extends IPCPoint> void runModifyIPCPointInstance(Class<T> pcPointClass) {
    Transaction tx;
    IPCPoint point;
    Object oid;
    int nrOfDbCalls;
    int nrOfAttrCalls;

    // Create a persistent IPCPoint instance and store its oid
    oid = createIPCPointInstances(pcPointClass, 1);

    // Cleanup the 2nd-level cache and close the pm to make sure PCPoint instances are not cached
    pm.getPersistenceManagerFactory().getDataStoreCache().evictAll(false, pcPointClass);
    pm.close();
    pm = null;

    nrOfDbCalls = IntegerToStringConverter.getNrOfConvertToDatastoreCalls();
    nrOfAttrCalls = IntegerToStringConverter.getNrOfConvertToAttributeCalls();
    pm = getPM();
    tx = pm.currentTransaction();
    tx.begin();
    // Load the PCPoint instance, this should call convertToAttribute
    point = (IPCPoint) pm.getObjectById(oid);
    // Update IPCPoint instance, this should call convertToDatastore
    point.setX(MIN_X + 1);
    point.setY(Integer.valueOf(MIN_Y + 1));
    // IPCPoint instance should be dirty
    assertTrue(JDOHelper.isDirty(point));
    tx.commit();

    // convertToDatastore should be called twice
    assertEquals(2, IntegerToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
    // convertToAttribute should be called twice
    assertEquals(2, IntegerToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);
  }

  /**
   * Helper method running a query with an int parameter. The parameter value is converted using the
   * AttributeConverter.
   *
   * @throws Exception
   */
  private <T extends IPCPoint> void runQueryWithIntParameter(Class<T> pcPointClass)
      throws Exception {
    int nrOfDbCalls;
    int nrOfAttrCalls;

    nrOfDbCalls = IntegerToStringConverter.getNrOfConvertToDatastoreCalls();
    nrOfAttrCalls = IntegerToStringConverter.getNrOfConvertToAttributeCalls();
    createIPCPointInstances(pcPointClass, 5);
    // convertToDatastore should be called twice per instance = 10 times
    assertEquals(10, IntegerToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
    // convertToAttribute should not be called
    assertEquals(0, IntegerToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);

    // Cleanup the 2nd-level cache and close the pm to make sure PCPoint instances are not cached
    pm.getPersistenceManagerFactory().getDataStoreCache().evictAll(false, pcPointClass);
    pm.close();
    pm = null;

    nrOfDbCalls = IntegerToStringConverter.getNrOfConvertToDatastoreCalls();
    nrOfAttrCalls = IntegerToStringConverter.getNrOfConvertToAttributeCalls();
    pm = getPM();
    pm.currentTransaction().begin();
    try (Query<T> q = pm.newQuery(pcPointClass, "this.x == :param")) {
      q.setParameters(MIN_X + 1);
      // AttributeConverter method convertToAttribute is called when loading instance from the
      // datastore
      List<T> res = q.executeList();
      assertEquals(1, res.size());
      IPCPoint point = res.get(0);

      // Check the coordinates of the associated Point instances
      assertEquals(MIN_X + 1, point.getX());
      assertEquals(MIN_Y + 1, point.getY() == null ? 0 : point.getY().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    } finally {
      pm.currentTransaction().commit();
    }

    // convertToDatastore should be called to handle the query parameter
    assertTrue(IntegerToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls >= 1);
    // convertToAttribute should be called at least twice
    assertTrue(IntegerToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls >= 2);
  }

  /**
   * Helper method running a query with a Point parameter. The string parameter is compared to the
   * converted int field.
   *
   * @throws Exception
   */
  private <T extends IPCPoint> void runQueryWithStringParameter(Class<T> pcPointClass)
      throws Exception {
    int nrOfDbCalls;
    int nrOfAttrCalls;

    nrOfDbCalls = IntegerToStringConverter.getNrOfConvertToDatastoreCalls();
    nrOfAttrCalls = IntegerToStringConverter.getNrOfConvertToAttributeCalls();
    createIPCPointInstances(pcPointClass, 5);
    // convertToDatastore should be called twice per instance = 10 times
    assertEquals(10, IntegerToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
    // convertToAttribute should not be called
    assertEquals(0, IntegerToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);

    // Cleanup the 2nd-level cache and close the pm to make sure PCPoint instances are not cached
    pm.getPersistenceManagerFactory().getDataStoreCache().evictAll(false, pcPointClass);
    pm.close();
    pm = null;

    nrOfDbCalls = IntegerToStringConverter.getNrOfConvertToDatastoreCalls();
    nrOfAttrCalls = IntegerToStringConverter.getNrOfConvertToAttributeCalls();
    pm = getPM();
    pm.currentTransaction().begin();
    try (Query<T> q = pm.newQuery(pcPointClass, "this.x == param")) {
      q.declareParameters("String param");
      q.setParameters("3");
      // AttributeConverter method convertToAttribute is called when loading instance from the
      // datastore
      List<T> res = q.executeList();
      assertEquals(1, res.size());
      IPCPoint point = res.get(0);

      assertEquals(MIN_X + 2, point.getX());
      assertEquals(MIN_Y + 2, point.getY() == null ? 0 : point.getY().intValue());
    } finally {
      pm.currentTransaction().commit();
    }

    // convertToDatastore should not be called
    assertTrue(IntegerToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls == 0);
    // convertToAttribute should be called at least twice
    assertTrue(IntegerToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls >= 2);
  }

  /**
   * Helper method to create IPCPoint instances.
   *
   * @param pcPointClass class instance of the IPCPoint implementation class to be created
   * @param nrOfObjects number of IPCPoint instances to be created
   * @return ObjectId of the first IPCPoint instance
   */
  private <T extends IPCPoint> Object createIPCPointInstances(
      Class<T> pcPointClass, int nrOfObjects) {
    IPCPoint point;
    Object oid = null;

    if (nrOfObjects < 1) {
      return null;
    }

    pm = getPM();
    try {
      pm.currentTransaction().begin();
      point = pcPointClass.getConstructor().newInstance();
      point.setX(MIN_X);
      point.setY(Integer.valueOf(MIN_Y));
      pm.makePersistent(point);
      oid = pm.getObjectId(point);
      for (int i = 1; i < nrOfObjects; i++) {
        point = pcPointClass.getConstructor().newInstance();
        point.setX(MIN_X + i);
        point.setY(Integer.valueOf(MIN_Y + i));
        pm.makePersistent(point);
      }
      pm.currentTransaction().commit();
    } catch (NoSuchMethodException
        | SecurityException
        | InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException ex) {
      fail("Error creating IPCPoint instance: " + ex.getMessage());
    } finally {
      if (pm.currentTransaction().isActive()) {
        pm.currentTransaction().rollback();
      }
    }
    return oid;
  }
}
