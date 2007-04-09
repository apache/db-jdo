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


package org.apache.jdo.tck.api.persistencemanager;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import org.apache.jdo.tck.pc.newInstance.AAddress;
import org.apache.jdo.tck.pc.newInstance.Address;
import org.apache.jdo.tck.pc.newInstance.IAddress;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Delete Persistent
 *<BR>
 *<B>Keywords:</B>
 *<BR>
 *<B>Assertion IDs:</B> A12.6.6-1
 *<BR>
 *<B>Assertion Description: </B>
[The parameter must be one of the following:
- an abstract class that is declared in the metadata as persistence-capable, in which all abstract methods are declared as persistent properties, or
- an interface that is declared in the metadata as persistence-capable, in which all methods are declared as persistent properties, or
- a concrete class that is declared in the metadata as persistence-capable. In this case, the concrete class must declare a public no-args constructor.
If the parameter does not satisfy the above requirements, JDOUserException is thrown.
 */

public class NewInstance extends PersistenceManagerTest {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A12.5.7-9 (NewInstance) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(NewInstance.class);
    }
    
    /** */
    public void testNewInstance() {
        pm = getPM();   

        /* positive tests */
        runTestNewInstanceInterface(pm);
        runTestNewInstanceAbstractClass(pm);
        runTestNewInstanceClass(pm);

        /* negative tests */

        pm.close();
        pm = null;
    }

    /** test newInstance (Class pcInterface) */
    private void runTestNewInstanceInterface(PersistenceManager pm) {
            IAddress iaddress = (IAddress)pm.newInstance(IAddress.class);
//        Transaction tx = pm.currentTransaction();
//        try {
//            tx = pm.currentTransaction();
//            tx.begin();
//            tx.commit();
            if (debug) logger.debug(" \nPASSED in testNewInstance()");
//        }
//        finally {
//            if (tx.isActive())
//                tx.rollback();
//        }        
    }

    /** test newInstance (Class pcAbstractClass) */
    private void runTestNewInstanceAbstractClass(PersistenceManager pm) {
        pm.newInstance(AAddress.class);
        Transaction tx = pm.currentTransaction();
 //       try {
 //           tx.begin();
//
//
//            tx.commit();
//            if (debug) logger.debug(" \nPASSED in testNewInstance()");
//        } 
//        finally {
//            if (tx.isActive())
//                tx.rollback();
//        }   
    }

        /** test newInstance (Class pcClass) */
    private void runTestNewInstanceClass(PersistenceManager pm) {
        pm.newInstance(Address.class);
        Transaction tx = pm.currentTransaction();
//        try {
//            tx.begin();
//
//            tx.commit();
//            if (debug) logger.debug(" \nPASSED in testNewInstance()");
//        }
//        finally {
//            if (tx.isActive())
//                tx.rollback();
//        }
    }

}
