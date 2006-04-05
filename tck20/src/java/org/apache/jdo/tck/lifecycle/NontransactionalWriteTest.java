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
 
package org.apache.jdo.tck.lifecycle;

import javax.jdo.JDOOptimisticVerificationException;
import javax.jdo.JDOUnsupportedOptionException;

import javax.jdo.PersistenceManager;

import org.apache.jdo.tck.JDO_Test;

import org.apache.jdo.tck.pc.mylib.VersionedPCPoint;

import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B> Test NontransactionalWrite abstract base class
 *<BR>
 *<B>Keywords:</B> NontransactionalWrite
 *<BR>
 *<B>Assertion ID:</B>
 *<BR>
 *<B>Assertion Description: </B>
 */

abstract public class NontransactionalWriteTest extends JDO_Test {

    /** 
     * The ObjectId of the pc instance, set by method
     * createAndModifyInstance. 
     */ 
    protected Object oid;

    /**
     * The original value of the X field of the pc instance, 
     * set by method createAndModifyInstance. 
     */
    protected int originalXValue = 100;
    protected int newXValue = 999;
    protected int conflictXValue = 555;

    /**
     * Create a new VersionedPCPoint instance,modify its X value,
     * and store its oid in the test class oid. The persistence manager
     * referenced by the pm field (in the superclass) manages the
     * nontransactional dirty instance.
     * This method is only executed if the
     * JDO implementation supports the required features, otherwise
     * this method would be localSetUp.
     */
    protected VersionedPCPoint createAndModifyVersionedPCPoint() {
        addTearDownClass(VersionedPCPoint.class);
        getPM().currentTransaction().begin();
        pm.currentTransaction().setNontransactionalWrite(true);
        pm.currentTransaction().setNontransactionalRead(true);
        pm.currentTransaction().setRetainValues(true);
        VersionedPCPoint instance =  new VersionedPCPoint(originalXValue, 200);
        pm.makePersistent(instance);
        oid = pm.getObjectId(instance);
        pm.currentTransaction().commit();
        instance.setX(newXValue);
        return instance;
    }

    /** 
     * Begin and rollback a transaction using the persistence manager
     * that manages the nontransactional dirty instance. 
     * @param optimistic use optimistic transaction
     */
    protected void beginAndRollbackTransaction(boolean optimistic) {
        getPM().currentTransaction().setOptimistic(optimistic);
        pm.currentTransaction().begin();
        pm.currentTransaction().rollback();
    }

    /** 
     * Begin and commit a transaction using the persistence manager
     * that manages the nontransactional dirty instance. 
     * @param optimistic use optimistic transaction
     */
    protected void beginAndCommitTransaction(boolean optimistic) {
        getPM().currentTransaction().setOptimistic(optimistic);
        pm.currentTransaction().begin();
        pm.currentTransaction().commit();
    }

    /** 
     * Begin and commit a transaction using the persistence manager
     * that manages the nontransactional dirty instance. This 
     * transaction must fail due to a conflicting update.
     * @param optimistic use optimistic transaction
     */
    protected void beginAndCommitTransactionFails(String location, 
            boolean optimistic) {
        getPM().currentTransaction().setOptimistic(optimistic);
        pm.currentTransaction().begin();
        try {
            pm.currentTransaction().commit();
        } catch (JDOOptimisticVerificationException ex) {
            // good catch; return
            return;
        }
        appendMessage(location + "transaction succeeded but" +
                " should not succeed.");
    }

    /**
     * Check the x value of the persistent instance referenced 
     * by the oid. Use a new persistence manager and check the
     * value in a new transaction so there is no interference 
     * with the persistence manager that managed the nontransactional
     * dirty instance.
     */
    protected void checkXValue(String location, int expectedXValue) {
        PersistenceManager pmCheck = pmf.getPersistenceManager();
        pmCheck.currentTransaction().begin();
        VersionedPCPoint instance = 
                (VersionedPCPoint)pmCheck.getObjectById(oid, true);
        int actualXValue = instance.getX();
        pmCheck.currentTransaction().commit();
        cleanupPM(pmCheck);
        if (expectedXValue != actualXValue) {
            appendMessage(location + NL + 
                    "expected: " + expectedXValue + NL +
                    "  actual: " + actualXValue);
        }
    }

    /**
     * Perform a conflicting transaction that updates the same field
     * as the nontransactional dirty instance. The field value is
     * conflictXValue. This conflicting transaction uses a new 
     * persistence manager.
     */
    protected void conflictingUpdate() {
        PersistenceManager pmConflict = pmf.getPersistenceManager();
        pmConflict.currentTransaction().setOptimistic(false);
        VersionedPCPoint instance = 
                (VersionedPCPoint)pmConflict.getObjectById(oid);
        instance.setX(conflictXValue);
        pmConflict.currentTransaction().commit();
        cleanupPM(pmConflict);
    }

    /** 
     * Check that all Nontransactional features are supported.
     * @param optimistic check for whether Optimistic is supported as well.
     */
    protected boolean checkNontransactionalFeaturesSupported
            (boolean optimistic) {
        if (!checkNontransactionalWriteSupported()
                || !checkNontransactionalReadSupported()
                || !checkRetainValuesSupported()) 
            return false;
        if (optimistic) {
            if (!checkOptimisticSupported()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if NontransactionalWrite is supported, and log a debug
     * message if it is not.
     */
    protected boolean checkNontransactionalWriteSupported() {
        if (!isNontransactionalWriteSupported()) {
            printUnsupportedOptionalFeatureNotTested(
                    getClass().getName(), 
                    "javax.jdo.option.NontransactionalWrite");
            return false;
        }
        return true;
    }

    /**
     * Check if NontransactionalRead is supported, and log a debug
     * message if it is not.
     */
    protected boolean checkNontransactionalReadSupported() {
        if (!isNontransactionalReadSupported()) {
            printUnsupportedOptionalFeatureNotTested(
                    getClass().getName(), 
                    "javax.jdo.option.NontransactionalRead");
            return false;
        }
        return true;
    }

    /**
     * Check if Optimistic is supported, and log a debug
     * message if it is not.
     */
    protected boolean checkOptimisticSupported() {
        if (!isOptimisticSupported()) {
            printUnsupportedOptionalFeatureNotTested(
                    getClass().getName(), 
                    "javax.jdo.option.Optimistic");
            return false;
        }
        return true;
    }

    /**
     * Check if RetainValues is supported, and log a debug
     * message if it is not.
     */
    protected boolean checkRetainValuesSupported() {
        if (!isRetainValuesSupported()) {
            printUnsupportedOptionalFeatureNotTested(
                    getClass().getName(), 
                    "javax.jdo.option.RetainValues");
            return false;
        }
        return true;
    }

}
