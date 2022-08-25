/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jdo.tck.pc.fieldtypes;

import java.io.Serializable;

public enum SimpleEnum {
    
AL("ALABAMA"),
AK("ALASKA"),
AS("AMERICAN SAMOA"),
AZ("ARIZONA"),
AR("ARKANSAS"),
CA("CALIFORNIA"),
CO("COLORADO"),
CT("CONNECTICUT"),
DE("DELAWARE"),
DC("DISTRICT OF COLUMBIA"),
FM("FEDERATED STATES OF MICRONESIA"),
FL("FLORIDA"),
GA("GEORGIA"),
GU("GUAM"),
HI("HAWAII"),
ID("IDAHO"),
IL("ILLINOIS"),
IN("INDIANA"),
IA("IOWA"),
KS("KANSAS"),
KY("KENTUCKY"),
LA("LOUISIANA"),
ME("MAINE"),
MH("MARSHALL ISLANDS"),
MD("MARYLAND"),
MA("MASSACHUSETTS"),
MI("MICHIGAN"),
MN("MINNESOTA"),
MS("MISSISSIPPI"),
MO("MISSOURI"),
MT("MONTANA"),
NE("NEBRASKA"),
NV("NEVADA"),
NH("NEW HAMPSHIRE"),
NJ("NEW JERSEY"),
NM("NEW MEXICO"),
NY("NEW YORK"),
NC("NORTH CAROLINA"),
ND("NORTH DAKOTA"),
MP("NORTHERN MARIANA ISLANDS"),
OH("OHIO"),
OK("OKLAHOMA"),
OR("OREGON"),
PW("PALAU"),
PA("PENNSYLVANIA"),
PR("PUERTO RICO"),
RI("RHODE ISLAND"),
SC("SOUTH CAROLINA"),
SD("SOUTH DAKOTA"),
TN("TENNESSEE"),
TX("TEXAS"),
UT("UTAH"),
VT("VERMONT"),
VI("VIRGIN ISLANDS"),
VA("VIRGINIA"),
WA("WASHINGTON"),
WV("WEST VIRGINIA"),
WI("WISCONSIN"),
WY("WYOMING");
    
    String sname;
    
    SimpleEnum(String s) {
        sname = s;
    }
    
    public String toString() {
        return sname;
    }

    public static class Oid implements Serializable, Comparable<Oid> {

        private static final long serialVersionUID = 1L;

        public long id;
        
        public Oid() {
        }
        
        public Oid(String s) { id = Integer.parseInt(justTheId(s)); }
        
        public String toString() { return this.getClass().getName() + ": "  + id;}
        
        public int hashCode() { return (int)id ; }
        
        public boolean equals(Object other) {
            if (other != null && (other instanceof Oid)) {
                Oid k = (Oid)other;
                return k.id == this.id;
            }
            return false;
        }
        
        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }
        
        public int compareTo(Oid p){
            return Long.compare(id, p.id);
        }
        
    }
}
