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
import java.io.ObjectInputStream;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.Factory;
import org.apache.jdo.test.util.JDORITestRunner;


/**
* This test is similar to Test_ActivateClass, but it adds extra steps of
* getting OIDs for objects and later retrieving those objects.  Unlike
* Test_Fetch and Test_Fetch2, this should be run in a separate JVM
* <i>after</i> a first JVM has inserted some objects.
*
* Note that by default, this fetches inserted PCPoints...to test other kinds of
* objects, use -Dclass=XXX on the java command line.
*
* @author Dave Bristor
*/
public class Test_FetchInserted extends AbstractTest {
    /** By default, we expect that the instances for which we have oids should
     * exist in the database.  But if shouldExist is false, then we expect
     * that they should not exist.
     */
    private final boolean shouldExist;

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_FetchInserted.class);
    }

    /** */
    public Test_FetchInserted() {
        super();
        existing = true; // Must use existing database!
        shouldExist = Boolean.getBoolean("shouldExist");
    }

    // The idea is that we're going to write a bunch of stuff to a data
    // output stream, then read it back in; we should get the same data
    // back.
    public void test() throws Exception {
        readObjects();
    }

    protected void readObjects() throws Exception {
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            ObjectInputStream in = getOIDFileInputStream();
            if (debug) logger.debug("\nFETCH");

            int count = 0;
            while (true) {
                Object oid = null;
                try {
                    oid = in.readObject();
                    if (debug) logger.debug("fetching: " + oid);
                    Object pc = pm.getObjectById(oid, true);
                    if (!shouldExist) {
                        fail("fetched " + oid +
                             " but it should not have been in database");
                    } else {
                        if (debug) logger.debug("After fetch: " + pc);
                        verify(count++, pc);
                    }
                } catch (JDOObjectNotFoundException ex) {
                    if (shouldExist) {
                        fail("should find object with ObjectId " + oid + ": " + ex);
                    } else {
                        if (debug)
                            logger.debug(oid.toString() +
                                " does not exist in database, as expected");
                    }
                }
            }
        } catch (EOFException ex) {
            // OK
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /**
     * Determines the kind of objects that are inserted.  Override this if
     * you want to insert some other kind of object.
     */
    protected Factory getFactory(int verify) {
        return getFactoryByClassProperty(verify, "org.apache.jdo.pc.PCPoint");
    }
}
