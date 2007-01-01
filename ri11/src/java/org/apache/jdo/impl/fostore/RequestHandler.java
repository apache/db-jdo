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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

import javax.jdo.JDOException;
import javax.jdo.JDOFatalDataStoreException;
import javax.jdo.JDOFatalUserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.util.I18NHelper;

/**
* This dispatches each request received by the store to the appropriate
* request-type-specific request handler.  It is very dependent on the
* 'message full of data' means of communicating between client and store.
*
* @author Dave Bristor
*/
//
// This is server-side code.  It does not need to live in the client.
//
abstract class RequestHandler {
    /** Subclasses use this Reply instance to send data back to their
     * corresponding client-side request. */
    protected final Reply reply;

    /** Length of the data in the Request. */
    protected final int length;

    /** Connection on which the Request arrived. */
    protected final FOStoreServerConnection con;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N    
    
    /** Means by which subclasses are created.  Each RequestHandler subclass
     * should have a static inner class that implements this interface.
     */
    interface HandlerFactory {
        /**
         * @param reply Reply instance into which the returned handler may
         * write request/reply - specific information.
         * @param length Length in bytes of the request data.
         * @param con Connection from which the request was made.
         *@return A RequestHandler instance for a specific subclass of
         * RequestHandler.
         */
        public RequestHandler getHandler(Reply reply, int length,
                                         FOStoreServerConnection con);
    }

    /**
    * @param reply Reply to which request handler should write all reply
    * information.
    * @param length Number of bytes in the connection's input that are for
    * this request; subclasses <strong>must</strong> read all bytes so that
    * other requests can work.
    * @param con Connection from which request handler reads the
    * request data.
    */
    protected RequestHandler(Reply reply, int length,
                             FOStoreServerConnection con) {
        
        this.reply = reply;
        this.length = length;
        this.con = con;
    }

    /**
    * The RollbackHandler should override this and return false, so that
    * finishers are not run when we are rolling back.
    */
    protected boolean getOkToFinish() {
        return true;
    }

    /**
    * The CommitHandler should override this and save the given value, then
    * use it to determine whether or not to commit.
    */
    protected void setOkToCommit(boolean okToCommit) { }

    /**
     * Handles all requests that can be read at this time from the given
     * connection.  Reads the number of requests, then reads each one and, in
     * turn, invokes the handleRequest operation on each.
     * <p>
     * The data it generates for the client is documented; see {@link
     * ReplyHandler#processReplies}.
     * <p>
     * If after all requests are thusly handled, none have indicated that the
     * second round of handling, called finishing, is not to be done, then
     * performs this second round.  The finishers that are invoked are
     * precisely those which were returned from each request's handleRequest
     * invocation, and the finishers have their finish() methods invoked in
     * the same order in which the requests were originally invoked.
     * <p>
     * Finally, writes the number of replies (at the beginning of the reply
     * data, in a spot that was set aside for this purpose), and sends replies
     * back to the client.
     * <p>
     * This method is <em>very</em> paranoid about error checking, which
     * clutters it up some, but is necessary to ensure that the server keeps
     * running.
     */
    static void handleRequests(FOStoreServerConnection con) {
        // List of finishers to run after all requests have been handled.
        // Provides a means for a second phase of operations to run, at the
        // request of the requests themselves.  Re-initialized each time
        // handleRequests is run.
        // @see Message#sendToStore for stream header writer.
        ArrayList finishers = new ArrayList();
        
        DataInput in = con.getInputFromClient();
        int numRequests = 0;

        FOStoreOutput serverData = con.getOutputForClient();
        int numReplies = 0;

        // Result of processing all requests.  Written as part of the reply
        // data's "header".
        Status overallStatus = Status.OK;

        // Position in serverData where the reply's overall status is written.
        // Used in case we have to overwrite that value.
        //
        // Why do we initialize statusPos?
        // We have to because if not, the compiler complains that it might
        // not have been initialized, at it is used as an R-value.  And while
        // this is technically true, in fact it is initialized right away
        // inside the first try block, and nothing at or before that
        // initialization can throw an exception.  (Well, at least not
        // anything we'd ever recover from.)
        int statusPos = 0;

        // Position in serverData wher number of replies is written.
        int numRepliesPos;
        
        // If true, then run finishers after handling all requests.
        // This is set below by invoking okToFinish on each RequestHandler
        // instance.  Only RollbackHandler should return false.
        boolean okToFinish = true;

        // The message might contain a CommitRequest.  We assume that all
        // requests will succeed.  But if one of them sets its reply's status
        // to Status.ERROR or Status.FATAL, we don't commit.
        boolean okToCommit = true;

        try {
            Reply.writeVersionNumber(serverData);

            // Write a default overall status of the reply.  Will be
            // overwritten later if there are any fatal problems in processing
            // the requests.
            statusPos = serverData.getPos();
            overallStatus.write(serverData);
            
            // Write a default number of replies.  Later write the real number
            // of replies.
            numRepliesPos = serverData.getPos();
            serverData.writeInt(0);

            try {
                Message.verifyVersionNumber(in);
                numRequests = in.readInt();
                if (logger.isDebugEnabled()) {
                    logger.debug("RequestHandler: numRequests=" + // NOI18N
                                   numRequests);
                }
            } catch (JDOFatalUserException ex) {
                // Message version mismatch
                throw ex;
            } catch (IOException ex) {
                throw new FOStoreFatalIOException(
                    RequestHandler.class,
                    "handleRequests/numRequests ", ex); // NOI18N
            }

            for (int i = 0; i < numRequests; i++) {
                Reply reply = null; // Prepare for this iteration.
                RequestId requestId = new RequestId(in);
                RequestType requestType = new RequestType(in);

                if (logger.isDebugEnabled()) {
                    logger.debug("RequestHandler: " + // NOI18N
                                   requestId + "/" + // NOI18N
                                   requestType);
                }

                HandlerFactory factory = requestType.getHandlerFactory();
                if (null == factory) {
                    throw new FOStoreFatalInternalException(
                        RequestHandler.class, "handleRequests", // NOI18N
                        msg.msg("ERR_CannotCreateHandler", // NOI18N
                                requestType.toString()));
                }
                int length = in.readInt();
                reply = con.createReply(requestId);
                numReplies++;

                RequestHandler rh =
                    factory.getHandler(reply, length, con);

                // Run the request, and save it's finisher (if any)
                rh.setOkToCommit(okToCommit);
                RequestFinisher rf = rh.handleRequest();

                // Check for commit- and finish- ability
                Status replyStatus = reply.getStatus();
                if (Status.ERROR.equals(replyStatus) ||
                    Status.FATAL.equals(replyStatus) ||
                    Status.OPTIMISTIC.equals(replyStatus)) {

                    okToCommit = false;

                } else if (Status.ROLLBACK.equals(replyStatus)) {
                    okToCommit = false;

                     // No point in doing work that'll just be undone
                    okToFinish = false;

                    // Mark the status as rolled back.
                    overallStatus = replyStatus;
                    int pos = serverData.getPos();
                    serverData.setPos(statusPos);
                    overallStatus.write(serverData);

                    // Instead of number of replies, write the number of bytes
                    // in the reply, so that the client can skip them.
                    serverData.setPos(numRepliesPos);
                    serverData.writeInt(pos - numRepliesPos);

                    serverData.setPos(pos);
                    
                    // Once a rollback has been requested, there's no point in
                    // processing other replies.
                    break;

                } else {
                    okToFinish = rh.getOkToFinish();
                    if (okToFinish && null != rf) {
                        finishers.add(rf);
                    }
                }
            }

            if (okToFinish) {
                // Run the finishers they produced (if any).
                int numFinishers = finishers.size();
                for (int i = 0; i < numFinishers; i++) {
                    RequestFinisher rf = (RequestFinisher)finishers.get(i);
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                            "RequestHandler.hR: finish " + // NOI18N
                            rf.getClass().getName());
                    }
                    rf.finish();
                }
            }

            // If rollback, numRepliesPos already overwritten with length of
            // reply data.
            if (! overallStatus.equals(Status.ROLLBACK)) {
                // Write the number of replies produced.
                int pos = serverData.getPos();
                serverData.setPos(numRepliesPos);
                serverData.writeInt(numReplies);
                serverData.setPos(pos);
            }

        } catch (FOStoreLoginException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Login failure"); // NOI18N
            }
            try {
                int pos = serverData.getPos();
                serverData.setPos(statusPos);
                overallStatus = Status.LOGIN;
                overallStatus.write(serverData);
                serverData.writeUTF(ex.toString());
                int messagePos = serverData.getPos();
                if (messagePos > pos) {
                    pos = messagePos;
                }
                serverData.setPos(pos);
            } catch (IOException ex2) {
                giveUp(ex2);
            }
            
        } catch (Throwable ex) {
            // Handle unexpected failure.  Inform client that the replies
            // could not be processed.  Rollback the database.
            if (logger.isDebugEnabled()) {
                ex.printStackTrace();
            }
            try {
                int pos = serverData.getPos();
                serverData.setPos(statusPos);
                overallStatus = Status.FATAL;
                overallStatus.write(serverData);
                String message = Reply.getExceptionMessage(ex);
                if (null == message) {
                    message = msg.msg("EXC_Unknown"); // NOI18N
                }
                serverData.writeUTF(message);
                int messagePos = serverData.getPos();
                if (messagePos > pos) {
                    pos = messagePos;
                }
                serverData.setPos(pos);

                try {
                    con.rollback();
                } catch (FOStoreDatabaseException ex2) {
                    // Oh well, we tried.
                }
            } catch (IOException ex2) {
                giveUp(ex2);
            }
            
        } finally {
            try {
                con.sendToClient();

                if (logger.isDebugEnabled()) {
                    if (Status.OK == overallStatus) {
                        logger.debug(
                            "RequestHandler: " + overallStatus + // NOI18N
                            ", numReplies=" + numReplies); // NOI18N
                        
                    } else {
                        logger.debug(
                            "RequestHandler: " + overallStatus); // NOI18N
                    }
                }
            } catch (IOException ex) {
                giveUp(ex);
            } catch (FOStoreDatabaseException ex) {
                giveUp(ex);
            }
        }
    }

    /**
     * Invoke this when all attempts at communicating errors to the client
     * have failed.  Hope that someone is watching the console!
     */
    private static void giveUp(Throwable ex) {
        System.err.println(msg.msg("ERR_SendToClient", ex)); // NOI18N
        // XXX Should log this, not just print.
        ex.printStackTrace(System.err);
    }
    
    /**
    * Subclasses implement this to take care of individiual requests.
    * @return A RequestFinisher or null.  If null, then no further work is
    * required on behalf of this request.  If a RequestFinisher is returned,
    * then it is added to a list, and after all requests have been processed,
    * the finishers in the list have their finish() method invoked on them.
    * Finishers are invoked in the same order as the requests were.
    */
    abstract RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException;
}


