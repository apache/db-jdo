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

import org.apache.jdo.util.I18NHelper;


/**
* Process Commit requests.
*
* @author Dave Bristor
*/
// This is server-side code.  It does not need to live in the client.
class CommitHandler extends RequestHandler {
    /** Indicates whether or not the request should be honored.
    */
    private boolean okToCommit = true;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    private CommitHandler(Reply reply, int length,
                         FOStoreServerConnection con) {

        super(reply, length, con);
    }
    
    public static final HandlerFactory factory =
        new HandlerFactory() {
                public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new CommitHandler(reply, length, con);
            }};

    /**
     * An instance of this class is the means by which a commit actually
     * happens.  We must do this in a finisher, and not in handleRequest,
     * because the finishers for insert, etc. must run before we commit.
     */
    private class CommitFinisher implements RequestFinisher {
        private final FOStoreServerConnection con;

        CommitFinisher(FOStoreServerConnection con) {
            this.con = con;
        }

        /**
         * Really commit.
         */
        public void finish() {
            if (logger.isDebugEnabled()) {
                logger.debug("CommitFinisher.finish: " + okToCommit); // NOI18N
            }
            if (okToCommit) {
                try {
                    con.commit();
                } catch (FOStoreDatabaseException ex) {
                    throw new FOStoreFatalInternalException(
                        this.getClass(), "finish", // NOI18N
                        msg.msg("ERR_CommitFailed", ex)); // NOI18N
                }
            }
        }
    }

    protected void setOkToCommit(boolean okToCommit) {
        this.okToCommit = okToCommit;
    }
    
    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        FOStoreInput in = con.getInputFromClient();
        boolean okToReleaseDatabase = in.readBoolean();

        if (logger.isDebugEnabled()) logger.debug(
            "CommitHandler.hR: ok=" + okToReleaseDatabase); // NOI18N

        con.setOkToReleaseDatabase(okToReleaseDatabase);
        reply.setStatus(Status.OK);

        return new CommitFinisher(con);
    }
}






