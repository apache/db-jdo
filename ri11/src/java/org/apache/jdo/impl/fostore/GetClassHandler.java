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

import javax.jdo.JDOUserException;

import org.apache.jdo.util.I18NHelper;

/**
* Process GetClass requests.
*
* @author Dave Bristor
*/
// This is server-side code.  It does not need to live in the client.
class GetClassHandler extends RequestHandler {

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    private GetClassHandler(Reply reply, int length,
                         FOStoreServerConnection con) {

        super(reply, length, con);
    }
    
    public static final HandlerFactory factory =
        new HandlerFactory() {
                public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new GetClassHandler(reply, length, con);
            }};

    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        DataInput in = con.getInputFromClient();
        FOStoreDatabase db = con.getDatabase();
        CLID clid = CLID.read(in);

        OID oid = db.getDBInfo().getDBClassOID(clid);
        if (logger.isDebugEnabled()) {
            logger.debug(
                "GetClassHandler.hR/0: " + clid +  // NOI18N
                " " + Tester.toHex(clid.getId(), 8) + ", " + oid + // NOI18N
                " " + Tester.toHex(oid.oid, 16)); // NOI18N
        }
        
        FOStoreDatabase fodb = con.getDatabase();
        try {
            DBClass dbClass = (DBClass)fodb.getIfExists(oid);
            if (null == dbClass) {
                reply.setStatus(
                    Status.ERROR, 
                    msg.msg("EXC_DoesNotExist", oid.toString())); // NOI18N
            } else {
                String className = dbClass.getName();
                reply.writeUTF(className);
                reply.setStatus(Status.OK);
                if (logger.isDebugEnabled()) {
                    logger.debug(
                        "GetClassHandler.hR/1: " + className); // NOI18N
                }
            }

        } catch (ClassCastException ex) {
            reply.setStatus(Status.ERROR, ex);
        }
        
        return null;
    }
}






