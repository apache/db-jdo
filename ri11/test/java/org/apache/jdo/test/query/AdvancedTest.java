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
 * AdvancedTest.java
 *
 * Created on April 10, 2000
 */

package org.apache.jdo.test.query;

import java.util.*;
import java.io.*;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.pc.xempdept.Department;
import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.pc.xempdept.PrimitiveTypes;
import org.apache.jdo.pc.xempdept.Project;

/** 
 *
 * @author  Michael Bouschen
 */
public class AdvancedTest
    extends PositiveTest
{
    public AdvancedTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
       return methodName.startsWith("advanced");
    }

    // ==========================================================================
    // Test methods
    // ==========================================================================

    /**
     * Testcase: check for null values ==
     */
    public void advanced001()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("firstname == null");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: check for null values !=
     */
    public void advanced002()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("firstname != null");
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
     * Testcase: serialize/deserialize non compiled query
     */
    public void advanced003()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid == 3");
        setEmployeeCandidates(query);
       
        // serialize query object
        ByteArrayOutputStream bout = new ByteArrayOutputStream ();
        ObjectOutputStream oout = new ObjectOutputStream (bout);
        oout.writeObject (query);
        oout.flush ();
        byte[] bytes = bout.toByteArray();
        oout.close ();

        // deserialize query object
        ByteArrayInputStream bin = new ByteArrayInputStream (bytes);
        ObjectInputStream oin = new ObjectInputStream (bin);
        query = (Query)oin.readObject ();
        oin.close ();

        // init and execute query
        query = pm.newQuery(query);
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: serialize/deserialize compiled query
     * checks bug report 4518967
     */
    public void advanced004()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid == 3");
        setEmployeeCandidates(query);
        query.compile();
       
        // serialize query object
        ByteArrayOutputStream bout = new ByteArrayOutputStream ();
        ObjectOutputStream oout = new ObjectOutputStream (bout);
        oout.writeObject (query);
        oout.flush ();
        byte[] bytes = bout.toByteArray();
        oout.close ();

        // deserialize query object
        ByteArrayInputStream bin = new ByteArrayInputStream (bytes);
        ObjectInputStream oin = new ObjectInputStream (bin);
        query = (Query)oin.readObject ();
        oin.close ();

        // init and execute query
        query = pm.newQuery(query);
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);
        
        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 3;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: serialize/deserialize compiled query 
     * (using more compilated query)
     * checks bug report 4518967
     */
    public void advanced005()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("Project p");
        query.declareVariables("Project project");
        query.setFilter("projects.contains(project) & project.projid == p.projid");
        setEmployeeCandidates(query);
        query.compile();
       
        // serialize query object
        ByteArrayOutputStream bout = new ByteArrayOutputStream ();
        ObjectOutputStream oout = new ObjectOutputStream (bout);
        oout.writeObject (query);
        oout.flush ();
        byte[] bytes = bout.toByteArray();
        oout.close ();

        // deserialize query object
        ByteArrayInputStream bin = new ByteArrayInputStream (bytes);
        ObjectInputStream oin = new ObjectInputStream (bin);
        query = (Query)oin.readObject ();
        oin.close ();

        // init and execute query
        Project p = getProjectById(1L);
        
        query = pm.newQuery(query);
        setEmployeeCandidates(query);
        Object result = query.execute(p);
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
        key.empid = 6;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: re-execute the same query w/o recompile
     * checks bug report 4349720
     */
    public void advanced006()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid == 6");;
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        checkQueryResult(oids, expected);

        // rexecute the query

        result = query.execute();
        oids = getResultOids(result);

        // check new query result

        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: use ESCuxxxx chars in string literal
     */
    public void advanced007()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("firstname == \"\u0066irstEngOne\"");;
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
        key.empid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
 
    /**
     * Testcase: use ESCxxx chars in string literal
     */
    public void advanced008()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("firstname == \"\146irstEngOne\"");;
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
        key.empid = 11;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: wildcard queries startsWith
     */
    public void advanced009()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("firstname.startsWith(\"firstEng\")");
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
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
 
    /**
     * Testcase: wildcard queries endsWith
     */
    public void advanced010()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("firstname.endsWith(\"e\")");
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
        key.empid = 3;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 13;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
 
    /**
     * Testcase: wildcard queries startsWith with non constant arguments
     */
    public void advanced011()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("firstname.startsWith(\"first\" + department.name)");
        setEmployeeCandidates(query);
        Object result = query.execute();
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
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 7;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
 
    /**
     * Testcase: wildcard queries endsWith with non constant arguments
     */
    public void advanced012()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("firstname.endsWith(department.name + \"Five\")");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 5;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
 
    /**
     * Testcase: wildcard queries startsWith with simple navigation
     * checks bug report 4418353
     */
    public void advanced015()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department.name.startsWith(\"Eng\")");
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
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
 
    /**
     * Testcase: wildcard queries endsWith with simple navigation
     * checks bug report 4418353
     */
    public void advanced016()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("department.name.endsWith(\"ing\")");
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
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: wildcard queries startsWith with collection navigation
     * checks bug report 4418353
     */
    public void advanced017()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Project");
        query.declareVariables("Project p");
        query.setFilter("projects.contains(p) & p.name.startsWith(\"E\")");
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
        key.empid = 6;
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
     * Testcase: wildcard queries endsWith with collection navigation
     * checks bug report 4418353
     */
    public void advanced018()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Project");
        query.declareVariables("Project p");
        query.setFilter("projects.contains(p) & p.name.endsWith(\"g Project\")");
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
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: no filter specified
     */
    public void advanced019()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
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
        key = new Department.Oid();
        key.deptid = 4;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 12;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: filter specified as empty string
     */
    public void advanced020()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.setFilter("  ");
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
        key = new Department.Oid();
        key.deptid = 4;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 12;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: optimize boolean constant == boolean constant
     */
    public void advanced021()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("true == !true");
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
     * Testcase: optimize boolean constant != boolean constant
     */
    public void advanced022()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.setFilter("true != (employees == null)");
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
     * Testcase: optimize boolean constant == expr
     */
    public void advanced023()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("true == (empid == 1)");
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
     * Testcase: optimize expr == boolean constant
     * checks bug report 4444393
     */
    public void advanced024()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.setFilter("employees.isEmpty() == false");
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
     * Testcase: optimize boolean constant != expr
     */
    public void advanced025()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("false != firstname.startsWith(\"firstEng\")");
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
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: optimize expr != boolean constant
     */
    public void advanced026()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.setFilter("employees.isEmpty() != true");
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
     * Testcase: optimize expr && true
     */
    public void advanced027()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("(empid == 1) && true");
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
     * Testcase: optimize expr & false
     */
    public void advanced028()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("lastname.endsWith(\"bla\") & false");
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
     * Testcase: true & expr
     */
    public void advanced029()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("true & team.isEmpty()");
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
     * Testcase: false && expr
     */
    public void advanced030()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("(team == null) && (empid == 1)");
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
     * Testcase: optimize expr | true
     */
    public void advanced031()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.setFilter("!employees.isEmpty() | true");
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
        key = new Department.Oid();
        key.deptid = 4;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 12;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
    
    /**
     * Testcase: optimize expr || false
     */
    public void advanced032()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("(empid == 1) || false");
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
     * Testcase: true || expr
     */
    public void advanced033()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("true || firstname.startsWith(\"bla\")");
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
     * Testcase: false | expr
     */
    public void advanced034()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("false | (empid == 2)");
        setEmployeeCandidates(query);
        Object result = query.execute();
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
     * Testcase: !true
     */
    public void advanced035()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("!true");
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
     * Testcase: invert inner equality comparison, 
     * because of outer equality comparison with constant
     */
    public void advanced036()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("(empid != 1) == false");
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
     * Testcase: invert inner logical not, 
     * because of outer equality comparison with constant
     */
    public void advanced037()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("!team.isEmpty() == false");
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
     * Testcase: multiple boolean constant expressions
     */
    public void advanced038()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter(" ((empid == 1) && !(false)) | (false && true) || ((empid == 2) == true)");
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
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: optimize constant == constant for non boolean constants
     * checks bug report 4481691
     */
    public void advanced039()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.setFilter("1 == 1");
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
        key = new Department.Oid();
        key.deptid = 4;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 11;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 12;
        expected.add(key);
        key = new Department.Oid();
        key.deptid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: optimize constant != constant for non boolean constants 
     * checks bug report 4481691
     */
    public void advanced040()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Department.class);
        query.setFilter("1 != 1");
        setDepartmentCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        checkQueryResult(oids, expected);

        tx.commit();
    }


    /**
     * Testcase: Method getPersistenceManager should return null 
     * if called for a deserialized, unbound query object.
     * checks bug report 4547932
     */
    public void advanced041()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("empid == 3");
        
        // serialize query object
        ByteArrayOutputStream bout = new ByteArrayOutputStream ();
        ObjectOutputStream oout = new ObjectOutputStream (bout);
        oout.writeObject (query);
        oout.flush ();
        byte[] bytes = bout.toByteArray();
        oout.close ();

        // deserialize query object
        ByteArrayInputStream bin = new ByteArrayInputStream (bytes);
        ObjectInputStream oin = new ObjectInputStream (bin);
        query = (Query)oin.readObject ();
        oin.close ();

        PersistenceManager otherPM = query.getPersistenceManager();
        if (otherPM != null)
            throw new Exception("Expected null PM for deserialized, unbound query object, pm == " + pm);
        tx.commit();
    }

    /**
     * Testcase: filter uses boolean fields (booleanField == true)
     */
    public void advanced042()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("booleanNull == true");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 1;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 3;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 5;
        expected.add(key);
        key = new PrimitiveTypes.Oid();
        key.id = 11;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: use of hex literal
     * checks bug report 4818832
     */
    public void advanced043()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("intNotNull == 0x7fffffff");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 10;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: use of oct literal
     * checks bug report 4818832
     */
    public void advanced044()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        // create query
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("intNotNull == 017777777777");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        PrimitiveTypes.Oid key = new PrimitiveTypes.Oid();
        key.id = 10;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: filter using Integer.MIN_VALUE
     * checks bug report 4818832
     */
    public void advanced045()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create and execute query

        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.setFilter("longNotNull == -2147483648");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result

        Collection expected = new ArrayList();
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: navigation through a null valued relationship field.
     * checks bug report 4833898
     */
    public void advanced046()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("this.department.deptid > 3 || this.empid == 100");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
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
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: navigation through a null valued relationship field.
     * checks bug report 4833898
     */
    public void advanced047()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create and execute query

        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.setFilter("this.empid == 100 | this.department.deptid > 3");
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 8;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 9;
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
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }
}

