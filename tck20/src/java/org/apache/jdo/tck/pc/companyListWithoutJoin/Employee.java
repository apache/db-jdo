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
 
package org.apache.jdo.tck.pc.companyListWithoutJoin;

import java.io.ObjectInputStream;
import java.io.IOException;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

/**
 * This class represents an employee.
 */
public abstract class Employee extends Person implements IEmployee {

    private Date             hiredate;
    private double           weeklyhours;
    private Department       department;
    private Department       fundingDept;
    private Employee         manager;
    private Employee         mentor;
    private Employee         protege;
    private Employee         hradvisor;
    private transient Set team = new HashSet();  // element-type is Employee
    private transient Set hradvisees = new HashSet();  // element-type is Employee

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
     * Get the employee's department.
     * @return The department associated with the employee.
     */
    public IDepartment getDepartment() {
        return department;
    }

    /**
     * Set the employee's department.
     * @param department The department.
     */
    public void setDepartment(IDepartment department) {
        this.department = (Department)department;
    }

    /**
     * Get the employee's funding department.
     * @return The funding department associated with the employee.
     */
    public IDepartment getFundingDept() {
        return fundingDept;
    }

    /**
     * Set the employee's funding department.
     * @param department The funding department.
     */
    public void setFundingDept(IDepartment department) {
        this.fundingDept = (Department)department;
    }

    /**
     * Get the employee's manager.
     * @return The employee's manager.
     */
    public IEmployee getManager() {
        return manager;
    }

    /**
     * Set the employee's manager.
     * @param manager The employee's manager.
     */
    public void setManager(IEmployee manager) {
        this.manager = (Employee)manager;
    }

    /**
     * Get the employee's team.
     * @return The set of <code>Employee</code>s on this employee's team,
     * returned as an unmodifiable set. 
     */
    public Set getTeam() {
        return Collections.unmodifiableSet(team);
    }

    /**
     * Add an <code>Employee</code> to this employee's team.
     * This method sets both sides of the relationship, modifying
     * this employees team to include parameter emp and modifying
     * emp to set its manager attribute to this object.
     * @param emp The <code>Employee</code> to add to the team.
     */
    public void addToTeam(Employee emp) {
        team.add(emp);
        emp.manager = this;
    }

    /**
     * Remove an <code>Employee</code> from this employee's team.
     * This method will also set the <code>emp</code> manager to null.
     * @param emp The <code>Employee</code> to remove from the team.
     */
    public void removeFromTeam(Employee emp) {
        team.remove(emp);
        emp.manager = null;
    }

    /**
     * Set the employee's team.
     * @param team The set of <code>Employee</code>s.
     */
    public void setTeam(Set team) {
        // workaround: create a new HashSet, because fostore does not
        // support LinkedHashSet
        this.team = (team != null) ? new HashSet(team) : null;
    }

    /**
     * Set the mentor for this employee. 
     * @param mentor The mentor for this employee.
     */
    public void setMentor(IEmployee mentor) {
        this.mentor = (Employee)mentor;
    }

    /**
     * Get the mentor for this employee.
     * @return The mentor.
     */
    public IEmployee getMentor() {
        return mentor;
    }

    /**
     * Set the protege for this employee.
     * @param protege The protege for this employee.
     */
    public void setProtege(IEmployee protege) {
        this.protege = (Employee)protege;
    }

    /**
     * Get the protege of this employee.
     * @return The protege of this employee.
     */
    public IEmployee getProtege() {
        return protege;
    }

    /**
     * Set the HR advisor for this employee.
     * @param hradvisor The hradvisor for this employee.
     */
    public void setHradvisor(IEmployee hradvisor) {
        this.hradvisor = (Employee)hradvisor;
    }

    /**
     * Get the HR advisor for the employee.
     * @return The HR advisor.
     */
    public IEmployee getHradvisor() {
        return hradvisor;
    }

    /**
     * Get the HR advisees of this HR advisor.
     * @return An unmodifiable <code>Set</code> containing the
     * <code>Employee</code>s that are HR advisees of this employee.
     */
    public Set getHradvisees() {
        return Collections.unmodifiableSet(hradvisees);
    }

    /**
     * Add an <code>Employee</code> as an advisee of this HR advisor. 
     * This method also sets the <code>emp</code> hradvisor to reference
     * this object. In other words, both sides of the relationship are
     * set. 
     * @param emp The employee to add as an advisee.
     */
    public void addAdvisee(Employee emp) {
        hradvisees.add(emp);
        emp.hradvisor = this;
    }

    /**
     * Remove an <code>Employee</code> as an advisee of this HR advisor.
     * This method also sets the <code>emp</code> hradvisor to null.
     * In other words, both sides of the relationship are set.
     * @param emp The employee to add as an HR advisee.
     */
    public void removeAdvisee(Employee emp) {
        hradvisees.remove(emp);
        emp.hradvisor = null;
    }

    /**
     * Set the HR advisees of this HR advisor.
     * @param hradvisees The <code>Employee</code>s that are HR advisees of
     * this employee. 
     */
    public void setHradvisees(Set hradvisees) {
        // workaround: create a new HashSet, because fostore does not
        // support LinkedHashSet
        this.hradvisees = (hradvisees != null) ? new HashSet(hradvisees) : null;
    }

    /** Serialization support: initialize transient fields. */
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        team = new HashSet();
        hradvisees = new HashSet();
    }

    /**
     * Return a String representation of a <code>Employee</code> object.
     * @return a String representation of a <code>Employee</code> object.
     */
    public String toString() {
        return "Employee(" + getFieldRepr() + ")";
    }

    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    protected String getFieldRepr() {
        StringBuffer rc = new StringBuffer();
        rc.append(super.getFieldRepr());
        rc.append(", hired ").append(formatter.format(hiredate));
        rc.append(", weeklyhours ").append(weeklyhours);
        return rc.toString();
    }

    /** 
     * Returns <code>true</code> if all the fields of this instance are
     * deep equal to the corresponding fields of the specified Employee.
     * @param other the object with which to compare.
     * @param helper EqualityHelper to keep track of instances that have
     * already been processed. 
     * @return <code>true</code> if all the fields are deep equal;
     * <code>false</code> otherwise.  
     * @throws ClassCastException if the specified instances' type prevents
     * it from being compared to this instance. 
     */
    public boolean deepCompareFields(Object other, 
                                     EqualityHelper helper) {
        IEmployee otherEmp = (IEmployee)other;
        String where = "Employee<" + getPersonid() + ">";
        return super.deepCompareFields(otherEmp, helper) &
            helper.equals(hiredate, otherEmp.getHiredate(),  where + ".hiredate") &
            helper.closeEnough(weeklyhours, otherEmp.getWeeklyhours(), where + ".weeklyhours") &
            helper.deepEquals(department, otherEmp.getDepartment(), where + ".department") &
            helper.deepEquals(fundingDept, otherEmp.getFundingDept(), where + ".fundingDept") &
            helper.deepEquals(manager, otherEmp.getManager(), where + ".manager") &
            helper.deepEquals(mentor, otherEmp.getMentor(), where + ".mentor") &
            helper.deepEquals(protege, otherEmp.getProtege(), where + ".protege") &
            helper.deepEquals(hradvisor, otherEmp.getHradvisor(), where + ".hradvisor") &
            helper.deepEquals(team, otherEmp.getTeam(), where + ".team") &
            helper.deepEquals(hradvisees, otherEmp.getHradvisees(), where + ".hradvisees");
    }

}

