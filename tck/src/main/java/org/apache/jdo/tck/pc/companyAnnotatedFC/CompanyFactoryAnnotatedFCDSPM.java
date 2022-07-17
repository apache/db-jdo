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
 * CompanyFactoryAnnotatedFCDSPM.java
 *
 * This class uses the PersistenceManager.newInstance method with the concrete
 * class as a parameter.
 */
public class CompanyFactoryAnnotatedFCDSPM
        implements CompanyFactoryNewInstance {
    
    PersistenceManager pm = null;

    /**
     * Creates a new instance of CompanyFactoryAnnotatedFCDSPM
     * @param pm the PersistenceManager
     */
    public CompanyFactoryAnnotatedFCDSPM(PersistenceManager pm) {
        this.pm = pm;        
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<?>[] getTearDownClasses() {
        return new Class[] {
            FCDSDentalInsurance.class,
            FCDSMedicalInsurance.class,
            FCDSPartTimeEmployee.class,
            FCDSFullTimeEmployee.class,
            FCDSProject.class,
            FCDSDepartment.class,
            FCDSMeetingRoom.class,
            FCDSCompany.class,
            FCDSAddress.class
        };
    }
    
    public IAddress newAddress() {
        return pm.newInstance(FCDSAddress.class);
    }

    public IMeetingRoom newMeetingRoom() {
        return pm.newInstance(FCDSMeetingRoom.class);
    }
    
    public ICompany newCompany() {
        return pm.newInstance(FCDSCompany.class);
    }
    
    public IDentalInsurance newDentalInsurance() {
        return pm.newInstance(FCDSDentalInsurance.class);
    }
    
    public IDepartment newDepartment() {
        return pm.newInstance(FCDSDepartment.class);
    }
    
    public IFullTimeEmployee newFullTimeEmployee() {
        return pm.newInstance(FCDSFullTimeEmployee.class);
    }
    
    public IMedicalInsurance newMedicalInsurance() {
        return pm.newInstance(FCDSMedicalInsurance.class);
    }
    
    public IPartTimeEmployee newPartTimeEmployee() {
        return pm.newInstance(FCDSPartTimeEmployee.class);
    }
    
    public IProject newProject() {
        return pm.newInstance(FCDSProject.class);
    }
}
