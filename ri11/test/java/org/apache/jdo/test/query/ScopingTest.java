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
 * ScopingTest.java
 *
 * Created on April 12, 2001
 */

package org.apache.jdo.test.query;

import java.util.*;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.pc.xempdept.Department;
import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.pc.xempdept.PrimitiveTypes;

/** 
 *
 * @author  Michael Bouschen
 */
public class ScopingTest
  extends PositiveTest
{
    public ScopingTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
       return methodName.startsWith("scoping");
    }

    // ==========================================================================
    // Test methods
    // ==========================================================================
    
    /**
     * Testcase: use of unqualified field name
     */
    public void scoping001()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("id == 1");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 1;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }
    
    /**
     * Testcase: use of this qualifier
     */
    public void scoping002()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("this.id == 1");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 1;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: parameter with same name as field
     */
    public void scoping003()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("String empid");
        query.setFilter("empid == firstname");
        setEmployeeCandidates(query);
        Object result = query.execute("firstEngTwo");
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: parameter with same name as field, using parameter and field
     */
    public void scoping004()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("String department");
        query.setFilter("this.department.name == department");
        setEmployeeCandidates(query);
        Object result = query.execute("Engineering");
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: variable with same name as field
     */
    public void scoping005()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("Employee manager");
        query.setFilter("team.contains(manager) & manager.empid == 5");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: variable with same name as field, using variable and field
     */
    public void scoping006()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("Employee team");
        query.setFilter("this.team.contains(team) & team.empid == 2");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: using a field having the same name as the candidate class
     */
    public void scoping007()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("PrimitiveTypes == 1");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 1;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: namespaces: using a type name having the same name as a field in declareParameters
     */
    public void scoping008()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        PrimitiveTypes param = getPrimitiveTypesById(1L);

        // create and execute query

        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.declareParameters("PrimitiveTypes p");
        query.setFilter("this == p");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute(param);
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 1;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: namespaces: using a type name having the same name as a field in declareParameters
     */
    public void scoping009()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        PrimitiveTypes param = getPrimitiveTypesById(1L);

        // create and execute query

        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.declareParameters("PrimitiveTypes PrimitiveTypes");
        query.setFilter("this == PrimitiveTypes");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute(param);
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 1;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: namespaces: using a parameter having the same name as the candidate class
     */
    public void scoping010()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("long Employee");
        query.setFilter("empid == Employee");
        setEmployeeCandidates(query);
        Object result = query.execute(new Long(2));
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: namespaces: using a parameter having the same name as the candidate class
     */
    public void scoping011()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Employee manager = getEmployeeById(1L);

        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("Employee Employee");
        query.setFilter("manager == Employee");
        setEmployeeCandidates(query);
        Object result = query.execute(manager);
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: namespaces: using a variable having the same name as the candidate class
     */
    public void scoping012()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("Employee Employee");
        query.setFilter("team.contains(Employee) & Employee.empid == 3");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: namespaces: using a parameter having the same name as an imported type
     */
    public void scoping013()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Department");
        query.declareParameters("long Department");
        query.setFilter("department.deptid == Department");
        setEmployeeCandidates(query);
        Object result = query.execute(new Long(2));
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: namespaces: using a parameter having the same name as an imported type
     */
    public void scoping014()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Department d = getDepartmentById(1L);

        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Department");
        query.declareParameters("Department Department");
        query.setFilter("Department == department");
        setEmployeeCandidates(query);
        Object result = query.execute(d);
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: namespaces: using a variable having the same name as an imported type
     */
    public void scoping015()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Project");
        query.declareVariables("Project Project");
        query.setFilter("projects.contains(Project) & Project.projid == 3");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
}
