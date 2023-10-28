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
 * <B>Title:</B> deletePersistent with Null Arguments <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A12.6-3, A12.6-4, A12.6-5 <br>
 * <B>Assertion Description: </B> A12.6-3 [Null arguments to APIs that take an Object parameter
 * cause the API to have no effect.] A12.6-4 [Null arguments to APIs that take Object[] or
 * Collection will cause the API to throw NullPointerException.] A12.6-5 [Non-null Object[] or
 * Collection arguments that contain null elements will have the documented behavior for non-null
 * elements, and the null elements will be ignored.]
 */
public class DeletePersistentNullArgs extends PersistenceManagerNullsTest {

  static final MethodUnderTest deletePersistent = new MethodUnderTestDeletePersistent();

  static class MethodUnderTestDeletePersistent extends MethodUnderTest {
    @Override
    public void pmApi(PersistenceManager pm, Object pc) {
      pm.deletePersistent(pc);
    }

    @Override
    public <T> void pmApi(PersistenceManager pm, Collection<T> pcs) {
      pm.deletePersistentAll(pcs);
    }

    @Override
    public void pmApi(PersistenceManager pm, Object[] pcs) {
      pm.deletePersistentAll(pcs);
    }
  }

  /** Test that deletePersistent() with null valued argument does nothing. */
  @Test
  public void testDeletePersistentNullObject() {
    executeNullObjectParameter(deletePersistent, "deletePersistent(null)");
  }

  /**
   * Test that deletePersistentAll() with null valued Collection argument throws
   * NullPointerException.
   */
  @Test
  public void testDeletePersistentNullCollection() {
    executeNullCollectionParameter(deletePersistent, "deletePersistentAll((Collection)null)");
  }

  /**
   * Test that deletePersistentAll() with null valued array argument throws NullPointerException.
   */
  @Test
  public void testDeletePersistentNullArray() {
    executeNullArrayParameter(deletePersistent, "deletePersistentAll((Object[])null)");
  }

  /**
   * Test that deletePersistentAll() with a null element of a Collection argument throws
   * NullPointerException.
   */
  @Test
  public void testDeletePersistentCollectionNullElement() {
    executeCollectionNullElement(collNullElem, deletePersistent, "deletePersistentAll(Collection)");
  }

  /**
   * Test that deletePersistentAll() with a null element of a array argument throws
   * NullPointerException.
   */
  @Test
  public void testDeletePersistentArrayNullElement() {
    executeArrayNullElement(arrayNullElem, deletePersistent, "deletePersistentAll(Object[])");
  }
}
