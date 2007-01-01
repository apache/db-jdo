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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCFullTimeEmployee1;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Tests that we can serialize PC instances with classes that extend another
* PC class and read them back correctly.
*
* @author Marina Vatkina
*/
public class Test_SerializeInher extends Test_ActivateClass {
    Object o = null;

    private static GregorianCalendar born;
    private static GregorianCalendar hired;
    
    static {
        born = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        born.set(1969, 7, 20);
        hired = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        hired.set(1982, 5, 5);
    }

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SerializeInher.class);
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

            PCFullTimeEmployee1 p = new PCFullTimeEmployee1(
                "Scott", "McNealy", born.getTime(), 1L, hired.getTime(), 
                200000.00, 15);

            pm.makePersistent(p);
            if (debug) logger.debug("Created: " + p);
            tx.commit();

            if (debug)
                logger.debug("\nSERIALIZE " +
                             ((retainValues)? "P-NONTX" : "HOLLOW"));

            String f = "myfile.tmp";

            ObjectOutputStream out = getObjectOutputStream(f);
            out.writeObject(p);
            out.flush();
            if (debug) logger.debug("Wrote: " + p);
                
            ObjectInputStream in = getObjectInputStream(f);
            PCFullTimeEmployee1 p1 = (PCFullTimeEmployee1)in.readObject();
            
            if (debug) logger.debug("Read: " + p1);
            assertEquals("Unexpected string repr of deserialized object", 
                         "PCFullTimeEmployee1: Emp: McNealy, Scott, id=1, born 20/Aug/1969, hired 5/Jun/1982 $200000.0 manager: none dept: none emps: 0 insurance: null range: 15", 
                         p1.toString());
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
}
