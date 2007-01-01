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

import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.util.I18NHelper;


/**
 * Represents a request to get the java.lang.Class corresponding to a CLID.
 *
 * @author Dave Bristor
 */
//
// This is client-side code.  It does not need to live in the server.
//
class GetClassRequest extends AbstractRequest {
    /** The CLID of the class sought by this request. */
    private final CLID clid;
    
    /** PersistenceManagerInternal used to load the class. */
    private final PersistenceManagerInternal pm;

    /** The Class sought by this request. */
    private Class classForCLID;

    GetClassRequest(CLID clid, Message m,
                    FOStorePMF pmf, PersistenceManagerInternal pm) {
        super(m, pmf);
        this.clid = clid;
        this.classForCLID = null;
        this.pm = pm;
    }

    protected void doRequestBody() throws IOException {
        clid.write(out);
        if (logger.isDebugEnabled()) {
            logger.debug("GetClassRequest.dRB: " + clid); // NOI18N
        }
    }

     public void handleReply(Status status, DataInput in, int length)
        throws IOException {

        String className = in.readUTF();
        if (logger.isDebugEnabled()) {
            logger.debug("GetClassRequest.hR: className=" + className // NOI18N
                           + " status=" + status); // NOI18N
        }
        // className is valid if not empty and request succeeded.
        if (null != className
            && className.length() > 0
            && Status.OK.equals(status)) {
            
            try {
                classForCLID = pm.loadClass(className, null);
                // Bind in the metadata
                FOStoreModel model = pmf.getModel();
                model.put(classForCLID, clid);

                if (logger.isDebugEnabled()) {
                    logger.debug("GetClassRequest.hR: classForCLID=" + classForCLID); // NOI18N
                }
            } catch (ClassNotFoundException ex) {
               throw new JDOUserException(
                   msg.msg("EXC_NoClassForCLID", clid, ex)); // NOI18N
            }
        }
    }

    Class getClassForCLID() {
        return classForCLID;
    }
}
