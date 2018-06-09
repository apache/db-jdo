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
 
package org.apache.jdo.tck.api.persistencemanagerfactory;

import java.util.Date;

import java.util.Properties;
import javax.jdo.Constants;
import javax.jdo.JDOHelper;
import javax.jdo.JDOReadOnlyException;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.Address;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B>ReadOnly property of PersistenceManagerFactory  
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory
 *<BR>
 *<B>Assertion IDs:</B> A11.1-28 
 *<BR>
 *<B>Assertion Description: </B>
 * if an attempt is made to write (via flush) then JDOReadOnlyException 
 * is thrown.
 */


public class FlushThrowsIfReadOnly extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A11.1-28 FlushThrowsIfReadOnly failed: ";

    /** Second PMF that is read-only */
    PersistenceManagerFactory pmf2;

    /** The company oid */
    Object oid;
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(FlushThrowsIfReadOnly.class);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(Company.class);
        Properties properties = loadProperties(PMFProperties);
        properties.put(Constants.PROPERTY_READONLY, "true");
        pmf2 = JDOHelper.getPersistenceManagerFactory(properties);
        // insert an instance to find
        getPM();
        pm.currentTransaction().begin();
        Company comp = new Company(1L, "Sun Microsystems", new Date(), 
                                   new Address(0,"","","","",""));
        pm.makePersistent(comp);
        oid = pm.getObjectId(comp);
        pm.currentTransaction().commit();
        pm.close();
    }

    /** */
    public void testMakePersistent() {
        //Try to makePersistent and flush the transaction
        pm = pmf2.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Company comp = new Company(2L, "Sun Microsystems2", new Date(), 
                                   new Address(0,"","","","",""));
        try {
            pm.makePersistent(comp);
            pm.flush();
            fail("When the PersistenceManagerFactory is read only, " +
                    "flush of a persistent-new instance must throw " +
                    "JDOReadOnlyException.");
        } catch (JDOReadOnlyException jDOReadOnlyException) {
            // good catch
        } catch (Throwable t) {
            fail("When the PersistenceManagerFactory is read only, " +
                    "flush of a persistent-deleted instance must throw " +
                    "JDOReadOnlyException. Flush threw " + t);
        }
        tx.rollback();
        pm.close();
        pm = null;
        closePMF(pmf2);
    }

    /** */
    public void testUpdate() {
        //Try to update and flush the transaction
        pm = pmf2.getPersistenceManager();
        pm.getExtent(Company.class); // make sure that oid class is loaded
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Company comp = (Company)pm.getObjectById(oid);
        try {
            comp.setName("new name");
            pm.flush();
            fail("When the PersistenceManagerFactory is read only, " +
                    "flush of an updated instance must throw " +
                    "JDOReadOnlyException.");
        } catch (JDOReadOnlyException jDOReadOnlyException) {
            // good catch
        } catch (Throwable t) {
            fail("When the PersistenceManagerFactory is read only, " +
                    "flush of a persistent-deleted instance must throw " +
                    "JDOReadOnlyException. Flush threw " + t);
        }
        tx.rollback();
        pm.close();
        pm = null;
        closePMF(pmf2);
    }

    /** */
    public void testDeletePersistent() {
        //Try to deletePersistent and flush the transaction
        pm = pmf2.getPersistenceManager();
        pm.getExtent(Company.class); // make sure that oid class is loaded
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Company comp = (Company)pm.getObjectById(oid);
        try {
            pm.deletePersistent(comp);
            pm.flush();
            fail("When the PersistenceManagerFactory is read only, " +
                    "flush of a persistent-deleted instance must throw " +
                    "JDOReadOnlyException.");
        } catch (JDOReadOnlyException jDOReadOnlyException) {
            // good catch
        } catch (Throwable t) {
            fail("When the PersistenceManagerFactory is read only, " +
                    "flush of a persistent-deleted instance must throw " +
                    "JDOReadOnlyException. Flush threw " + t);
        }
        tx.rollback();
        pm.close();
        pm = null;
        closePMF(pmf2);
    }
}
