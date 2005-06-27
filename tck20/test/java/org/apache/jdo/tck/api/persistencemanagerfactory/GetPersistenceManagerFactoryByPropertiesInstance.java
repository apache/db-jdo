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
 
package org.apache.jdo.tck.api.persistencemanagerfactory;

import java.util.Date;

import javax.jdo.JDOHelper;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.company.Address;
import org.apache.jdo.tck.pc.company.Company;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B>Close of PersistenceManagerFactory  
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory
 *<BR>
 *<B>Assertion IDs:</B> A11.1-32
 *<BR>
 *<B>Assertion Description: </B>
 * An implementation must provide a method to construct a PersistenceManagerFactory by a Properties instance.
 * This static method is called by the JDOHelper method getPersistenceManagerFactory (Properties props).
 */


public class GetPersistenceManagerFactoryByPropertiesInstance extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertions A11.1-32 (GetPersistenceManagerFactoryByPropertiesInstance) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetPersistenceManagerFactoryByPropertiesInstance.class);
    }

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        addTearDownClass(Company.class);
    }

    /** */
    public void test() {
        PMFPropertiesObject = loadProperties(PMFProperties);
        pmf = JDOHelper.getPersistenceManagerFactory(PMFPropertiesObject);
        //Try to get a PersistenceManager and begin and commit a transaction
        pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        Company comp = new Company(1L, "Sun Microsystems", new Date(), new Address(0,"","","","",""));
        pm.makePersistent(comp);
        tx.commit();
    }
}
