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

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.jdo.JDOHelper;
import javax.jdo.Query;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.converter.IPCRect;
import org.apache.jdo.tck.pc.converter.PCRect;
import org.apache.jdo.tck.pc.converter.PCRectAnnotated;
import org.apache.jdo.tck.pc.mylib.Point;
import org.apache.jdo.tck.util.BatchTestRunner;
import org.apache.jdo.tck.util.PointToStringConverter;

/**
 * <B>Title:</B>PointAttributeConverterTest <br>
 * <B>Keywords:</B> mapping <br>
 * <B>Assertion ID:</B> [not identified] <br>
 * <B>Assertion Description: </B> A IPCRect instance refers two Point instances, that are stored as
 * strings in the datastore. A Point instance is converted using an AttributeConverter.
 */
public class PointAttributeConverterTest extends JDO_Test {

  private static final int UL_X = 1;
  private static final int UL_Y = 10;
  private static final int LR_X = 10;
  private static final int LR_Y = 1;

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(PointAttributeConverterTest.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PCRect.class);
    addTearDownClass(PCRectAnnotated.class);
  }

  /** Test method creating and storing a PCRectString instance. */
  public void testStorePCRectStringInstance() {
    runStoreIPCRectInstance(PCRect.class);
  }

  /** Test method reading a PCRectString instance from the datastore. */
  public void testReadPCRectStringInstance() {
    runReadIPCRectInstance(PCRect.class);
  }

  /** Test method modifying a PCRectString instance and storing in the datastore. */
  public void testModifyPCRectStringInstance() {
    runModifyIPCRectInstance(PCRect.class);
  }

  /** Test method running a PCRectString query with a query parameter of type Point. */
  public void testPCRectStringQueryWithPointParam() throws Exception {
    runQueryWithPointParameter(PCRect.class);
  }

  /** Test method running a PCRectString query with a query parameter of type String. */
  public void testPCRectStringQueryWithStringParam() throws Exception {
    runQueryWithStringParameter(PCRect.class);
  }

  /** Test method creating and storing a PCRectStringAnnotated instance. */
  public void testStorePCRectStringAnnotatedInstance() {
    runStoreIPCRectInstance(PCRectAnnotated.class);
  }

  /** Test method reading a PCRectStringAnnotated instance from the datastore. */
  public void testReadPCRectStringAnnotatedInstance() {
    runReadIPCRectInstance(PCRectAnnotated.class);
  }

  /** Test method modifying a PCRectStringAnnotated instance and storing in the datastore. */
  public void testModifyPCRectStringAnnotatedInstance() {
    runModifyIPCRectInstance(PCRectAnnotated.class);
  }

  /** Test method running a PCRectStringAnnotated query with a query parameter of type String. */
  public void testPCRectStringAnnotatedQueryWithPointParam() throws Exception {
    runQueryWithPointParameter(PCRectAnnotated.class);
  }

  /** Test method running a PCRectStringAnnotated query with a query parameter of type Point. */
  public void testPCRectStringAnnotatedQueryWithStringParam() throws Exception {
    runQueryWithStringParameter(PCRectAnnotated.class);
  }

  // Helper methods

  /**
   * Helper method creating a IPCRect instance. It should call AttributeConverter method
   * convertToDatastore.
   */
  private <T extends IPCRect> void runStoreIPCRectInstance(Class<T> pcrectClass) {
    int nrOfDbCalls = PointToStringConverter.getNrOfConvertToDatastoreCalls();
    int nrOfAttrCalls = PointToStringConverter.getNrOfConvertToAttributeCalls();

    // Create a persistent IPCRect instance and store its oid
    // AttributeConverter method convertToDatastore is called when persisting instance
    createIPCRectInstances(pcrectClass, 1);

    // convertToDatastore should be called twice
    assertEquals(2, PointToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
    // convertToAttribute should not be called
    assertEquals(0, PointToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);
  }

  /**
   * Helper method reading a IPCRect instance from the datastore. It should call AttributeConverter
   * method convertToAttribute.
   */
  private <T extends IPCRect> void runReadIPCRectInstance(Class<T> pcrectClass) {
    IPCRect rect;
    Object oid;
    int nrOfDbCalls;
    int nrOfAttrCalls;

    // Create a persistent IPCRect instance and store its oid
    oid = createIPCRectInstances(pcrectClass, 1);

    // Cleanup the 2nd-level cache and close the pm to make sure PCRect instances are not cached
    pm.getPersistenceManagerFactory().getDataStoreCache().evictAll(false, pcrectClass);
    pm.close();
    pm = null;

    nrOfDbCalls = PointToStringConverter.getNrOfConvertToDatastoreCalls();
    nrOfAttrCalls = PointToStringConverter.getNrOfConvertToAttributeCalls();
    pm = getPM();
    pm.currentTransaction().begin();
    // Read the IPCRect instance from the datastore, this should call convertToAttribute
    rect = (IPCRect) pm.getObjectById(oid);
    Point ul = rect.getUpperLeft();
    Point lr = rect.getLowerRight();
    pm.currentTransaction().commit();

    // convertToDatastore should not be called
    assertEquals(0, PointToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
    // convertToAttribute should be called twice
    assertEquals(2, PointToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);
    // Check the values of the associated Point instances
    assertEquals(UL_X, ul.getX());
    assertEquals(UL_Y, ul.getY() == null ? 0 : ul.getY().intValue());
    assertEquals(LR_X, lr.getX());
    assertEquals(LR_Y, lr.getY() == null ? 0 : lr.getY().intValue());
  }

  /**
   * Helper method modifying a IPCRect instance. It should call AttributeConverter method
   * convertToDatastore.
   */
  private <T extends IPCRect> void runModifyIPCRectInstance(Class<T> pcrectClass) {
    Transaction tx;
    IPCRect rect;
    Object oid;
    int nrOfDbCalls;
    int nrOfAttrCalls;

    // Create a persistent IPCRect instance and store its oid
    oid = createIPCRectInstances(pcrectClass, 1);

    // Cleanup the 2nd-level cache and close the pm to make sure PCRect instances are not cached
    pm.getPersistenceManagerFactory().getDataStoreCache().evictAll(false, pcrectClass);
    pm.close();
    pm = null;

    nrOfDbCalls = PointToStringConverter.getNrOfConvertToDatastoreCalls();
    nrOfAttrCalls = PointToStringConverter.getNrOfConvertToAttributeCalls();
    pm = getPM();
    tx = pm.currentTransaction();
    tx.begin();
    rect = (IPCRect) pm.getObjectById(oid);
    // should trigger convertToAttribute
    rect.getLowerRight();
    rect.getUpperLeft();
    // Update IPCRect instance, this should call convertToDatastore
    rect.setUpperLeft(new Point(UL_X + 1, UL_Y + 1));
    rect.setLowerRight(new Point(LR_X + 1, LR_Y + 1));
    // IPCRect instance should be dirty
    assertTrue(JDOHelper.isDirty(rect));
    tx.commit();

    // convertToDatastore should be called twice
    assertEquals(2, PointToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
    // convertToAttribute should be called twice
    assertEquals(2, PointToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);
  }

  /**
   * Helper method running a query with a Point parameter. The parameter value is converted using
   * the AttributeConverter.
   *
   * @throws Exception
   */
  private <T extends IPCRect> void runQueryWithPointParameter(Class<T> pcrectClass)
      throws Exception {
    int nrOfDbCalls;
    int nrOfAttrCalls;

    nrOfDbCalls = PointToStringConverter.getNrOfConvertToDatastoreCalls();
    nrOfAttrCalls = PointToStringConverter.getNrOfConvertToAttributeCalls();
    createIPCRectInstances(pcrectClass, 5);
    // convertToDatastore should be called twice per instance = 10 times
    assertEquals(10, PointToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
    // convertToAttribute should not be called
    assertEquals(0, PointToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);

    // Cleanup the 2nd-level cache and close the pm to make sure PCRect instances are not cached
    pm.getPersistenceManagerFactory().getDataStoreCache().evictAll(false, pcrectClass);
    pm.close();
    pm = null;

    nrOfDbCalls = PointToStringConverter.getNrOfConvertToDatastoreCalls();
    nrOfAttrCalls = PointToStringConverter.getNrOfConvertToAttributeCalls();
    pm = getPM();
    pm.currentTransaction().begin();
    try (Query<T> q = pm.newQuery(pcrectClass, "this.upperLeft == :point")) {
      q.setParameters(new Point(UL_X + 1, UL_Y + 1));
      // AttributeConverter method convertToAttribute is called when loading instance from the
      // datastore
      List<T> res = q.executeList();
      assertEquals(1, res.size());
      IPCRect rect = res.get(0);
      Point ul = rect.getUpperLeft();
      Point lr = rect.getLowerRight();

      // Check the coordinates of the associated Point instances
      assertEquals(UL_X + 1, ul.getX());
      assertEquals(UL_Y + 1, ul.getY() == null ? 0 : ul.getY().intValue());
      assertEquals(LR_X + 1, lr.getX());
      assertEquals(LR_Y + 1, lr.getY() == null ? 0 : lr.getY().intValue());
    } catch (Exception e) {
      fail(e.getMessage());
    } finally {
      pm.currentTransaction().commit();
    }

    // convertToDatastore should be called to handle the query parameter
    assertTrue(PointToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls >= 1);
    // convertToAttribute should be called at least twice
    assertTrue(PointToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls >= 2);
  }

  /**
   * Helper method running a query with a Point parameter. The string parameter is compared to the
   * converted Point field.
   *
   * @throws Exception
   */
  private <T extends IPCRect> void runQueryWithStringParameter(Class<T> pcrectClass)
      throws Exception {
    int nrOfDbCalls;
    int nrOfAttrCalls;

    nrOfDbCalls = PointToStringConverter.getNrOfConvertToDatastoreCalls();
    nrOfAttrCalls = PointToStringConverter.getNrOfConvertToAttributeCalls();
    createIPCRectInstances(pcrectClass, 5);
    // convertToDatastore should be called twice per instance = 10 times
    assertEquals(10, PointToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls);
    // convertToAttribute should not be called
    assertEquals(0, PointToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls);

    // Cleanup the 2nd-level cache and close the pm to make sure PCRect instances are not cached
    pm.getPersistenceManagerFactory().getDataStoreCache().evictAll(false, pcrectClass);
    pm.close();
    pm = null;

    nrOfDbCalls = PointToStringConverter.getNrOfConvertToDatastoreCalls();
    nrOfAttrCalls = PointToStringConverter.getNrOfConvertToAttributeCalls();
    pm = getPM();
    pm.currentTransaction().begin();
    try (Query<T> q = pm.newQuery(pcrectClass, "this.upperLeft == str")) {
      q.declareParameters("String str");
      q.setParameters("3:12");
      // AttributeConverter method convertToAttribute is called when loading instance from the
      // datastore
      List<T> res = q.executeList();
      assertEquals(1, res.size());
      IPCRect rect = res.get(0);
      Point ul = rect.getUpperLeft();
      Point lr = rect.getLowerRight();

      // Check the coordinates of the associated Point instances
      assertEquals(UL_X + 2, ul.getX());
      assertEquals(UL_Y + 2, ul.getY() == null ? 0 : ul.getY().intValue());
      assertEquals(LR_X + 2, lr.getX());
      assertEquals(LR_Y + 2, lr.getY() == null ? 0 : lr.getY().intValue());
    } finally {
      pm.currentTransaction().commit();
    }

    // convertToDatastore should not be called
    assertTrue(PointToStringConverter.getNrOfConvertToDatastoreCalls() - nrOfDbCalls == 0);
    // convertToAttribute should be called at least twice
    assertTrue(PointToStringConverter.getNrOfConvertToAttributeCalls() - nrOfAttrCalls >= 2);
  }

  /**
   * Helper method to create IPCRect instances.
   *
   * @param pcrectClass class instance of the IPCRect implementation class to be created
   * @param nrOfObjects number of IPCRect instances to be created
   * @return ObjectId of the first IPCRect instance
   */
  private <T extends IPCRect> Object createIPCRectInstances(Class<T> pcrectClass, int nrOfObjects) {
    IPCRect rect;
    Object oid = null;

    if (nrOfObjects < 1) {
      return null;
    }

    pm = getPM();
    try {
      pm.currentTransaction().begin();
      rect = pcrectClass.getConstructor().newInstance();
      rect.setUpperLeft(new Point(UL_X, UL_Y));
      rect.setLowerRight(new Point(LR_X, LR_Y));
      pm.makePersistent(rect);
      oid = pm.getObjectId(rect);
      for (int i = 1; i < nrOfObjects; i++) {
        rect = pcrectClass.getConstructor().newInstance();
        rect.setUpperLeft(new Point(UL_X + i, UL_Y + i));
        rect.setLowerRight(new Point(LR_X + i, LR_Y + i));
        pm.makePersistent(rect);
      }
      pm.currentTransaction().commit();
    } catch (NoSuchMethodException
        | SecurityException
        | InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException ex) {
      fail("Error creating IPCRect instance: " + ex.getMessage());
    } finally {
      if (pm.currentTransaction().isActive()) {
        pm.currentTransaction().rollback();
      }
    }
    return oid;
  }
}
