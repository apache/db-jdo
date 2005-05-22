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

/*
 * PCFullTimeEmployee.java
 *
 * Created on May 22, 2001, 9:36 AM
 */

package org.apache.jdo.pc.empdept;

/**
 *
 * @author  clr
 * @version 
 */
public class PCFullTimeEmployee extends PCEmployee {

    public double salary;
    
    /** Creates new PCFullTimeEmployee */
    public PCFullTimeEmployee() {
    }
    public PCFullTimeEmployee(
            String _first,  
            String _last, 
            java.util.Date _born, 
            long _empid, 
            java.util.Date _hired,
            double _sal ) {
        super (_first, _last, _born, _empid, _hired, null, null, null, null, null);
        salary = _sal;
    }
    
    public double getSalary() {
        return salary;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
    
    public String toString() {
        StringBuffer rc = new StringBuffer("PCFullTimeEmployee: ");
//        rc.append(lastname);
//        rc.append(", " + firstname);
        rc.append(super.toString());
//        rc.append(", id=" + empid);
//        rc.append(", born " + formatter.format(birthdate));
//        rc.append(", hired " + formatter.format(hiredate));
        rc.append(" $" + salary);
//        String mgrName = "none";
//        if (null != manager) {
//            mgrName = manager.getLastname();
//        }
//        rc.append(" manager: " + mgrName);
//        rc.append(" dept: " + department.getName());
//        int numEmps = 0;
//        if (null != employees) {
//            numEmps = employees.size();
//        }
//        rc.append(" emps: " + numEmps);
//        rc.append(" insurance: " + insurance.getCarrier());
        return rc.toString();
    }
            

}
