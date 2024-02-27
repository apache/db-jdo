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
 * <B>Title:</B> Is Deleted <br>
 * <B>Keywords:</B> jdohelper <br>
 * <B>Assertion ID:</B> A8.4.5-1 <br>
 * <B>Assertion Description: </B> The jdohelper.isDeleted method returns true if the instance of
 * persistence capable have been deleted in the current transaction evaluating to true when == is
 * used.
 */
/*
 * Revision History
 * ================
 * Author         :   	Date   : 	Version
 * Azita Kamangar  	9/26/01		 1.0
 */

public class IsDeleted extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED = "Assertion A8.4.5-1 (IsDeleted) failed: ";

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PCPoint.class);
  }

  /* test jdohelper.isDeleted(Object pc)
   *
   */
  @Test
  public void testIsDeleted() {
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    tx.begin();
    PCPoint p1 = new PCPoint(1, 3);
    pm.makePersistent(p1);
    pm.deletePersistent(p1);
    boolean deleted = JDOHelper.isDeleted(p1);
    tx.commit();
    if (!deleted) fail(ASSERTION_FAILED, "JDOHelper.isDeleted returns false for deleted instance");
    pm.close();
    pm = null;
  }
}
