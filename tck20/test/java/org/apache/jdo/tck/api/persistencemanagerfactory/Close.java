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

import javax.jdo.JDOUserException;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B>Close of PersistenceManagerFactory  
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory
 *<BR>
 *<B>Assertion IDs:</B> A11.4-2
 *<BR>
 *<B>Assertion Description: </B>
 * PersistenceManagerFactory.close() closes this PersistenceManagerFactory.
 */


public class Close extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertions A11.4-2 (Close) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(Close.class);
    }

    /** */
    public void test() {
        pmf = getPMF();
        pmf.close();
        //check that pmf is really closed by trying to get a getPersistenceManager
        try {
            pm = pmf.getPersistenceManager();
            fail(ASSERTION_FAILED,
                "JDOUserException was not thrown when calling pmf.getPersistenceManager() after pmf was closed");
        } catch (JDOUserException ex) {
            // expected exception
            if (debug)
                logger.debug("caught expected exception " + ex.toString());
        }
    }
}
