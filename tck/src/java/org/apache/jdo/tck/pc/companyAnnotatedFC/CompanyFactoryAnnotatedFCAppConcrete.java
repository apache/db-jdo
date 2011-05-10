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

package org.apache.jdo.tck.pc.companyAnnotatedFC;

import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.*;

/*
 * CompanyFactoryAnnotatedPMFieldClass.java
 *
 * This class uses the PersistenceManager.newInstance method with the concrete
 * class as a parameter.
 */
public class CompanyFactoryAnnotatedFCAppConcrete
        implements CompanyFactoryNewInstance {
    
    PersistenceManager pm = null;
    
    /**
     * Creates a new instance of CompanyFactoryAnnotatedPMFieldClass
     */
    public CompanyFactoryAnnotatedFCAppConcrete(PersistenceManager pm) {
        this.pm = pm;        
    }
    
    public Class[] getTearDownClasses() {
        return new Class[] {
            FCAppDentalInsurance.class,
            FCAppMedicalInsurance.class,
            FCAppPartTimeEmployee.class,
            FCAppFullTimeEmployee.class,
            FCAppProject.class,
            FCAppDepartment.class,
            FCAppMeetingRoom.class,
            FCAppCompany.class,
            FCAppAddress.class
        };
    }
    
    public IAddress newAddress() {
        return (IAddress) new FCAppAddress();
    }

    public IMeetingRoom newMeetingRoom() {
        return (IMeetingRoom) new FCAppMeetingRoom();
    }
    
    public ICompany newCompany() {
        return (ICompany) new FCAppCompany();
    }
    
    public IDentalInsurance newDentalInsurance() {
        return (IDentalInsurance) new FCAppDentalInsurance();
    }
    
    public IDepartment newDepartment() {
        return (IDepartment) new FCAppDepartment();
    }
    
    public IFullTimeEmployee newFullTimeEmployee() {
        return (IFullTimeEmployee) new FCAppFullTimeEmployee();
    }
    
    public IMedicalInsurance newMedicalInsurance() {
        return (IMedicalInsurance) new FCAppMedicalInsurance();
    }
    
    public IPartTimeEmployee newPartTimeEmployee() {
        return (IPartTimeEmployee) new FCAppPartTimeEmployee();
    }
    
    public IProject newProject() {
        return (IProject) new FCAppProject();
    }
}
