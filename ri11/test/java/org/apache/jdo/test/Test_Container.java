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
 * Test_Container.java
 *
 * Created on August 30, 2001
 */

package org.apache.jdo.test;

import java.util.*;

import javax.jdo.*;

import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.test.util.Container;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
 * Test driver to test transactions in a managed environment
 * 
 * @author Marina Vatkina
 */
public class Test_Container extends AbstractTest {

    Container tc = null;
    String url = null;
    Object oid = null;

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Container.class);
    }

    /** */
    protected void setUp()  {
        tc = new Container(debug);
        url = createURL();
    }

    /** */
    protected void tearDown()  {
        pmf = tc.close();
        closePMF();
    }

    /** */
    public void test() {
        String repr = null;

        // Insert - rollback
        if (debug) logger.debug("===TESTCMT_NEG: ");
        runTestCMT(false);
        
        // Insert - commit
        if (debug) logger.debug("===TESTCMT_POS: ");
        runTestCMT(true);
        
        // Get Extent with 1 instance
        if (debug) logger.debug("===QUERY: ");
        repr = queryInstance();
        assertEquals("Wrong PCPoint instance",
                     "org.apache.jdo.pc.PCPoint x: 9, y: 999", repr);

        // Update instance and test the same PMs in both "beans"
        if (debug) logger.debug("===TESTCMT_CMT: ");
        runTestCMT_CMT();

        // Update instance and test the same PMs in both "beans"
        if (debug) logger.debug("===TESTBMT_JDO_CMT: ");
        runTestBMT_JDO_CMT();

        // Update instance 
        if (debug) logger.debug("===TESTBMT_JDO_POS: ");
        runTestBMT_JDO_POS();

        // Get Extent with 1 updated instance
        if (debug) logger.debug("===QUERY: ");
        repr = queryInstance();
        assertEquals("Wrong PCPoint instance",
                     "org.apache.jdo.pc.PCPoint x: 72, y: 720", repr);

        // Delete the instance
        if (debug) logger.debug("===TESTBMT_UT_POS: ");
        runTestBMT_UT_POS();

        // Get Extent with 0 instances
        if (debug) logger.debug("===QUERY: ");
        repr = queryInstance();
        assertEquals("Empty query result expected", "NONE", repr);

        // Fail to delete instance got by non-existent Oid
        if (debug) logger.debug("===TESTBMT_UT_NEG: ");
        runTestBMT_UT_NEG();

        // Fail to update instance got by non-existent Oid
        if (debug) logger.debug("===TESTBMT_JDO_NEG: ");
        runTestBMT_JDO_NEG();
    }

    private void runTestCMT(boolean commit) {
        PersistenceManager pm = null;
        try {
            PersistenceManagerFactory pmf = tc.getPMF(url);
            tc.startCMT();
            pm = pmf.getPersistenceManager();

            Object o = createInstance();
            pm.makePersistent(o);
        
            oid = pm.getObjectId(o);
        }
        finally {
            pm.close();
        
            // This is a hack to make transaction fail:
            tc.finishCMT(commit);
        }
    }
    
    private void runTestCMT_CMT() {
        PersistenceManager pm = null;
        try {
            PersistenceManagerFactory pmf = tc.getPMF(url);
            tc.startCMT();
            pm = pmf.getPersistenceManager();

            PCPoint p = (PCPoint)pm.getObjectById(oid, false); 
            updateInstance(p);

            validate(p, pm);

        } finally {
            pm.close();
            tc.finishCMT(true);
        }
    }

    private void runTestBMT_JDO_CMT() {
        PersistenceManager pm = null;
        Transaction tx = null;
        try { 
            PersistenceManagerFactory pmf = tc.getPMF(url);
            tc.startBMT(); 
            pm = pmf.getPersistenceManager(); 
            tx = pm.currentTransaction();
            tx.begin();

            PCPoint p = (PCPoint)pm.getObjectById(oid, false); 
            updateInstance(p);

            validate(p, pm);
            
            tx.commit();
        } finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
            pm.close();
            tc.finishBMT(); 
        }
    }
    
    private void runTestBMT_JDO_POS() { 
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            PersistenceManagerFactory pmf = tc.getPMF(url);
            tc.startBMT(); 
            pm = pmf.getPersistenceManager(); 
            tx = pm.currentTransaction();
            tx.begin();

            updateInstance((PCPoint)pm.getObjectById(oid, false)); 

            tx.commit();
        }
        finally {
            if ((tx != null) && tx.isActive())
                tx.rollback();
            pm.close();
            tc.finishBMT(); 
        }
    }

    private void runTestBMT_JDO_NEG() { 
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            PersistenceManagerFactory pmf = tc.getPMF(url);
            tc.startBMT(); 
            pm = pmf.getPersistenceManager(); 
            tx = pm.currentTransaction();
            tx.begin();
            
            updateInstance((PCPoint)pm.getObjectById(oid, false)); 
            
            tx.commit();
            fail("ERROR TO FINISH: ");
        } catch (Exception e) { 
            if (debug)
                logger.debug ("EXPECTED FOR testBMT_JDO_NEG: " + e.getMessage());
            tx.rollback();
        } 
        finally {
            pm.close();
            tc.finishBMT(); 
        }
    }


    private void runTestBMT_UT_POS() {  
        PersistenceManager pm = null;
        try { 
            PersistenceManagerFactory pmf = tc.getPMF(url);
            tc.startBMT();
            tc.getUserTransaction().begin();
            
            pm = pmf.getPersistenceManager(); 
            pm.deletePersistent(pm.getObjectById(oid, false));  
            
            pm.close();
            tc.getUserTransaction().commit();
        } catch (Exception e) {  
            if (debug) logger.debug ("testBMT_UT_POS: " + e);
            try {
                tc.getUserTransaction().rollback();
            } catch (Exception e1) {
                if (debug) logger.debug("testBMT_UT_POS: " + e1);
                fail("testBMT_UT_POS: " + e1);
            }
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
            tc.finishBMT(); 
        }
    }

    private void runTestBMT_UT_NEG() {  
        PersistenceManager pm = null;
        try { 
            PersistenceManagerFactory pmf = tc.getPMF(url);
            tc.startBMT();
            tc.getUserTransaction().begin();

            pm = pmf.getPersistenceManager(); 
            pm.deletePersistent(pm.getObjectById(oid, false));  

            pm.close();

            tc.getUserTransaction().commit();
            tc.finishBMT(); 
            fail("ERROR TO FINISH: ");
        } catch (Exception e) {  
            if (debug)
                logger.debug ("EXPECTED FOR testBMT_UT_NEG: " + e.getMessage());
            try {
                tc.getUserTransaction().rollback();
            } catch (Exception e1) {
                if (debug) logger.debug ("ERROR IN testBMT_UT_NEG: " + e1);
                fail("ERROR IN testBMT_UT_NEG: " + e1);
            }
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
        
    }

    private PCPoint createInstance() {
        PCPoint p = new PCPoint(9, 999);
        return p;
    }

    private void updateInstance(PCPoint p) {
        int i = p.getX();
        p.setX(i*2);
        p.setY(new Integer(i*20));
    }


    private String queryInstance() {
        PersistenceManagerFactory pmf = tc.getPMF(url);
        tc.startBMT();
        PersistenceManager pm = pmf.getPersistenceManager();
/* Not yet implemented
        Query query = pm.newQuery();
        query.setClass (test.PCPoint.class);
        query.setFilter("x == " + ???);
        query.setCandidates(pm.getExtent(test.PCPoint.class, false));

        Object result = query.execute();
*/
        Extent result = pm.getExtent(org.apache.jdo.pc.PCPoint.class, false);

        String s = "";
        if (result ==  null) {
            s = "NULL";
        } else {
            int count = 0;
            Object pc = null;

            for (Iterator i = result.iterator(); i.hasNext();) {
                pc = i.next();
                count++;
            }
            if ( count == 0 ) {
                s = "NONE";
            } else if ( count > 1 ) {
                s = "MANY";
            } else {
                s = "" + pc;
            }
        }
        pm.close();
        tc.finishBMT();

        return s;
    }

    private void validate(PCPoint e, PersistenceManager pm0) {
        PersistenceManagerFactory pmf = tc.getPMF(url);
        PersistenceManager pm = pmf.getPersistenceManager();

        PCPoint p = (PCPoint)pm.getObjectById(oid, false);

        if (p != e) {
            fail("OBJECTS NOT EQUAL: " + e + " <> " + p);
        } else {
            if (debug) logger.debug ("OBJECTS ARE EQUAL!!! " );
        }

        if (!pm0.equals(pm)) {
            fail("PMS NOT EQUAL!!!");
        } else {
            if (debug) logger.debug ("PMS ARE EQUAL!!!");
        }

        pm.close();
    }
}
