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
* Process requests to update instances in the datastore.
*
* @author Dave Bristor
*/
//
// This is server-side code.  It does not need to live in the client.
//
class UpdateHandler extends InsertHandler {
    /** I18N Support */
    // Note that in this file we're using keys with the "EXC" prefix, but
    // outside the context of throwing an exception: this is because the
    // client side will take these messages and put them into exceptions.  See
    // ReplyHandler.handleReplies.
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Construct a new handler for processing updates.
     * @param reply the Reply.
     * @param length the length of data in the stream.
     * @param con the connection to use.
     */    
    protected UpdateHandler(Reply reply, int length,
                          FOStoreServerConnection con) {

        super(reply, length, con);
    }

    static final HandlerFactory factory =
        new HandlerFactory() {
            public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new UpdateHandler(reply, length, con);
            }};

    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        RequestFinisher rc = null;
        FOStoreInput in = con.getInputFromClient();

        // True if verify() returns true or datastore transaction.
        boolean okToUpdate = true;

        // True if verify throws an exception.  In that case, okToUpdate
        // *will* be false.
        boolean verifyException = true;

        if (logger.isDebugEnabled()) {
            logger.debug("UpdateHandler.hR: begin"); // NOI18N
        }

        OID oid = null;
        try {
            boolean optimistic = in.readBoolean();
            if (optimistic) {
                oid = OID.read(in);
                Block block = readBlock(in);
                okToUpdate = verify(oid, true, block);
            }
            verifyException = false;
        } catch(DoesNotExistException ex) {
            okToUpdate = false;
            reply.writeOID(oid);
            reply.setStatus(
                Status.OPTIMISTIC, 
                msg.msg("EXC_OptimisticDoesNotExist", oid)); // NOI18N
        } catch(Exception ex) {
            okToUpdate = false;
            reply.writeOID(oid);
            reply.setStatus(
                Status.ERROR,
                msg.msg("ERR_VerifyException", oid), ex); // NOI18N
        }

        int length = in.readInt();
        if (okToUpdate) {
            // datastore image exists and matches before image
            rc = super.handleRequest();
        } else {
            in.skipBytes(length);
            if ( ! verifyException) {
                reply.writeOID(oid);
                // mismatch between before image and datastore image
                // status has not yet been set by exception handling
                reply.setStatus(
                    Status.OPTIMISTIC,
                    msg.msg("EXC_OptimisticVerifyFailed", oid)); // NOI18N
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("UpdateHandler.hR: end"); // NOI18N
        }

        return rc;
    }

    /** Verify before image versus database state of an object.
     * @return true if verify succeeds as per <code>verifyFields</code>.
     * @param oid OID of object to be verified
     * @param verifyFields If true, verify that values of the data
     * corresponding to <code>oid</code> in the database match those in the
     * given block; if false verify that object corresponding to
     * <code>oid</code> exists.
     * @param block Block of data for value verification; may be null if
     * <code>verifyFields</code> is false.
     * @throws IOException if stream errors.
     * @throws FOStoreDatabaseException if any errors in the database.
     * @throws DoesNotExistException if the object to be updated does not exist.
     */
    protected boolean verify(OID oid, boolean verifyFields, Block block)
        throws IOException, FOStoreDatabaseException, DoesNotExistException {

        if (logger.isDebugEnabled()) {
            logger.debug("UpdateHandler.verify for: " + oid + // NOI18N
                           ", verifyFields=" + verifyFields); // NOI18N
        }

        boolean rc = false;
        
        FOStoreDatabase fodb = con.getDatabase();
        Block current = (Block)fodb.getIfExists(oid);

        rc = (null != current);

        if (verifyFields) {
            byte blockData[] = block.getData();

            if (logger.isTraceEnabled()) {
                Tester.dump("BLK", blockData, blockData.length); // NOI18N
            }

            // Reset to false so that if we get an error, client won't try to
            // set status.
            rc = false; 

            if (null == current) {
                throw new DoesNotExistException();
            } else {
                byte currentData[] = current.getData();
                if (logger.isTraceEnabled()) {
                    logger.debug(
                        "UpdateHandler.verify current data:"); // NOI18N
                    Tester.dump("CUR", currentData, currentData.length); // NOI18N
                }

                // XXX TBD Compare and report based on field values
                rc = Arrays.equals(blockData, currentData);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("UpdateHandler.verify returns: " + rc); // NOI18N
        }

        return rc;
    }

    /**
     * Thrown by {@link UpdateHandler#verify} if an object which should exist
     * in the database, does not.
     */
    private class DoesNotExistException extends Exception { }

    /** Replace a block in the database.
     * @see InsertHandler#updateDB
     * @param provOID provisional OID.
     * @param realOID OID to use as key in the database.
     * @param block Block to be inserted in database.
     * @param db Database into which block is replaced.
     * @throws IOException for stream errors.
     * @throws FOStoreDatabaseException for any database error except for object not found.
     */
    protected void updateDB(OID realOID, OID provOID, Block block,
                            FOStoreDatabase db)
        throws IOException, FOStoreDatabaseException {

        if (logger.isDebugEnabled()) {
            logger.debug("UpdateHandler.updateDb: " + realOID); // NOI18N
        }
        db.replace(realOID, block);
    }
}
