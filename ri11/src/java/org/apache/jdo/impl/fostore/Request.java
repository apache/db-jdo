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

import org.apache.jdo.state.StateManagerInternal;


/**
 * Represents the ability to send information to the actual file/object store.
 *
 * @author Dave Bristor
 */
interface Request {
    /** Does whatever it is that the kind of request does in actually making a
     * request of the store.
     * @throws IOException in case of errors with the stream.
     */
    public void doRequest() throws IOException;
    
    /** Processes the results of the effect of the request in the store.  To be
     * invoked after the store has processed the request, and has returned
     * information about that request, such as its status and any accompanying
     * data.
     * @param in the input stream.
     * @param length the length of data in the stream.
     * @param status Indication as to the success, failure, etc. of the
     * request as handled by the store.
     * @throws IOException if any problems reading the stream.
     */
    public void handleReply(Status status, DataInput in, int length)
        throws IOException;
    
    /** Get the StateManager associated with this request, null if none.
     * @return the StateManager.
     */
    public StateManagerInternal getStateManager();
}
