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

/*
 * ShortIdentity.java
 *
 */

package javax.jdo.identity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class is for identity with a single short field.
 *
 * @version 2.0
 */
public class ShortIdentity extends SingleFieldIdentity<ShortIdentity> {
  private static final long serialVersionUID = 1L;

  private short key;

  private void construct(short key) {
    this.key = key;
    hashCode = hashClassName() ^ key;
  }

  /**
   * Constructor with class and key.
   *
   * @param pcClass the class
   * @param key the key
   */
  public ShortIdentity(Class<?> pcClass, short key) {
    super(pcClass);
    construct(key);
  }

  /**
   * Constructor with class and key.
   *
   * @param pcClass the class
   * @param key the key
   */
  public ShortIdentity(Class<?> pcClass, Short key) {
    super(pcClass);
    setKeyAsObject(key);
    construct(key.shortValue());
  }

  /**
   * Constructor with class and key.
   *
   * @param pcClass the class
   * @param str the key
   */
  public ShortIdentity(Class<?> pcClass, String str) {
    super(pcClass);
    assertKeyNotNull(str);
    construct(Short.parseShort(str));
  }

  /** Constructor only for Externalizable. */
  public ShortIdentity() {}

  /**
   * Return the key.
   *
   * @return the key
   */
  public short getKey() {
    return key;
  }

  /**
   * Return the String form of the key.
   *
   * @return the String form of the key
   */
  public String toString() {
    return Short.toString(key);
  }

  /**
   * Determine if the other object represents the same object id.
   *
   * @param obj the other object
   * @return true if both objects represent the same object id
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (!super.equals(obj)) {
      return false;
    } else {
      ShortIdentity other = (ShortIdentity) obj;
      return key == other.key;
    }
  }

  /**
   * Determine the ordering of identity objects.
   *
   * @param o Other identity
   * @return The relative ordering between the objects
   * @since 2.2
   */
  public int compareTo(ShortIdentity o) {
    if (o == null) {
      throw new ClassCastException("object is null");
    }
    int result = super.compare(o);
    return (result == 0) ? (key - o.key) : result;
  }

  /**
   * Create the key as an Object.
   *
   * @return the key as an Object
   * @since 2.0
   */
  @Override
  protected Object createKeyAsObject() {
    return Short.valueOf(key);
  }

  /**
   * Write this object. Write the superclass first.
   *
   * @param out the output
   */
  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeShort(key);
  }

  /**
   * Read this object. Read the superclass first.
   *
   * @param in the input
   */
  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    key = in.readShort();
  }
}
