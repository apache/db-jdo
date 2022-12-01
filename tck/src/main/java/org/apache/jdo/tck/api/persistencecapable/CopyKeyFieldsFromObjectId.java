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

package org.apache.jdo.tck.api.persistencecapable;

import javax.jdo.identity.ByteIdentity;
import javax.jdo.identity.CharIdentity;
import javax.jdo.identity.IntIdentity;
import javax.jdo.identity.LongIdentity;
import javax.jdo.identity.ObjectIdentity;
import javax.jdo.identity.ShortIdentity;
import javax.jdo.identity.SingleFieldIdentity;
import javax.jdo.identity.StringIdentity;
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
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B>CopyKeyFieldsFromObjectId of PersistenceCapable <br>
 * <B>Keywords:</B> persistencecapable <br>
 * <B>Assertion IDs:</B> A7.12-25 <br>
 * <B>Assertion Description: </B>
 *
 * <p>A7.12-25<br>
 * public void jdoCopyKeyFieldsFromObjectId(ObjectIdFieldConsumer fc, Object oid); This method
 * copies fields to the field manager instance from the second parameter instance.
 *
 * @author Michael Watzek
 */
public class CopyKeyFieldsFromObjectId extends AbstractPersistenceCapableTest {

  /** */
  private static final String ASSERTION_FAILED =
      "Assertion A7.12-25 (jdoCopyKeyFieldsFromObjectId) failed: ";

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(CopyKeyFieldsFromObjectId.class);
  }

  public void testSingleFieldIdentitybyte() {
    if (isTestToBePerformed()) {
      singleFieldIdentity(new PCPointSingleFieldPrimitivebyte(1, 2));
    }
  }

  public void testSingleFieldIdentityByte() {
    if (isTestToBePerformed()) {
      singleFieldIdentity(new PCPointSingleFieldByte(1, 2));
    }
  }

  public void testSingleFieldIdentitychar() {
    if (isTestToBePerformed()) {
      singleFieldIdentity(new PCPointSingleFieldPrimitivechar(1, 2));
    }
  }

  public void testSingleFieldIdentityCharacter() {
    if (isTestToBePerformed()) {
      singleFieldIdentity(new PCPointSingleFieldCharacter(1, 2));
    }
  }

  public void testSingleFieldIdentityint() {
    if (isTestToBePerformed()) {
      singleFieldIdentity(new PCPointSingleFieldPrimitiveint(1, 2));
    }
  }

  public void testSingleFieldIdentityInteger() {
    if (isTestToBePerformed()) {
      singleFieldIdentity(new PCPointSingleFieldInteger(1, 2));
    }
  }

  public void testSingleFieldIdentitylong() {
    if (isTestToBePerformed()) {
      singleFieldIdentity(new PCPointSingleFieldPrimitivelong(1, 2));
    }
  }

  public void testSingleFieldIdentityLong() {
    if (isTestToBePerformed()) {
      singleFieldIdentity(new PCPointSingleFieldLong(1, 2));
    }
  }

  public void testSingleFieldIdentityshort() {
    if (isTestToBePerformed()) {
      singleFieldIdentity(new PCPointSingleFieldPrimitiveshort(1, 2));
    }
  }

  public void testSingleFieldIdentityShort() {
    if (isTestToBePerformed()) {
      singleFieldIdentity(new PCPointSingleFieldShort(1, 2));
    }
  }

  public void testSingleFieldIdentityString() {
    if (isTestToBePerformed()) {
      singleFieldIdentity(new PCPointSingleFieldString(1, 2));
    }
  }

  public void testSingleFieldIdentityDate() {
    if (isTestToBePerformed()) {
      singleFieldIdentity(new PCPointSingleFieldDate(1, 2));
    }
  }

  /**
   * The common method for test cases checking for assertion A7.12-25:<br>
   * public void jdoCopyKeyFieldsFromObjectId(ObjectIdFieldConsumer fc, Object oid); This method
   * copies fields to the field manager instance from the second parameter instance.
   *
   * @param o the object to check
   */
  private void singleFieldIdentity(AbstractPCPointSingleField o) {
    assertPersistenceCapable(o);
    SingleFieldIdentity sfi = (SingleFieldIdentity) makePersistent(o);
    PersistenceCapable pc = (PersistenceCapable) o;
    ObjectIdFieldConsumer objectIdFieldConsumer = new ObjectIdFieldConsumer();
    pc.jdoCopyKeyFieldsFromObjectId(objectIdFieldConsumer, sfi);
    if (!objectIdFieldConsumer.equals(sfi)) {
      fail(
          ASSERTION_FAILED
              + "pc.jdoCopyKeyFieldsFromObjectId copies unexpected "
              + "fields into an object id field consumer using a "
              + "single field indentity object id.");
    }
  }

  private static class ObjectIdFieldConsumer implements PersistenceCapable.ObjectIdFieldConsumer {

    private boolean booleanField;
    private byte byteField;
    private char charField;
    private double doubleField;
    private float floatField;
    private int intField;
    private long longField;
    private short shortField;
    private String stringField;
    private Object objectField;

    /*
     * @see PersistenceCapable.ObjectIdFieldConsumer#storeBooleanField(int, boolean)
     */
    public void storeBooleanField(int fieldNumber, boolean value) {
      this.booleanField = value;
    }

    /*
     * @see PersistenceCapable.ObjectIdFieldConsumer#storeCharField(int, char)
     */
    public void storeCharField(int fieldNumber, char value) {
      this.charField = value;
    }

    /*
     * @see PersistenceCapable.ObjectIdFieldConsumer#storeByteField(int, byte)
     */
    public void storeByteField(int fieldNumber, byte value) {
      this.byteField = value;
    }

    /*
     * @see PersistenceCapable.ObjectIdFieldConsumer#storeShortField(int, short)
     */
    public void storeShortField(int fieldNumber, short value) {
      this.shortField = value;
    }

    /*
     * @see PersistenceCapable.ObjectIdFieldConsumer#storeIntField(int, int)
     */
    public void storeIntField(int fieldNumber, int value) {
      this.intField = value;
    }

    /*
     * @see PersistenceCapable.ObjectIdFieldConsumer#storeLongField(int, long)
     */
    public void storeLongField(int fieldNumber, long value) {
      this.longField = value;
    }

    /*
     * @see PersistenceCapable.ObjectIdFieldConsumer#storeFloatField(int, float)
     */
    public void storeFloatField(int fieldNumber, float value) {
      this.floatField = value;
    }

    /*
     * @see PersistenceCapable.ObjectIdFieldConsumer#storeDoubleField(int, double)
     */
    public void storeDoubleField(int fieldNumber, double value) {
      this.doubleField = value;
    }

    /*
     * @see PersistenceCapable.ObjectIdFieldConsumer#storeStringField(int, java.lang.String)
     */
    public void storeStringField(int fieldNumber, String value) {
      this.stringField = value;
    }

    /*
     * @see PersistenceCapable.ObjectIdFieldConsumer#storeObjectField(int, java.lang.Object)
     */
    public void storeObjectField(int fieldNumber, Object value) {
      if (value instanceof Boolean) this.booleanField = ((Boolean) value).booleanValue();
      else if (value instanceof Boolean) this.booleanField = ((Boolean) value).booleanValue();
      else if (value instanceof Byte) this.byteField = ((Byte) value).byteValue();
      else if (value instanceof Character) this.charField = ((Character) value).charValue();
      else if (value instanceof Double) this.doubleField = ((Double) value).doubleValue();
      else if (value instanceof Float) this.floatField = ((Float) value).floatValue();
      else if (value instanceof Integer) this.intField = ((Integer) value).intValue();
      else if (value instanceof Long) this.longField = ((Long) value).longValue();
      else if (value instanceof Short) this.shortField = ((Short) value).shortValue();
      else if (value instanceof String) this.stringField = (String) value;
      else this.objectField = value;
    }

    public boolean equals(SingleFieldIdentity sfi) {
      if (sfi == null) throw new IllegalArgumentException("Argument sfi must not be null");
      boolean result = false;
      if (sfi instanceof ByteIdentity) result = ((ByteIdentity) sfi).getKey() == this.byteField;
      else if (sfi instanceof CharIdentity)
        result = ((CharIdentity) sfi).getKey() == this.charField;
      else if (sfi instanceof IntIdentity) result = ((IntIdentity) sfi).getKey() == this.intField;
      else if (sfi instanceof LongIdentity)
        result = ((LongIdentity) sfi).getKey() == this.longField;
      else if (sfi instanceof ShortIdentity)
        result = ((ShortIdentity) sfi).getKey() == this.shortField;
      else if (sfi instanceof StringIdentity)
        result = ((StringIdentity) sfi).getKey().equals(this.stringField);
      else result = ((ObjectIdentity) sfi).getKey().equals(this.objectField);
      return result;
    }
  }
}
