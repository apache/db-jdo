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

import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.api.persistencemanager.PersistenceManagerTest;
import org.apache.jdo.tck.pc.mylib.Point;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> GetObjectIdForNullOrNotPersistent <br>
 * <B>Keywords:</B> identity <br>
 * <B>Assertion IDs:</B> A12.5.6-14. <br>
 * <B>Assertion Description: </B> If a call is made to PersistenceManager.getObjectId and the
 * parameter pc is not persistent, or is null, then null is returned.
 */
public class GetObjectIdForNullOrNotPersistent extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A12.5.6-14 (GetObjectIdForNullOrNotPersistent) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetObjectIdForNullOrNotPersistent.class);
  }

  /** */
  public void testGetObjectIdForNullOrNotPersistent() {
    pm = getPM();
    runTestNonPcClass(pm);
    runTestNullParam(pm);
    pm.close();
    pm = null;
  }

  /** */
  private void runTestNonPcClass(PersistenceManager pm) {
    Point p = new Point(1, 3);
    Object oid = pm.getObjectId(p);
    if (oid != null) {
      fail(
          ASSERTION_FAILED,
          "pm.getObjectId returned non null ObjectId: " + oid + " for instance of non-pc class");
    }
  }

  /** */
  private void runTestNullParam(PersistenceManager pm) {
    Object oid = pm.getObjectId(null);
    if (oid != null) {
      fail(ASSERTION_FAILED, "pm.getObjectId(null) returned non null ObjectId: " + oid);
    }
  }
}
