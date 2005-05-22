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
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

/**
* Represents a kind of Request.  Used to identify request types between
* client and store; a smaller representation than a Request's
* java.lang.Class.
*
* @author Dave Bristor
*/
class RequestType {
    // We use an Integer so that we can support the requestHandlers HashTable. 
    private final Integer id;

    private static HashMap requestTypes = new HashMap();
    private static HashMap requestHandlers = new HashMap();

    private static HashMap debug = new HashMap(); // for debugging only

    private static int nextType = 0;
    static {
        RequestType rt = null;

        rt = new RequestType(nextType++, ActivateClassRequest.class, 
                             ActivateClassHandler.factory);

        rt = new RequestType(nextType++, InsertRequest.class, 
                             InsertHandler.factory);

        rt = new RequestType(nextType++, UpdateRequest.class, 
                             UpdateHandler.factory);

        rt = new RequestType(nextType++, VerifyRequest.class, 
                             VerifyHandler.factory);

        rt = new RequestType(nextType++, DeleteRequest.class, 
                             DeleteHandler.factory);

        rt = new RequestType(nextType++, FetchRequest.class, 
                             FetchHandler.factory);

        rt = new RequestType(nextType++, GetExtentRequest.class, 
                             GetExtentHandler.factory);

        rt = new RequestType(nextType++, GetInstancesRequest.class, 
                             GetInstancesHandler.factory);

        rt = new RequestType(nextType++, GetClassRequest.class, 
                             GetClassHandler.factory);
        
        rt = new RequestType(nextType++, CommitRequest.class, 
                             CommitHandler.factory);

        rt = new RequestType(nextType++, RollbackRequest.class, 
                             RollbackHandler.factory);

        rt = new RequestType(nextType++, CreateOIDRequest.class, 
                             CreateOIDHandler.factory);
        
        rt = new RequestType(nextType++, LoginRequest.class,
                            LoginHandler.factory);
        
        rt = new RequestType(nextType++, BeginTxRequest.class,
                            BeginTxHandler.factory);

        rt = new RequestType(nextType++, DumpRequest.class,
                            DumpHandler.factory);
    }

    RequestType(int id, Class requestClass,
                RequestHandler.HandlerFactory factory) {
        this.id = new Integer(id);
        if (null != requestTypes.put(requestClass, this)) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "constructor", // NOI18N
                "duplicate requestClass entry"); // NOI18N
        }

        if (null != requestHandlers.put(this, factory)) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "constructor", // NOI18N
                "duplicate factory entry"); // NOI18N
        }

        debug.put(this.id, requestClass);
    }

    RequestType(DataInput in) throws IOException {
        this.id = new Integer(in.readInt());
    }

    void write(DataOutput out) throws IOException {
        out.writeInt(id.intValue());
    }

    /**
    * @return The RequestType corresponding to the given class.
    */
    static RequestType get(Class cls) {
        return (RequestType)requestTypes.get(cls);
    }
    
    public int hashCode() {
        return id.hashCode();
    }
    
    public boolean equals (Object other) {
        return this.id.intValue() == ((RequestType)other).id.intValue();
    }

    /**
    * @return The factory that can create a RequestHandler corresponding to
    * our request type.
    */
    RequestHandler.HandlerFactory getHandlerFactory() {
       return (RequestHandler.HandlerFactory)requestHandlers.get(this);
    }

    public String toString() {
        String name = ((Class)(debug.get(id))).getName();
        name = name.substring(name.lastIndexOf('.')+1);
        return "ReqType=" + id.toString() + " " + // NOI18N
            "(" + name + ")"; // NOI18N
    }
}
    
