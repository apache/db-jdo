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

import javax.jdo.JDOFatalUserException;

import org.apache.jdo.util.I18NHelper;


/**
* This is an exception which _should_ never be thrown, as it indicates an
* error in the implementation, such as a bug that has been found.
*
* @author Dave Bristor
*/
public class FOStoreLoginException extends JDOFatalUserException {
    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /**
     * @param nested Exception which caused failure
     * thrown.
     */
    FOStoreLoginException(String dbname, String user, Exception nested) {
        super(msg.msg("EXC_LoginFailed", dbname, user), nested); // NOI18N
    }
}

