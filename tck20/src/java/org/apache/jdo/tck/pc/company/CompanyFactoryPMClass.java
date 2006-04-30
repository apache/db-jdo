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

package org.apache.jdo.tck.pc.company;

import javax.jdo.PersistenceManager;

/*
 * CompanyFactoryPMInterface.java
 *
 * This class uses the PersistenceManager.newInstance method with the concrete
 * class as a parameter. 
 */
public class CompanyFactoryPMClass 
        extends CompanyFactoryAbstractImpl {
    
    /** */
    public static final Class[] tearDownClasses = new Class[] {
        DentalInsurance.class, MedicalInsurance.class,
        Person.class, Employee.class, 
        PartTimeEmployee.class, FullTimeEmployee.class,  
        Project.class, Department.class, Company.class
    };

    /**
     * Creates a new instance of CompanyFactoryPMInterface 
     */
    public CompanyFactoryPMClass(PersistenceManager pm) {
        super(pm);
    }
    
    IAddress newAddress() {
        return (IAddress)pm.newInstance(Address.class);
    }
    
    ICompany newCompany() {
        return (ICompany)pm.newInstance(Company.class);
    }
    
    IDentalInsurance newDentalInsurance() {
        return (IDentalInsurance)pm.newInstance(DentalInsurance.class);
    }
    
    IDepartment newDepartment() {
        return (IDepartment)pm.newInstance(Department.class);
    }
    
    IFullTimeEmployee  newFullTimeEmployee() {
        return (IFullTimeEmployee)pm.newInstance(FullTimeEmployee.class);
    }
    
    IMedicalInsurance newMedicalInsurance() {
        return (IMedicalInsurance)pm.newInstance(MedicalInsurance.class);        
    }
    
    IPartTimeEmployee  newPartTimeEmployee() {
        return (IPartTimeEmployee)pm.newInstance(PartTimeEmployee.class);
    }
    
    IProject newProject() {
        return (IProject)pm.newInstance(Project.class);
    }

    public Class[] getTearDownClasses() {
        return tearDownClasses;
    }
}
