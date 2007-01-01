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
import java.security.AccessController;
import java.security.PrivilegedAction;
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
 * Represents a request to get the extent of a class and possibly its
 * subclasses.
 *
 * @author Dave Bristor
 */
//
// This is client-side code.  It does not need to live in the server.
//
class GetExtentRequest extends AbstractRequest {
    /** The PersistenceManagerInternal that is making this request. */
    private final PersistenceManagerInternal pm;
    
    /** The class of the extent sought by this request. */
    private final Class cls;

    /** If true retrieve instances of cls and its subclasses, otherwise just
     * of cls. */
    private final boolean subclasses;

    /** Maximum number of instances that should be returned at a time. */
    private static final int maxInstances;

    /** Extent returned to user. */
    private FOStoreExtent extent;

    /** List of returned instances */
    private ArrayList instances;

    /** List of returned object Ids */
    private ArrayList oids;

    static {
        int max = 100;
        try {
            String property = (String)AccessController.doPrivileged(
                new PrivilegedAction () {
                    public Object run () {
                        return System.getProperty("maxInstances"); // NOI18N
                    }
                }
                );
            max = Integer.parseInt(property);
        } catch (SecurityException ex) {
            // cannot read maxInstances flag => log warning
            if (logger.isWarnEnabled())
                logger.warn(msg.msg("MSG_CannotGetSystemProperty", //NOI18N
                    "maxInstances", ex.toString())); //NOI18N
        } catch (Exception ex) {
            // use default
        }
        maxInstances = max;
    }

    GetExtentRequest(FOStoreExtent extent, Class cls, boolean subclasses,  Message m,
                     PersistenceManagerInternal pm) {

        super(m, (FOStorePMF)pm.getPersistenceManagerFactory());
        this.pm = pm;
        this.cls = cls;
        this.subclasses = subclasses;
        this.extent = extent;
    }

    /**
     * @see AbstractRequest#doRequestBody
     * @see InsertRequest#doRequestBody
     */
    protected void doRequestBody() throws IOException {
        //
        // The format of this request is variable, depending on whether or not
        // we already have in-client metadata for the given class.
        //
        // If we do have in-client metadata for the given class:
        //
        // int: maximum number of instances to return
        // boolean: true
        // clid of the class
        // boolean: true if subclasses, false otherwise
        //
        // Otherwise:
        //
        // int: maximum number of instances to return
        // boolean: flase
        // String name of the class
        // FOStoreSchemaUID of the class.
        // boolean: true if subclasses, false otherwise
        //

        FOStoreModel model = pmf.getModel();
        CLID clid = model.getCLID(cls);
        out.writeInt(maxInstances);
        if (null != clid && ! clid.isProvisional()) {
            out.writeBoolean(true);
            clid.write(out);
            if (logger.isDebugEnabled()) {
                logger.debug("GetExtentRequest.dRB: " + clid // NOI18N
                               + " subclasses=" + subclasses); // NOI18N
            }
        } else {
            out.writeBoolean(false);
            out.writeUTF(cls.getName());
            FOStoreSchemaUID fsuid = FOStoreSchemaUID.lookup(cls, model);
            fsuid.write(out);
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "GetExtentRequest.dRB: " + cls.getName() // NOI18N
                    + " subclasses=" + subclasses); // NOI18N
            }
        }
        out.writeBoolean(subclasses);
    }

    /**
     * Handles replies to GetExtentRequests.
     * The format of the reply is:
     * <pre>
     * int: count of number of instances in the extent
     * int: count of the number of instances returned
     * int: count of the number of oids returned
     * that many instances
     * </pre>
     * The number of instances returned + number of oid returned = number
     * of instances in the extent.  This is for performance: we don't
     * want to return *all* the instances at once: we return some, plus
     * information so that we can get the rest, if the user requests so.
     * <p>
     * The status might be Status.WARN, in which case there were OID's in
     * the database extent which were unreadable.  But the count should
     * be for the actual number of objects returned.
     */
     public void handleReply(Status status, DataInput in, int length)
         throws IOException {


         int extentSize = in.readInt();
         int numInstances = in.readInt();
         int numOIDs = in.readInt();
         if (logger.isDebugEnabled()) {
             logger.debug(
                 "GER.hR/1: extentSize=" + extentSize + // NOI18N
                 ", numInstances=" + numInstances + // NOI18N
                 ", numOIDs=" + numOIDs); // NOI18N
         }

         // Get instances for the extent
         instances =
             readInstances(in, numInstances, pmf.getModel(), pm, cls);

         // Add OIDs for the extent
         oids = new ArrayList(numOIDs);
         for (int i = 0; i < numOIDs; i++) {
             OID oid = OID.read(in);
             if (logger.isDebugEnabled()) {
                 logger.debug("GER.hR/4: " + oid + // NOI18N
                                " " + Tester.toHex(oid.oid, 16)); // NOI18N
             }
             oids.add(oid);
         }
    }


    /**
     * Reads instances from given DataInput using a {@link FieldFetcher}.
     * @param in DataInput from which instances are read.
     * @param numInstances Number of instances to read from <code>in</code>.
     * @param model Model required to by {@link FieldFetcher}.
     * @param pm PersistenceManagerInternal required  {@link FieldFetcher}.
     * @param cls Candidate Class for which instances are being obtained.
     * @return ArrayList of instances read.
     */
    static ArrayList readInstances(DataInput in, int numInstances,
                                   FOStoreModel model,
                                   PersistenceManagerInternal pm,
                                   Class cls)
        throws IOException {

         ArrayList rc = new ArrayList(numInstances);
         for (int i = 0; i < numInstances; i++) {
             OID oid = OID.read(in);
             if (logger.isDebugEnabled()) {
                 logger.debug("GER.readInstances/1: " + oid + // NOI18N
                                " " + Tester.toHex(oid.oid, 16)); // NOI18N
             }
             
             // Fill in pc's fields.
             int objLength = in.readInt();
             if (logger.isDebugEnabled()) {
                 logger.debug(
                     "GER.readInstances/2: reading " + objLength +  // NOI18N
                     " bytes"); // NOI18N
             }
             byte data[] = new byte[objLength];
             in.readFully(data);
             FieldFetcher ff =
                 new FieldFetcher(new FOStoreInput(data, 0, objLength),
                                  model,
                                  pm,
                                  cls.getClassLoader());
             StateManagerInternal sm = ff.fetch(oid);
             Object pc = sm.getObject();
             rc.add(pc);
         }
         return rc;
    }

    /** Returns max number of instances */
    int getMaxInstances() {
        return maxInstances;
    }

    /** Returns the list of instances */
    ArrayList getInstances() {
        return instances;
    }

    /** Returns the list of object id's */
    ArrayList getOIDs() {
        return oids;
    }

    /** Returns the Extent associated with this request */
    Extent getExtent() {
        return extent;
    }

}
