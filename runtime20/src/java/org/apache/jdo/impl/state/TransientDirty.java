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
 *  TransientDirty.java    August 10, 2001
 */

package org.apache.jdo.impl.state;

import javax.jdo.Transaction;

/**
 * This class represents TransientDirty state specific state transitions as
 * requested by StateManagerImpl. This state is the result of a wrie operation
 * on a TransientClean instance
 *
 * @author Marina Vatkina
 */
class TransientDirty extends LifeCycleState {

    TransientDirty() {
        // these flags are set only in the constructor 
        // and shouldn't be changed afterwards
        // (cannot make them final since they are declared in superclass 
        // but their values are specific to subclasses)
        isPersistent = false;
        isTransactional = true;
        isDirty = true;
        isNew = false;
        isDeleted = false;

        isNavigable = true;
        isRefreshable = true;
        isBeforeImageUpdatable = true;
        isFlushed = false;
        
        stateType =  T_DIRTY;
    }

   /**
    * @see LifeCycleState#transitionMakeTransient(StateManagerImpl sm,
    * Transaction tx)
    */
    protected LifeCycleState transitionMakeTransient(StateManagerImpl sm,
        Transaction tx) { 
        return this;
    } 
 
   /**
    * @see LifeCycleState#transitionMakePersistent(StateManagerImpl sm)
    */
    protected LifeCycleState transitionMakePersistent(StateManagerImpl sm) {    

        sm.registerTransactional();
        return changeState(P_NEW);
    }

   /**
    * @see LifeCycleState#transitionToAutoPersistent(StateManagerImpl sm)
    */
    protected LifeCycleState transitionToAutoPersistent(StateManagerImpl sm) {    
        sm.registerTransactional();
        return changeState(AP_NEW);
    }

   /**
    * @see LifeCycleState#transitionCommit(boolean retainValues, StateManagerImpl sm)
    */
    protected LifeCycleState transitionCommit(boolean retainValues, StateManagerImpl sm) {     
        sm.reset();
        return changeState(T_CLEAN);
    } 
 
   /**
    * @see LifeCycleState#transitionRollback(boolean restoreValues, StateManagerImpl sm)
    */
    protected LifeCycleState transitionRollback(boolean restoreValues, StateManagerImpl sm)  {      
        sm.restoreFields();
        sm.reset();
        return changeState(T_CLEAN); 
    }  
  
}
