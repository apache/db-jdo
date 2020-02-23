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

package org.apache.jdo.tck.pc.companyListWithoutJoin;

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
        IPartTimeEmployee.class, IFullTimeEmployee.class,  
        IDepartment.class, ICompany.class
    };

    /**
     * Creates a new instance of CompanyFactoryPMInterface
     * @param pm the PersistenceManager
     */
    public CompanyFactoryPMInterface(PersistenceManager pm) {
        super(pm);
    }
    
    ICompany newCompany() {
        return (ICompany)pm.newInstance(ICompany.class);
    }
    
    IDepartment newDepartment() {
        return (IDepartment)pm.newInstance(IDepartment.class);
    }
    
    IFullTimeEmployee  newFullTimeEmployee() {
        return (IFullTimeEmployee)pm.newInstance(IFullTimeEmployee.class);
    }
    
    IPartTimeEmployee  newPartTimeEmployee() {
        return (IPartTimeEmployee)pm.newInstance(IPartTimeEmployee.class);
    }
    
    public Class[] getTearDownClasses() {
        return tearDownClasses;
    }
}
