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
 * JDOReadOnlyException.java
 */

package javax.jdo;

/**
 * This class represents exceptions caused by an attempt to update a datastore when the PMF was
 * created as "read-only".
 *
 * @version 2.2
 * @since 2.2
 */
public class JDOReadOnlyException extends JDOUserException {

  private static final long serialVersionUID = -6612305192571046806L;

  /**
   * Constructs a new <code>JDOReadOnlyException</code> without a detail message.
   *
   * @since 2.2
   */
  public JDOReadOnlyException() {}

  /**
   * Constructs a new <code>JDOReadOnlyException</code> with the specified detail message.
   *
   * @param msg the detail message.
   * @since 2.2
   */
  public JDOReadOnlyException(String msg) {
    super(msg);
  }

  /**
   * Constructs a new <code>JDOReadOnlyException</code> with the specified detail message and nested
   * <code>Throwable</code>s.
   *
   * @param msg the detail message.
   * @param nested the nested <code>Throwable[]</code>.
   * @since 2.2
   */
  public JDOReadOnlyException(String msg, Throwable[] nested) {
    super(msg, nested);
  }

  /**
   * Constructs a new <code>JDOReadOnlyException</code> with the specified detail message and nested
   * <code>Throwable</code>s.
   *
   * @param msg the detail message.
   * @param nested the nested <code>Throwable</code>.
   * @since 2.2
   */
  public JDOReadOnlyException(String msg, Throwable nested) {
    super(msg, nested);
  }
}
