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

import java.io.IOException;

import javax.jdo.JDOUserException;
import javax.jdo.spi.PersistenceCapable;

import org.apache.jdo.model.java.JavaField;
import org.apache.jdo.model.java.JavaType;
import org.apache.jdo.model.jdo.JDOClass;
import org.apache.jdo.model.jdo.JDOField;
import org.apache.jdo.util.I18NHelper;


/**
 *
 * @author Marina Vatkina
 */
class AIDTranscriber 
    implements PersistenceCapable.ObjectIdFieldConsumer {

    // Streams to use
    private FOStoreOutput out;

    // Metadata for the request.
    private final FOStoreModel model;
    private final Class cls;

    /** I18N support. */
    private static final I18NHelper msg = I18NHelper.getInstance(I18N.NAME);

    AIDTranscriber(FOStoreOutput out, Class pcClass, FOStorePMF pmf) {
        this.out = out;
        cls = pcClass;
        model = pmf.getModel();
    }

    /** Store one field into the field manager.  This field was retrieved from
     * the field of the ObjectId.
     * @param fieldNumber the field number of the key field.
     * @param value the value of the field from the ObjectId.
     */
    public void storeBooleanField(int fieldNumber, boolean value){
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNumber);
        try {
            t.storeBoolean(value, out);
        } catch (IOException ex) {
            throw new FOStoreFatalIOException(
                this.getClass(), "storeBooleanField", ex); // NOI18N
        }
    }

    /** Store one field into the field manager.  This field was retrieved from
     * the field of the ObjectId.
     * @param fieldNumber the field number of the key field.
     * @param value the value of the field from the ObjectId.
     */
    public void storeCharField(int fieldNumber, char value){
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNumber);
        try {
            t.storeChar(value, out);
        } catch (IOException ex) { 
            throw new FOStoreFatalIOException(
                this.getClass(), "storeCharField", ex); // NOI18N 
        }
    }

    /** Store one field into the field manager.  This field was retrieved from
     * the field of the ObjectId.
     * @param fieldNumber the field number of the key field.
     * @param value the value of the field from the ObjectId.
     */
    public void storeByteField(int fieldNumber, byte value){
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNumber);
        try {
            t.storeByte(value, out);
        } catch (IOException ex) { 
            throw new FOStoreFatalIOException(
                this.getClass(), "storeByteField", ex); // NOI18N 
        }
    }

    /** Store one field into the field manager.  This field was retrieved from
     * the field of the ObjectId.
     * @param fieldNumber the field number of the key field.
     * @param value the value of the field from the ObjectId.
     */
    public void storeShortField(int fieldNumber, short value){
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNumber);
        try {
            t.storeShort(value, out);
        } catch (IOException ex) { 
            throw new FOStoreFatalIOException(
                this.getClass(), "storeShortField", ex); // NOI18N 
        }
    }

    /** Store one field into the field manager.  This field was retrieved from
     * the field of the ObjectId.
     * @param fieldNumber the field number of the key field.
     * @param value the value of the field from the ObjectId.
     */
    public void storeIntField(int fieldNumber, int value){
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNumber);
        try {
            t.storeInt(value, out);
        } catch (IOException ex) { 
            throw new FOStoreFatalIOException(
                this.getClass(), "storeIntField", ex); // NOI18N 
        }
    }

    /** Store one field into the field manager.  This field was retrieved from
     * the field of the ObjectId.
     * @param fieldNumber the field number of the key field.
     * @param value the value of the field from the ObjectId.
     */
    public void storeLongField(int fieldNumber, long value){
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNumber);
        try {
            t.storeLong(value, out);
        } catch (IOException ex) { 
            throw new FOStoreFatalIOException(
                this.getClass(), "storeLongField", ex); // NOI18N 
        }
    }

    /** Store one field into the field manager.  This field was retrieved from
     * the field of the ObjectId.
     * @param fieldNumber the field number of the key field.
     * @param value the value of the field from the ObjectId.
     */
    public void storeFloatField(int fieldNumber, float value){
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNumber);
        try {
            t.storeFloat(value, out);
        } catch (IOException ex) { 
            throw new FOStoreFatalIOException(
                this.getClass(), "storeFloatField", ex); // NOI18N 
        }
    }

    /** Store one field into the field manager.  This field was retrieved from
     * the field of the ObjectId.
     * @param fieldNumber the field number of the key field.
     * @param value the value of the field from the ObjectId.
     */
    public void storeDoubleField(int fieldNumber, double value) {
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNumber);
        try {
            t.storeDouble(value, out);
        } catch (IOException ex) { 
            throw new FOStoreFatalIOException(
                this.getClass(), "storeDoubleField", ex); // NOI18N 
        }
    }

    /** Store one field into the field manager.  This field was retrieved from
     * the field of the ObjectId.
     * @param fieldNumber the field number of the key field.
     * @param value the value of the field from the ObjectId.
     */
    public void storeStringField(int fieldNumber, String value) {
        assertNotNull(fieldNumber, value);
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNumber);
        try {
            t.storeObject(value, out, null);
        } catch (IOException ex) { 
            throw new FOStoreFatalIOException(
                this.getClass(), "storeStringField", ex); // NOI18N 
        }
    }

    /** Store one field into the field manager.  This field was retrieved from
     * the field of the ObjectId.
     * @param fieldNumber the field number of the key field.
     * @param value the value of the field from the ObjectId.
     */
    public void storeObjectField(int fieldNumber, Object value) {
        assertNotNull(fieldNumber, value);
        FOStoreTranscriber t = model.getTranscriber(cls, fieldNumber);
        try {
            t.storeObject(value, out, null); // no need have pm here.
        } catch (IOException ex) { 
            throw new FOStoreFatalIOException(
                this.getClass(), "storeObjectField", ex); // NOI18N 
        }
    }
    
    void assertNotNull(int fieldNumber, Object value) {
        if (value == null) {
            JDOClass jdoClass = model.getJDOClass(cls);
            JavaType javaType = jdoClass.getJavaType();
            String className = javaType.getName();
            JDOField jdoField = jdoClass.getField(fieldNumber);
            JavaField javaField = jdoField.getJavaField();
            String fieldName = javaField.getName();
            throw new NullPointerException(
                msg.msg("EXC_ObjectIdKeyFieldNull", className, fieldName));
        }
    }
        
}
