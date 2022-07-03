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
 * CompanyFactoryAnnotatedPMFieldClass.java
 *
 * This class uses the PersistenceManager.newInstance method with the concrete
 * class as a parameter.
 */
public abstract class CompanyFactoryAnnotatedAbstractDelegator
        extends CompanyFactoryAbstractImpl {
    
    public CompanyFactoryNewInstance delegate = null;
    
    public final String identitytype =
            System.getProperty("jdo.tck.identitytype");
    public boolean isAppIdentity = "applicationidentity".equals(identitytype);

    /**
     * Creates a new instance of CompanyFactory
     * @param pm the PersistenceManager
     */
    public CompanyFactoryAnnotatedAbstractDelegator(PersistenceManager pm) {
        super(pm);
    }
    
    public IAddress newAddress() {
        return delegate.newAddress();
    }

    public IMeetingRoom newMeetingRoom() {
        return delegate.newMeetingRoom();
    }

    public ICompany newCompany() {
        return delegate.newCompany();
    }
    
    public IDentalInsurance newDentalInsurance() {
        return delegate.newDentalInsurance();
    }
    
    public IDepartment newDepartment() {
        return delegate.newDepartment();
    }
    
    public IFullTimeEmployee newFullTimeEmployee() {
        return delegate.newFullTimeEmployee();
    }
    
    public IMedicalInsurance newMedicalInsurance() {
        return delegate.newMedicalInsurance();
    }
    
    public IPartTimeEmployee newPartTimeEmployee() {
        return delegate.newPartTimeEmployee();
    }
    
    public IProject newProject() {
        return delegate.newProject();
    }
    
    public Class<?>[]getTearDownClasses() {
        return delegate.getTearDownClasses();
    }
}
