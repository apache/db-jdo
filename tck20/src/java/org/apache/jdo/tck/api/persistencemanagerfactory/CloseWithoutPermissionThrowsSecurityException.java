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

import java.security.Permission;

import javax.jdo.spi.JDOPermission;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B>Close of PersistenceManagerFactory Throws SecurityException if
 * JDOPermission("closePersistenceManagerFactory") Is Not Set
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory SecurityException
 *<BR>
 *<B>Assertion IDs:</B> A11.4-3
 *<BR>
 *<B>Assertion Description: </B>
 * PersistenceManagerFactory.close() closes this PersistenceManagerFactory.
 * If JDOPermission("closePersistenceManagerFactory") is not set, then
 * SecurityException in thrown. 
 */


public class CloseWithoutPermissionThrowsSecurityException extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertions A11.4-3 (CloseWithoutPermissionThrowsSecurityException) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CloseWithoutPermissionThrowsSecurityException.class);
    }

    /** */
    public void test() {
	    pmf = getPMF();

        SecurityManager oldSecMgr = System.getSecurityManager();
        try {
            System.setSecurityManager(new MySecurityManager());
        } catch (SecurityException se) {
            fail(ASSERTION_FAILED,
                "SecurityManager already set, setSecurityManager throws " +
                se.toString());
        }

        try {
            pmf.close();
        } catch (SecurityException ex) {
            // expected exception if JDOPermission("closePersistenceManagerFactory") is not set
            if (debug)
                logger.debug("caught expected exception " + ex.toString());
        }
        finally {
            System.setSecurityManager(oldSecMgr);
        }
    }

    public class MySecurityManager extends SecurityManager {
        public void checkPermission(Permission perm) {
            if (perm==JDOPermission.CLOSE_PERSISTENCE_MANAGER_FACTORY)
                throw new SecurityException(
                    "JDOPermission.CLOSE_PERSISTENCE_MANAGER_FACTORY not set");
        }
    }
}
