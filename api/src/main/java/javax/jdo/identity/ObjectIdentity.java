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
 * ObjectIdentity.java
 *
 */

package javax.jdo.identity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivilegedAction;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOUserException;
import javax.jdo.LegacyJava;
import javax.jdo.spi.JDOImplHelper;

/**
 * This class is for identity with a single Object type field.
 *
 * @version 2.0
 */
public class ObjectIdentity extends SingleFieldIdentity<ObjectIdentity> {

  private static final long serialVersionUID = 1L;

  /** The key is stored in the superclass field keyAsObject. */

  /** The JDOImplHelper instance used for parsing the String to an Object. */
  private static final JDOImplHelper HELPER = doPrivileged(JDOImplHelper::getInstance);

  @SuppressWarnings("unchecked")
  private static <T> T doPrivileged(PrivilegedAction<T> privilegedAction) {
    try {
      return (T) LegacyJava.doPrivilegedAction.invoke(null, privilegedAction);
    } catch (IllegalAccessException | InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException) {
        throw (RuntimeException) e.getCause();
      }
      throw new JDOFatalInternalException(e.getMessage());
    }
  }

  /** The delimiter for String constructor. */
  private static final String STRING_DELIMITER = ":"; // NOI18N

  /**
   * Constructor with class and key.
   *
   * @param pcClass the class
   * @param param the key
   */
  @SuppressWarnings("static-access")
  public ObjectIdentity(Class<?> pcClass, Object param) {
    super(pcClass);
    assertKeyNotNull(param);
    String paramString = null;
    String keyString = null;
    String className = null;
    if (param instanceof String) {
      /* The paramString is of the form "<className>:<keyString>" */
      paramString = (String) param;
      if (paramString.length() < 3) {
        throw new JDOUserException(
            MSG.msg("EXC_ObjectIdentityStringConstructionTooShort")
                + // NOI18N
                MSG.msg(
                    "EXC_ObjectIdentityStringConstructionUsage", // NOI18N
                    paramString));
      }
      int indexOfDelimiter = paramString.indexOf(STRING_DELIMITER);
      if (indexOfDelimiter < 0) {
        throw new JDOUserException(
            MSG.msg("EXC_ObjectIdentityStringConstructionNoDelimiter")
                + // NOI18N
                MSG.msg(
                    "EXC_ObjectIdentityStringConstructionUsage", // NOI18N
                    paramString));
      }
      keyString = paramString.substring(indexOfDelimiter + 1);
      className = paramString.substring(0, indexOfDelimiter);
      keyAsObject = HELPER.construct(className, keyString);
    } else {
      keyAsObject = param;
    }
    hashCode = hashClassName() ^ keyAsObject.hashCode();
  }

  /** Constructor only for Externalizable. */
  public ObjectIdentity() {}

  /**
   * Return the key.
   *
   * @return the key
   */
  public Object getKey() {
    return keyAsObject;
  }

  /**
   * Return the String form of the object id. The class of the object id is written as the first
   * part of the result so that the class can be reconstructed later. Then the toString of the key
   * instance is appended. During construction, this process is reversed. The class is extracted
   * from the first part of the String, and the String constructor of the key is used to construct
   * the key itself.
   *
   * @return the String form of the key
   */
  @Override
  public String toString() {
    return keyAsObject.getClass().getName() + STRING_DELIMITER + keyAsObject.toString();
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
      ObjectIdentity other = (ObjectIdentity) obj;
      return keyAsObject.equals(other.keyAsObject);
    }
  }

  /**
   * Provide the hash code for this instance. The hash code is the hash code of the contained key.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return keyAsObject.hashCode();
  }

  /**
   * Determine the ordering of identity objects.
   *
   * @param o Other identity
   * @return The relative ordering between the objects
   * @since 2.2
   */
  @SuppressWarnings("unchecked")
  public int compareTo(ObjectIdentity o) {
    if (o == null) {
      throw new ClassCastException("object is null");
    }
    int result = super.compare(o);
    if (result == 0) {
      if (o.keyAsObject instanceof Comparable && keyAsObject instanceof Comparable) {
        return ((Comparable<Object>) keyAsObject).compareTo(o.keyAsObject);
      } else {
        throw new ClassCastException(
            "The key class ("
                + keyAsObject.getClass().getName()
                + ") does not implement Comparable");
      }
    } else {
      return result;
    }
  }

  /**
   * Write this object. Write the superclass first.
   *
   * @param out the output
   */
  @Override
  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
    out.writeObject(keyAsObject);
  }

  /**
   * Read this object. Read the superclass first.
   *
   * @param in the input
   */
  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
    keyAsObject = in.readObject();
  }
}
