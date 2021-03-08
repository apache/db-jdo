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
 *<B>Title:</B>JDOConfigListener
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory, jdoconfig.xml
 *<BR>
 *<B>Assertion IDs:</B> A11.1-40
 *<BR>
 *<B>Assertion Description: </B>
 Properties whose key begins with the prefix
 * "javax.jdo.listener.InstanceLifecycleListener." have special treatment.
 * The part of the key following the prefix is used as the class name of a class
 * that implements javax.jdo.listener.InstanceLifecycleListener.
 * The implementation first attempts to get an instance via the static method
 * getInstance() that returns an instance that implements the 
 * javax.jdo.listener.InstanceLifecycleListener. If this is unsuccessful,
 * an instance of this class is constructed via a no-args constructor.
 * The value of the key is a comma-separated list of classes to which 
 * the instantiated listener is registered to listen.
 */

public class JDOConfigListener extends JDO_Test {

    /** Creates a new instance of JDOConfigListener */
    public JDOConfigListener() {
    }
    /** */
    private static final String ASSERTION_FAILED =
            "Assertions 11.1-40 failed: ";
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
        BatchTestRunner.run(JDOConfigListener.class);
    }

    /**
     * @see org.apache.jdo.tck.JDO_Test#localSetUp()
     */
    @Override
    protected void localSetUp() {
        /* Instance can be persisted ONLY if javax.jdo.mapping.Schema
         *   is overriden with 
         *   JDOHelper.getPersistenceManagerFactory(Map overrides, ...).
         *   The value depends on identity type so can only be known at runtime.
         */
        if (isTestToBePerformed()) {
            overrides.put("javax.jdo.mapping.Schema", SCHEMANAME);
        }
    }

    /** 
     * Test instantiation of InstanceLifecycleListener where listener class 
     * and target classes are specified in jdoconfig.xml where listener
     * class provides only a public no-args constructor.
     */
    public void testPCPointListener() {
        String pmfname = "testInstanceLifecycleListenerStore";
        pmf = JDOHelper.getPersistenceManagerFactory(overrides, pmfname);
        makePersistent();
        assertTrue("Expected preStore() to have been invoked.",
                StoreListener.isPreStore());
        assertTrue("Expected postStore() to have been invoked.",
                StoreListener.isPostStore());
    }

    /** 
     * Test instantiation of InstanceLifecycleListener where listener class
     * and target classes are specified in jdoconfig.xml where listener
     * class provides a static method getInstance().
     */
    public void testPCPointListenerGetInstance() {
        String pmfname = "testInstanceLifecycleListenerStoreGetInstance";
        pmf = JDOHelper.getPersistenceManagerFactory(overrides, pmfname);
        makePersistent();
        assertTrue("Expected preStore() to have been invoked.",
                StoreListenerGetInstance.isPreStore());
        assertTrue("Expected postStore() to have been invoked.",
                StoreListenerGetInstance.isPostStore());
    }

    /** 
     * Test instantiation of Instance LifeCycleListener specified via 
     * property overrides.
     */
    public void testPCPointListenerOverrides() {
        overrides.put("javax.jdo.listener.InstanceLifecycleListener.org.apache.jdo.tck.api.persistencemanagerfactory.config.StoreListener",
                "org.apache.jdo.tck.pc.mylib.PCPoint");
        String pmfname = "testInstanceLifecycleListenerStoreOverrides";
        pmf = JDOHelper.getPersistenceManagerFactory(overrides, pmfname);
        makePersistent();
        assertTrue("Expected preStore() to have been invoked.",
                StoreListener.isPreStore());
        assertTrue("Expected postStore() to have been invoked.",
                StoreListener.isPostStore());
    }

    /** 
     * Test instantiation of InstanceLifecycleListener where class value
     * is null
     */
    public void testPCPointListenerNullClasses() {
        String pmfname = "testInstanceLifecycleListenerClassesNull";
        pmf = JDOHelper.getPersistenceManagerFactory(overrides, pmfname);
        makePersistent();
        assertTrue("Expected preStore() to have been invoked.",
                StoreListener.isPreStore());
        assertTrue("Expected postStore() to have been invoked.",
                StoreListener.isPostStore());
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

    protected void localTearDown() {
        super.localTearDown();
        StoreListener.resetValues();
        StoreListenerGetInstance.resetValues();
        closePMF();
    }
}
