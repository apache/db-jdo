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

import javax.jdo.listener.InstanceLifecycleEvent;

import org.apache.jdo.tck.pc.mylib.PCPoint;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test TestInstanceLifecycleListener
 * <BR>
 * <B>Keywords:</B> addLifeCycleListener removeLifeCycleListener
 * <BR>
 * <B>Assertion IDs:</B> A12.15-1
 * <BR>
 * <B>Assertion Description: </B>
 * A12.15-1 void postCreate(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent instance is created, 
 * during makePersistent. It is called after the instance transitions 
 * to persistent-new.
 */

public class InstanceLifecycleListenerCreate 
        extends AbstractInstanceLifecycleListener {

    /**
     * The InstanceLifecycleListener used for this test
     */
    private final InstanceLifecycleListenerImpl listener =
            new InstanceLifecycleListenerCreateImpl();

    /** Return the listener.
     */
    protected InstanceLifecycleListenerImpl getListener() {
        return listener;
    }

    /**
     * The persistent classes used for this test.
     */
    @SuppressWarnings("rawtypes")
    private final static Class<?>[] persistentClasses = new Class[] {PCPoint.class};

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
        BatchTestRunner.run(InstanceLifecycleListenerCreate.class);
    }

    /** */
    public void testCreate() {

        // set up the persistent instance
        PCPoint point = new PCPoint(12, 15);
        listener.setExpectedSource(point);

        // makePersistent should cause the create listener to be called
        getPM();
        pm.currentTransaction().begin();
        pm.makePersistent(point);
        pm.currentTransaction().commit();

        // verify that the listener was called
        listener.verifyCallbacks(ASSERTION1_FAILED, new int[] {
                InstanceLifecycleListenerImpl.POST_CREATE_LISTENER});
    }
    
    /** 
     * The LifeCycleListener class to be registered with the 
     * PersistenceManager.
     */
    private static class InstanceLifecycleListenerCreateImpl 
            extends InstanceLifecycleListenerImpl {

        @Override
        public void postCreate(InstanceLifecycleEvent event) {
            notifyEvent(POST_CREATE_LISTENER);
            checkEventType(ASSERTION1_FAILED,
                    InstanceLifecycleEvent.CREATE, event.getEventType());
            checkEventSource(ASSERTION1_FAILED, event.getSource());
            checkPersistent(ASSERTION1_FAILED + " in postCreate, ",
                    expectedSource);
            checkNew(ASSERTION1_FAILED + " in postCreate, ",
                    expectedSource);
        }

    }
}
