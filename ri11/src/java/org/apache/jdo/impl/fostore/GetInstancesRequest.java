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
import java.util.ArrayList;

import org.apache.jdo.pm.PersistenceManagerInternal;


/**
 * Represents a request to fetch a batch of instances.
 *
 * @author Dave Bristor
 */
//
// This is client-side code.  It does not need to live in the server.
//
class GetInstancesRequest extends AbstractRequest {
    /** List of oids from which we get instances. */
    private final ArrayList oids;

    /** Starting position in oids ArrayList. */
    private final int start;

    /** Number of instances to get. */
    private final int numInstances;

    /** The PersistenceManagerInternal that is making this request. */
    private final PersistenceManagerInternal pm;

    /** Candidate class for which instances are being obtained. */
    private final Class cls;

    /** ArrayList returned to user. */
    private ArrayList instances;

    GetInstancesRequest(ArrayList oids, int start, int numInstances,
        Message m, PersistenceManagerInternal pm, Class cls) {

        super(m, (FOStorePMF)pm.getPersistenceManagerFactory());
        this.oids = oids;
        this.start = start;
        this.numInstances = numInstances;
        this.pm = pm;
        this.cls = cls;
        
        if (logger.isDebugEnabled()) {
            logger.debug("GetInstancesRequest: " + // NOI18N
                           "start=" + start + // NOI18N
                           ", numInstances=" + numInstances); // NOI18N
        }
    }

    ArrayList getInstances() {
        return instances;
    }

    //
    // Methods from AbstractRequest
    //

    protected void doRequestBody() throws IOException {
        //
        // The format of this request is (aside from the request header):
        //
        // int: numOIDs
        // oid: OID...
        //

        int numRequested = numInstances;
        if (start + numInstances > oids.size()) {
            numRequested = oids.size() - start;
        }

        out.writeInt(numRequested);

        if (logger.isDebugEnabled()) {
            logger.debug(
                "GetInstancesRequest.dRB/0: max=" + numRequested); // NOI18N
        }

        int end = start + numRequested;
        for (int i = start; i < end; i++) {
            OID oid = (OID)oids.get(i);
            oid.write(out);
        }
    }

    
    //
    // Methods from Request
    //

    public void handleReply(Status status, DataInput in, int length)
        throws IOException {

        //
        // The format of this reply is
        //
        // int: number of instances
        // that many instances
        //

        int numReturned = in.readInt();
        if (logger.isDebugEnabled()) {
            logger.debug("GetInstancesRequest.hR: numReturned=" + // NOI18N
                           numReturned);
        }
        instances = GetExtentRequest.readInstances(
            in, numReturned, pmf.getModel(), pm, cls);
    }
}
