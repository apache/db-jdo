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

package org.apache.jdo.impl.fostore;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Map;

import javax.jdo.JDOFatalException;
import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOFatalDataStoreException;
import javax.jdo.JDOFatalUserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.store.Connector;
import org.apache.jdo.util.I18NHelper;

/**
* Represents a set of one or more requests that will be sent to the store.
* @see Request
*
* @author Dave Bristor
*/
class Message {
    /** Connector for which this message acts as a transport. */
    private final Connector connector;

    /** Set of request objects that are in the process of carrying out their
     * function; maps from a RequestId to Request.  When a request is
     * created, it adds itself to this map.  When a request's reply is
     * received, it is looked up by RequestId in this map, removed, and the
     * Request object given the reply.
     */
    private final HashMap requests = new HashMap();

    /** Set of CLIDs associated with ActivateClass requests that are in this
     * Message. It is cleared by initOutput. */
    private final HashSet clids = new HashSet();

    /**
     * This contains the Message's actual data for the store.
     */
    private final FOStoreOutput out = new FOStoreOutput();

    /**
     * Indicates the number of requests that have been written into this
     * Message.
     */
    private int numRequests = 0;
    
    /**
     * Contains the position in the output of the numRequest stashed by
     * initOutput.  This is modified and used only by initOutput and 
     * finishOutput.
     */
    private int numRequestStash = 0;
    
    /** 
     * The version number of the current protocol.  In future, this version 
     * number can be used to identify mismatches in protocol.
     * The format is (short)major; (byte)minor; (byte)patch
     * Only use major for compatibility checks; always bump major when
     * incompatibly changing protocol.
     */
    private static final int VERSION_NUMBER = 0x00010000; // version 1.0.0

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N

    /** Constructor for Message.  The output stream is initialized to
     * contain the version number and a stash for the number of requests.
     * @param connector The Connector on whose behalf this Message is serving
     * as a transport.  May be null.
     */
    Message(Connector connector) {
        this.connector = connector;
        initOutput();
    }

    Message() {
        this(null);
    }

    /**
     * @return The connector associated with this Message.
     */
    Connector getConnector() {
        return connector;
    }
    
    /** Return the FOStoreOutput stream for requests to be inserted.
     * @return the FOStoreOutput under construction
     */
    public FOStoreOutput getOutput() {
        return out;
    }

    /**
     * Write this message to the given connection, and read replies from that
     * connection, processing replies as they are read.
     * @see RequestHandler#handleRequests for stream header reader.
     * @param con the FOStoreClientConnection for this message
     */
    void processInStore(FOStoreClientConnection con,
                        boolean okToReleaseConnection) {

        try {
            sendToStore (con);
        } finally {
            try {
                receiveFromStore (con, okToReleaseConnection);
            } finally {
                initOutput(); // Prepare for next send.
            }
        }
    }
    
    /** Send the current Message buffer to the store.  The data contained
     * in out is written as one block of data.  The connection's 
     * sendToStore is responsible for sending the data and handling the
     * processing at the server side.  
     * @param con the FOStoreClientConnection
     */
    private void sendToStore(FOStoreClientConnection con) {
        try {
            finishOutput();
            if (logger.isTraceEnabled()) {
                Tester.dump("MsTS", out.getBuf(), out.getPos()); // NOI18N
            }
            con.sendToStore(out.getBuf(), 0, out.getPos());
        } catch (IOException ex) {
            throw new JDOFatalDataStoreException(
                msg.msg("ERR_SendToStore"), ex); // NOI18N
        }
    }

    /** Receive the replies from the store and process them.
     * @param con the FOStoreConnection with the replies.
     */
    private void receiveFromStore(FOStoreClientConnection con,
                                  boolean okToReleaseConnection) {
        
        try {
            DataInput di = con.getInputFromServer();

            // Process the replies *and then* close the connection, to prevent
            // the byte array underlying data input (which is property of the
            // connection) from being overwritten by another thread.
            ReplyHandler.processReplies(di, this);
        } catch (IOException ioe) {
            throw new JDOFatalDataStoreException (
                msg.msg("ERR_ReceiveFromStore"), ioe); // NOI18N
        } finally {
            if (okToReleaseConnection) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                        "Message.receiveFromStore: closing connection"); //NOI18N
                }
                closeConnection(con);
            }
        }
    }


    /**
     * Maps the given request to the given requestId.
     * @param requestId Identifies a request within a JVM.
     * @param request A request for some operation on the store for which a
     * reply is expected. */
    void putRequest(RequestId requestId, Request request) {
        if (logger.isDebugEnabled()) {
            logger.debug("Msg.putRequest: " + requestId + // NOI18N
                           " " + request.getClass().getName()); // NOI18N
        }
        if (requests.containsKey(requestId)) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "putRequest", // NOI18N
                msg.msg("ERR_DuplicateRequestID", requestId)); // NOI18N
        } else {
            numRequests++;
            requests.put(requestId, request);
        }
    }

    /**
    * Provides the Request corresponding to the give requestId, removing it
    * from the internal map of requests (i.e., subsequent getRequest
    * invocations for the same requestId will return null).
    * @param requestId Identifier for a particular request in this JVM.
    * @return The Request identified by the given identifier or null if there
    * is no such Request.
    */
    Request getRequest(RequestId requestId) {
        if (logger.isDebugEnabled()) {
            logger.debug ("Msg.getRequest: " + requestId); // NOI18N
        }
        Request rc = (Request)requests.remove(requestId);
        if (rc == null) { // oops, this should never happen
        if (logger.isDebugEnabled()) {
            logger.debug ("Msg.getRequest: unable to find: " + requestId); // NOI18N
                for (Iterator it = requests.entrySet().iterator();it.hasNext();) {
                    Map.Entry entry = (Map.Entry) it.next();
                    logger.debug ("Msg.getRequest: found: " +  // NOI18N
                                    "entry.key: " + entry.getKey() + // NOI18N
                                    " entry.Value: " + entry.getValue()); // NOI18N
                }
            }
        }
        return rc;
    }

    /**
     * Returns true if this message has requests for the store.
     * @return true if there are any messages.
     */
    boolean hasRequests() {
        return numRequests > 0;
    }

    /** Initialize the output buffer with version number and a stash for number
     * of requests.
     */
    private void initOutput() {
        try {
            out.setPos(0);
            out.writeInt (VERSION_NUMBER);
            numRequests = 0;
            numRequestStash = out.beginStash();
            clids.clear();
        } catch (IOException ioe) {
            throw new JDOFatalInternalException (
                msg.msg("ERR_InitOutput"), ioe); // NOI18N
        }
    }
    
    /** Finish the output buffer by updating the stash with number of requests.
     */
    private void finishOutput () {
        try {
            out.endStash (numRequests, numRequestStash);
        } catch (IOException ioe) {
            throw new JDOFatalInternalException (
                msg.msg("ERR_FinishOutput"), ioe); // NOI18N
        }
    }
    
    /** Close the connection to the store.
     * @param con the connection to close.
     */
    private void closeConnection(FOStoreClientConnection con) {
        if (logger.isDebugEnabled()) {
            logger.debug ("Msg.closeConnection:" + con); // NOI18N
        }
        try {
            con.close();
        } catch (IOException ioe) {
            throw new JDOFatalInternalException (
                msg.msg("ERR_CloseConnection"), ioe); // NOI18N
        }
    }
            
    /**
     * Verify a Message's version number.
     * @throws JDOFatalUserException if the version number does not match
     * that in the caller's JVM.
     */
    static void verifyVersionNumber(DataInput in) throws IOException {
        int verNum = in.readInt();
        if (VERSION_NUMBER != verNum) {
            throw new JDOFatalUserException(
                msg.msg("EXC_MessageVersionMismatch", // NOI18N
                        new Integer(verNum), new Integer(VERSION_NUMBER)));
        }
    }

    /**
     * Add the given CLID to the set of CLIDs maintained by this Message.
     */
    void addCLID(CLID clid) {
        clids.add(clid);
    }

    /**
     * Returns true if the given CLID is in this Message's set of CLIDs.
     */
    boolean containsCLID(CLID clid) {
        return clids.contains(clid);
    }

    // Debug support
    /** Dump the complete current contents of the message buffer.
     */    
    public void dump() {
        Tester.dump("MSG", out.getBuf(), out.getPos()); // NOI18N
    }
}
