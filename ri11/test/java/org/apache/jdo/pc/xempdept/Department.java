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

package org.apache.jdo.pc.xempdept;

import java.io.Serializable;
import java.util.HashSet;

public class Department implements Serializable, Identifiable {

    private long deptid;
    private String name;
    private Company company;
    private HashSet employees;

    public Object getOid() {
        Oid oid = new Oid();
        oid.deptid = this.deptid;
        return oid;
    }

    public String toString() {
        return "Department(" + name + 
               ", id=" + deptid +
               ", employees=" + ((employees == null) ? 0 : employees.size()) + 
               ", company=" + ((company == null) ? "null" : company.getName()) +
               ")";
    }

    public Department() {
    }
    
    public Department(long deptid, 
                      String name, 
                      Company company, 
                      HashSet employees) {
        this.deptid = deptid;
        this.name = name;
        this.company = company;
        this.employees = employees;
    }
    
    public long getDeptid() {
        return deptid;
    }

    public void setDeptid(long deptid) {
        this.deptid = deptid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public java.util.HashSet getEmployees() {
        return employees;
    }

    public void setEmployees(HashSet employees) {
        this.employees = employees;
    }

    public static class Oid implements Serializable, Comparable {

        public long deptid;

        public Oid() {
        }

        public boolean equals(java.lang.Object obj) {
            if( obj==null ||
                !this.getClass().equals(obj.getClass()) ) return( false );
            Oid o=(Oid) obj;
            if( this.deptid!=o.deptid ) return( false );
            return( true );
        }

        public int hashCode() {
            int hashCode=0;
            hashCode += deptid;
            return( hashCode );
        }

        public int compareTo(Object o) {
            if (o == null)
                throw new ClassCastException();
            if (o == this)
                return 0;
            long otherDeptid = ((Oid)o).deptid;
            if (deptid == otherDeptid)
                return 0;
            else if (deptid < otherDeptid)
                return -1;
            return 1;
        }

    }

}

