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
 * UnsupportedTest.java
 *
 * Created on April 6, 2000
 */

package org.apache.jdo.test.query;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.pc.xempdept.Department;
import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.pc.xempdept.Project;

/** 
 *
 * @author  Michael Bouschen
 */
public class UnsupportedTest
  extends NegativeTest
{
    public UnsupportedTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
       return methodName.startsWith("unsupported");
    }
    
    // ==========================================================================
    // Test methods
    // ==========================================================================
    /**
     * Testcase: unconstraint variable
     */
    public void unsupported001()
        throws Exception
    {
        String expectedMsg = msg.msg("EXC_UnconstraintVariable", "e"); //NOI18N
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Department.class);
            query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
            query.declareVariables("Employee e");
            query.setFilter("deptid == 1");
            query.compile();
            query.execute();
            checkMissingException(JDOUnsupportedOptionException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOUnsupportedOptionException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: unconstriant variable
     */
    public void unsupported002()
        throws Exception
    {
        String expectedMsg = msg.msg("EXC_UnconstraintVariable", "d");
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery();
            query.setClass(Project.class);
            query.declareVariables("org.apache.jdo.pc.xempdept.Employee e; org.apache.jdo.pc.xempdept.Department d");
            query.setFilter("d.employees.contains(e) & e.department == d");
            query.compile();
            checkMissingException(JDOUnsupportedOptionException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOUnsupportedOptionException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: variable constraint, but not used
     */
    public void unsupported003()
        throws Exception
    {
        String expectedMsg = msg.msg("EXC_UnusedVariable", "e"); //NOI18N
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Department.class);
            query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
            query.declareVariables("Employee e");
            query.setFilter("employees.contains(e)");
            query.compile();
            query.execute();
            checkMissingException(JDOUnsupportedOptionException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOUnsupportedOptionException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: variable constraint, but not used
     */
    public void unsupported004()
        throws Exception
    {
        String expectedMsg = msg.msg("EXC_UnusedVariable", "e"); //NOI18N
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Department.class);
            query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
            query.declareVariables("Employee e");
            query.setFilter("employees.contains(e) | (deptid == 1)");
            query.execute();
            checkMissingException(JDOUnsupportedOptionException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOUnsupportedOptionException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: cyclic variable constraint in one expression
     */
    public void unsupported005()
        throws Exception
    {
        String expectedMsg = 
            msg.msg("EXC_UnsupportedCyclicConstaint", "e"); //NOI18N
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Employee.class);
            query.declareVariables("Employee e");
            query.setFilter("e.team.contains(e)");
            query.setCandidates(pm.getExtent(Employee.class, false));
            query.execute();
            checkMissingException(JDOUnsupportedOptionException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOUnsupportedOptionException.class, expectedMsg);
        }
        
        tx.rollback();
    }

    /**
     * Testcase: cyclic variable constraint using two expressions
     */
    public void unsupported006()
        throws Exception
    {
        String expectedMsg = 
            msg.msg("EXC_UnsupportedCyclicConstaint", "p"); //NOI18N
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        try
        {
            Query query = pm.newQuery(Project.class);
            query.declareVariables("org.apache.jdo.pc.xempdept.Employee e; org.apache.jdo.pc.xempdept.Project p");
            query.setFilter("p.employees.contains(e) & e.projects.contains(p)");
            query.execute();
            checkMissingException(JDOUnsupportedOptionException.class, expectedMsg);
        }
        catch (JDOException ex)
        {
            checkJDOException(ex, JDOUnsupportedOptionException.class, expectedMsg);
        }
        
        tx.rollback();
    }
}
