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
 * Represents a request to verify that in-memory data is the same as that in
 * the database.
 *
 * @author Dave Bristor
 */
//
// This is client-side code.  It does not need to live in the server.
//

//
// XXX About VerifyRequest's current implementation
// For now, VerifyRequest  ignores the given array which indicates which
// fields should be checked.
//
class VerifyRequest extends InsertRequest {
    /** If true, verify values of object, otherwise verify only existence (and
     * ignore remaining parameters). */
    private final boolean verifyFields;

    /** Fields to verify in database. */
    private final BitSet fieldsToVerify;

    /** Result of executing request. */
    private boolean verified;

    VerifyRequest(StateManagerInternal sm, Message m, FOStorePMF pmf,
                  boolean verifyFields, BitSet fieldsToVerify) {

        super(sm, m, pmf);
        this.verifyFields = verifyFields;
        this.fieldsToVerify = fieldsToVerify;
    }

    //
    // Methods from AbstractRequest
    //

    /**
     * Provides the information necessary for a VerifyRequest.
     * The format of this request is (aside from the request header):
     * <pre>
     * oid: OID
     * boolean: verifyFields
     * data block (optional; only if verifyFields is true)
     * </pre>
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
            logger.debug("VerifyRequest.dRB: begin, "+ oid + // NOI18N
                           ", verifyFields=" + verifyFields); // NOI18N
        }

        oid.write(out);
        out.writeBoolean(verifyFields);

        if (verifyFields) {
            // XXX For now, verify the values of all the fields.
            int fieldNumbers[] = jdoClass.getPersistentFieldNumbers();
            writeBlock(fieldNumbers, true);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("VerifyRequest.dRB: end"); // NOI18N
        }
    }

    //
    // Methods from Request
    //

    /**
     * Handles reply data from a VerifyReply.
     * The format of this reply is
     * <pre>
     * boolean: true => object exists in database, and (if verifyFields is
     * true) values match those in request.
     * </pre>
     */
    public void handleReply(Status status, DataInput in, int length)
        throws IOException {

        verified = in.readBoolean();

        if (logger.isDebugEnabled()) {
            logger.debug("VerifyRequest.hR: " + getOID() + // NOI18N
                ", verified=" + verified + ", status=" + status); // NOI18N
        }
    }

    boolean getVerified() {
        return verified;
    }
}
