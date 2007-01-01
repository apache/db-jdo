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

import javax.jdo.JDOFatalDataStoreException;

import org.apache.jdo.util.I18NHelper;

/**
* Process requests to insert objects in the datastore.
*
* @author Dave Bristor
*/
//
// This is server-side code.  It does not need to live in the client.
//
class InsertHandler extends RequestHandler {

    /** I18N help */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);
    
    InsertHandler(Reply reply, int length,
                          FOStoreServerConnection con) {

        super(reply, length, con);
    }

    static final HandlerFactory factory =
        new HandlerFactory() {
            public RequestHandler getHandler(Reply reply, int length,
                                             FOStoreServerConnection con) {
                return new InsertHandler(reply, length, con);
            }};


    /**
    * Changes all provisional OID's in an instance's in-store Block to real,
    * datastore OID's.
    */
    private class InsertFinisher implements RequestFinisher {
        private final FOStoreDatabase db;
        
        // This is the oid of the object in which we're going to replace the
        // provisional OID with a real OID.
        private final OID oid;

        // Offsets into the Block mapped by OID at which provisional OID's
        // lie.  Replace them with datastore OID's. 
        private final int oidOffsets[];

        // Offsets into the Block's CLID appendix at which provisional CLID's
        // lie.  Replace them with datastore CLID's.
        private final int clidOffsets[];

        InsertFinisher(FOStoreDatabase db, OID oid, DataInput in) 
            throws IOException {
            this.db = db;
            this.oid = oid;

            // Read oidOffsets
            int numOIDOffsets = in.readInt();
            if (logger.isDebugEnabled()) {
                logger.debug("InsertFinisher: numOIDOffsets=" +  // NOI18N
                               numOIDOffsets);
            }

            this.oidOffsets = numOIDOffsets > 0 ? new int[numOIDOffsets] : null;
            if (numOIDOffsets > 0) {
                for (int i = 0; i < numOIDOffsets; i++) {
                    oidOffsets[i] = in.readInt();
                }
            }

            // Read CLID offsets
            int numCLIDOffsets = in.readInt();
            if (logger.isDebugEnabled()) {
                logger.debug("InsertFinisher: numCLIDOffsets=" +  // NOI18N
                               numCLIDOffsets);
            }
            this.clidOffsets = numCLIDOffsets > 0 ? new int[numCLIDOffsets] : null;
            if (numCLIDOffsets > 0) {
                for (int i = 0; i < numCLIDOffsets; i++) {
                    clidOffsets[i] = in.readInt();
                }
            }
        }

        /**
         * Replace all provisional OIDs in the data with real OIDs.
         * Byte array data is the datablock containing provisional OID's which
         * need to be converted in-place.
         */
        private void finishOIDOffsets(byte data[])
            throws FOStoreDatabaseException, IOException {

            FOStoreInput in =
                new FOStoreInput(data, 0, data.length);
            FOStoreOutput out = new FOStoreOutput();

            if (null != oidOffsets) {
                int numOIDOffsets = oidOffsets.length;
                for (int i = 0; i < numOIDOffsets; i++) {
                    int offset = oidOffsets[i];
                    in.setPos(offset);
                    OID provOID = OID.read(in);
                    OID realOID = db.getRealOIDFromProvisional(provOID);
                    
                    if (logger.isDebugEnabled()) {
                        logger.debug("InsertFinisher.finish: i=" + i // NOI18N
                                       + " offset="+ offset); // NOI18N
                        logger.debug("InsertFinisher.finish: prov" + provOID +
                                       " " // NOI18N
                                       + Tester.toHex(provOID.oid, 16));
                        logger.debug("InsertFinisher.finish: real" + realOID +
                                       " " // NOI18N
                                       + Tester.toHex(realOID.oid, 16));
                    }
                    
                    // Write the real OID
                    // XXX PERF Best way to convert a long to byte array?
                    // XXX Conversion should be done by OID itself, since only
                    // it really knows it's representation is a long.
                    realOID.write(out);
                    System.arraycopy(out.getBuf(), 0, data, offset, 8);
                    out.setPos(0); // Set for next time through.
                }
            }
        }
        
        /**
         * Replace all provisional CLIDs in the data with real CLIDs.
         * Byte array data is the datablock containing provisional CLID's
         * which need to be converted in-place.
         */
        private void finishCLIDOffsets(byte data[])
            throws FOStoreDatabaseException, IOException {

            FOStoreInput in =
                new FOStoreInput(data, 0, data.length);
            FOStoreOutput out = new FOStoreOutput();

            if (null != clidOffsets) {
                int numCLIDOffsets = clidOffsets.length;
                for (int i = 0; i < numCLIDOffsets; i++) {
                    int offset = clidOffsets[i];
                    in.setPos(offset);
                    CLID provCLID = CLID.read(in);
                    CLID realCLID = db.getRealCLIDFromProvisional(provCLID);

                    if (logger.isDebugEnabled()) {
                        logger.debug("InsertFinisher.finish: i=" + i // NOI18N
                                       + " offset="+ offset); // NOI18N
                        logger.debug("InsertFinisher.finish: prov" + provCLID); // NOI18N
                        logger.debug("InsertFinisher.finish: real" + realCLID); // NOI18N
                    }

                    if (null == realCLID) {
                        throw new JDOFatalDataStoreException(
                            msg.msg("ERR_NullRealCLID", provCLID)); // NOI18N
                    }

                    // Write the real CLID
                    // XXX PERF Best way to convert an int to byte array?
                    // XXX Conversion should be done by CLID itself, since only
                    // it really knows its representation is an int.
                    realCLID.write(out);
                    System.arraycopy(out.getBuf(), 0, data, offset, 4);
                    out.setPos(0);
                }
            }
        }
        
        /**
        * @see RequestFinisher#finish
        */
        public void finish() {
            if (logger.isDebugEnabled()) {
                logger.debug("InsertFinisher.finish: " + oid); // NOI18N
            }

            if (null != oidOffsets || null != clidOffsets) {
                try {
                    Block block = (Block)db.get(oid);
                    byte data[] = block.getData();

                    if (logger.isDebugEnabled()) {
                        logger.debug("InsertFinisher.finish: block length=" + // NOI18N
                                       data.length);
                    }

                    // Replace each provisional OID with a datastore OID.
                    finishOIDOffsets(data);

                    // Replace each provisional CLID with a datastore CLID.
                    finishCLIDOffsets(data);

                    if (logger.isTraceEnabled()) {
                        logger.trace("InsertFinisher: after finalizing"); // NOI18N
                        block.dump();
                    }
            
                } catch (FOStoreDatabaseException ex) {
                    throw new FOStoreFatalInternalException(
                        this.getClass(), "finish", ex); // NOI18N
                } catch (IOException ex) {
                    throw new FOStoreFatalIOException(
                        this.getClass(), "finish", ex); // NOI18N
                }
            }
        }
    }


    /**
     * @see RequestHandler#handleRequest
     */
    RequestFinisher handleRequest()
        throws IOException, FOStoreDatabaseException {

        RequestFinisher rc = null;
        FOStoreInput in = con.getInputFromClient();
        FOStoreDatabase db = con.getDatabase();
        DBInfo dbInfo = db.getDBInfo();

        OID oid = OID.read(in);
        CLID clid = oid.getCLID();
        if (logger.isDebugEnabled()) {
            logger.debug("InsertHandler.hR/1: given" + oid // NOI18N
                           + " " + Tester.toHex(oid.oid, 16)); // NOI18N
        }

        if (clid.isProvisional()) {
            clid = db.getRealCLIDFromProvisional(clid);
        }

        OID realOID;
        if (oid.isProvisional()) {
            realOID = db.getRealOIDFromProvisional(oid);
            if (null == realOID) {
                realOID = dbInfo.newInstanceOID(clid);
            }
        } else {
            realOID = oid;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("InsertHandler.hR/2: real" + realOID + " " // NOI18N
                           + Tester.toHex(realOID.oid, 16));
        }
        Block block = readBlock(in);

        rc = new InsertFinisher(db, realOID, in);

        try {
            updateDB(realOID, oid, block, db);

            reply.writeOID(realOID);
            reply.setStatus(Status.OK);

        } catch (Exception ex) {
            reply.writeOID(realOID);
            reply.setStatus(Status.ERROR,
                            msg.msg("ERR_InsertException", realOID)); // NOI18N
        }
        
        return rc;
    }

    protected Block readBlock(FOStoreInput in) throws IOException {
        int blockLength = in.readInt();
        Block block = FOStoreDatabase.createBlock(in, blockLength);

        if (logger.isDebugEnabled()) {
            logger.debug("InsertHandler.readBlock: blockLength="+  // NOI18N
                           blockLength);
            if (logger.isTraceEnabled()) {
                block.dump();
            }
        }
        return block;
    }

    /**
     * Add the block to the database, and to the database's extent.
     * @param realOID OID to use as key in the database.
     * @param givenOID by which object was inserted, possibly provisional.
     * @param block Block to be inserted in database.
     * @param db Database into which block is inserted.
     */
    protected void updateDB(OID realOID, OID givenOID, Block block,
                            FOStoreDatabase db)
        throws IOException, FOStoreDatabaseException {

        db.add(realOID, block);
        if (givenOID.isProvisional()) {
            db.mapProvisionalOIDToReal(givenOID, realOID);
        }

        // Add instance to its extent
        DBInfo dbInfo = db.getDBInfo();
        OID extentOID = dbInfo.getExtentOID(realOID.getCLID());
        DBExtent dbExtent  = (DBExtent)db.get(extentOID);
        dbExtent.add(realOID);
        con.addExtent(dbExtent);
    }
}
