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
 * JDOFatalException.java
 *
 */

package javax.jdo;

/** This class represents exceptions that are fatal; that is, the condition
 * that caused it cannot be bypassed even if the operation is retried.
 *
 * @version 1.0
 */
public class JDOFatalException extends JDOException {

	private static final long serialVersionUID = -414371106009364006L;

	/**
     * Constructs a new <code>JDOFatalException</code> without a detail message.
     */
    public JDOFatalException() {
    }

    /**
     * Constructs a new <code>JDOFatalException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public JDOFatalException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new <code>JDOFatalException</code> with the specified detail
     * message and nested <code>Throwable</code>s.
     * @param msg the detail message.
     * @param nested the nested <code>Throwable[]</code>.
     */
    public JDOFatalException(String msg, Throwable[] nested) {
        super(msg, nested);
    }
  
    /**
     * Constructs a new <code>JDOFatalException</code> with the specified detail
     * message and nested <code>Throwable</code>s.
     * @param msg the detail message.
     * @param nested the nested <code>Throwable</code>.
     */
    public JDOFatalException(String msg, Throwable nested) {
        super(msg, nested);
    }
  
    /** Constructs a new <code>JDOFatalException</code> with the specified detail message
     * and failed object.
     * @param msg the detail message.
     * @param failed the failed object.
     */
    public JDOFatalException(String msg, Object failed) {
        super(msg, failed);
    }
  
    /** Constructs a new <code>JDOFatalException</code> with the specified detail message,
     * nested <code>Throwable</code>s, and failed object.
     * @param msg the detail message.
     * @param nested the nested <code>Throwable[]</code>.
     * @param failed the failed object.
     */
    public JDOFatalException(String msg, Throwable[] nested, Object failed) {
        super(msg, nested, failed);
    }

    /** Constructs a new <code>JDOFatalException</code> with the specified detail message,
     * nested <code>Throwable</code>s, and failed object.
     * @param msg the detail message.
     * @param nested the nested <code>Throwable</code>.
     * @param failed the failed object.
     */
    public JDOFatalException(String msg, Throwable nested, Object failed) {
        super(msg, nested, failed);
    }
}
