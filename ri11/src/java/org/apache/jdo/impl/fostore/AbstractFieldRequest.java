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

import org.apache.jdo.state.FieldManager;
import org.apache.jdo.state.StateManagerInternal;

/**
* This is an AbstractRequest that uses an exception-throwing implementation
* FieldManager to implement that interface.  Subclasses can override
* particular FieldManager methods they need.  E.g., a subclass might override
* only the storeABCField methods, knowing that its fetchABCField methods will
* never be invoked.
*
* @author Dave Bristor
*/
abstract class AbstractFieldRequest
    extends AbstractRequest implements FieldManager {

    private AbstractFieldManager afm = new AbstractFieldManager();

    AbstractFieldRequest(StateManagerInternal  sm, Message m, FOStorePMF pmf) {
        super(sm, m, pmf);
    }


    /**
    * @see org.apache.jdo.state.FieldManager#storeBooleanField(int fieldNum,
    * boolean value)
    */
    public void storeBooleanField(int fieldNum, boolean value) {
        afm.storeBooleanField(fieldNum, value);
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchBooleanField(int fieldNum)
    */
    public boolean fetchBooleanField(int fieldNum) {
        return afm.fetchBooleanField(fieldNum);
    }
    


    /**
    * @see org.apache.jdo.state.FieldManager#storeCharField(int fieldNum,
    * char value)
    */
    public void storeCharField(int fieldNum, char value) {
        afm.storeCharField(fieldNum, value);
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchCharField(int fieldNum)
    */
    public char fetchCharField(int fieldNum) {
        return afm.fetchCharField(fieldNum);
    }
    


    /**
    * @see org.apache.jdo.state.FieldManager#storeByteField(int fieldNum,
    * byte value)
    */
    public void storeByteField(int fieldNum, byte value) {
        afm.storeByteField(fieldNum, value);
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchByteField(int fieldNum)
    */
    public byte fetchByteField(int fieldNum) {
        return afm.fetchByteField(fieldNum);
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeShortField(int fieldNum,
    * short value)
    */
    public void storeShortField(int fieldNum, short value) {
        afm.storeShortField(fieldNum, value);
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchShortField(int fieldNum)
    */
    public short fetchShortField(int fieldNum) {
        return afm.fetchShortField(fieldNum);
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeIntField(int fieldNum, int value)
    */
    public void storeIntField(int fieldNum, int value) {
        afm.storeIntField(fieldNum, value);
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchIntField(int fieldNum)
    */
    public int fetchIntField(int fieldNum) {
        return afm.fetchIntField(fieldNum);
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeLongField(int fieldNum,
    * long value)
    */
    public void storeLongField(int fieldNum, long value) {
        afm.storeLongField(fieldNum, value);
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchLongField(int fieldNum)
    */
    public long fetchLongField(int fieldNum) {
        return afm.fetchLongField(fieldNum);
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeFloatField(int fieldNum,
    * float value)
    */
    public void storeFloatField(int fieldNum, float value) {
        afm.storeFloatField(fieldNum, value);
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchFloatField(int fieldNum)
    */
    public float fetchFloatField(int fieldNum) {
        return afm.fetchFloatField(fieldNum);
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeDoubleField(int fieldNum,
    * double value)
    */
    public void storeDoubleField(int fieldNum, double value) {
        afm.storeDoubleField(fieldNum, value);
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchDoubleField(int fieldNum)
    */
    public double fetchDoubleField(int fieldNum) {
        return afm.fetchDoubleField(fieldNum);
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeStringField(int fieldNum,
    * String value)
    */
    public void storeStringField(int fieldNum, String value) {
        afm.storeStringField(fieldNum, value);
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchStringField(int fieldNum)
    */
    public String fetchStringField(int fieldNum) {
        return afm.fetchStringField(fieldNum);
    }



    /**
    * @see org.apache.jdo.state.FieldManager#storeObjectField(int fieldNum,
    * Object value)
    */
    public void storeObjectField(int fieldNum, Object value) {
        afm.storeObjectField(fieldNum, value);
    }

    /**
    * @see org.apache.jdo.state.FieldManager#fetchObjectField(int fieldNum)
    */
    public Object fetchObjectField(int fieldNum) {
        return afm.fetchObjectField(fieldNum);
    }
}
