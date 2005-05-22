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

/*
 * Test_ClosePMF.java
 *
 * Created on April 8, 2003, 2:59 PM
 */

package org.apache.jdo.test;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.JDOUserException;
import javax.jdo.JDOException;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

import org.apache.jdo.test.util.Factory;
import org.apache.jdo.pc.PointFactory;

/**
 * Test that PMF.close() behaves properly.  This test creates multiple PMs
 * and verifies that close() throws an exception with a nested exception
 * for each PM still open; once all PMs are closed, then close() should
 * complete normally.
 * 
 * @author  Craig Russell
 */
public class Test_ClosePMF extends AbstractTest {
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_ClosePMF.class);
    }

    /** Creates a new instance of Test_ClosePMF */
    public Test_ClosePMF() {
        super();
    }
    
    /**
     * Inserts some number of objects in the database.  Here, we just get
     * some PMs and use them to add objects.  Next, we get a PM that is not
     * closed explicitly (it should be closed automatically by PMF.close()).
     * Next, get a PM, begin a tx and verify that this one causes PMF.close()
     * to fail.
     */
    public void test() throws Exception {
        PersistenceManager pm0 = null;
        PersistenceManager pm2 = null;
        PersistenceManager pm4 = null;
        try {
            pm0 = pmf.getPersistenceManager();
            pm0.currentTransaction().begin();
            pm0.currentTransaction().rollback();
            insertObjects(); // this pm is closed by insertObjects
            pm2 = pmf.getPersistenceManager();
            pm2.currentTransaction().begin();
            pm2.currentTransaction().commit();
            insertObjects(); // this pm is closed by insertObjects
            pm4 = pmf.getPersistenceManager();
            pm4.currentTransaction().begin();

            assertNotClosed(pm0, "pm0");
            assertNotClosed(pm2, "pm2");
            assertNotClosed(pm4, "pm4");
            JDOUserException jdouex = null;
            try {
                AccessController.doPrivileged(new PrivilegedAction () {
                        public Object run () throws JDOUserException {
                            pmf.close(); // this should throw an exception with pm4
                            return null;
                        }});
            } catch (JDOUserException ex) {
                jdouex = ex;
            } catch (Exception ex) {
                fail("Wrong exception, expected JDOUserException, got " + ex);
            }
            
            assertNotNull("Failed to catch exception from close.", jdouex);

            Throwable[] nested = jdouex.getNestedExceptions();
            assertEquals("Got wrong number of nested exceptions", 1, nested.length);

            JDOException jdoex = (JDOException)nested[0];
            Object failed = jdoex.getFailedObject();
            assertTrue("Got wrong failed object of type: " + failed.getClass().getName() + 
                       ", expected PersistenceManager.", (failed instanceof PersistenceManager));

            assertEquals("Got wrong PersistenceManager", pm4, failed);

            // since we didn't explicitly close these, they should still be open
            assertNotClosed(pm0, "pm0");
            assertNotClosed(pm2, "pm2");
            assertNotClosed(pm4, "pm4");
            pm4.currentTransaction().commit();
            // now any exception is fatal
            AccessController.doPrivileged(new PrivilegedAction () {
                    public Object run () throws JDOUserException {
                        pmf.close(); 
                        return null;
                    }});
            // the pmf.close() should have closed all the open pms
            assertClosed(pm0, "pm0");
            assertClosed(pm2, "pm2");
            assertClosed(pm4, "pm4");
            // should be able to call this now without effect
            pmf.close();
        }
        finally {
            finalizePM(pm0);
            finalizePM(pm2);
            finalizePM(pm4);
        }
    }

    void assertNotClosed(PersistenceManager pm, String msg) {
        assertFalse("PersistenceManager " + msg + "(" + pm + ") is closed.", pm.isClosed());
    }

    void assertClosed(PersistenceManager pm, String msg) {
        assertTrue("PersistenceManager " + msg + "(" + pm + ") is not closed.", pm.isClosed());
    }

    void finalizePM(PersistenceManager pm)
    {
        if (pm != null && !pm.isClosed()) {
            Transaction tx = pm.currentTransaction();
            if ((tx != null) && tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }
    
    /** Redefine getFactory called be insertObjects. */
    protected Factory getFactory(int verify)
    {
        return new PointFactory();
    }
}
