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

package org.apache.jdo.test.util;

import java.util.Comparator;

import org.apache.jdo.impl.fostore.OID;

public class OIDComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        // Please note, 
        //   return (int)(((OID)o1).oid - ((OID)o2).oid);
        // does not work, because the difference might exceed the int range
        // and then the cast to an int returns unwanted results.
        long oid1 = ((OID)o1).oid;
        long oid2 = ((OID)o2).oid;
        if (oid1 == oid2) return 0;
        else if (oid1 > oid2) return 1;
        else return -1;
    }

    public boolean equals(Object obj) {
        return obj.equals(this);
    }
}
