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

package org.apache.jdo.tck.pc.company;

import javax.jdo.PersistenceManager;

/*
 * CompanyFactoryAnnotatedPMFieldClass.java
 *
 * This class uses the PersistenceManager.newInstance method with the concrete
 * class as a parameter.
 */
public class CompanyFactoryAnnotatedPMFieldClass
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
     * Creates a new instance of CompanyFactoryAnnotatedPMFieldClass
     */
    public CompanyFactoryAnnotatedPMFieldClass(PersistenceManager pm) {
        super(pm);
        if (isAppIdentity){
            addressClass =
                    org.apache.jdo.tck.pc.companyAnnotatedApp.FCAddress.class;
            dentalInsuranceClass =
                    org.apache.jdo.tck.pc.companyAnnotatedApp.FCDentalInsurance.class;
            medicalInsuranceClass =
                    org.apache.jdo.tck.pc.companyAnnotatedApp.FCMedicalInsurance.class;
            partTimeEmployeeClass =
                    org.apache.jdo.tck.pc.companyAnnotatedApp.FCPartTimeEmployee.class;
            fullTimeEmployeeClass =
                    org.apache.jdo.tck.pc.companyAnnotatedApp.FCFullTimeEmployee.class;
            projectClass =
                    org.apache.jdo.tck.pc.companyAnnotatedApp.FCProject.class;
            departmentClass =
                    org.apache.jdo.tck.pc.companyAnnotatedApp.FCDepartment.class;
            companyClass =
                    org.apache.jdo.tck.pc.companyAnnotatedApp.FCCompany.class;
        } else { //datastoreidentity
            addressClass =
                    org.apache.jdo.tck.pc.companyAnnotatedDS.FCAddress.class;
            dentalInsuranceClass = 
                    org.apache.jdo.tck.pc.companyAnnotatedDS.FCDentalInsurance.class;
            medicalInsuranceClass =
                    org.apache.jdo.tck.pc.companyAnnotatedDS.FCMedicalInsurance.class;
            partTimeEmployeeClass =
                    org.apache.jdo.tck.pc.companyAnnotatedDS.FCPartTimeEmployee.class;
            fullTimeEmployeeClass =
                    org.apache.jdo.tck.pc.companyAnnotatedDS.FCFullTimeEmployee.class;
            projectClass =
                    org.apache.jdo.tck.pc.companyAnnotatedDS.FCProject.class;
            departmentClass =
                    org.apache.jdo.tck.pc.companyAnnotatedDS.FCDepartment.class;
            companyClass =
                    org.apache.jdo.tck.pc.companyAnnotatedDS.FCCompany.class;
        }
        
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
