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

package org.apache.jdo.impl.fostore;

import java.io.IOException;

import javax.jdo.JDOException;
import javax.jdo.JDODataStoreException;
import javax.jdo.JDOFatalInternalException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.store.Connector;
import org.apache.jdo.util.I18NHelper;

/**
* FOStoreConnector represents a connection to the FOStoreDatabase.
*
* @author Dave Bristor
*/
class FOStoreConnector implements Connector {
    /** @see org.apache.jdo.store.Connector#setRollbackOnly */
    private boolean rollbackOnly = false;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N
    
    /**
    * Message in which this Connector buffers requests for the store.
    */
    private final Message message;

    /**
    * Datasource to which this Connector writes its Message.
    */
    private final FOStorePMF pmf;

    /**
    * Connection for interacting with store.
    */
    FOStoreClientConnection connection = null;
    
    /**
    * True if we can release this connection after flushing.  By default we
    * can; affected by transactions beginning and ending and their types.
    */
    private boolean okToReleaseConnection = true;

    /** True if flush is in progress. */
    private boolean busy = false;
    
    FOStoreConnector(FOStorePMF pmf) {
        this.pmf = pmf;
        this.message = new Message(this);
    }

    //
    // Implement Connector
    //

    /**
     * @see org.apache.jdo.store.Connector#begin
     */
    public void begin(boolean optimistic) {
        assertNotRollbackOnly();
        
        // If transaction is optimistic, then we can release the connection as
        // soon as data is flushed.  If it's datastore, then we can't release
        // the connection until after commit/rollback.
        this.okToReleaseConnection = optimistic;
        
        if (logger.isDebugEnabled()) {
            logger.debug(
                "FOConnector.begin: okToReleaseConnection=" +  // NOI18N
                okToReleaseConnection);
        }

        try {
            RequestFactory rf = pmf.getRequestFactory();
            BeginTxRequest request =
                rf.getBeginTxRequest(message, pmf, optimistic);
            request.doRequest();
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                getClass(), "update", ex); // NOI18N
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "update", ex); // NOI18N
        }
    }

    /**
     * @see org.apache.jdo.store.Connector#beforeCompletion
     */
    public void beforeCompletion() {
        assertNotRollbackOnly();
        
        // Nothing to do.
    }

    /**
     * Get a connection, process the message by using that connection to
     * interact with the database, read back the reply, release the
     connection.
     * @see org.apache.jdo.store.Connector#flush
     */
    public void flush() {
        assertNotRollbackOnly();
        assertNotBusy("flush"); // NOI18N
        busy = true;

        try {
        
            if (logger.isDebugEnabled()) {
                logger.debug(
                  "FOConnector.flush: " + // NOI18N
                  "okToReleaseConnection=" + okToReleaseConnection + // NOI18N
                  ", connection=" + connection); // NOI18N
            }

            if (message.hasRequests()) {
                if (logger.isTraceEnabled()) message.dump();

                if (connection == null) {
                    FOStoreConnectionFactory cf =
                        (FOStoreConnectionFactory)pmf.getConnectionFactory();
                    connection = (FOStoreClientConnection)cf.getConnection();
                }

                // Now send the message and process it in the store
                try {
                    message.processInStore(connection, okToReleaseConnection);
                } finally {
                    if (okToReleaseConnection) {
                        connection = null;
                    }
                }
            }
        } finally {
            busy = false;
        }
    }

    /**
    * Add a CommitRequest to the connector's message, and send it to the
    * store.  Then close the connection.
    * @see org.apache.jdo.store.Connector#commit
    */
    public synchronized void commit() {
        assertNotRollbackOnly();
        assertNotBusy("commit"); // NOI18N
        
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("FOConnector.commit"); // NOI18N
            }

            RequestFactory rf = pmf.getRequestFactory();
            CommitRequest request = rf.getCommitRequest(message, pmf);

            request.doRequest();
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "commit", ex); // NOI18N
        } catch (JDOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                getClass(), "commit", ex); // NOI18N
        } finally {
            // Now that we've commited, we can release the connection.
            okToReleaseConnection = true;
            flush();
        }
    }

    /**
     * If rollbackOnly is set, then the store has already done a
     * rollback, so we don't do one now (but neither do we throw an
     * exception, as do other methds).
     * @see org.apache.jdo.store.Connector#rollback
     * @see org.apache.jdo.impl.fostore.ReplyHandler#processReplies
     */
    public synchronized void rollback() {
        assertNotBusy("rollback"); // NOI18N
        if (logger.isDebugEnabled()) {
            logger.debug(
                "FOConnector.rollback, RBO=" + rollbackOnly); // NOI18N
        }
        
        if (! rollbackOnly) {
            try {
                RequestFactory rf = pmf.getRequestFactory();
                RollbackRequest request = rf.getRollbackRequest(message, pmf);
                
                request.doRequest();
            } catch (IOException ex) {
                throw new FOStoreFatalIOException(
                    this.getClass(), "rollback", ex); // NOI18N
            } catch (JDOException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new FOStoreFatalInternalException(
                    getClass(), "rollback", ex); // NOI18N
            } finally {
                // Now that we've rolled back, we can release the connection.
                okToReleaseConnection = true;
                flush();
            }
        }
    }

    /**
     * @see org.apache.jdo.store.Connector#setRollbackOnly
     */
    public void setRollbackOnly() {
        rollbackOnly = true;
    }

    /**
     * @see org.apache.jdo.store.Connector#getRollbackOnly
     */
    public boolean getRollbackOnly() {
        return rollbackOnly;
    }

    //
    // Implementation
    //

    /**
     * Provides the Message which this this connector uses to send data to the
     * store.
     */
    Message getMessage() {
        assertNotRollbackOnly();
        assertNotBusy("getMessage"); // NOI18N
        
        return message;
    }

    private void assertNotRollbackOnly() {
        if (rollbackOnly) {
            throw new JDODataStoreException(
                msg.msg("EXC_RollbackOnly")); // NOI18N
        }
    }

    private void assertNotBusy(String methodName) {
        if (busy) {
            throw new FOStoreFatalInternalException(
              getClass(), methodName,
                msg.msg("EXC_Busy")); // NOI18N
        }
    }
}
