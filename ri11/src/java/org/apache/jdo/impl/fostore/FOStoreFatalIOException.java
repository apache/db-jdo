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

package org.apache.jdo.impl.fostore;

/**
* This is an exception which _should_ never be thrown, as it indicates an
* error in I/O traffic between client and server.  It most likely indicates a
* bug in the implementation.
*
* @author Dave Bristor
*/
public class FOStoreFatalIOException extends FOStoreFatalInternalException {
    /**
     * @param clz Class in which the exception is thrown.
     * @param methodName Name of the method from which the exception is
     * thrown.
     */
    FOStoreFatalIOException(Class clz, String methodName, Exception nested) {
        super(clz, methodName, nested);
    }
}
