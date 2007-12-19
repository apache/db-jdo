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
import org.apache.jdo.tck.pc.company.FullTimeEmployee;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.pc.company.IMedicalInsurance;
import org.apache.jdo.tck.pc.company.MedicalInsurance;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B>RelationshipNegative1To1Test
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

public class RelationshipNegative1To1Test extends AbstractRelationshipTest {
    
    String testMethod = null;
    protected String ASSERTION_FAILED =
        "Assertion A15-3.14 (RelationshipNegative1To1Test) failed: ";
    
    Object emp1Oid = null;
    Object emp2Oid = null;
    Object medIns1Oid = null;
    Object medIns2Oid = null;
    IEmployee emp1 = null;
    IEmployee emp2 = null;
    IMedicalInsurance medIns1 = null;
    IMedicalInsurance medIns2 = null;
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(RelationshipNegative1To1Test.class);
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
            emp2Oid = getOidByName("emp2");
            medIns1Oid = getOidByName("medicalIns1");
            medIns2Oid = getOidByName("medicalIns2");
            emp1 = (IEmployee)pm.getObjectById(emp1Oid);
            emp2 = (IEmployee)pm.getObjectById(emp2Oid);
            medIns1 = (IMedicalInsurance)pm.getObjectById(medIns1Oid);
            medIns2 = (IMedicalInsurance)pm.getObjectById(medIns2Oid);
        }
    }
    
    /**
     * Test that JdoUserException is thrown if two sides of a relationship
     * do not refer to each other.
     */
    public void testA2BbutNotB2AMapped() {
        testMethod = "testA2BbutNotB2AMapped";
        if (isTestToBePerformed) {
            IEmployee empNew = new FullTimeEmployee(99, "Matthew", "", "Adams",
                new Date(0L), new Date(10000L), 125000);
            pm.makePersistent(empNew);
            emp1.setMedicalInsurance(medIns2);
            medIns2.setEmployee(empNew);
            doFlush(testMethod);
        }
    }
    
    /**
     * Test that JdoUserException is thrown if two sides of a relationship
     * do not refer to each other.
     */
    public void testA2BbutNotB2AMappedBy() {
        testMethod = "testA2BbutNotB2AMappedBy";
        if (isTestToBePerformed) {
            IMedicalInsurance medInsNew = new MedicalInsurance(99,
                "The American Company", "B");
            pm.makePersistent(medInsNew);
            medIns2.setEmployee(emp1);
            emp1.setMedicalInsurance(medInsNew);
            doFlush(testMethod);
        }
    }
    
    /**
     * Test that JdoUserException is thrown setting mapped side
     * of a one-to-one relationship and setting the other side to null
     */
    public void testSetOtherSideToNullMapped() {
        testMethod = "testSetOtherSideToNullMapped";
        if (isTestToBePerformed) {
            emp1.setMedicalInsurance(medIns2);
            medIns2.setEmployee(null);
            doFlush(testMethod);
        }
    }
    
    /**
     * Test that JdoUserException is thrown setting mapped by side
     * of a one-to-one relationship and setting the other side to null
     */
    public void testSetOtherSideToNullMappedBy() {
        testMethod = "testSetOtherSideToNullMappedBy";
        if (isTestToBePerformed) {
            medIns2.setEmployee(emp1);
            emp1.setMedicalInsurance(null);
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
