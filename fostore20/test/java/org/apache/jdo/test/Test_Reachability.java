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

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TimeZone;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.pc.PCDepartment1;
import org.apache.jdo.pc.PCEmployee1;
import org.apache.jdo.pc.PCInsurance1;
import org.apache.jdo.pc.PCPoint;
import org.apache.jdo.pc.PCProject1;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Test a reachability algorithm for PC that has some fields which 
* are Collections or PC's.
*
* @author Marina Vatkina
* @version 1.0.1
*/
public class Test_Reachability extends AbstractTest {

    PCEmployee1 scott;
    PCEmployee1 ed;
    PCEmployee1 admin;

    PCDepartment1 board;
    PCDepartment1 emg;

    PCInsurance1 scottIns;
    PCInsurance1 edIns;
    PCInsurance1 adminIns;

    PCProject1 solaris;
    PCProject1 sparc;
    
    Object adminKey;

    Object scottOid;
    Object edOid;

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Reachability.class);
    }

    /** */
    public void test() {
        createObjects();
        insertObjects();
        checkExtent(PCEmployee1.class, 2);
        checkExtent(PCDepartment1.class, 2);
        checkExtent(PCInsurance1.class, 1);
        checkExtent(PCProject1.class, 2);
        checkExtent(PCPoint.class, 3);
        readObjects();
    }
    
    // We override this from Test_ActivateClass and insert our own objects.
    // Use Test_Extent to read them back.
    protected void createObjects() {
        HashSet h;
        Map m;
        LinkedList l;
        Object[] a;
        
        // Create and set up employees.  Scott is Ed's manager.  Ed is
        // Scott's sole employee. Admin reports to Ed.
        //
        GregorianCalendar born =
            new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        GregorianCalendar hired =
            new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));

        born.set(1969, 7, 20);
        hired.set(1982, 5, 5);
        scott = new PCEmployee1(1L, "McNealy", "Scott", 200000.0,
                                born.getTime(), hired.getTime());
        born.set(1960, 4, 8);
        hired.set(1985, 2, 3);
        ed = new PCEmployee1(100L, "Zander", "Ed", 400000.0,
                             born.getTime(), hired.getTime());
        born.set(1980, 10, 18);
        hired.set(1998, 5, 3);
        admin = new PCEmployee1(99100L, "Clark", "Jane", 50000.0,
                                born.getTime(), hired.getTime());
                           
        // Set up their departments.
        board = new PCDepartment1(100L, "board");
        h = new HashSet();
        h.add(scott);
        board.setEmployees(h);
        scott.setDepartment(board);

        emg = new PCDepartment1(200L, "emg");
        h = new HashSet();
        h.add(ed);
        h.add(admin);
        emg.setEmployees(h);
        ed.setDepartment(emg);
        admin.setDepartment(emg);

        // Insure these guys
        scottIns = new PCInsurance1(1000, "Aetna", scott);
        edIns = new PCInsurance1(1001, "BlueCross", ed);
        adminIns = new PCInsurance1(2001, "BlueCross", admin);
        scott.setInsurance(scottIns);
        ed.setInsurance(edIns);
        admin.setInsurance(adminIns);

        // Give them some projects to work on.  Scott works on both; Ed and admin
        // only on one.
        solaris = new PCProject1(1L, "Solaris");
        sparc = new PCProject1(2L, "Sparc");

        m = new HashMap();
        m.put(new PCPoint(2, 111), scott);
        m.put(new PCPoint(10, 110), ed);
        adminKey=new PCPoint(99, 999);
        m.put(adminKey, admin);
        solaris.setEmployees(m); // Solaris is worked on by Scott, Ed, and Admin

        m = new HashMap();
        m.put(new PCPoint(1, 112), scott);
        sparc.setEmployees(m); // Sparc is worked on by Scott
        
        l = new LinkedList();
        l.add(solaris);
        l.add(sparc);
        scott.setProjects(l); // Scott works on Solaris and Sparc

        l = new LinkedList();
        l.add(solaris);
        ed.setProjects(l); // Ed works on Solaris
        admin.setProjects(l); // Admin works on Solaris

        // Set managers:
        ed.setManager(scott);
        admin.setManager(ed);

        a = new PCEmployee1[1];
        a[0] = ed;
        scott.setEmployees(a);

        a = new PCEmployee1[1];
        a[0] = admin;
        ed.setEmployees(a);

        // Show what we've got
        if (debug) {
            logger.debug("Before insert: ");
            printObjects();
        }
    }

    protected void insertObjects() {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();

            // Make it all persistent.  If reachability were implemented, we'd
            // only have to make scott and ed persistent, as everything else is
            // reachable from them.
            pm.makePersistent(board);
            pm.makePersistent(emg);
            
            sparc.setName("SPARC");
            
            // Show what we have
            if (debug) {
                logger.debug("\nAfter insert and update: ");
                printObjects();
            }
            
            // Scott's insurance is not reachable any more:
            scott.setInsurance(null);
            
            // Admin and her insurance are not reachable any more:
            emg.getEmployees().remove(admin);
            solaris.getEmployees().remove(adminKey);
            pm.deletePersistent(admin);
            
            // Show what we have now
            if (debug) {
                logger.debug("\nBefore commit: ");
                printObjects();
            }
            
            tx.commit();
            
            scottOid = JDOHelper.getObjectId(scott);
            edOid = JDOHelper.getObjectId(ed);
            
            // Show with what we ended
            if (debug) {
                logger.debug("\nAfter commit: ");
                printObjects();
            }
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    protected void readObjects() {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();

            scott = (PCEmployee1)pm.getObjectById(scottOid, true);
            ed = (PCEmployee1)pm.getObjectById(edOid, true);
            if (debug) {
                logger.debug("\nload scott and ed: ");
                logger.debug(scott.toString());
                logger.debug(ed.toString());
            }

            // check scott
            assertEquals("Wrong string representation of scott", 
                         "Emp: McNealy, Scott, id=1, born 20/Aug/1969, hired 5/Jun/1982 $200000.0 manager: none dept: board emps: 1 insurance: null",
                         scott.toString());
            assertEquals("Wrong string representation of ed", 
                         "Emp: Zander, Ed, id=100, born 8/May/1960, hired 3/Mar/1985 $400000.0 manager: McNealy dept: emg emps: 1 insurance: BlueCross",
                         ed.toString());

            // check departments
            assertEquals("Wrong string representation of board department", 
                         "Dept: board, id=100, emps: 1", 
                         scott.getDepartment().toString()); 
            assertEquals("Wrong string representation of emg department", 
                         "Dept: emg, id=200, emps: 1", 
                         ed.getDepartment().toString()); 

            // check insurances
            assertEquals("Wrong string representation of blue cross insurance", 
                         "Ins: BlueCross, id=1001, emp Zander", 
                         ed.getInsurance().toString());

            // check projects
            assertEquals("Wrong string representation of scotts projects", 
                         "[Project: Solaris, id=1, emps: 2, Project: SPARC, id=2, emps: 1]", 
                         scott.getProjects().toString()); 
            assertEquals("Wrong string representation of scotts projects", 
                         "[Project: Solaris, id=1, emps: 2]", 
                         ed.getProjects().toString()); 
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    private void printObjects() {
        logger.debug(scott.toString());
        logger.debug(ed.toString());
        try {
            logger.debug(admin.toString());
        } catch (Exception e) {
            // Ignore - it was about deleted instance.
        }
        logger.debug(board.toString());
        logger.debug(emg.toString());
        logger.debug(scottIns.toString());
        logger.debug(edIns.toString());
        logger.debug(adminIns.toString());
        logger.debug(solaris.toString());
        logger.debug(sparc.toString());
    }
}
