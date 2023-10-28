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
 * <B>Title:</B> makeTransactional with Null Arguments <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A12.6-3, A12.6-4, A12.6-5 <br>
 * <B>Assertion Description: </B> A12.6-3 [Null arguments to APIs that take an Object parameter
 * cause the API to have no effect.] A12.6-4 [Null arguments to APIs that take Object[] or
 * Collection will cause the API to throw NullPointerException.] A12.6-5 [Non-null Object[] or
 * Collection arguments that contain null elements will have the documented behavior for non-null
 * elements, and the null elements will be ignored.]
 */
public class MakeTransactionalNullArgs extends PersistenceManagerNullsTest {

  static final MethodUnderTest makeTransactional = new MethodUnderTestMakeTransactional();

  static class MethodUnderTestMakeTransactional extends MethodUnderTest {
    @Override
    public void pmApi(PersistenceManager pm, Object pc) {
      pm.makeTransactional(pc);
    }

    @Override
    public <T> void pmApi(PersistenceManager pm, Collection<T> pcs) {
      pm.makeTransactionalAll(pcs);
    }

    @Override
    public void pmApi(PersistenceManager pm, Object[] pcs) {
      pm.makeTransactionalAll(pcs);
    }
  }

  /** Test that makeTransactional() with null valued argument does nothing. */
  @Test
  public void testMakeTransactionalNullObject() {
    executeNullObjectParameter(makeTransactional, "makeTransactional(null)");
  }

  /**
   * Test that makeTransactionalAll() with null valued Collection argument throws
   * NullPointerException.
   */
  @Test
  public void testMakeTransactionalNullCollection() {
    executeNullCollectionParameter(makeTransactional, "makeTransactionalAll((Collection)null)");
  }

  /**
   * Test that makeTransactionalAll() with null valued array argument throws NullPointerException.
   */
  @Test
  public void testMakeTransactionalNullArray() {
    executeNullArrayParameter(makeTransactional, "makeTransactionalAll((Object[])null)");
  }

  /**
   * Test that makeTransactionalAll() with a null element of a Collection argument throws
   * NullPointerException.
   */
  @Test
  public void testMakeTransactionalCollectionNullElement() {
    executeCollectionNullElement(
        collNullElem, makeTransactional, "makeTransactionalAll(Collection)");
  }

  /**
   * Test that makeTransactionalAll() with a null element of a array argument throws
   * NullPointerException.
   */
  @Test
  public void testMakeTransactionalArrayNullElement() {
    executeArrayNullElement(arrayNullElem, makeTransactional, "makeTransactionalAll(Object[])");
  }
}
