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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import org.apache.jdo.pc.empdept.PCDepartment;
import org.apache.jdo.pc.empdept.PCEmployee;
import org.apache.jdo.pc.empdept.PCFullTimeEmployee;
import org.apache.jdo.pc.empdept.PCInsurance;
import org.apache.jdo.pc.empdept.PCPartTimeEmployee;
import org.apache.jdo.pc.empdept.PCProject;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Test a PC that has some fields which are PC's.
*/
public class Test_Inheritance extends EmpDeptSupport {
    
    Map emps = new HashMap();
    Map depts = new HashMap();
    Map inss = new HashMap();
    Map projs = new HashMap();
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Inheritance.class);
    }

    // The idea is that we're going to write a bunch of stuff to a data
    // output stream, then read it back in; we should get the same data
    // back.
    public void test() throws Exception 
    {
        insertObjects();
        getEmpExtent();
    }
    
    // The above test inserted a bunch of instances and committed them.  We
    // now try to load the extent of Employee and navigate to all associated
    // instances.
    protected void getEmpExtent() {
        PersistenceManager pm = null;
        try {
            if (debug) logger.debug("\ngetEmpExtent");
            pm = pmf.getPersistenceManager();
            Extent extent;
            Iterator it;
            extent = pm.getExtent (PCEmployee.class, true);
            it = extent.iterator();

            // Put the printed version of each emp in the extent into a SortedSet
            TreeSet sortedEmps = new TreeSet();

            while (it.hasNext()) {
                PCEmployee e = (PCEmployee)it.next();
                sortedEmps.add("from extent of PCEmployee: " + e);
                checkEmployee(e);
            }

            // Print out the extent in sorted order
            if (debug)
                for (Iterator i = sortedEmps.iterator(); i.hasNext();) {
                    logger.debug((String)i.next());
                }

            PCFullTimeEmployee scott = (PCFullTimeEmployee) emps.get (new Long (1L));
            PCPartTimeEmployee ed = (PCPartTimeEmployee) emps.get (new Long (100L));
            PCDepartment board = (PCDepartment) depts.get (new Long (100L));
            PCDepartment emg = (PCDepartment) depts.get (new Long (200L));
            PCInsurance scottIns = (PCInsurance) inss.get (new Long (1000L));
            PCInsurance edIns = (PCInsurance) inss.get (new Long (1001L));
            PCProject solaris = (PCProject) projs.get (new Long (1L));
            PCProject sparc = (PCProject) projs.get (new Long (2L));

            String repr = null;
            repr = scott.toString();
            if (debug) logger.debug(repr);
            assertEquals("Wrong string representation of scott", 
                         "PCFullTimeEmployee: PCEmployee: PCPerson: McNealy, Scott, born 20/Aug/1969, id=1, hired 5/Jun/1982 manager: none dept: board emps: 1 insurance: Aetna $200000.0",
                         repr); 
            repr = ed.toString();
            if (debug) logger.debug(repr);
            assertEquals("Wrong string representation of ed",
                         "PCPartTimeEmployee: PCEmployee: PCPerson: Zander, Ed, born 8/May/1960, id=100, hired 3/Mar/1985 manager: McNealy dept: emg emps: 0 insurance: BlueCross $400000.0",
                         repr);
            repr = board.toString();
            if (debug) logger.debug(repr);
            assertEquals("Wrong string representation of board", 
                         "Dept: board, id=100, emps: 1", 
                         repr);
            repr = emg.toString();
            if (debug) logger.debug(repr);
            assertEquals("Wrong string representation of emg", 
                         "Dept: emg, id=200, emps: 1", 
                         repr);
            repr = scottIns.toString();
            if (debug) logger.debug(repr);
            assertEquals("Wrong string representation of scotts insurance", 
                         "Ins: Aetna, id=1000, emp McNealy", 
                         repr);
            repr = edIns.toString();
            if (debug) logger.debug(repr);
            assertEquals("Wrong string representation of eds insurance", 
                         "Ins: BlueCross, id=1001, emp Zander", 
                         repr);
            repr = solaris.toString();
            if (debug) logger.debug(repr);
            assertEquals("Wrong string representation of project solaris", 
                         "PCProject: Solaris, id=1, emps: 2", 
                         repr);
            repr = sparc.toString();
            if (debug) logger.debug(repr);
            assertEquals("Wrong string representation of project sparc", 
                         "PCProject: Sparc, id=2, emps: 1", 
                         repr);
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    protected void checkEmployee (PCEmployee e) {
        emps.put (new Long(e.getEmpid()), e);
        for (Iterator it = e.getProjects().iterator(); it.hasNext();) {
            PCProject proj = (PCProject)it.next();
            checkProject (proj);
        }
        checkDepartment ((PCDepartment) e.getDepartment());
        checkInsurance ((PCInsurance) e.getInsurance());
    }
    
    protected void checkDepartment (PCDepartment d) {
        depts.put (new Long(d.getDeptid()), d);
    }
    
    protected void checkProject (PCProject p) {
        projs.put (new Long(p.getProjid()), p);
    }
    
    protected void checkInsurance (PCInsurance i) {
        inss.put (new Long(i.getInsid()), i);
    }
    
}
