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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

/**
* Process GetInstances requests.
*
* @author Dave Bristor
*/
// This is server-side code.  It does not need to live in the client.
class GetInstancesHandler extends RequestHandler {

    private GetInstancesHandler(Reply reply, int length,
                         FOStoreServerConnection con) {

        super(reply, length, con);
    }
    
    public static final HandlerFactory factory =
        new HandlerFactory() {
                public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new GetInstancesHandler(reply, length, con);
            }};

    /**
     * Get some instances from the database, and return them.
     */
    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        FOStoreInput in = con.getInputFromClient();
        FOStoreDatabase db = con.getDatabase();

        int numInstances = in.readInt();

        Status status = null;

        int numReturned = 0;
        int numReturnedPos = reply.beginStash();

        for (int i = 0; i < numInstances; i++) {
            try {
                OID oid = OID.read(in);
                reply.writeOID(oid);
                if (logger.isDebugEnabled()) {
                    logger.debug(
                        "GetInstancesHandler.hR/2: " + // NOI18N
                        oid  + " " + // NOI18N
                        Tester.toHex(oid.oid, 16));
                }

                Block block = (Block)db.get(oid);
                if (logger.isTraceEnabled()) {
                    block.dump();
                }

                byte data[] = block.getData();
                reply.writeInt(data.length);
                reply.write(data);
                numReturned++;
            } catch (FOStoreDatabaseException ex) {
                status = Status.WARN;
            }
        }

        reply.endStash(numReturned, numReturnedPos);
            
        if (null == status) {
            status = Status.OK;
        }
        reply.setStatus(status);

        if (logger.isDebugEnabled()) {
            logger.debug(
                "GetInstancesHandler.hR/3: " + // NOI18N
                "numReturned=" + numReturned + ": " +status); // NOI18N
        }
        return null;
    }
}
