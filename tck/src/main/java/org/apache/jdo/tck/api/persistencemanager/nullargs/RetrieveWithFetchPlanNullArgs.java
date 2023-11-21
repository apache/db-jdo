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

package org.apache.jdo.tck.api.persistencemanager.nullargs;

import java.util.Collection;
import javax.jdo.PersistenceManager;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> retrieveWithFetchPlan with Null Arguments <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A12.6-3, A12.6-4, A12.6-5 <br>
 * <B>Assertion Description: </B> A12.6-3 [Null arguments to APIs that take an Object parameter
 * cause the API to have no effect.] A12.6-4 [Null arguments to APIs that take Object[] or
 * Collection will cause the API to throw NullPointerException.] A12.6-5 [Non-null Object[] or
 * Collection arguments that contain null elements will have the documented behavior for non-null
 * elements, and the null elements will be ignored.]
 */
public class RetrieveWithFetchPlanNullArgs extends PersistenceManagerNullsTest {

  static final MethodUnderTest retrieveWithFetchPlan = new MethodUnderTestRetrieveWithFetchPlan();

  static class MethodUnderTestRetrieveWithFetchPlan extends MethodUnderTest {
    @Override
    public void pmApi(PersistenceManager pm, Object pc) {
      pm.retrieve(pc, false);
    }

    @Override
    public <T> void pmApi(PersistenceManager pm, Collection<T> pcs) {
      pm.retrieveAll(pcs, false);
    }

    @Override
    public void pmApi(PersistenceManager pm, Object[] pcs) {
      pm.retrieveAll(false, pcs);
    }
  }

  /** Test that retrieveWithFetchPlan() with null valued argument does nothing. */
  @Test
  public void testRetrieveWithFetchPlanNullObject() {
    executeNullObjectParameter(retrieveWithFetchPlan, "retrieveWithFetchPlan(null)");
  }

  /**
   * Test that retrieveWithFetchPlanAll() with null valued Collection argument throws
   * NullPointerException.
   */
  @Test
  public void testRetrieveWithFetchPlanNullCollection() {
    executeNullCollectionParameter(
        retrieveWithFetchPlan, "retrieveWithFetchPlanAll((Collection)null)");
  }

  /**
   * Test that retrieveWithFetchPlanAll() with null valued array argument throws
   * NullPointerException.
   */
  @Test
  public void testRetrieveWithFetchPlanNullArray() {
    executeNullArrayParameter(retrieveWithFetchPlan, "retrieveWithFetchPlanAll((Object[])null)");
  }

  /**
   * Test that retrieveWithFetchPlanAll() with a null element of a Collection argument throws
   * NullPointerException.
   */
  @Test
  public void testRetrieveWithFetchPlanCollectionNullElement() {
    executeCollectionNullElement(
        collNullElem, retrieveWithFetchPlan, "retrieveWithFetchPlanAll(Collection)");
  }

  /**
   * Test that retrieveWithFetchPlanAll() with a null element of a array argument throws
   * NullPointerException.
   */
  @Test
  public void testRetrieveWithFetchPlanArrayNullElement() {
    executeArrayNullElement(
        arrayNullElem, retrieveWithFetchPlan, "retrieveWithFetchPlanAll(Object[])");
  }
}
