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

package org.apache.jdo.tck.api.persistencecapable;

import javax.jdo.JDONullIdentityException;
import javax.jdo.identity.SingleFieldIdentity;
import javax.jdo.spi.PersistenceCapable;
import org.apache.jdo.tck.pc.singlefieldidentity.AbstractPCPointSingleField;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldByte;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldCharacter;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldDate;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldInteger;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldLong;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldPrimitivebyte;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldPrimitivechar;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldPrimitiveint;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldPrimitivelong;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldPrimitiveshort;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldShort;
import org.apache.jdo.tck.pc.singlefieldidentity.PCPointSingleFieldString;
import org.junit.jupiter.api.Test;

/**
 * <B>Title:</B>NewObjectIdInstance of PersistenceCapable <br>
 * <B>Keywords:</B> persistencecapable <br>
 * <B>Assertion IDs:</B> A7.12-38, A7.12-39 <br>
 * <B>Assertion Description: </B>
 *
 * <p>A7.12-38:<br>
 * For classes using single field identity method <code>PersistenceCapable.newObjectIdInstance()
 * </code> must be called on a persistent instance with its primary key field initialized, or a
 * JDONullIdentityException is thrown.
 *
 * <p>A7.12-39:<br>
 * The instance returned is initialized with the value of the primary key field of the instance on
 * which the method is called.
 *
 * @author Michael Watzek
 */
public class NewObjectIdInstance extends AbstractPersistenceCapableTest {

  /** */
  private static final String FAILED = " (jdoNewObjectIdInstance) failed: ";

  /** */
  private static final String ASSERTION_A7_12_38_FAILED = "Assertion A7.12-38" + FAILED;

  /** */
  private static final String ASSERTION_A7_12_39_FAILED = "Assertion A7.12-39" + FAILED;

  @Test
  public void testSingleFieldIdentityNotInitializedByte() {
    if (isTestToBePerformed()) {
      singleFieldIdentityNotInitialized(new PCPointSingleFieldByte());
    }
  }

  @Test
  public void testSingleFieldIdentityNotInitializedCharacter() {
    if (isTestToBePerformed()) {
      singleFieldIdentityNotInitialized(new PCPointSingleFieldCharacter());
    }
  }

  @Test
  public void testSingleFieldIdentityNotInitializedInteger() {
    if (isTestToBePerformed()) {
      singleFieldIdentityNotInitialized(new PCPointSingleFieldInteger());
    }
  }

  @Test
  public void testSingleFieldIdentityNotInitializedLong() {
    if (isTestToBePerformed()) {
      singleFieldIdentityNotInitialized(new PCPointSingleFieldLong());
    }
  }

  @Test
  public void testSingleFieldIdentityNotInitializedShort() {
    if (isTestToBePerformed()) {
      singleFieldIdentityNotInitialized(new PCPointSingleFieldShort());
    }
  }

  @Test
  public void testSingleFieldIdentityNotInitializedString() {
    if (isTestToBePerformed()) {
      singleFieldIdentityNotInitialized(new PCPointSingleFieldString());
    }
  }

  @Test
  public void testSingleFieldIdentityNotInitializedDate() {
    if (isTestToBePerformed()) {
      singleFieldIdentityNotInitialized(new PCPointSingleFieldDate());
    }
  }

  @Test
  public void testSingleFieldIdentityInitializedbyte() {
    if (isTestToBePerformed()) {
      singleFieldIdentityInitialized(new PCPointSingleFieldPrimitivebyte(1, 2));
    }
  }

  @Test
  public void testSingleFieldIdentityInitializedByte() {
    if (isTestToBePerformed()) {
      singleFieldIdentityInitialized(new PCPointSingleFieldByte(1, 2));
    }
  }

  @Test
  public void testSingleFieldIdentityInitializedchar() {
    if (isTestToBePerformed()) {
      singleFieldIdentityInitialized(new PCPointSingleFieldPrimitivechar(1, 2));
    }
  }

  @Test
  public void testSingleFieldIdentityInitializedCharacter() {
    if (isTestToBePerformed()) {
      singleFieldIdentityInitialized(new PCPointSingleFieldCharacter(1, 2));
    }
  }

  @Test
  public void testSingleFieldIdentityInitializedint() {
    if (isTestToBePerformed()) {
      singleFieldIdentityInitialized(new PCPointSingleFieldPrimitiveint(1, 2));
    }
  }

  @Test
  public void testSingleFieldIdentityInitializedInteger() {
    if (isTestToBePerformed()) {
      singleFieldIdentityInitialized(new PCPointSingleFieldInteger(1, 2));
    }
  }

  @Test
  public void testSingleFieldIdentityInitializedlong() {
    if (isTestToBePerformed()) {
      singleFieldIdentityInitialized(new PCPointSingleFieldPrimitivelong(1, 2));
    }
  }

  @Test
  public void testSingleFieldIdentityInitializedLong() {
    if (isTestToBePerformed()) {
      singleFieldIdentityInitialized(new PCPointSingleFieldLong(1, 2));
    }
  }

  @Test
  public void testSingleFieldIdentityInitializedshort() {
    if (isTestToBePerformed()) {
      singleFieldIdentityInitialized(new PCPointSingleFieldPrimitiveshort(1, 2));
    }
  }

  @Test
  public void testSingleFieldIdentityInitializedShort() {
    if (isTestToBePerformed()) {
      singleFieldIdentityInitialized(new PCPointSingleFieldShort(1, 2));
    }
  }

  @Test
  public void testSingleFieldIdentityInitializedString() {
    if (isTestToBePerformed()) {
      singleFieldIdentityInitialized(new PCPointSingleFieldString(1, 2));
    }
  }

  @Test
  public void testSingleFieldIdentityInitializedDate() {
    if (isTestToBePerformed()) {
      singleFieldIdentityInitialized(new PCPointSingleFieldDate(1, 2));
    }
  }

  /**
   * The common method for test cases checking for assertion A7.12-38:<br>
   * For classes using single field identity method <code>PersistenceCapable.newObjectIdInstance()
   * </code> must be called on a persistent instance with its primary key field initialized,
   *
   * @param o the object to check
   */
  private void singleFieldIdentityNotInitialized(AbstractPCPointSingleField o) {
    assertPersistenceCapable(o);
    PersistenceCapable pc = (PersistenceCapable) o;
    try {
      Object sfi = pc.jdoNewObjectIdInstance();
      String message =
          ASSERTION_A7_12_38_FAILED
              + "pc.jdoNewObjectIdInstance should throw "
              + "JDONullIdentityException if PK field is null, but it returned "
              + sfi;
      fail(message);
    } catch (JDONullIdentityException e) {
      // expected exception
      if (debug) logger.debug("caught expected exception " + e);
    }
  }

  /**
   * The common method for test cases checking for assertion A7.12-39:<br>
   * The instance returned is initialized with the value of the primary key field of the instance on
   * which the method is called.
   *
   * @param o the persistent instance to check
   */
  private void singleFieldIdentityInitialized(AbstractPCPointSingleField o) {
    assertPersistenceCapable(o);
    makePersistent(o);
    PersistenceCapable pc = (PersistenceCapable) o;
    SingleFieldIdentity sfi = (SingleFieldIdentity) pc.jdoNewObjectIdInstance();
    if (!o.equalsPKField(sfi)) {
      fail(
          ASSERTION_A7_12_39_FAILED
              + "pc.jdoNewObjectIdInstance() returned unexpected "
              + "single field identity object id.");
    }
  }
}
