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
import java.util.HashSet;
import java.util.Iterator;

/**
* Process GetExtent requests.
*
* @author Dave Bristor
*/
// This is server-side code.  It does not need to live in the client.
class GetExtentHandler extends RequestHandler {

    private GetExtentHandler(Reply reply, int length,
                         FOStoreServerConnection con) {

        super(reply, length, con);
    }
    
    public static final HandlerFactory factory =
        new HandlerFactory() {
                public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new GetExtentHandler(reply, length, con);
            }};

    /**
    * The desired extent's class can be specified either by CLID (i.e., the
    * class's CLID was already loaded from store to the client) or classname
    * and FSUID (in case the CLID wasn't known).  In the latter case, look
    * up the CLID.
    * <br>
    * With the CLID, get the set of extents: it will be only one if
    * subclasses is false, an aribtrary number otherwise.
    * <br>
    * Iterate over the extents, writing the items into the reply.
    * @see RequestHandler#handleRequest
    * @see GetExtentRequest#doRequestBody
    */
    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        FOStoreInput in = con.getInputFromClient();
        FOStoreDatabase db = con.getDatabase();
        HashSet dbExtents = null;

        // true => resulting extent to include subclasses of indicated class.
        boolean subclasses;

        int maxInstances = in.readInt();

        // By CLID or by classname/fsuid?
        boolean isCLID = in.readBoolean();

        CLID clid = null;
        if (isCLID) {
            clid = CLID.read(in);
            subclasses = in.readBoolean();

        } else {
            // Find the clid for the given name/fsuid.
            
            String name = in.readUTF();
            FOStoreSchemaUID fsuid = FOStoreSchemaUID.read(in);
            subclasses = in.readBoolean();

            if (logger.isDebugEnabled()) {
                logger.debug(
                    "GetExtentHandler for name=" + name); // NOI18N
            }

            for (Iterator i = db.getDBInfo().getDBClasses(); i.hasNext();) {
                DBClass dbClass = (DBClass)i.next();
                if (fsuid.equals(dbClass.getFSUID()) &&
                    name.equals(dbClass.getName())) {
                    clid = dbClass.getCLID();
                    // break because there can be only one match.
                    break;
                }
            }
        }

        // Get the set of CLIDs of extents
        if (null != clid) {
            dbExtents = getDBExtents(clid, subclasses, db);
        }

        if (logger.isDebugEnabled()) {
            if (null == dbExtents) {
                logger.debug("GetExtentHandler.hr: no extents"); // NOI18N
            } else {
                for (Iterator i = dbExtents.iterator(); i.hasNext();) {
                    logger.debug(
                        "GetExtentHandler.hr: extent=" + i.next()); // NOI18N
                }
            }
        }

        Status status = null;
        int extentSize = 0;
        int numInstances = 0;
        int numOIDs = 0;
        
        // Write instances into the reply
        if (null == dbExtents) {
            reply.writeInt(0);
            reply.writeInt(0);
            reply.writeInt(0);
            status = Status.OK;
            
        } else {
            // Save space for writing various counts in the reply.
            int extentSizePos = reply.beginStash();
            int numInstancesPos = reply.beginStash();
            int numOIDsPos = reply.beginStash();

            for (Iterator i = dbExtents.iterator(); i.hasNext();) {
                DBExtent extent = (DBExtent)i.next();
        
                if (null != extent && extent.size() > 0) {

                    // Fetch some of the instances from the store.
                    // If we fail to get an object, continue, but set status
                    // to WARN.
                    for (Iterator j = extent.iterator(); j.hasNext();) {
                        OID oid = (OID)j.next();
                        try {
                            reply.writeOID(oid);
                            if (logger.isDebugEnabled()) {
                                logger.debug(
                                    "GetExtentHandler.hR/2: " + // NOI18N
                                    oid  + " " + // NOI18N
                                    Tester.toHex(oid.oid, 16));
                            }

                            if (numInstances < maxInstances) {                    
                                Block block = (Block)db.get(oid);
                                if (logger.isTraceEnabled()) {
                                    block.dump();
                                }

                                byte data[] = block.getData();
                                reply.writeInt(data.length);
                                reply.write(data);
                                numInstances++;
                            } else {
                                numOIDs++;
                            }
                            extentSize++;
                        } catch (FOStoreDatabaseException ex) {
                            status = Status.WARN;
                        }
                    }
                }
            }

            // Write counts.
            reply.endStash(extentSize, extentSizePos);
            reply.endStash(numInstances, numInstancesPos);
            reply.endStash(numOIDs, numOIDsPos);
            
            if (null == status) {
                status = Status.OK;
            }
        }
        reply.setStatus(status);

        if (logger.isDebugEnabled()) {
            logger.debug(
                "GetExtentHandler.hR/3: " + // NOI18N
                "extentSize=" + extentSize + // NOI18N
                ", numInstances=" + numInstances + // NOI18N
                ", numOIDs=" + numOIDs + " " + status); // NOI18N
        }
        return null;
    }

    /**
    * Get a set of CLIDs of extents for the given clid.  The set will have at
    * most one element if subclasses is false, or an arbitrary number
    * otherwise.
    * @param clid The CLID of the class whose extent is wanted.
    * @param subclasses If true, then include extents for all subclasses of
    * the class corresponding to CLID.
    * @param db Database in which to find extents.
    * @return Set of extents of clid (and possibly its subclasses).
    * @throws FOStoreDatabaseException if a database error occurs.
    */
    HashSet getDBExtents(CLID clid, boolean subclasses, FOStoreDatabase db)
        throws FOStoreDatabaseException {

        HashSet rc = null;
        
        if (logger.isDebugEnabled()) {
            logger.debug(
                "GetExtentHandler.getExtents: for " + clid); // NOI18N
        }
        OID oid = db.getDBInfo().getExtentOID(clid);
        DBExtent extent = (DBExtent)db.getIfExists(oid);
        if (null != extent) {
            rc = new HashSet();
            rc.add(extent);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug(
                "GetExtentHandler.getExtents: got " + extent); // NOI18N
        }
        
        if (subclasses) {
            OID ssOID = DBInfo.getSubclassSetOID(clid);
            SubclassSet ss = (SubclassSet)db.getIfExists(ssOID);
            if (null != ss) {
                for (Iterator i = ss.iterator(); i.hasNext();) {
                    CLID subCLID = (CLID)i.next();
                    OID subExtentOID = DBInfo.getExtentOID(subCLID);
                    DBExtent subExtent =
                        (DBExtent)db.getIfExists(subExtentOID);
                    if (null != subExtent) {
                        if (logger.isDebugEnabled()) {
                            logger.debug(
                                "GetExtentHandler.getExtents: "+ // NOI18N
                                "subclass " + subExtent); // NOI18N
                        }
                        if (null == rc) {
                            rc = new HashSet();
                        }
                        rc.add(subExtent);
                    }
                }
            }
        }
        return rc;
    }
}
