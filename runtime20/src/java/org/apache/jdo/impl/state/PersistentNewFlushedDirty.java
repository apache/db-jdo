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
 *  PersistentNewFlushedDirty.java    August 3, 2001
 */

package org.apache.jdo.impl.state;

import java.util.BitSet;

import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.store.StoreManager;


/**
 * This class represents PersistentNewFlushedDirty state specific state
 * transitions as requested by StateManagerImpl. This state is the result
 * calls: makePersistent-flush-write_field on the same instance.
 *
 * @author Marina Vatkina
 */
class PersistentNewFlushedDirty extends PersistentNew {
    
    protected PersistentNewFlushedDirty() {
        super();

        isFlushed = false;

        stateType = P_NEW_FLUSHED_DIRTY;
    }

   /**
    * @see LifeCycleState#transitionDeletePersistent(StateManagerImpl sm)
    */
    protected LifeCycleState transitionDeletePersistent(StateManagerImpl sm) {
        sm.preDelete();
        return changeState(P_NEW_FLUSHED_DELETED);
    }

   /**
    * @see LifeCycleState#flush(BitSet loadedFields, BitSet dirtyFields,
    *   StoreManager srm, StateManagerImpl sm)
    */
    protected LifeCycleState flush(BitSet loadedFields, BitSet dirtyFields,
        StoreManager srm, StateManagerImpl sm) {

        if(srm.update(loadedFields, dirtyFields, sm) ==
            StateManagerInternal.FLUSHED_COMPLETE) {
            sm.markAsFlushed();
            return changeState(P_NEW_FLUSHED);
        }

        return this;
    }
}

