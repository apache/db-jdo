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

/**
* Process BeginTx requests.
*
* @author Dave Bristor
*/
// This is server-side code.  It does not need to live in the client.
class BeginTxHandler extends RequestHandler {
    private BeginTxHandler(Reply reply, int length,
                         FOStoreServerConnection con) {

        super(reply, length, con);
    }
    
    public static final HandlerFactory factory =
        new HandlerFactory() {
                public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new BeginTxHandler(reply, length, con);
            }};

    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        FOStoreInput in = con.getInputFromClient();
        boolean optimistic = in.readBoolean();
        con.setOkToReleaseDatabase(optimistic);
        
        if (logger.isDebugEnabled()) {
            logger.debug("BeginTxHandler.hR: " + optimistic); // NOI18N
        }

        reply.setStatus(Status.OK);

        return null;
    }
}






