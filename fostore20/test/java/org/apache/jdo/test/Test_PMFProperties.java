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

package org.apache.jdo.test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Properties;

import javax.jdo.JDOException;
import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import org.apache.jdo.impl.fostore.FOStorePMF;
import org.apache.jdo.test.util.AbstractTest;
import org.apache.jdo.test.util.JDORITestRunner;

/**
* Test the PersistenceManagerFactory.getPersistenceManagerFactory (Properties).
*
* @author Craig Russell
*/
public class Test_PMFProperties extends AbstractTest {

    /** */
    public static void main(String args[]) {
        JDORITestRunner.run(Test_PMFProperties.class);
    }

    /** */
    protected void setUp() {
        // create PMF here to allow configuration
        pmf = new FOStorePMF();
    }
    
    /**
     * 
     */
    public void test() throws Exception  {
        configPMF();
    }
    
    /**
     * Configures a PMF with some basic properties, and creates the
     * corresponding database.
     */
    protected void configPMF() throws Exception {
        PersistenceManagerFactory pmf2, pmf3, pmf4, pmf5, pmf6, pmf7;

        if( pmf instanceof FOStorePMF )
            ((FOStorePMF)pmf).setConnectionCreate(true);

        pmf.setConnectionUserName(System.getProperty ("user", "fred"));
        pmf.setConnectionPassword(System.getProperty ("password", "wombat"));

        String url = createURL();
        pmf.setConnectionURL(url);
        Properties propout;
        if( pmf instanceof FOStorePMF )
            propout = ((FOStorePMF)pmf).getAsProperties();
        else {
            initProperties();
            propout = this.properties;
        }

        writeProperties(propout, "testPropsBefore.tmp", "Original Properties");
        // use the properties to construct a new pmf from the same properties
        pmf = JDOHelper.getPersistenceManagerFactory (propout);
        // the same instance should be returned with the same properties.
        pmf2 = JDOHelper.getPersistenceManagerFactory (propout);
        if (pmf != pmf2) {
            dump (pmf, "original");
            dump (pmf2, "from properties");
            fail ("PersistenceManagerFactory instances don't match.");
        }
        // the same instance should be returned if a default property is set explicitly
        propout.setProperty("javax.jdo.option.Optimistic", "true");
        pmf3 = JDOHelper.getPersistenceManagerFactory (propout);
        if (pmf != pmf3) {
            dump (pmf, "original");
            dump (pmf3, "from properties");
            fail ("PersistenceManagerFactory instances don't match.");
        }
        // the same instance should be returned if an unknown property is set explicitly
        propout.setProperty("junque", "ignored");
        pmf4 = JDOHelper.getPersistenceManagerFactory (propout);
        if (pmf != pmf4) {
            dump (pmf, "original");
            dump (pmf4, "from properties");
            fail ("PersistenceManagerFactory instances don't match.");
        }
        // a different instance should be returned if a non-default property is set explicitly
        propout.setProperty("javax.jdo.option.Optimistic", "false");
        pmf5 = JDOHelper.getPersistenceManagerFactory (propout);
        if (pmf == pmf5) {
            dump (pmf, "original");
            dump (pmf5, "from properties");
            fail ("PersistenceManagerFactory instances match.");
        }
        // an exception should be thrown if the user doesn't configure ConnectionURL
        propout.remove("javax.jdo.option.ConnectionURL");
        writeProperties(propout, "testPropsAfter.tmp", "After removing javax.jdo.option.ConnectionURL");
        try {
            pmf6 = JDOHelper.getPersistenceManagerFactory (propout);
            fail ("Expected exception not caught (no ConnectionURL in properties).");
        } catch (JDOException e) {
            // ok to throw JDOFatalUserException
            if (!(e instanceof JDOFatalUserException)) {
                fail ("Unexpected exception caught (no ConnectionURL in properties): " + e);
            }
        }
        // configure using FOStoreConnectionFactory
        propout.setProperty ("org.apache.jdo.FOStoreConnectionFactory.option.URL", url);
        writeProperties(propout, "testPropsAfterConnectionFactory.tmp", "After adding FOStoreConnectionFactory");
        pmf7 = JDOHelper.getPersistenceManagerFactory (propout);
        if (pmf == pmf7) {
            dump (pmf, "original");
            dump (pmf7, "from properties");
            fail ("PersistenceManagerFactory instances match.");
        }
    }

    /** */
    protected void dump (PersistenceManagerFactory pmf, String header) throws IOException {
        Properties props;
        if( pmf instanceof FOStorePMF )
            props = ((FOStorePMF)pmf).getAsProperties();
        else
            props = this.properties;
        props.store (System.out, header);
    }

    /** */
    protected void writeProperties(final Properties props, 
                                   final String filename, 
                                   final String header) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = (FileOutputStream)AccessController.doPrivileged(
                new PrivilegedExceptionAction () {
                    public Object run () throws FileNotFoundException {
                        return new FileOutputStream(filename);
                    }});
        }
        catch (PrivilegedActionException ex) {
            // unwrap FileNotFoundException
            throw (FileNotFoundException)ex.getException();
        } 
        props.store(fos, header);
    }
    
}
