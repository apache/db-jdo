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

import org.apache.jdo.pc.PCCollections;

/**
* This test tries to insert a persistent capable object into a database;
* the object has a field of each of the kind of primitive and immutable
* Java types.
*
* @author Dave Bristor
*/
public class Test_Collections extends Test_SCO_Base {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Collections.class);
    }

    /** */
    public void test() throws Exception {
        insertObjects();
        readObjects();
        checkExtent(PCCollections.class, 1);
    }

    /** */
    protected void insertObjects() {
        insertAllTypes();
    }

    /**
     * redefine verify called by readObjects to check whether the read
     * instance is correct.
     */
    protected void verify(int i, Object pc) {
        switch (i) {
        case 0 :
            assertEquals("Wrong PCPoint instance", createPoint(), pc);
            break;
        case 1:
            assertEqualsPCCollections(createAllTypes(createPoint()), pc);
            break;
        default:
            fail("Wrong number of inserted objects, expected two");
            break;
        }
    }
}
