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

package org.apache.jdo.state;

/**
 * This is the means by which a StateManager implementation's giveXXXField()
 * method (where XXX is e.g. Int) can give the value to an object that wants
 * the field.
 *
 * @author Dave Bristor
 */
public interface FieldManager {
    /**
     * Provides the means by which the value of a boolean field can be given
     * by a StateManager to an object that needs the value.
     * @param fieldNum Field number of the field in the object whose value is
     * given.
     * @param value Boolean that is the value of a particular field.
     */
    public void storeBooleanField(int fieldNum, boolean value);

    public boolean fetchBooleanField(int fieldNum);
    
    /**
     * Provides the means by which the value of a char field can be given
     * by a StateManager to an object that needs the value.
     * @param fieldNum Field number of the field in the object whose value is
     * given.
     * @param value Char that is the value of a particular field.
     */
    public void storeCharField(int fieldNum, char value);

    public char fetchCharField(int fieldNum);
    
    /**
     * Provides the means by which the value of a byte field can be given
     * by a StateManager to an object that needs the value.
     * @param fieldNum Field number of the field in the object whose value is
     * given.
     * @param value Byte that is the value of a particular field.
     */
    public void storeByteField(int fieldNum, byte value);

    public byte fetchByteField(int fieldNum);

    /**
     * Provides the means by which the value of a short field can be given
     * by a StateManager to an object that needs the value.
     * @param fieldNum Field number of the field in the object whose value is
     * given.
     * @param value Short that is the value of a particular field.
     */
    public void storeShortField(int fieldNum, short value);

    public short fetchShortField(int fieldNum);

    /**
     * Provides the means by which the value of a int field can be given
     * by a StateManager to an object that needs the value.
     * @param fieldNum Field number of the field in the object whose value is
     * given.
     * @param value Int that is the value of a particular field.
     */
    public void storeIntField(int fieldNum, int value);

    public int fetchIntField(int fieldNum);

    /**
     * Provides the means by which the value of a long field can be given
     * by a StateManager to an object that needs the value.
     * @param fieldNum Field number of the field in the object whose value is
     * given.
     * @param value Long that is the value of a particular field.
     */
    public void storeLongField(int fieldNum, long value);

    public long fetchLongField(int fieldNum);

    /**
     * Provides the means by which the value of a  field can be given
     * by a StateManager to an object that needs the value.
     * @param fieldNum Field number of the field in the object whose value is
     * given.
     * @param value  that is the value of a particular field.
     */
    public void storeFloatField(int fieldNum, float value);

    public float fetchFloatField(int fieldNum);

    /**
     * Provides the means by which the value of a double field can be given
     * by a StateManager to an object that needs the value.
     * @param fieldNum Field number of the field in the object whose value is
     * given.
     * @param value Double that is the value of a particular field.
     */
    public void storeDoubleField(int fieldNum, double value);

    public double fetchDoubleField(int fieldNum);

    /**
     * Provides the means by which the value of a String field can be given
     * by a StateManager to an object that needs the value.
     * @param fieldNum Field number of the field in the object whose value is
     * given.
     * @param value String that is the value of a particular field.
     */
    public void storeStringField(int fieldNum, String value);

    public String fetchStringField(int fieldNum);

    /**
     * Provides the means by which the value of an Object field can be given
     * by a StateManager to an object that needs the value.
     * @param fieldNum Field number of the field in the object whose value is
     * given.
     * @param value Object that is the value of a particular field.
     */
    public void storeObjectField(int fieldNum, Object value);

    public Object fetchObjectField(int fieldNum);

}
