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


/**
 * A sample department class.
 */
public class Department {
    private int deptid;
    private String name;
    private Collection employees;

    public Department() {
    }

    public Department(String str) {
        super();
    }

    public Object clone() throws java.lang.CloneNotSupportedException {
        // clone() method for enhancer testing purpose
        final empdept.Department pc = (empdept.Department)super.clone();
        pc.deptid = this.deptid;
        pc.employees = this.employees;
        pc.name = this.name;
        return pc;
    }

    public int getDeptid() {
        return deptid;
    }
    
    public void setDeptid(int deptid) {
        this. deptid = deptid;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this. name = name;
    }
    
    public Collection getEmployees() {
        return employees;
    }
    
    public void setEmployees(Collection employees) {
        this. employees = employees;
    }
    
    public static class Oid
        implements java.io.Serializable {
        public int deptid;
    
        public Oid() {
            super();
        }
    
        public Oid(String str) {
            // not implemented yet
            throw new UnsupportedOperationException();
        }
    
        public int hashCode() {
            int hash = 0;
            hash += (int)deptid;
            return hash;
        }
    
        public boolean equals(java.lang.Object pk) {
            if (pk == null || !this.getClass().equals(pk.getClass())) {
                return false;
            }
            Oid oid = (Oid)pk;
            if (this.deptid != oid.deptid) return false;
            return true;
        }
    }    
}

