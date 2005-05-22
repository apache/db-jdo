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

import javax.jdo.JDOFatalUserException;

import org.apache.jdo.util.I18NHelper;


/**
* Represents a simple id associated with a request.  This is used, for
* example, to pair up requests and replies: when a Request is written to the
* store, it is put in a map keyed by RequestId, and when replies are received
* from the store, they contain a RequestId; this is used to find the
* corresponding Request which then handles the reply data.
*
* @author Dave Bristor
*/
class RequestId {
    // RequestId's are per-pmf; this maps from FOStorePMF to IdFactory.
    private static HashMap idFactoryTable = new HashMap();

    // The representation of a RequestId.  We use a Long so that we can
    // support them as keys in Maps.  Note also the need for hashCode() and
    // equals() for the same reason.
    private final Long id;

    // When the server is having catastrophic problems, it should use this as
    // a request id in the reply it creates.
    public static final RequestId FAILURE = new RequestId(-1L);

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    private RequestId(long id) {
        this.id = new Long(id);
    }

    /**
    * Create a new RequestId by reading it's representation from the input.
    * @param in DataInput from which representation is read.
    */
    RequestId(DataInput in) throws IOException {
        this.id = new Long(in.readLong());
    }

    /**
    * Writes its representation to the output stream.
    * @param out DataOutput stream to which RequestId's representation is
    * written.
    */
    void write(DataOutput out) throws IOException {
        out.writeLong(id.longValue());
    }

    // This is how you allocate yourself a RequestId.  Allocates an id from
    // the factory corresponding to the given PMF.
    //
    synchronized static RequestId allocate(FOStorePMF pmf) {

        // IdFactory is in charge of allocating the instances of the
        // representation of a RequestId.
        class IdFactory {
            // Using this might make debug output easier to read:
            //private long lastAllocated = 0;
            private long lastAllocated = Long.MIN_VALUE;
            
            long allocate() {
                if (lastAllocated == Long.MAX_VALUE) {
                    throw new FOStoreFatalInternalException(
                        RequestId.class, "allocate", // NOI18N
                        msg.msg("ERR_Overflow", new Long(Long.MAX_VALUE))); // NOI18N
                }
                return lastAllocated++;
            }
        }
        
        IdFactory f = (IdFactory)idFactoryTable.get(pmf);
        if (null == f) {
            f = new IdFactory();
            idFactoryTable.put(pmf, f);
        }
        return new RequestId(f.allocate());
    }

    /**
    * Returns true if the other id is equal to this one.
    * @param other RequestId to which this one is compared.
    */
    public boolean equals(Object other) {
        return id.longValue() == ((RequestId)other).id.longValue();
    }

    public int hashCode() {
        return id.hashCode();
    }

    public String toString() {
        return "ReqId=" + id.toString(); // NOI18N
    }
}
