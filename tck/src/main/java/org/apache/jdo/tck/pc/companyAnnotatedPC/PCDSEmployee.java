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
 
package org.apache.jdo.tck.pc.companyAnnotatedPC;

import javax.jdo.annotations.*;

import java.io.ObjectInputStream;
import java.io.IOException;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.jdo.tck.pc.company.IDentalInsurance;
import org.apache.jdo.tck.pc.company.IDepartment;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.pc.company.IMedicalInsurance;
import org.apache.jdo.tck.pc.company.IProject;
import org.apache.jdo.tck.util.EqualityHelper;
import org.apache.jdo.tck.util.JDOCustomDateEditor;

/**
 * This class represents an employee.
 */
@PersistenceCapable
@Inheritance(strategy=InheritanceStrategy.SUPERCLASS_TABLE)
@DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="DATASTORE_IDENTITY")
public abstract class PCDSEmployee extends PCDSPerson implements IEmployee {

    private static final long serialVersionUID = 1L;

    @NotPersistent()
    private Date             _hiredate;
    @NotPersistent()
    private double           _weeklyhours;
    @NotPersistent()
    private PCDSDentalInsurance  _dentalInsurance;
    @NotPersistent()
    private PCDSMedicalInsurance _medicalInsurance;
    @NotPersistent()
    private PCDSDepartment       _department;
    @NotPersistent()
    private PCDSDepartment       _fundingDept;
    @NotPersistent()
    private PCDSEmployee         _manager;
    @NotPersistent()
    private PCDSEmployee         _mentor;
    @NotPersistent()
    private PCDSEmployee         _protege;
    @NotPersistent()
    private PCDSEmployee         _hradvisor;
    @NotPersistent()
    private transient Set<IProject> _reviewedProjects = new HashSet<>();
    @NotPersistent()
    private transient Set<IProject> _projects = new HashSet<>();
    @NotPersistent()
    private transient Set<IEmployee> _team = new HashSet<>();
    @NotPersistent()
    private transient Set<IEmployee> _hradvisees = new HashSet<>();


    /** This is the JDO-required no-args constructor */
    protected PCDSEmployee() {}

    /**
     * Construct an <code>PCDSEmployee</code> instance.
     * 
     * 
     * @param personid The identifier for the person.
     * @param firstname The first name of the employee.
     * @param lastname The last name of the employee.
     * @param middlename The middle name of the employee.
     * @param birthdate The birth date of the employee.
     * @param hiredate The date that the employee was hired.
     */
    public PCDSEmployee(long personid, String firstname, String lastname, 
                    String middlename, Date birthdate,
                    Date hiredate) {
        super(personid, firstname, lastname, middlename, birthdate);
        this._hiredate = hiredate;
    }

    /**
     * Construct an <code>PCDSEmployee</code> instance.
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
    public PCDSEmployee(long personid, String firstname, String lastname, 
                    String middlename, Date birthdate, PCDSAddress address,
                    Date hiredate) {
        super(personid, firstname, lastname, middlename, birthdate, address);
        this._hiredate = hiredate;
    }

    /**
     * Get the date that the employee was hired.
     * @return The date the employee was hired.
     */

    @Column(name="HIREDATE")
    public Date getHiredate() {
        return _hiredate;
    }

    /**
     * Set the date that the employee was hired.
     * @param hiredate The date the employee was hired.
     */
    public void setHiredate(Date hiredate) {
        this._hiredate = hiredate;
    }

    /**
     * Get the weekly hours of the employee.
     * @return The number of hours per week that the employee works.
     */

    @Column(name="WEEKLYHOURS")
    public double getWeeklyhours() {
        return _weeklyhours;
    }

    /**
     * Set the number of hours per week that the employee works.
     * @param weeklyhours The number of hours per week that the employee
     * works. 
     */
    public void setWeeklyhours(double weeklyhours) {
        this._weeklyhours = weeklyhours;
    }

    /**
     * Get the reviewed projects.
     * @return The reviewed projects.
     */

    @Persistent(mappedBy="reviewers")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSProject.class)
    public Set<IProject> getReviewedProjects() {
        return _reviewedProjects;
    }

    /**
     * Add a reviewed project.
     * @param project A reviewed project.
     */
    public void addReviewedProjects(PCDSProject project) {
        _reviewedProjects.add(project);
    }

    /**
     * Remove a reviewed project.
     * @param project A reviewed project.
     */
    public void removeReviewedProject(PCDSProject project) {
        _reviewedProjects.remove(project);
    }

    /**
     * Set the reviewed projects for the employee.
     * @param reviewedProjects The set of reviewed projects.
     */
    public void setReviewedProjects(Set<IProject> reviewedProjects) {
        // workaround: create a new HashSet, because fostore does not
        // support LinkedHashSet
        this._reviewedProjects = 
            (reviewedProjects != null) ? new HashSet<>(reviewedProjects) : null;
    }

    /**
     * Get the employee's projects.
     * @return The employee's projects. 
     */

    @Persistent(mappedBy="members")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSProject.class)
    public Set<IProject> getProjects() {
        return _projects;
    }

    /**
     * Add a project for the employee.
     * @param project The project.
     */
    public void addProject(PCDSProject project) {
        _projects.add(project);
    }

    /**
     * Remove a project from an employee's set of projects.
     * @param project The project.
     */
    public void removeProject(PCDSProject project) {
        _projects.remove(project);
    }

    /**
     * Set the projects for the employee.
     * @param projects The set of projects of the employee.
     */
    public void setProjects(Set<IProject> projects) {
        // workaround: create a new HashSet, because fostore does not
        // support LinkedHashSet
        this._projects = (projects != null) ? new HashSet<>(projects) : null;
    }
    
    /**
     * Get the dental insurance of the employee.
     * @return The employee's dental insurance.
     */

    @Persistent(mappedBy="employee",
        types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSDentalInsurance.class)
    public IDentalInsurance getDentalInsurance() {
        return _dentalInsurance;
    }

    /**
     * Set the dental insurance object for the employee.
     * @param dentalInsurance The dental insurance object to associate with
     * the employee. 
     */
    public void setDentalInsurance(IDentalInsurance dentalInsurance) {
        this._dentalInsurance = (PCDSDentalInsurance)dentalInsurance;
    }
    
    /**
     * Get the medical insurance of the employee.
     * @return The employee's medical insurance.
     */
    @Persistent(mappedBy="employee",
        types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSMedicalInsurance.class)
        public IMedicalInsurance getMedicalInsurance() {
        return _medicalInsurance;
    }

    /**
     * Set the medical insurance object for the employee.
     * @param medicalInsurance The medical insurance object to associate
     * with the employee. 
     */
    public void setMedicalInsurance(IMedicalInsurance medicalInsurance) {
        this._medicalInsurance = (PCDSMedicalInsurance)medicalInsurance;
    }

    /**
     * Get the employee's department.
     * @return The department associated with the employee.
     */

    @Persistent(types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSDepartment.class)
    @Column(name="DEPARTMENT")
    public IDepartment getDepartment() {
        return _department;
    }

    /**
     * Set the employee's department.
     * @param department The department.
     */
    public void setDepartment(IDepartment department) {
        this._department = (PCDSDepartment)department;
    }

    /**
     * Get the employee's funding department.
     * @return The funding department associated with the employee.
     */

    @Persistent(types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSDepartment.class)
    @Column(name="FUNDINGDEPT")
    public IDepartment getFundingDept() {
        return _fundingDept;
    }

    /**
     * Set the employee's funding department.
     * @param department The funding department.
     */
    public void setFundingDept(IDepartment department) {
        this._fundingDept = (PCDSDepartment)department;
    }

    /**
     * Get the employee's manager.
     * @return The employee's manager.
     */

    @Persistent(types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSEmployee.class)
    @Column(name="MANAGER")
    public IEmployee getManager() {
        return _manager;
    }

    /**
     * Set the employee's manager.
     * @param manager The employee's manager.
     */
    public void setManager(IEmployee manager) {
        this._manager = (PCDSEmployee)manager;
    }

    /**
     * Get the employee's team.
     * 
     * 
     * @return The set of <code>PCDSEmployee</code>s on this employee's team.
     */

    @Persistent(mappedBy="manager")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSEmployee.class)
    public Set<IEmployee> getTeam() {
        return _team;
    }

    /**
     * Add an <code>PCDSEmployee</code> to this employee's team.
     * This method sets both sides of the relationship, modifying
     * this employees team to include parameter emp and modifying
     * emp to set its manager attribute to this object.
     * 
     * 
     * @param emp The <code>PCDSEmployee</code> to add to the team.
     */
    public void addToTeam(PCDSEmployee emp) {
        _team.add(emp);
        emp._manager = this;
    }

    /**
     * Remove an <code>PCDSEmployee</code> from this employee's team.
     * This method will also set the <code>emp</code> manager to null.
     * 
     * 
     * @param emp The <code>PCDSEmployee</code> to remove from the team.
     */
    public void removeFromTeam(PCDSEmployee emp) {
        _team.remove(emp);
        emp._manager = null;
    }

    /**
     * Set the employee's team.
     * 
     * 
     * @param team The set of <code>PCDSEmployee</code>s.
     */
    public void setTeam(Set<IEmployee> team) {
        // workaround: create a new HashSet, because fostore does not
        // support LinkedHashSet
        this._team = (team != null) ? new HashSet<>(team) : null;
    }

    /**
     * Set the mentor for this employee. 
     * @param mentor The mentor for this employee.
     */
    public void setMentor(IEmployee mentor) {
        this._mentor = (PCDSEmployee)mentor;
    }

    /**
     * Get the mentor for this employee.
     * @return The mentor.
     */

    @Persistent(types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSEmployee.class)
    @Column(name="MENTOR")
    public IEmployee getMentor() {
        return _mentor;
    }

    /**
     * Set the protege for this employee.
     * @param protege The protege for this employee.
     */
    public void setProtege(IEmployee protege) {
        this._protege = (PCDSEmployee)protege;
    }

    /**
     * Get the protege of this employee.
     * @return The protege of this employee.
     */

    @Persistent(mappedBy="mentor",
        types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSEmployee.class)
    public IEmployee getProtege() {
        return _protege;
    }

    /**
     * Set the HR advisor for this employee.
     * @param hradvisor The hradvisor for this employee.
     */
    public void setHradvisor(IEmployee hradvisor) {
        this._hradvisor = (PCDSEmployee)hradvisor;
    }

    /**
     * Get the HR advisor for the employee.
     * @return The HR advisor.
     */

    @Persistent(types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSEmployee.class)
    @Column(name="HRADVISOR")
    public IEmployee getHradvisor() {
        return _hradvisor;
    }

    /**
     * Get the HR advisees of this HR advisor.
     * 
     * 
     * @return The <code>PCDSEmployee</code>s that are HR advisees of this employee.
     */

    @Persistent(mappedBy="hradvisor")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSEmployee.class)
    public Set<IEmployee> getHradvisees() {
        return _hradvisees;
    }

    /**
     * Add an <code>PCDSEmployee</code> as an advisee of this HR advisor. 
     * This method also sets the <code>emp</code> hradvisor to reference
     * this object. In other words, both sides of the relationship are
     * set. 
     * 
     * 
     * @param emp The employee to add as an advisee.
     */
    public void addAdvisee(PCDSEmployee emp) {
        _hradvisees.add(emp);
        emp._hradvisor = this;
    }

    /**
     * Remove an <code>PCDSEmployee</code> as an advisee of this HR advisor.
     * This method also sets the <code>emp</code> hradvisor to null.
     * In other words, both sides of the relationship are set.
     * 
     * 
     * @param emp The employee to add as an HR advisee.
     */
    public void removeAdvisee(PCDSEmployee emp) {
        _hradvisees.remove(emp);
        emp._hradvisor = null;
    }

    /**
     * Set the HR advisees of this HR advisor.
     * 
     * 
     * @param hradvisees The <code>PCDSEmployee</code>s that are HR advisees of
     * this employee.
     */
    public void setHradvisees(Set<IEmployee> hradvisees) {
        // workaround: create a new HashSet, because fostore does not
        // support LinkedHashSet
        this._hradvisees = (hradvisees != null) ? new HashSet<>(hradvisees) : null;
    }

    /**
     * Serialization support: initialize transient fields.
     * @param in stream
     * @throws IOException error during reading
     * @throws ClassNotFoundException class could not be found
     */
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        _reviewedProjects = new HashSet<>();
        _projects = new HashSet<>();
        _team = new HashSet<>();
        _hradvisees = new HashSet<>();
    }

    /**
     * Return a String representation of a <code>PCDSEmployee</code> object.
     * 
     * 
     * @return a String representation of a <code>PCDSEmployee</code> object.
     */
    @Override
    public String toString() {
        return "FCEmployee(" + getFieldRepr() + ")";
    }

    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    @Override
    protected String getFieldRepr() {
        StringBuilder rc = new StringBuilder();
        rc.append(super.getFieldRepr());
        rc.append(", hired ").append(JDOCustomDateEditor.getDateRepr(_hiredate));
        rc.append(", weeklyhours ").append(_weeklyhours);
        return rc.toString();
    }

    /**
     * 
     * Returns <code>true</code> if all the fields of this instance are
     * deep equal to the corresponding fields of the specified PCDSEmployee.
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
    @Override
    public boolean deepCompareFields(Object other,
                                     EqualityHelper helper) {
        PCDSEmployee otherEmp = (PCDSEmployee)other;
        String where = "Employee<" + getPersonid() + ">";
        return super.deepCompareFields(otherEmp, helper) &
            helper.equals(_hiredate, otherEmp.getHiredate(),  where + ".hiredate") &
            helper.closeEnough(_weeklyhours, otherEmp.getWeeklyhours(), where + ".weeklyhours") &
            helper.deepEquals(_dentalInsurance, otherEmp.getDentalInsurance(), where + ".dentalInsurance") &
            helper.deepEquals(_medicalInsurance, otherEmp.getMedicalInsurance(), where + ".medicalInsurance") &
            helper.deepEquals(_department, otherEmp.getDepartment(), where + ".department") &
            helper.deepEquals(_fundingDept, otherEmp.getFundingDept(), where + ".fundingDept") &
            helper.deepEquals(_manager, otherEmp.getManager(), where + ".manager") &
            helper.deepEquals(_mentor, otherEmp.getMentor(), where + ".mentor") &
            helper.deepEquals(_protege, otherEmp.getProtege(), where + ".protege") &
            helper.deepEquals(_hradvisor, otherEmp.getHradvisor(), where + ".hradvisor") &
            helper.deepEquals(_reviewedProjects, otherEmp.getReviewedProjects(), where + ".reviewedProjects") &
            helper.deepEquals(_projects, otherEmp.getProjects(), where + ".projects") &
            helper.deepEquals(_team, otherEmp.getTeam(), where + ".team") &
            helper.deepEquals(_hradvisees, otherEmp.getHradvisees(), where + ".hradvisees");
    }

}

