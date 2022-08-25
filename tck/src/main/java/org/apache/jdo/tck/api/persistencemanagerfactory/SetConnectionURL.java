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

import javax.jdo.JDOException;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;


/**
 *<B>Title:</B>Set ConnectionURL of PersistenceManagerFactory  
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory
 *<BR>
 *<B>Assertion IDs:</B> A11.1-16,A11.1-17.
 *<BR>
 *<B>Assertion Description: </B>
 * PersistenceManagerFactory.setConnectionURL(String URL) sets the value of
 * the ConnectionURL property (the URL for the data source). 
 * PersistenceManagerFactory.getConnectionURL() returns the value of the
 * ConnectionURL property.
 */

public class SetConnectionURL extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A11.1-16,A11.1-17 (SetConnectionURL) failed: ";
    
    /** The value of the ConnectionURL property. */
    private String url;

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetConnectionURL.class);
    }

    /** */
    @Override
    protected void localSetUp() {
        closePMF();
        pmf = getUnconfiguredPMF();
        url = getPMFProperty(CONNECTION_URL_PROP);
    }
    
    /** Set ConnectionURL value and get ConnectionURL value to verify. */ 
    public void test() {
        try {
            if (url == null) {
                throw new JDOException(
                    "Missing PMF property " + CONNECTION_URL_PROP);
            }
            pmf.setConnectionURL(url);
            if (!url.equals(pmf.getConnectionURL())) {
                fail(ASSERTION_FAILED,
                     "ConnectionURL set to '" + url + "' ," +
                     "value returned by PMF is '" +
                     pmf.getConnectionURL() + "'.");
            }
        } finally {
            closePMF();
        }
    }
}
