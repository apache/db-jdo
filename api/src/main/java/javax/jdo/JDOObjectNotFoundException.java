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
 * JDOObjectNotFoundException.java
 *
 */

package javax.jdo;

/** This class represents exceptions caused by the user accessing 
 * an object that does not exist in the datastore.
 *
 * @since 1.0.1
 * @version 1.0.1
 */
public class JDOObjectNotFoundException extends JDODataStoreException {

	private static final long serialVersionUID = 1367920495587689648L;

    /**
     * Constructs a new <code>JDOObjectNotFoundException</code> without a detail message.
     */
    public JDOObjectNotFoundException() {
    }

    /**
     * Constructs a new <code>JDOObjectNotFoundException</code> 
     * with the specified detail message.
     * @param msg the detail message.
     */
    public JDOObjectNotFoundException(String msg) {
        super(msg);
    }

    /** Constructs a new <code>JDOObjectNotFoundException</code> with the specified detail message
     * and failed object.
     * @param msg the detail message.
     * @param failed the failed object.
     */
    public JDOObjectNotFoundException(String msg, Object failed) {
        super(msg, failed);
    }

    /**
     * Constructs a new <code>JDOObjectNotFoundException</code> with the
     * specified detail message and nested <code>Throwable</code>s.
     * @param msg the detail message.
     * @param nested the nested <code>Throwable</code>.
     */
    public JDOObjectNotFoundException(String msg, Throwable nested) {
        super(msg, nested);
    }

    /**
     * Constructs a new <code>JDOObjectNotFoundException</code> with the
     * specified detail message and nested <code>Throwable</code>s.
     * @param msg the detail message.
     * @param nested the nested <code>Throwable[]</code>.
     */
    public JDOObjectNotFoundException(String msg, Throwable[] nested) {
        super(msg, nested);
    }

    /**
     * Constructs a new <code>JDOObjectNotFoundException</code> with the specified
     * detail message, nested <code>Throwable</code>s, and failed object.
     * @param msg the detail message.
     * @param nested the nested <code>Throwable[]</code>.
     * @param failed the failed object.
     */
    public JDOObjectNotFoundException(String msg, Throwable[] nested, Object failed) {
        super(msg, nested, failed);
    }

    /**
     * Constructs a new <code>JDOFatalInternalException</code> with the specified
     * detail message, nested <code>Throwable</code>s, and failed object.
     * @param msg the detail message.
     * @param nested the nested <code>Throwable</code>.
     * @param failed the failed object.
     */
    public JDOObjectNotFoundException(String msg, Throwable nested, Object failed) {
        super(msg, nested, failed);
    }
}

