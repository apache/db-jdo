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

import java.util.Properties;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B>Set RetainValues of persistencemanagerfactory  
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory
 *<BR>
 *<B>Assertion IDs:</B> A11.1-3,A11.1-4.
 *<BR>
 *<B>Assertion Description: </B>
 * PersistenceManagerFactory.setRetainValues(boolean flag) sets the value of the RetainValues property (the transaction mode that specifies the treatment of persistent instances after commit),
PersistenceManagerFactory.getRetainValues() returns the value of the
RetainValues property.
 */

/* 
 * Revision History
 * ================
 * Author         :   Linga Neerathilingam
 * Date   :  10/18/01
 * Version  :     1.0
 *
 */

public class SetRetainValues extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A11.1-3,A11.1-4 (SetRetainValues) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(SetRetainValues.class);
    }

    private PersistenceManagerFactory   pmf;
    private PersistenceManager          pm;
    private String 			pmfClass;
    private String                      url;
    private String 			username;
    private String 			password;

    private static  String  		PMFCLASS = "javax.jdo.PersistenceManagerFactoryClass";
    private static  String  		URL      = "javax.jdo.option.ConnectionURL";
    private static  String  		USERNAME = "javax.jdo.option.ConnectionUserName";
    private static  String  		PASSWORD = "javax.jdo.option.ConnectionPassword";

    /** set RetainValues to true or false and use getRetainValues value to verify */ 
    public void test() {
        Properties props = loadProperties(PMFProperties);
        pmfClass = props.getProperty(PMFCLASS);  
        url      = props.getProperty(URL);
        username = props.getProperty(USERNAME);  
        password = props.getProperty(PASSWORD);  

        try {
            Class cl = Class.forName(pmfClass);
            pmf = (PersistenceManagerFactory) cl.newInstance();
            pmf.setRetainValues(false);
            if (pmf.getRetainValues() != false) {
                fail(ASSERTION_FAILED,
                     "RetainValues set to false, value returned by PMF is " +
                     pmf.getRetainValues());
            }
            pmf.setRetainValues(true);
            if (pmf.getRetainValues() != true) {
                fail(ASSERTION_FAILED,
                     "RetainValues set to true, value returned by PMF is " +
                     pmf.getRetainValues());
            }
        } catch (Exception ex) {
            fail(ASSERTION_FAILED,
                 "Failed in setting RetainValues " + ex);
        }
    }
}
