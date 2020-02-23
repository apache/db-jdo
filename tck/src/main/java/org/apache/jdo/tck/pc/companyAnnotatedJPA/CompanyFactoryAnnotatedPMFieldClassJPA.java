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

import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.*;

/*
 * CompanyFactoryAnnotatedPMFieldClassJPA.java
 *
 * This class uses the PersistenceManager.newInstance method with the concrete
 * class as a parameter.
 */
public class CompanyFactoryAnnotatedPMFieldClassJPA
        extends org.apache.jdo.tck.pc.company.CompanyFactoryAbstractImpl {
    
    Class addressClass = null;
    Class dentalInsuranceClass = null;
    Class medicalInsuranceClass = null;
    Class partTimeEmployeeClass = null;
    Class fullTimeEmployeeClass = null;
    Class projectClass = null;
    Class departmentClass = null;
    Class companyClass = null;

    /**
     * Creates a new instance of CompanyFactoryAnnotatedPMFieldClassJPA
     * @param pm the PersistenceManager
     */
    public CompanyFactoryAnnotatedPMFieldClassJPA(PersistenceManager pm) {
        super(pm);
            addressClass =
                    org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppAddress.class;
            dentalInsuranceClass =
                    org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppDentalInsurance.class;
            medicalInsuranceClass =
                    org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppMedicalInsurance.class;
            partTimeEmployeeClass  =
                    org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppPartTimeEmployee.class;
            fullTimeEmployeeClass =
                    org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppFullTimeEmployee.class;
            projectClass =
                    org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppProject.class;
            departmentClass =
                    org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppDepartment.class;
            companyClass =
                    org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppCompany.class;
    }
    
    public Class[] getTearDownClasses() {
        return new Class[] {
            dentalInsuranceClass,
            medicalInsuranceClass,
            partTimeEmployeeClass,
            fullTimeEmployeeClass,
            projectClass,
            departmentClass,
            companyClass
        };
    }
    
    public IAddress newAddress() {
        return (IAddress)pm.newInstance(addressClass);
    }

    public IMeetingRoom newMeetingRoom() {
        return (IMeetingRoom)null;
    }
    
    public ICompany newCompany() {
        return (ICompany)pm.newInstance(companyClass);
    }
    
    public IDentalInsurance newDentalInsurance() {
        return (IDentalInsurance)pm.newInstance(dentalInsuranceClass);
    }
    
    public IDepartment newDepartment() {
        return (IDepartment)pm.newInstance(departmentClass);
    }
    
    public IFullTimeEmployee newFullTimeEmployee() {
        return (IFullTimeEmployee)pm.newInstance(fullTimeEmployeeClass);
    }
    
    public IMedicalInsurance newMedicalInsurance() {
        return (IMedicalInsurance)pm.newInstance(medicalInsuranceClass);
    }
    
    public IPartTimeEmployee newPartTimeEmployee() {
        return (IPartTimeEmployee)pm.newInstance(partTimeEmployeeClass);
    }
    
    public IProject newProject() {
        return (IProject)pm.newInstance(projectClass);
    }
}
