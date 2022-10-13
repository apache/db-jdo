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

package org.apache.jdo.tck.api.persistencemanager.nullargs;

import java.util.Collection;
import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> retrieve with Null Arguments <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A12.6-3, A12.6-4, A12.6-5 <br>
 * <B>Assertion Description: </B> A12.6-3 [Null arguments to APIs that take an Object parameter
 * cause the API to have no effect.] A12.6-4 [Null arguments to APIs that take Object[] or
 * Collection will cause the API to throw NullPointerException.] A12.6-5 [Non-null Object[] or
 * Collection arguments that contain null elements will have the documented behavior for non-null
 * elements, and the null elements will be ignored.]
 */
public class RetrieveNullArgs extends PersistenceManagerNullsTest {

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(RetrieveNullArgs.class);
  }

  static final MethodUnderTest retrieve = new MethodUnderTestRetrieve();

  static class MethodUnderTestRetrieve extends MethodUnderTest {
    @Override
    public void pmApi(PersistenceManager pm, Object pc) {
      pm.retrieve(pc);
    }

    @Override
    public <T> void pmApi(PersistenceManager pm, Collection<T> pcs) {
      pm.retrieveAll(pcs);
    }

    @Override
    public void pmApi(PersistenceManager pm, Object[] pcs) {
      pm.retrieveAll(pcs);
    }
  }

  /** Test that retrieve() with null valued argument does nothing. */
  public void testRetrieveNullObject() {
    executeNullObjectParameter(retrieve, "retrieve(null)");
  }

  /** Test that retrieveAll() with null valued Collection argument throws NullPointerException. */
  public void testRetrieveNullCollection() {
    executeNullCollectionParameter(retrieve, "retrieveAll((Collection)null)");
  }

  /** Test that retrieveAll() with null valued array argument throws NullPointerException. */
  public void testRetrieveNullArray() {
    executeNullArrayParameter(retrieve, "retrieveAll((Object[])null)");
  }

  /**
   * Test that retrieveAll() with a null element of a Collection argument throws
   * NullPointerException.
   */
  public void testRetrieveCollectionNullElement() {
    executeCollectionNullElement(collNullElem, retrieve, "retrieveAll(Collection)");
  }

  /**
   * Test that retrieveAll() with a null element of a array argument throws NullPointerException.
   */
  public void testRetrieveArrayNullElement() {
    executeArrayNullElement(arrayNullElem, retrieve, "retrieveAll(Object[])");
  }
}
