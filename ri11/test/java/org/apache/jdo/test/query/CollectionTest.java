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

/*
 * CollectionTest.java
 *
 * Created on April 10, 2000
 */

package org.apache.jdo.test.query;

import java.util.*;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.pc.xempdept.Department;
import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.pc.xempdept.Project;

/** 
 *
 * @author  Michael Bouschen
 */
public class CollectionTest
    extends PositiveTest
{
    public CollectionTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
        return methodName.startsWith("collection");
    }

    // ==========================================================================
    // Test methods
    // ==========================================================================

    /**
     * Testcase: simple collection navigation (1:n)
     */
    public void collection001()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee;");
        query.declareVariables("Employee e");
        query.setFilter("employees.contains(e) & e.firstname == \"firstEngOne\"");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple collection navigation (1:n) + &-clause
     */
    public void collection002()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee;");
        query.declareVariables("Employee e");
        query.setFilter("(employees.contains(e) & e.firstname == \"firstEngOne\") & name == \"Engineering\"");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple collection navigation (1:n) + &-clause (changed order of & operators)
     */
    public void collection003()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee;");
        query.declareVariables("Employee e");
        query.setFilter("employees.contains(e) & (e.firstname == \"firstEngOne\" & name == \"Engineering\")");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple collection navigation (1:n) + &-clause (changed order of & operators)
     */
    public void collection004()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee;");
        query.declareVariables("Employee e");
        query.setFilter("employees.contains(e) & (name == \"Engineering\" & e.firstname == \"firstEngOne\")");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple collection navigation (1:n) + &-clause (changed order of & operators)
     */
    public void collection005()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee;");
        query.declareVariables("Employee e");
        query.setFilter("name == \"Engineering\" & (employees.contains(e) & e.firstname == \"firstEngOne\")");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple collection navigation (1:n) + |-clause
     */
    public void collection006()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee;");
        query.declareVariables("Employee e");
        query.setFilter("employees.contains(e) & e.firstname == \"firstEngOne\" | name == \"Sales\"");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 2;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple collection navigation (1:n) + |-clause (changed order of | operands)
     */
    public void collection007()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee;");
        query.declareVariables("Employee e");
        query.setFilter("name == \"Sales\" | (employees.contains(e) & e.firstname == \"firstEngOne\")");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 2;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple collection navigation (1:n) + simple reference navigation
     */
    public void collection008()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee;");
        query.declareVariables("Employee e");
        query.setFilter("employees.contains(e) & e.insurance.carrier == \"Carrier One\"");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple collection navigation (n:m)
     */
    public void collection009()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Project.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee;");
        query.declareVariables("Employee e");
        query.setFilter("employees.contains(e) & e.firstname == \"firstEngOne\"");
        setProjectCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Project.Oid key = new Project.Oid();
        key.projid = 1;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 2;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 3;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple collection navigation (n:m) + simple reference navigation
     * checks bug report 4339510
     */
    public void collection010()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Project.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee;");
        query.declareVariables("Employee e");
        query.setFilter("employees.contains(e) & e.insurance.carrier == \"Carrier One\"");
        setProjectCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Project.Oid key = new Project.Oid();
        key.projid = 1;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 2;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: simple collection navigation + 
     *   navigating a reference relationship from the variable twice inside an or expression
     */
    public void collection011()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareVariables("org.apache.jdo.pc.xempdept.Employee e");
        query.setFilter("employees.contains(e) & " + 
                        "(e.insurance.carrier == \"Carrier Three\" | e.insurance.carrier == \"Carrier Seven\")");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: collection navigation + 
     *   navigate a reference relationship to get the collection +
     *   navigating a reference relationship from the variable twice inside an or expression
     */
    public void collection012()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("org.apache.jdo.pc.xempdept.Employee e");
        query.setFilter("manager.team.contains(e) & " + 
                        "(e.department.deptid == 1 || e.department.deptid == 2)");
        setEmployeeCandidates(query);
        Object result = query.execute();
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
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: variable field access
     */
    public void collection013()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Project p = new Project();
        p.setProjid(1);
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Project");
        query.declareParameters("Project p");
        query.declareVariables("Project project");
        query.setFilter("projects.contains(project) & project.projid == p.projid");
        setEmployeeCandidates(query);
        Object result = query.execute(p);
        List oids = getResultOids(result);
        Collections.sort(oids);

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
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }
    
    /**
     * Testcase: object comparison
     *   variable == param 
     *   navigating join table relationship
     */
    public void collection014()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Project p = getProjectById(1L);
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Project");
        query.declareParameters("Project p");
        query.declareVariables("Project project");
        query.setFilter("projects.contains(project) & project == p");
        setEmployeeCandidates(query);
        Object result = query.execute(p);
        List oids = getResultOids(result);
        Collections.sort(oids);

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
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   param == variable 
     *   navigating join table relationship
     */
    public void collection015()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Project p = getProjectById(1L);
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Project");
        query.declareParameters("Project p");
        query.declareVariables("Project project");
        query.setFilter("projects.contains(project) & p == project");
        setEmployeeCandidates(query);
        Object result = query.execute(p);
        List oids = getResultOids(result);
        Collections.sort(oids);

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
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   variable != param
     *   navigating join table relationship
     */
    public void collection016()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Project p = getProjectById(1L);
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Project");
        query.declareParameters("Project p");
        query.declareVariables("Project project");
        query.setFilter("projects.contains(project) & project != p");
        setEmployeeCandidates(query);
        Object result = query.execute(p);
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   param != variable
     *   navigating join table relationship
     */
    public void collection017()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Project p = getProjectById(1L);
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Project");
        query.declareParameters("Project p");
        query.declareVariables("Project project");
        query.setFilter("projects.contains(project) & p != project");
        setEmployeeCandidates(query);
        Object result = query.execute(p);
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   variable == param 
     *   navigating collection side of 1:n relationship
     */
    public void collection018()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Employee e = getEmployeeById(8L);
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
        query.declareParameters("Employee e");
        query.declareVariables("Employee employee");
        query.setFilter("employees.contains(employee) & employee == e");
        setDepartmentCandidates(query);
        Object result = query.execute(e);
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 4;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   param == variable
     *   navigating collection side of 1:n relationship
     */
    public void collection019()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Employee e = getEmployeeById(8L);
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
        query.declareParameters("Employee e");
        query.declareVariables("Employee employee");
        query.setFilter("employees.contains(employee) & e == employee");
        setDepartmentCandidates(query);
        Object result = query.execute(e);
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 4;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   variable != param 
     *   navigating collection side of 1:n relationship
     */
    public void collection020()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Employee e = getEmployeeById(8L);
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
        query.declareParameters("Employee e");
        query.declareVariables("Employee employee");
        query.setFilter("employees.contains(employee) & employee != e");
        setDepartmentCandidates(query);
        Object result = query.execute(e);
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 2;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 3;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 4;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   param != variable
     *   navigating collection side of 1:n relationship
     */
    public void collection021()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Employee e = getEmployeeById(8L);
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
        query.declareParameters("Employee e");
        query.declareVariables("Employee employee");
        query.setFilter("employees.contains(employee) & e != employee");
        setDepartmentCandidates(query);
        Object result = query.execute(e);
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 2;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 3;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 4;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   variable == param 
     *   navigating collection side of 1:n self referencing relationship
     */
    public void collection022()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Employee e = getEmployeeById(2L);
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("Employee e");
        query.declareVariables("Employee employee");
        query.setFilter("team.contains(employee) & employee == e");
        setEmployeeCandidates(query);
        Object result = query.execute(e);
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   variable != param 
     *   navigating collection side of 1:n self referencing relationship
     */
    public void collection023()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        Employee e = getEmployeeById(5L);
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("Employee e");
        query.declareVariables("Employee employee");
        query.setFilter("team.contains(employee) & employee != e");
        setEmployeeCandidates(query);
        Object result = query.execute(e);
        List oids = getResultOids(result);
        Collections.sort(oids);

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   variable == this
     */
    public void collection024()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("org.apache.jdo.pc.xempdept.Employee e");
        query.setFilter("team.contains(e) & e == this");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   variable == this
     *   plus reference navigation to get to the collection
     */
    public void collection025()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("org.apache.jdo.pc.xempdept.Employee e");
        query.setFilter("manager.team.contains(e) & e == this");
        setEmployeeCandidates(query);
        Object result = query.execute();
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
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

     /**
     * Testcase: object comparison
     *   this == variable
     *   plus reference navigation to get to the collection
     */
    public void collection026()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("org.apache.jdo.pc.xempdept.Employee e");
        query.setFilter("manager.team.contains(e) & this == e");
        setEmployeeCandidates(query);
        Object result = query.execute();
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
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   variable == variable
     *   one contains clause uses navigation to get to the collection
     */
    public void collection027()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        // This query returns employees having an employee in its team woking in the same department
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("org.apache.jdo.pc.xempdept.Employee e1; org.apache.jdo.pc.xempdept.Employee e2");
        query.setFilter("department.employees.contains(e1) & (team.contains(e2) & e1 == e2)");
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
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   variable == variable
     *   both contains clauses use navigation to get to the collection
     */
    public void collection028()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        // this query returns employees having a colleague (member of the same team) 
        // working in the same department.
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("org.apache.jdo.pc.xempdept.Employee e1; org.apache.jdo.pc.xempdept.Employee e2");
        query.setFilter("department.employees.contains(e1) & (manager.team.contains(e2) & e1 == e2)");
        setEmployeeCandidates(query);
        Object result = query.execute();
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
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: nested collection navigation
     * checks bug report 4423803 
     */
    public void collection029()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareVariables("org.apache.jdo.pc.xempdept.Employee e; org.apache.jdo.pc.xempdept.Project p");
        query.setFilter("employees.contains(e) & (e.projects.contains(p) & p.projid == 1)");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 2;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: nested collection navigation + simple condition for each variable
     * checks bug report 4423803 
     */
    public void collection030()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareVariables("org.apache.jdo.pc.xempdept.Employee e; org.apache.jdo.pc.xempdept.Project p");
        query.setFilter("employees.contains(e) & (e.empid == 1 & (e.projects.contains(p) & p.projid == 1))");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: nested collection navigation + simple condition for each variable
     * (changed order of conditions)
     * checks bug report 4423803 
     */
    public void collection031()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareVariables("org.apache.jdo.pc.xempdept.Employee e; org.apache.jdo.pc.xempdept.Project p");
        query.setFilter("employees.contains(e) & ((e.projects.contains(p) & p.projid == 1) & e.empid == 1)");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: nested collection navigation 
     * checks bug report 4423803 
     */
    public void collection032()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareVariables("Employee deptEmp; Project p; Employee teamMember");
        query.setFilter("employees.contains(deptEmp) & " + 
                        "(deptEmp.projects.contains(p) & (p.projid == 1 & " + 
                        "(deptEmp.team.contains(teamMember) & teamMember.empid == 2)))");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: nested collection navigation 
     * checks bug report 4423803 
     */
    public void collection033()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.declareVariables("Employee deptEmp; Project p; Employee projEmp");
        query.setFilter("employees.contains(deptEmp) & (deptEmp.projects.contains(p) & " + 
                        "(p.employees.contains(projEmp) & projEmp.empid == 1))");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Department.Oid key = new Department.Oid();
        key.deptid = 1;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 2;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: collection comparison
     * collection == null
     */
    public void collection034()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("team == null");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);
       
        tx.commit();
    }

    /**
     * Testcase: collection comparison
     * null == collection
     */
    public void collection035()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Project.class);
        query.setFilter("null == employees");
        setProjectCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Project.Oid key = new Project.Oid();
        key.projid = 4;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: collection comparison
     * collection != null
     */
    public void collection036()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("team != null");
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
        key.empid = 2;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 4;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: collection comparison
     * null != collection
     */
    public void collection037()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Project.class);
        query.setFilter("null != employees");
        setProjectCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Project.Oid key = new Project.Oid();
        key.projid = 1;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 2;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 3;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 11;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: isEmpty 1:n relationship
     */
    public void collection038()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("team.isEmpty()");
        setEmployeeCandidates(query);
        Object result = query.execute();
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
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: !isEmpty 1:n relationship
     */
    public void collection039()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("!team.isEmpty()");
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
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: isEmpty n:m relationship
     */
    public void collection040()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Project.class);
        query.setFilter("employees.isEmpty()");
        setProjectCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Project.Oid key = new Project.Oid();
        key.projid = 4;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }
    
    /**
     * Testcase: !isEmpty n:m relationship
     */
    public void collection041()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Project.class);
        query.setFilter("!employees.isEmpty()");
        setProjectCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Project.Oid key = new Project.Oid();
        key.projid = 1;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 2;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 3;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 11;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigating different relationships between the same classes (1:n)
     */
    public void collection042()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("Employee e1; Employee e2");
        query.setFilter("(team.contains(e1) & e1.empid == 9) & (hradvisees.contains(e2) & e2.empid == 2)");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigating different relationships between the same classes (1:n)
     */
    public void collection043()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("Employee e1; Employee e2");
        query.setFilter("team.contains(e1) & (e1.empid == 9 & (hradvisees.contains(e2) & e2.empid == 2))");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigating different relationships between the same classes (1:n)
     */
    public void collection044()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("Employee e1; Employee e2");
        query.setFilter("(team.contains(e1) & e1.empid == 9) && (hradvisees.contains(e2) & e2.empid == 2)");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigating different relationships between the same classes (1:n)
     */
    public void collection045()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareVariables("Employee e1; Employee e2");
        query.setFilter("team.contains(e1) & (e1.empid == 9 && (hradvisees.contains(e2) & e2.empid == 2))");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigating different relationships between the same classes (1:n)
     */
    public void collection046()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Project.class);
        query.declareVariables("org.apache.jdo.pc.xempdept.Employee e1; org.apache.jdo.pc.xempdept.Employee e2");
        query.setFilter("(employees.contains(e1) & e1.empid == 1) & (reviewers.contains(e2) & e2.empid == 4)");
        setProjectCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Project.Oid key = new Project.Oid();
        key.projid = 1;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 2;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: navigating different relationships between the same classes (1:n)
     */
    public void collection047()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Project.class);
        query.declareVariables("org.apache.jdo.pc.xempdept.Employee e1; org.apache.jdo.pc.xempdept.Employee e2");
        query.setFilter("employees.contains(e1) & e1.empid == 1 & reviewers.contains(e2) & e2.empid == 4");
        setProjectCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Project.Oid key = new Project.Oid();
        key.projid = 1;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 2;
        expected.add(key);
        key = new Project.Oid();
        key.projid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    

}
