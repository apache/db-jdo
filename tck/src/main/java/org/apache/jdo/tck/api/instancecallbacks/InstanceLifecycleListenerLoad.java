/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.api.instancecallbacks;

import java.util.Date;
import javax.jdo.listener.InstanceLifecycleEvent;
import javax.jdo.listener.LoadCallback;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 * <B>Title:</B> Test InstanceLifecycleListenerLoad <br>
 * <B>Keywords:</B> LifeCycleListener postLoad <br>
 * <B>Assertion IDs:</B> A12.15-2 <br>
 * <B>Assertion Description: </B> A12.15-2 void postLoad(InstanceLifecycleEvent event); This method
 * is called whenever a persistent instance is loaded. It is called after the jdoPostLoad method is
 * invoked on the instance.
 */
public class InstanceLifecycleListenerLoad extends AbstractInstanceLifecycleListener {

  /** The InstanceLifecycleListener used for this test */
  private final InstanceLifecycleListenerImpl listener = new InstanceLifecycleListenerLoadImpl();

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
    BatchTestRunner.run(InstanceLifecycleListenerLoad.class);
  }

  /**
   * This test creates a new persistent instance and commits it. In a new transaction, a value is
   * accessed, which causes the instance to be loaded.
   */
  public void testLoad() {

    // set up the persistent instance
    PC pc = new PC(listener);
    listener.setExpectedSource(pc);

    // field access should cause the load listeners to be called
    getPM();
    pm.currentTransaction().begin();
    pm.currentTransaction().setRetainValues(false);
    pm.makePersistent(pc);
    pm.currentTransaction().commit();
    pm.currentTransaction().begin();
    pc.getValue();
    pm.currentTransaction().commit();

    // now check the callback and listener were called
    listener.verifyCallbacks(
        ASSERTION2_FAILED,
        new int[] {
          InstanceLifecycleListenerImpl.POST_LOAD_CALLBACK,
          InstanceLifecycleListenerImpl.POST_LOAD_LISTENER
        });
  }

  /** The LifeCycleListener to be registered with the PersistenceManager. */
  private static class InstanceLifecycleListenerLoadImpl extends InstanceLifecycleListenerImpl {

    @Override
    public void postLoad(InstanceLifecycleEvent event) {
      notifyEvent(POST_LOAD_LISTENER);
      checkEventType(ASSERTION2_FAILED, InstanceLifecycleEvent.LOAD, event.getEventType());
      checkEventSource(ASSERTION2_FAILED, event.getSource());
      checkPersistent(ASSERTION2_FAILED + " in postLoad, ", expectedSource);
      checkNotNew(ASSERTION2_FAILED + " in postLoad, ", expectedSource);
    }
  }

  /** The persistence-capable class that implements the load callback. */
  public static class PC implements LoadCallback {
    transient InstanceLifecycleListenerImpl listener;
    int id;
    int value;

    public int getValue() {
      return value;
    }

    public PC() {}

    public PC(InstanceLifecycleListenerImpl listener) {
      id = counter++;
      this.listener = listener;
    }

    static int counter = (int) (new Date().getTime());

    public void jdoPostLoad() {
      if (listener != null) {
        listener.notifyEvent(InstanceLifecycleListenerImpl.POST_LOAD_CALLBACK);
      }
    }
  }
}
