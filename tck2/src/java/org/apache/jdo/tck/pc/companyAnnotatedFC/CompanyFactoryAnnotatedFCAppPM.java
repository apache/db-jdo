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
public class CompanyFactoryAnnotatedFCAppPM
        implements CompanyFactoryNewInstance {
    
    PersistenceManager pm = null;
    
    /**
     * Creates a new instance of CompanyFactoryAnnotatedPMFieldClass
     */
    public CompanyFactoryAnnotatedFCAppPM(PersistenceManager pm) {
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
            FCAppCompany.class,
            FCAppAddress.class
        };
    }
    
    public IAddress newAddress() {
        return (IAddress)pm.newInstance(FCAppAddress.class);
    }
    
    public ICompany newCompany() {
        return (ICompany)pm.newInstance(FCAppCompany.class);
    }
    
    public IDentalInsurance newDentalInsurance() {
        return (IDentalInsurance)pm.newInstance(FCAppDentalInsurance.class);
    }
    
    public IDepartment newDepartment() {
        return (IDepartment)pm.newInstance(FCAppDepartment.class);
    }
    
    public IFullTimeEmployee newFullTimeEmployee() {
        return (IFullTimeEmployee)pm.newInstance(FCAppFullTimeEmployee.class);
    }
    
    public IMedicalInsurance newMedicalInsurance() {
        return (IMedicalInsurance)pm.newInstance(FCAppMedicalInsurance.class);
    }
    
    public IPartTimeEmployee newPartTimeEmployee() {
        return (IPartTimeEmployee)pm.newInstance(FCAppPartTimeEmployee.class);
    }
    
    public IProject newProject() {
        return (IProject)pm.newInstance(FCAppProject.class);
    }
}
