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

package javax.jdo;

import java.util.Properties;

import javax.jdo.pc.PCPoint;
import javax.jdo.util.AbstractTest;
import javax.jdo.util.BatchTestRunner;

/**
 * Tests class javax.jdo.JDOHelper.
 * <p>
 * TBD: implementation of testMakeDirty, 
 * TBD: testing interrogative methods for persistent instances
 * TBD: getPMF for valid PMF class
 */
public class JDOHelperTest extends AbstractTest {
    
    /** */
    public static void main(String args[]) {
        BatchTestRunner.run(JDOHelperTest.class);
    }

    /** */
    public void testGetPM() {
        PCPoint p = new PCPoint(1, new Integer(1));
        if (JDOHelper.getPersistenceManager(p) != null) {
            fail("JDOHelper.getPersistenceManager should return null pm for non-persistent instance");
        }

        // TBD: test for persistent instance
    }

    /** */
    public void testMakeDirty() {
        // TBD: test JDOHelper.makeDirty(pc, fieldName);
    }

    /** */
    public void testGetObjectId() {
        PCPoint p = new PCPoint(1, new Integer(1));
        if (JDOHelper.getObjectId(p) != null) {
            fail("JDOHelper.getObjectId should return null ObjectId for non-persistent instance");
        }

        // TBD test JDOHelper.getObjectId(pc) for persistent instance
    }

    /** */
    public void testGetTransactionObjectId() {
        PCPoint p = new PCPoint(1, new Integer(1));
        if (JDOHelper.getObjectId(p) != null) {
            fail("JDOHelper.getTransactionalObjectId should return null ObjectId for non-persistent instance");
        }

        // TBD test JDOHelper.getTransactionalObjectId(pc) for persistent instance
    }

    /** */
    public void testIsDirty() {
        PCPoint p = new PCPoint(1, new Integer(1));
        if (JDOHelper.isDirty(p)) {
            fail("JDOHelper.isDirty should return false for non-persistent instance");
        }

        // TBD test JDOHelper.isDirty(pc) for persistent instance
    }

    /** */
    public void testIsTransactional() {
        PCPoint p = new PCPoint(1, new Integer(1));
        if (JDOHelper.isTransactional(p)) {
            fail("JDOHelper.isTransactional should return false for non-persistent instance");
        }

        // TBD test JDOHelper.isTransactional(pc) for persistent instance
    }

    /** */
    public void testIsPersistent() {
        PCPoint p = new PCPoint(1, new Integer(1));
        if (JDOHelper.isPersistent(p)) {
            fail("JDOHelper.isPersistent should return false for non-persistent instance");
        }

        // TBD test JDOHelper.isPersistent(pc) for persistent instance
    }

    /** */
    public void testIsNew() {
        PCPoint p = new PCPoint(1, new Integer(1));
        if (JDOHelper.isNew(p)) {
            fail("JDOHelper.isNew should return false for non-persistent instance");
        }

        // TBD test JDOHelper.isNew(pc) for persistent instance
    }


    /** */
    public void testIsDeleted() {
        PCPoint p = new PCPoint(1, new Integer(1));
        if (JDOHelper.isDeleted(p)) {
            fail("JDOHelper.isDeleted should return false for non-persistent instance");
        }

        // TBD test JDOHelper.isDeleted(pc) for persistent instance
    }

    /** */
    public void testGetPMF() {
        // test missing property javax.jdo.PersistenceManagerFactoryClass
        PersistenceManagerFactory pmf = null;
        try {
            pmf = JDOHelper.getPersistenceManagerFactory(new Properties());
            fail("Missing property PersistenceManagerFactoryClass should result in JDOFatalUserException ");
        }
        catch (JDOFatalUserException ex) {
            if (verbose)
                println("Caught expected exception " + ex);
        }

        // TBD: valid PMF class
    }

}

