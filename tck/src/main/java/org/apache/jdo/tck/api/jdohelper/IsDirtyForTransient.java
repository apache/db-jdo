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

package org.apache.jdo.tck.api.jdohelper;

import javax.jdo.JDOHelper;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Is Dirty For Transient <br>
 * <B>Keywords:</B> jdohelper <br>
 * <B>Assertion ID:</B> A8.4.1-3 <br>
 * <B>Assertion Description: </B> The jdohelper.isDirty method returns false If the instance is
 * transient, evaluating to true when == is used.
 */

/*
 * Revision History
 * ================
 * Author         :   	Date   : 	Version
 * Azita Kamangar  	9/28/01		 1.0
 */
public class IsDirtyForTransient extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A8.4.1-3 (IsDirtyForTransient) failed: ";

  /* test JDOHelper.isDeleted(Object pc)
   *
   */
  @Test
  public void testIsDirtyForTransient() {
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    tx.begin();
    PCPoint p1 = new PCPoint(1, 3);
    boolean dirty = JDOHelper.isDirty(p1);
    tx.commit();
    if (dirty) fail(ASSERTION_FAILED, "JDOHelper.isDirty returns true for transient instance");
    pm.close();
    pm = null;
  }
}
