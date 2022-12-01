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
package org.apache.jdo.tck.api.persistencemanager.detach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.jdo.tck.pc.shoppingcart.Cart;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test DetachSerialize <br>
 * <B>Keywords:</B> serialize detach <br>
 * <B>Assertion IDs:</B> A12.6.8-4, A12.6.8-5 <br>
 * <B>Assertion Description: </B> The JDO 1.0 specification requires that serialized instances be
 * made ready for serialization by instantiating all serializable persistent fields before calling
 * writeObject. For binary- compatible implementations, this is done by the enhancer adding a call
 * to the StateManager prior to invoking the user's writeObject method. The behavior is the same in
 * JDO 2.0, with the additional requirement that restored detachable serialized instances are
 * treated as detached instances.
 */
public class DetachSerialize extends DetachTest {

  private static final String ASSERTION_FAILED =
      "Assertion A12.6.8-4, A12.6.8-5 (DetachSerialize) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(DetachSerialize.class);
  }

  /** */
  public void testDetachSerialize() {
    getPM().currentTransaction().begin();
    setCartFetchGroups();
    // pm.retrieveAll(cartClosure);
    Cart cartDetached = (Cart) detachClosure(cart1);
    checkCartValues(ASSERTION_FAILED + "after deserializing cart," + NL, cartDetached);
    pm.currentTransaction().commit();
    failOnError();
  }

  /**
   * Detach the parameter instance.
   *
   * @param pc pc instance
   * @return detached instance
   */
  protected Object detachClosure(Object pc) {
    byte[] cartStream = serialize(pc);
    return deserialize(cartStream);
  }
  /** */
  private byte[] serialize(Object root) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(root);
      return baos.toByteArray();
    } catch (Exception ex) {
      fail(ASSERTION_FAILED + "serializing cart:" + ex);
      return null; // will not be reached
    }
  }

  private Object deserialize(byte[] stream) {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(stream);
      ObjectInputStream ois = new ObjectInputStream(bais);
      return ois.readObject();
    } catch (Exception ex) {
      fail(ASSERTION_FAILED + "deserializing cart:" + ex);
      return null; // will not be reached
    }
  }
}
