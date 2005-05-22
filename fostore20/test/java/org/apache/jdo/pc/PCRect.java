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

import org.apache.jdo.test.util.Util;

/**
* Test for instances containing instances of a user-defined class.
*
* @author Dave Bristor
*/
public class PCRect {
    public PCPoint upperLeft;
    public PCPoint lowerRight;

    public PCRect() { }

    public PCRect(PCPoint ul, PCPoint lr) {
        upperLeft = ul;
        lowerRight = lr;
    }

    public PCPoint getUpperLeft() {
        return upperLeft;
    }

    public void setUpperLeft(PCPoint upperLeft) {
        this.upperLeft = upperLeft;
    }

    public PCPoint getLowerRight() {
        return lowerRight;
    }

    public void setLowerRight(PCPoint lowerRight) {
        this.lowerRight = lowerRight;
    }

    public String toString() {
        String rc = null;
        try {
            rc = Util.getClassName(this)
                + " ul: " + getUpperLeft().name()
                + " lr: " + getLowerRight().name();
        } catch (NullPointerException ex) {
            rc = "NPE getting PCRect's values";
        }
        return rc;
    }
}
