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

package empdept;

import java.util.Collection;
import java.util.Date;

/**
 * A sample employee class.
 */
public class Employee {
    private long empid;    
    private String name;
    private Date birthdate;
    private Department department;    
    
    public Employee() {
    }

    public Employee(String str) {
        super();
    }

    public Object clone() throws java.lang.CloneNotSupportedException {
        // clone() method for enhancer testing purpose
        final empdept.Employee pc = (empdept.Employee)super.clone();
        pc.birthdate = this.birthdate;
        pc.department = this.department;
        pc.empid = this.empid;
        pc.name = this.name;
        return pc;
    }

    public long getEmpid() {
        return empid;
    }
    
    public void setEmpid(long empid) {
        this.empid = empid;
    }

    public String setName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public java.util.Date getBirthdate() {
        return birthdate;
    }
    
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
    
    public Department getDepartment() {
        return department;
    }
    
    public void setDepartment(Department department) {
        this.department = department;
    }
    
    public static class Oid
        implements java.io.Serializable {
        public long empid;
    
        public Oid() {
            super();
        }
    
        public Oid(String str) {
            // not implemented yet
            throw new UnsupportedOperationException();
        }
    
        public int hashCode() {
            int hash = 0;
            hash += (int)empid;
            return hash;
        }
    
        public boolean equals(java.lang.Object pk) {
            if (pk == null || !this.getClass().equals(pk.getClass())) {
                return false;
            }
            Oid oid = (Oid)pk;
            if (this.empid != oid.empid) return false;
            return true;
        }
    
    }    
}

