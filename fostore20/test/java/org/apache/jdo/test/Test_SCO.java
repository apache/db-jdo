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

package org.apache.jdo.test;

import org.apache.jdo.pc.PCSCO;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* This test tries to insert a persistent capable object into a database;
* the object has a field of each of the kind of SCO types.
*
* @author Dave Bristor
*/
public class Test_SCO extends Test_SCO_Base {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SCO.class);
    }

    /** */
    public void test() throws Exception {
        insertObjects();
        readObjects();
        checkExtent(PCSCO.class, 1);
    }

    /** */
    protected void insertObjects() {
        insertDate();
    }

    /**
     * redefine verify called by readObjects to check whether the read
     * instance is correct.
     */
    protected void verify(int i, Object pc) {
        if (i > 0)
            fail("Wrong number of inserted objects, expected only one");
        assertEqualsPCSCO(createDate(), pc);
    }
    
}
