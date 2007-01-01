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
import java.util.Iterator;

/**
* Process requests to activate classes.  'Activating a class' means giving it
* a representation in the store.
*
* @author Dave Bristor
*/
// This is server-side code.  It does not need to live in the client.
class ActivateClassHandler extends RequestHandler {

    private ActivateClassHandler(Reply reply, int length,
                                 FOStoreServerConnection con) {

        super(reply, length, con);
    }
    
    public static final HandlerFactory factory =
        new HandlerFactory() {
                public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new ActivateClassHandler(reply, length, con);
            }};


    /** Takes care of changing the provisional CLIDs in fields whose classes
     * have been activated into real CLIDs.
     */
    class ActivateClassFinisher implements RequestFinisher {
        private final FOStoreDatabase db;
        private final OID oid;

        ActivateClassFinisher(FOStoreDatabase db, OID oid) {
            this.db = db;
            this.oid = oid;
            if (logger.isDebugEnabled()) {
                logger.debug("ACFinisher for " + oid); // NOI18N
            }
        }

        /**
        * @see RequestFinisher#finish
        */
        public void finish() {
            DBClass dbClass = null;
            try {
                dbClass = (DBClass)db.get(oid);

                if (dbClass.hasProvisionals()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("ACFinisher for " + oid + " remapCLIDs"); // NOI18N
                    }
                    dbClass.remapCLIDs(db);
                }

                if (dbClass.hasSuperclasses()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("ACFinisher for " + oid + " setupSubclasses"); // NOI18N
                    }
                    dbClass.setupSubclasses(db);
                }
                
                db.replace(oid, dbClass);
            } catch (FOStoreDatabaseException ex) {
                throw new FOStoreFatalInternalException(
                    this.getClass(), "finish", ex); // NOI18N
            }
        }
    }
    // end of class ActivateClassFinisher

    /** @see RequestHandler#handleRequest
     * @see ActivateClassRequest#doRequestBody
     */
    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        RequestFinisher rc = null;

        // We know the CLID is provisional; we wouldn't be activating
        // otherwise.  See if we have already activated this class in this
        // store.  If so, return it's CLID in the reply; otherwise allocate a
        // new CLID and provide that to the client.

        DataInput in = con.getInputFromClient();
        FOStoreDatabase db = con.getDatabase();
        DBInfo dbInfo = db.getDBInfo();

        OID classOID = null; // datastore-assigned OID for given class
        CLID rCLID = null; // datastore-assigned CLID for given class

        String name = in.readUTF();
        CLID pCLID = CLID.read(in);
        FOStoreSchemaUID fsuid = FOStoreSchemaUID.read(in);

        if (logger.isDebugEnabled()) {
            logger.debug(
                "ActivateClassHandler.hR/0: " + pCLID + // NOI18N
                ", " + name + // NOI18N
                ", fsuid=" + fsuid); // NOI18N
        }

        // Iterate over all classes in the store, looking for one who's name
        // is the same.  If there isn't one, assign a new CLID, and store the
        // below data with it.  If there already is one, get it's CLID, and
        // read the rest of the data from the buffer, but throw it away.
        DBExtent extent = null;
        for (Iterator i = dbInfo.getExtents(); i.hasNext();) {
            extent = (DBExtent)i.next();
            if (extent.isExtentFor(name, fsuid)) {
                break;
            } else {
                extent = null;
            }
        }

        // Regardless of whether the class is already activated, we create a
        // DBClass, as doing so reads the remainder of the associated data.
        // XXX PERF Skip bytes instead of reading/parsing data
        DBClass dbClass = DBClass.create(name, pCLID, fsuid,in, db);

        if (null != extent) {
            classOID = extent.getDBClassOID();
            rCLID = classOID.getCLID();
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "ActivateClassHandler.hR/1: existing " +  // NOI18N
                    classOID);
            }
        } else {
            // Get a real OID for the dbClass.  Keep dbClass's existing CLID
            // around to map from references to it from other requests in the
            // same message as this ActivateClassRequest.
            classOID = dbInfo.newClassOID();
            rCLID = classOID.getCLID();
            dbClass.setCLID(rCLID);

            // Put the dbClass in the database
            db.put(classOID, dbClass);

            // Now create an extent for objects of this type.
            DBExtent dbExtent =
                DBExtent.create(db, dbClass.getName(), fsuid, rCLID);
            dbExtent.store(db);
            con.addExtent(dbExtent);
        }

        if (logger.isDebugEnabled()) {   
            logger.debug("ActivateClassHandler.hr/3:"); // NOI18N
            for (Iterator i = dbInfo.getExtents(); i.hasNext();) {
                extent = (DBExtent)i.next();
                OID o = extent.getDBClassOID();
                DBClass dbc = (DBClass)db.get(o);
                logger.debug(dbc.toString());
            }
        }
        
        db.mapProvisionalCLIDToReal(pCLID, rCLID);
        reply.writeCLID(rCLID);
        reply.setStatus(Status.OK);
        
        if (dbClass.hasProvisionals() || dbClass.hasSuperclasses()) {
            rc = new ActivateClassFinisher(db, classOID);
        }
        
        return rc;
    }
}
