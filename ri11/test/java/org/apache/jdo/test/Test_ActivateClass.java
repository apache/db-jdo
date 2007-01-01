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

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

import org.apache.jdo.test.util.Factory;
import org.apache.jdo.pc.PointFactory;

/**
* Tests that we can activate a class.  Actually goes farther than that,
* storing the value of an instance of a class, since that's the only way we can
* cause a class to be activated.
*
* @author Dave Bristor
*/
public class Test_ActivateClass extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_ActivateClass.class);
    }

    /**
     * Inserts some number of objects in the database
     */
    public void test() throws Exception {
        insertObjects();
    }

    /**
     * Determines the kind of objects that are inserted.  Override this if
     * you want to insert some other kind of object.
     */
    protected Factory getFactory(int verify) {
        PointFactory rc = new PointFactory();
        rc.setVerify(verify);
        return rc;
    }
}
