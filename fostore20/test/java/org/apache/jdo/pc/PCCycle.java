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

// Test graph with cycles: each class contains a member of the other class.
//


/**
* A class whose instances refer to instances of this class, creating a cycle.
*
* @author Dave Bristor
*/
public class PCCycle {
    public String name;
    public PCCycle2 c2;

    public PCCycle() { }

    public void init(String name, PCCycle2 c2) {
        this.name = name;
        this.c2 = c2;
    }

    PCCycle2 getC2() {
        return c2;
    }

    public String toString(PCCycle2 c2) {
        String rc = "loop!";
        if (this.c2 != c2) {
            rc = toString();
        }
        return rc;
    }
    
    public String toString() {
        String rc = null;
        try {
            rc = this.getClass().getName()
                + " name=" + name;
            try {
                // Compare w/ PCCycle2.toString()
                rc += " c2='" + getC2().toString() + "'";
            } catch (NullPointerException ex) {
                ex.printStackTrace(System.err);
                rc += " c2=<NPE>";
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace(System.err);
            rc = "PCCycle has no values";
        }
        return rc;
    }
}
