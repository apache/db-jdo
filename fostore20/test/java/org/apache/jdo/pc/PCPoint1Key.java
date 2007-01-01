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

package org.apache.jdo.pc;

import java.io.Serializable;

/**
* A simple key class with one field
*
* @author Marina Vatkina
*/
public class PCPoint1Key implements Serializable {
    public int x;

    static final Class cls = org.apache.jdo.pc.PCPoint1.class;

    public PCPoint1Key() {}

    //public PCPoint1Key(String s) { x = Integer.parseInt(s); }

    public String toString() { return this.getClass().getName() + ": "  + x;}

    public int hashCode() { return x ; }

    public boolean equals(Object other) {
        if (other instanceof PCPoint1Key) {
            PCPoint1Key k = (PCPoint1Key)other;
            return k.x == this.x;
        }
        return false;
    }
}
