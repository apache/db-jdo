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
import java.util.Iterator;

import javax.jdo.JDOUserException;

import org.apache.jdo.util.I18NHelper;


/**
* Process fetch requests.
*
* @author Dave Bristor
*/
//
// This is server-side code.  It does not need to live in the client.
//
class FetchHandler extends RequestHandler {

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    private FetchHandler(Reply reply, int length,
                         FOStoreServerConnection con) {

        super(reply, length, con);
    }
    
    public static final HandlerFactory factory =
        new HandlerFactory() {
                public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new FetchHandler(reply, length, con);
            }};

    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        // XXX TBD Handle numFields and fieldNums.  For now, we're expecting
        // only an OID, and we return all field values.

        DataInput in = con.getInputFromClient();
        boolean knownclass = in.readBoolean();
        OID oid = OID.read(in);
        CLID clid = oid.getCLID();

        FOStoreDatabase fodb = con.getDatabase();
        Block block = null;
        try {
            block = (Block)fodb.getIfExists(oid);
            if (null == block) {
                if (logger.isDebugEnabled()) {
                    logger.debug("FR.reply: nonexistent: " + oid); // NOI18N
                }

                if (knownclass == false) {
                    long uid = oid.getUID();
                    OID tempOid = null;
                    if (clid.isProvisional()) {
                        // Retry with the known class id.
                        if (logger.isDebugEnabled()) {
                            logger.debug("FH.hR: replace provisional clid..."); // NOI18N
                        }
                        clid = fodb.getRealCLIDFromProvisional(clid);
                        tempOid = OID.create(clid, uid);
                        block = (Block)fodb.getIfExists(tempOid);
                    }
                    if (null == block) {
                        // Still not found. Now test subclasses.

                        if (logger.isDebugEnabled()) {
                            logger.debug("FH.hR: test subclassess..."); // NOI18N
                        }

                        OID ssOID = DBInfo.getSubclassSetOID(clid);
                        SubclassSet ss = (SubclassSet)fodb.getIfExists(ssOID);
                        if (logger.isDebugEnabled()) {
                           logger.debug("FH.hR: subclassOID: " + ssOID); // NOI18N
                        }
                        if (null != ss) {
                            if (logger.isDebugEnabled()) {
                                logger.debug("FH.hR: SubclassSet exists for : " + clid); // NOI18N
                            }
                            for (Iterator i = ss.iterator(); i.hasNext();) {
                                CLID subCLID = (CLID)i.next();
                                tempOid = OID.create(subCLID, uid);
                                if (logger.isDebugEnabled()) {
                                    logger.debug("FH.hR: looking for : " + tempOid); // NOI18N
                                }
                                block = (Block)fodb.getIfExists(tempOid);
                                if (block != null) {
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("FH.hR: exists: " + tempOid); // NOI18N
                                    }

                                    clid = subCLID;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (null == block) {
                reply.setStatus(
                    Status.WARN,
                    msg.msg("EXC_DoesNotExist", oid.toString())); // NOI18N
            } else {
                if (logger.isTraceEnabled()) {
                    logger.trace("FR.reply data:"); // NOI18N
                    block.dump();
                }
                reply.write(block.getData());
                reply.writeCLID(clid);
                reply.setStatus(Status.OK);
            }
        } catch (FOStoreDatabaseException ex) {
            reply.setStatus(Status.ERROR, ex);
        }

        return null;
    }
}
