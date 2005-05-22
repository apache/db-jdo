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


import java.util.*;

import javax.jdo.*;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

import org.apache.jdo.pc.appid.PCDepartment;
import org.apache.jdo.pc.appid.PCEmployee;
import org.apache.jdo.pc.appid.PCFullTimeEmployee;
import org.apache.jdo.pc.appid.PCInsurance;
import org.apache.jdo.pc.appid.PCPartTimeEmployee;
import org.apache.jdo.pc.appid.PCProject;

/**
* Test a PC that inherits from another PC with an application identity.
*
* @author Marina Vatkina
*/
public class Test_EmpDeptAppId extends AbstractTest {
    
    protected PersistenceManager pm;
    protected Transaction tx;
    protected String scottRepr = null;
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_EmpDeptAppId.class);
    }

    /** */
    public void test() throws Exception
    {
        insertObjects();
        checkObjects();
        updateObject();
        checkObjects();
        deleteObjects();
    }    
    
    /** */
    protected void insertObjects() {
        try
        {
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            createObjects();
            tx.commit();
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    /** Check created objects by Extent and getObjectById.
     */
    protected void checkObjects() {
        try {
            pm = pmf.getPersistenceManager();
            if (debug) logger.debug("EXTENT PCEmployee");
            Extent ext = pm.getExtent(PCEmployee.class, true);
            TreeMap elements = new TreeMap(); // Sort the results.
            for (Iterator i = ext.iterator(); i.hasNext();) {
                PCEmployee p = (PCEmployee)i.next();
                elements.put(p.toString(), p);
            }
            
            int count = 0;
            for (Iterator i = elements.values().iterator(); i.hasNext();) {
                Object p = i.next();
                verify(count++, p);
                loadAndVerifyObject(p, pm);
            }
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    /** Delete objects and check by getObjectById.
     */
    protected void deleteObjects() {
        try {
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();

            if (debug) logger.debug("EXTENT PCEmployee");
            Extent ext = pm.getExtent(PCEmployee.class, true);
            TreeMap elements = new TreeMap(); // Sort the results.
            for (Iterator i = ext.iterator(); i.hasNext();) {
                PCEmployee p = (PCEmployee)i.next();
                elements.put(p.toString(), p);
            }
            
            for (Iterator i = elements.values().iterator(); i.hasNext();) {
                Object p = i.next();
                Object oid = pm.getObjectId(p);
                if (debug) logger.debug("Delete with OID: " + oid);
                pm.deletePersistent(p);
                
                Object o =  pm.getObjectById(oid, true);
                assertSame("Expected same instance from getObjectById", p, o);
                assertTrue("Instance should be deleted", JDOHelper.isDeleted(o));
            }
            tx.commit();
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
        checkExtent(PCEmployee.class, 0);
    }

    protected void createObjects() {
        // Create and set up employees.  Scott is Ed's manager.  Ed is
        // Scott's sole employee.
        //
        ArrayList arr;
        
        GregorianCalendar born =
            new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        GregorianCalendar hired =
            new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));

        born.set(1945, 5, 9);
        hired.set(2001, 3, 5);
        PCEmployee john = 
            new PCEmployee( 
                "John", "One", born.getTime(), 
                900L, hired.getTime(),
                null, null, null, null, null);
                           
        born.set(1969, 7, 20);
        hired.set(1982, 5, 5);
        PCEmployee scott =
            new PCFullTimeEmployee(
                "Scott", "McNealy", born.getTime(), 
                1L, hired.getTime(),
                200000.0);
        born.set(1960, 4, 8);
        hired.set(1985, 2, 3);
        PCEmployee ed = 
            new PCPartTimeEmployee(
                "Ed", "Zander", born.getTime(), 
                100L, hired.getTime(),
                400000.0); 
                           
        ed.setManager(scott);

        arr = new ArrayList();
        arr.add(ed);
        scott.setEmployees(arr);

        // Set up their departments.
        PCDepartment board =
            new PCDepartment(100L, "board");
        HashSet h = new HashSet();
        h.add(scott);
        board.setEmployees(h);
        scott.setDepartment(board);

        PCDepartment emg =
            new PCDepartment(200L, "emg");
        h = new HashSet();
        h.add(ed);
        emg.setEmployees(h);
        ed.setDepartment(emg);
        john.setDepartment(emg);

        // Insure these guys
        PCInsurance scottIns = new PCInsurance(1000, "Aetna", scott);
        PCInsurance edIns = new PCInsurance(1001, "BlueCross", ed);
        scott.setInsurance(scottIns);
        ed.setInsurance(edIns);
        PCInsurance johnIns = new PCInsurance(9001, "BlueCross", john);
        john.setInsurance(johnIns);

        // Give them some projects to work on.  Scott works on both; Ed only
        // on one.
        PCProject solaris = new PCProject(1L, "Solaris");
        PCProject sparc = new PCProject(2L, "Sparc");
        h = new HashSet();
        h.add(scott);
        h.add(ed);
        solaris.setEmployees(h); // Solaris is worked on by Scott and Ed

        h = new HashSet();
        h.add(scott);
        sparc.setEmployees(h); // Sparc is worked on by Scott
        
        arr = new ArrayList();
        arr.add(solaris);
        arr.add(sparc);
        scott.setProjects(arr); // Scott works on Solaris and Sparc

        arr = new ArrayList();
        arr.add(solaris);
        ed.setProjects(arr); // Ed works on Solaris

        // Show what we've got
        if (debug) {
            logger.debug("Before insert: ");
            logger.debug(scott.toString());
            logger.debug(ed.toString());
            logger.debug(john.toString());
            logger.debug(board.toString());
            logger.debug(emg.toString());
            logger.debug(scottIns.toString());
            logger.debug(edIns.toString());
            logger.debug(johnIns.toString());
            logger.debug(solaris.toString());
            logger.debug(sparc.toString());
        }

        // Make it all persistent.  If reachability were implemented, we'd
        // only have to make scott and ed persistent, as everything else is
        // reachable from them.
        pm.makePersistent(scott);
        pm.makePersistent(ed);
        pm.makePersistent(john);

        Object oid = pm.getObjectId(scott);
        if (debug) logger.debug("Instance OID for scott: " + oid);
        Object o =  pm.getObjectById(oid, true);
        assertSame("getObjectById should return the same instance", scott, o); 

        oid = pm.getObjectId(ed);
        if (debug) logger.debug("Instance OID for ed: " + oid);
        o =  pm.getObjectById(oid, true);
        assertSame("getObjectById should return the same instance", ed, o); 

        oid = pm.getObjectId(john);
        if (debug) logger.debug("Instance OID for john: " + oid);
        o =  pm.getObjectById(oid, true);
        assertSame("getObjectById should return the same instance", john, o); 

        scottRepr = "PCFullTimeEmployee: PCEmployee: PCPerson: McNealy, Scott, born 20/Aug/1969, id=1, hired 5/Jun/1982 manager: none dept: board emps: 1 insurance: Aetna $200000.0";
    }
        
   /** Check created objects by Extent and getObjectById.
     */
    protected void updateObject() {
        try {
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            String s = "1"; 
            Object oid = pm.newObjectIdInstance(PCEmployee.class, s);
            if (debug)
                logger.debug("GOT OID: " + oid.getClass().getName() +
                             " value: " + oid);
            PCEmployee p = (PCEmployee)pm.getObjectById(oid, true);
            if (debug) logger.debug("GOT INSTANCE TO UPDATE: " + p);

            GregorianCalendar date =
                new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
            date.set(1999, 7, 9);

            p.setHiredate(date.getTime());
            tx.commit();
            
            oid = pm.getObjectId(p);
            Object o = pm.getObjectById(oid, true);
            if (debug) logger.debug("GOT INSTANCE AFTER COMMIT: " + o);
            assertSame("Expected to get same instance", p, o);
            scottRepr = "PCFullTimeEmployee: PCEmployee: PCPerson: McNealy, Scott, born 20/Aug/1969, id=1, hired 9/Aug/1999 manager: none dept: board emps: 1 insurance: Aetna $200000.0";
            assertEquals("Wrong string represenatation of updated instance scott", 
                         scottRepr, o.toString());
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    protected void loadAndVerifyObject(Object obj, PersistenceManager pm) {
        Object oid = pm.getObjectId(obj);
        if (debug) logger.debug("Instance OID: " + oid);
        
        PersistenceManager pm1 = null;
        try {
            pm1 = pmf.getPersistenceManager();
            Object obj1 =  pm1.getObjectById(oid, true);
            if (debug) logger.debug("Instance from OID: " + obj1);
            assertEquals("Wrong string represenatation of loaded instance",
                         obj.toString(), obj1.toString());
            Object oid1 = pm1.getObjectId(obj1);
            if (debug) logger.debug("OID from instance: " + oid1);
            assertEquals("OID from instance not equal", oid, oid1);
            Object obj2 =  pm1.getObjectById(oid, true);
            assertSame("Same instance from getObjectById", obj1, obj2);
        }
        finally {
            if (pm1 != null && !pm1.isClosed())
                pm1.close();
        }
    }

    /** */
    protected void verify(int i, Object pc) {
        if (debug) logger.debug("verify " + pc);
        Object oid = JDOHelper.getObjectId(pc);
        switch(i) {
        case 0 :
            // should be John
            assertEquals("Unexpected string represenatation of John", 
                         "PCEmployee: PCPerson: One, John, born 9/Jun/1945, id=900, hired 5/Apr/2001 manager: none dept: emg emps: 0 insurance: BlueCross", 
                         pc.toString());
            assertEquals("Unexpected oid", new PCEmployee.Oid("900"), oid);
            break; 
        case 1:
            // should be Scott
            assertEquals("Unexpected string represenatation of Scott", scottRepr, pc.toString());
            assertEquals("Unexpected oid", new PCEmployee.Oid("1"), oid);
            break;
        case 2:
            // should be Ed
            assertEquals("Unexpected string represenatation of Ed", 
                         "PCPartTimeEmployee: PCEmployee: PCPerson: Zander, Ed, born 8/May/1960, id=100, hired 3/Mar/1985 manager: McNealy dept: emg emps: 0 insurance: BlueCross $400000.0", 
                         pc.toString());
            assertEquals("Unexpected oid", new PCEmployee.Oid("100"), oid);
            break;
        default:
            fail("Wrong number of inserted objects, expected three");
            break;
        }
    }
}
