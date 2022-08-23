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

package org.apache.jdo.tck.api.persistencemanager.getobject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test GetObjectsById <br>
 * <B>Keywords:</B> getObjectsById getObjectById <br>
 * <B>Assertion IDs:</B> 12.5.6-17 <br>
 * <B>Assertion Description: </B> 12.5.6-17 [Collection getObjectsById (Collection oids); Object[]
 * getObjectsById (Object[] oids); Collection getObjectsById (Collection oids, boolean validate);
 * Object[] getObjectsById (Object[] oids, boolean validate); The getObjectsById method attempts to
 * find instances in the cache with the specified JDO identities. The elements of the oids parameter
 * object might have been returned by earlier calls to getObjectId or getTransactionalObjectId, or
 * might have been constructed by the application. If a method with no validate parameter is used,
 * the method behaves exactly as the correspond ing method with the validate flag set to true. If
 * the Object[] form of the method is used, the returned objects correspond by position with the
 * object ids in the oids parameter. If the Collection form of the method is used, the iterator over
 * the returned Collection returns instances in the same order as the oids returned by an iterator
 * over the parameter Collection. The cardinality of the return value is the same as the cardinality
 * of the oids parameter. ]
 */
public class GetObjectsById extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED = "Assertion 12.5.6-17 (GetObjectsById) failed: ";

  /** Number of persistent instances */
  private static final int NUMBER_OF_INSTANCES = 50;

  /** Persistent instances */
  private final Collection instanceCollection = new ArrayList(NUMBER_OF_INSTANCES);

  /** Object ids */
  private final Collection oidCollection = new ArrayList(NUMBER_OF_INSTANCES);

  /** Persistent instances */
  private final Object[] instanceArray = new Object[NUMBER_OF_INSTANCES];

  /** Object ids */
  private final Object[] oidArray = new Object[NUMBER_OF_INSTANCES];

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetObjectsById.class);
  }

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PCPoint.class);
    getPM().currentTransaction().begin();
    Object instance;
    Object oid;
    instanceCollection.clear();
    oidCollection.clear();
    for (int i = 0; i < NUMBER_OF_INSTANCES; ++i) {
      instance = new PCPoint(i, 1000 + i);
      pm.makePersistent(instance);
      instanceCollection.add(instance);
      instanceArray[i] = instance;
      oid = pm.getObjectId(instance);
      oidCollection.add(oid);
      oidArray[i] = oid;
    }
    pm.currentTransaction().commit();
  }

  /** */
  public void testCollection() {
    getPM().currentTransaction().begin();
    Collection result = pm.getObjectsById(oidCollection);
    pm.currentTransaction().commit();
    checkResultCollection("after getObjectsById(Collection)", result);
    failOnError();
  }

  /** */
  public void testCollectionTrue() {
    getPM().currentTransaction().begin();
    Collection result = pm.getObjectsById(oidCollection, true);
    pm.currentTransaction().commit();
    checkResultCollection("after getObjectsById(Collection, true)", result);
    failOnError();
  }

  /** */
  public void testCollectionFalse() {
    getPM().currentTransaction().begin();
    Collection result = pm.getObjectsById(oidCollection, false);
    pm.currentTransaction().commit();
    checkResultCollection("after getObjectsById(Collection, false)", result);
    failOnError();
  }

  /** */
  public void testArray() {
    getPM().currentTransaction().begin();
    Object[] result = pm.getObjectsById(oidArray);
    pm.currentTransaction().commit();
    checkResultArray("after getObjectsById(Object[], false)", result);
    failOnError();
  }

  /** */
  public void testArrayTrue() {
    getPM().currentTransaction().begin();
    Object[] result = pm.getObjectsById(true, oidArray);
    pm.currentTransaction().commit();
    checkResultArray("after getObjectsById(Object[], false)", result);
    failOnError();
  }

  /** */
  public void testArrayFalse() {
    getPM().currentTransaction().begin();
    Object[] result = pm.getObjectsById(false, oidArray);
    pm.currentTransaction().commit();
    checkResultArray("after getObjectsById(Object[], false)", result);
    failOnError();
  }

  /** Check the results of getObjectsById */
  private void checkResultCollection(String location, Collection instances) {
    Iterator expected = instanceCollection.iterator();
    Iterator actual = instances.iterator();
    for (int i = 0; i < NUMBER_OF_INSTANCES; ++i) {
      checkIdentity(
          ASSERTION_FAILED + location + ", position " + i, expected.next(), actual.next());
    }
    checkIteratorComplete(ASSERTION_FAILED, actual);
  }

  /** Check the results of getObjectsById */
  private void checkResultArray(String location, Object[] actual) {
    int length = actual.length;
    if (length != NUMBER_OF_INSTANCES) {
      appendMessage(
          location
              + "incorrect length of result. "
              + "expected: "
              + NUMBER_OF_INSTANCES
              + "  actual: "
              + length);
    }
    Object[] expected = instanceArray;
    for (int i = 0; i < NUMBER_OF_INSTANCES; ++i) {
      checkIdentity(ASSERTION_FAILED + location + ", position " + i, expected[i], actual[i]);
    }
  }

  /** Check the identity of the instances */
  private void checkIdentity(String location, Object expected, Object actual) {
    if (expected != actual) {
      appendMessage(
          location + "failed to compare \n" + "expected: " + expected + " actual: " + actual);
    }
  }

  /** Check that the iterator has no more elements */
  private void checkIteratorComplete(String location, Iterator iterator) {
    if (iterator.hasNext()) {
      appendMessage(location + "result iterator has more elements than expected.");
    }
  }
}
