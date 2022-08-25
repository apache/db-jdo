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

import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.*;

/*
 * CompanyFactoryAnnotatedPMInterface.java
 *
 */
public class CompanyFactoryAnnotatedPMInterface 
        extends CompanyFactoryAbstractImpl {
    
    Class addressClass = null;
    Class dentalInsuranceClass = null;
    Class medicalInsuranceClass = null;
    Class partTimeEmployeeClass = null;
    Class fullTimeEmployeeClass = null;
    Class projectClass = null;
    Class departmentClass = null;
    Class companyClass = null;
    
    /** */
    public  final Class[] tearDownClasses = new Class[] {
        dentalInsuranceClass,
        medicalInsuranceClass,
        partTimeEmployeeClass,
        fullTimeEmployeeClass,
        projectClass,
        departmentClass,
        companyClass
    };

    /**
     * Creates a new instance of CompanyFactoryAnnotatedPMInterface
     * @param pm the PersistenceManager
     */
    public CompanyFactoryAnnotatedPMInterface(PersistenceManager pm) {
        super(pm);

        if (isAppIdentity){
            addressClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppAddress.class;
            dentalInsuranceClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppDentalInsurance.class;
            medicalInsuranceClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppMedicalInsurance.class;
            partTimeEmployeeClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppPartTimeEmployee.class;
            fullTimeEmployeeClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppFullTimeEmployee.class;
            projectClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppProject.class;
            departmentClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppDepartment.class;
            companyClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppCompany.class;
        } else { //datastoreidentity
            addressClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSAddress.class;
            dentalInsuranceClass = 
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSDentalInsurance.class;
            medicalInsuranceClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSMedicalInsurance.class;
            partTimeEmployeeClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSPartTimeEmployee.class;
            fullTimeEmployeeClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSFullTimeEmployee.class;
            projectClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSProject.class;
            departmentClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSDepartment.class;
            companyClass =
                    org.apache.jdo.tck.pc.companyAnnotatedPI.PIDSCompany.class;
        }
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

    public Class[] getTearDownClasses() {
        return tearDownClasses;
    }
}
