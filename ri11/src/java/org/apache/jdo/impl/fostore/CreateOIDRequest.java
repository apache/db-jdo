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

import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.state.StateManagerInternal;


/**
 * Causes a datastore OID to be associated with a provisional OID.
 *
 * @author Dave Bristor
 */
//
// This is client-side code.  It does not need to live in the server.
//
class CreateOIDRequest extends AbstractRequest {
    private final OID oid;
    private final PersistenceManagerInternal pm;

    CreateOIDRequest(StateManagerInternal sm, Message m, FOStorePMF pmf,
                     OID oid, PersistenceManagerInternal pm) {
        super(sm, m, pmf);
        
        this.oid = oid;
        this.pm = pm;

        if (logger.isDebugEnabled()) {
            logger.debug("CreateOIDRequest: " + oid); // NOI18N
        }
    }

    //
    // Methods from AbstractRequest
    //

    protected void doRequestBody() throws IOException {
        oid.write(out); 
    }

    //
    // Methods from Request
    //

    public void handleReply(Status status, DataInput in, int length)
    throws IOException {

        OID realOID = OID.read(in);
        oid.replaceProvisionalOIDWithReal(realOID, pmf, sm);

        if (logger.isDebugEnabled()) {
            logger.debug(
                "CreateOIDRequest.hR: " + oid + " -> " + realOID); // NOI18N
        }

    }
}
