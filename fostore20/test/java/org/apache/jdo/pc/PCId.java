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

public class PCId implements Comparable {
    private int id;
    private static int count = 0;

    public PCId() {
        id = 100 + count;
        count++;
    }

    public int hashCode() {
        return id;
    }

    public boolean equals(Object o) {
        boolean rc = false;
        if (null != o && o instanceof PCId) {
            PCId other = (PCId)o;
            rc = (id == other.id);
        }
        return rc;
    }

    public String toString() {
        return "PCId=" + id;
    }
    
    public int compareTo(Object obj) {
        if (obj == null) {
            throw new RuntimeException("Null cannot be compared.");
        }
        if (obj instanceof PCId) {
            return id - ((PCId)obj).id;
        } else {
            throw new RuntimeException("Object of class " 
            + obj.getClass().getName() 
            + " cannot be compared to an instance of PCId.");
        }
    }
    
}
