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
     * Creates a new instance of CompanyFactoryAnnotatedFCAppConcrete
     * @param pm the PersistenceManager
     */
    public CompanyFactoryAnnotatedFCAppConcrete(PersistenceManager pm) {
        this.pm = pm;        
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<?>[] getTearDownClasses() {
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
        return new FCAppAddress();
    }

    public IMeetingRoom newMeetingRoom() {
        return new FCAppMeetingRoom();
    }
    
    public ICompany newCompany() {
        return new FCAppCompany();
    }
    
    public IDentalInsurance newDentalInsurance() {
        return new FCAppDentalInsurance();
    }
    
    public IDepartment newDepartment() {
        return new FCAppDepartment();
    }
    
    public IFullTimeEmployee newFullTimeEmployee() {
        return new FCAppFullTimeEmployee();
    }
    
    public IMedicalInsurance newMedicalInsurance() {
        return new FCAppMedicalInsurance();
    }
    
    public IPartTimeEmployee newPartTimeEmployee() {
        return new FCAppPartTimeEmployee();
    }
    
    public IProject newProject() {
        return new FCAppProject();
    }
}
