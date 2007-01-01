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

import java.util.ArrayList;

import org.apache.jdo.test.util.Util;


/**
* Test for instances containing collection instances of user-defined class.
*
* @author Dave Bristor
*/
public class PCStroke {
    public ArrayList points;

    public PCStroke() { }

    // Create a PCStroke with "interesting" values.
    public PCStroke(ArrayList al) {
        this.points = al;
    }

    public String toString() {
        StringBuffer rc = new StringBuffer(Util.getClassName(this) + ":\n");
        int size = points.size();
        if (0 == size) {
            rc.append("\tPCStroke has no points");
        } else {
            for (int i = 0; i < size; i++) {
                rc.append("\t").append((PCPoint)points.get(i));
                rc.append("\n");
            }
        }
        return rc.toString();
    }
}
