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

import java.util.Properties;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B>Set Multithreaded of persistencemanagerfactory  
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory
 *<BR>
 *<B>Assertion IDs:</B> A11.1-11,A11.1-12.
 *<BR>
 *<B>Assertion Description: </B>
 * PersistenceManagerFactory.setMultithreaded(boolean flag) sets the value of
 * the Multithreaded flag that indicates that the application will invoke
 * methods or access fields of managed instances from multiple threads.
 */

public class SetMultithreaded extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertions A11.1-11,A11.1-12 (SetMultithreaded) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetMultithreaded.class);
    }

    /** */
    protected void localSetUp() {
        closePMF();
        pmf = getUnconfiguredPMF();
    }

    /** 
     * Set Multithreaded to true or false and use getMultithreaded value to
     * verify.
     */ 
    public void test() {
        try {
            setMultithreaded(false);
            setMultithreaded(true);
        } finally {
            closePMF();
        }
    }

    /** */
    private void setMultithreaded(boolean newValue) {
        pmf.setMultithreaded(newValue);
        boolean current = pmf.getMultithreaded();
        if (current != newValue) {
            fail(ASSERTION_FAILED, "Multithreaded set to " + newValue + 
                 ", value returned by PMF is " + current);
        }
    }
}
