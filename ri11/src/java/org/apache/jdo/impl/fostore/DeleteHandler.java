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
import java.io.IOException;

import org.apache.jdo.util.I18NHelper;


/**
* Processes requests to delete objects from the datastore.
*
* @author Dave Bristor
*/
class DeleteHandler extends RequestHandler {

    /** I18N Support */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    private DeleteHandler(Reply reply, int length,
                          FOStoreServerConnection con) {
        super(reply, length, con);
    }

    public static HandlerFactory factory =
        new HandlerFactory() {
            public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new DeleteHandler(reply, length, con);
            }};


    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        if (logger.isDebugEnabled()) logger.debug("DeleteHandler.hR"); //NOI18N

        DataInput in = con.getInputFromClient();
        OID oid = OID.read(in);
        boolean optimistic = in.readBoolean();

        FOStoreDatabase fodb = con.getDatabase();

        if (logger.isDebugEnabled()) {
            logger.debug("DeleteHandler.hR: deleting " + oid + // NOI18N
                           ", optimistic=" + optimistic); // NOI18N
        }

        if (logger.isTraceEnabled()) {
            Block block = (Block)fodb.get(oid);
            block.dump();
        }

        // Remove instance from database and from its Extent
        boolean found = fodb.remove(oid);

        if (found) {
            DBInfo dbInfo = fodb.getDBInfo();
            OID extentOID = dbInfo.getExtentOID(oid.getCLID());
            DBExtent dbExtent = (DBExtent)fodb.get(extentOID);
            dbExtent.remove(oid);
            con.addExtent(dbExtent);
            reply.setStatus(Status.OK);
        } else {
            // Instance to be deleted not in database.  If Tx is optimistic,
            // it's a consistency failure; if datastore it's a bug.
            if (optimistic) {
                // XXX when delete verify is added, write the oid there also
                oid.write(reply);
                reply.setStatus(
                    Status.OPTIMISTIC,
                    msg.msg("EXC_DeleteVerifyFailed", oid)); // NOI18N
            } else {

                // Should not happen: datastore tx and instance not in store
                if (logger.isDebugEnabled()) {
                    logger.debug(
                        "DeleteHandler.hR: " + oid + " not in db"); // NOI18N
                }
                reply.setStatus(
                    Status.FATAL,
                    msg.msg("ERR_DatastoreFailed", oid)); // NOI18N
            }
        }

        return null;
    }
}
