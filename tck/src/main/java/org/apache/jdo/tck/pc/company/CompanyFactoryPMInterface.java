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
 * CompanyFactoryPMInterface.java
 *
 * Created on August 29, 2005, 9:56 PM
 *
 */
public class CompanyFactoryPMInterface 
        extends CompanyFactoryAbstractImpl {
    
    /** */
    public static final Class<?>[] tearDownClasses = new Class<?>[] {
        PIDentalInsurance.class, PIMedicalInsurance.class,
        PIPartTimeEmployee.class, PIFullTimeEmployee.class,  
        PIProject.class, PIDepartment.class, PICompany.class
    };

    /**
     * Creates a new instance of CompanyFactory
     * @param pm the PersistenceManager
     */
    public CompanyFactoryPMInterface(PersistenceManager pm) {
        super(pm);
    }
    
    public IAddress newAddress() {
        return pm.newInstance(PIAddress.class);
    }
    
    public IMeetingRoom newMeetingRoom() {
        return pm.newInstance(PIMeetingRoom.class);
    }
    
    public ICompany newCompany() {
        return pm.newInstance(PICompany.class);
    }
    
    public IDentalInsurance newDentalInsurance() {
        return pm.newInstance(PIDentalInsurance.class);
    }
    
    public IDepartment newDepartment() {
        return pm.newInstance(PIDepartment.class);
    }
    
    public IFullTimeEmployee  newFullTimeEmployee() {
        return pm.newInstance(PIFullTimeEmployee.class);
    }
    
    public IMedicalInsurance newMedicalInsurance() {
        return pm.newInstance(PIMedicalInsurance.class);        
    }
    
    public IPartTimeEmployee  newPartTimeEmployee() {
        return pm.newInstance(PIPartTimeEmployee.class);
    }
    
    public IProject newProject() {
        return pm.newInstance(PIProject.class);
    }

    public Class<?>[] getTearDownClasses() {
        return tearDownClasses;
    }
}
