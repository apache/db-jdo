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

/**
* A simple class with one field that's an array.  Make it a big one, and
* verify() it's contents.
*
* @author Dave Bristor
*/
public class PCLargeObj {
    public int x[];

    public PCLargeObj() { }

    public PCLargeObj(int length) {
        this.x = new int[length];
        verify(true);
    }

    public void init(int size) {
        this.x = new int[size];
        verify(true);
    }

    // If create is true, then put elements into x and return true; otherwise
    // verify that the correct elements are there returning true or false as
    // appropriate.
    boolean verify(boolean create) {
        boolean rc = true;
        int length = x.length;
        for (int i = 0; i < length; i++) {
            if (create) {
                x[i] = i;
            } else if (x[i] != i) {
                rc = false;
                break;
            }
        }
        return rc;
    }

    public String toString() {
        String rc = null;
        try {
            rc = this.getClass().getName() + name();
        } catch (NullPointerException ex) {
            rc = "PCPCLargeObj has no values";
        }
        return rc;
    }

    public String name() {
        return " length=" + x.length;
    }
}
