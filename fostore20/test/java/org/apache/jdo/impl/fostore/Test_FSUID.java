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

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.jdo.JDOUserException;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Test that FOStoreSchemaUID works as expected.
*
* @author Dave Bristor
*/
public class Test_FSUID extends AbstractTest {
    /** */
    private FOStorePMF pmf;
    
    /** */
    private FOStoreModel model;
        

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_FSUID.class);
    }

    /** Sets up the PMF for the test. */
    protected void setUp() throws Exception 
    {  
        pmf = new FOStorePMF();
        model = pmf.getModel();
    }

    /** */
    protected void tearDown() 
    {
        if (pmf != null) {
            AccessController.doPrivileged(
                new PrivilegedAction () {
                    public Object run () {
                        try {
                            pmf.close();
                        } catch (JDOUserException ex) {
                            System.out.println("pmf.close threw " + ex);
                            System.out.println("forcing close");
                            if( pmf instanceof FOStorePMF )
                                ((FOStorePMF)pmf).close(true);
                        }
                        return null;
                    }});
        }
        pmf = null;
        model = null;
    }

    /**
     * TestCase: 
     */
    public void test() throws Exception
    {
        check("org.apache.jdo.pc.PCPoint",
              "8956920886101650067");
        check("org.apache.jdo.pc.PCRect",
              "-8480732594287021208");
        check("org.apache.jdo.pc.PCArrays",
              "-8444984152061270200");
        check("org.apache.jdo.pc.empdept.PCPerson",
              "-6869885283474787185");
        check("org.apache.jdo.pc.empdept.PCEmployee",
              "-9214907875158685");
        check("org.apache.jdo.pc.empdept.PCPartTimeEmployee",
              "-7548523301931157884");
    }
    
    /** */
    private void check(String className, String expected) 
        throws ClassNotFoundException
    {
        Class cls = Class.forName(className);
        FOStoreSchemaUID fsuid = FOStoreSchemaUID.lookup(cls, model);
        assertNotNull("FSUID for class " + className + " is null", fsuid);
        assertEquals("Wrong FSUID", expected, fsuid.toString());
    }
    
}
