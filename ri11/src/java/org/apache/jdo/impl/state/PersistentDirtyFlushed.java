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
 *  PersistentDirtyFlushed.java    August 8, 2001
 */

package org.apache.jdo.impl.state;

import java.util.BitSet;

import javax.jdo.Transaction;

import org.apache.jdo.store.StoreManager;


/**
 * This class represents PersistentDirtyFlushedstate specific state
 * transitions as requested by StateManagerImpl. This state differs from
 * PersistentDirty in that it is the result of flush operation.
 *
 * @author Marina Vatkina
 */
class PersistentDirtyFlushed extends PersistentDirty {

    PersistentDirtyFlushed() {
        isFlushed = true;

        stateType =  P_DIRTY_FLUSHED;
    }

   /**
    * @see LifeCycleState#transitionWriteField(StateManagerImpl sm,
    *    Transaction tx) 
    */
    protected LifeCycleState transitionWriteField(StateManagerImpl sm,
        Transaction tx) {
        return changeState(P_DIRTY);
    }

   /**
    * It is a no-op.
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
