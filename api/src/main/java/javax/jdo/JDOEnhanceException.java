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
package javax.jdo;

/**
 * Exception thrown when an error occurs during enhancement.
 *
 * @since 3.0
 */
public class JDOEnhanceException extends JDOException {
  private static final long serialVersionUID = 7953336394264555958L;

  /** Constructs a new <code>JDOEnhanceException</code> without a detail message. */
  public JDOEnhanceException() {}

  /**
   * Constructs a new <code>JDOEnhanceException</code> with the specified detail message.
   *
   * @param msg the detail message.
   */
  public JDOEnhanceException(String msg) {
    super(msg);
  }

  /**
   * Constructs a new <code>JDOEnhanceException</code> with the specified detail message and nested
   * <code>Throwable</code>s.
   *
   * @param msg the detail message.
   * @param nested the nested <code>Throwable[]</code>.
   */
  public JDOEnhanceException(String msg, Throwable[] nested) {
    super(msg, nested);
  }

  /**
   * Constructs a new <code>JDOEnhanceException</code> with the specified detail message and nested
   * <code>Throwable</code>s.
   *
   * @param msg the detail message.
   * @param nested the nested <code>Throwable</code>.
   */
  public JDOEnhanceException(String msg, Throwable nested) {
    super(msg, nested);
  }
}
