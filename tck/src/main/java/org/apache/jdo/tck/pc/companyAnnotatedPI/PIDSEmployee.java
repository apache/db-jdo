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
 
package org.apache.jdo.tck.pc.companyAnnotatedPI;

import javax.jdo.annotations.*;

import java.util.Date;
import java.util.Set;
import org.apache.jdo.tck.pc.company.IDentalInsurance;
import org.apache.jdo.tck.pc.company.IDepartment;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.pc.company.IMedicalInsurance;
import org.apache.jdo.tck.pc.company.IProject;

/**
 * This interface represents the persistent state of Employee.
 * Javadoc was deliberately omitted because it would distract from
 * the purpose of the interface.
 */
@PersistenceCapable
@Inheritance(strategy=InheritanceStrategy.SUPERCLASS_TABLE)
public interface PIDSEmployee extends IEmployee, PIDSPerson {

    @Column(name="HIREDATE")
    Date getHiredate();
    @Column(name="WEEKLYHOURS")
    double getWeeklyhours();
    @Persistent(mappedBy="reviewers")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSProject.class)
    Set<IProject> getReviewedProjects();
    @Persistent(mappedBy="members")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSProject.class)
    Set<IProject> getProjects();
    @Persistent(mappedBy="employee",types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSDentalInsurance.class)
    IDentalInsurance getDentalInsurance();
    @Persistent(mappedBy="employee",types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSMedicalInsurance.class)
    IMedicalInsurance getMedicalInsurance();
    @Persistent(types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSDepartment.class)
    @Column(name="DEPARTMENT")
    IDepartment getDepartment();
    @Persistent(types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSDepartment.class)
    @Column(name="FUNDINGDEPT")
    IDepartment getFundingDept();
    @Persistent(types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSEmployee.class)
    @Column(name="MANAGER")
    IEmployee getManager();
    @Persistent(mappedBy="manager")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSEmployee.class)
    Set<IEmployee> getTeam();
    @Persistent(types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSEmployee.class)
    @Column(name="MENTOR")
    IEmployee getMentor();
    @Persistent(types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSEmployee.class, mappedBy="mentor")
    IEmployee getProtege();
    @Persistent(types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSEmployee.class)
    @Column(name="HRADVISOR")
    IEmployee getHradvisor();
    @Persistent(mappedBy="hradvisor")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSEmployee.class)
    Set<IEmployee> getHradvisees();
    
    void setHiredate(Date hiredate);
    void setWeeklyhours(double weeklyhours);
    void setReviewedProjects(Set<IProject> reviewedProjects);
    void setProjects(Set<IProject> projects);
    void setDentalInsurance(IDentalInsurance dentalInsurance);
    void setMedicalInsurance(IMedicalInsurance medicalInsurance);
    void setDepartment(IDepartment department);
    void setFundingDept(IDepartment department);
    void setManager(IEmployee manager);
    void setTeam(Set<IEmployee> team);
    void setMentor(IEmployee mentor);
    void setProtege(IEmployee protege);
    void setHradvisor(IEmployee hradvisor);
    void setHradvisees(Set<IEmployee> hradvisees);
    
}
