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
* This test is similar to Test_ActivateClass, but it adds extra steps of
* getting OIDs for objects and later retrieving those objects.  Unlike
* TestFetch, this test inserts more objects.
*
* @author Dave Bristor
*/
public class Test_Fetch2 extends Test_Fetch {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Fetch2.class);
    }

    // The idea is that we're going to write a bunch of stuff to a data
    // output stream, then read it back in; we should get the same data
    // back.
    public void test() throws Exception {
        super.test();

        insertObjects(); // Do another insertion.
        readObjects();
    }
}
