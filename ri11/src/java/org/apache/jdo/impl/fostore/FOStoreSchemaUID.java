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

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.DigestOutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;

import java.util.HashMap;

import javax.jdo.JDOFatalInternalException;
import javax.jdo.JDOUserException;
import javax.jdo.JDOFatalUserException;
import javax.jdo.spi.JDOImplHelper;
import javax.jdo.spi.PersistenceCapable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.jdo.util.I18NHelper;

/**
* A FOStoreSchemaUID is an internal name used to distinguish persistence
* capable classes based on the structure of their persistent fields as
* indicated by the enhancer.  This is very similar to, and the implementation
* is based on, the process of computing a serialVersionUID as described in 
* <a href="http://java.sun.com/products/jdk/1.2/docs/guide/serialization/spec/class.doc4.html"> Object Serialization Specification, Section 4.4, Stream Unique Identifiers</a>. // NOI18N
*
* @author Dave Bristor
*/
class FOStoreSchemaUID {
    /** The 'value' of this FOStoreSchemaUID */
    private final long fsuid;
    
    /** Map from ClassLoader to a HashMap that in turn maps from Class to
    * FOStoreSchemaUID.
    */
    private static final HashMap loaderMap = new HashMap();

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    /** Logger */
    static final Log logger = LogFactory.getFactory().getInstance(
        "org.apache.jdo.impl.fostore"); // NOI18N

    private static final FOStoreSchemaUID NOT_PERSISTENT =
        new FOStoreSchemaUID(0);

    /** JDOImplHelper instance */
    private static final JDOImplHelper helper = 
        (JDOImplHelper) AccessController.doPrivileged (
            new PrivilegedAction () {
                public Object run () {
                    try {
                        return JDOImplHelper.getInstance();
                    }
                    catch (SecurityException e) {
                        throw new JDOFatalUserException (msg.msg(
                            e.getMessage()), e); // NOI18N
                    }
                }
            }    
        );

    private FOStoreSchemaUID(long value) {
        this.fsuid = value;
    }
    
    private FOStoreSchemaUID(Class cls) {
        this.fsuid = computeUID(cls);
    }

    /** Provides a FOStoreSchemaUID corresponding to the given class.  Any
     *  class which
     * <ul>
     * <li>implements PersistenceCapable and</li>
     * <li>is itself enhanced</li>
     *</ul>
     * has a distinct FOStoreSchemaUID.  All other classes share a common
     * FOStoreSchemaUID. (A class can be PersistenceCapable but not enhanced
     * by virtue of being in an inheritance chain, in which a class which does
     * not have a JDO model inherits a class that does have a JDO model.)
     * <em>This should only be invoked by client code, never by server code,
     * because it involves client-side concepts such as
     * PersistenceCapable.</em> 
    * @param cls Class for which a FOStoreSchemaUID is needed.
    * @param model FOStoreModel for determining whether cls was enhanced.
    * @return FOStoreSchemaUID corresponding to given Class.
    */
    static synchronized FOStoreSchemaUID lookup(Class cls, FOStoreModel model) {
        FOStoreSchemaUID rc = null;
        if (! PersistenceCapable.class.isAssignableFrom(cls) ||
               model.getJDOClass(cls) == null) {
            rc = NOT_PERSISTENT;
        } else {        
            HashMap classMap = (HashMap)loaderMap.get(cls.getClassLoader());
            if (null == classMap) {
                rc = new FOStoreSchemaUID(cls);
                classMap = new HashMap();
                classMap.put(cls, rc);
                loaderMap.put(cls.getClassLoader(), classMap);
            } else {
                rc = (FOStoreSchemaUID)classMap.get(cls);
                if (null == rc) {
                    rc = new FOStoreSchemaUID(cls);
                    classMap.put(cls, rc);
                }
            }
        }
        return rc;
    }

    private long computeUID(Class cls) {
        long rc = 0;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA"); // NOI18N
            ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
            DigestOutputStream mdo = new DigestOutputStream(baos, md);
            DataOutputStream data = new DataOutputStream(mdo);

            computeUID(cls, data);
     
            // Compute the hash value for cls
            data.flush();
            byte hasharray[] = md.digest();
            for (int i = 0; i < Math.min(8, hasharray.length); i++) {
                rc += (long)(hasharray[i] & 255) << (i * 8);
            }
            
        } catch (IOException ex) {
            // Can't happen, but be deterministic anyway
            rc = -1;
        } catch (NoSuchAlgorithmException ex) {
            throw new JDOFatalInternalException(
                msg.msg("ERR_Algorithm"), ex); // NOI18N
        }
        return rc;
    }

    private void computeUID(Class cls, DataOutputStream out)
        throws IOException {

        if (logger.isDebugEnabled()) {
            logger.debug("FSUID.computeUID: " + cls.getName()); // NOI18N
        }
        
        out.writeUTF(cls.getName());

        // We don't need to sort these as the enhancer will have done that.
        String names[] = helper.getFieldNames(cls);
        Class types[] = helper.getFieldTypes(cls);
        byte flags[] = helper.getFieldFlags(cls);
        int length = names.length;

        for (int i = 0; i < length; i++) {
            out.writeUTF(names[i]);
            out.writeUTF(types[i].getName());
            out.writeByte(flags[i]);
        }

        Class spr = helper.getPersistenceCapableSuperclass(cls);
        if (null != spr) {
            if (logger.isDebugEnabled()) {
                logger.debug("FSUID: spr for " + cls.getName() + // NOI18N
                           " is " + cls.getName()); // NOI18N
            }
            computeUID(spr, out);
        }
    }

    public String toString() {
        return "" + this.fsuid; // NOI18N
    }

    //
    // Support for I/O
    //

    void write(DataOutput out) throws IOException {
        out.writeLong(fsuid);
    }

    static FOStoreSchemaUID read(DataInput in) throws IOException {
        return new FOStoreSchemaUID(in.readLong());
    }

    //
    // Support for equality
    //

    public boolean equals(Object o) {
        boolean rc = false;
        if ((null != o) && (o instanceof FOStoreSchemaUID)) {
            rc = (fsuid == ((FOStoreSchemaUID)o).fsuid);
        }
        return rc;
    }

    /**
     * Computes a hashcode for this FOStoreSchemaUID. The result is the
     * exclusive OR of the two halves of the primitive <code>long</code>
     * value represented by this <code>Long</code> object. That is, the
     * hashcode is the value of the expression:
     * <blockquote><pre>
     * (int)(this.longValue()^(this.longValue()>>>32))
     * </pre></blockquote>
     * This is the same algoritm as is used for java.lang.Long.
     *
     * @return  a hash code value for this object.
     */
    public int hashCode() {
        // Same as for java.lang.Long
        return (int)(fsuid ^ (fsuid >> 32));
    }
}
