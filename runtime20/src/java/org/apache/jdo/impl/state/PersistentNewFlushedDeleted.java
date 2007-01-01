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
 *  PersistentNewFlushedDeleted.java    March 10, 2001
 */

package org.apache.jdo.impl.state;

import java.util.BitSet;

import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.store.StoreManager;


/**
 * This class represents PersistentNewFlushedDeleted state specific state 
 * transitions as requested by StateManagerImpl. This state is the result
 * calls: makePersistent-flush-deletePersistent on the same instance.
 *
 * @author Marina Vatkina
 */
class PersistentNewFlushedDeleted extends PersistentNewDeleted {

    protected PersistentNewFlushedDeleted() {
        super();

        isFlushed = false;

        stateType = P_NEW_FLUSHED_DELETED;
    }


   /**
    * @see LifeCycleState#flush(BitSet loadedFields, BitSet dirtyFields,
    *   StoreManager srm, StateManagerImpl sm)
    */
    protected LifeCycleState flush(BitSet loadedFields, BitSet dirtyFields,
        StoreManager srm, StateManagerImpl sm) { 
        if (srm.delete(loadedFields, dirtyFields, sm) ==
                StateManagerInternal.FLUSHED_COMPLETE) {
            sm.markAsFlushed();
            return changeState(P_NEW_DELETED);
        }
        return this;
    }
}








