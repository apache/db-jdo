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

package org.apache.jdo.pc.appid;

import java.util.HashSet;

/**
* A sample insurance class, generated from an SQL schema.
*/
public class PCInsurance implements java.io.Serializable{
    public long insid;
    
    public String carrier;
    
    public org.apache.jdo.pc.appid.PCEmployee employee;
    
    public String toString() {
        return "Ins: " + carrier + ", id=" + insid +
            ", emp " + employee.getLastname();
    }

    public PCInsurance() {
    }

    public PCInsurance(long _id, String _carrier, PCEmployee _emp) {
        insid = _id;
        carrier = _carrier;
        employee = _emp;
    }
    
    public long getInsid() {
        return insid;
    }
    
    public void setInsid(long insid) {
        this. insid = insid;
    }
    
    public String getCarrier() {
        return carrier;
    }
    
    public void setCarrier(String carrier) {
        this. carrier = carrier;
    }
    
    public PCEmployee getEmployee() {
        return employee;
    }
    
    public void setEmployee(PCEmployee employee) {
        this. employee = employee;
    }
    
    public static class Oid {
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
        
        
}
}

