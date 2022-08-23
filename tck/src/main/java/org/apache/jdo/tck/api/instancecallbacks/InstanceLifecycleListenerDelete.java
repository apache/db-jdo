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
import javax.jdo.listener.DeleteCallback;
import javax.jdo.listener.InstanceLifecycleEvent;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test TestInstanceLifecycleListenerDelete <br>
 * <B>Keywords:</B> LifeCycleListener deletePersistent preDelete postDelete <br>
 * <B>Assertion IDs:</B> A12.15-7 A12.15-8 <br>
 * <B>Assertion Description: </B> A12.15-7 void preDelete(InstanceLifecycleEvent event); This method
 * is called whenever a persistent instance is deleted, during deletePersistent. It is called before
 * the state transition and before the jdoPreDelete method is invoked on the instance. A12.15-8 void
 * postDelete(InstanceLifecycleEvent event); This method is called whenever a persistent instance is
 * deleted, during deletePersistent. It is called after the jdoPreDelete method is invoked on the
 * instance and after the state transition.
 */
public class InstanceLifecycleListenerDelete extends AbstractInstanceLifecycleListener {

  /** The InstanceLifecycleListener used for this test */
  InstanceLifecycleListenerImpl listener = new InstanceLifecycleListenerDeleteImpl();

  /** Return the listener. */
  protected InstanceLifecycleListenerImpl getListener() {
    return listener;
  }

  /** The persistent classes used for this test. */
  private static Class[] persistentClasses = new Class[] {PC.class};

  /** Return the persistent classes. */
  protected Class[] getPersistentClasses() {
    return persistentClasses;
  }

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(InstanceLifecycleListenerDelete.class);
  }

  /**
   * This test creates a new persistent instance, and deletes it. The pre-delete listener,
   * pre-delete callback, and post-delete listener will be called.
   */
  public void testDelete() {

    // set up the persistent instance
    PC pc = new PC(listener);
    listener.setExpectedSource(pc);

    // deletePersistent should cause the delete listeners to be called
    getPM();
    pm.currentTransaction().begin();
    pm.makePersistent(pc);
    pm.deletePersistent(pc);
    pm.currentTransaction().commit();

    // now check the callback and listeners were called
    listener.verifyCallbacks(
        ASSERTION7_FAILED,
        (new int[] {
          listener.PRE_DELETE_LISTENER, listener.PRE_DELETE_CALLBACK, listener.POST_DELETE_LISTENER
        }));
  }

  /** The LifeCycleListener to be registered with the PersistenceManager. */
  private static class InstanceLifecycleListenerDeleteImpl extends InstanceLifecycleListenerImpl {

    public void preDelete(InstanceLifecycleEvent event) {
      notifyEvent(PRE_DELETE_LISTENER);
      checkEventType(ASSERTION7_FAILED, InstanceLifecycleEvent.DELETE, event.getEventType());
      checkEventSource(ASSERTION7_FAILED, event.getSource());
      checkPersistent(ASSERTION7_FAILED + " in preDelete, ", expectedSource);
      checkNotDeleted(ASSERTION7_FAILED + " in preDelete, ", expectedSource);
    }

    public void postDelete(InstanceLifecycleEvent event) {
      notifyEvent(POST_DELETE_LISTENER);
      checkEventType(ASSERTION8_FAILED, InstanceLifecycleEvent.DELETE, event.getEventType());
      checkEventSource(ASSERTION8_FAILED, event.getSource());
      checkPersistent(ASSERTION8_FAILED + " in postDelete, ", expectedSource);
      checkDeleted(ASSERTION8_FAILED + " in postDelete, ", expectedSource);
    }
  }

  /** The persistence-capable class that implements the delete callback. */
  public static class PC implements DeleteCallback {
    transient InstanceLifecycleListenerImpl listener;
    int id;

    public PC() {}

    public PC(InstanceLifecycleListenerImpl listener) {
      id = counter++;
      this.listener = listener;
    }

    static int counter = (int) (new Date().getTime());

    public void jdoPreDelete() {
      if (listener != null) {
        listener.notifyEvent(listener.PRE_DELETE_CALLBACK);
      }
    }
  }
}
