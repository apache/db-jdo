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
 * JDOQL result class not having a no-args constructor.
 */
public class MissingNoArgsConstructor {

    private long l;
    
    public MissingNoArgsConstructor(int l) {
        this.l = l;
    }
    
    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        int result = 0;
        result += (int) (this.l % Integer.MAX_VALUE);
        return result;
    }
    
    /**
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (!(o instanceof MissingNoArgsConstructor))
            return false;
        MissingNoArgsConstructor other = (MissingNoArgsConstructor) o;
        return this.l == other.l;
    }
    
    /**
     * @see Object#toString()
     */
    public String toString() {
        return getClass().getName() + '(' + l + ')';
    }

    /**
     * @return Returns the l.
     */
    public long getL() {
        return l;
    }

    /**
     * @param l The l to set.
     */
    public void setL(int l) {
        this.l = l;
    }
}
