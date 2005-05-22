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
 * TreeTest.java
 *
 * Created on April 10, 2000
 */

package org.apache.jdo.test.query;

import java.util.*;
import java.io.*;
import java.io.PrintStream;

import javax.jdo.*;

import org.apache.jdo.impl.jdoql.tree.FilterExpressionDumper;
import org.apache.jdo.jdoql.tree.Expression;
import org.apache.jdo.jdoql.tree.Node;
import org.apache.jdo.jdoql.tree.QueryTree;
import org.apache.jdo.jdoql.tree.TreeWalker;
import org.apache.jdo.pc.xempdept.Department;
import org.apache.jdo.pc.xempdept.Employee;
import org.apache.jdo.pc.xempdept.Project;

/**
 *
 * @author  Michael Bouschen
 */
public class TreeTest
    extends PositiveTest
{
    private final TreeWalker walker = new TreeWalker();
    private final FilterExpressionDumper dumper = new FilterExpressionDumper();
    private void dumpTree(Node qt)
    {   walker.walk( qt, dumper );
    }

    public TreeTest(PersistenceManagerFactory pmf, PrintStream log)
    {
        super(pmf, log);
    }

    protected boolean isTestMethodName(String methodName)
    {
       return methodName.startsWith("tree");
    }

    // ==========================================================================
    // Test methods
    // ==========================================================================

    /**
     * Testcase: relational operation
     * field == literal
     */
    public void tree001()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query tree

        QueryTree qt = newQueryTree();
        qt.setCandidateClass(Employee.class);
        Expression field = qt.newIdentifier("firstname");
        Expression constant = qt.newConstant("firstEngOne");
        Expression equals = qt.newEquals(field, constant);
        qt.setFilter(equals);

        // create query

        Query query = pm.newQuery(qt);
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
     * Testcase: using parameters
     */
    public void tree002()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query tree

        QueryTree qt = newQueryTree();
        qt.declareParameter(String.class, "name");
        qt.setCandidateClass(Employee.class);
        Expression field = qt.newIdentifier("lastname");
        Expression param = qt.newIdentifier("name");
        Expression equals = qt.newEquals(field, param);
        qt.setFilter(equals);

        // create query

        Query query = pm.newQuery(qt);
        setEmployeeCandidates(query);
        Object result = query.execute("lastEngTwo");
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
     * Testcase: compiled query
     */
    public void tree003()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query tree

        QueryTree qt = newQueryTree();
        qt.setCandidateClass(Department.class);
        Expression field = qt.newIdentifier("deptid");
        Expression constant = qt.newConstant(new Integer(1));
        Expression equals = qt.newEquals(field, constant);
        qt.setFilter(equals);

        // create query

        Query query = pm.newQuery(qt);

        // serialize query object
        ByteArrayOutputStream bout = new ByteArrayOutputStream ();
        ObjectOutputStream oout = new ObjectOutputStream (bout);
        oout.writeObject(query);
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
     * Testcase: compiled query
     * simple query filter: Department.deptid == 1
     */
    public void tree004()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query tree

        QueryTree qt = newQueryTree();
        qt.setCandidateClass( Department.class );
        Expression equals = qt.newEquals( qt.newIdentifier("deptid"), qt.newConstant(1) );
        qt.setFilter( equals );

        // create query

        Query query = pm.newQuery(qt);
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
     * Testcase: compiled query
     * bound variable access on Project
     */
    public void tree005()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query tree

        QueryTree qt = newQueryTree();
        qt.setCandidateClass( Employee.class );
        qt.declareVariable( Project.class, "project" );
        Expression contains = qt.newMethodCall( qt.newIdentifier("projects"), "contains", new Expression[] {qt.newIdentifier("project")} );
        Expression projectName = qt.newFieldAccess( qt.newIdentifier("project"), "name" );
        Expression equals = qt.newEquals( projectName, qt.newConstant("Sales Project") );
        Expression and = qt.newConditionalAnd( contains, equals );
        qt.setFilter( and );

        // create query

        Query query = pm.newQuery(qt);
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
        key.empid = 5;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 6;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: compiled query
     * bound variable access on Employee
     */
    public void tree006()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query tree

        QueryTree qt = newQueryTree();
        qt.setCandidateClass( Project.class );
        qt.declareVariable( Employee.class, "employee" );
        Expression contains = qt.newMethodCall( qt.newIdentifier("employees"), "contains", new Expression[] {qt.newIdentifier("employee")} );
        Expression firstName = qt.newFieldAccess( qt.newIdentifier("employee"), "firstname" );
        Expression equals = qt.newEquals( firstName, qt.newConstant("firstEngOne") );
        Expression and = qt.newAnd( contains, equals );
        qt.setFilter( and );

        // create query

        Query query = pm.newQuery(qt);
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
     * Testcase: compiled query
     * nested bound variable access on Employee and Project
     */
    public void tree007()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query tree

        QueryTree qt = newQueryTree();
        qt.setCandidateClass( Employee.class );
        qt.declareVariable( Project.class, "project" );
        qt.declareVariable( Employee.class, "employee" );
        Expression contains1 = qt.newMethodCall( qt.newIdentifier("projects"), "contains", new Expression[] {qt.newIdentifier("project")} );
        Expression contains2 = qt.newMethodCall( qt.newFieldAccess(qt.newIdentifier("project"), "employees"), "contains", new Expression[] {qt.newIdentifier("employee")} );
        Expression firstName = qt.newFieldAccess( qt.newIdentifier("employee"), "firstname" );
        Expression equals = qt.newEquals( firstName, qt.newConstant("firstEngOne") );
        Expression and2 = qt.newAnd( contains2, equals );
        Expression and1 = qt.newConditionalAnd( contains1, and2 );
        qt.setFilter( and1 );

        // create query

        Query query = pm.newQuery(qt);
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
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 12;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: compiled query
     * method call
     */
    public void tree008()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query tree

        QueryTree qt = newQueryTree();
        qt.setCandidateClass( Employee.class );
        Expression firstName = qt.newIdentifier( "firstname" );
        Expression methodCall = qt.newMethodCall( firstName, "endsWith", new Expression[]{qt.newConstant("One")} );
        qt.setFilter( methodCall );

        // create query

        Query query = pm.newQuery(qt);
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
     * Testcase: compiled query
     * ascending
     */
    public void tree009()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query tree

        QueryTree qt = newQueryTree();
        qt.setCandidateClass( Employee.class );
        Expression empid = qt.newIdentifier( "empid" );
        Expression birthdate = qt.newIdentifier( "birthdate" );
        qt.addAscendingOrdering( empid );
        Expression firstName = qt.newIdentifier( "firstname" );
        Expression equals = qt.newEquals( firstName, qt.newConstant("firstEngOne") );
        qt.setFilter( equals );

        // create query

        Query query = pm.newQuery(qt);
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);

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
     * Testcase: compiled query
     * decending
     */
    public void tree010()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query tree

        QueryTree qt = newQueryTree();
        qt.setCandidateClass( Employee.class );
        Expression empid = qt.newIdentifier( "empid" );
        Expression birthdate = qt.newIdentifier( "birthdate" );
        qt.addDescendingOrdering( empid );
        Expression firstName = qt.newIdentifier( "firstname" );
        Expression equals = qt.newEquals( firstName, qt.newConstant("firstEngOne") );
        qt.setFilter( equals );

        // create query

        Query query = pm.newQuery(qt);
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);

        // check query result

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: compiled query
     * ascending/decending
     */
    public void tree011()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query tree

        QueryTree qt = newQueryTree();
        qt.setCandidateClass( Employee.class );
        Expression firstName = qt.newIdentifier( "firstname" );
        Expression empid = qt.newIdentifier( "empid" );
        qt.addAscendingOrdering( firstName );
        qt.addDescendingOrdering( empid );
        Expression name = qt.newIdentifier( "firstname" );
        Expression equals = qt.newEquals( name, qt.newConstant("firstEngOne") );
        qt.setFilter( equals );

        // create query

        Query query = pm.newQuery(qt);
        setEmployeeCandidates(query);
        Object result = query.execute();
        List oids = getResultOids(result);

        // check query result

        Collection expected = new ArrayList();
        Employee.Oid key = new Employee.Oid();
        key.empid = 11;
        expected.add(key);
        key = new Employee.Oid();
        key.empid = 1;
        expected.add(key);
        checkQueryResult(oids, expected);

        tx.commit();
    }

    /**
     * Testcase: compiled query
     * decending
     */
    public void tree012()
        throws Exception
    {
        Transaction tx = pm.currentTransaction();
        tx.begin();

        // create query tree

        QueryTree qt = newQueryTree();
        qt.declareParameter( Collection.class, "collection" );
        qt.setCandidateClass( Employee.class );
        Expression equals = qt.newEquals( qt.newIdentifier("empid"), qt.newConstant(1) );
        Expression contains = qt.newMethodCall( qt.newIdentifier("collection"), "contains", new Expression[]{qt.newConstant((short)1)} );
        Expression and = qt.newAnd( contains, equals );
        qt.setFilter( and );

        // create query

        Query query = pm.newQuery(qt);
        setEmployeeCandidates(query);
        Collection collection = new HashSet();
        collection.add( new Short((short)1) );
        Object result = query.execute( collection );
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

}

