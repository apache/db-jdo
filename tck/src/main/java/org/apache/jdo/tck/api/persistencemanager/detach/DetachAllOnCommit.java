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
package org.apache.jdo.tck.api.persistencemanager.detach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Collection;
import java.util.HashSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.tck.pc.shoppingcart.Cart;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test DetachAllOnCommit <br>
 * <B>Keywords:</B> DetachAllOnCommit detach <br>
 * <B>Assertion IDs:</B> A12.6.8-3 <br>
 * <B>Assertion Description: </B> With this flag set to true, during beforeCompletion all cached
 * instances are prepared for detachment according to the fetch plan in effect at commit. Loading
 * fields and unload- ing fields required by the fetch plan is done after calling the user's
 * beforeCompletion callback. During afterCompletion, before calling the user's afterCompletion
 * callback, all detachable persistent instances in the cache transition to detached; non-detachable
 * persistent instances transition to transient; and detachable instances can be serialized as
 * detached instances. Transient transactional instances are unaffected by this flag.
 */
public class DetachAllOnCommit extends DetachTest {

  private static final String ASSERTION_FAILED = "Assertion A12.6.8-3 (DetachAllOnCommit) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(DetachAllOnCommit.class);
  }

  /** */
  public void testDetachAllOnCommit() {
    getPM().currentTransaction().begin();
    setCartFetchGroups();
    pm.retrieveAll(cartClosure);
    pm.setDetachAllOnCommit(true);
    pm.currentTransaction().commit();
    checkCartValues(ASSERTION_FAILED + "after commit with DetachAllOnCommit," + NL, cart1);
    failOnError();
  }
}
