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

/*
 * JDODetachedFieldAccessException.java
 *
 */

package javax.jdo;

/** This class represents exceptions caused by access of an unloaded field while
 *  the instance is detached.
 *
 * @version 2.0
 * @since 2.0
 */
public class JDODetachedFieldAccessException extends JDOUserException {

  /**
   * Constructs a new <code>JDODetachedFieldAccessException</code> without a 
   * detail message.
   * @since 2.0
   */
  public JDODetachedFieldAccessException() {
  }
  

  /**
   * Constructs a new <code>JDODetachedFieldAccessException</code> with the 
   * specified detail message.
   * @param msg the detail message.
   * @since 2.0
   */
  public JDODetachedFieldAccessException(String msg) {
    super(msg);
  }

  /** Constructs a new <code>JDODetachedFieldAccessException</code> 
   * with the specified detail message
   * and failed object.
   * @param msg the detail message.
   * @param failed the failed object.
   */
  public JDODetachedFieldAccessException(String msg, Object failed) {
    super(msg, failed);
  }
  
  /**
   * Constructs a new <code>JDODetachedFieldAccessException</code> with the
   * specified detail message and nested <code>Throwable</code>s.
   * @param msg the detail message.
   * @param nested the nested <code>Throwable[]</code>.
   * @since 2.0
   */
  public JDODetachedFieldAccessException(String msg, Throwable[] nested) {
    super(msg, nested);
  }

  /**
   * Constructs a new <code>JDODetachedFieldAccessException</code> with the
   * specified detail message and nested <code>Throwable</code>s.
   * @param msg the detail message.
   * @param nested the nested <code>Throwable</code>.
   * @since 2.0
   */
  public JDODetachedFieldAccessException(String msg, Throwable nested) {
    super(msg, nested);
  }
}

