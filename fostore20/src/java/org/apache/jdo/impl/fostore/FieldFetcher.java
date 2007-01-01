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

import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOUserException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.model.jdo.PersistenceModifier;
import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.state.StateManagerInternal;
import org.apache.jdo.util.I18NHelper;

/**
* Extend AbstractFieldManager overriding only fetchABCField methods
*
* @author Dave Bristor
*/
class FieldFetcher extends AbstractFieldManager {
    /** Read values from here. */
    private final FOStoreInput fin;

    /** PM on whose behalf we read */
    private final PersistenceManagerInternal pm;

    private final FOStoreModel model;

    /** ClassLoader to use for loading the class of the instance. */
    private final ClassLoader candidateLoader;

    /** Class of instance being fetched. */
    private Class cls;

    //
    // One of the follwing 2 fields, sm and oid, will be set at an entry to
    // this object.
    //
    
    // StateManger for which we're reading data.
    private StateManagerInternal sm = null;

    // OID for which we're reading data.
    private OID oid = null;

    // OID provided for the request.
    private OID oldOID = null;

    // Last read field number
    private int currNum = 0;

    // Flag that enables actual field skip.
    // If set to false only currNum is ignored.
    private boolean skip = true;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N
    
    FieldFetcher(FOStoreInput fin,
                 FOStoreModel model,
                 PersistenceManagerInternal pm,
                 ClassLoader candidateLoader) {

        this.fin = fin;
        this.model = model;
        this.pm = pm;
        this.candidateLoader = candidateLoader;
        this.skip = true;
    }

    /** Called by AID when PK fields are written sequentially
     * independent of their actual field numbers.
     */
    FieldFetcher(FOStoreInput fin,
                 FOStoreModel model,
                 PersistenceManagerInternal pm,
                 ClassLoader candidateLoader,
                 boolean skip) {

        this.fin = fin;
        this.model = model;
        this.pm = pm;
        this.candidateLoader = candidateLoader;
        this.skip = skip;
    }

    //
    // Entry points
    //

    /**
    * Invoke this if you have a StateManagerInternal for the object that
    * you're fetching.  For example, see FetchRequest.handleReply().
    */
    void fetch(StateManagerInternal sm, OID oid) throws IOException {
        this.sm = sm;
        this.oldOID = oid;
        fetch();
    }

    /**
    * Invoke this if you have an OID for the object that you're fetching.
    * For example, see GetExtentRequest.handleReply().
    */
    StateManagerInternal fetch(OID oid) throws IOException {
        this.oid = oid;
        fetch();
        return sm;
    }

            
    /**
     * Fetches data from input, resulting in an PersistenceCapable object
     * with state from the datastore.  The format of the data is as per that
     * described in InsertRequest.
     */
    private void fetch() throws IOException {
        String className = fin.readUTF();
        FOStoreSchemaUID fsuid = FOStoreSchemaUID.read(fin);
        if (logger.isDebugEnabled()) {
            logger.debug("FF.fetch/1: className=" + className + // NOI18N
                ", fsuid=" + fsuid + ", oid=" + oid); // NOI18N
        }

        // Class of the instance that we are loading.
        Class instanceClass = null;

        // JDOClass corresponding to instanceClass.
        JDOClass jdoClass = null;

        // ClassLoader of the instance we are loading.
        ClassLoader loader = null;

        try {
            instanceClass = pm.loadClass(className, candidateLoader);
            jdoClass = model.getJDOClass(instanceClass);
            
            FOStoreSchemaUID instanceClassFsuid =
                FOStoreSchemaUID.lookup(instanceClass, model);
            if ( ! instanceClassFsuid.equals(fsuid)) {
                throw new JDOUserException(
                    msg.msg("EXC_FsuidMismatch", className)); // NOI18N
            }
            loader = instanceClass.getClassLoader();

            // If we have an oid, we might not yet have the classname/CLID
            // mapping.  Make it so.
            if (null != oid) {
                CLID clid = oid.getCLID();
                if (model.getClass(clid) == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                            "FF.fetch/1a: putting for " + // NOI18N
                            instanceClass.getName()  + ", " + clid); // NOI18N
                    }
                    model.put(instanceClass, clid);
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new JDOUserException(
                msg.msg("EXC_CannotLoadInstanceClass", className)); // NOI18N
        }

        // We need read the CLID/classname pairs *before* reading the data
        // which contains fields using those CLID's, so that we can establish
        // a mapping that the FieldFetcher (and StateManager) will use to
        // create instances of those classes.
        synchronized(fin) {
            int fieldsLength = fin.readInt();
            int fieldsPos = fin.getPos(); // Save for later
            fin.advance(fieldsLength);

            //
            // Read the CLID/classname pairs, and make sure that we have
            // both loaded the class and mapped the class to the CLID
            //
            
            int size = fin.readInt(); // number of CLID/classname pairs
            for (int i = 0; i < size; i++) {
                CLID clid = CLID.read(fin);
                String fieldClassName = fin.readUTF();
                FOStoreSchemaUID flduid = FOStoreSchemaUID.read(fin);

                Class fieldClass = model.getClass(clid);

                if (logger.isDebugEnabled()) {
                    logger.debug("FF.fetch/2: fieldClassName=" + // NOI18N
                                   fieldClassName + 
                                   ", fieldClass=" + fieldClass + // NOI18N
                                   ", " + clid + // NOI18N
                                   ", flduid=" + flduid); // NOI18N
                }

                if (fieldClass != null) {
                    // The class is already loaded, just do a sanity check.
                    if (!fieldClassName.equals(fieldClass.getName())) {
                        throw new FOStoreFatalInternalException(
                            getClass(), "handleReply", // NOI18N
                            msg.msg("ERR_ClassMismatch", // NOI18N
                                    className,
                                    fieldClassName,
                                    fieldClass.getName()));
                    }
                } else {
                    try {
                        fieldClass = pm.loadClass(fieldClassName, loader);

                        // If the FOStoreSchemaUID for fieldClass doesn't
                        // match flduid, then we've got a structural mismatch
                        // between the class in the database and the class in
                        // the current JVM.
                        if (!
                            flduid.equals(FOStoreSchemaUID.lookup(fieldClass, model))) {
                            throw new JDOUserException(
                                msg.msg(
                                    "EXC_FsuidMismatch", fieldClassName)); // NOI18N
                        }
                        
                        if (logger.isDebugEnabled()) {
                            logger.debug(
                                "FF.fetch/3: putting for " + // NOI18N
                                fieldClass.getName() +
                                ", " + jdoClass + //  NOI18N
                                ", " + clid); // NOI18N
                        }
                        model.put(fieldClass, clid);
                    } catch (ClassNotFoundException ex) {
                        throw new JDOUserException(
                            msg.msg("EXC_CannotLoadFieldClass", // NOI18N
                                    className, fieldClassName));
                    }
                }
            }

            // sm will be null if we were invoked via the "fetch(oid)" entry
            // point.  In that case, pm.getStateManager(oid) is only safe
            // once we have gone through the above CLID/classname mapping.
            if (null == sm) {
                sm = pm.getStateManager(oid, instanceClass);
                sm.setPCClass(instanceClass); // this will be a no-op if sm is replaced above.
            } else {
                // It may be the case when StateManager did not know the
                // actual type of the instance Class (application identity)
                // Read the actual CLID from the stream.
                CLID newCLID = CLID.read(fin);
                if (oldOID != null && !oldOID.isDataStoreIdentity()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                            "FF.fetch: check OID=" + oldOID + " for SM=" + sm); // NOI18N
                    }
                    long uid = oldOID.getUID();
                    CLID clid = oldOID.getCLID();
                    if (!newCLID.equals(clid)) {
                        FOStorePMF pmf = (FOStorePMF)pm.getPersistenceManagerFactory();
                        oldOID.replaceProvisionalOIDWithReal(OID.create(newCLID, uid), null, null);

                        if (logger.isDebugEnabled()) {
                            logger.debug(
                                "FF.fetch: new OID=" + oldOID + ", SM=" + sm); // NOI18N
                        }
                    }
                }
                sm.setPCClass(instanceClass); // this will be a no-op if sm is replaced above.
            }

            // Save this end-of-data position for later use
            int endPos = fin.getPos();
            
            // Set the stream's position for reading data values, and get
            // them.
            fin.setPos(fieldsPos);

            // XXX We should be using the field numbers as given to FetchRequest.
            int fields[] = jdoClass.getPersistentFieldNumbers();
            int numFields = fields.length;
            if (logger.isDebugEnabled()) {
                logger.debug(
                    "FF.fetch/4: numFields=" + numFields); // NOI18N
            }
            currNum = 0;
            cls = sm.getPCClass();
            sm.replace(fields, this);

            fin.setPos(endPos);
        }
    }

    void setPCClass(Class pcClass) {
        cls = pcClass;
    }

    public boolean fetchBooleanField(int fieldNum) {
        boolean rc = false;
        
        skipFields(fieldNum);
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            rc = t.fetchBoolean(fin);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "fetchBooleanField", ex); // NOI18N
        }
        return rc;
    }
    
    public char fetchCharField(int fieldNum) {
        char rc = ' ';
        
        skipFields(fieldNum);
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            rc = t.fetchChar(fin);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "fetchCharField", ex); // NOI18N
        }
        return rc;
    }
   
    public byte fetchByteField(int fieldNum) {
        byte rc = 0;
        
        skipFields(fieldNum);
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            rc = t.fetchByte(fin);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "fetchByteField", ex); // NOI18N
        }
        return rc;
    }

    public short fetchShortField(int fieldNum) {
        short rc = 0;
        
        skipFields(fieldNum);
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            rc = t.fetchShort(fin);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "fetchShortField", ex); // NOI18N
        }
        return rc;
    }

    public int fetchIntField(int fieldNum) {
        int rc = 0;
        
        skipFields(fieldNum);
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            rc = t.fetchInt(fin);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "fetchIntField", ex); // NOI18N
        }
        return rc;
    }

    public long fetchLongField(int fieldNum) {
        long rc = 0L;
        
        skipFields(fieldNum);
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            rc = t.fetchLong(fin);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "fetchLongField", ex); // NOI18N
        }
        return rc;
    }

    public float fetchFloatField(int fieldNum) {
        float rc = 0.0F;
        
        skipFields(fieldNum);
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            rc = t.fetchFloat(fin);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "fetchFloatField", ex); // NOI18N
        }
        return rc;
    }

    public double fetchDoubleField(int fieldNum) {
        double rc = 0.0;
        
        skipFields(fieldNum);
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            rc = t.fetchDouble(fin);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "fetchDoubleField", ex); // NOI18N
        }
        return rc;
    }

    public String fetchStringField(int fieldNum) {
        String rc = ""; // NOI18N
        
        skipFields(fieldNum);
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            // Need to pass all correct parameters as this can be
            // a nested request and we don't want to override important
            // info with null's.
            rc = (String)t.fetchObject(fin, sm, fieldNum, pm); 
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "fetchStringField", ex); // NOI18N
        }
        return rc;
    }

    public Object fetchObjectField(int fieldNum) {
        Object rc = null;
        
        skipFields(fieldNum);
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNum);
        try {
            rc = t.fetchObject(fin, sm, fieldNum, pm);
        } catch (Exception ex) {
            throw new FOStoreFatalInternalException(
                this.getClass(), "fetchObjectField", ex); // NOI18N
        }
        return rc;
    }

    private void skipFields(int fieldNum) {
        if (skip) {
            while(currNum < fieldNum) {
                FOStoreTranscriber t = model.getTranscriber(cls, currNum);
                try {
                    t.skip(fin);
                } catch (Exception ex) {
                    throw new FOStoreFatalInternalException(
                        this.getClass(), "skipFields", ex); // NOI18N
                }
                currNum++;
            }
            currNum++; // for the next time.
        } // do nothing otherwise.
    }
}
