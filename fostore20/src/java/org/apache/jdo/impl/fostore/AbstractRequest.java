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
import java.util.HashMap;

import javax.jdo.PersistenceManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.util.I18NHelper;

/**
* Base implementation for all Request subtypes.  Provides RequestId, and
* representation of Request types for all subclasses.  Ergo, when you add a
* Request type you must add it's request type representation here.
*
* @author Dave Bristor
*/
abstract class AbstractRequest implements Request {
    //
    // Several members are accessible from subclasses
    //

    /**
    * The state manager which is the subject of this request.
    */
    // Please treat this as final after constructors have run.
    protected /* final */ StateManagerInternal sm;
    
    /**
    * PersistenceManagerFactory via which request is being done.
    */
    protected final FOStorePMF pmf;

    /**
    * Stream to which request writes itself.
    */
    protected final FOStoreOutput out;

    /**
    * Class meta data of the object represented by the constructor's given
    * state manager
    */
    // Please treat this as final after constructors (including those of
    // subclasses, which might set this) have run.
    protected /* final */ JDOClass jdoClass;

    /**
     * uid corresponding to the same java.lang.Class that initializes
     * jdoClass.
     */
    // Please treat this as final after constructors (including those of
    // subclasses, which might set this) have run.
    protected /* final */ FOStoreSchemaUID fsuid;

    protected static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N
    
    //
    // Requests are written into the FOStoreOutput, and have this
    // format:
    //
    // * RequestId: Per-PMF, one per request.
    // * RequestType: See above table requestTypes.
    // * Length: Length in bytes of the RequestType-specific data
    // * RequestType-specific data, Length bytes long.
    //
    // The methods in this class provide the means of writing the RequestId
    // and RequestType (via beginRequest()) and the Length (via
    // endRequest()).  All AbstractRequest subclasses should invoke these
    // methods at the beginning and end of their operations which create the
    // actual RequestType-specific data.
    //
    
    // Position in the output at which we will write the length of the
    // request.  Set when we beginRequest().  Referenced when we
    // endRequest().
    private int lengthPos = 0;

    // Written at lengthPos to advance the output over the length field.
    protected static final int LENGTH_COOKIE = 0x10badbad;

    // Identifies this request uniquely within a PMF.
    private final RequestId requestId;

    // Identifies the type of the request.  Please treat as 'final' after
    // beginRequest.
    private RequestType requestType;

    // When true, indicates that the request has written all its information
    // to the FOStoreOutput.  See endRequest().
    private boolean sealed = false;

    protected AbstractRequest(StateManagerInternal sm, Message m,
                              FOStorePMF pmf) {

        this(m, pmf);

        this.sm = sm;
        FOStoreModel model = pmf.getModel();
        if (sm != null) {
            Class cls = sm.getPCClass();
            this.fsuid = FOStoreSchemaUID.lookup(cls, model);
            this.jdoClass = model.getJDOClass(cls);
        }
    }

    protected AbstractRequest(Message m, FOStorePMF pmf) {
        this.out = m.getOutput();
        this.pmf = pmf;
        
        this.requestId  = RequestId.allocate(pmf);
        m.putRequest(requestId, this);
    }

    //
    // NOTE: when initialized by the second constructor, the sm and jdoClass
    // fields are not filled in.  Therefore, methods in this class must *not*
    // assume they have valid values!
    //
    
    protected RequestId getId() {
        return requestId;
    }

    /**
    * @see Request#doRequest
    */
    public final void doRequest() throws IOException {
        // We must be the only one writing to the output to ensure we
        // write a valid request
        synchronized (out) {
            beginRequest();
            doRequestBody();
            endRequest();
        }
    }

    /**
    * Writes the header of the request.  The header is always:<p>
    * request id<br>
    * request type<br>
    * length<br>
    *
    * No, we don't really know the length yet.  We write out any old
    * integer, and then later (in endRequest) come back and write in the
    * real length.
    */
    // Final to enforce the above rules
    private final void beginRequest() throws IOException {
        if (lengthPos != 0) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "beginRequest", // NOI18N
                msg.msg("ERR_RequestAlreadyBegun")); // NOI18N
        }
        requestId.write(out);
        requestType = RequestType.get(this.getClass());
        if (null == requestType) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "beginRequest", // NOI18N
                msg.msg("ERR_NoRequestType", this.getClass().getName())); // NOI18N
        }
        requestType.write(out);
        lengthPos = out.getPos();
        out.writeInt(LENGTH_COOKIE);
    }

    /**
    * Subclasses must implement in this method the actual writing of their
    * Request type-specific data.
    */
    protected abstract void doRequestBody() throws IOException;

    /**
    * Write the length.  Remember, in beginRequest we only wrote a
    * placeholder.
    */
    // Final to enforce the rules noted in beginRequest().
    private final void endRequest() throws IOException {
        if (sealed) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "endRequest", // NOI18N
                msg.msg("ERR_RequestAlreadyEnded")); // NOI18N
        }
        int currentPos = out.getPos();
        // Subtract off an extra 4, which is the size of the length itself.
        int length = currentPos - lengthPos - 4;
        out.setPos(lengthPos);
        out.writeInt(length);
        out.setPos(currentPos);
        sealed = true;

        if (logger.isTraceEnabled()) {
            logger.trace("Request " + requestId + "/" + requestType); // NOI18N
            Tester.dump("REQ", out, lengthPos, length); // NOI18N
        }
    }
    
    /** Get the StateManager associated with this request, null if none.
     *@return the StateManager.
     */
    public StateManagerInternal getStateManager() {
        return sm;
    }
    
}
