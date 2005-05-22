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

import org.apache.jdo.pc.PCDerived;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

public class Test_PCDerived extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_PCDerived.class);
    }
    
    /** */
    protected void setUp() { }

    /** */
    protected void tearDown()  { }
    

    /** */
    public void test() {
        PCDerived d = new PCDerived();
        d.s = "hello, world";
    }
}
