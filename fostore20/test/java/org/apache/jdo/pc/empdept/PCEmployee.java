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

package org.apache.jdo.pc.empdept;

import java.util.HashSet;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
* A sample employee class, generated from an SQL schema.
*/
public class PCEmployee extends PCPerson {
    
    public long empid;
    
    public java.util.Date hiredate;
    
    public org.apache.jdo.pc.empdept.PCDepartment department;
    
    public java.util.HashSet projects;
    
    public org.apache.jdo.pc.empdept.PCEmployee manager;
    
    public java.util.HashSet employees;
    
    public org.apache.jdo.pc.empdept.PCInsurance insurance;
    
    public String toString() {
        StringBuffer rc = new StringBuffer("PCEmployee: ");
//        rc.append(lastname);
//        rc.append(", " + firstname);
        rc.append(super.toString());
        rc.append(", id=" + empid);
//        rc.append(", born " + formatter.format(birthdate));
        rc.append(", hired " + formatter.format(hiredate));
//        rc.append(" $" + salary);
        String mgrName = "none";
        if (null != manager) {
            mgrName = manager.getLastname();
        }
        rc.append(" manager: " + mgrName);
        rc.append(" dept: " + department.getName());
        int numEmps = 0;
        if (null != employees) {
            numEmps = employees.size();
        }
        rc.append(" emps: " + numEmps);
        rc.append(" insurance: " + insurance.getCarrier());
        return rc.toString();
    }
            
    public PCEmployee() {
    }

    PCEmployee(        
        String _first, 
        String _last, 
        java.util.Date _born, 
        long _empid, 
        java.util.Date _hired) {
    super (_first, _last, _born);
    empid = _empid;
    hiredate = _hired;
    }
    
    PCEmployee(        
        String _first, 
        String _last, 
        java.util.Date _born, 
        long _empid, 
        java.util.Date _hired, 
        org.apache.jdo.pc.empdept.PCDepartment department,
        java.util.HashSet projects,
        org.apache.jdo.pc.empdept.PCEmployee manager,
        java.util.HashSet employees,
        org.apache.jdo.pc.empdept.PCInsurance insurance) {
    super (_first, _last, _born);
    this.empid = _empid;
    this.hiredate = _hired;
    this.department = department;
    this.projects = projects;
    this.manager = manager;
    this.employees = employees;
    this.insurance = insurance;
    }
    public long getEmpid() {
        return empid;
    }
    
    public void setEmpid(long empid) {
        this. empid = empid;
    }

    public void setEmployees(HashSet s) {
        employees = s;
    }
    
/*    public String getLastname() {
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
    
*/
    public java.util.Date getHiredate() {
        return hiredate;
    }
    
    public void setHiredate(java.util.Date hiredate) {
        this. hiredate = hiredate;
    }
    
/*    public java.util.Date getBirthdate() {
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
*/     
    
    public java.util.HashSet getProjects() {
        return projects;
    }
    
    public void setProjects(java.util.HashSet projects) {
        this. projects = projects;
    }
    
    public PCDepartment getDepartment() {
        return department;
    }
    
    public void setDepartment(PCDepartment department) {
        this. department = department;
    }
    
    public PCEmployee getManager() {
        return manager;
    }
    
    public void setManager(PCEmployee manager) {
        this. manager = manager;
    }
    
    public java.util.HashSet getPCEmployees() {
        return employees;
    }
    
    public void setPCEmployees(java.util.HashSet employees) {
        this. employees = employees;
    }
    
    public PCInsurance getInsurance() {
        return insurance;
    }
    
    public void setInsurance(PCInsurance insurance) {
        this. insurance = insurance;
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
