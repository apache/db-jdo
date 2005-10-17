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
 
package org.apache.jdo.tck.api.instancecallbacks;

import java.util.Date;

import javax.jdo.JDOHelper;

import javax.jdo.listener.InstanceLifecycleEvent;
import javax.jdo.listener.InstanceLifecycleListener;
import javax.jdo.listener.ClearLifecycleListener;

import org.apache.jdo.tck.JDO_Test;

import org.apache.jdo.tck.pc.mylib.PCPoint;

import org.apache.jdo.tck.util.BatchTestRunner;


/**
 * <B>Title:</B> Test InstanceLifecycleListenerDirty
 * <BR>
 * <B>Keywords:</B> LifeCycleListener preDirty postDirty
 * <BR>
 * <B>Assertion IDs:</B> A12.15-9 A12.15-10
 * <BR>
 * <B>Assertion Description: </B>
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
 */

public class InstanceLifecycleListenerDirty 
        extends AbstractInstanceLifecycleListener {

    
    /**
     * The InstanceLifecycleListener used for this test
     */
    InstanceLifecycleListenerImpl listener = 
            new InstanceLifecycleListenerDirtyImpl();

    /** Return the listener.
     */
    protected InstanceLifecycleListenerImpl getListener() {
        return listener;
    }

    /**
     * The persistent classes used for this test.
     */
    private static Class[] persistentClasses = new Class[] {PCPoint.class};

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
        BatchTestRunner.run(InstanceLifecycleListenerDirty.class);
    }

    /** 
     * This test creates a new persistent instance and commits it.
     * A new transaction is started and a persistent field is modified.
     * This will call the dirty listeners.
     */
    public void testDirty() {

        // set up the persistent instance
        PCPoint pc = new PCPoint(100, 200);
        listener.setExpectedSource(pc);

        // setY should cause the dirty listeners to be called
        getPM();
        pm.currentTransaction().begin();
        pm.makePersistent(pc);
        pm.currentTransaction().commit();
        pm.currentTransaction().begin();
        pc.setY(new Integer(666));
        pm.currentTransaction().commit();

        // now check the callback and listener were called
        listener.verifyCallbacks(ASSERTION10_FAILED, new int[] {
                listener.PRE_DIRTY_LISTENER,
                listener.POST_DIRTY_LISTENER});
    }
    
    /** 
     * The LifeCycleListener to be registered with the 
     * PersistenceManager.
     */
    private static class InstanceLifecycleListenerDirtyImpl 
            extends InstanceLifecycleListenerImpl {

        public void preDirty(InstanceLifecycleEvent event) {
            notifyEvent(PRE_DIRTY_LISTENER);
            checkEventType(ASSERTION9_FAILED,
                    InstanceLifecycleEvent.DIRTY, event.getEventType());
            checkEventSource(ASSERTION9_FAILED, event.getSource());
            checkPersistent(ASSERTION9_FAILED + " in preDirty, ",
                    expectedSource);
            checkNotDirty(ASSERTION9_FAILED + " in preDirty, ",
                    expectedSource);
        }

        public void postDirty(InstanceLifecycleEvent event) {
            notifyEvent(POST_DIRTY_LISTENER);
            checkEventType(ASSERTION10_FAILED,
                    InstanceLifecycleEvent.DIRTY, event.getEventType());
            checkEventSource(ASSERTION10_FAILED, event.getSource());
            checkPersistent(ASSERTION10_FAILED + " in postDirty,",
                    expectedSource);
            checkDirty(ASSERTION9_FAILED + " in postDirty, ",
                    expectedSource);
        }

    }

}
