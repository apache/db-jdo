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
 * CompanyFactoryPMClass.java
 *
 * This class uses the PersistenceManager.newInstance method with the concrete
 * class as a parameter. 
 */
public class CompanyFactoryPMClass 
        extends CompanyFactoryAbstractImpl {
    
    /** */
    public static final Class[] tearDownClasses = new Class[] {
        DentalInsurance.class, MedicalInsurance.class,
        PartTimeEmployee.class, FullTimeEmployee.class,  
        Project.class, Department.class, Company.class, MeetingRoom.class
    };

    /**
     * Creates a new instance of CompanyFactoryPMInterface 
     */
    public CompanyFactoryPMClass(PersistenceManager pm) {
        super(pm);
    }
    
    public IAddress newAddress() {
        return (IAddress)pm.newInstance(Address.class);
    }

    public IMeetingRoom newMeetingRoom() {
        return (IMeetingRoom)pm.newInstance(MeetingRoom.class);
    }
    
    public ICompany newCompany() {
        return (ICompany)pm.newInstance(Company.class);
    }
    
    public IDentalInsurance newDentalInsurance() {
        return (IDentalInsurance)pm.newInstance(DentalInsurance.class);
    }
    
    public IDepartment newDepartment() {
        return (IDepartment)pm.newInstance(Department.class);
    }
    
    public IFullTimeEmployee  newFullTimeEmployee() {
        return (IFullTimeEmployee)pm.newInstance(FullTimeEmployee.class);
    }
    
    public IMedicalInsurance newMedicalInsurance() {
        return (IMedicalInsurance)pm.newInstance(MedicalInsurance.class);        
    }
    
    public IPartTimeEmployee  newPartTimeEmployee() {
        return (IPartTimeEmployee)pm.newInstance(PartTimeEmployee.class);
    }
    
    public IProject newProject() {
        return (IProject)pm.newInstance(Project.class);
    }

    public Class[] getTearDownClasses() {
        return tearDownClasses;
    }
}
