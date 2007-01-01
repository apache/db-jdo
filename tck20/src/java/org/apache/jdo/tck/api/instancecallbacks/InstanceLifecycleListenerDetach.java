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

import javax.jdo.listener.DetachCallback;

import javax.jdo.JDOHelper;

import javax.jdo.listener.InstanceLifecycleEvent;
import javax.jdo.listener.InstanceLifecycleListener;
import javax.jdo.listener.ClearLifecycleListener;

import org.apache.jdo.tck.JDO_Test;

import org.apache.jdo.tck.util.BatchTestRunner;


/**
 * <B>Title:</B> Test InstanceLifecycleListenerDetach
 * <BR>
 * <B>Keywords:</B> LifeCycleListener preDetach postDetach
 * <BR>
 * <B>Assertion IDs:</B> A12.15-11 A12.15-12
 * <BR>
 * <B>Assertion Description: </B>
 * A12.15-11 void preDetach(InstanceLifecycleEvent event); 
 * This method is called before a persistent instance is copied 
 * for detachment.
 * A12.15-12 void postDetach(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent instance is copied 
 * for detachment. 
 * The source instance is the detached copy; the target instance
 * is the persistent instance.
 */

public class InstanceLifecycleListenerDetach 
        extends AbstractInstanceLifecycleListener {

    
    /**
     * The InstanceLifecycleListener used for this test
     */
    InstanceLifecycleListenerImpl listener = 
            new InstanceLifecycleListenerDetachImpl();

    /** Return the listener.
     */
    protected InstanceLifecycleListenerImpl getListener() {
        return listener;
    }

    /**
     * The persistent classes used for this test.
     */
    private static Class[] persistentClasses = new Class[] {PC.class};

    /** Return the persistent classes.
     */
    protected Class[] getPersistentClasses() {
        return persistentClasses;
    }

    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(InstanceLifecycleListenerLoad.class);
    }

    /** 
     * This test creates a new persistent instance and commits it.
     * Since the RetainValues flag is set to false, the persistent
     * fields will be cleared, and the pre-clear listener,
     * pre-clear callback, and post-clear listener will be called.
     */
    public void testDetach() {

        // set up the persistent instance
        PC pc = new PC(listener);
        listener.setExpectedSource(pc);

        getPM();
        pm.currentTransaction().begin();
        pm.makePersistent(pc);
        // detachCopy should cause the detach listeners to be called
        PC detached = (PC)pm.detachCopy(pc);
        pm.currentTransaction().commit();

        // now check the callback and listener were called
        listener.verifyCallbacks(ASSERTION11_FAILED, new int[] {
                listener.PRE_DETACH_LISTENER,
                listener.PRE_DETACH_CALLBACK,
                listener.POST_DETACH_LISTENER});
    }
    
    /** 
     * The LifeCycleListener to be registered with the 
     * PersistenceManager.
     */
    private static class InstanceLifecycleListenerDetachImpl 
            extends InstanceLifecycleListenerImpl {

        public void preDetach(InstanceLifecycleEvent event) {
            notifyEvent(PRE_DETACH_LISTENER);
            checkEventType(ASSERTION11_FAILED,
                    InstanceLifecycleEvent.DETACH, event.getEventType());
            checkEventSource(ASSERTION11_FAILED, event.getSource());
            checkPersistent(ASSERTION11_FAILED + "in preDetach, source ",
                    event.getSource());
        }

        public void postDetach(InstanceLifecycleEvent event) {
            notifyEvent(POST_DETACH_LISTENER);
            checkEventType(ASSERTION12_FAILED,
                    InstanceLifecycleEvent.DETACH, event.getEventType());
            checkSame(ASSERTION12_FAILED + "in postDetach, target ",
                    expectedSource, event.getTarget());
            checkNotPersistent(ASSERTION12_FAILED + "in postDetach, source ",
                    event.getSource());
            checkPersistent(ASSERTION12_FAILED + "in postDetach, target ",
                    event.getTarget());
            checkTrue(ASSERTION12_FAILED + 
                    "in postDetach, source wasPostDetachCalled() ",
                    ((PC)event.getSource()).wasPostDetachCalled());
        }

    }

    /** The persistence-capable class that implements the load callback.
     */
    public static class PC implements DetachCallback {
        transient InstanceLifecycleListenerImpl listener;
        int id; 
        public PC() {
        }
        public PC(InstanceLifecycleListenerImpl listener) {
            id = counter++;
            this.listener = listener;
        }
        static int counter = (int)(new Date().getTime());

        transient boolean postDetachCalled = false;
        public boolean wasPostDetachCalled() {
            return postDetachCalled;
        }

        transient Object postDetachObject = null;
        public Object getPostDetachObject() {
            return postDetachObject;
        }
 
        public void jdoPreDetach() {
            if (listener != null) {
                listener.notifyEvent(listener.PRE_DETACH_CALLBACK);
            }
        }
        public void jdoPostDetach(Object obj) {
            postDetachCalled = true;
            postDetachObject = obj;
        }
    }

}
