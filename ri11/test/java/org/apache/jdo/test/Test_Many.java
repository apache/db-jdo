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

package org.apache.jdo.test;

import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that we can insert objects into a database.  
*
* @author Michael Bouschen
*/
public class Test_Many extends Test_Insert {
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Many.class);
    }

    /** */
    protected int getDefaultInsert()
    {
        // MBO: 
        // There is a problem when running with a SecurityManager.
        // The test runs into a AccessControlException with 998 and 999.
        // With 1000 and more the test runs into JDODataStoreException: 
        // Object with id OID: 100-511 already exists in the database
        // FOStore needs to add some more doPrivileged blocks (see
        // corresponding bug report). 
        // Switch back to 2000 as soon as teh bug is fixed.
        return 990;
    }
}
