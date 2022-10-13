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
import javax.jdo.listener.ClearCallback;
import javax.jdo.listener.InstanceLifecycleEvent;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test TestInstanceLifecycleListenerClear <br>
 * <B>Keywords:</B> LifeCycleListener hollow preClear postClear <br>
 * <B>Assertion IDs:</B> A12.15-5 A12.15-6 <br>
 * <B>Assertion Description: </B> A12.15-5 void preClear(InstanceLifecycleEvent event); This method
 * is called whenever a persistent instance is cleared, for example during afterCompletion. It is
 * called before the jdoPreClear method is invoked on the instance. A12.15-6 void
 * postClear(InstanceLifecycleEvent event); This method is called whenever a persistent instance is
 * cleared, for example during afterCompletion. It is called after the jdoPreClear method is invoked
 * on the instance and the fields have been cleared by the JDO implementation.
 */
public class InstanceLifecycleListenerClear extends AbstractInstanceLifecycleListener {

  /** The InstanceLifecycleListener used for this test */
  private final InstanceLifecycleListenerImpl listener = new InstanceLifecycleListenerClearImpl();

  /** Return the listener. */
  protected InstanceLifecycleListenerImpl getListener() {
    return listener;
  }

  /** The persistent classes used for this test. */
  @SuppressWarnings("rawtypes")
  private static final Class<?>[] persistentClasses = new Class[] {PC.class};

  /** Return the persistent classes. */
  protected Class<?>[] getPersistentClasses() {
    return persistentClasses;
  }

  /**
   * The <code>main</code> is called when the class is directly executed from the command line.
   *
   * @param args The arguments passed to the program.
   */
  public static void main(String[] args) {
    BatchTestRunner.run(InstanceLifecycleListenerClear.class);
  }

  /**
   * This test creates a new persistent instance and commits it. Since the RetainValues flag is set
   * to false, the persistent fields will be cleared, and the pre-clear listener, pre-clear
   * callback, and post-clear listener will be called.
   */
  public void testClear() {

    // set up the persistent instance
    PC pc = new PC(listener);
    listener.setExpectedSource(pc);

    // commit should cause the clear listeners to be called
    getPM();
    pm.currentTransaction().begin();
    pm.currentTransaction().setRetainValues(false);
    pm.makePersistent(pc);
    pm.currentTransaction().commit();

    // now check the callback and listeners were called
    listener.verifyCallbacks(
        ASSERTION5_FAILED,
        new int[] {
          InstanceLifecycleListenerImpl.PRE_CLEAR_LISTENER,
          InstanceLifecycleListenerImpl.PRE_CLEAR_CALLBACK,
          InstanceLifecycleListenerImpl.POST_CLEAR_LISTENER
        });
  }

  /** The LifeCycleListener to be registered with the PersistenceManager. */
  private static class InstanceLifecycleListenerClearImpl extends InstanceLifecycleListenerImpl {

    @Override
    public void preClear(InstanceLifecycleEvent event) {
      notifyEvent(PRE_CLEAR_LISTENER);
      checkEventType(ASSERTION5_FAILED, InstanceLifecycleEvent.CLEAR, event.getEventType());
      checkEventSource(ASSERTION5_FAILED, event.getSource());
      checkPersistent(ASSERTION5_FAILED + " in preClear, ", expectedSource);
    }

    @Override
    public void postClear(InstanceLifecycleEvent event) {
      notifyEvent(POST_CLEAR_LISTENER);
      checkEventType(ASSERTION6_FAILED, InstanceLifecycleEvent.CLEAR, event.getEventType());
      checkEventSource(ASSERTION6_FAILED, event.getSource());
      checkPersistent(ASSERTION6_FAILED + " in postClear, ", expectedSource);
    }
  }

  /** The persistence-capable class that implements the clear callback. */
  public static class PC implements ClearCallback {
    transient InstanceLifecycleListenerImpl listener;
    int id;

    public PC() {}

    public PC(InstanceLifecycleListenerImpl listener) {
      id = counter++;
      this.listener = listener;
    }

    static int counter = (int) (new Date().getTime());

    public void jdoPreClear() {
      if (listener != null) {
        listener.notifyEvent(InstanceLifecycleListenerImpl.PRE_CLEAR_CALLBACK);
      }
    }
  }
}
