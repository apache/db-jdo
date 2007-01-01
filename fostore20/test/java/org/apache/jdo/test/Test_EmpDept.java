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

import java.util.*;

import javax.jdo.*;

import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

import org.apache.jdo.pc.PCDepartment;
import org.apache.jdo.pc.PCEmployee;
import org.apache.jdo.pc.PCInsurance;
import org.apache.jdo.pc.PCProject;

/**
* Test a PC that has some fields which are PC's.
*
* @author Dave Bristor
*/
public class Test_EmpDept extends AbstractTest {
    PCEmployee scott;
    PCEmployee ed;

    PCDepartment board;
    PCDepartment emg;

    PCInsurance scottIns;
    PCInsurance edIns;

    PCProject solaris;
    PCProject sparc;

    Object scottOid;
    Object edOid;
    Object boardOid;
    Object emgOid;
    Object scottInsOid;
    Object edInsOid;
    Object solarisOid;
    Object sparcOid;

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_EmpDept.class);
    }

    /** */
    public void test() throws Exception {
        createObjects();
        insertObjects();
        readObjects();
    }

    // We override this from Test_ActivateClass and insert our own objects.
    // Use Test_Extent to read them back.
    protected void createObjects() {
        HashSet h;
        
        // Create and set up employees.  Scott is Ed's manager.  Ed is
        // Scott's sole employee.
        //
        GregorianCalendar born =
            new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
        GregorianCalendar hired =
            new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));

        born.set(1969, 7, 20);
        hired.set(1982, 5, 5);
        scott = new PCEmployee(1L, "McNealy", "Scott", 200000.0,
                               born.getTime(), hired.getTime());
        born.set(1960, 4, 8);
        hired.set(1985, 2, 3);
        ed = new PCEmployee(100L, "Zander", "Ed", 400000.0,
                            born.getTime(), hired.getTime());
                           
        ed.setManager(scott);

        h = new HashSet();
        h.add(ed);
        scott.setEmployees(h);

        // Set up their departments.
        board = new PCDepartment(100L, "board");
        h = new HashSet();
        h.add(scott);
        board.setEmployees(h);
        scott.setDepartment(board);

        emg = new PCDepartment(200L, "emg");
        h = new HashSet();
        h.add(ed);
        emg.setEmployees(h);
        ed.setDepartment(emg);

        // Insure these guys
        scottIns = new PCInsurance(1000, "Aetna", scott);
        edIns = new PCInsurance(1001, "BlueCross", ed);
        scott.setInsurance(scottIns);
        ed.setInsurance(edIns);

        // Give them some projects to work on.  Scott works on both; Ed only
        // on one.
        solaris = new PCProject(1L, "Solaris");
        sparc = new PCProject(2L, "Sparc");
        h = new HashSet();
        h.add(scott);
        h.add(ed);
        solaris.setEmployees(h); // Solaris is worked on by Scott and Ed

        h = new HashSet();
        h.add(scott);
        sparc.setEmployees(h); // Sparc is worked on by Scott
        
        h = new HashSet();
        h.add(solaris);
        h.add(sparc);
        scott.setProjects(h); // Scott works on Solaris and Sparc

        h = new HashSet();
        h.add(solaris);
        ed.setProjects(h); // Ed works on Solaris

        // Show what we've got
        if (debug) {
            logger.debug("Before insert: ");
            logger.debug(scott.toString());
            logger.debug(ed.toString());
            logger.debug(board.toString());
            logger.debug(emg.toString());
            logger.debug(scottIns.toString());
            logger.debug(edIns.toString());
            logger.debug(solaris.toString());
            logger.debug(sparc.toString());
        }
    }

    protected void insertObjects() throws Exception {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();

            // Make it all persistent.  If reachability were implemented, we'd
            // only have to make scott and ed persistent, as everything else is
            // reachable from them.
            pm.makePersistent(scott);
            pm.makePersistent(ed);
            pm.makePersistent(board);
            pm.makePersistent(emg);
            pm.makePersistent(scottIns);
            pm.makePersistent(edIns);
            pm.makePersistent(solaris);
            pm.makePersistent(sparc);
        
            tx.commit();

            scottOid = JDOHelper.getObjectId(scott);
            edOid = JDOHelper.getObjectId(ed);
            boardOid = JDOHelper.getObjectId(board);
            emgOid = JDOHelper.getObjectId(emg);
            scottInsOid = JDOHelper.getObjectId(scottIns);
            edInsOid = JDOHelper.getObjectId(edIns);
            solarisOid = JDOHelper.getObjectId(solaris);
            sparcOid = JDOHelper.getObjectId(sparc);
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }

    protected void readObjects() throws Exception {
        PersistenceManager pm = null;
        Transaction tx = null;
        try {
            pm = pmf.getPersistenceManager();
            tx = pm.currentTransaction();
            tx.begin();
            if (debug) logger.debug("readObjects: ");
            assertScott(pm.getObjectById(scottOid, true));
            assertEd(pm.getObjectById(edOid, true));
            assertBoard(pm.getObjectById(boardOid, true));
            assertEmg(pm.getObjectById(emgOid, true));
            assertScottIns(pm.getObjectById(scottInsOid, true));
            assertEdIns(pm.getObjectById(edInsOid, true));
            assertSolaris(pm.getObjectById(solarisOid, true));
            assertSparc(pm.getObjectById(sparcOid, true));
            tx.rollback();
        }
        finally {
            if (tx != null && tx.isActive())
                tx.rollback();
            if (pm != null && !pm.isClosed())
                pm.close();
        }
        
    }
    
    protected void assertScott(Object scott) {
        assertNotNull("scott should not be null", scott); 
        if (debug) logger.debug(scott.toString());
        assertTrue("scott is expected to be a PCEmployee", 
                   (scott instanceof PCEmployee));
        assertEquals("Wrong string representation of scott", 
                     "Emp: McNealy, Scott, id=1, born 20/Aug/1969, hired 5/Jun/1982 $200000.0 manager: none dept: board emps: 1 insurance: Aetna", 
                     scott.toString());
    }

    protected void assertEd(Object ed) {
        assertNotNull("ed should not be null", ed); 
        if (debug) logger.debug(ed.toString());
        assertTrue("ed is expected to be a PCEmployee", 
                   (ed instanceof PCEmployee));
        assertEquals("Wrong string representation of ed",
                     "Emp: Zander, Ed, id=100, born 8/May/1960, hired 3/Mar/1985 $400000.0 manager: McNealy dept: emg emps: 0 insurance: BlueCross",
                     ed.toString());
    }

    protected void assertBoard(Object board) {
        assertNotNull("board should not be null", board); 
        if (debug) logger.debug(board.toString());
        assertTrue("board is expected to be a PCDepartment", 
                   (board instanceof PCDepartment));
        assertEquals("Wrong string representation of board",
                     "Dept: board, id=100, emps: 1",
                     board.toString());
    }

    protected void assertEmg(Object emg) {
        assertNotNull("emg should not be null", emg); 
        if (debug) logger.debug(emg.toString());
        assertTrue("emg is expected to be a PCDepartment", 
                   (emg instanceof PCDepartment));
        assertEquals("Wrong string representation of emg",
                     "Dept: emg, id=200, emps: 1",
                     emg.toString());
    }

    protected void assertScottIns(Object scottIns) {
        assertNotNull("scottIns should not be null", scottIns); 
        if (debug) logger.debug(scottIns.toString());
        assertTrue("scottIns is expected to be a PCInsurance", 
                   (scottIns instanceof PCInsurance));
        assertEquals("Wrong string representation of scotts insurance",
                     "Ins: Aetna, id=1000, emp McNealy",
                     scottIns.toString());
    }

    protected void assertEdIns(Object edIns) {
        assertNotNull("edIns should not be null", edIns); 
        if (debug) logger.debug(edIns.toString());
        assertTrue("edIns is expected to be a PCInsurance", 
                   (edIns instanceof PCInsurance));
        assertEquals("Wrong string representation of eds insurance",
                     "Ins: BlueCross, id=1001, emp Zander",
                     edIns.toString());
    }

    protected void assertSolaris(Object solaris) {
        assertNotNull("solaris should not be null", solaris); 
        if (debug) logger.debug(solaris.toString());
        assertTrue("solaris is expected to be a PCProject", 
                   (solaris instanceof PCProject));
        assertEquals("Wrong string representation of project solaris",
                     "PCProject: Solaris, id=1, emps: 2",
                     solaris.toString());
    }

    protected void assertSparc(Object sparc) {
        assertNotNull("sparc should not be null", sparc); 
        if (debug) logger.debug(sparc.toString());
        assertTrue("sparc is expected to be a PCProject", 
                   (sparc instanceof PCProject));
        assertEquals("Wrong string representation of project sparc",
                     "PCProject: Sparc, id=2, emps: 1",
                     sparc.toString());
    }
    
}

