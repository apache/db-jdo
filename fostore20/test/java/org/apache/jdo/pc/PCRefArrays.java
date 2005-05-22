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

import java.lang.reflect.Array;

import org.apache.jdo.test.util.Util;


/**
* Test that arrays can refer to PersistenceCapable instances
*
* @author Dave Bristor
*/
public class PCRefArrays {
    public PCPoint _pcpointArray[];

    public PCRefArrays() { }

    public void init(PCPoint p1, PCPoint p2) {
        _pcpointArray = new PCPoint[] { p1, p2 };
    }

    String stringify(Object arr, String name) {
        StringBuffer rc =new StringBuffer("\n").append(name).append(": ");

        if (null == arr) {
            rc.append(" __null__");
        } else try {
            int length = Array.getLength(arr);
            if (0 == length) {
                rc.append(" __empty__");
            } else {
                for (int i = 0; i < length; i++) {
                    if (i > 0) {
                        rc.append(",");
                    }
                    rc.append(" " + Array.get(arr, i));
                }
            }
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("illegal argument: " + arr.getClass().getName());
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new RuntimeException("out of bounds: " + arr.getClass().getName());
        }

        return rc.toString();
    }

    public String toString() {
        StringBuffer rc = new StringBuffer(Util.getClassName(this));

        rc.append(stringify(_pcpointArray, "_pcpointArray"));

        return rc.toString();
    }
}
