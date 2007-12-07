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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.JDOFatalException;
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

public class StateTransitionsReturnedObjects extends JDO_Test {

    /** */
    private static final String ASSERTION_FAILED = 
        "Assertions A5.9-1 through A5.5.8 (serialization, detachment, attachment) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(StateTransitionsReturnedObjects.class);
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
    private static final int DETACHCOPYOUTSIDETXNTRTRU = 1;
    private static final int DETACHCOPYOUTSIDETXNTRFLS = 2;
    private static final int DETACHCOPYINSIDEDATASTORETX = 3;
    private static final int DETACHCOPYINSIDEOPTIMISTICTX = 4;
    private static final int SERIALIZEOUTSIDETX = 5;
    private static final int SERIALIZEINSIDETX       = 6;
    
    private static final int NUM_OPERATIONS          = 7;
    
    private static final String[] operations = {
        "makePersistent",
        "detachCopy outside tx with NontransactionalRead true",
        "detachCopy outside tx with NontransactionalRead false",
        "detachCopy with active datastore tx",
        "detachCopy with active optimistic tx",
        "serialize outside tx",
        "serialize with active tx"
    };

    /**
     * Illegal state transitions
     */
    private static final int UNCHANGED                   = -1;
    private static final int ERROR                       = -2;
    private static final int IMPOSSIBLE                  = -3;
    private static final int NOT_APPLICABLE              = -4;
    private static final int UNSPECIFIED                 = -5;

    /**
     * State transitions
     */
    public static final int[][] statesOfReturnedObjects = { // [operation] [current state] = new state
        //  TRANSIENT,                      PERSISTENT_NEW,                     PERSISTENT_CLEAN,
        //  PERSISTENT_DIRTY,               HOLLOW,                             TRANSIENT_CLEAN,
        //  TRANSIENT_DIRTY,                PERSISTENT_NEW_DELETED,             PERSISTENT_DELETED, 
        //  PERSISTENT_NONTRANSACTIONAL,    PERSISTENT_NONTRANSACTIONAL_DIRTY,  DETACHED_CLEAN, 
        //  DETACHED_DIRTY
        
        // makePersistent
        {   PERSISTENT_NEW,                 UNCHANGED,                          UNCHANGED,
            UNCHANGED,                      UNCHANGED,                          PERSISTENT_NEW,
            PERSISTENT_NEW,                 UNCHANGED,                          UNCHANGED, 
            UNCHANGED,                      UNCHANGED,                          PERSISTENT_CLEAN,     
            PERSISTENT_DIRTY},

        // detachCopy outside tx with NontransactionalRead=true
        {   ERROR,                          IMPOSSIBLE,                         IMPOSSIBLE,
            IMPOSSIBLE,                     DETACHED_CLEAN,                     ERROR,
            IMPOSSIBLE,                     IMPOSSIBLE,                         IMPOSSIBLE,
            DETACHED_CLEAN,                 UNSPECIFIED,                        DETACHED_CLEAN,
            UNSPECIFIED},

        // detachCopy outside tx with NontransactionalRead=false
        {   ERROR,                          ERROR,                              ERROR,
            ERROR,                          ERROR,                              ERROR,
            IMPOSSIBLE,                     IMPOSSIBLE,                         IMPOSSIBLE,
            ERROR,                          ERROR,                              ERROR,
            ERROR},

        // detachCopy with active datastore tx
        {   DETACHED_CLEAN,                 DETACHED_CLEAN,                     DETACHED_CLEAN,
            DETACHED_CLEAN,                 DETACHED_CLEAN,                     DETACHED_CLEAN,
            DETACHED_CLEAN,                 ERROR,                              ERROR,
            DETACHED_CLEAN,                 IMPOSSIBLE,                         DETACHED_CLEAN,
            DETACHED_CLEAN},

        // detachCopy with active optimistic tx
        {   DETACHED_CLEAN,                 DETACHED_CLEAN,                     DETACHED_CLEAN,
            DETACHED_CLEAN,                 DETACHED_CLEAN,                     DETACHED_CLEAN,
            DETACHED_CLEAN,                 ERROR,                              ERROR,
            DETACHED_CLEAN,                 IMPOSSIBLE,                         DETACHED_CLEAN,
            DETACHED_CLEAN},

        // serialize outside tx
        {   UNCHANGED,                      IMPOSSIBLE,                         IMPOSSIBLE,
            IMPOSSIBLE,                     DETACHED_CLEAN,                     IMPOSSIBLE,
            IMPOSSIBLE,                     IMPOSSIBLE,                         IMPOSSIBLE,
            DETACHED_CLEAN,                 UNSPECIFIED,                        UNCHANGED,
            UNCHANGED},

        // serialize with active tx
        {   UNCHANGED,                      DETACHED_CLEAN,                     DETACHED_CLEAN,
            DETACHED_CLEAN,                 DETACHED_CLEAN,                     TRANSIENT,
            TRANSIENT,                      TRANSIENT,                          TRANSIENT,
            DETACHED_CLEAN,                 TRANSIENT,                          UNCHANGED,
            UNCHANGED},
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
        // since the spec leaves detachCopy outside tx a bit underspecified,
        // we decided to disable this scanario for now
        {   false,         false,       true },   // detachCopy outside tx with NontransactionalRead=true
        {   false,         false,       false },   // detachCopy outside tx with NontransactionalRead=false
        {   true,          true,        false },  // detachCopy with active datastore tx
        {   true,          true,        false },  // detachCopy with active optimistic tx
        {   false,         false,       true },   // serialize outside tx
        {   true,          true,        false }   // serialize with active tx
    };

    /**
     * @see JDO_Test#localSetUp()
     */
    protected void localSetUp() {
        pm = getPM();
        addTearDownClass(StateTransitionObj.class);
        generatePersistentInstances();
    }
    
    public void test() {
        scenario = DATASTORE_TX;
        checkTransitions();

        if( isOptimisticSupported() ){
            scenario = OPTIMISTIC_TX;
            checkTransitions();
        }

        scenario = NO_TX;
        checkTransitions();
        failOnError();
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
                logger.debug("StateTransitionsReturnedObjects unable to create instances of StateTransitionsObj");
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
    public void prepareTransactionAndJDOSettings(Transaction transaction) {
        if( scenario != NO_TX ) {
            transaction.setNontransactionalRead(false);
            transaction.setNontransactionalWrite(false);
            pm.setDetachAllOnCommit(false);

            transaction.setOptimistic(scenario == OPTIMISTIC_TX);
            transaction.begin();
            if( !transaction.isActive() )
                if (debug)
                    logger.debug("StateTransitionsReturnedObjects: Transaction should be active, but it is not");
        } else {
            if( operation == DETACHCOPYOUTSIDETXNTRTRU ||
                operation == SERIALIZEOUTSIDETX ) {
                transaction.setNontransactionalRead(true);
            }
            if( current_state == PERSISTENT_NONTRANSACTIONAL_DIRTY) {
                transaction.setNontransactionalRead(true);
            }
        }
    }
    
    /** */
    void checkTransitions()
    {
        for( operation = 0; operation < NUM_OPERATIONS; ++operation ){
            // rule out situations that do not apply
            if( !applies_to_scenario[operation][scenario] ) continue;
            if( (operation == DETACHCOPYOUTSIDETXNTRTRU ||
                 operation == SERIALIZEOUTSIDETX) && 
                 !isNontransactionalReadSupported() ) continue;

            for( current_state = 0; current_state < NUM_STATES; ++current_state){
                if( scenario == OPTIMISTIC_TX && current_state == PERSISTENT_CLEAN ) continue;
                if( (current_state == TRANSIENT_CLEAN || current_state == TRANSIENT_DIRTY) &&
                    !isTransientTransactionalSupported() )
                    continue;   // this state is not supported by implementation
                if( current_state == PERSISTENT_NONTRANSACTIONAL &&
                    !(isNontransactionalReadSupported() || isNontransactionalWriteSupported()) )
                    continue;   // this state is not supported by implementation
                if( current_state == PERSISTENT_NONTRANSACTIONAL_DIRTY &&
                        !isNontransactionalWriteSupported() )
                    continue;

                expected_state = statesOfReturnedObjects[operation][current_state];
                if( expected_state == IMPOSSIBLE ) continue;
                if( expected_state == NOT_APPLICABLE ) continue;
                if( expected_state == UNSPECIFIED ) continue;
                if( expected_state == UNCHANGED ) expected_state = current_state;
                try {
                    transaction = pm.currentTransaction();
                    if( transaction.isActive()){
                        if (debug)
                            logger.debug("Transaction is active (but should not be), rolling back");
                        transaction.rollback();
                    }

                    prepareTransactionAndJDOSettings(transaction);

                    printSituation();
                    
                    StateTransitionObj obj = getInstanceInState(current_state);
                    if( obj == null ){  // could not get object in state
                        if( transaction.isActive() ) transaction.rollback();
                        continue;
                    }

                    // Apply operation, catching possible exception
                    Exception e = null;
                    Object returnedObject = null;
                    try {
                        returnedObject = applyOperation(operation, obj);
                    } catch( Exception excep ){
                        if( excep instanceof javax.jdo.JDOUserException ){
                            e = excep;
                        } else {
                            appendMessage(ASSERTION_FAILED + NL +
                                          "StateTransitionsReturnedObjects: " +
                                          scenario_string[scenario] +
                                          "; current state " + states[current_state] + NL +
                                          operations[operation] +
                                          "; unexpected exception: " + excep);
                            continue;
                        }
                    }

                    if( expected_state == ERROR ){
                        if( e == null ){
                            appendMessage(ASSERTION_FAILED + NL +
                                          "StateTransitionsReturnedObjects: " +
                                          scenario_string[scenario] +
                                          "; current state " + states[current_state] + NL +
                                          operations[operation] +
                                          "; JDOUserException should have been thrown");
                        } else {
                            int stateOfObj = currentState(obj);
                            if( stateOfObj != current_state ){
                                appendMessage(ASSERTION_FAILED + NL +
                                              "StateTransitionsReturnedObjects: " +
                                              scenario_string[scenario] +
                                              "; current state " + states[current_state] + NL +
                                              operations[operation] +
                                              "; JDOUserException properly thrown, but instance should remain in current state," +
                                              " instance changed state to " + states[stateOfObj]);
                            }
                        }
                    } else {
                        // Get new state, verify correct transition and exceptions occurred
                        new_state = currentState(returnedObject);
                        if( !compareStates(new_state, expected_state) ) { 
                            appendMessage(ASSERTION_FAILED + NL +
                                          "StateTransitionsReturnedObjects: " +
                                          scenario_string[scenario] +
                                          "; current state " + states[current_state] + NL +
                                          operations[operation] +
                                          " returned instance in invalid state " + states[new_state] + 
                                          "; expected state " + states[expected_state]);
                        }
                    }
                    if( transaction.isActive() ) transaction.rollback();
                } 
                catch(Exception unexpected_exception) {
                    if (transaction.isActive()) 
                        transaction.rollback();
                    appendMessage(ASSERTION_FAILED + NL +
                                  "StateTransitionsReturnedObjects: " +
                                  scenario_string[scenario] +
                                  "; current state " + states[current_state] + NL +
                                  operations[operation] +
                                  "; unexpected exception caught: " + unexpected_exception);
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
                         ", " + operations[operation] + ")");
        }
    }

    /** */
    Object applyOperation(int operation, StateTransitionObj stobj)
    {
        Object result = null;
        StateTransitionObj obj = (StateTransitionObj) stobj;
        switch( operation ){
            case MAKEPERSISTENT:
                result = pm.makePersistent(obj);
                break;
    
            case DETACHCOPYOUTSIDETXNTRTRU:
            case DETACHCOPYOUTSIDETXNTRFLS:
            case DETACHCOPYINSIDEDATASTORETX:
            case DETACHCOPYINSIDEOPTIMISTICTX:
                result = pm.detachCopy(obj);
                break;      
    
            case SERIALIZEOUTSIDETX:
            case SERIALIZEINSIDETX:
                ObjectOutputStream oos = null;
                ObjectInputStream ois = null;
                try {
                    ByteArrayOutputStream byteArrayOutputStream = 
                        new ByteArrayOutputStream();
                    oos = new ObjectOutputStream(byteArrayOutputStream);
                    oos.writeObject(obj);
                    ois = new ObjectInputStream(new ByteArrayInputStream(
                            byteArrayOutputStream.toByteArray()));
                    result = ois.readObject();
                } catch (IOException e) {
                    throw new JDOFatalException(e.getMessage(), e);
                } catch (ClassNotFoundException e) {
                    throw new JDOFatalException(e.getMessage(), e);
                } finally {
                    try {
                        if (oos != null) {
                            oos.close();
                        }
                        if (ois != null) {
                            ois.close();
                        }
                    } catch (IOException e) {
                        throw new JDOFatalException(e.getMessage(), e);
                    }
                }
                break;   
                
            default:
                appendMessage(ASSERTION_FAILED + NL +
                              "StateTransitionsReturnedObjects: " +
                              scenario_string[scenario] +
                              "; internal error, illegal operation: " + operation);
        }
        return result;
    }

    /**
     * Get an instance in the specified state.
     */
    public StateTransitionObj getInstanceInState(int state)
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
        case PERSISTENT_NONTRANSACTIONAL_DIRTY:
            return getPersistentNontransactionalDirtyInstance();
        case DETACHED_CLEAN:
            return getDetachedCleanInstance();
        case DETACHED_DIRTY:
            return getDetachedDirtyInstance();
        default:
            return null;
        }
    }

    /** */
    public StateTransitionObj getTransientInstance()
    {
        StateTransitionObj obj = new StateTransitionObj(23);
        int curr = currentState(obj);
        if( curr != TRANSIENT ) {
            if (debug) {
                logger.debug("StateTransitionsReturnedObjects: Unable to create transient instance, state is " + 
                             states[curr]);
            }
            return null;
        }
        return obj;
    }

    /** */
    public StateTransitionObj getPersistentNewInstance()
    {
        StateTransitionObj obj = getTransientInstance();
        if( obj == null ) return null;
        pm.makePersistent(obj); // should transition to persistent-new
        int curr = currentState(obj);
        if( curr != PERSISTENT_NEW ) {
            if (debug) {
                logger.debug("StateTransitionsReturnedObjects: Unable to create persistent-new instance" +
                             " from transient instance via makePersistent(), state is " +
                             states[curr]);
            }
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
        sto.readField();
        int curr = currentState(sto);
        if( curr != PERSISTENT_CLEAN ) {
            if (debug) {
                logger.debug("StateTransitionsReturnedObjects: Unable to create persistent-clean instance" +
                             " from a hollow instance by reading a field, state is " +
                             states[curr]);
            }
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
                logger.debug("StateTransitionsReturnedObjects: Unable to create persistent-dirty instance" +
                             " from a hollow instance by writing a field, state is " +
                             states[curr]);
            }
            return null;
        }
        return obj;
    }

    /** */
    public StateTransitionObj getHollowInstance()
    {
        if ( !transaction.isActive() )
            transaction.begin();
        if( !transaction.isActive() )
            if (debug)
                logger.debug("getHollowInstance: Transaction should be active, but it is not");
        
        Extent extent = pm.getExtent(StateTransitionObj.class, false);
        Iterator iter = extent.iterator();
        if( !iter.hasNext() ){
            if (debug)
                logger.debug("Extent for StateTransitionObj should not be empty");
            return null;
        }
        StateTransitionObj obj = (StateTransitionObj) iter.next();
        
        pm.makeTransactional(obj);
        transaction.setRetainValues(false);
        transaction.commit(); // This should put the instance in the HOLLOW state

        prepareTransactionAndJDOSettings(transaction);

        int curr = currentState(obj);
        if( curr != HOLLOW && curr != PERSISTENT_NONTRANSACTIONAL ){
            if (debug) {
                logger.debug("getHollowInstance: Attempt to get hollow instance via accessing extent failed, state is " +
                             states[curr]);
            }
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
                logger.debug("StateTransitionsReturnedObjects: Unable to create transient-clean instance" +
                             " from a transient instance via makeTransactional(), state is " +
                             states[curr]);
            }
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
                logger.debug("StateTransitionsReturnedObjects: Unable to create transient-dirty instance" +
                             " from a transient-clean instance via modifying a field, state is " +
                             states[curr]);
            }
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
                logger.debug("StateTransitionsReturnedObjects: Unable to create persistent-new-deleted instance" +
                             " from a persistent-new instance via deletePersistent, state is " +
                             states[curr]);
            }
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
                logger.debug("StateTransitionsReturnedObjects: Unable to create persistent-deleted instance" +
                             " from a persistent instance via deletePersistent(), state is " +
                             states[curr]);
            }
            return null;
        }
        return obj;
    }

    /** */
    public StateTransitionObj getPersistentNontransactionalInstance()
    {
        StateTransitionObj obj = getHollowInstance();
        if( obj == null ) return null;
        boolean nontransactionalRead = 
                pm.currentTransaction().getNontransactionalRead();
        pm.currentTransaction().setNontransactionalRead(true);
        obj.readField();
        pm.makeNontransactional(obj);
        pm.currentTransaction().setNontransactionalRead(nontransactionalRead);
        int curr = currentState(obj);
        if( curr != PERSISTENT_NONTRANSACTIONAL && curr != HOLLOW ) { 
            if (debug) {
                logger.debug("StateTransitionsReturnedObjects: Unable to create persistent-nontransactional instance" +
                             " from a persistent-clean instance via makeNontransactional(), state is " +
                             states[curr]);
            }
            return null;
        }
        return obj;
    }

    /** */
    public StateTransitionObj getPersistentNontransactionalDirtyInstance()
    {
        StateTransitionObj obj = getPersistentNontransactionalInstance();
        if( obj == null ) return null;
        obj.writeField(10000);
        int curr = currentState(obj);
        if( curr != PERSISTENT_NONTRANSACTIONAL_DIRTY ) { 
            if (debug) {
                logger.debug("StateTransitionsReturnedObjects: Unable to create persistent-nontransactional-dirty instance" +
                             " from a persistent-clean instance via makeNontransactional()/JDOHelper.makeDirty," +
                             " state is " + states[curr]);
            }
            return null;
        }
        return obj;
    }

    /** */
    public StateTransitionObj getDetachedCleanInstance()
    {
        StateTransitionObj obj = getHollowInstance();
        if( obj == null ) return null;
        obj = (StateTransitionObj) pm.detachCopy(obj);
        int curr = currentState(obj);
        if( curr != DETACHED_CLEAN ) { 
            if (debug) {
                logger.debug("StateTransitionsReturnedObjects: Unable to create detached-clean instance" +
                             " from a persistent-clean instance via detachCopy," +
                             " state is " + states[curr]);
            }
            return null;
        }
        return obj;
    }

    /** */
    public StateTransitionObj getDetachedDirtyInstance()
    {
        StateTransitionObj obj = getHollowInstance();
        if( obj == null ) return null;
        obj = (StateTransitionObj) pm.detachCopy(obj);
        obj.writeField(1000);
        int curr = currentState(obj);
        if( curr != DETACHED_DIRTY ) { 
            if (debug) {
                logger.debug("StateTransitionsReturnedObjects: Unable to create detached-dirty instance" +
                             " from a persistent-clean instance via detachCopy/persistent field modification," +
                             " state is " + states[curr]);
            }
            return null;
        }
        return obj;
    }
}
