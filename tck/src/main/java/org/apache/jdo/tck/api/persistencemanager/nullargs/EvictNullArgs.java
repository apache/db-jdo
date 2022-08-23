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
 * <B>Title:</B> evict with Null Arguments <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion IDs:</B> A12.6-3, A12.6-4, A12.6-5 <br>
 * <B>Assertion Description: </B> A12.6-3 [Null arguments to APIs that take an Object parameter
 * cause the API to have no effect.] A12.6-4 [Null arguments to APIs that take Object[] or
 * Collection will cause the API to throw NullPointerException.] A12.6-5 [Non-null Object[] or
 * Collection arguments that contain null elements will have the documented behavior for non-null
 * elements, and the null elements will be ignored.]
 */
public class EvictNullArgs extends PersistenceManagerNullsTest {

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(EvictNullArgs.class);
  }

  static MethodUnderTest evict = new MethodUnderTestEvict();

  static class MethodUnderTestEvict extends MethodUnderTest {
    public void pmApi(PersistenceManager pm, Object pc) {
      pm.evict(pc);
    }

    public void pmApi(PersistenceManager pm, Collection pcs) {
      pm.evictAll(pcs);
    }

    public void pmApi(PersistenceManager pm, Object[] pcs) {
      pm.evictAll(pcs);
    }
  }
  ;

  /** Test that evict() with null valued argument does nothing. */
  public void testEvictNullObject() {
    executeNullObjectParameter(evict, "evict(null)");
  }

  /** Test that evictAll() with null valued Collection argument throws NullPointerException. */
  public void testEvictNullCollection() {
    executeNullCollectionParameter(evict, "evictAll((Collection)null)");
  }

  /** Test that evictAll() with null valued array argument throws NullPointerException. */
  public void testEvictNullArray() {
    executeNullArrayParameter(evict, "evictAll((Object[])null)");
  }

  /**
   * Test that evictAll() with a null element of a Collection argument throws NullPointerException.
   */
  public void testEvictCollectionNullElement() {
    executeCollectionNullElement(collNullElem, evict, "evictAll(Collection)");
  }

  /** Test that evictAll() with a null element of a array argument throws NullPointerException. */
  public void testEvictArrayNullElement() {
    executeArrayNullElement(arrayNullElem, evict, "evictAll(Object[])");
  }
}
