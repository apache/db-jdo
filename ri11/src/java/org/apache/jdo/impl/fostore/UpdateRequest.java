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
import java.util.BitSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.apache.jdo.model.jdo.PersistenceModifier;
import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.util.I18NHelper;



/**
 * Represents a request to change one or more fields of a persistent object in
 * the store.
 *
 * @author Dave Bristor
 */
//
// This is client-side code.  It does not need to live in the server.
//
class UpdateRequest extends InsertRequest {
    /** Fields which are to be updated. */
    private final BitSet loadedFields;

    /** Fields which are to be verified in database, in case of
     * optimistic transaction. */
    private final BitSet dirtyFields;

    /** If true, update happens in context of an optimistic transaction. */
    private final boolean optimistic;

    UpdateRequest(StateManagerInternal sm, Message m, FOStorePMF pmf,
                  BitSet loadedFields, BitSet dirtyFields, boolean optimistic) {

        super(sm, m, pmf);
        this.loadedFields = loadedFields;
        this.dirtyFields = dirtyFields;
        this.optimistic = optimistic;
    }

    //
    // Methods from AbstractRequest
    //

    /**
     * Provides the information necessary to do an UpdateRequest.
     * The format of this request is (aside from the request header):
     * <pre>
     * optimistic: boolean
     * oid: OID
     * data block
     * length: int
     * InsertRequest's doRequestBody - written data
     * </pre>
     * The oid and data block are written only if optimistic is true.
     * @see AbstractRequest#doRequestBody
     */
    protected void doRequestBody() throws IOException {
        OID oid = (OID)sm.getInternalObjectId();
        if (oid.isProvisional()) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "doRequestBody", // NOI18N
                msg.msg("ERR_OidIsProvisional", oid)); // NOI18N
        }

        if (logger.isDebugEnabled()) {
            logger.debug("UpdateRequest.dRB: begin: " + optimistic); // NOI18N
        }

        out.writeBoolean(optimistic);

        if (optimistic) {
            oid.write(out);
            // XXX For now, verify the values of all the fields.
            writeBlock(jdoClass.getPersistentFieldNumbers(), true);
        }

        // Save space to write the length of the data written by
        // InsertRequest.  The reason is, that on the Handler side, if we're
        // running an optimistic transaction and the verify fails, we just
        // want to skip over InsertRequest's bytes.
        //
        int lengthPos = out.getPos();
        out.writeInt(LENGTH_COOKIE);
        int startPos = out.getPos();

        super.doRequestBody();

        int currentPos = out.getPos();
        int length = currentPos - startPos;
        if (logger.isDebugEnabled()) {
            logger.debug("UpdateRequest.dRB: length=" + length); // NOI18N
        }
        out.setPos(lengthPos);
        out.writeInt(length);
        out.setPos(currentPos);

        if (logger.isDebugEnabled()) {
            logger.debug("UpdateRequest.dRB: end"); // NOI18N
        }
    }

    //
    // Methods from Request
    //

    /**
     * Handles reply data from an UpdateReply.
     * The format of this reply is
     * <pre>
     * oid: OID
     * </pre>
     */
    public void handleReply(Status status, DataInput in, int length)
        throws IOException {

        OID replyOid = OID.read(in);

        if (logger.isDebugEnabled()) {
            logger.debug("UpdateRequest.hR: " + replyOid + // NOI18N
                ", " + status); // NOI18N
        }
    }
}
