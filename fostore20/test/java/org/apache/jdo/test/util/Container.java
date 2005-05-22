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

package org.apache.jdo.test.util;

import java.util.*;

import javax.jdo.*;

import javax.transaction.*;
import javax.transaction.xa.*;

import org.apache.jdo.ejb.EJBHelper;
import org.apache.jdo.ejb.EJBImplHelper;
import org.apache.jdo.impl.fostore.FOStorePMF;
import org.apache.jdo.impl.pm.PersistenceManagerWrapper;
import org.apache.jdo.pm.PersistenceManagerFactoryInternal;
import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.store.Connector;

/**
 * Helper container to simulate test in a managed environment
 * @author Marina Vatkina
 */

public class Container implements EJBHelper {
    
    Vector pmfs = new Vector();
    PersistenceManagerFactory pmf0 = null;
    PersistenceManagerInternal pm = null;
    GlobalTx globaltx = null;
    UserTx usertx = null;
    final boolean verbose;
    
    /** Register this container with EJBImplHelper
     */
    public Container(boolean verbose) {
        this.verbose = verbose;
        EJBImplHelper.registerEJBHelper (this);
    }
    
    /** Get new PersistenceManagerFactory
     */
    public PersistenceManagerFactory getPMF(String url)
    {
        // RESOLVE:
        FOStorePMF pmf = new FOStorePMF();
        pmf.setConnectionURL (url);
        ((FOStorePMF)pmf).setConnectionCreate ("true");
        pmf.setConnectionUserName("");
        pmf.setConnectionPassword("");
        pmf.setOptimistic(false);
        
        return pmf;
    }

    /** Start CMT transaction
     */
    public void startCMT() {
        globaltx = new GlobalTx();
    }

    /** Start BMT transaction
     */
    public void startBMT() {
        if (usertx == null) {
            usertx = new UserTx();
        }
    }

    /** Finish BMT transaction
     */
    public void finishBMT() {
        usertx = null;
    }

    /** Commit CMT transaction. Rollback transaction if commit fails.
     */
    public void finishCMT(boolean commit) {
        try {
            if (commit) globaltx.commit();
            else throw new Exception("FORCING ROLLBACK");
        } catch (Exception e) {
            if (verbose) System.err.println ("finishCMT IN COMMIT: " + e.getMessage());
            try {
                globaltx.setRollbackOnly();
                globaltx.rollback();
            } catch (Exception e1) {
                if (verbose) System.err.println ("finishCMT IN ROLLBACK: " + e1);
                e1.printStackTrace();
            }
        }
        globaltx = null;
    }

    //
    // --- Implementation of EJBHelper interface ----
    //

    public javax.transaction.Transaction getTransaction(){
        return globaltx;
    }

    public UserTransaction getUserTransaction() {
        return usertx;
    }

    public int translateStatus(int st) {
        return st;
    }

    public PersistenceManagerFactory replacePersistenceManagerFactory(
        PersistenceManagerFactory pmf) {
        try {
            Iterator it = pmfs.iterator();
            while (it.hasNext()) {
                Object o = it.next();
                if (o.equals(pmf)) {
                    pmf0 = (PersistenceManagerFactory) o;
                    return pmf0;
                }
            }
            pmfs.add(pmf); // not found
            pmf0 = pmf;
            return pmf;

        } catch (Exception e) {
            e.printStackTrace();
            throw new JDOFatalInternalException("", e);  //NOI18N
        }
    }

    public Object enlistBeforeCompletion(Object component) {
        return null;
    }

    public void delistBeforeCompletion(Object im) {
    }

    //
    // --- End implementation of EJBHelper interface ----
    //

    protected Connector getConnector() {
        PersistenceManager pm = pmf0.getPersistenceManager();
        return ((PersistenceManagerFactoryInternal)pmf0).
            getStoreManager(pm).getConnector();
    }

    /**
     * Unregisters this EJBHelper and returns the delegate PMF.
     */
    public PersistenceManagerFactory close()
    {
        EJBImplHelper.registerEJBHelper(null);
        return pmf0;
    }

    /**
     * Class that simulates JTA Transaction
     */
    class GlobalTx implements javax.transaction.Transaction {
    
        Synchronization sync = null;
        int st = Status.STATUS_ACTIVE;
        Connector conn = null;

        public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException,
            java.lang.SecurityException, SystemException {
            try {
                sync.beforeCompletion();
                st = Status.STATUS_PREPARED;
    
                conn = getConnector();
                conn.commit();
                conn = null;
                pm = null;
    
                st = Status.STATUS_COMMITTED;
                sync.afterCompletion(st);
    
            } catch (Exception e) {
                e.printStackTrace();
                st = Status.STATUS_MARKED_ROLLBACK;
                this.rollback();
                throw new SystemException(e.toString());
            }
        }
    
        public boolean delistResource(XAResource xaRes, int flag)
            throws java.lang.IllegalStateException, SystemException{
            return false;
        }
    
        public boolean enlistResource(XAResource xaRes)
            throws RollbackException, java.lang.IllegalStateException, SystemException {
            return false; 
        }
    
        public int getStatus() throws SystemException {
            return st;
        }
    
        public void registerSynchronization(Synchronization sync)
            throws RollbackException, java.lang.IllegalStateException, SystemException {
            this.sync = sync;
        }
    
        public void rollback() throws java.lang.IllegalStateException, SystemException {
            if (st == Status.STATUS_ACTIVE) {
                st = Status.STATUS_ROLLING_BACK;
            }
            try {
                if (conn == null) {
                    conn = getConnector();
                }
                conn.rollback();
                conn = null;
                pm = null;
    
                st = Status.STATUS_ROLLEDBACK;
                sync.afterCompletion(st);
    
            } catch (Exception e) {
                e.printStackTrace();
                throw new SystemException(e.toString());
            }
        }
    
        public void setRollbackOnly() throws java.lang.IllegalStateException, SystemException {
            st = Status.STATUS_MARKED_ROLLBACK;
        }

    }
   
    /**
     * Class that simulates JTA UserTransaction
     */
    class UserTx implements UserTransaction {

        public void begin() throws NotSupportedException, SystemException {
            if (globaltx == null) {
                globaltx = new GlobalTx();
            }
        }

        public void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, java.lang.SecurityException,
            java.lang.IllegalStateException, SystemException {

            globaltx.commit();
            globaltx = null;
        }

        public void rollback() throws java.lang.IllegalStateException,
            java.lang.SecurityException, SystemException {
            globaltx.rollback(); 
            globaltx = null;
        }
    
        public void setRollbackOnly() throws java.lang.IllegalStateException, SystemException {
            globaltx.setRollbackOnly();
        }
    
        public int getStatus() throws SystemException {
            return globaltx.getStatus();
        }
    
        public void setTransactionTimeout(int seconds) throws SystemException { }
    }
   
}
