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

import org.apache.jdo.util.I18NHelper;


/**
* Represents a quick summary of the result of processing a request.
*
* @author Dave Bristor
*/
class Status {
    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Request is being processed.  Should be used only by Reply's
     * constructor, to write a value which will be overwritten once the
     * requests's actual status is known.  */
    private static final Status UNKNOWN = new Status(-1);

    /** Request was successfully processed. */
    static final Status OK       = new Status(0);

    /** Request succeeded, but with warnings. */
    static final Status WARN     = new Status(1);

    /** Request failed. */
    static final Status ERROR    = new Status(2);

    /** Request failed fatally in server. */
    static final Status FATAL    = new Status(3);

    /** Database rolled back due to user request. */
    static final Status ROLLBACK = new Status(4);

    /** Could not login to database. */
    static final Status LOGIN = new Status(5);

    /** Optimistic failure. Verify, delete, or duplicate oid. */
    static final Status OPTIMISTIC = new Status(6);
    
    /** Minimum status value. */
    private static final int MIN_STATUS = -1;

    /** Maximum status value. */
    private static final int MAX_STATUS = 6;

    /** Status value. */
    private final int status;

    /**
    * Used to create the public static final elements.
    */
    private Status(int status) {
        this.status = status;
    }

    /**
     * Used to initialize a reply
     */
    static void initialize(Reply reply) throws IOException {
        UNKNOWN.write(reply);
    }

    /**
    * Used to 'reconstitute' a Status value from a DataInput.
    */
    Status(DataInput in) throws IOException {
        this.status = in.readInt();
        if (status < MIN_STATUS || status > MAX_STATUS){
            throw new FOStoreFatalInternalException(
                this.getClass(), "constructor(DataInput)", // NOI18N
                msg.msg("ERR_OutOfRange", new Integer(status), // NOI18N
                        new Integer(MIN_STATUS), new Integer(MAX_STATUS)));
        }
    }

    /**
    * Used to externalize a Status value.
    */
    void write(DataOutput out) throws IOException {
        out.writeInt(status);
    }

    /**
    * Returns length of a Status value's representation in bytes.
    */
    int getLength() {
        return 4;
    }

    /**
    * Compares this to another Status.
    */
    public boolean equals(Object other) {
        return this.status == ((Status)other).status;
    }

    public String toString() {
        String rc = msg.msg("MSG_Invalid"); // NOI18N
        switch (status) {
          case -1: rc = msg.msg("MSG_Unknown"); break; // NOI18N
          case 0: rc = msg.msg("MSG_Ok"); break; // NOI18N
          case 1: rc = msg.msg("MSG_Warn"); break; // NOI18N
          case 2: rc = msg.msg("MSG_Error"); break; // NOI18N
          case 3: rc = msg.msg("MSG_Fatal"); break; // NOI18N
          case 4: rc = msg.msg("MSG_Rollback"); break; // NOI18N
          case 5: rc = msg.msg("MSG_Login"); break; // NOI18N
          case 6: rc = msg.msg("MSG_Optimistic"); break; // NOI18N
          default: break;
        }
        return rc;
    }
}
    
