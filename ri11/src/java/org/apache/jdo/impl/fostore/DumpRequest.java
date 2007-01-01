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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.JDOUserException;
import javax.jdo.PersistenceManager;

import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.state.StateManagerInternal;


/**
 * Represents a request to dump information from the store.
 *
 * @author Markus Fuchs
 */
//
// This is client-side code.  It does not need to live in the server.
//
class DumpRequest extends AbstractRequest {
    /** The DumpOption.
     */
    private final DumpOption option;

    /** The name of the class queried by this request.
     */
    private final String className;

    /** Returned dump information.
     */
    private String dump;

    DumpRequest(DumpOption option, String className, Message m,
                     FOStorePMF pmf) {

        super(m, pmf);
        this.option = option;
        this.className = className;
    }

    protected void doRequestBody() throws IOException {
        //
        // The format of this request is:
        //
        // int: option code
        // String: className
        //

        option.write(out);
        out.writeUTF((null != className) ? className : ""); // NOI18N
        if (logger.isDebugEnabled()) {
            logger.debug("DR.dRB: " + option // NOI18N
                           + " class=" + className); // NOI18N
        }
    }

     public void handleReply(Status status, DataInput in, int length) throws IOException {
         //
         // The format of the reply is:
         // int: number of objects (<= one)
         // String: information dump
         //
         // The status might be Status.WARN, in which case either
         // * The class to be queried wasn't found.
         // * The className parameter was null.

         int count = in.readInt();
         if (logger.isDebugEnabled()) {
             logger.debug("DR.hR/0: count=" + count); // NOI18N
         }

         StringBuffer results = new StringBuffer();
         for (int i = 0; i < count; i++) {
             results.append(in.readUTF());
         }
         dump = results.toString();
         
         if (logger.isDebugEnabled()) {
             logger.debug("DR.hR/1: dump=" + dump); // NOI18N
         }
    }

    String getDump() {
        return dump;
    }
}
