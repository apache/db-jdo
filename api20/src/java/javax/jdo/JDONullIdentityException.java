/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

/*
 * JDONullIdentityException.java
 *
 */

package javax.jdo;

/** An instance of this class is thrown when attempting to create an object id
 *  when the object id constructor parameter is null. This might occur when 
 *  creating an object id instance from a transient instance where an identity 
 *  field is null.
 *
 * @since 2.0
 * @version 2.0
 */
public class JDONullIdentityException extends JDOUserException {

  /**
   * Constructs a new <code>JDONullIdentityException</code> without a detail message.
   */
  public JDONullIdentityException() {
  }

  /**
   * Constructs a new <code>JDONullIdentityException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public JDONullIdentityException(String msg) {
    super(msg);
  }

  /** Constructs a new <code>JDONullIdentityException</code> with the specified detail message
   * and failed object.
   * @param msg the detail message.
   * @param failed the failed object.
   */
  public JDONullIdentityException(String msg, Object failed) {
    super(msg, failed);
  }

  /**
   * Constructs a new <code>JDONullIdentityException</code> with the specified
   * detail message and nested <code>Throwable</code>s.
   * @param msg the detail message.
   * @param nested the nested <code>Throwable[]</code>.
   */
  public JDONullIdentityException(String msg, Throwable[] nested) {
    super(msg, nested);
  }

  /**
   * Constructs a new <code>JDONullIdentityException</code> with the specified detail message
   * and nested <code>Throwable</code>s.
   * @param msg the detail message.
   * @param nested the nested <code>Throwable</code>.
   */
  public JDONullIdentityException(String msg, Throwable nested) {
    super(msg, nested);
  }

}
