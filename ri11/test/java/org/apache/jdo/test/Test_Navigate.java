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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;

import org.apache.jdo.pc.PCDepartment;
import org.apache.jdo.pc.PCEmployee;
import org.apache.jdo.pc.PCInsurance;
import org.apache.jdo.pc.PCProject;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Test a PC that has some fields which are PC's.
*/
public class Test_Navigate extends Test_EmpDept {
    
    Map emps = new HashMap();
    Map depts = new HashMap();
    Map inss = new HashMap();
    Map projs = new HashMap();
    
    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_Navigate.class);
    }

    /** */
    protected void readObjects() {
        getEmpExtent();
    }

    /** The above test inserted a bunch of instances and committed them.  We
     * now try to load the extent of Employee and navigate to all associated
     * instances.
     */
    protected void getEmpExtent() {
        PersistenceManager pm = null;
        try {
            pm = pmf.getPersistenceManager();
            if (debug) logger.debug("\ngetEmpExtent");
            Extent extent = pm.getExtent (PCEmployee.class, false);
            Iterator it = extent.iterator();
            while (it.hasNext()) {
                PCEmployee e = (PCEmployee)it.next();
                checkEmployee(e);
            }
            PCEmployee scott = (PCEmployee) emps.get (new Long (1L));
            PCEmployee ed = (PCEmployee) emps.get (new Long (100L));
            PCDepartment board = (PCDepartment) depts.get (new Long (100L));
            PCDepartment emg = (PCDepartment) depts.get (new Long (200L));
            PCInsurance scottIns = (PCInsurance) inss.get (new Long (1000L));
            PCInsurance edIns = (PCInsurance) inss.get (new Long (1001L));
            PCProject solaris = (PCProject) projs.get (new Long (1L));
            PCProject sparc = (PCProject) projs.get (new Long (2L));
            assertScott(scott);
            assertEd(ed);
            assertBoard(board);
            assertEmg(emg);
            assertScottIns(scottIns);
            assertEdIns(edIns);
            assertSolaris(solaris);
            assertSparc(sparc);
        }
        finally {
            if (pm != null && !pm.isClosed())
                pm.close();
        }
    }
    
    /** */
    void checkEmployee (PCEmployee e) {
        emps.put (new Long(e.getEmpid()), e);
        for (Iterator it = e.getProjects().iterator(); it.hasNext();) {
            PCProject proj = (PCProject)it.next();
            checkProject (proj);
        }
        checkDepartment ((PCDepartment) e.getDepartment());
        checkInsurance ((PCInsurance) e.getInsurance());
    }
    
    /** */
    void checkDepartment (PCDepartment d) {
        depts.put (new Long(d.getDeptid()), d);
    }
    
    /** */
    void checkProject (PCProject p) {
        projs.put (new Long(p.getProjid()), p);
    }
    
    /** */
    void checkInsurance (PCInsurance i) {
        inss.put (new Long(i.getInsid()), i);
    }
    
}
