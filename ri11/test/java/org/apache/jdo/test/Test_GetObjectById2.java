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

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.jdo.PersistenceManager;

import org.apache.jdo.impl.fostore.FOStorePMF;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
 * This is similar to Test_GetObjectById except that it is much simpler. It 
 * explicitly does not use any features inherited from AbstractTest other than 
 * creating an url and accessing system properties. It ensures that we can run 
 * a user application which does not explicitly load a PersistenceCapable class
 * but can nonetheless fetch instances of that class by OID.
 *
 * @author Dave Bristor
 */
public class Test_GetObjectById2 extends AbstractTest {
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_GetObjectById2.class);
    }
    
    /** */
    protected void setUp() { }

    /** */
    protected void tearDown()  { }

    /** */
    public void test () throws Exception {
        File f = new File(dbName + ".oid");
        assertTrue("Test_GetObjectById2: " +  dbName + ".oid does not exist", f.exists());

        FOStorePMF pmf = new FOStorePMF();
        pmf.setConnectionCreate(false);
        pmf.setConnectionUserName(System.getProperty ("user", "fred"));
        pmf.setConnectionPassword(System.getProperty ("password", "wombat"));

        String url = createURL();
        pmf.setConnectionURL(url);

        PersistenceManager pm = pmf.getPersistenceManager();

        try {
            ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(f));
            if (debug) System.out.println("\nFETCH");

            int count = 0;
            while (true) {
                Object oid = in.readObject();
                if (debug) 
                    System.out.println("Test_GetObjectById2: getting object by id: " + oid);
                Object pc = pm.getObjectById(oid, true);
                if (debug) 
                    System.out.println("After getObjectById: " + pc);
            }
        } catch (EOFException ex) {
            // OK
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
}
