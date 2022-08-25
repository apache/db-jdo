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

import javax.jdo.PersistenceManager;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B>GetPersistenceManager of PersistenceManagerFactory  
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory
 *<BR>
 *<B>Assertion IDs:</B> A11.3-1.
 *<BR>
 *<B>Assertion Description: </B>
 * PersistenceManagerFactory.getPersistenceManager() returns a
 * PersistenceManager instance with the configured properties and the
 * default values for option settings. 
 */

public class GetPersistenceManager extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A11.3-1 (GetPersistenceManager) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(GetPersistenceManager.class);
    }

    /** */
    public void test() {
        PersistenceManager pm = null;
        try {
            pm = pmf.getPersistenceManager();
            if (pm == null) {
                fail(ASSERTION_FAILED, "pmf.getPersistenceManager should " + 
                     "return a non-null value.");
            }
        }
        finally { 
            if ((pm != null) && !pm.isClosed()) {
                pm.close();
            }
            closePMF();
        }
    }
}
