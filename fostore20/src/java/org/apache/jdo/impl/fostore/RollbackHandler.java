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
* Process Rollback requests.
*
* @author Dave Bristor
*/
// This is server-side code.  It does not need to live in the client.
class RollbackHandler extends RequestHandler {
    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    private RollbackHandler(Reply reply, int length,
                         FOStoreServerConnection con) {

        super(reply, length, con);
    }
    
    public static final HandlerFactory factory =
        new HandlerFactory() {
                public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new RollbackHandler(reply, length, con);
            }};

    /**
    * Causes RequestHandler.handleRequests to not run finishers when a
    * RollbackRequest has been found in a message.  There's no reason to run
    * them in such a case, as the rollback will undo their effects anyway.
    */
    protected boolean getOkToFinish() {
        return false;
    }


    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        if (logger.isDebugEnabled()) {
            logger.debug("RollbackHandler.hR"); // NOI18N
        }
        
        try {
            con.rollback();
        } catch (FOStoreDatabaseException ex) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "handleRequest", // NOI18N
                msg.msg("ERR_RollbackFailed", ex)); // NOI18N
        }

        con.setOkToReleaseDatabase(true);
        reply.setStatus(Status.ROLLBACK);
        return null;
    }
}
