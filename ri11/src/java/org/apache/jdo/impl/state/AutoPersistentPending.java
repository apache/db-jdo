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

/*
 *  AutoPersistentPending.java    September 6, 2001
 */

package org.apache.jdo.impl.state;

import java.util.BitSet;

import org.apache.jdo.store.StoreManager;


/**
 * This class represents AutoPersistentPending state that is a result of a 
 * previous call to makePersistent on a transient instance that referenced this 
 * instance (persistence-by-reachability). This state represents instance
 * not reachable any more.
 *
 * @author Marina Vatkina
 */
class AutoPersistentPending extends AutoPersistentNewFlushed {

    AutoPersistentPending() {
        super();

        stateType = AP_PENDING;
    }

   /**
    * @see LifeCycleState#transitionFromAutoPersistent(StateManagerImpl sm)
    */
    protected LifeCycleState transitionFromAutoPersistent(StateManagerImpl sm) {
        sm.disconnect();
        return changeState(TRANSIENT);
    }

   /**
    * This is a no-op operation if reached.
    * @see LifeCycleState#flush(BitSet loadedFields, BitSet dirtyFields,
    *   StoreManager srm, StateManagerImpl sm)
    */
    protected LifeCycleState flush(BitSet loadedFields, BitSet dirtyFields,
        StoreManager srm, StateManagerImpl sm) {
        return this;
    }

   /**
    * Differs from the generic implementation in LifeCycleState that state transitions
    * to Transient and keeps all fields. Called only if state becomes not reachable.
    * @see LifeCycleState#transitionCommit(boolean retainValues, StateManagerImpl sm)
    */
    protected LifeCycleState transitionCommit(boolean retainValues, StateManagerImpl sm) {
        sm.disconnect();
        return changeState(TRANSIENT);
    }

   /**
    * Differs from the generic implementation in LifeCycleState that state transitions
    * to Transient and restores all fields. Called only if state becomes not reachable.
    * @see LifeCycleState#transitionRollback(boolean restoreValues, StateManagerImpl sm)
    */
    protected LifeCycleState transitionRollback(boolean restoreValues, StateManagerImpl sm) {
        if (restoreValues) {
            sm.restoreFields();
        } else {
            sm.unsetSCOFields();
        }
        sm.disconnect();
        return changeState(TRANSIENT);
    }

}

