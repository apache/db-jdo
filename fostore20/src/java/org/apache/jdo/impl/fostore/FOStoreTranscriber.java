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

import org.apache.jdo.pm.PersistenceManagerInternal;
import org.apache.jdo.store.Transcriber;


//
// A note on Strings and Objects
//
// The JDO spec calls out special treatment for primitives and Strings,
// lumping everything else into the category "Object".  For example,
// StateManager has providedStringField().  So, one would think this singling
// out of String as a "primitive Object" would continue here.
//
// We have not done so.  The reason is Collections.  Primitives are never
// elements of Collections, only Objects.  Strings are Objects.  To treat
// them as primitives would require special treatment in each of the
// collection transcribers.  Rather than do that, we treat Strings as
// Objects.  Eventually, Strings get the same treatment as other immutable
// types such as Integer; see ImmutableStringTranscriber.
//

/**
* FOStoreTranscriber contains methods to transcribe each primitive type,
* but they all throw AbstractMethodError.  Subclasses do the actual
* work, implementing methods for one and only one type of data.
*
* @author Dave Bristor
*/
abstract class FOStoreTranscriber implements Transcriber {
    // See ObjectTranscriber's description of getInstance() method.
    static final ObjectTranscriber objectTranscriber =
        new ObjectTranscriber();

    //
    // Transcriber methods.  All are quasi-abstract: they are intended to be
    // overridden.
    //
    
    void storeBoolean(boolean value, DataOutput out) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".storeBoolean"); // NOI18N
    }

    boolean fetchBoolean(DataInput in) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".fetchBoolean"); // NOI18N
    }

    
    void storeChar(char value, DataOutput out) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".storeChar"); // NOI18N
    }

    char fetchChar(DataInput in) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".fetchChar"); // NOI18N
    }

    
    void storeByte(byte value, DataOutput out) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".storeByte"); // NOI18N
    }

    byte fetchByte(DataInput in) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".fetchByte"); // NOI18N
    }

    
    void storeShort(short value, DataOutput out) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".storeShort"); // NOI18N
    }

    short fetchShort(DataInput in) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".fetchShort"); // NOI18N
    }

    
    void storeInt(int value, DataOutput out) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".storeInt"); // NOI18N
    }

    int fetchInt(DataInput in) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".fetchInt"); // NOI18N
    }

    
    void storeLong(long value, DataOutput out) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".storeLong"); // NOI18N
    }

    long fetchLong(DataInput in) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".fetchLong"); // NOI18N
    }

    
    void storeFloat(float value, DataOutput out) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".storeFloat"); // NOI18N
    }

    float fetchFloat(DataInput in) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".fetchFloat"); // NOI18N
    }

    
    void storeDouble(double value, DataOutput out) throws IOException {
        throw new FOStoreAbstractMethodException(getClass().getName() +
                                        ".storeDouble"); // NOI18N
    }

    double fetchDouble(DataInput in) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".fetchDouble"); // NOI18N
    }

    void skip(DataInput in) throws IOException {
        throw new FOStoreAbstractMethodException(
            getClass().getName() + ".skip"); // NOI18N
    }


    //
    // Note that the signatures and visibilty of the Object-related
    // transcribing methods differ from the above.
    //
    // The publicly visible storeObject requires a PersistenceManagerInternal
    // parameter, so that we can get its StoreManager and an OIDs
    // corresponding to a Java objects.
    //
    // The signature of fetchObject takes Object and int parameters so that
    // we can support SCO's, which must be created with an owner and field
    // number.  It takes a PM so that it can create an object from an OID.
    //

    int[] storeObject(Object value, FOStoreOutput out,
                      PersistenceManagerInternal pm) throws IOException {
                          throw new FOStoreAbstractMethodException(
                              getClass().getName() + ".storeObject"); // NOI18N
    }

    protected int[] storeObject(Object value, FOStoreOutput out)
        throws IOException {

            throw new FOStoreAbstractMethodException(
                getClass().getName() + ".storeObject"); // NOI18N
    }

    Object fetchObject(DataInput in, Object owner, int fieldNum,
                       PersistenceManagerInternal pm)
        throws IOException, Exception {

            throw new FOStoreAbstractMethodException(
                getClass().getName() + ".fetchObject"); // NOI18N
    }

    protected Object fetchObject(DataInput in, Object owner, int fieldNum)
        throws IOException, Exception {

            throw new FOStoreAbstractMethodException(
                getClass().getName() + ".fetchObject"); // NOI18N
    }
}
