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

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;

import javax.jdo.listener.InstanceLifecycleEvent;
import javax.jdo.listener.InstanceLifecycleListener;
import javax.jdo.listener.AttachLifecycleListener;
import javax.jdo.listener.ClearLifecycleListener;
import javax.jdo.listener.CreateLifecycleListener;
import javax.jdo.listener.DeleteLifecycleListener;
import javax.jdo.listener.DetachLifecycleListener;
import javax.jdo.listener.DirtyLifecycleListener;
import javax.jdo.listener.LoadLifecycleListener;
import javax.jdo.listener.StoreLifecycleListener;

import org.apache.jdo.tck.JDO_Test;

import org.apache.jdo.tck.pc.mylib.PCPoint;

import org.apache.jdo.tck.util.BatchTestRunner;


/**
 * <B>Title:</B> Abstract test AbstractInstanceLifecycleListener
 * <BR>
 * <B>Keywords:</B> LifeCycleListener lifecycle listener callback
 * <BR>
 * <B>Assertion IDs:</B> A12.15-1, A12.15-2, A12.15-3, A12.15-4, A12.15-5, 
 * A12.15-6, A12.15-7, A12.15-8, A12.15-9, A12.15-10, A12.15-11, A12.15-12, 
 * A12.15-13, A12.15-14, 
 * <BR>
 * <B>Assertion Description: </B>
 * A12.15-1 void postCreate(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent instance is created, 
 * during makePersistent. It is called after the instance transitions 
 * to persistent-new.
 * A12.15-2 void postLoad(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent instance is loaded. 
 * It is called after the jdoPostLoad method is invoked on the instance.
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
 * A12.15-5 void preClear(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent instance 
 * is cleared, for example during afterCompletion. It is called 
 * before the jdoPreClear method is invoked on the instance.
 * A12.15-6 void postClear(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent instance 
 * is cleared, for example during afterCom?pletion. It is called 
 * after the jdoPreClear method is invoked on the instance and the fields 
 * have been cleared by the JDO implementation.
 * A12.15-7 void preDelete(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent instance 
 * is deleted, during deletePersistent. It is called before 
 * the state transition and before the jdoPreDelete method 
 * is invoked on the instance.
 * A12.15-8 void postDelete(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent instance 
 * is deleted, during deletePersistent. It is called 
 * after the jdoPreDelete method is invoked on the instance
 * and after the state transition.
 * A12.15-9 void preDirty(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent clean instance 
 * is first made dirty, during an operation that modifies 
 * the value of a persistent or transactional field. It is called
 * before the field value is changed.
 * A12.15-10 void postDirty(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent clean instance 
 * is first made dirty, during an operation that modifies 
 * the value of a persistent or transactional field. It is called
 * after the field value was changed.
 * A12.15-11 void preDetach(InstanceLifecycleEvent event); 
 * This method is called before a persistent instance is copied 
 * for detachment.
 * A12.15-12 void postDetach(InstanceLifecycleEvent event); 
 * This method is called whenever a persistent instance is copied 
 * for detachment. 
 * The source instance is the detached copy; the target instance
 * is the persistent instance.
 * A12.15-13 void preAttach(InstanceLifecycleEvent event); 
 * This method is called before a detached instance is attached. 
 * The source instance is the detached instance].
 * A12.15-14 void postAttach(InstanceLifecycleEvent event);
 * This method is called after a detached instance is attached. 
 * The source instance is the corresponding persistent instance in the cache;
 * the target instance is the detached instance.
 *
 * This is the abstract base class for the lifecycle listener test classes.
 * It contains constants, variables, and methods used by each listener test
 * class. This class implements the setup and teardown methods used by each
 * concrete test class.
 *
 * This class also includes the listener base class that is extended by
 * each specific listener. 
 *
 * The strategy of each test case is the same. A specific listener is
 * implemented that responds only to events for that specific test; a
 * specific persistence-capable class is also implemented, that responds
 * only to callbacks for the specific test. A sequence of operations that
 * stimulates the callbacks is executed. During these operations, listener
 * events and callbacks are collected by the listener instance. Finally,
 * the actual events and callbacks that were collected are analyzed and
 * differences are reported.
 */

public abstract class AbstractInstanceLifecycleListener extends JDO_Test {

    /** */
    protected static final String ASSERTION1_FAILED = 
        "Assertion A12.15-1 (TestInstanceLifecycleListenerCreate) failed: ";
    protected static final String ASSERTION2_FAILED = 
        "Assertion A12.15-2 (TestInstanceLifecycleListener) failed: ";
    protected static final String ASSERTION3_FAILED = 
        "Assertion A12.15-3 (TestInstanceLifecycleListener) failed: ";
    protected static final String ASSERTION4_FAILED = 
        "Assertion A12.15-4 (TestInstanceLifecycleListener) failed: ";
    protected static final String ASSERTION5_FAILED = 
        "Assertion A12.15-5 (TestInstanceLifecycleListener) failed: ";
    protected static final String ASSERTION6_FAILED = 
        "Assertion A12.15-6 (TestInstanceLifecycleListener) failed: ";
    protected static final String ASSERTION7_FAILED = 
        "Assertion A12.15-7 (TestInstanceLifecycleListenerDelete) failed: ";
    protected static final String ASSERTION8_FAILED = 
        "Assertion A12.15-8 (TestInstanceLifecycleListenerDelete) failed: ";
    protected static final String ASSERTION9_FAILED = 
        "Assertion A12.15-9 (TestInstanceLifecycleListener) failed: ";
    protected static final String ASSERTION10_FAILED = 
        "Assertion A12.15-10 (TestInstanceLifecycleListener) failed: ";
    protected static final String ASSERTION11_FAILED = 
        "Assertion A12.15-11 (TestInstanceLifecycleListener) failed: ";
    protected static final String ASSERTION12_FAILED = 
        "Assertion A12.15-12 (TestInstanceLifecycleListener) failed: ";
    protected static final String ASSERTION13_FAILED = 
        "Assertion A12.15-13 (TestInstanceLifecycleListener) failed: ";
    protected static final String ASSERTION14_FAILED = 
        "Assertion A12.15-14 (TestInstanceLifecycleListener) failed: ";

    /** 
     * The listener to be used for the test. This method is implemented
     * by the subclasses to use the proper listener.
     * @return listener
     */
    protected abstract InstanceLifecycleListenerImpl getListener();

    /** 
     * The classes to be used for the test. This method is implemented
     * by the subclasses to use the proper classes. The classes returned
     * by this method are used for two purposes: they are used to register
     * listeners; and they are used as the teardown classes whose instances
     * are removed from the datastore at the end of the test.
     * @return classes
     */
    protected abstract Class[] getPersistentClasses();

    /** 
     * Register a new InstanceLifecycleListenerImpl with the 
     * PersistenceManager.
     */
    protected void addListener() {
        getPM();
        pm.addInstanceLifecycleListener(getListener(), getPersistentClasses());
    }
    
    /**
     * Unregister the InstanceLifecycleListenerImpl with the 
     * PersistenceManager.
     */
    protected void removeListener() {
        getPM();
        pm.removeInstanceLifecycleListener(getListener());
    }

    /** 
     * Set up for lifecycle tests:
     * Register the LifecycleListener; add PCPoint to tearDownClasses.
     */
    protected void localSetUp() {
        addListener();
        addTearDownClass(getPersistentClasses());
    }

    /**
     * Clean up after lifecycle tests:
     * Unregister the LifecycleListener.
     */
    protected void localTearDown() {
        removeListener(); // no callbacks for teardown
        super.localTearDown();
    }

    /** 
     * The LifeCycleListener to be registered with the 
     * PersistenceManager. This is the base class that is extended 
     * by each test.
     */
    protected abstract static class InstanceLifecycleListenerImpl 
            implements AttachLifecycleListener, ClearLifecycleListener,
            CreateLifecycleListener, DeleteLifecycleListener,
            DetachLifecycleListener, DirtyLifecycleListener,
            LoadLifecycleListener, StoreLifecycleListener {

        protected InstanceLifecycleListenerImpl() {}

        protected Object expectedSource = null;
        protected Object expectedTarget = null;
        
        public void setExpectedSource(Object source) {
            expectedSource = source;
        }

        public void setExpectedTarget(Object target) {
            expectedTarget = target;
        }

        /** 
         * This is the list names of locations for listener events and 
         * callbacks. Each instance of an event or callback has a specific 
         * name that is associated with the value. The values
         * are used during collection of events and both names and values
         * are used for reporting results.
         */
        private static List locations = new ArrayList();

        /** These are indexes into the sequence array. Each index
         * represents the position in the sequence array for the
         * corresponding callback or listener event.
         */
        public static final int PRE_ATTACH_CALLBACK;
        public static final int POST_ATTACH_CALLBACK;
        public static final int PRE_ATTACH_LISTENER;
        public static final int POST_ATTACH_LISTENER;
        public static final int PRE_CLEAR_CALLBACK;
        public static final int PRE_CLEAR_LISTENER;
        public static final int POST_CLEAR_LISTENER;
        public static final int POST_CREATE_LISTENER;
        public static final int PRE_DELETE_CALLBACK;
        public static final int PRE_DELETE_LISTENER;
        public static final int POST_DELETE_LISTENER;
        public static final int PRE_DETACH_CALLBACK;
        public static final int POST_DETACH_CALLBACK;
        public static final int PRE_DETACH_LISTENER;
        public static final int POST_DETACH_LISTENER;
        public static final int PRE_DIRTY_LISTENER;
        public static final int POST_DIRTY_LISTENER;
        public static final int POST_LOAD_CALLBACK;
        public static final int POST_LOAD_LISTENER;
        public static final int PRE_STORE_CALLBACK;
        public static final int PRE_STORE_LISTENER;
        public static final int POST_STORE_LISTENER;

        /*
         * Initialize the list of names and the associated values. For each
         * listener event and callback, add the name and then assign the value.
         */
        static {
            int index = 0;
            locations.add("PRE_ATTACH_LISTENER");
            PRE_ATTACH_LISTENER = index++;
            locations.add("PRE_ATTACH_CALLBACK");
            PRE_ATTACH_CALLBACK = index++;
            locations.add("POST_ATTACH_CALLBACK");
            POST_ATTACH_CALLBACK = index++;
            locations.add("POST_ATTACH_LISTENER");
            POST_ATTACH_LISTENER = index++;
            locations.add("PRE_CLEAR_LISTENER");
            PRE_CLEAR_LISTENER = index++;
            locations.add("PRE_CLEAR_CALLBACK");
            PRE_CLEAR_CALLBACK = index++;
            locations.add("POST_CLEAR_LISTENER");
            POST_CLEAR_LISTENER = index++;
            locations.add("POST_CREATE_LISTENER");
            POST_CREATE_LISTENER = index++;
            locations.add("PRE_DELETE_LISTENER");
            PRE_DELETE_LISTENER = index++;
            locations.add("PRE_DELETE_CALLBACK");
            PRE_DELETE_CALLBACK = index++;
            locations.add("POST_DELETE_LISTENER");
            POST_DELETE_LISTENER = index++;
            locations.add("POST_DETACH_CALLBACK");
            POST_DETACH_CALLBACK = index++;
            locations.add("PRE_DETACH_LISTENER");
            PRE_DETACH_LISTENER = index++;
            locations.add("PRE_DETACH_CALLBACK");
            PRE_DETACH_CALLBACK = index++;
            locations.add("POST_DETACH_LISTENER");
            POST_DETACH_LISTENER = index++;
            locations.add("PRE_DIRTY_LISTENER");
            PRE_DIRTY_LISTENER = index++;
            locations.add("POST_DIRTY_LISTENER");
            POST_DIRTY_LISTENER = index++;
            locations.add("POST_LOAD_CALLBACK");
            POST_LOAD_CALLBACK = index++;
            locations.add("POST_LOAD_LISTENER");
            POST_LOAD_LISTENER = index++;
            locations.add("PRE_STORE_LISTENER");
            PRE_STORE_LISTENER = index++;
            locations.add("PRE_STORE_CALLBACK");
            PRE_STORE_CALLBACK = index++;
            locations.add("POST_STORE_LISTENER");
            POST_STORE_LISTENER = index++;
        }

        private int[] actual = new int[locations.size()];

        /** Track the current sequence of callbacks.
         */
        private int current = 0;

        public void notifyEvent(int event) {
            actual[event] = ++current;
        }

        /**
         * Initialize the expected ordering of callbacks and listeners.
         * Each entry in the sequence is one of the PRE_ or POST_XXX_LISTENER
         * or _CALLBACK above. The order in the sequence is the expected
         * calling order.
         */
        private static int[] newExpected(int[] sequence) {
            int order = 0;
            int[] result = new int[locations.size()];
            for (int i = 0; i < sequence.length; ++i) {
                result[sequence[i]] = ++order;
            }
            return result;
        }

        /** 
         * The error message buffer to report exceptions.
         */
        protected StringBuffer messages = new StringBuffer();
        
        protected void checkEventType(String where, int expected, int actual) {
            if (expected != actual) {
                messages.append(where + "wrong event type: " +
                        "expected <" + expected +
                        ">, actual <" + actual + ">\n");
            }
        }
        
        protected void checkEventSource(String where, Object eventSource) {
            if (expectedSource != eventSource) {
                messages.append(where + "wrong event source: " +
                        "expected <" + expectedSource +
                        ">, actual <" + eventSource + ">\n");
            }            
        }

        protected void checkEventTarget(String where, Object eventTarget) {
            if (expectedTarget != eventTarget) {
                messages.append(where + "wrong event target: " +
                        "expected <" + expectedTarget +
                        ">, actual <" + eventTarget + ">\n");
            }            
        }

        protected void checkTrue(String where, boolean condition) {
            if (!condition) {
                messages.append(where + "was not true.\n");
            }
        }

        protected void checkSame(String where, 
                Object expected, Object actual) {
            if (expected != actual) {
                messages.append(where + 
                        "expected <" + expected +
                        ">, actual <" + actual + ">\n");
            }            
        }

        protected void checkPersistent(String where, Object obj) {
            if (!JDOHelper.isPersistent(obj)) {
                messages.append(where + "object should be persistent.\n");
            }
        }

        protected void checkNotPersistent(String where, Object obj) {
            if (JDOHelper.isPersistent(obj)) {
                messages.append(where + "object should not be persistent.\n");
            }
        }

        protected void checkDirty(String where, Object obj) {
            if (!JDOHelper.isDirty(obj)) {
                messages.append(where + "object should be dirty.\n");
            }
        }

        protected void checkNotDirty(String where, Object obj) {
            if (JDOHelper.isDirty(obj)) {
                messages.append(where + "object should not be dirty.\n");
            }
        }

        protected void checkNew(String where, Object obj) {
            if (!JDOHelper.isNew(obj)) {
                messages.append(where + "object should be new.\n");
            }
        }

        protected void checkNotNew(String where, Object obj) {
            if (JDOHelper.isNew(obj)) {
                messages.append(where + "object should not be new.\n");
            }
        }

        protected void checkDeleted(String where, Object obj) {
            if (!JDOHelper.isDeleted(obj)) {
                messages.append(where + "object should be deleted.\n");
            }
        }

        protected void checkNotDeleted(String where, Object obj) {
            if (JDOHelper.isDeleted(obj)) {
                messages.append(where + "object should not be deleted.\n");
            }
        }

        /** 
         * Verify the actual sequence of callbacks and listeners
         * against the expected. Each of the expected and actual are
         * int arrays in which each listener and callback have one
         * position in the array and the value of the array at that
         * position is the order, starting with 1, of the callback.
         * If the callback or listener was never invoked, the value
         * is 0.
         * @param where where
         * @param expected expected
         */
        public void verifyCallbacks(String where, int[] expected) {
            int[] expectedSequence = newExpected(expected);
            for (int index = 0; index < locations.size(); ++index) {
                if (expectedSequence[index] != actual[index]) {
                    messages.append("\nSequence verification failed for " +
                            locations.get(index) + 
                            "; expected: <" + expectedSequence[index] +
                            "> actual: <" + actual[index] +
                            ">\n");
                }
            }
            if (messages.length() > 0) {
                fail(where + "\n" + messages.toString());
            }
        }

        public void postAttach(InstanceLifecycleEvent event) {
        }

        public void postClear(InstanceLifecycleEvent event) {
        }

        public void postCreate(InstanceLifecycleEvent event) {
        }

        public void postDelete(InstanceLifecycleEvent event) {
        }

        public void postDetach(InstanceLifecycleEvent event) {
        }

        public void postDirty(InstanceLifecycleEvent event) {
        }

        public void postLoad(InstanceLifecycleEvent event) {
        }

        public void postStore(InstanceLifecycleEvent event) {
        }

        public void preAttach(InstanceLifecycleEvent event) {
        }

        public void preClear(InstanceLifecycleEvent event) {
        }

        public void preDelete(InstanceLifecycleEvent event) {
        }

        public void preDetach(InstanceLifecycleEvent event) {
        }

        public void preDirty(InstanceLifecycleEvent event) {
        }

        public void preStore(InstanceLifecycleEvent event) {
        }
        
    }
}
