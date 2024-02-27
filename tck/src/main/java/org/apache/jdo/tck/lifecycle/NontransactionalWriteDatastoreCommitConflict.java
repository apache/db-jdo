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

package org.apache.jdo.tck.lifecycle;

import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B> Test NontransactionalWriteDatastoreCommitConflict <br>
 * <B>Keywords:</B> NontransactionalWrite datastore commit <br>
 * <B>Assertion ID:</B> A5.6.2-4, A5.6.2-6, A5.6.2-8, A5.6.2-10. <br>
 * <B>Assertion Description: </B> A5.6.2-4 [If a datastore transaction is begun, commit will write
 * the changes to the datastore with no checking as to the current state of the instances in the
 * datastore. That is, the changes made outside the transaction together with any changes made
 * inside the transaction will overwrite the current state of the datastore.]
 *
 * <p>A5.6.2-6 [If a datastore transaction is begun, rollback will not write any changes to the
 * datastore.]
 *
 * <p>A5.6.2-8 [If an optimistic transaction is begun, commit will write the changes to the
 * datastore after checking as to the current state of the instances in the datastore. The changes
 * made outside the transaction together with any changes made inside the transaction will update
 * the current state of the datastore if the version checking is successful.]
 *
 * <p>A5.6.2-10 [If an optimistic transaction is begun, rollback will not write any changes to the
 * datastore. The persistent-nontransactional-dirty instances will transition according to the
 * RestoreValues flag. ]
 */
public class NontransactionalWriteDatastoreCommitConflict extends NontransactionalWriteTest {

  /** */
  protected static final String ASSERTION_FAILED =
      "Assertion A5.6.2-4 " + "(NontransactionalWriteDatastoreCommitConflict) failed: ";

  /**
   * Create a nontransactional dirty instance, begin and commit a conflicting transaction, begin and
   * commit a datastore transaction, and check that the instance in the datastore was overwritten by
   * this datastore transaction.
   */
  @Test
  public void testDatastoreCommitConflict() {
    if (!checkNontransactionalFeaturesSupported(false)) return;
    createAndModifyVersionedPCPoint();
    conflictingUpdate();
    beginAndCommitTransaction(false);
    checkXValue(ASSERTION_FAILED + "after datastore commit with conflict", NEW_XVALUE);
    failOnError();
  }
}
