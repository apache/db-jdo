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

import javax.jdo.JDOUserException;
import javax.jdo.JDOObjectNotFoundException;

import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.state.StateManagerInternal;


/**
 * Represents a request to read the values of one or more fields for a
 * persistent object from the store.
 *
 *
 * @author Dave Bristor
 */
//
// This is client-side code.  It does not need to live in the server.
//
class FetchRequest extends AbstractRequest implements FieldRequest {
    private final OID oid;
    private final Object owner;
    private final PersistenceManagerInternal pm;

    FetchRequest(StateManagerInternal sm, Message m, FOStorePMF pmf) {
        super(sm, m, pmf);
        
        this.oid = (OID)sm.getInternalObjectId();
        this.owner = sm; // Owner will be set to StateManagerInternal
        this.pm =  (PersistenceManagerInternal)sm.getPersistenceManager();

        if (logger.isDebugEnabled()) {
            logger.debug("FetchRequest: " + oid); // NOI18N
        }
    }

    //
    // Methods from AbstractRequest
    //

    protected void doRequestBody() throws IOException {
        //
        // The format of this request is (aside from the request header):
        //
        // oid: OID
        // numFields: int
        // fieldNum: int...
        //

        out.writeBoolean(pm.getStoreManager().hasActualPCClass(oid));
        oid.write(out);

        // XXX TBD Make use of the fieldNums (right now we fetch all
        // fields).  This will entail writing more output as per above.
    }

    
    //
    // Methods from Request
    //

    public void handleReply(Status status, DataInput in, int length)
    throws IOException {

        //
        // The format of this reply is
        //
        // className: String
        // fieldsLength: int
        // fieldValue: Object...
        // numPairs: int
        // CLID, classname pairs...
        //
        // The value types are as per the metadata, and are provided in the
        // same order as that in which they were requested.
        //

        byte data[] = new byte[length];
        if (logger.isDebugEnabled()) {
            logger.debug("FR.hR: reading " + length + " bytes"); // NOI18N
        }
        in.readFully(data);

        if (status.equals(Status.WARN)) {
            String message = in.readUTF();
            throw new JDOObjectNotFoundException(message);
        }

        ClassLoader loader = sm.getPCClass().getClassLoader();
        
        FieldFetcher ff =
            new FieldFetcher(new FOStoreInput(data, 0, length),
                             pmf.getModel(),
                             pm,
                             loader);
        ff.fetch(sm, oid);
    }

    
    //
    // Implement FieldRequest
    //
    
    /**
    * Determines which fields (at least) will be read from the store.  We say
    * 'at least' because while we guarantee that the specified fields will be
    * read, other fields may be read; this is implementation dependent.
    * @param fieldNums Numbers of the fields which are to be read from the
    * store.
    */
    public void setFieldNums(int fieldNums[]) { }

    /**
      * Adds to the set of fields that are to be manipulated.
      * @param fieldNum Number of the field to be manipulated.
      */
    public void addFieldNum(int fieldNum) { }
}
