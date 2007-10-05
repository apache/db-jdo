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
 
package org.apache.jdo.tck.pc.companyAnnotatedJPA;

import javax.persistence.*;

import java.io.ObjectInputStream;
import java.io.IOException;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.jdo.tck.pc.company.IAddress;
import org.apache.jdo.tck.pc.company.IDentalInsurance;
import org.apache.jdo.tck.pc.company.IDepartment;

import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.pc.company.IMedicalInsurance;
import org.apache.jdo.tck.util.EqualityHelper;

/**
 * This class represents an employee.
 */
@Entity
@Table(name="employees")
public abstract class JPAAppEmployee extends JPAAppPerson implements IEmployee {

    @Column(name="HIREDATE")
    @Temporal(TemporalType.DATE)
    private Date             hiredate;
    @Column(name="WEEKLYHOURS")
    private double           weeklyhours;
    @OneToOne(mappedBy="employee")
    private JPAAppDentalInsurance  dentalInsurance;
    @OneToOne(mappedBy="employee")
    private JPAAppMedicalInsurance medicalInsurance;
    @Column(name="DEPARTMENT")
    private JPAAppDepartment       department;
    @Column(name="FUNDINGDEPT")
    private JPAAppDepartment       fundingDept;
    @Column(name="MANAGER")
    private JPAAppEmployee         manager;
    @Column(name="MENTOR")
    private JPAAppEmployee         mentor;
    @OneToOne(mappedBy="mentor")
    private JPAAppEmployee         protege;
    @Column(name="HRADVISOR")
    private JPAAppEmployee         hradvisor;
    @ManyToMany(mappedBy="reviewers",
        targetEntity=org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppProject.class)
    private Set reviewedProjects = new HashSet();
    @ManyToMany(mappedBy="members",
        targetEntity=org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppProject.class)
    private Set projects = new HashSet();
    @OneToMany(mappedBy="manager",
        targetEntity=org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppEmployee.class)
    private Set team = new HashSet();
    @OneToMany(mappedBy="hradvisor",
        targetEntity=org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppEmployee.class)
    private Set hradvisees = new HashSet();

    /** This is the JDO-required no-args constructor */
    protected JPAAppEmployee() {}

    /**
     * Construct an <code>JPAAppEmployee</code> instance.
     * 
     * 
     * @param personid The identifier for the person.
     * @param firstname The first name of the employee.
     * @param lastname The last name of the employee.
     * @param middlename The middle name of the employee.
     * @param birthdate The birth date of the employee.
     * @param hiredate The date that the employee was hired.
     */
    public JPAAppEmployee(long personid, String firstname, String lastname, 
                    String middlename, Date birthdate,
                    Date hiredate) {
        super(personid, firstname, lastname, middlename, birthdate);
        this.hiredate = hiredate;
    }

    /**
     * Construct an <code>JPAAppEmployee</code> instance.
     * 
     * 
     * @param personid The identifier for the person.
     * @param firstname The first name of the employee.
     * @param lastname The last name of the employee.
     * @param middlename The middle name of the employee.
     * @param birthdate The birth date of the employee.
     * @param address The address of the employee.
     * @param hiredate The date that the employee was hired.
     */
    public JPAAppEmployee(long personid, String firstname, String lastname, 
                    String middlename, Date birthdate, IAddress address,
                    Date hiredate) {
        super(personid, firstname, lastname, middlename, birthdate,
                (JPAAppAddress)address);
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
     * Get the reviewed projects.
     * @return The reviewed projects as an unmodifiable set.
     */
    public Set getReviewedProjects() {
        return Collections.unmodifiableSet(reviewedProjects);
    }

    /**
     * Add a reviewed project.
     * @param project A reviewed project.
     */
    public void addReviewedProjects(JPAAppProject project) {
        reviewedProjects.add(project);
    }

    /**
     * Remove a reviewed project.
     * @param project A reviewed project.
     */
    public void removeReviewedProject(JPAAppProject project) {
        reviewedProjects.remove(project);
    }

    /**
     * Set the reviewed projects for the employee.
     * @param reviewedProjects The set of reviewed projects.
     */
    public void setReviewedProjects(Set reviewedProjects) {
        // workaround: create a new HashSet, because fostore does not
        // support LinkedHashSet
        this.reviewedProjects = 
            (reviewedProjects != null) ? new HashSet(reviewedProjects) : null;
    }

    /**
     * Get the employee's projects.
     * @return The employee's projects are returned as an unmodifiable
     * set. 
     */
    public Set getProjects() {
        return Collections.unmodifiableSet(projects);
    }

    /**
     * Add a project for the employee.
     * @param project The project.
     */
    public void addProject(JPAAppProject project) {
        projects.add(project);
    }

    /**
     * Remove a project from an employee's set of projects.
     * @param project The project.
     */
    public void removeProject(JPAAppProject project) {
        projects.remove(project);
    }

    /**
     * Set the projects for the employee.
     * @param projects The set of projects of the employee.
     */
    public void setProjects(Set projects) {
        // workaround: create a new HashSet, because fostore does not
        // support LinkedHashSet
        this.projects = (projects != null) ? new HashSet(projects) : null;
    }
    
    /**
     * Get the dental insurance of the employee.
     * @return The employee's dental insurance.
     */
    public IDentalInsurance getDentalInsurance() {
        return dentalInsurance;
    }

    /**
     * Set the dental insurance object for the employee.
     * @param dentalInsurance The dental insurance object to associate with
     * the employee. 
     */
    public void setDentalInsurance(IDentalInsurance dentalInsurance) {
        this.dentalInsurance = (JPAAppDentalInsurance)dentalInsurance;
    }
    /**
     * Get the medical insurance of the employee.
     * @return The employee's medical insurance.
     */
    public IMedicalInsurance getMedicalInsurance() {
        return medicalInsurance;
    }

    /**
     * Set the medical insurance object for the employee.
     * @param medicalInsurance The medical insurance object to associate
     * with the employee. 
     */
    public void setMedicalInsurance(IMedicalInsurance medicalInsurance) {
        this.medicalInsurance = (JPAAppMedicalInsurance)medicalInsurance;
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
        this.department = (JPAAppDepartment)department;
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
        this.fundingDept = (JPAAppDepartment)department;
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
        this.manager = (JPAAppEmployee)manager;
    }

    /**
     * Get the employee's team.
     * 
     * 
     * @return The set of <code>JPAAppEmployee</code>s on this employee's team,
     * returned as an unmodifiable set.
     */
    public Set getTeam() {
        return Collections.unmodifiableSet(team);
    }

    /**
     * Add an <code>JPAAppEmployee</code> to this employee's team.
     * This method sets both sides of the relationship, modifying
     * this employees team to include parameter emp and modifying
     * emp to set its manager attribute to this object.
     * 
     * 
     * @param emp The <code>JPAAppEmployee</code> to add to the team.
     */
    public void addToTeam(JPAAppEmployee emp) {
        team.add(emp);
        emp.manager = this;
    }

    /**
     * Remove an <code>JPAAppEmployee</code> from this employee's team.
     * This method will also set the <code>emp</code> manager to null.
     * 
     * 
     * @param emp The <code>JPAAppEmployee</code> to remove from the team.
     */
    public void removeFromTeam(JPAAppEmployee emp) {
        team.remove(emp);
        emp.manager = null;
    }

    /**
     * Set the employee's team.
     * 
     * 
     * @param team The set of <code>JPAAppEmployee</code>s.
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
        this.mentor = (JPAAppEmployee)mentor;
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
        this.protege = (JPAAppEmployee)protege;
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
        this.hradvisor = (JPAAppEmployee)hradvisor;
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
     * 
     * 
     * @return An unmodifiable <code>Set</code> containing the
     * <code>JPAAppEmployee</code>s that are HR advisees of this employee.
     */
    public Set getHradvisees() {
        return Collections.unmodifiableSet(hradvisees);
    }

    /**
     * Add an <code>JPAAppEmployee</code> as an advisee of this HR advisor. 
     * This method also sets the <code>emp</code> hradvisor to reference
     * this object. In other words, both sides of the relationship are
     * set. 
     * 
     * 
     * @param emp The employee to add as an advisee.
     */
    public void addAdvisee(JPAAppEmployee emp) {
        hradvisees.add(emp);
        emp.hradvisor = this;
    }

    /**
     * Remove an <code>JPAAppEmployee</code> as an advisee of this HR advisor.
     * This method also sets the <code>emp</code> hradvisor to null.
     * In other words, both sides of the relationship are set.
     * 
     * 
     * @param emp The employee to add as an HR advisee.
     */
    public void removeAdvisee(JPAAppEmployee emp) {
        hradvisees.remove(emp);
        emp.hradvisor = null;
    }

    /**
     * Set the HR advisees of this HR advisor.
     * 
     * 
     * @param hradvisees The <code>JPAAppEmployee</code>s that are HR advisees of
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
        reviewedProjects = new HashSet();
        projects = new HashSet();
        team = new HashSet();
        hradvisees = new HashSet();
    }

    /**
     * Return a String representation of a <code>JPAAppEmployee</code> object.
     * 
     * 
     * @return a String representation of a <code>JPAAppEmployee</code> object.
     */
    public String toString() {
        return "JPAAPPEmployee(" + getFieldRepr() + ")";
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
     * 
     * Returns <code>true</code> if all the fields of this instance are
     * deep equal to the corresponding fields of the specified JPAAppEmployee.
     * 
     * 
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
        JPAAppEmployee otherEmp = (JPAAppEmployee)other;
        String where = "Employee<" + getPersonid() + ">";
        return super.deepCompareFields(otherEmp, helper) &
            helper.equals(hiredate, otherEmp.getHiredate(),  where + ".hiredate") &
            helper.closeEnough(weeklyhours, otherEmp.getWeeklyhours(), where + ".weeklyhours") &
            helper.deepEquals(dentalInsurance, otherEmp.getDentalInsurance(), where + ".dentalInsurance") &
            helper.deepEquals(medicalInsurance, otherEmp.getMedicalInsurance(), where + ".medicalInsurance") &
            helper.deepEquals(department, otherEmp.getDepartment(), where + ".department") &
            helper.deepEquals(fundingDept, otherEmp.getFundingDept(), where + ".fundingDept") &
            helper.deepEquals(manager, otherEmp.getManager(), where + ".manager") &
            helper.deepEquals(mentor, otherEmp.getMentor(), where + ".mentor") &
            helper.deepEquals(protege, otherEmp.getProtege(), where + ".protege") &
            helper.deepEquals(hradvisor, otherEmp.getHradvisor(), where + ".hradvisor") &
            helper.deepEquals(reviewedProjects, otherEmp.getReviewedProjects(), where + ".reviewedProjects") &
            helper.deepEquals(projects, otherEmp.getProjects(), where + ".projects") &
            helper.deepEquals(team, otherEmp.getTeam(), where + ".team") &
            helper.deepEquals(hradvisees, otherEmp.getHradvisees(), where + ".hradvisees");
    }

}

