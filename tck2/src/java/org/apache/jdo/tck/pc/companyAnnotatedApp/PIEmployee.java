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
 
package org.apache.jdo.tck.pc.companyAnnotatedApp;

import javax.jdo.annotations.*;

import org.apache.jdo.tck.pc.company.IPerson;
import java.util.Date;
import java.util.Set;
import org.apache.jdo.tck.pc.company.IDentalInsurance;
import org.apache.jdo.tck.pc.company.IDepartment;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.pc.company.IMedicalInsurance;

/**
 * This interface represents the persistent state of Employee.
 * Javadoc was deliberately omitted because it would distract from
 * the purpose of the interface.
 */
@PersistenceCapable(identityType=IdentityType.APPLICATION)
@Inheritance(strategy=InheritanceStrategy.SUPERCLASS_TABLE)
public interface PIEmployee extends PIPerson, IPerson {

    @Column(name="HIREDATE")
    Date getHiredate();
    @Column(name="WEEKLYHOURS")
    double getWeeklyhours();
    @Property(persistenceModifier=FieldPersistenceModifier.PERSISTENT,
            mappedBy="reviewers")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedApp.PIProject.class)
    Set getReviewedProjects();
    @Property(persistenceModifier=FieldPersistenceModifier.PERSISTENT,
            mappedBy="members")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedApp.PIProject.class)
    Set getProjects();
    @Property(mappedBy="employee")
    IDentalInsurance getDentalInsurance();
    @Property(mappedBy="employee")
    IMedicalInsurance getMedicalInsurance();
    @Column(name="DEPARTMENT")
    IDepartment getDepartment();
    @Column(name="FUNDINGDEPT")
    IDepartment getFundingDept();
    @Column(name="MANAGER")
    IEmployee getManager();
    @Property(persistenceModifier=FieldPersistenceModifier.PERSISTENT,
            mappedBy="manager")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedApp.PIEmployee.class)
    Set getTeam();
    @Column(name="MENTOR")
    IEmployee getMentor();
    @Property(mappedBy="mentor")
    IEmployee getProtege();
    @Column(name="HRADVISOR")
    IEmployee getHradvisor();
    @Property(persistenceModifier=FieldPersistenceModifier.PERSISTENT)
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedApp.PIEmployee.class)
    Set getHradvisees();
    
    void setHiredate(Date hiredate);
    void setWeeklyhours(double weeklyhours);
    void setReviewedProjects(Set reviewedProjects);
    void setProjects(Set projects);
    void setDentalInsurance(IDentalInsurance dentalInsurance);
    void setMedicalInsurance(IMedicalInsurance medicalInsurance);
    void setDepartment(IDepartment department);
    void setFundingDept(IDepartment department);
    void setManager(IEmployee manager);
    void setTeam(Set team);
    void setMentor(IEmployee mentor);
    void setProtege(IEmployee protege);
    void setHradvisor(IEmployee hradvisor);
    void setHradvisees(Set hradvisees);
    
}
