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
import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.jdo.JDOHelper;

import org.apache.jdo.state.StateManagerInternal;


/**
 * Represents a requests to remove a persistent object in the store.
 *
 * @author Dave Bristor
 */
//
// This is client-side code.  It does not need to live in the server.
//
class DeleteRequest extends AbstractRequest {
    /** OID of the instance being deleted. */
    private final OID oid;

    /** Optimistic/datastore state of current transaction. */
    private final boolean optimistic;

    DeleteRequest(StateManagerInternal sm, Message m, FOStorePMF pmf) {
        super(sm, m, pmf);
        this.oid = (OID)sm.getInternalObjectId();
        PersistenceManager pm = sm.getPersistenceManager(); 
        Transaction tx = pm.currentTransaction();
        this.optimistic = tx.getOptimistic();
    }

    protected void doRequestBody() throws IOException {
        //
        // The format of this request is (aside from the request header):
        //
        // oid: OID
        // optimistic: boolean
        // numFields: int
        // fieldValue: Object...
        //

        // XXX Need in optimistic Tx, verify w/ dirtyFields? ; see StoreManger#delete
        if (logger.isDebugEnabled()) {
            logger.debug("DeleteRequest.dRB: " + oid); // NOI18N
        }
        oid.write(out);
        out.writeBoolean(optimistic);
    }

    //
    // Methods from Request
    //

    /**
     * @see Request#handleReply
     */
    public void handleReply(Status status, DataInput in, int length)
        throws IOException {

        //
        // The format of this reply is
        //
        //  empty, that's right, no data needed.
        //
        if (logger.isDebugEnabled()) logger.debug("DeleteRequest.hR"); // NOI18N
    }
}
