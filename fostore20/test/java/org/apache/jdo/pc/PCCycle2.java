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

package org.apache.jdo.pc;

// Test graph with cycles: each class contains a member of the other class.
//

/**
* A class whose instances refer to instances of this class, creating a cycle.
*
* @author Dave Bristor
*/
public class PCCycle2 {
    public int id;
    public PCCycle c;

    public PCCycle2() { }

    public void init (int id, PCCycle c) {
        this.id = id;
        this.c = c;
    }

    PCCycle getC() {
        return c;
    }

    public String toString() {
        String rc = null;
        try {
            rc = this.getClass().getName()
                + " id=" + id;
            try {
                // Compare w/ PCCycle.toString()
                rc += " c='" + getC().toString(this) + "'";
            } catch (NullPointerException ex) {
                ex.printStackTrace(System.err);
                rc += " c=<NPE>";
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace(System.err);
            rc = "PCCycle has no values";
        }
        return rc;
    }
}
