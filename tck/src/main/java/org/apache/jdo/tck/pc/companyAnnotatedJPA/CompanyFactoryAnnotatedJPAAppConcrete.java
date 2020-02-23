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

package org.apache.jdo.tck.pc.companyAnnotatedJPA;

import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.*;

/*
 * CompanyFactoryAnnotatedPMFieldClass.java
 *
 * This class uses the PersistenceManager.newInstance method with the concrete
 * class as a parameter.
 */
public class CompanyFactoryAnnotatedJPAAppConcrete
        extends CompanyFactoryAbstractImpl {
    
    PersistenceManager pm = null;

    /**
     * Creates a new instance of CompanyFactoryAnnotatedJPAAppConcrete
     * @param pm the PersistenceManager
     */
    public CompanyFactoryAnnotatedJPAAppConcrete(PersistenceManager pm) {
        super(pm);
        this.pm = pm;        
    }
    
    public Class[] getTearDownClasses() {
        return new Class[] {
            JPAAppDentalInsurance.class,
            JPAAppMedicalInsurance.class,
            JPAAppPartTimeEmployee.class,
            JPAAppFullTimeEmployee.class,
            JPAAppProject.class,
            JPAAppDepartment.class,
            JPAAppCompany.class,
            JPAAppAddress.class
        };
    }
    
    public IAddress newAddress() {
        return (IAddress) new JPAAppAddress();
    }

    public IMeetingRoom newMeetingRoom() {
        return (IMeetingRoom)null;
    }
    
    public ICompany newCompany() {
        return (ICompany) new JPAAppCompany();
    }
    
    public IDentalInsurance newDentalInsurance() {
        return (IDentalInsurance) new JPAAppDentalInsurance();
    }
    
    public IDepartment newDepartment() {
        return (IDepartment) new JPAAppDepartment();
    }
    
    public IFullTimeEmployee newFullTimeEmployee() {
        return (IFullTimeEmployee) new JPAAppFullTimeEmployee();
    }
    
    public IMedicalInsurance newMedicalInsurance() {
        return (IMedicalInsurance) new JPAAppMedicalInsurance();
    }
    
    public IPartTimeEmployee newPartTimeEmployee() {
        return (IPartTimeEmployee) new JPAAppPartTimeEmployee();
    }
    
    public IProject newProject() {
        return (IProject) new JPAAppProject();
    }
}
