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

import org.apache.jdo.state.FieldManager;

/**
* An implementation of FieldManager in which all methods throw a
* FOStoreAbstractMethodException with the name of the method invoked.
*
* @author Dave Bristor
*/
class AbstractFieldManager implements FieldManager {
    private static final String name = AbstractFieldManager.class.getName();

    /**
    * @see org.apache.jdo.state.FieldManager#storeBooleanField(int fieldNum,
    * boolean value)
    */
    public void storeBooleanField(int fieldNum, boolean value) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": storeBooleanField"); // NOI18N
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchBooleanField(int fieldNum)
    */
    public boolean fetchBooleanField(int fieldNum) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": fetchBooleanField"); // NOI18N
    }
    


    /**
    * @see org.apache.jdo.state.FieldManager#storeCharField(int fieldNum,
    * char value)
    */
    public void storeCharField(int fieldNum, char value) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": storeCharField"); // NOI18N
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchCharField(int fieldNum)
    */
    public char fetchCharField(int fieldNum) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": fetchCharField"); // NOI18N
    }
    


    /**
    * @see org.apache.jdo.state.FieldManager#storeByteField(int fieldNum,
    * byte value)
    */
    public void storeByteField(int fieldNum, byte value) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": storeByteField"); // NOI18N
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchByteField(int fieldNum)
    */
    public byte fetchByteField(int fieldNum) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": fetchByteField"); // NOI18N
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeShortField(int fieldNum,
    * short value)
    */
    public void storeShortField(int fieldNum, short value) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": storeShortField"); // NOI18N
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchShortField(int fieldNum)
    */
    public short fetchShortField(int fieldNum) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": fetchShortField"); // NOI18N
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeIntField(int fieldNum, int value)
    */
    public void storeIntField(int fieldNum, int value) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": storeIntField"); // NOI18N
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchIntField(int fieldNum)
    */
    public int fetchIntField(int fieldNum) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": fetchIntField"); // NOI18N
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeLongField(int fieldNum,
    * long value)
    */
    public void storeLongField(int fieldNum, long value) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": storeLongField"); // NOI18N
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchLongField(int fieldNum)
    */
    public long fetchLongField(int fieldNum) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": fetchLongField"); // NOI18N
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeFloatField(int fieldNum,
    * float value)
    */
    public void storeFloatField(int fieldNum, float value) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": storeFloatField"); // NOI18N
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchFloatField(int fieldNum)
    */
    public float fetchFloatField(int fieldNum) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": fetchFloatField"); // NOI18N
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeDoubleField(int fieldNum,
    * double value)
    */
    public void storeDoubleField(int fieldNum, double value) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": storeDoubleField"); // NOI18N
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchDoubleField(int fieldNum)
    */
    public double fetchDoubleField(int fieldNum) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": fetchDoubleField"); // NOI18N
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeStringField(int fieldNum,
    * String value)
    */
    public void storeStringField(int fieldNum, String value) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": storeStringField"); // NOI18N
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchStringField(int fieldNum)
    */
    public String fetchStringField(int fieldNum) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": fetchStringField"); // NOI18N
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeObjectField(int fieldNum,
    * Object value)
    */
    public void storeObjectField(int fieldNum, Object value) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": storeObjectField"); // NOI18N
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchObjectField(int fieldNum)
    */
    public Object fetchObjectField(int fieldNum) {
        throw new FOStoreAbstractMethodException(
            name + " " + fieldNum + ": fetchObjectField"); // NOI18N
    }
}
