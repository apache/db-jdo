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

/**
* Process CreateOIDRequests.  Causes allocation of a new OID in the datastore.
*
* @author Dave Bristor
*/
//
// This is server-side code.  It does not need to live in the client.
//
class CreateOIDHandler extends RequestHandler {
    private CreateOIDHandler(Reply reply, int length,
                         FOStoreServerConnection con) {

        super(reply, length, con);
    }
    
    public static final HandlerFactory factory =
        new HandlerFactory() {
                public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new CreateOIDHandler(reply, length, con);
            }};

    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        FOStoreDatabase fodb = con.getDatabase();
        DataInput in = con.getInputFromClient();
        OID oid = OID.read(in);

        CLID clid = oid.getCLID();
        if (clid.isProvisional()) {
            clid = fodb.getRealCLIDFromProvisional(clid);
        }

        DBInfo dbInfo = fodb.getDBInfo();
        OID realOID;
        if (oid.isProvisional()) {
            realOID = dbInfo.newInstanceOID(clid);
        } else {
            realOID = oid;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("CreateOIDHandler.hR: " + oid + " -> " + realOID); // NOI18N
        }

        if (oid.isProvisional()) {
            fodb.mapProvisionalOIDToReal(oid, realOID);
        }

        reply.writeOID(realOID);
        reply.setStatus(Status.OK);

        return null;
    }
}
