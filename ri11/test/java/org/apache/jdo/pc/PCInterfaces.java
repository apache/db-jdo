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
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import org.apache.jdo.test.util.Util;


/**
* Test ability to store & fetch instances of classes which refer to other
* PersistenceCapable classes, where the field type in the referring class is
* of an interface, not of an implementation.
*
* @author Dave Bristor
*/
public class PCInterfaces {
    public java.util.List _list;
    public java.util.List _emptyList = new Vector();
    public java.util.List _nullList = null;

    public PCInterfaces() { }

    public void init() {
        _list = new ArrayList();
        _list.add(new Integer(42));
        _list.add(new Long(5551212));
    }
    
    public boolean equals(Object o) {
        // Note, this implementation assumes all _nullXXX fields are null
        // and all _XXX fields are not null in this instance
        if (null == o || !(o instanceof PCInterfaces))
            return false;
        PCInterfaces other = (PCInterfaces)o;
        return _list.equals(other._list) &&
               _emptyList.equals(other._emptyList) && 
               _nullList == other._nullList;
    }

    public String toString() {
        StringBuffer rc = new StringBuffer(this.getClass().getName());
        rc.append(Util.stringifyList(_list,
                                     Util.getInfo("_list", _list)));
        rc.append(Util.stringifyList(_emptyList, "_emptyList"));
        rc.append(Util.stringifyList(_nullList, "_nullList"));
        return rc.toString();
    }
}
