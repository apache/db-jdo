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

package org.apache.jdo.pc.xempdept;

import java.io.Serializable;
import java.util.HashSet;

public class Insurance implements Serializable, Identifiable {

    private long insid;
    private String carrier;
    private Employee employee;

    public Object getOid() {
        Oid oid = new Oid();
        oid.insid = this.insid;
        return oid;
    }
    
    public String toString() {
        return "Insurance(" + carrier +
               ", insid=" + insid +
            ", employee=" + ((employee==null) ? "null" : employee.getLastname()) +
            ")";
    }

    public Insurance() {
    }

    public Insurance(long insid,
                     String carrier,
                     Employee employee) {
        this.insid = insid;
        this.carrier = carrier;
        this.employee = employee;
    }

    public long getInsid() {
        return insid;
    }

    public void setInsid(long insid) {
        this.insid = insid;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public static class Oid implements Serializable, Comparable {

        public long insid;

        public Oid() {
        }

        public boolean equals(java.lang.Object obj) {
            if( obj==null ||
                !this.getClass().equals(obj.getClass()) ) return( false );
            Oid o=(Oid) obj;
            if( this.insid!=o.insid ) return( false );
            return( true );
        }

        public int hashCode() {
            int hashCode=0;
            hashCode += insid;
            return( hashCode );
        }

        public int compareTo(Object o) {
            if (o == null)
                throw new ClassCastException();
            if (o == this)
                return 0;
            long otherInsid = ((Oid)o).insid;
            if (insid == otherInsid)
                return 0;
            else if (insid < otherInsid)
                return -1;
            return 1;
        }
    }

}

