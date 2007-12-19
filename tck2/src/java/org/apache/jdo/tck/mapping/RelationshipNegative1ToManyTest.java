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

package org.apache.jdo.tck.mapping;

import java.util.Date;
import javax.jdo.JDOUserException;
import org.apache.jdo.tck.pc.company.Department;
import org.apache.jdo.tck.pc.company.Employee;
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B>RelationshipNegative1ToManyTest
 *<BR>
 *<B>Keywords:</B> mapping, managed relationships
 *<BR>
 *<B>Assertion ID:</B> A15.3-14
 *<BR>
 *<B>Assertion Description: Regardless of which side changes the relationship,
 * flush (whether done as part of commit or explicitly by the user) will modify
 * the datastore to reflect the change and will update the memory model
 * for consistency...</B>
 */

public class RelationshipNegative1ToManyTest extends AbstractRelationshipTest {
    
    String testMethod = null;
    protected String ASSERTION_FAILED =
        "Assertion A15-3.14 (RelationshipNegative1ToManyTest) failed: ";
    
    Object emp1Oid = null;
    Object dept1Oid = null;
    Object dept2Oid = null;
    Employee emp1 = null;
    Department dept1 = null;
    Department dept2 = null;
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(RelationshipNegative1ToManyTest.class);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        super.localSetUp();
        if (isTestToBePerformed) {
            getPM();
            pm.currentTransaction().begin();
            
            emp1Oid = getOidByName("emp1");
            dept1Oid = getOidByName("dept1");
            dept2Oid = getOidByName("dept2");
            emp1 = (Employee)pm.getObjectById(emp1Oid);
            dept1 = (Department)pm.getObjectById(dept1Oid);
            dept2 = (Department)pm.getObjectById(dept2Oid);
        }
    }
    
    /**
     * Test that JdoUserException is thrown setting one side
     * of a one-to-many relationship and deleting the other side
     */
    public void testDeleteOtherSide() {
        testMethod = "testDeleteOtherSide";
        if (isTestToBePerformed) {
            dept2.addEmployee(emp1);
            pm.deletePersistent(emp1);
            doFlush(testMethod);
        }
    }
    
    /**
     * adding a related instance (with a single-valued mapped-by relationship 
     * field) to more than one one-to-many collection relationship
     */
    public void testAddToMoreThanOne() {
        testMethod = "testAddToMoreThanOne";
        if (isTestToBePerformed) {
            Employee empNew = new FullTimeEmployee(99, "Matthew", "", "Adams",
                new Date(0L), new Date(10000L), 125000);
            pm.makePersistent(empNew);
            dept1.addEmployee(empNew);
            dept2.addEmployee(empNew);
            doFlush(testMethod);
        }
    }
    
    /**
     * adding a related instance to a collection and setting the other side 
     * to a different instance 
     */
    public void testAInBbutNotB2A() {
        testMethod = "testAInBbutNotB2A";
        if (isTestToBePerformed) {
            Employee empNew = new FullTimeEmployee(99, "Matthew", "", "Adams",
                new Date(0L), new Date(10000L), 125000);
            pm.makePersistent(empNew);
            dept1.addEmployee(empNew);
            empNew.setDepartment(dept2);
            doFlush(testMethod);
        }
    }
    
    protected void doFlush(String method) {
        try {
            pm.flush();
            fail(ASSERTION_FAILED + testMethod
                + ": expected JDOUserException on flush!");
        } catch (JDOUserException jdoe) {
            // expected exception
        }
        pm.currentTransaction().rollback();
    }
}
