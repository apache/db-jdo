/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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

/*
 * StringIdentity.java
 *
 */

package javax.jdo.identity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This class is for identity with a single String field.
 *
 * @version 2.0
 */
public class StringIdentity extends SingleFieldIdentity<StringIdentity> {

  private static final long serialVersionUID = 1L;

  /** The key is stored in the superclass field keyAsObject. */

  /**
   * Constructor with class and key.
   *
   * @param pcClass the class
   * @param key the key
   */
  public StringIdentity(Class<?> pcClass, String key) {
    super(pcClass);
    setKeyAsObject(key);
    hashCode = hashClassName() ^ key.hashCode();
  }

  /** Constructor only for Externalizable. */
  public StringIdentity() {}

  /**
   * Return the key.
   *
   * @return the key
   */
  public String getKey() {
    return (String) keyAsObject;
  }

  /**
   * Return the String form of the key.
   *
   * @return the String form of the key
   */
  public String toString() {
    return (String) keyAsObject;
  }

  /**
   * Determine if the other object represents the same object id.
   *
   * @param obj the other object
   * @return true if both objects represent the same object id
   */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (!super.equals(obj)) {
      return false;
    } else {
      StringIdentity other = (StringIdentity) obj;
      return keyAsObject.equals(other.keyAsObject);
    }
  }

  /**
   * Determine the ordering of identity objects.
   *
   * @param o Other identity
   * @return The relative ordering between the objects
   * @since 2.2
   */
  public int compareTo(StringIdentity o) {
    if (o == null) {
      throw new ClassCastException("object is null");
    }
    int result = super.compare(o);
    return (result == 0) ? ((String) keyAsObject).compareTo((String) o.keyAsObject) : result;
  }

  /**
   * Write this object. Write the superclass first.
   *
   * @param out the output
   */
  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(keyAsObject);
  }

  /**
   * Read this object. Read the superclass first.
   *
   * @param in the input
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    keyAsObject = in.readObject();
  }
}
