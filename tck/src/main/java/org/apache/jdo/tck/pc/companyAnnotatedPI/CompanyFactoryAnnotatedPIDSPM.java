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
 * CompanyFactoryAnnotatedPIDSPM.java
 *
 * This class uses the PersistenceManager.newInstance method with the concrete
 * class as a parameter.
 */
public class CompanyFactoryAnnotatedPIDSPM
        implements CompanyFactoryNewInstance {
    
    PersistenceManager pm = null;

    /**
     * Creates a new instance of CompanyFactoryAnnotatedPIDSPM
     * @param pm the PersistenceManager
     */
    public CompanyFactoryAnnotatedPIDSPM(PersistenceManager pm) {
        this.pm = pm;        
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<?>[] getTearDownClasses() {
        return new Class[] {
            PIDSDentalInsurance.class,
            PIDSMedicalInsurance.class,
            PIDSPartTimeEmployee.class,
            PIDSFullTimeEmployee.class,
            PIDSProject.class,
            PIDSDepartment.class,
            PIDSCompany.class,
            PIDSAddress.class
        };
    }
    
    public IAddress newAddress() {
        return pm.newInstance(PIDSAddress.class);
    }

    public IMeetingRoom newMeetingRoom() {
        return null;
    }
    
    public ICompany newCompany() {
        return pm.newInstance(PIDSCompany.class);
    }
    
    public IDentalInsurance newDentalInsurance() {
        return pm.newInstance(PIDSDentalInsurance.class);
    }
    
    public IDepartment newDepartment() {
        return pm.newInstance(PIDSDepartment.class);
    }
    
    public IFullTimeEmployee newFullTimeEmployee() {
        return pm.newInstance(PIDSFullTimeEmployee.class);
    }
    
    public IMedicalInsurance newMedicalInsurance() {
        return pm.newInstance(PIDSMedicalInsurance.class);
    }
    
    public IPartTimeEmployee newPartTimeEmployee() {
        return pm.newInstance(PIDSPartTimeEmployee.class);
    }
    
    public IProject newProject() {
        return pm.newInstance(PIDSProject.class);
    }
}
