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
 * PositiveTest.java
 *
 * Created on March 31, 2000
 */

package org.apache.jdo.test.query;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.jdo.impl.fostore.FOStorePMF;
import org.apache.jdo.impl.jdoql.tree.Tree;
import org.apache.jdo.jdoql.tree.QueryTree;

import org.apache.jdo.pc.xempdept.Company;
import org.apache.jdo.pc.xempdept.Department;
import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.pc.xempdept.FullTimeEmployee;
import org.apache.jdo.pc.xempdept.Insurance;
import org.apache.jdo.pc.xempdept.PartTimeEmployee;
import org.apache.jdo.pc.xempdept.PrimitiveTypes;
import org.apache.jdo.pc.xempdept.Project;

/** 
 *
 * @author  Michael Bouschen
 */
public abstract class PositiveTest
    implements QueryTest
{
    /** The persistence manager factory. */
    protected PersistenceManagerFactory pmf;

    /** The persistence manager. */
    protected PersistenceManager pm;

    /** The log stream. */
    protected PrintStream log;

    /** Company candidates */
    protected Collection companyCandidates;

    /** Department candidates */
    protected Collection departmentCandidates;

    /** Employee candidates */
    protected Collection employeeCandidates;

    /** FullTimeEmployee candidates */
    protected Collection fullTimeEmployeeCandidates;

    /** PartTimeEmployee candidates */
    protected Collection partTimeEmployeeCandidates;

    /** Insurance candidates */
    protected Collection insuranceCandidates;

    /** Project candidates */
    protected Collection projectCandidates;

    /** PrimitiveTypes candidates */
    protected Collection primitiveTypesCandidates;

    /** Company extent */
    protected Extent companyExtent;

    /** Department extent */
    protected Extent departmentExtent;

    /** Employee extent */
    protected Extent employeeExtent;

    /** FullTimeEmployee extent */
    protected Extent fullTimeEmployeeExtent;

    /** PartTimeEmployee extent */
    protected Extent partTimeEmployeeExtent;

    /** Insurance extent */
    protected Extent insuranceExtent;

    /** Project extent */
    protected Extent projectExtent;

    /** PrimitiveTypes extent */
    protected Extent primitiveTypesExtent;

    /**
     *
     */
    public PositiveTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        this.pmf = pmf;
        this.pm = pmf.getPersistenceManager();
        this.log = log;
    }
    
    /**
     *
     */
    public void initCandidates(Collection companyCandidates, 
                               Collection departmentCandidates, 
                               Collection employeeCandidates, 
                               Collection fullTimeEmployeeCandidates,
                               Collection partTimeEmployeeCandidates, 
                               Collection insuranceCandidates,
                               Collection projectCandidates, 
                               Collection primitiveTypesCandidates)
    {
        this.companyCandidates = companyCandidates;
        if (companyCandidates == null)
            companyExtent = pm.getExtent(Company.class, true);
        this.departmentCandidates = departmentCandidates;
        if (departmentCandidates == null)
            departmentExtent = pm.getExtent(Department.class, true);
        this.employeeCandidates = employeeCandidates;
        if (employeeCandidates == null)
            employeeExtent = pm.getExtent(Employee.class, true);
        this.fullTimeEmployeeCandidates = fullTimeEmployeeCandidates;
        if (fullTimeEmployeeCandidates == null)
            fullTimeEmployeeExtent = pm.getExtent(FullTimeEmployee.class, true);
        this.partTimeEmployeeCandidates = partTimeEmployeeCandidates;
        if (partTimeEmployeeCandidates == null)
            partTimeEmployeeExtent = pm.getExtent(PartTimeEmployee.class, true);
        this.insuranceCandidates = insuranceCandidates;
        if (insuranceCandidates == null)
            insuranceExtent = pm.getExtent(Insurance.class, true);
        this.projectCandidates = projectCandidates;
        if (projectCandidates == null)
            projectExtent = pm.getExtent(Project.class, true);
        this.primitiveTypesCandidates = primitiveTypesCandidates;
        if (primitiveTypesCandidates == null)
            primitiveTypesExtent = pm.getExtent(PrimitiveTypes.class, true);
    }

    /**
     *
     */
    public Department getDepartmentById(long deptid)
    {
        Iterator i = (departmentCandidates == null) ? 
            departmentExtent.iterator() : departmentCandidates.iterator();
        while (i.hasNext()) {
            Department next = (Department)i.next();
            if (deptid == next.getDeptid())
                return next;
        }
        return null;
    }
    
    /**
     *
     */
    public Employee getEmployeeById(long empid)
    {
        Iterator i = (employeeCandidates == null) ? 
            employeeExtent.iterator() : employeeCandidates.iterator();
        while (i.hasNext()) {
            Employee next = (Employee)i.next();
            if (empid == next.getEmpid())
                return next;
        }
        return null;
    }

    /**
     *
     */
    public FullTimeEmployee getFullTimeEmployeeById(long empid)
    {
        Iterator i = (fullTimeEmployeeCandidates == null) ? 
            fullTimeEmployeeExtent.iterator() : fullTimeEmployeeCandidates.iterator();
        while (i.hasNext()) {
            FullTimeEmployee next = (FullTimeEmployee)i.next();
            if (empid == next.getEmpid())
                return next;
        }
        return null;
    }
    
    /**
     *
     */
    public PartTimeEmployee getPartTimeEmployeeById(long empid)
    {
        Iterator i = (partTimeEmployeeCandidates == null) ? 
            partTimeEmployeeExtent.iterator() : partTimeEmployeeCandidates.iterator();
        while (i.hasNext()) {
            PartTimeEmployee next = (PartTimeEmployee)i.next();
            if (empid == next.getEmpid())
                return next;
        }
        return null;
    }
    
    /**
     *
     */
    public Insurance getInsuranceById(long insid)
    {
        Iterator i =  (insuranceCandidates == null) ? 
            insuranceExtent.iterator() : insuranceCandidates.iterator();
        while (i.hasNext()) {
            Insurance next = (Insurance)i.next();
            if (insid == next.getInsid())
                return next;
        }
        return null;
    }
    
    /**
     *
     */
    public Project getProjectById(long projid)
    {
        Iterator i = (projectCandidates == null) ? 
            projectExtent.iterator() : projectCandidates.iterator();
        while (i.hasNext()) {
            Project next = (Project)i.next();
            if (projid == next.getProjid())
                return next;
        }
        return null;
    }
    
    /**
     *
     */
    public PrimitiveTypes getPrimitiveTypesById(long id)
    {
        Iterator i = (primitiveTypesCandidates == null) ?
            primitiveTypesExtent.iterator() : primitiveTypesCandidates.iterator();
        while (i.hasNext()) {
            PrimitiveTypes next = (PrimitiveTypes)i.next();
            if (id == next.getId())
                return next;
        }
        return null;
    }

    /** */
    protected void setDepartmentCandidates(Query query)
    {
        if (departmentCandidates == null)
            query.setCandidates(departmentExtent);
        else
            query.setCandidates(departmentCandidates);
    }

    /** */
    protected void setEmployeeCandidates(Query query)
    {
        if (employeeCandidates == null)
            query.setCandidates(employeeExtent);
        else
            query.setCandidates(employeeCandidates);
    }

    /** */
    protected void setFullTimeEmployeeCandidates(Query query)
    {
        if (fullTimeEmployeeCandidates == null)
            query.setCandidates(fullTimeEmployeeExtent);
        else
            query.setCandidates(fullTimeEmployeeCandidates);
    }

    /** */
    protected void setPartTimeEmployeeCandidates(Query query)
    {
        if (partTimeEmployeeCandidates == null)
            query.setCandidates(partTimeEmployeeExtent);
        else
            query.setCandidates(partTimeEmployeeCandidates);
    }

    /** */
    protected void setInsuranceCandidates(Query query)
    {
        if (employeeCandidates == null)
            query.setCandidates(insuranceExtent);
        else
            query.setCandidates(insuranceCandidates);
    }

    /** */
    protected void setProjectCandidates(Query query)
    {
        if (employeeCandidates == null)
            query.setCandidates(projectExtent);
        else
            query.setCandidates(projectCandidates);
    }

    /** */
    protected void setPrimitiveTypesCandidates(Query query)
    {
        if (employeeCandidates == null)
            query.setCandidates(primitiveTypesExtent);
        else
            query.setCandidates(primitiveTypesCandidates);
    }

    /**
     * Create a new query tree node
     */
    protected QueryTree newQueryTree()
    {
        if (pmf instanceof FOStorePMF)
            return ((FOStorePMF)pmf).newQueryTree();
        else
            return new Tree();
    }
    
    /**
     * Runs all test method provided by this class.
     */
    public boolean runAll()
    {
        boolean ok = true;
        Method[] methods = this.getClass().getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            Method m = methods[i];
            if (isTestMethod(m))
                if (!runTest(m))
                    ok = false;
        }
        return ok;
    }

    /**
     * Runs all test case specified in tests
     */
    public boolean runTests(Vector tests)
    {
        boolean ok = true;
        for (Iterator i = tests.iterator(); i.hasNext();)
        {
            String methodName = (String)i.next();
            try
            {
                if (isTestMethodName(methodName))
                {
                    Method m = this.getClass().getMethod(methodName, new Class[0]);
                    if (!runTest(m))
                        ok = false;
                }
            }
            catch (NoSuchMethodException ex)
            {
                // this test class does not support the test method => skip
            }
        }
        return ok;
    }

    /**
     * Runs test method spcified as java.lang.reflect.Method argument.
     * Method must be a method taking no arguments.
     */
    protected boolean runTest(Method method)
    {
        String methodName = method.getName();
        try
        {
            method.invoke(this, new Object[0]);
            log.println(methodName + ": OK ");
        }
        catch (InvocationTargetException ex)
        {
            Throwable target = ex.getTargetException();
            log.println(methodName + ": " + target.getMessage());
            log.println("  stack trace:");
            target.printStackTrace(log);
            return false;
        }
        catch (Exception ex)
        {
            log.println(methodName + ": problems invoking test method " + methodName + " " + ex);
            ex.printStackTrace(log);
            return false;
        }
        finally
        {
            // close transaction, if it is left open
            Transaction tx = pm.currentTransaction();
            if (tx.isActive())
                tx.rollback();
        }
        return true;
    }

    /**
     * Checks the specified method being a test method
     */
    protected boolean isTestMethod(Method method)
    {
       return (isTestMethodName(method.getName()) &&
               method.getReturnType().equals(void.class) && 
               method.getParameterTypes().length == 0);
    }
    
    /**
     * Checks the specified argument being the name of a test method
     */
    protected abstract boolean isTestMethodName(String methodName);
    
    /**
     * Compares the query result with the expectd result
     */
    protected void checkQueryResult(Object result, Collection expected)
        throws Exception
    {
        if (result == null)
        {
             throw new Exception("Query returns null");
        }
        if (!(result instanceof Collection))
        {
            throw new Exception("Query result is not a collection: " + result.getClass().getName());
        }
        if (!result.equals(expected))
        {
            String lf = System.getProperty("line.separator");
            throw new Exception("Wrong query result: " + lf + 
                                "query returns: " + result + lf + 
                                "expected result: " + expected);
        }
    }

    /**
     *
     */
    protected List getResultOids(Object result)
        throws Exception
    {
        if (result == null) {
            throw new Exception("Query returns null");
        }
        else if (!(result instanceof Collection)) {
            throw new Exception("Query result is not a collection: " + result.getClass().getName());
        }
        else {
            List oids = new ArrayList();
            for (Iterator i = ((Collection)result).iterator(); i.hasNext(); ) {
                Object next = i.next();
                if (next instanceof org.apache.jdo.pc.xempdept.Identifiable) 
                    oids.add(((org.apache.jdo.pc.xempdept.Identifiable)next).getOid());
                else
                    throw new Exception("Unknown instance in query result: " + next.getClass().getName());
            }
            return oids;
        }
    }
}
