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
 *  PersistentNewFlushed.java    March 10, 2001
 */

package org.apache.jdo.impl.state;

import java.util.BitSet;

import javax.jdo.Transaction;

import org.apache.jdo.store.StoreManager;

/**
 * This class represents PersistentNewFlushed state specific state
 * transitions as requested by StateManagerImpl. This state differs from
 * PersistentNew state as the correspondinfg instance has been flushed
 * to a datastore.
 *
 * @author Marina Vatkina
 */
class PersistentNewFlushed extends PersistentNew {
    
    protected PersistentNewFlushed() {
        super();
        isFlushed = true;

        stateType = P_NEW_FLUSHED;
    }

   /**
    * @see LifeCycleState#transitionDeletePersistent(StateManagerImpl sm)
    */
    protected LifeCycleState transitionDeletePersistent(StateManagerImpl sm) {
        sm.preDelete();
        return changeState(P_NEW_FLUSHED_DELETED);
    }

   /**
    * @see LifeCycleState#transitionWriteField(StateManagerImpl sm,
    * Transaction tx)
    */
    protected LifeCycleState transitionWriteField(StateManagerImpl sm,
        Transaction tx) { 
        return changeState(P_NEW_FLUSHED_DIRTY); 
    }

   /**
    * This is a no-op.
    * @see LifeCycleState#flush(BitSet loadedFields, BitSet dirtyFields,
    *   StoreManager srm, StateManagerImpl sm)
    */
    protected LifeCycleState flush(BitSet loadedFields, BitSet dirtyFields,
        StoreManager srm, StateManagerImpl sm) {
        return this;
    }

   /**
    * @see LifeCycleState#transitionCommit(boolean retainValues, StateManagerImpl sm)
    */
    protected LifeCycleState transitionCommit(boolean retainValues, StateManagerImpl sm) {
        int newstate;

        if (retainValues) {
            sm.replaceSCOFields();
            newstate = P_NON_TX;
        } else { 
            sm.clearFields(); 
            newstate = HOLLOW; 
        } 
        sm.reset(); 
        return changeState(newstate); 
    }
}

