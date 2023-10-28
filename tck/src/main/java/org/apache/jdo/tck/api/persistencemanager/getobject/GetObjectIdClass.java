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

package org.apache.jdo.tck.api.persistencemanager.getobject;

import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Get ObjectId Class <br>
 * <B>Keywords:</B> identity <br>
 * <B>Assertion IDs:</B> A12.10-1 <br>
 * <B>Assertion Description: </B> The method PersistenceManager.getObjectIdClass returns the class
 * of the object id for the given class. If the parameter class is not persistence-capable, or the
 * parameter is null, null is returned. If the object-id class defined in the metadata for the
 * parameter class is abstract then null is returned. If the implementation does not support
 * application identity, and the class is defined in the jdo metadata to use application identity,
 * then null is returned.
 */
public class GetObjectIdClass extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A12.10-1 (GetObjectIdClass) failed: ";

  /** */
  @Test
  public void test() {
    pm = getPM();
    runTestGetObjectIdClassForNull(pm);
    runTestGetObjectIdClassForNonPCClass(pm);
    runTestGetObjectIdClassForAbstractClass(pm);
    runTestGetObjectIdClass(pm);
    pm.close();
    pm = null;
  }

  /** */
  private void runTestGetObjectIdClass(PersistenceManager pm) {
    if (pm.getObjectIdClass(PCPoint.class) == null) {
      fail(ASSERTION_FAILED, "pm.getObjectIdClass(PCPoint.class) returned null");
    }
  }

  /** */
  private void runTestGetObjectIdClassForNull(PersistenceManager pm) {
    Class<?> cl = pm.getObjectIdClass(null);
    if (cl != null) {
      fail(
          ASSERTION_FAILED,
          "pm.getObjectIdClass for null returned non-null class: " + cl.getName());
    }
  }

  /** */
  private void runTestGetObjectIdClassForNonPCClass(PersistenceManager pm) {
    Class<?> cl = pm.getObjectIdClass(java.lang.Throwable.class);
    if (cl != null) {
      fail(
          ASSERTION_FAILED,
          "pm.getObjectIdClass for non-PC class returned non-null class: " + cl.getName());
    }
  }

  private void runTestGetObjectIdClassForAbstractClass(PersistenceManager pm) {
    // not implemented; the tck contains no classes with which to test this.
    // we need an abstract class whose object-id class is itself abstract.
  }
}
