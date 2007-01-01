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
 *  PersistentDeletedFlushed.java    March 10, 2001
 */

package org.apache.jdo.impl.state;

/**
 * This class represents PersistentDeletedFlushed state specific state
 * transitions as requested by StateManagerImpl. This state differs from
 * PersistentDeleted in that it is the result of a flush operation on
 * a deleted instance.
 *
 * @author Marina Vatkina
 */
class PersistentDeletedFlushed extends PersistentDeleted {

    protected PersistentDeletedFlushed() {
        super();
        isFlushed = true;
        isStored = false;

        stateType =  P_DELETED_FLUSHED;
    }

   /**
    * @see LifeCycleState#transitionCommit(boolean retainValues, StateManagerImpl sm)
    */
    protected LifeCycleState transitionCommit(boolean retainValues, StateManagerImpl sm) {
        sm.clearFields();
        sm.disconnect();
        return changeState(TRANSIENT);
    }
}









