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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.serializable.PCClass1;
import org.apache.jdo.pc.serializable.PCClass2A;
import org.apache.jdo.pc.serializable.PCClass2B;
import org.apache.jdo.pc.serializable.PCSub3;
import org.apache.jdo.pc.serializable.PCSub4A;
import org.apache.jdo.pc.serializable.PCSub4B;
import org.apache.jdo.pc.serializable.PCSuper;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;
/**
* Tests that the enhancer adds method writeObject or adds a call of 
* jdoPreSerialize to an existing method writeObject or writeReplace.
*
* @author Michael Bouschen
*/
public class Test_SerializeComplex extends AbstractTest {

    /** name of the writeObject method */
    public static String WRITE_OBJECT_NAME = "writeObject";
    /** parameter types of the writeObject method */
    public static Class[] WRITE_OBJECT_PARAMS = { ObjectOutputStream.class };

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_SerializeComplex.class);
    }

    /**
     * This method checks whether the enhanced pc classes have or have not
     * the method writeObject.
     */
    public void testSerializationSupportMethods()
    {
        // PCClass1 must have writeObject
        try {
            PCClass1.class.getDeclaredMethod(WRITE_OBJECT_NAME, WRITE_OBJECT_PARAMS);
            if (debug)
                logger.debug("OK: Class PCClass1 has method " + WRITE_OBJECT_NAME);
        }
        catch (NoSuchMethodException ex) {
            fail("ERROR: missing method " + WRITE_OBJECT_NAME + " in class PCClass1");
        }

        // PCClass2A must have writeObject
        try {
            PCClass2A.class.getDeclaredMethod(WRITE_OBJECT_NAME, WRITE_OBJECT_PARAMS);
            if (debug)
                logger.debug("OK: Class PCClass2A has method " + WRITE_OBJECT_NAME);
        }
        catch (NoSuchMethodException ex) {
            fail("ERROR: missing method " + WRITE_OBJECT_NAME + " in class PCClass2A");
        }

        // PCClass2B must NOT have writeObject
        try {
            PCClass2B.class.getDeclaredMethod(WRITE_OBJECT_NAME, WRITE_OBJECT_PARAMS);
            fail("ERROR: Class PCClass1 must not have method " + WRITE_OBJECT_NAME);
        }
        catch (NoSuchMethodException ex) {
            if (debug)
                logger.debug("OK: Class PCClass2B does not have method " + WRITE_OBJECT_NAME);
        }

        // PCSuper must NOT have writeObject
        try {
            PCSuper.class.getDeclaredMethod(WRITE_OBJECT_NAME, WRITE_OBJECT_PARAMS);
            fail("ERROR: Class PCSuper must not have method " + WRITE_OBJECT_NAME);
        }
        catch (NoSuchMethodException ex) {
            if (debug)
                logger.debug("OK: Class PCSuper does not have method " + WRITE_OBJECT_NAME);
        }
        // PCSub4A must have writeObject
        try {
            PCSub4A.class.getDeclaredMethod(WRITE_OBJECT_NAME, WRITE_OBJECT_PARAMS);
            if (debug)
                logger.debug("OK: Class PCSub4A has method " + WRITE_OBJECT_NAME);
        }
        catch (NoSuchMethodException ex) {
            fail("ERROR: missing method " + WRITE_OBJECT_NAME + " in class PCSub4A");
        }

        // PCSub4B must NOT have writeObject
        try {
            PCSub4B.class.getDeclaredMethod(WRITE_OBJECT_NAME, WRITE_OBJECT_PARAMS);
            fail("ERROR: Class PCSub4B must not have method " + WRITE_OBJECT_NAME);
        }
        catch (NoSuchMethodException ex) {
            if (debug)
                logger.debug("OK: Class PCSub4B does not have method " + WRITE_OBJECT_NAME);
        }
    }

    /**
     * This method creates pc instances, commits the transaction and then
     * seralizes the pc instances.
     */
    public void testSerialization() throws Exception {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            if (debug) logger.debug("\nINSERT");
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.setRetainValues(false);

            tx.begin();
            PCClass1 o1 = new PCClass1(1, 1); 
            pm.makePersistent(o1);
            Object o1Oid = pm.getObjectId(o1);
            PCClass2A o2 = new PCClass2A(2, 2); 
            pm.makePersistent(o2);
            Object o2Oid = pm.getObjectId(o2);
            PCClass2B o3 = new PCClass2B(3, 3); 
            pm.makePersistent(o3);
            Object o3Oid = pm.getObjectId(o3);
            PCSub3 o4 = new PCSub3(4, 4, "4", "4"); 
            pm.makePersistent(o4);
            Object o4Oid = pm.getObjectId(o4);
            PCSub4A o5 = new PCSub4A(5, 5, "5", "5"); 
            pm.makePersistent(o5);
            Object o5Oid = pm.getObjectId(o5);
            PCSub4B o6 = new PCSub4B(6, 6, "6", "6"); 
            pm.makePersistent(o6);
            Object o6Oid = pm.getObjectId(o6);
            tx.commit(); tx = null;
            pm.close(); pm = null;

            if (debug) logger.debug("\nSERIALIZE HOLLOW instances");
            doSerialize(o1Oid, false, new PCClass1(1, 0));
            doSerialize(o1Oid, true,  new PCClass1(Integer.MIN_VALUE, 0));
            doSerialize(o2Oid, false, new PCClass2A(2, 0));
            doSerialize(o2Oid, true,  new PCClass2A(Integer.MIN_VALUE, 0));
            doSerialize(o3Oid, false, new PCClass2B(3, 0));
            doSerialize(o3Oid, true,  new PCClass2B(Integer.MIN_VALUE, 0));
            doSerialize(o4Oid, false, new PCSub3(Integer.MIN_VALUE,
                        Integer.MIN_VALUE, "4", null));
            doSerialize(o4Oid, true,  new PCSub3(Integer.MIN_VALUE,
                        Integer.MIN_VALUE, "empty", null));
            doSerialize(o5Oid, false, new PCSub4A(Integer.MIN_VALUE,
                        Integer.MIN_VALUE, "5", null));
            doSerialize(o5Oid, true,  new PCSub4A(Integer.MIN_VALUE,
                        Integer.MIN_VALUE, "empty", null));
            doSerialize(o6Oid, false, new PCSub4B(Integer.MIN_VALUE,
                        Integer.MIN_VALUE, "6", null));
            doSerialize(o6Oid, true,  new PCSub4B(Integer.MIN_VALUE,
                        Integer.MIN_VALUE, "empty", null));
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /**
     *
     */
    protected void doSerialize(Object oid, 
                               boolean makeTransient, 
                               Object expected) throws Exception 
    {
        PersistenceManager pm = null;
        try {
            // get the object
            pm = pmf.getPersistenceManager();
            Object o = pm.getObjectById(oid, false);
            
            // makeTransient?
            if (makeTransient)
                pm.makeTransient(o);

            // serialize
            ByteArrayOutputStream bout = new ByteArrayOutputStream ();
            ObjectOutputStream oout = new ObjectOutputStream (bout);
            oout.writeObject(o);
            oout.flush ();
            if (debug) logger.debug("Wrote: " + o);
            byte[] bytes = bout.toByteArray();
            oout.close ();
            
            // deserialize 
            ByteArrayInputStream bin = new ByteArrayInputStream (bytes);
            ObjectInputStream oin = new ObjectInputStream (bin);
            Object read = oin.readObject();
            if (debug) logger.debug("Read:  " + read);
            oin.close ();

            assertEquals("Unexpected deserialized object", expected, read);
        } 
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    
}
