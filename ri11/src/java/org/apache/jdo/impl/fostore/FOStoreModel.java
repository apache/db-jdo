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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.jdo.JDOFatalInternalException;

import javax.jdo.spi.PersistenceCapable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.impl.model.java.runtime.RuntimeJavaModelFactory;
import org.apache.jdo.model.ModelException;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.model.jdo.JDOIdentityType;
import org.apache.jdo.model.jdo.JDOModel;
import org.apache.jdo.pm.PersistenceManagerFactoryInternal;
import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.util.I18NHelper;

/**
* Provides model information required by fostore: mapping between CLID's and
* the java.lang.Class's.
*
* @author Dave Bristor
*/
class FOStoreModel {
    // Maps from java.lang.Class to CLID.
    private HashMap clids = new HashMap();

    /** Maps from provisional CLID's to java.lang.Class's.  See updateCLID
    * and getClass.
    */
    private HashMap provisionalCLIDs = new HashMap();

    /** Map from jdoClass to an array of FOStoreTranscribers. */
    private final HashMap transcribers = new HashMap();

    /** Convenience; so that we don't have to getInstance() all the time. */
    // XXX We may want to rethink how transcribers are accessed.
    private final FOStoreTranscriberFactory transcriberFactory =
        FOStoreTranscriberFactory.getInstance();

    /** RuntimeJavaModelFactory. */
    private static final RuntimeJavaModelFactory javaModelFactory =
        (RuntimeJavaModelFactory) AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return RuntimeJavaModelFactory.getInstance();
                }
            }
        );

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(
        I18N.NAME, FOStoreModel.class.getClassLoader());

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N

    /** Constructor */
    FOStoreModel() { }

    /**
     * Provides the class id for the given class.
     * @param cls The class for which the corresponding class id is needed.
     * @return CLID for the given class, or null if there is no metadata for
     * that class.
     */
   CLID getCLID(Class cls) {
       // First check if cls has a well-known CLID.  If not, get  metadata for
       // cls and get the CLID for that.  If still no CLID, create one and map
       // it in the clids table.
       CLID rc = CLID.getKnownCLID(cls);
       if (null == rc) {
           synchronized(clids) {
               rc = (CLID)clids.get(cls);
               if (null == rc) {
                   rc = CLID.createProvisional();
                   clids.put(cls, rc);
                   if (logger.isDebugEnabled()) {
                       logger.debug(
                         "FOM.getCLID (new): " + cls + " -> " + rc); // NOI18N
                   }
               }
           }
       }
       return rc;
    }

    /**
    * Add a mapping from ClassMetaData to CLID.
    */
    synchronized void put(Class cls, CLID clid) {
        clids.put(cls, clid);
    }
    
    /**
    * Causes the given class mapped to an OID.
    * @param cls Class to be mapped.
    * @param type JDOIdentityType as an int.
    * @param pc PersistenceCapable instance to copy fields from if available. 
    * @param oid Object Id instance to copy fields from if available. 
    * @param pm PersistenceManagerInternal that requested the operation.
    * @param pmf FOStorePMF that requested the operation.
    * @return ObjectId corresponding to given class.
    */
    OID bind(Class cls, int type, PersistenceCapable pc, 
             Object oid, PersistenceManagerInternal pm, FOStorePMF pmf) {
        OID rc = null;
        switch (type) {
            case JDOIdentityType.APPLICATION:
                rc = AID.create(cls, pc, oid, pm, pmf);
                break;
            case JDOIdentityType.DATASTORE:
                CLID clid = getCLID(cls);
                rc = OID.create(clid);
                break;
            default:
                break;
        }
        return rc;
    }

    /**
     * Changes the class id by which this metadata is known.
     * @param pCLID The class id by which the class was previously known.
     * @param rCLID The class id by which the class should be known from now
     * on in this JVM.
     */
    void updateCLID(CLID pCLID, CLID rCLID) {
        Map.Entry entry = getEntry(pCLID);
        if (null != entry) {
            entry.setValue(rCLID);
            if (null == provisionalCLIDs.get(pCLID)) {
                Class cls = (Class)entry.getKey();
                provisionalCLIDs.put(pCLID, cls);
            }
        }
    }

    /**
    * Return the class corresponding to the given CLID.
    * @param clid The CLID for which a class is wanted.
    * @return The java.lang.Class corresponding to the given CLID, or null if
    * none is found.
    */
    Class getClass(CLID clid) {
        Class rc = null;
        Map.Entry entry = getEntry(clid);
        if (null != entry) {
            rc = (Class)entry.getKey();
        }

        // It is possible we did not find a mapping.  This can be the case if,
        // for example, 2 objects are created, and one is stored but not the
        // other.  The act of storing one will update the clids table via
        // updateCLID. So we have to look in this other table.
        if (null == rc && clid.isProvisional()) {
            rc = (Class)provisionalCLIDs.get(clid);
        }
        return rc;
    }


    // Provide the entry which represents the Class - CLID mapping.  While
    // this may seem time-wise expensive, we don't expect there to be all
    // *that* many classes (O(100)) per JVM.
    synchronized Map.Entry getEntry(CLID clid) {
        Map.Entry rc = null;
        Set entries = (Set)clids.entrySet();
        for (Iterator i = entries.iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry)i.next();
            CLID aCLID = (CLID)entry.getValue();
            if (clid.equals(aCLID)) {
                rc = entry;
                break;
            }
        }
        return rc;
    }

    //
    // Provide access to the JDOModel
    //

    /** @param c Class whose corresponding JDOClass is needed.
    * @return The JDOClass for the given class.
    */
    JDOClass getJDOClass(Class c) {
        if (logger.isDebugEnabled()) {
            logger.debug("FOM.getJDOClass for " + c.getName()); // NOI18N
        }
        return javaModelFactory.getJavaType(c).getJDOClass();
    }

    /** Provides a transcriber for the field in the given JDOClass indicated
    * by fieldNum.
    * @param c JDOClass for which a transcriber is needed.
    * @param fieldNum The absolute fieldNumber in the class modeled by
    * jdoClass that is to be transcribed.
    * @return a FOStoreTranscriber appropriate for the type of field in the
    * class modeled by field fieldNum in the class corresponding to
    * jdoClass.
    */
    FOStoreTranscriber getTranscriber(Class c, int fieldNum) {
        FOStoreTranscriber t[] =
            (FOStoreTranscriber[])transcribers.get(c);
        if (null == t) {
            // Create transcribers for jdoClass
            JDOClass jdoClass = getJDOClass(c);
            // Use managed fields here to preserve correct fieldNum's.
            JDOField fields[] = jdoClass.getManagedFields();
            int length = fields.length;
            t = new FOStoreTranscriber[length];

            for (int i = 0; i < length; i++) {
                if (fields[i].isPersistent()) {
                    t[i] = (FOStoreTranscriber)transcriberFactory.getTranscriber(
                        javaModelFactory.getJavaClass(fields[i].getType()))[0];
                } else {
                    t[i] = DummyTranscriber.getInstance();
                }
            }
            transcribers.put(c, t);
        }
        return t[fieldNum];
    }

    //
    // Debug support
    //
    
    void print(Object o) {
        print (o.getClass());
    }

    void print(Class c) {
        JDOModel m = null;
        try {
            ClassLoader cl = c.getClassLoader();
                m = javaModelFactory.getJavaModel(cl).getJDOModel();
            org.apache.jdo.impl.model.jdo.util.PrintSupport.printJDOModel(m);
        } catch (Exception ex) {
            System.out.println("Cannot print model"); // NOI18N
        }
    }
}
