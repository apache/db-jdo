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

import javax.jdo.JDOFatalException;

/**
* This exception means that a FOStore doesn't have support for what is being
* requested.  This differs from  JDOUnsupportedException, which indicates that
* an optional feature has not been implemented.  This is used, for example, to
* indicate that storing of a particular type (e.g. an array of non-PC objects)
* is not supported.
*
* @author Dave Bristor
*/
class FOStoreUnsupportedException extends JDOFatalException {
    FOStoreUnsupportedException(String msg) {
        super(msg);
    }
}
