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
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.jdo.impl.model.java.runtime.RuntimeJavaModelFactory;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.state.StateManagerInternal;



/**
 * Represents a request to cause the description of a class in the client to
 * have a representation in the store.
 *
 * @author Dave Bristor
 */
//
// This is client-side code.  It does not need to live in the server.
//
class ActivateClassRequest extends AbstractRequest {
    // CLID of the class to be activated.
    private final CLID clid;

    /** RuntimeJavaModelFactory. */
    private static final RuntimeJavaModelFactory javaModelFactory =
        (RuntimeJavaModelFactory) AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return RuntimeJavaModelFactory.getInstance();
                }
            }
        );

    ActivateClassRequest(Class cls, Message m, FOStorePMF pmf) {
        super(m, pmf);
        FOStoreModel model = pmf.getModel();
        this.clid = model.getCLID(cls);
        super.fsuid = FOStoreSchemaUID.lookup(cls, model);
        super.jdoClass = model.getJDOClass(cls);
    }
    
    /**
     * @see AbstractRequest#doRequestBody
     * The format of this request is (aside from the request header):
     * <pre>
     * class's name: String
     * clid: CLID (will always be provisional)
     * FOStoreSchemaUID for class
     * numPCSuperclasses: int
     * foreach PCSuperclass in inheritance order going towards Object:
     *     the superclass' class name
     *     the superclass's CLID
     *     the superclass's FOStoreSchemaUID
     * numFields: int
     * scoField: boolean
     * foreach field in the class:
     *     the field's name
     *     the field's CLID
     *     the field's class's FOStoreSchemaUID
     * </pre>
     * Note that we do not currently support all sco information, and will
     * have to ammend this when we do.  In that case, we will write also
     * write, for each field:
     * <pre>
     *     scoField: boolean
     *     elementType: String
     *     allowNulls: boolean
     *     initialSize: int
     * </pre>
     * Note that we write out information in order of name, CLID, FSUID in all
     * cases above.
     */
    public void doRequestBody() throws IOException {

        FOStoreModel model = pmf.getModel();

        if (logger.isDebugEnabled()) {
            logger.debug("ACRequest.dRB: " + clid + // NOI18N
                           ", " + jdoClass.getName() + // NOI18N
                           ", fsuid=" + fsuid); // NOI18N
        }
        out.writeUTF(jdoClass.getName());
        clid.write(out);
        fsuid.write(out);

        // Write PC superclass information
        ArrayList jdoSupers = null;
        int numSupers = 0;
        JDOClass jdoSuper = jdoClass.getPersistenceCapableSuperclass();
        if (null != jdoSuper) {
            jdoSupers = new ArrayList();
            while (null != jdoSuper) {
                jdoSupers.add(jdoSuper);
                jdoSuper = jdoSuper.getPersistenceCapableSuperclass();
            }
            numSupers = jdoSupers.size();
        }
        
        out.writeInt(numSupers);
        
        if (logger.isDebugEnabled()) {
            logger.debug("ACRequest.dRB: numSupers=" + numSupers); // NOI18N
        }
        
        for (int i = 0; i < numSupers; i++) {
            jdoSuper = (JDOClass)jdoSupers.get(i);
            Class javaSuper = javaModelFactory.getJavaClass(jdoSuper.getJavaType());

            if (logger.isDebugEnabled()) {
                logger.debug(
                    "ACRequest.dRB: jdoSuper: " + //NOI18N
                    jdoSuper + ", javaSuper: " + javaSuper); // NOI18N
            }
            CLID superCLID = model.getCLID(javaSuper);
            FOStoreSchemaUID superFSUID =
                FOStoreSchemaUID.lookup(javaSuper, model);
            
            if (logger.isDebugEnabled()) {
                logger.debug("ACRequest.dRB: super" + i + // NOI18N
                               ", " + javaSuper.getName() + // NOI18N
                               ", " + superCLID + // NOI18N
                               ", superFSUID: " + superFSUID); // NOI18N
            }
            
            out.writeUTF(jdoSuper.getName());
            superCLID.write(out);
            superFSUID.write(out);
        }

        // Write field information.
        JDOField fields[] = jdoClass.getManagedFields();
        int numFields = fields.length;
        out.writeInt(numFields);
        
        if (logger.isDebugEnabled()) {
            logger.debug("ACRequest.dRB: numFields=" + numFields); // NOI18N
        }
        
        for (int i = 0; i < numFields; i++) {
            // XXX TBD Model: Add SCO-, other model-provided info
            JDOField fld = fields[i];

            Class cls = javaModelFactory.getJavaClass(fld.getType());
            CLID fldCLID = model.getCLID(cls);
            FOStoreSchemaUID fldFSUID = FOStoreSchemaUID.lookup(cls, model);
            if (logger.isDebugEnabled()) {
                logger.debug("ACRequest.dRB: field" + i + // NOI18N
                               ", " + cls.getName() + // NOI18N
                               ", " + fldCLID + // NOI18N
                               ", fldFSUID: " + fldFSUID); // NOI18N
            }
            
            out.writeUTF(fld.getName());
            fldCLID.write(out);
            fldFSUID.write(out);
        }
    }

    /**
     * @see Request#handleReply
     */
    public void handleReply(Status status, DataInput in, int length)
        throws IOException {
            //
            // The format of this reply is:
            //
            // clid
            //

        CLID newClid = CLID.read(in);
        if (logger.isDebugEnabled()) {
            logger.debug(
                "ActivateClass.hR: " + status + ", " + clid + " " + // NOI18N
                Tester.toHex(clid.getId(), 16) +
                " -> " + newClid + " " + // NOI18N
                Tester.toHex(newClid.getId(), 16));                
        }
        
        // Update the metadata to use the new, store-provided "real" CLID.
        pmf.getModel().updateCLID(clid, newClid);
    }
}
