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
 
package org.apache.jdo.tck.api.persistencemanagerfactory;

import javax.jdo.JDOException;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;

import org.apache.jdo.tck.JDO_Test;
import org.apache.jdo.tck.util.BatchTestRunner;

/**
 *<B>Title:</B>CloseFailsIfTransactionActive of PersistenceManagerFactory  
 *<BR>
 *<B>Keywords:</B> persistencemanagerfactory
 *<BR>
 *<B>Assertion IDs:</B> A11.4-4.
 *<BR>
 *<B>Assertion Description: </B>
 * PersistenceManagerFactory.getPersistenceManager() throws
 * JDOUserException after the PersistenceManagerFactory is closed.
 * The exception contains an array of nested exceptions; each nested
 * exception contains as its failed object the PersistenceManager whose
 * Transaction is still active.
 * During close of the PersistenceManagerFactory, all PersistenceManager 
 * instances obtained from this PersistenceManagerFactory are 
 * themselves closed.
 */

/* 
 * Revision History
 * ================
 * Author :  Craig Russell
 * Date   :  05/16/03
 *
 */

public class CloseFailsIfTransactionActive extends JDO_Test {
    
    /** */
    private static final String ASSERTION_FAILED = 
        "Assertion A11.4-4 (CloseFailsIfTransactionActive) failed: ";
    
    /**
     * The <code>main</code> is called when the class
     * is directly executed from the command line.
     * @param args The arguments passed to the program.
     */
    public static void main(String[] args) {
        BatchTestRunner.run(CloseFailsIfTransactionActive.class);
    }

    protected boolean aborted = false;
    
    /** */
    public void test() {
        PersistenceManager pm1 = null;
        PersistenceManager pm2 = null;
        cleanupPMF(getPMF());
        pmf = null;
        
        try {
            pm1 = getPM();
            pm2 = getPM();
            pm1.currentTransaction().begin();
            pm1.currentTransaction().commit();
            pm2.currentTransaction().begin();
            closePMF(pmf); // don't use closePMF() because that sets pmf to null
            setAborted();
            fail(ASSERTION_FAILED,
                 "Close incorrectly succeeded with active transaction");
        } 
        catch (JDOUserException ex) {
            try {
                if (debug)
                    logger.debug("Caught expected exception " + ex.getMessage());
                PersistenceManager[] pms = getFailedPersistenceManagers(ex);
                if (pms.length != 1) {
                    setAborted();
                    fail(ASSERTION_FAILED,
                         "Unexpected number of nested exceptions: " + pms.length);
                } 
                else {
                    Object failed = pms[0];
                    if (pm2.equals(failed)) {
                        if (debug)
                            logger.debug("Found expected failed object " +
                                         failed.toString());
                    } 
                    else {
                        setAborted();
                        fail(ASSERTION_FAILED,
                             "Found unexpected failed object " +
                             failed.toString());
                    }
                }
                if (pm1.isClosed()){
                    fail(ASSERTION_FAILED, "Unexpected pm1 is closed.");
                }
                if (pm2.isClosed()){
                    fail(ASSERTION_FAILED, "Unexpected pm2 is closed.");
                }
            } 
            catch (Exception uex) {
                setAborted();
                fail(ASSERTION_FAILED,
                     "Caught 1 unexpected exception " + uex.toString());
            }
        } 
        catch (Exception ex) {
            setAborted();
            fail(ASSERTION_FAILED,
                 "Caught 2 unexpected exception " + ex.toString());
        }
        
        if (!isAborted()) {
            try {
                pm2.currentTransaction().commit();
                closePMF(pmf); // don't use closePMF() because that sets pmf to null
                if (!pm1.isClosed()){
                    fail(ASSERTION_FAILED,
                         "Unexpected pm1 is not closed.");
                }
                if (!pm2.isClosed()) {
                    fail(ASSERTION_FAILED,
                         "Unexpected pm2 is not closed.");
                }
            } 
            catch (Exception ex) {
                fail(ASSERTION_FAILED,
                     "Caught 3 unexpected exception " + ex.toString());
            }
        }
    }
    
    /** */
    protected void cleanupPMF(PersistenceManagerFactory pmf) {
        try {
            closePMF(pmf); // don't use closePMF() because that sets pmf to null
        } catch (JDOException ex) {
            PersistenceManager[] pms = getFailedPersistenceManagers(ex);
            int numberOfPersistenceManagers = pms.length;
            for (int i = 0; i < numberOfPersistenceManagers; ++i) {
                PersistenceManager pm = pms[i];
                if (pm == null) {
                    fail(ASSERTION_FAILED,
                         "Found unexpected null PersistenceManager");
                } 
                else {
                    Transaction tx = pm.currentTransaction();
                    if (tx.isActive()) {
                        if (debug)
                            logger.debug("Found active transaction; rolling back.");
                        tx.rollback();
                    } 
                    else {
                        fail(ASSERTION_FAILED,
                             "Unexpectedly, this transaction is not active: " + tx);
                    }
                }
            }
        }
    }
    
    /** */
    protected void setAborted() {
        aborted = true;
    }
    
    /** */
    protected boolean isAborted() {
        return aborted;
    }

    /** */
    protected PersistenceManager[] getFailedPersistenceManagers(JDOException ex) {
        Throwable[] nesteds = ex.getNestedExceptions();
        int numberOfExceptions = nesteds==null ? 0 : nesteds.length;
        PersistenceManager[] result = new PersistenceManager[numberOfExceptions];
        for (int i = 0; i < numberOfExceptions; ++i) {
            JDOException exc = (JDOException)nesteds[i];
            Object failedObject = exc.getFailedObject();
            if (exc.getFailedObject() instanceof PersistenceManager) {
                result[i] = (PersistenceManager)failedObject;
            } else {
                fail(ASSERTION_FAILED,
                     "Unexpected failed object of type: " +
                     failedObject.getClass().getName());
            }
        }
        return result;
    }
}
