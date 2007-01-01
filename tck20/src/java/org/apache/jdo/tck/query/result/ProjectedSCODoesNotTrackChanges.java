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


package org.apache.jdo.tck.query.result;

import java.util.Date;

import javax.jdo.JDOHelper;
import javax.jdo.Query;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.Address;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Projected Second Class Objects Do Not Track Their Changes
 *<BR>
 *<B>Keywords:</B> embedded query
 *<BR>
 *<B>Assertion ID:</B> [None yet]
 *<BR>
 *<B>Assertion Description: </B>
Section 14.6.9: Projected SCOs are never owned, projected FCOs are always
managed. Modifying unowned SCOs never has an effect on the database.
Modifying FCOs no matter how you get them always has an effect if the
tx commits.
 */

public class ProjectedSCODoesNotTrackChanges extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A?? (ProjectedSCODoesNotTrackChanges) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(ProjectedSCODoesNotTrackChanges.class);
    }
    
    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(Company.class);
    }

    private Company getPersistentNewInstance(long companyid) {
        Company obj = new Company(companyid, "MyCompany", new Date(),
                new Address(0,"","","","",""));
        pm.makePersistent(obj); // obj should transition to persistent-new
        int curr = currentState(obj);
        if( curr != PERSISTENT_NEW ){
            fail(ASSERTION_FAILED,
                "Unable to create persistent-new instance "
                + "from transient instance via makePersistent(), state is "
                + states[curr]);
        }
        return obj;
    }

    /** This tests that when an embedded field is selected
     *  and modified, there is no change in the owned instance. 
     */
    public void testEmbeddedField() {
        String testZip = "94040";
        pm = getPM();
        pm.currentTransaction().begin();
        Company comp = getPersistentNewInstance(0);
        pm.currentTransaction().commit();

        // Select address, modify, and commit
        pm.currentTransaction().begin();
        Query query = pm.newQuery(Company.class,
                "name.startsWith(\"MyCompany\")");
        query.setResult("address");
        query.setUnique(true);
        Address myCompanyAddress = (Address) query.execute();
        myCompanyAddress.setZipcode(testZip);
        if (JDOHelper.isDirty((Object)comp)) {
            appendMessage("Expected Company instance not to be dirty; "
                + "actual state is " + getStateOfInstance((Object)comp));
        }
        pm.currentTransaction().commit();

        // Check value of address
        pm.currentTransaction().begin();
        Address persistedCompanyAddress = (Address) query.execute();
        String actualZip = persistedCompanyAddress.getZipcode();
        if (actualZip.equals(testZip)) {
            appendMessage("Expected projected field value is "
                + testZip + "; actual value is " + actualZip);
        }
        logger.debug("MyCompany's zipcode is '" + actualZip + "'");
        pm.currentTransaction().commit();
        failOnError();
    }

    /** This tests that when a Date field is selected
     *  and modified, there is no change in the owned instance. 
     */
    public void testDateField() {
        Date testDate = new Date(123789L);
        pm = getPM();
        pm.currentTransaction().begin();
        Company comp = getPersistentNewInstance(0);
        pm.currentTransaction().commit();

        // Select date, modify, and commit
        pm.currentTransaction().begin();
        Query query = pm.newQuery(Company.class,
                "name.startsWith(\"MyCompany\")");
        query.setResult("founded");
        query.setUnique(true);
        Date expectedDate = (Date) query.execute();
        comp.setFounded(testDate);
        if (JDOHelper.isDirty((Object)comp)) {
            appendMessage("Expected Company instance not to be dirty; "
                + "actual state is " + getStateOfInstance((Object)comp));
        }
        pm.currentTransaction().commit();

        // Check value of date
        pm.currentTransaction().begin();
        Date actualDate = (Date) query.execute();
        if (!actualDate.equals(expectedDate)) {
            appendMessage("Expected projected field value is "
                + expectedDate + "; actual value is " + actualDate);
        }
        logger.debug("MyCompany's founded date is '" + actualDate + "'");
        pm.currentTransaction().commit();
        failOnError();
    }
}
