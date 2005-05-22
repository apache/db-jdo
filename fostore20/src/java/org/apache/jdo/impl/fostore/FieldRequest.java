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

/**
 * Represents a request to manipulate (read or update) the fields of an
 * object.
 *
 * @author Dave Bristor
 */
interface FieldRequest extends Request {
    /**
      * Indicates which fields are to be manipulated in the object.
      * @param fieldNums The set of field numbers indicating the fields that
      * are to be manipulated.
      */
    public void setFieldNums(int fieldNums[]);

    /**
      * Adds to the set of fields that are to be manipulated.
      * @param fieldNum Number of the field to be manipulated.
      */
    public void addFieldNum(int fieldNum);
}
