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

/*
 * ObjectIdentity.java
 *
 */
 
package javax.jdo.identity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.jdo.JDOUserException;

import javax.jdo.spi.JDOImplHelper;

/** This class is for identity with a single Object type field.
 * @version 2.0
 */
public class ObjectIdentity extends SingleFieldIdentity {
    
    /** The key is stored in the superclass field keyAsObject.
     */
    
    /** The JDOImplHelper instance used for parsing the String to an Object.
     */
    private static JDOImplHelper helper = (JDOImplHelper)
        AccessController.doPrivileged(
            new PrivilegedAction () {
                public Object run () {
                    return JDOImplHelper.getInstance();
                }
            }
        );
    
    /** The delimiter for String constructor.
     */
    private static final String STRING_DELIMITER = ":"; //NOI18N
    
    /** Constructor with class and key.
     * @param pcClass the class
     * @param param the key
     */
    public ObjectIdentity (Class pcClass, Object param) {
        super (pcClass);
        assertKeyNotNull(param);
        String paramString = null;
        String keyString = null;
        String className = null;
        if (param instanceof String) {
            /* The paramString is of the form "<className>:<keyString>" */
            paramString = (String)param;
            if (paramString.length() < 3) {
                throw new JDOUserException(
                    msg.msg("EXC_ObjectIdentityStringConstructionTooShort") + //NOI18N
                    msg.msg("EXC_ObjectIdentityStringConstructionUsage", //NOI18N
                        paramString));
            }
            int indexOfDelimiter = paramString.indexOf(STRING_DELIMITER);
            if (indexOfDelimiter < 0) {
                throw new JDOUserException(
                    msg.msg("EXC_ObjectIdentityStringConstructionNoDelimiter") + //NOI18N
                    msg.msg("EXC_ObjectIdentityStringConstructionUsage", //NOI18N
                        paramString));
            }
            keyString = paramString.substring(indexOfDelimiter+1);
            className = paramString.substring(0, indexOfDelimiter);
            keyAsObject = helper.construct(className, keyString);
        } else {
            keyAsObject = param;
        }
        hashCode = hashClassName() ^ keyAsObject.hashCode();
    }

    /** Constructor only for Externalizable.
     */
    public ObjectIdentity () {
    }

    /** Return the key.
     * @return the key
     */
    public Object getKey () {
        return keyAsObject;
    }

    /** Return the String form of the object id. The class of the
     * object id is written as the first part of the result so that
     * the class can be reconstructed later. Then the toString
     * of the key instance is appended. During construction, 
     * this process is reversed. The class is extracted from 
     * the first part of the String, and the String constructor
     * of the key is used to construct the key itself.
     * @return the String form of the key
     */
    public String toString () {
        return keyAsObject.getClass().getName()
                + STRING_DELIMITER
                + keyAsObject.toString();
    }

    /** Determine if the other object represents the same object id.
     * @param obj the other object
     * @return true if both objects represent the same object id
     */
    public boolean equals (Object obj) {
        if (this == obj) {
            return true;
        } else if (!super.equals (obj)) {
            return false;
        } else {
            ObjectIdentity other = (ObjectIdentity) obj;
            return keyAsObject.equals(other.keyAsObject);
        }
    }

    /** Determine the ordering of identity objects.
     * @param o Other identity
     * @return The relative ordering between the objects
     * @since 2.2
     */
    public int compareTo(Object o) {
        if (o instanceof ObjectIdentity) {
        	ObjectIdentity other = (ObjectIdentity)o;
            int result = super.compare(other);
            if (result == 0) {
                if (other.keyAsObject instanceof Comparable && 
                        keyAsObject instanceof Comparable) {
                    return ((Comparable)keyAsObject).compareTo(
                            (Comparable)other.keyAsObject);
                }
                else
                {
                    throw new ClassCastException("The key class (" + 
                            keyAsObject.getClass().getName() + 
                            ") does not implement Comparable");
                }
            } else {
                return result;
            }
        }
        else if (o == null) {
            throw new ClassCastException("object is null");
        }
        throw new ClassCastException(this.getClass().getName() + 
                " != " + o.getClass().getName());
    }

    /** Write this object. Write the superclass first.
     * @param out the output
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal (out);
        out.writeObject(keyAsObject);
    }

    /** Read this object. Read the superclass first.
     * @param in the input
     */
    public void readExternal(ObjectInput in)
		throws IOException, ClassNotFoundException {
        super.readExternal (in);
        keyAsObject = in.readObject();
    }
    
}
