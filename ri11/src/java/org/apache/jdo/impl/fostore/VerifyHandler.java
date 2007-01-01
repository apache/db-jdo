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
import java.util.Arrays;

import javax.jdo.JDOUserException;

import org.apache.jdo.util.I18NHelper;


/**
* Process requests to verify instances in the datastore.
*
* @author Dave Bristor
*/
//
// This is server-side code.  It does not need to live in the client.
//
class VerifyHandler extends UpdateHandler {
    /** I18N Support */
    // Note that in this file we're using keys with the "EXC" prefix, but
    // outside the context of throwing an exception: this is because the
    // client side will take these messages and put them into exceptions.  See
    // ReplyHandler.handleReplies.
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    private VerifyHandler(Reply reply, int length,
                          FOStoreServerConnection con) {

        super(reply, length, con);
    }

    static final HandlerFactory factory =
        new HandlerFactory() {
            public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new VerifyHandler(reply, length, con);
            }};

    /**
     * Verify that instance exists and/or its values match those in the
     * client.
     */
    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        FOStoreInput in = con.getInputFromClient();

        if (logger.isDebugEnabled()) {
            logger.debug("VerifyHandler.hR: begin"); // NOI18N
        }

        boolean rc = false;

        OID oid = OID.read(in);
        boolean verifyFields = in.readBoolean();

        try {
            if (verifyFields) {
                Block block = readBlock(in);
                rc = verify(oid, true, block);
            } else {
                rc = verify(oid, false, null);
            }
            reply.writeBoolean(rc);
            reply.setStatus(Status.OK);
        } catch (Exception ex) {
            reply.writeBoolean(false);
            reply.setStatus(Status.ERROR,
                            msg.msg("ERR_VerifyException", oid, ex)); // NOI18N
        }

        if (logger.isDebugEnabled()) {
            logger.debug("VerifyHandler.hR: end, rc=" + rc); // NOI18N
        }

        return null;
    }
}
