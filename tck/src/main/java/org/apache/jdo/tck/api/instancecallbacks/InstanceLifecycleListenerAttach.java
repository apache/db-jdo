/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jdo.tck.api.instancecallbacks;

import java.util.Date;
import javax.jdo.listener.AttachCallback;
import javax.jdo.listener.InstanceLifecycleEvent;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test TestInstanceLifecycleListenerAttach <br>
 * <B>Keywords:</B> LifeCycleListener preAttach postAttach <br>
 * <B>Assertion IDs:</B> A12.15-13 A12.15-14 <br>
 * <B>Assertion Description: </B> A12.15-13 void preAttach(InstanceLifecycleEvent event); This
 * method is called before a detached instance is attached. The source instance is the detached
 * instance]. A12.15-14 void postAttach(InstanceLifecycleEvent event); This method is called after a
 * detached instance is attached. The source instance is the corresponding persistent instance in
 * the cache; the target instance is the detached instance.
 */
public class InstanceLifecycleListenerAttach extends AbstractInstanceLifecycleListener {

  /** The InstanceLifecycleListener used for this test */
  private final InstanceLifecycleListenerImpl listener = new InstanceLifecycleListenerAttachImpl();

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
    BatchTestRunner.run(InstanceLifecycleListenerAttach.class);
  }

  /**
   * This test creates a new persistent instance and detaches it. The instance is attached in a new
   * transaction, which causes the attach callbacks and listeners to be called.
   */
  public void testAttach() {

    // set up the persistent instance
    PC pc = new PC();

    getPM();
    pm.currentTransaction().begin();
    pm.makePersistent(pc);
    PC detached = pm.detachCopy(pc);
    detached.listener = listener;
    pm.currentTransaction().commit();
    pm.currentTransaction().begin();
    listener.setExpectedSource(detached);
    // makePersistent should cause the attach listeners to be called
    PC attached = pm.makePersistent(detached);
    pm.currentTransaction().commit();

    // now check the callback and listener were called
    listener.verifyCallbacks(
        ASSERTION13_FAILED,
        new int[] {
          InstanceLifecycleListenerImpl.PRE_ATTACH_LISTENER,
          InstanceLifecycleListenerImpl.PRE_ATTACH_CALLBACK,
          InstanceLifecycleListenerImpl.POST_ATTACH_LISTENER
        });
  }

  /** The LifeCycleListener to be registered with the PersistenceManager. */
  private static class InstanceLifecycleListenerAttachImpl extends InstanceLifecycleListenerImpl {

    @Override
    public void preAttach(InstanceLifecycleEvent event) {
      notifyEvent(PRE_ATTACH_LISTENER);
      checkEventType(ASSERTION13_FAILED, InstanceLifecycleEvent.ATTACH, event.getEventType());
      checkEventSource(ASSERTION13_FAILED, event.getSource());
    }

    @Override
    public void postAttach(InstanceLifecycleEvent event) {
      notifyEvent(POST_ATTACH_LISTENER);
      checkEventType(ASSERTION14_FAILED, InstanceLifecycleEvent.ATTACH, event.getEventType());
      checkPersistent(ASSERTION14_FAILED + "in postAttach, source ", event.getSource());
      checkTrue(
          ASSERTION14_FAILED + "in postAttach, source wasPostAttachCalled() ",
          ((PC) event.getSource()).wasPostAttachCalled());
      checkSame(ASSERTION14_FAILED + "in postAttach, target ", expectedSource, event.getTarget());
    }
  }

  /** The persistence-capable class that implements the attach callback. */
  public static class PC implements AttachCallback {
    transient InstanceLifecycleListenerImpl listener;

    transient boolean postAttachCalled = false;

    public boolean wasPostAttachCalled() {
      return postAttachCalled;
    }

    transient Object postAttachObject = null;

    public Object getPostAttachObject() {
      return postAttachObject;
    }

    int id;

    public PC() {}

    public PC(InstanceLifecycleListenerImpl listener) {
      id = counter++;
      this.listener = listener;
    }

    static int counter = (int) (new Date().getTime());

    public void jdoPreAttach() {
      if (listener != null) {
        listener.notifyEvent(InstanceLifecycleListenerImpl.PRE_ATTACH_CALLBACK);
      }
    }

    public void jdoPostAttach(Object obj) {
      postAttachCalled = true;
      postAttachObject = obj;
    }
  }
}
