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

/*
 * PCPerson.java
 *
 * Created on May 22, 2001, 9:34 AM
 */

package org.apache.jdo.pc.appid;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Locale;

/**
 *
 * @author  clr
 * @version 
 */
abstract public class PCPerson implements java.io.Serializable{

    public String firstname;
    
    public String lastname;
    
    public java.util.Date birthdate;
    
    protected static SimpleDateFormat formatter;

    static {
        formatter = new SimpleDateFormat("d/MMM/yyyy", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
    }
    
    /** Creates new PCPerson */
    
    public PCPerson() {
    }
    
    public PCPerson(String firstname, String lastname, java.util.Date birthdate) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
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
    
    public java.util.Date getBirthdate() {
        return birthdate;
    }
    
    public void setBirthdate(java.util.Date birthdate) {
        this. birthdate = birthdate;
    }
    

    public String toString() {
        StringBuffer rc = new StringBuffer("PCPerson: ");
        rc.append(getLastname());
        rc.append(", " + getFirstname());
//        rc.append(", id=" + empid);
        rc.append(", born " + formatter.format(getBirthdate()));
//        rc.append(", hired " + formatter.format(hiredate));
//        rc.append(" $" + salary);
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
