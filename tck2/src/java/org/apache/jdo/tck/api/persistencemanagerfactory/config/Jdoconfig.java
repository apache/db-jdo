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
package org.apache.jdo.tck.api.persistencemanagerfactory.config;

import java.util.HashMap;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.mylib.PCPoint;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B>Close of PersistenceManagerFactory  
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory, jdoconfig.xml
 *<BR>
 *<B>Assertion IDs:</B> A11.1.2-1
 *<BR>
 *<B>Assertion Description: </B>
 * Users can access a PersistenceManagerFactory by creating a jdoconfig.xml file
 * and making it available on the class path as META-INF/jdoconfig.xml.
 */
public class Jdoconfig extends JDO_Test {

    /** Creates a new instance of Jdoconfig */
    public Jdoconfig() {
    }
    /** */
    private static final String ASSERTION_FAILED =
            "Assertion A11.1.2-1 failed: ";
    private static final String ANONYMOUS_PMF_NAME = "";
    // Do not use superclass pmf, pm
    private PersistenceManagerFactory pmf = null;
    private PersistenceManager pm = null;
    private HashMap overrides = new HashMap();

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(Jdoconfig.class);
    }
    
        /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        if (isTestToBePerformed()) {
            overrides.put("javax.jdo.mapping.Schema", schemaname);
        }
    }

    /** */
    public void testGetPMFNoArgs() {
        pmf = JDOHelper.getPersistenceManagerFactory();
        checkIsOpen(ANONYMOUS_PMF_NAME);
    }

    /** */
    public void testGetPMFEmptyString() {
        String name = "";
        pmf = JDOHelper.getPersistenceManagerFactory(name);
        checkIsOpen(ANONYMOUS_PMF_NAME);
    }

    /** */
    public void testGetPMFNull() {
        String name = null;
        pmf = JDOHelper.getPersistenceManagerFactory(name);
        checkIsOpen(ANONYMOUS_PMF_NAME);
    }

    /** */
    public void testGetPMFStringSpace() {
        String name = " ";
        pmf = JDOHelper.getPersistenceManagerFactory(name);
        checkIsOpen(ANONYMOUS_PMF_NAME);
    }

    /** */
    public void testGetPMFNamed() {
        String name = "namedPMF0";
        pmf = JDOHelper.getPersistenceManagerFactory(name);
        assertEquals("Incorrect value for RestoreValues",
                pmf.getRestoreValues(), false);
        checkIsOpen(name);
    }

    /** */
    public void testGetPMFEmptyStringOverrides() {
        String name = "";
        pmf = JDOHelper.getPersistenceManagerFactory(overrides, name);
        checkPersistent(ANONYMOUS_PMF_NAME);
    }

    /** */
    public void testGetPMFNullOverrides() {
        String name = null;
        pmf = JDOHelper.getPersistenceManagerFactory(overrides, name);
        checkPersistent(ANONYMOUS_PMF_NAME);
    }

    /** */
    public void testGetPMFStringSpaceOverrides() {
        String name = " ";
        pmf = JDOHelper.getPersistenceManagerFactory(overrides, name);
        checkPersistent(ANONYMOUS_PMF_NAME);
    }

    /** */
    public void testGetPMFNamedOverrides() {
        String name = "namedPMF0";
        pmf = JDOHelper.getPersistenceManagerFactory(overrides, name);
        assertEquals("Incorrect value for RestoreValues",
                pmf.getRestoreValues(), false);
        checkPersistent(name);
    }

    /** */
    public void testGetPMFNamedSpacesOverrides() {
        String name = "namedPMF1";
        pmf = JDOHelper.getPersistenceManagerFactory(overrides,
                " \t" + name + " \n");
        assertEquals("Incorrect value for RestoreValues",
                pmf.getRestoreValues(), true);
        checkPersistent(name);
    }

    /** */
    public void checkIsOpen(String name) {
        assertEquals("Incorrect PMF name", pmf.getName(), name);
        if (pmf.isClosed()) {
            fail(ASSERTION_FAILED,
                    "PMF.isClosed() returned true on an open pmf");
        }
        pmf.close();
        // have next invocation of getPMF() get a new pmf
        pmf = null;
    }

    /** */
    public void checkPersistent(String name) {
        assertEquals("Incorrect PMF name", pmf.getName(), name);

        makePersistent();

        pmf.close();
        if (!pmf.isClosed()) {
            fail(ASSERTION_FAILED,
                    "PMF.isClosed() returned false on a closed pmf");
        }
        // have next invocation of getPMF() get a new pmf
        pmf = null;
    }

    protected void makePersistent() {
        addTearDownClass(PCPoint.class);
        pm = pmf.getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        tx.begin();
        PCPoint comp = new PCPoint(1, 2);
        pm.makePersistent(comp);
        tx.commit();
    }
}
