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
 * ParameterTest.java
 *
 * Created on April 10, 2000
 */

package org.apache.jdo.test.query;

import java.util.*;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.pc.xempdept.Department;
import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.pc.xempdept.Insurance;
import org.apache.jdo.pc.xempdept.PrimitiveTypes;

/** 
 *
 * @author  Michael Bouschen
 */
public class ParameterTest
    extends PositiveTest
{
    public ParameterTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
        return methodName.startsWith("parameter");
    }

    // ==========================================================================
    // Test methods
    // ==========================================================================

    /**
     * Testcase: parameter in relational operation 
     * field == parameter
     */
    public void parameter001()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("long id");
        query.setFilter("empid == id");
        setEmployeeCandidates(query);
        Object result = query.execute(new Long(1));
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
     * Testcase: parameter in relational operation 
     * field == parameter (type String)
     */
    public void parameter002()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("String lastname");
        query.setFilter("this.lastname == lastname");
        setEmployeeCandidates(query);
        Object result = query.execute("lastSalesFour");
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
     * Testcase: parameter in relational operation 
     * field == parameter (type java.lang.String)
     */
    public void parameter003()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("java.lang.String lastname");
        query.setFilter("lastname == this.lastname");
        setEmployeeCandidates(query);
        Object result = query.execute("lastSalesFour");
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
     * Testcase: executeWithArray
     */
    public void parameter004()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("long l");
        query.setFilter("empid == l");
        setEmployeeCandidates(query);
        Object[] actualParams = {new Long(1)};
        Object result = query.executeWithArray(actualParams);
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
     * Testcase: executeWithMap
     */
    public void parameter005()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("long l");
        query.setFilter("empid == l");
        setEmployeeCandidates(query);
        Map actualParams = new java.util.HashMap();
        actualParams.put("l", new Long(1));
        Object result = query.executeWithMap(actualParams);
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
     * Testcase: date comparison
     */
    public void parameter006()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import java.util.Date");
        query.declareParameters("Date d");
        query.setFilter("hiredate == d");
        setEmployeeCandidates(query);
        Object result = query.execute(new GregorianCalendar(2000, 1, 2).getTime());
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
     * Testcase: date comparison
     */
    public void parameter007()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import java.util.Date");
        query.declareParameters("Date d");
        query.setFilter("hiredate < d");
        setEmployeeCandidates(query);
        Object result = query.execute(new GregorianCalendar(2000, 1, 2).getTime());
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
     * Testcase: multiple parameters
     */
    public void parameter008()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("String name, long id");
        query.setFilter("firstname == name && empid == id");
        setEmployeeCandidates(query);
        Object result = query.execute("firstEngOne", new Long(1));
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
     * Testcase: parameter value is null
     */
    public void parameter009()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("String name");
        query.setFilter("firstname == name");
        setEmployeeCandidates(query);
        Object result = query.execute(null);
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
     * Testcase: parameter field access
     */
    public void parameter010()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Department d = getDepartmentById(1L);

        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Department");
        query.declareParameters("Department d");
        query.setFilter("department.deptid == d.deptid");
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
     * Testcase: object comparison 
     *   relship == param
     *   navigating reference side of 1:n relationship
     */
    public void parameter011()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Department d = getDepartmentById(1L);

        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Department");
        query.declareParameters("Department d");
        query.setFilter("department == d");
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
     * Testcase: object comparison
     *   param == relship
     *   navigating reference side of 1:n relationship
     */
    public void parameter012()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Department d = getDepartmentById(1L);

        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Department");
        query.declareParameters("Department d");
        query.setFilter("d == department");
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
     * Testcase: object comparison 
     *   relship != param
     *   navigating reference side of 1:n relationship
     */
    public void parameter013()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Department d = getDepartmentById(1L);

        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Department");
        query.declareParameters("Department d");
        query.setFilter("department != d");
        setEmployeeCandidates(query);
        Object result = query.execute(d);
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
        // MBO: include employees having no department.
        // Problem with SQL queries, outer joins?
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   param != relship
     *   navigating reference side of 1:n relationship
     */
    public void parameter014()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Department d = getDepartmentById(1L);

        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Department");
        query.declareParameters("Department d");
        query.setFilter("d != department");
        setEmployeeCandidates(query);
        Object result = query.execute(d);
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
        // MBO: include employees having no department.
        // Problem with SQL queries, outer joins?
        key = new Employee.Oid();
        key.empid = 100;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison 
     *   relship == param
     *   navigating 1:1 relationship
     */
    public void parameter015()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Employee e = getEmployeeById(2L);

        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Insurance.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
        query.declareParameters("Employee e");
        query.setFilter("employee == e");
        setInsuranceCandidates(query);
        Object result = query.execute(e);
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        Insurance.Oid key = new Insurance.Oid();
        key.insid = 2;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   param == relship
     *   navigating 1:1 relationship
     */

    public void parameter016()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Employee e = getEmployeeById(2L);

        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Insurance.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
        query.declareParameters("Employee e");
        query.setFilter("e == employee");
        setInsuranceCandidates(query);
        Object result = query.execute(e);
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        Insurance.Oid key = new Insurance.Oid();
        key.insid = 2;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison 
     *   relship != param
     *   navigating 1:1 relationship
     */
    public void parameter017()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Employee e = getEmployeeById(2L);

        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Insurance.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
        query.declareParameters("Employee e");
        query.setFilter("employee != e");
        setInsuranceCandidates(query);
        Object result = query.execute(e);
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        Insurance.Oid key = new Insurance.Oid();
        key.insid = 1;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 3;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 4;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 5;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 6;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 7;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 8;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 9;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 11;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 12;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 13;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison
     *   param != relship
     *   navigating 1:1 relationship
     */
    public void parameter018()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Employee e = getEmployeeById(2L);

        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Insurance.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Employee");
        query.declareParameters("Employee e");
        query.setFilter("e != employee");
        setInsuranceCandidates(query);
        Object result = query.execute(e);
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        Insurance.Oid key = new Insurance.Oid();
        key.insid = 1;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 3;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 4;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 5;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 6;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 7;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 8;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 9;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 11;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 12;
        expected.add(key);
        key = new Insurance.Oid();
        key.insid = 13;
        expected.add(key);
        checkQueryResult(oids, expected);
        
        tx.commit();
    }

    /**
     * Testcase: object comparison 
     *   relship == param
     *   navigating navigating reference side of 1:n self referencing relationship
     */
    public void parameter019()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Employee e = getEmployeeById(1L);

        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("Employee mgr");
        query.setFilter("manager == mgr");
        setEmployeeCandidates(query);
        Object result = query.execute(e);
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
     * Testcase: object comparison 
     *   relship != param
     *   navigating navigating reference side of 1:n self referencing relationship
     */
    public void parameter020()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Employee e = getEmployeeById(1L);

        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareParameters("Employee mgr");
        query.setFilter("manager != mgr");
        setEmployeeCandidates(query);
        Object result = query.execute(e);
        List oids = getResultOids(result);
        Collections.sort(oids);

        // check query result
        
        Collection expected = new ArrayList();
        // MBO: include employees having no manager.
        // Problem with SQL queries, outer joins?
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
     * Testcase: declareImports including two imports
     * checks bug report 4421917
     */
    public void parameter021()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Department d = getDepartmentById(1L);
        Insurance i = getInsuranceById(1L);

        // create and execute query
        
        Query query = pm.newQuery();
        query.setClass(Employee.class);
        query.declareImports("import org.apache.jdo.pc.xempdept.Department; import org.apache.jdo.pc.xempdept.Insurance;");
        query.declareParameters("Department d, Insurance i");
        query.setFilter("department == d & insurance == i");
        setEmployeeCandidates(query);
        Object result = query.execute(d, i);
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
     * Testcase: Boolean parameter compared with boolean field
     * checks bug report 4527554
     */
    public void parameter022()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.declareParameters("Boolean value");
        query.setFilter("booleanNotNull == value");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute(Boolean.TRUE);
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
     * Testcase: boolean parameter compared with Boolean field
     * checks bug report 4527554
     */
    public void parameter023()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.declareParameters("boolean value");
        query.setFilter("booleanNull == value");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute(Boolean.TRUE);
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
     * Testcase: BigDecimal parameter compared with BigDecimal field having different scale.
     * checks bug report 4531789
     */
    public void parameter024()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();
        
        Query query = pm.newQuery();
        query.setClass(PrimitiveTypes.class);
        query.declareParameters("java.math.BigDecimal value");
        query.setFilter("bigDecimal == value");
        setPrimitiveTypesCandidates(query);
        Object result = query.execute(new java.math.BigDecimal("1.00000"));
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
    
}
