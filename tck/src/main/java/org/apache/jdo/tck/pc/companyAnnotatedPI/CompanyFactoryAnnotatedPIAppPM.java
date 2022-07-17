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

package org.apache.jdo.tck.pc.companyAnnotatedPI;

import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.*;

/*
 * CompanyFactoryAnnotatedPMFieldClass.java
 *
 * This class uses the PersistenceManager.newInstance method with the concrete
 * class as a parameter.
 */
public class CompanyFactoryAnnotatedPIAppPM
        implements CompanyFactoryNewInstance {
    
    PersistenceManager pm = null;

    /**
     * Creates a new instance of CompanyFactoryAnnotatedPMFieldClass
     * @param pm the PersistenceManager
     */
    public CompanyFactoryAnnotatedPIAppPM(PersistenceManager pm) {
        this.pm = pm;        
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class<?>[] getTearDownClasses() {
        return new Class[] {
            PIAppDentalInsurance.class,
            PIAppMedicalInsurance.class,
            PIAppPartTimeEmployee.class,
            PIAppFullTimeEmployee.class,
            PIAppProject.class,
            PIAppDepartment.class,
            PIAppCompany.class,
            PIAppAddress.class
        };
    }
    
    public IAddress newAddress() {
        return pm.newInstance(PIAppAddress.class);
    }

    public IMeetingRoom newMeetingRoom() {
        return null;
    }
    
    public ICompany newCompany() {
        return pm.newInstance(PIAppCompany.class);
    }
    
    public IDentalInsurance newDentalInsurance() {
        return pm.newInstance(PIAppDentalInsurance.class);
    }
    
    public IDepartment newDepartment() {
        return pm.newInstance(PIAppDepartment.class);
    }
    
    public IFullTimeEmployee newFullTimeEmployee() {
        return pm.newInstance(PIAppFullTimeEmployee.class);
    }
    
    public IMedicalInsurance newMedicalInsurance() {
        return pm.newInstance(PIAppMedicalInsurance.class);
    }
    
    public IPartTimeEmployee newPartTimeEmployee() {
        return pm.newInstance(PIAppPartTimeEmployee.class);
    }
    
    public IProject newProject() {
        return pm.newInstance(PIAppProject.class);
    }
}
