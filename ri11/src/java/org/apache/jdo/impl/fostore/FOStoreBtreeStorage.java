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

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.netbeans.mdr.persistence.MOFID;
import org.netbeans.mdr.persistence.ObjectResolver;
import org.netbeans.mdr.persistence.StorageException;
import org.netbeans.mdr.persistence.btreeimpl.btreestorage.BtreeFactory;
import org.netbeans.mdr.persistence.btreeimpl.btreestorage.BtreeStorage;

/**
 * FOStore specific BtreeStorage subclass. This class manages MOFIDs for a
 * FOStore datastore. A MOFID consists of two parts: a storageId of type
 * String and a serial number of type long. When storing on disk the
 * storageId is converted into a number and stored as 16-bit value. Only 48
 * bits of the serial number get represented on disk. FOStore uses the
 * serial number to encode the uid part of an OID and the storageId for the
 * class id of an OID. The btree class MOFIDInfo converts a MOFID into a
 * byte array of 8 bytes that is stored on disk. The two high order bytes
 * represent the storageId. Class MOFIDInfo calls method storageIdToNumber
 * to convert a String storageId into a number. The remaining 6 bytes
 * represent the serial number.  
 *
 * @author Michael Bouschen
 * @since 1.1
 * @version 1.1
 */
public class FOStoreBtreeStorage extends BtreeStorage {
    
    /** 
     * Dummy ObjectResolver instance required by BtreeStorage create and 
     * open methods.
     */
    private static final ObjectResolver resolver = new Resolver();

    /** Prefix for the storageId generated from an class id. */
    private static final String CLID_PREFIX = "FOSTORE_CLID";

    /** Length of class id prefix. */
    private static final int CLID_PREFIX_LENGTH = 12;

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N

    /**
     * Creates a new FOStoreBtreeStorage instance. The constructor creates
     * or opens a storage based on the specified argument <code>isNew</code>.
     * @param name the name of the storage.
     * @param isNew true if the database is being created
     */
    public FOStoreBtreeStorage(String name, boolean isNew) 
        throws StorageException {
        super(name);
        if (isNew) {
            create(true, resolver);
        } 
        else {
            open(false, resolver);
        }
    }
    
    /**
     * Returns Creates a MOFID based on the class id and uid taken from a
     * FOStore OID. The storageId of the returned MOFID represents the
     * class id and the serial number represents the uid.
     * @param clid the class id 
     * @param uid the unique id
     * @return MOFID representing class id and uid taken from  FOStore OID.
     */
    public MOFID createMOFID(int clid, long uid) {
        // Add FIRST_EXTERNAL_ID to avoid clashes with 
        // internal btree serial numbers
        long serialNumber = uid + BtreeFactory.FIRST_EXTERNAL_ID;
        // The prefix CLID_PREFIX check whether the storageId denotes a
        // fostore class id. Add FIRST_EXTERNAL_CODE to avoid clashes with  
        // internal btree storageId codes
        String storageId = 
            CLID_PREFIX + (clid + BtreeFactory.FIRST_EXTERNAL_CODE);
        return new MOFID(serialNumber, storageId);
    }
    
    /** 
     * Converts a storageId to an int. If the specified storageId starts
     * with CLID_PREFIX, then the storageId represents a class id. In this
     * case use the class id as the numeric reresentation. 
     * @param storageId the storageId as String
     * @return the numeric representation of the storageId 
     */
    public int storageIdToNumber(String storageId) throws StorageException {
        if (storageId.startsWith(CLID_PREFIX))
            return Integer.parseInt(storageId.substring(CLID_PREFIX_LENGTH));
        else 
            return super.storageIdToNumber(storageId);
    }
    
    /**
     * Creates a storage id from an int. It returns the FOStore specific
     * String representation for a storageId representing a class id.
     * @param number the numberic representation of the storageId 
     * @return the storageId as String
     */
    public String numberToStorageId(int number) throws StorageException {
        if (number >= BtreeFactory.FIRST_EXTERNAL_CODE)
            return CLID_PREFIX + number;
        else
            return super.numberToStorageId(number);
    }

    /** Dummy implementation. */
    static class Resolver implements ObjectResolver {
        
        /** */
        public Object resolve(String storageID, Object key) {
            if (logger.isDebugEnabled()) {
                logger.debug("FOStoreBtreeStorage: called Resolver.resolve(" +
                             storageID + ", " + key + ")");
            }
            return new Object();
        }
    }
}

