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

import org.apache.jdo.pc.PointFactory;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.Factory;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that we can insert objects into a database.  Creates objects as per
* Test_ActivateClass, and writes their OIDs into a file.
* Test_FetchInserted reads that file, and fetches the objects by OID.
*
* @author Dave Bristor
*/
public class Test_Insert extends AbstractTest {
    
    /** */
    private static boolean silent = false;

    /** */
    public static void main(String args[]) {
        handleArgs(args);
        if (silent)
            runSilentInsert();
        else
            JDORITestRunner.run(Test_Insert.class);
    }

    /** Write the OIDs of the objects that were inserted to a file dbName.oid.
     */
    public void test() throws Exception
    {
        insertObjects();
        checkExtent(factory.getPCClass(), numInsert);
        writeOIDs();
    }

    /** */
    protected Factory getFactory(int verify) {
        PointFactory rc = new PointFactory();
        rc.setVerify(verify);
        return rc;
    }

    /** */
    private static void runSilentInsert() {
        try {
            Test_Insert insert = new Test_Insert();
            insert.setupPMF();
            insert.test();
            insert.closePMF();
        }
        catch (Exception ex) {
            System.out.println("Excetion during insert");
            ex.printStackTrace();
        }
    }

    /** */
    private static void handleArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if ("-silent".equals(args[i]))
                silent = true;
            else
                System.err.println("Test_Insert: ignoring unknon option" + args[i]);
        }
    }
    
}
