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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that we can serialize PCPoint instances and read them back correctly.
*
* @author Marina Vatkina
*/
public class Test_Serialize extends AbstractTest {
    Object o = null;

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Serialize.class);
    }

    /** */
    public void test() throws Exception {
        runTestSerialization(false);
    }

    /** */
    public void testRetainValues() throws Exception {
        runTestSerialization(true);
    }
    

    /** Inserts a point instance, and serializes it. */
    protected void runTestSerialization(boolean retainValues) throws Exception {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            if (debug) logger.debug("\nINSERT");
        
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.setRetainValues(retainValues);
            tx.begin();

            PCPoint p = new PCPoint(1, 10);
            pm.makePersistent(p);
            if (debug) logger.debug("Created: " + p);

            tx.commit();

            if (debug)
                logger.debug("\nSERIALIZE " + ((retainValues)?"P-NONTX":"HOLLOW"));

            String f = "myfile.tmp";
            ObjectOutputStream out = getObjectOutputStream(f);
            out.writeObject(p);
            out.flush();
            if (debug) logger.debug("Wrote: " + p);
            
            ObjectInputStream in = getObjectInputStream(f);
            PCPoint p1 = (PCPoint)in.readObject();
            if (debug) logger.debug("Read: " + p1);
            assertEquals("Unexpected deserialzed object", p, p1);
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
}
