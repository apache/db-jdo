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

import org.netbeans.mdr.persistence.StorageException;

/**
* This provides an insulation layer between BtreeDatabase and FOStore.
*
* @author Dave Bristor
*/
class FOStoreDatabaseException extends StorageException {
    private final StorageException ex;

    FOStoreDatabaseException(StorageException ex) {
        this.ex = ex;
    }

    public String getLocalizedMessage() {
        return ex.getLocalizedMessage();
    }

    public String getMessage() {
        return ex.getMessage();
    }

    public void printStackTrace() {
        ex.printStackTrace();
    }

    public void printStackTrace(java.io.PrintStream s) {
        ex.printStackTrace(s);
    }

    public void printStackTrace(java.io.PrintWriter s) {
        ex.printStackTrace(s);
    }

    public String toString() {
        return ex.toString();
    }
}
