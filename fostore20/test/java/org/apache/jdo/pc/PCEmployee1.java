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

package org.apache.jdo.pc;

import org.apache.jdo.test.util.Util;

/**
* A extended employee class that differs from Employee class
* in types of the relationship fields on the many side:
* Array of javax.jdo.spi.PersistenceCapable instances for
* manager-employees relationship, and LinkedList for the list
* of projects.
*
* @author Marina Vatkina
*/
public class PCEmployee1 implements java.io.Serializable {
    public long empid;
    
    public String lastname;
    
    public String firstname;
    
    public java.util.Date hiredate;
    
    public java.util.Date birthdate;
    
    public double salary;
    
    public java.util.LinkedList projects;
    
    public PCDepartment1 department;
    
    public PCEmployee1 manager;
    
    public Object[] employees;
    
    public PCInsurance1 insurance;
    
    public String toString() {
        StringBuffer rc = new StringBuffer("Emp: ");
        rc.append(lastname);
        rc.append(", " + firstname);
        rc.append(", id=" + empid);
        rc.append(", born " + Util.shortFormatter.format(birthdate));
        rc.append(", hired " + Util.shortFormatter.format(hiredate));
        rc.append(" $" + salary);
        String mgrName = "none";
        if (null != manager) {
            mgrName = manager.getLastname();
        }
        rc.append(" manager: " + mgrName);
        String dname = "none";
        if (null != department) {
            dname = department.getName();
        }
        rc.append(" dept: " + dname);
        int numEmps = 0;
        if (null != employees) {
            numEmps = employees.length;
        }
        rc.append(" emps: " + numEmps);
        if (null != insurance) {
            rc.append(" insurance: " + insurance.getCarrier());
        } else {
            rc.append(" insurance: null");
        }

        return rc.toString();
    }
            
    public PCEmployee1() {
    }

    public PCEmployee1(long _empid, String _last, String _first, double _sal,
             java.util.Date _born, java.util.Date _hired) {
        empid = _empid;
        lastname = _last;
        firstname = _first;
        salary = _sal;
        birthdate = _born;
        hiredate = _hired;
    }
    
    public long getEmpid() {
        return empid;
    }
    
    public void setEmpid(long empid) {
        this. empid = empid;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    public void setLastname(String lastname) {
        this. lastname = lastname;
    }
    
    public String getFirstname() {
        return firstname;
    }
    
    public void setFirstname(String firstname) {
        this. firstname = firstname;
    }
    
    public java.util.Date getHiredate() {
        return hiredate;
    }
    
    public void setHiredate(java.util.Date hiredate) {
        this. hiredate = hiredate;
    }
    
    public java.util.Date getBirthdate() {
        return birthdate;
    }
    
    public void setBirthdate(java.util.Date birthdate) {
        this. birthdate = birthdate;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public void setSalary(double salary) {
        this. salary = salary;
    }
    
    public java.util.LinkedList getProjects() {
        return projects;
    }
    
    public void setProjects(java.util.LinkedList projects) {
        this. projects = projects;
    }
    
    public PCDepartment1 getDepartment() {
        return department;
    }
    
    public void setDepartment(PCDepartment1 department) {
        this. department = department;
    }
    
    public PCEmployee1 getManager() {
        return manager;
    }
    
    public void setManager(PCEmployee1 manager) {
        this. manager = manager;
    }
    
    public Object[] getEmployees() {
        return employees;
    }
    
    public void setEmployees(Object[] employees) {
        this. employees = employees;
    }
    
    public PCInsurance1 getInsurance() {
        return insurance;
    }
    
    public void setInsurance(PCInsurance1 insurance) {
        this. insurance = insurance;
    }

    // Returns true iff the values of fields in this are == to those in the
    // given object.
    public boolean sameValues(Object o) {
        PCEmployee1 other = (PCEmployee1)o;

        return
            (empid == other.empid) &&
            (lastname == other.lastname) &&
            (firstname == other.firstname) &&
            (hiredate == other.hiredate) &&
            (salary == other.salary) &&
            (projects == other.projects) &&
            (department == other.department) &&
            (manager == other.manager) &&
            (employees == other.employees) &&
            (insurance == other.insurance);
    }        
    
    public static class Oid {
        public long empid;
        
        public Oid() {
        }
        
        public boolean equals(java.lang.Object obj) {
            if( obj==null ||
            !this.getClass().equals(obj.getClass()) ) return( false );
            Oid o=(Oid) obj;
            if( this.empid!=o.empid ) return( false );
            return( true );
        }
        
        public int hashCode() {
            int hashCode=0;
            hashCode += empid;
            return( hashCode );
        }
        
        
    }
}

