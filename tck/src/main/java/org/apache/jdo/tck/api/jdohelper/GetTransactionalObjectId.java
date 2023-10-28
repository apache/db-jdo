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
 * <B>Title:</B> Get Transactional Object Id <br>
 * <B>Keywords:</B> jdohelper <br>
 * <B>Assertion ID:</B> A8.3-5 <br>
 * <B>Assertion Description: </B> If the object parameter implements PersistenceCapable,
 * JDOHelper.getTransactionalObjectId delegates to the parameter instance and returns the JDO
 * identity of the instance.
 */
/*
 * Revision History
 * ================
 * Author         :   	Date   : 	Version
 * Azita Kamangar  	9/26/01		 1.0
 */

public class GetTransactionalObjectId extends JDO_Test {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A8.3-5 (GetTransactionalObjectId) failed: ";

  /**
   * @see org.apache.jdo.tck.JDO_Test#localSetUp()
   */
  @Override
  protected void localSetUp() {
    addTearDownClass(PCPoint.class);
  }

  /* test JDOHelper.getTransactionalObjectId(Object pc)
   *
   */
  @Test
  public void testGetTransactionalObjectId() {
    pm = getPM();
    Transaction tx = pm.currentTransaction();
    tx.begin();
    PCPoint p1 = new PCPoint(1, 3);
    pm.makePersistent(p1);
    Object oid = JDOHelper.getTransactionalObjectId(p1);
    tx.commit();

    if (oid == null)
      fail(ASSERTION_FAILED, "JDOHelper.getTransactionalObjectId returned null oid.");
    pm.close();
    pm = null;
  }
}
