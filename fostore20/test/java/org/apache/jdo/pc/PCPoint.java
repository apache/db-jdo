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
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import org.apache.jdo.test.util.Util;


/**
* A simple class with two fields
*
* @author Dave Bristor
*/
public class PCPoint implements Serializable {
    public int x;
    public Integer y;

    public PCPoint() { }

    public PCPoint(int x, int y) {
        this.x = x;
        this.y = new Integer(y);
    }

    public PCPoint(int x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object o) {
        if ((o == null) || !(o instanceof PCPoint))
            return false;
        PCPoint other = (PCPoint)o;
        if (x != other.x)
            return false;
        if (y == null)
            return other.y == null;
        else if (other.y == null)
            return y == null;
        else 
            return y.intValue() == other.y.intValue();
    }

    public String toString() {
        String rc = null;
        try {
            rc = Util.getClassName(this) + name();
        } catch (NullPointerException ex) {
            rc = "NPE getting PCPoint's values";
        }
        return rc;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getY() {
        return y;
    }
    
    public String name() {
        return " x: " + getX() + ", y: " + getY().intValue();
    }

    public static Iterator getSortedIterator(Iterator i) {
        TreeSet sorted = new TreeSet(new Comparator() {
                public int compare(Object o1, Object o2) {
                    return (((PCPoint)o1).x - ((PCPoint)o2).x);
                }
                public boolean equals(Object obj) {
                    return obj.equals(this);
                }
            });
        
        while(i.hasNext()) {
            Object o = i.next();
            sorted.add(o);
        }
        return sorted.iterator();
    }
}
