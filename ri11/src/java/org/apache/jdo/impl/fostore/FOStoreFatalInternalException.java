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

package org.apache.jdo.impl.fostore;

import javax.jdo.JDOFatalInternalException;

/**
* This is an exception which _should_ never be thrown, as it indicates an
* error in the implementation, such as a bug that has been found.
*
* @author Dave Bristor
*/
public class FOStoreFatalInternalException extends JDOFatalInternalException {
    /**
     * @param clz Class in which the exception is thrown.
     * @param methodName Name of the method from which the exception is
     * thrown.
     * @param msg The exception message.
     */
    FOStoreFatalInternalException(Class clz, String methodName, String msg) {
        super(clz.getName() + "." + methodName + ": " + msg); // NOI18N
    }

    /**
     * @param clz Class in which the exception is thrown.
     * @param methodName Name of the method from which the exception is
     * thrown.
     */
    FOStoreFatalInternalException(Class clz, String methodName,
                                  Exception nested) {
        super(clz.getName() + "." + methodName, // NOI18N
              new Exception[] {nested}); // NOI18N
    }

    /**
     * @param clz Class in which the exception is thrown.
     * @param methodName Name of the method from which the exception is
     * thrown.
     * @param msg The exception message.
     */
    FOStoreFatalInternalException(Class clz, String methodName, String msg,
                                  Exception nested) {

        super(clz.getName() + "." + methodName + ": " + msg, // NOI18N
              new Exception[] {nested}); // NOI18N
    }
}
