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
 
package org.apache.jdo.tck.lifecycle;

import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.pc.lifecycle.StateTransitionObj;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Test State Transitions
 *<BR>
 *<B>Keywords:</B> lifecycle
 *<BR>
 *<B>Assertion IDs:</B> A5.9-1 through A5.9-190
 *<B>Assertion Description: </B>
 All possible state transistions are being tested in this test.
 */

public class StateTransitions extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertions A5.9-1 through A5.9-190 (StateTransitions) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(StateTransitions.class);
    }
    
    private Transaction                 transaction;
    private int                         scenario;
    private int                         operation;
    private int                         current_state;
    private int                         expected_state;
    private int                         new_state;

/**
 * Operations that cause state changes
 */
    private static final int MAKEPERSISTENT          = 0;
    private static final int DELETEPERSISTENT        = 1;
    private static final int MAKETRANSACTIONAL       = 2;
    private static final int MAKENONTRANSACTIONAL    = 3;
    private static final int MAKETRANSIENT           = 4;
    private static final int COMMITNORETAINVALUES    = 5;
    private static final int COMMITRETAINVALUES      = 6;
    private static final int ROLLBACKNORESTOREVALUES = 7;
    private static final int ROLLBACKRESTOREVALUES   = 8;
    private static final int REFRESHDATASTORE        = 9;
    private static final int REFRESHOPTIMISTIC       = 10;
    private static final int EVICT                   = 11;
    private static final int READOUTSIDETX           = 12;
    private static final int READOPTIMISTIC          = 13;
    private static final int READDATASTORE           = 14;
    private static final int WRITEOUTSIDETX          = 15;
    private static final int WRITEINSIDETX           = 16;
    private static final int RETRIEVEOUTSIDETX       = 17;
    private static final int RETRIEVEINSIDETX        = 18;

    private static final String[] operations = {
        "makePersistent",
        "deletePersistent",
        "makeTransactional",
        "makeNontransactional",
        "makeTransient",
        "commit, retainValues=false",
        "commit, retainValues=true",
        "rollback, restoreValues=false",
        "rollback, restoreValues=true",
        "refresh with active datastore tx",
        "refresh with active optimistic tx",
        "evict",
        "read field outside tx",
        "read field with active optimistic tx",
        "read field with active datastore tx",
        "write field outside tx",
        "write field with active tx",
        "retrieve outside tx",
        "retrieve with active tx"
    };
    private static final int NUM_OPERATIONS = 19;

    private static final boolean[] closes_transaction =
    { false, false, false, false, false, true, true, true, true, false,
      false, false, false, false, false, false, false, false, false };

    /**
     * Illegal state transitions
     */
    private static final int UNCHANGED                   = -1;
    private static final int ERROR                       = -2;
    private static final int IMPOSSIBLE                  = -3;
    private static final int NOT_APPLICABLE              = -4;

    /**
     * State transitions
     */
    public static final int[][] transitions = { // [operation] [current state] = new state
        // makePersistent
        { PERSISTENT_NEW, UNCHANGED, UNCHANGED, UNCHANGED, UNCHANGED, PERSISTENT_NEW,
          PERSISTENT_NEW, UNCHANGED, UNCHANGED, UNCHANGED},

        // deletePersistent
        { ERROR, PERSISTENT_NEW_DELETED, PERSISTENT_DELETED, PERSISTENT_DELETED,
          PERSISTENT_DELETED, ERROR, ERROR, UNCHANGED, UNCHANGED, PERSISTENT_DELETED},

        // makeTransactional
        { TRANSIENT_CLEAN, UNCHANGED, UNCHANGED, UNCHANGED, PERSISTENT_CLEAN,
          UNCHANGED, UNCHANGED, UNCHANGED, UNCHANGED, PERSISTENT_CLEAN},
    
        // makeNontransactional
        { ERROR, ERROR, PERSISTENT_NONTRANSACTIONAL, ERROR, UNCHANGED,
          TRANSIENT, ERROR, ERROR, ERROR, UNCHANGED},

        // makeTransient
        { UNCHANGED, ERROR, TRANSIENT, ERROR, TRANSIENT,
          UNCHANGED, UNCHANGED, ERROR, ERROR, TRANSIENT},

        // commit, retainValues = false
        { UNCHANGED, HOLLOW, HOLLOW, HOLLOW, UNCHANGED, UNCHANGED,
          TRANSIENT_CLEAN, TRANSIENT, TRANSIENT, UNCHANGED},

        // commit, retainValues = true
        { UNCHANGED, PERSISTENT_NONTRANSACTIONAL, PERSISTENT_NONTRANSACTIONAL,
          PERSISTENT_NONTRANSACTIONAL, UNCHANGED, UNCHANGED, TRANSIENT_CLEAN,
          TRANSIENT, TRANSIENT, UNCHANGED},

        // rollback, restoreValues = false
        { UNCHANGED, TRANSIENT, HOLLOW, HOLLOW, UNCHANGED, UNCHANGED,
          TRANSIENT_CLEAN, TRANSIENT, HOLLOW, UNCHANGED},

        // rollback, restoreValues = true
        { UNCHANGED, TRANSIENT, PERSISTENT_NONTRANSACTIONAL, PERSISTENT_NONTRANSACTIONAL,
          UNCHANGED, UNCHANGED, TRANSIENT_CLEAN, TRANSIENT, PERSISTENT_NONTRANSACTIONAL, UNCHANGED},

        // refresh with active datastore transaction
        { UNCHANGED, UNCHANGED, UNCHANGED, PERSISTENT_CLEAN, UNCHANGED,
          UNCHANGED, UNCHANGED, UNCHANGED, UNCHANGED, UNCHANGED},

        // refresh with active optimistic transaction
        { UNCHANGED, UNCHANGED, UNCHANGED, PERSISTENT_NONTRANSACTIONAL, UNCHANGED,
          UNCHANGED, UNCHANGED, UNCHANGED, UNCHANGED, UNCHANGED},

        // evict
        { NOT_APPLICABLE, UNCHANGED, HOLLOW, UNCHANGED, UNCHANGED,
          UNCHANGED, UNCHANGED, UNCHANGED, UNCHANGED, HOLLOW},

        // read field outside transaction
        { UNCHANGED, IMPOSSIBLE, IMPOSSIBLE, IMPOSSIBLE, PERSISTENT_NONTRANSACTIONAL,
          UNCHANGED, IMPOSSIBLE, IMPOSSIBLE, IMPOSSIBLE, UNCHANGED},

        // read field with active optimistic transaction
        { UNCHANGED, UNCHANGED, UNCHANGED, UNCHANGED, PERSISTENT_NONTRANSACTIONAL,
          UNCHANGED, UNCHANGED, ERROR, ERROR, UNCHANGED},

        // read field with active datastore transaction
        { UNCHANGED, UNCHANGED, UNCHANGED, UNCHANGED, PERSISTENT_CLEAN,
          UNCHANGED, UNCHANGED, ERROR, ERROR, PERSISTENT_CLEAN},

        // write field outside transaction
        { UNCHANGED, IMPOSSIBLE, IMPOSSIBLE, IMPOSSIBLE, PERSISTENT_NONTRANSACTIONAL,
          UNCHANGED, IMPOSSIBLE, IMPOSSIBLE, IMPOSSIBLE, UNCHANGED},
    
        // write field with active transaction
        { UNCHANGED, UNCHANGED, PERSISTENT_DIRTY, UNCHANGED, PERSISTENT_DIRTY,
          TRANSIENT_DIRTY, UNCHANGED, ERROR, ERROR, PERSISTENT_DIRTY},
	  
        // retrieve outside transaction
        { UNCHANGED, IMPOSSIBLE, IMPOSSIBLE, IMPOSSIBLE, PERSISTENT_NONTRANSACTIONAL,
          UNCHANGED, IMPOSSIBLE, IMPOSSIBLE, IMPOSSIBLE, UNCHANGED},
	  
        // retrieve with active transaction
        { UNCHANGED, UNCHANGED, UNCHANGED, UNCHANGED, PERSISTENT_CLEAN,
          UNCHANGED, UNCHANGED, UNCHANGED, UNCHANGED, PERSISTENT_CLEAN}
    };

    private static final int DATASTORE_TX = 0;
    private static final int OPTIMISTIC_TX = 1;
    private static final int NO_TX = 2;

    private static final String[] scenario_string = {
        "datastore transaction", "optimistic transaction", "no transaction"
    };

    private static final boolean[][] applies_to_scenario = {
        //  Datastore   Optimistic      No tx
        {   true,          true,        false },  // makePersistent
        {   true,          true,        false },  // deletePersistent
        {   true,          true,        false },  // makeTransactional
        {   true,          true,        false },  // makeNontransactional
        {   true,          true,        false },  // makeTransient
        {   true,          true,        false },  // commit RetainValues = false
        {   true,          true,        false },  // commit RetainValues = true
        {   true,          true,        false },  // rollback RestoreValues = false
        {   true,          true,        false },  // rollback RestoreValues = true
        {   true,          false,       false },  // refresh with active datastore transaction
        {   false,         true,        false },  // refresh with active optimistic transaction
        {   true,          true,        false },  // evict
        {   false,         false,       true  },  // read field outside of a transaction
        {   false,         true,        false },  // read field with active optimistic transaction
        {   true,          false,       false },  // read field with active datastore transaction
        {   false,         false,       true  },  // write field or makeDirty outside of a transaction
        {   true,          true,        false },  // write field or makeDirty with active transaction
        {   false,         true,        true  },  // retrieve outside of a transaction or with active optimistic transaction
        {   true,          false,       false }   // retrieve with active datastore transaction
    };

    public StateTransitions()
    {
    }

    public void test() {
        pm = getPM();
        generatePersistentInstances();

        scenario = DATASTORE_TX;
        checkTransitions();

        if( isOptimisticSupported() ){
            scenario = OPTIMISTIC_TX;
            checkTransitions();
        }

        scenario = NO_TX;
        checkTransitions();
    }

    /** */
    private void generatePersistentInstances()
    {
        if( doPersistentInstancesExist() ) return;
        int i;
        Transaction t = pm.currentTransaction();
        t.begin();
        for( i = 0; i < 50; ++i ){
            StateTransitionObj sto = new StateTransitionObj(i);
            sto.writeField(i);
            pm.makePersistent(sto);
        }
        t.commit();
        if( !doPersistentInstancesExist() )
            if (debug)
                logger.debug("StateTransitions unable to create instances of StateTransitionsObj");
    }

    /** */
    private boolean doPersistentInstancesExist()
    {
        boolean ret;
        Transaction t = pm.currentTransaction();
        t.begin();
        Extent e = pm.getExtent(StateTransitionObj.class, false);
        Iterator iter = e.iterator();
        ret = iter.hasNext();
        t.rollback();
        return ret;
    }

    /** */
    void checkTransitions()
    {
        for( operation = 0; operation < NUM_OPERATIONS; ++operation ){
            // rule out situations that do not apply
            if( ! applies_to_scenario[operation][scenario] ) continue;
            if( operation == READOUTSIDETX && !isNontransactionalReadSupported() ) continue;
            if( operation == WRITEOUTSIDETX && !isNontransactionalWriteSupported() ) continue;
            if( operation == COMMITRETAINVALUES && !isRetainValuesSupported() ) continue;
            if( operation == MAKENONTRANSACTIONAL &&
                !(isNontransactionalReadSupported() || isNontransactionalWriteSupported()) )
                continue;

            for( current_state = 0; current_state < NUM_STATES; ++current_state){
                if( scenario == OPTIMISTIC_TX && current_state == PERSISTENT_CLEAN ) continue;
                if( (current_state == TRANSIENT_CLEAN || current_state == TRANSIENT_DIRTY) &&
                    !isTransientTransactionalSupported() )
                    continue;   // this state is not supported by implementation
                if( current_state == PERSISTENT_NONTRANSACTIONAL &&
                    !(isNontransactionalReadSupported() || isNontransactionalWriteSupported()) )
                    continue;   // this state is not supported by implementation

                expected_state = transitions[operation][current_state];
                if( expected_state == IMPOSSIBLE ) continue;
                if( expected_state == NOT_APPLICABLE ) continue;
                if( expected_state == UNCHANGED ) expected_state = current_state;
                try {
                    transaction = pm.currentTransaction();
                    if( transaction.isActive()){
                        if (debug)
                            logger.debug("Transaction is active (but should not be), rolling back");
                        transaction.rollback();
                    }

                    if( scenario != NO_TX ){
                        if( operation == COMMITNORETAINVALUES )
                            transaction.setRetainValues(false);
                        if( operation == COMMITRETAINVALUES )
                            transaction.setRetainValues(true);
                        if( operation == ROLLBACKNORESTOREVALUES )
                            transaction.setRestoreValues(false);
                        if( operation == ROLLBACKRESTOREVALUES )
                            transaction.setRestoreValues(true);

                        transaction.setOptimistic(scenario == OPTIMISTIC_TX);
                        transaction.begin();
                        if( !transaction.isActive() )
                            if (debug)
                                logger.debug("StateTransitions: Transaction should be active, but it is not");
                    }

                    StateTransitionObj obj = getInstanceInState(current_state);
                    if( obj == null ){  // could not get object in state
                        if( transaction.isActive() ) transaction.rollback();
                        continue;
                    }

                    // Apply operation, catching possible exception
                    Exception e = null;
                    try {
                        applyOperation(operation, obj);
                    } catch( Exception excep ){
                        if( excep instanceof javax.jdo.JDOUserException ){
                            e = excep;
                        } else {
                            printSituation();
                            fail(ASSERTION_FAILED,
                                 "StateTransitions: Unexpected exception:" + excep);
                            continue;
                        }
                    }

                    // Get new state, verify correct transition and exceptions occurred
                    new_state = currentState(obj);
                    if( expected_state == ERROR ){
                        if( e == null ){
                            printSituation();
                            fail(ASSERTION_FAILED,
                                 "StateTransitions: JDOUserException should have been thrown");
                        } else {
                            if( new_state != current_state ){
                                printSituation();
                                fail(ASSERTION_FAILED,
                                     "StateTransitions: " + 
                                     " JDOUserException properly thrown, but instance should remain in current state," +
                                     "instance changed state to " + states[new_state]);
                            }
                        }
                    }
                    if( expected_state >= 0 && new_state != expected_state && 
                        !((new_state == HOLLOW && expected_state == PERSISTENT_NONTRANSACTIONAL) ||
                          (new_state == PERSISTENT_NONTRANSACTIONAL && expected_state == HOLLOW)) ) { 
                        // status interrogation gives same values for PERSISTENT_NONTRANSACTIONAL and HOLLOW
                        printSituation();
                        fail(ASSERTION_FAILED,
                             "StateTransitions: Invalid state transition to " +
                             states[new_state] + ", new state should be " +
                             states[expected_state]);
                    }
                    if( transaction.isActive() ) transaction.rollback();
                } 
                catch(Exception unexpected_exception) {
                    printSituation();
                    if (transaction.isActive()) 
                        transaction.rollback();
                    fail(ASSERTION_FAILED,
                         "Unexpected exception caught in StateTransitions " + unexpected_exception);
                }
            }
        }
    }

    /** */
    void printSituation()
    {
        if (debug) {
            logger.debug(" (" + scenario_string[scenario] +
                         ", initial state=" + states[current_state] + 
                         ", operation=" + operations[operation] + ")");
        }
    }

    /** */
    void applyOperation(int operation, StateTransitionObj stobj)
    {
        StateTransitionObj obj = (StateTransitionObj) stobj;
        switch( operation ){
        case MAKEPERSISTENT:
        {
            pm.makePersistent(obj);
            break;
        }
        case DELETEPERSISTENT:
        {
            pm.deletePersistent(obj);
            break;
        }
        case MAKETRANSACTIONAL:
        {
            pm.makeTransactional(obj);
            break;
        }
        case MAKENONTRANSACTIONAL:
        {
            pm.makeNontransactional(obj);
            break;
        }
        case MAKETRANSIENT:
        {
            pm.makeTransient(obj);
            break;
        }
        case COMMITNORETAINVALUES:
        {
            pm.currentTransaction().commit();
            break;
        }
        case COMMITRETAINVALUES:
        {
            pm.currentTransaction().commit();
            break;
        }
        case ROLLBACKNORESTOREVALUES:
        {
            pm.currentTransaction().rollback();
            break;
        }
        case ROLLBACKRESTOREVALUES:
        {
            pm.currentTransaction().rollback();
            break;
        }
        case REFRESHDATASTORE:
        {
            pm.refresh(obj);
            break;
        }
        case REFRESHOPTIMISTIC:
        {
            pm.refresh(obj);
            break;
        }
        case EVICT:
        {
            pm.evict(obj);
            break;
        }
        case READOUTSIDETX:
        {
            int val = obj.readField();
            break;
        }
        case READOPTIMISTIC:
        {
            int val = obj.readField();
            break;
        }
        case READDATASTORE:
        {
            int val = obj.readField();
            break;
        }
        case WRITEOUTSIDETX:
        {
            obj.writeField(42);
            break;
        }
        case WRITEINSIDETX:
        {
            obj.writeField(42);
            break;
        }
        case RETRIEVEOUTSIDETX:
        {
            pm.retrieve(obj);
            break;
        }
        case RETRIEVEINSIDETX:
        {
            pm.retrieve(obj);
            break;		
        }
        default:
        {
            fail(ASSERTION_FAILED,
                 "StateTransitions internal error, illegal operation " + operation);
        }
        }
    }

    /**
     * Get an instance in the specified state.
     */
    private StateTransitionObj getInstanceInState(int state)
    {
        switch(state) {
        case TRANSIENT:
            return getTransientInstance();
        case PERSISTENT_NEW:
            return getPersistentNewInstance();
        case PERSISTENT_CLEAN:
            return getPersistentCleanInstance();
        case PERSISTENT_DIRTY:
            return getPersistentDirtyInstance();
        case HOLLOW:
            return getHollowInstance();
        case TRANSIENT_CLEAN:
            return getTransientCleanInstance();
        case TRANSIENT_DIRTY:
            return getTransientDirtyInstance();
        case PERSISTENT_NEW_DELETED:
            return getPersistentNewDeletedInstance();
        case PERSISTENT_DELETED:
            return getPersistentDeletedInstance();
        case PERSISTENT_NONTRANSACTIONAL:
            return getPersistentNontransactionalInstance();
        default:
        {
            return null;
        }
        }
    }

    /** */
    private StateTransitionObj getTransientInstance()
    {
        StateTransitionObj obj = new StateTransitionObj(23);
        int curr = currentState(obj);
        if( curr != TRANSIENT ) {
            if (debug) {
                logger.debug("StateTransitions: Unable to create transient instance, state is " + 
                             states[curr]);
            }
            printSituation();
            return null;
        }
        return obj;
    }

    /** */
    private StateTransitionObj getPersistentNewInstance()
    {
        StateTransitionObj obj = getTransientInstance();
        if( obj == null ) return null;
        pm.makePersistent(obj); // should transition to persistent-new
        int curr = currentState(obj);
        if( curr != PERSISTENT_NEW ) {
            if (debug) {
                logger.debug("StateTransitions: Unable to create persistent-new instance" +
                             " from transient instance via makePersistent(), state is " +
                             states[curr]);
            }
            printSituation();
            return null;
        }
        return obj;
    }
    
    /** */
    public StateTransitionObj getPersistentCleanInstance()
    {
        StateTransitionObj obj = getHollowInstance();
        if( obj == null ) return null;
        StateTransitionObj sto = (StateTransitionObj) obj;
        int val = sto.readField();
        int curr = currentState(sto);
        if( curr != PERSISTENT_CLEAN ) {
            if (debug) {
                logger.debug("StateTransition: Unable to create persistent-clean instance" +
                             " from a hollow instance by reading a field, state is " +
                             states[curr]);
            }
            printSituation();
            return null;
        }
        return obj;
    }
    
    /** */
    public StateTransitionObj getPersistentDirtyInstance()
    {
        StateTransitionObj obj = getHollowInstance();
        if( obj == null ) return null;
        StateTransitionObj pcobj = (StateTransitionObj) obj;
        pcobj.writeField(23);
        int curr = currentState(obj);
        if( curr != PERSISTENT_DIRTY ) {
            if (debug) {
                logger.debug("StateTransition: Unable to create persistent-dirty instance" +
                             " from a hollow instance by writing a field, state is " +
                             states[curr]);
            }
            printSituation();
            return null;
        }
        return obj;
    }

    /** */
    public StateTransitionObj getHollowInstance()
    {
        Extent extent = pm.getExtent(StateTransitionObj.class, false);
        Iterator iter = extent.iterator();
        if( !iter.hasNext() ){
            if (debug)
                logger.debug("Extent for StateTransitionObj should not be empty");
            return null;
        }
        StateTransitionObj obj = (StateTransitionObj) iter.next();
        
        transaction.setRetainValues(false);
        if ( !transaction.isActive() )
            transaction.begin();
        if( !transaction.isActive() )
            if (debug)
                logger.debug("getHollowInstance: Transaction should be active, but it is not");
		
        transaction.commit(); // This should put the instance in the HOLLOW state

        if( scenario != NO_TX ){
            if( operation == COMMITNORETAINVALUES )
                transaction.setRetainValues(false);
            if( operation == COMMITRETAINVALUES )
                transaction.setRetainValues(true);
            if( operation == ROLLBACKNORESTOREVALUES )
                transaction.setRestoreValues(false);
            if( operation == ROLLBACKRESTOREVALUES )
                transaction.setRestoreValues(true);

            transaction.setOptimistic(scenario == OPTIMISTIC_TX);
            transaction.begin();
            if( !transaction.isActive() )
                if (debug)
                    logger.debug("getHollowInstance: Transaction should be active, but it is not");
        }

        int curr = currentState(obj);
        if( curr != HOLLOW && curr != PERSISTENT_NONTRANSACTIONAL ){
            if (debug) {
                logger.debug("StateTransition: Attempt to get hollow instance via accessing extent failed, state is " +
                             states[curr]);
            }
            printSituation();
            return null;
        }
        return obj;
    }
    
    /** */
    public StateTransitionObj getTransientCleanInstance()
    {
        StateTransitionObj obj = getTransientInstance();
        if( obj == null ) return null;
        pm.makeTransactional(obj);
        int curr = currentState(obj);
        if( curr != TRANSIENT_CLEAN ) {
            if (debug) {
                logger.debug("StateTransition: Unable to create transient-clean instance" +
                             " from a transient instance via makeTransactional(), state is " +
                             states[curr]);
            }
            printSituation();
            return null;
        }
        return obj;
    }

    /** */
    public StateTransitionObj getTransientDirtyInstance()
    {
        StateTransitionObj obj = getTransientCleanInstance();
        if( obj == null ) return null;
        StateTransitionObj pcobj = (StateTransitionObj) obj;
        pcobj.writeField(23);
        int curr = currentState(obj);
        if( curr != TRANSIENT_DIRTY ) { 
            if (debug) {
                logger.debug("StateTransition: Unable to create transient-dirty instance" +
                             " from a transient-clean instance via modifying a field, state is " +
                             states[curr]);
            }
            printSituation();
            return null;
        }
        return obj;
    }

    /** */
    public StateTransitionObj getPersistentNewDeletedInstance()
    {
        StateTransitionObj obj = getPersistentNewInstance();
        if( obj == null ) return null;
        pm.deletePersistent(obj);   // should transition to persistent-new-deleted
        int curr = currentState(obj);
        if( curr != PERSISTENT_NEW_DELETED) { 
            if (debug) {
                logger.debug("StateTransition: Unable to create persistent-new-deleted instance" +
                             " from a persistent-new instance via deletePersistent, state is " +
                             states[curr]);
            }
            printSituation();
            return null;
        }
        return obj;
    }
    
    /** */
    public StateTransitionObj getPersistentDeletedInstance()
    {
        StateTransitionObj obj = getHollowInstance();
        if( obj == null ) return null;
        pm.deletePersistent(obj);
        int curr = currentState(obj);
        if( curr != PERSISTENT_DELETED ) { 
            if (debug) {
                logger.debug("StateTransition: Unable to create persistent-deleted instance" +
                             " from a persistent instance via deletePersistent(), state is " +
                             states[curr]);
            }
            printSituation();
            return null;
        }
        return obj;
    }

    /** */
    public StateTransitionObj getPersistentNontransactionalInstance()
    {
        StateTransitionObj obj = getHollowInstance();
        if( obj == null ) return null;
        pm.makeNontransactional(obj);
        int curr = currentState(obj);
        if( curr != PERSISTENT_NONTRANSACTIONAL && curr != HOLLOW ) { 
            if (debug) {
                logger.debug("StateTransition: Unable to create persistent-nontransactional instance" +
                             " from a persistent-clean instance via makeNontransactional(), state is " +
                             states[curr]);
            }
            printSituation();
            return null;
        }
        return null;
    }

}
