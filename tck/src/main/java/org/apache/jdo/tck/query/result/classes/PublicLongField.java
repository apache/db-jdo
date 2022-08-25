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
 
package org.apache.jdo.tck.query.result.classes;

/**
 * JDOQL result class having a public lonf field.
 */
public class PublicLongField {

    public long l;

    public PublicLongField() {}

    public PublicLongField(long l) {
        this.l = l;
    }
    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        return (int) (this.l % Integer.MAX_VALUE);
    }
    
    /**
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (!(o instanceof PublicLongField))
            return false;
        PublicLongField other = (PublicLongField) o;
        return this.l == other.l;
    }
    
    /**
     * @see Object#toString()
     */
    public String toString() {
        return getClass().getName() + '(' + this.l + ')';
    }

}
