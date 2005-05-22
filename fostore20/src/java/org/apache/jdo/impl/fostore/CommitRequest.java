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

/**
* Represents a request to cause previous insert, update, and so on
* operations since the previous Commit or Rollback request to commit to
* the datastore.
*
* @author Dave Bristor
*/
//
// This is client-side code.  It does not need to live in the server.
//
class CommitRequest extends AbstractRequest {
    /** Message in which this request is written
     */
    private final Message message;

    /** If true, then after committing the database should be released.
     */
    private boolean okToReleaseDatabase = true;
    
    CommitRequest(Message m, FOStorePMF pmf) {
        super(m, pmf);
        this.message = m;
    }

    void setOkToReleaseDatabase(boolean ok) {
        okToReleaseDatabase = ok;
    }

    //
    // Methods from AbstractRequest
    //

    /**
     * Provides the information required to fulfill a CommitRequest.
     * The format of this request is (aside from the request header):
     * 
     * boolean: okToReleaseDatabase
     */
    protected void doRequestBody() throws IOException {
        if (logger.isDebugEnabled()) 
            logger.debug("CommitRequest.dRB: ok=" + // NOI18N
                         okToReleaseDatabase);

        out.writeBoolean(okToReleaseDatabase);
    }

    //
    // Methods from Request
    //

    /**
     * Handles reply data from a CommitReply.
     */
    public void handleReply(Status status, DataInput in, int length)
        throws IOException {

        if (logger.isDebugEnabled()) {
            logger.debug("CommitRequest.hR " + status); // NOI18N
        }
    }
}
