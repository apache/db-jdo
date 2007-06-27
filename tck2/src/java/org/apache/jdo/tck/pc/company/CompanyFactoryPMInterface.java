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
    public static final Class[] tearDownClasses = new Class[] {
        IDentalInsurance.class, IMedicalInsurance.class,
        IPartTimeEmployee.class, IFullTimeEmployee.class,  
        IProject.class, IDepartment.class, ICompany.class
    };

    /**
     * Creates a new instance of CompanyFactoryPMInterface 
     */
    public CompanyFactoryPMInterface(PersistenceManager pm) {
        super(pm);
    }
    
    IAddress newAddress() {
        return (IAddress)pm.newInstance(IAddress.class);
    }
    
    ICompany newCompany() {
        return (ICompany)pm.newInstance(ICompany.class);
    }
    
    IDentalInsurance newDentalInsurance() {
        return (IDentalInsurance)pm.newInstance(IDentalInsurance.class);
    }
    
    IDepartment newDepartment() {
        return (IDepartment)pm.newInstance(IDepartment.class);
    }
    
    IFullTimeEmployee  newFullTimeEmployee() {
        return (IFullTimeEmployee)pm.newInstance(IFullTimeEmployee.class);
    }
    
    IMedicalInsurance newMedicalInsurance() {
        return (IMedicalInsurance)pm.newInstance(IMedicalInsurance.class);        
    }
    
    IPartTimeEmployee  newPartTimeEmployee() {
        return (IPartTimeEmployee)pm.newInstance(IPartTimeEmployee.class);
    }
    
    IProject newProject() {
        return (IProject)pm.newInstance(IProject.class);
    }

    public Class[] getTearDownClasses() {
        return tearDownClasses;
    }
}
