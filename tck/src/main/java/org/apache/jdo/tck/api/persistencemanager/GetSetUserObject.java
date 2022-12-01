/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.api.persistencemanager;

import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.mylib.Point;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Get/Set User Object <br>
 * <B>Keywords:</B> <br>
 * <B>Assertion ID:</B> A12.8-1. <br>
 * <B>Assertion Description: </B> The PersistenceManager.setUserObject method is used to store an
 * object associated with the PersistenceManager. One uses the method getUserObject to later
 * retrieve the object.
 */
public class GetSetUserObject extends PersistenceManagerTest {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A12.8-1 (GetSetUserObject) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(GetSetUserObject.class);
  }

  /** */
  public void testGetSetUserObject() {
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    try {
      tx.begin();
      Point p = new Point(10, 10);
      pm.setUserObject(p);
      tx.commit();

      tx.begin();
      Object obj = pm.getUserObject();

      if (obj != p) {
        fail(ASSERTION_FAILED, "Unexpected pm user object, expected " + p + ", got " + obj);
      }
      tx.commit();
      tx = null;
    } finally {
      if ((tx != null) && tx.isActive()) tx.rollback();
    }
    pm.close();
    pm = null;
  }
}
