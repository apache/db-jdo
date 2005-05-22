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

/*
 *  AutoPersistentNewFlushed.java    September 4, 2001
 */

package org.apache.jdo.impl.state;

import java.util.BitSet;

import javax.jdo.JDOFatalInternalException;
import javax.jdo.Transaction;

import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.store.StoreManager;


/**
 * This class represents AutoPersistentNewFlushed state specific state
 * transitions as requested by StateManagerImpl. This state differs from
 * AutoPersistentNew state as the correspondinfg instance has been flushed
 * to a datastore.
 *
 * @author Marina Vatkina
 */
class AutoPersistentNewFlushed extends PersistentNewFlushed {
    
    protected AutoPersistentNewFlushed() {
        super();

        isAutoPersistent = true;
        stateType = AP_NEW_FLUSHED;
    }

   /**
    * @see LifeCycleState#transitionMakePersistent(StateManagerImpl sm)
    */
    protected LifeCycleState transitionMakePersistent(StateManagerImpl sm) {
        return changeState(P_NEW_FLUSHED);
    }

   /**
    * @see LifeCycleState#transitionDeletePersistent(StateManagerImpl sm)
    */
    protected LifeCycleState transitionDeletePersistent(StateManagerImpl sm) {
        sm.registerTransactional();
        sm.preDelete();
        return changeState(P_NEW_FLUSHED_DELETED);
    }

   /**
    * @see LifeCycleState#transitionWriteField(StateManagerImpl sm, 
    * Transaction tx)
    */
    protected LifeCycleState transitionWriteField(StateManagerImpl sm, 
        Transaction tx) { 
        sm.registerTransactional();
        return changeState(AP_NEW_FLUSHED_DIRTY); 
    }

   /**
    * This is a reverse operation if reached.
    * @see LifeCycleState#flush(BitSet loadedFields, BitSet dirtyFields,
    *   StoreManager srm, StateManagerImpl sm)
    */
    protected LifeCycleState flush(BitSet loadedFields, BitSet dirtyFields,
        StoreManager srm, StateManagerImpl sm) {
        if (sm.getPersistenceManager().insideCommit()) {
            // Need to restore state - it is unreachable at commit:          
            if (srm.delete(loadedFields, dirtyFields, sm) ==
                StateManagerInternal.FLUSHED_COMPLETE) {

                sm.markAsFlushed();
                return changeState(AP_PENDING); 
            }
        }
        return this;
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

   /**
    * @see LifeCycleState#transitionCommit(boolean retainValues, StateManagerImpl sm)
    */
    protected LifeCycleState transitionCommit(boolean retainValues, StateManagerImpl sm) {
        throw new JDOFatalInternalException(msg.msg(
            "EXC_InconsistentState", // NOI18N
            "commit", this.toString())); // NOI18N
    }
}

