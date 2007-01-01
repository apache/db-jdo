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
 * JDOFatalInternalException.java
 *
 * Created on March 8, 2000, 8:35 AM
 */

package javax.jdo;

/** This class represents errors in the implementation for which no user
 * error handling is possible.  The error should be reported to the JDO
 * vendor for corrective action.
 *
 * @author  Craig Russell
 * @version 1.0
 */
public class JDOFatalInternalException extends JDOFatalException {

  /**
   * Constructs a new <code>JDOFatalInternalException</code> without a detail message.
   */
  public JDOFatalInternalException() {
  }
  

  /**
   * Constructs a new <code>JDOFatalInternalException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public JDOFatalInternalException(String msg) {
    super(msg);
  }

  /**
   * Constructs a new <code>JDOFatalInternalException</code> with the specified
   * detail message and nested <code>Throwable</code>s.
   * @param msg the detail message.
   * @param nested the nested <code>Throwable[]</code>.
   */
  public JDOFatalInternalException(String msg, Throwable[] nested) {
    super(msg, nested);
  }

  /**
   * Constructs a new <code>JDOFatalInternalException</code> with the specified
   * detail message and nested <code>Throwable</code>s.
   * @param msg the detail message.
   * @param nested the nested <code>Throwable</code>.
   */
  public JDOFatalInternalException(String msg, Throwable nested) {
    super(msg, nested);
  }
}

