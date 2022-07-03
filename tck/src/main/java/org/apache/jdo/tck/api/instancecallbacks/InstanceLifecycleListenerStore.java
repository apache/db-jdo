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
 
package org.apache.jdo.tck.api.instancecallbacks;

import java.util.Date;

import javax.jdo.listener.StoreCallback;


import javax.jdo.listener.InstanceLifecycleEvent;

import org.apache.jdo.tck.util.BatchTestRunner;


/**
 * <B>Title:</B> Test InstanceLifecycleListenerLoad
 * <BR>
 * <B>Keywords:</B> LifeCycleListener preStore postStore
 * <BR>
 * <B>Assertion IDs:</B> A12.15-3 A12.15-4
 * <BR>
 * <B>Assertion Description: </B>
 * A12.15-3 void preStore(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent instance is stored, 
 * for example during flush or commit. It is called 
 * before the jdoPreStore method is invoked on the instance.
 * A12.15-4 void postStore(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent instance is stored,
 * for example during flush or commit. It is called 
 * after the jdoPreStore method is invoked on the instance. 
 * An object identity for a per?sistent-new instance must have been assigned
 * to the instance when this callback is invoked.
 */

public class InstanceLifecycleListenerStore 
        extends AbstractInstanceLifecycleListener {

    
    /**
     * The InstanceLifecycleListener used for this test
     */
    InstanceLifecycleListenerImpl listener = 
            new InstanceLifecycleListenerStoreImpl();

    /** Return the listener.
     */
    protected InstanceLifecycleListenerImpl getListener() {
        return listener;
    }

    /**
     * The persistent classes used for this test.
     */
    private static Class<?>[] persistentClasses = new Class[] {PC.class};

    /** Return the persistent classes.
     */
    protected Class<?>[] getPersistentClasses() {
        return persistentClasses;
    }

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(InstanceLifecycleListenerStore.class);
    }

    /** 
     * This test creates a new persistent instance and commits it.
     * The store callback and listeners will be called.
     */
    public void testStore() {

        // set up the persistent instance
        PC pc = new PC(listener);
        listener.setExpectedSource(pc);

        // commit should cause the store listeners to be called
        getPM();
        pm.currentTransaction().begin();
        pm.makePersistent(pc);
        pm.currentTransaction().commit();

        // now check the callback and listener were called
        listener.verifyCallbacks(ASSERTION2_FAILED, new int[] {
                listener.PRE_STORE_LISTENER,
                listener.PRE_STORE_CALLBACK,
                listener.POST_STORE_LISTENER});
    }
    
    /** 
     * The LifeCycleListener to be registered with the 
     * PersistenceManager.
     */
    private static class InstanceLifecycleListenerStoreImpl 
            extends InstanceLifecycleListenerImpl {

        public void preStore(InstanceLifecycleEvent event) {
            notifyEvent(PRE_STORE_LISTENER);
            checkEventType(ASSERTION3_FAILED,
                    InstanceLifecycleEvent.STORE, event.getEventType());
            checkEventSource(ASSERTION3_FAILED, event.getSource());
            checkPersistent(ASSERTION3_FAILED + " in preStore, ",
                    expectedSource);
        }

        public void postStore(InstanceLifecycleEvent event) {
            notifyEvent(POST_STORE_LISTENER);
            checkEventType(ASSERTION4_FAILED,
                    InstanceLifecycleEvent.STORE, event.getEventType());
            checkEventSource(ASSERTION4_FAILED, event.getSource());
            checkPersistent(ASSERTION4_FAILED + " in postStore, ",
                    expectedSource);
        }

    }

    /** The persistence-capable class that implements the load callback.
     */
    public static class PC implements StoreCallback {
        transient InstanceLifecycleListenerImpl listener;
        int id; 
        public PC() {
        }
        public PC(InstanceLifecycleListenerImpl listener) {
            id = counter++;
            this.listener = listener;
        }
        static int counter = (int)(new Date().getTime());

        public void jdoPreStore() {
            if (listener != null) {
                listener.notifyEvent(listener.PRE_STORE_CALLBACK);
            }
        }
    }

}
