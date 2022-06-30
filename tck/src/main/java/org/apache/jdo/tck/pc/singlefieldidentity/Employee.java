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
 
package org.apache.jdo.tck.pc.singlefieldidentity;

import java.util.Date;

/**
 * This class represents an employee.
 */
public abstract class Employee extends Person {

    private Date             hiredate;
    private double           weeklyhours;

    /** This is the JDO-required no-args constructor */
    protected Employee() {}

    /**
     * Construct an <code>Employee</code> instance.
     * @param personid The identifier for the person.
     * @param firstname The first name of the employee.
     * @param lastname The last name of the employee.
     * @param middlename The middle name of the employee.
     * @param birthdate The birth date of the employee.
     * @param hiredate The date that the employee was hired.
     */
    public Employee(long personid, String firstname, String lastname, 
                    String middlename, Date birthdate,
                    Date hiredate) {
        super(personid, firstname, lastname, middlename, birthdate);
        this.hiredate = hiredate;
    }

    /**
     * Get the date that the employee was hired.
     * @return The date the employee was hired.
     */
    public Date getHiredate() {
        return hiredate;
    }

    /**
     * Set the date that the employee was hired.
     * @param hiredate The date the employee was hired.
     */
    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    /**
     * Get the weekly hours of the employee.
     * @return The number of hours per week that the employee works.
     */
    public double getWeeklyhours() {
        return weeklyhours;
    }

    /**
     * Set the number of hours per week that the employee works.
     * @param weeklyhours The number of hours per week that the employee
     * works. 
     */
    public void setWeeklyhours(double weeklyhours) {
        this.weeklyhours = weeklyhours;
    }

    /**
     * Return a String representation of a <code>Employee</code> object.
     * @return a String representation of a <code>Employee</code> object.
     */
    @Override
    public String toString() {
        return "Employee(" + getFieldRepr() + ")";
    }

    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    @Override
    protected String getFieldRepr() {
        StringBuffer rc = new StringBuffer();
        rc.append(super.getFieldRepr());
        rc.append(", hired ").append(
            hiredate==null ? "null" : formatter.format(hiredate));
        rc.append(", weeklyhours ").append(weeklyhours);
        return rc.toString();
    }

}

