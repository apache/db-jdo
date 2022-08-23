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

package org.apache.jdo.tck.pc.companyAnnotatedPC;

import javax.jdo.PersistenceManager;
import org.apache.jdo.tck.pc.company.CompanyFactoryNewInstance;
import org.apache.jdo.tck.pc.company.IAddress;
import org.apache.jdo.tck.pc.company.ICompany;
import org.apache.jdo.tck.pc.company.IDentalInsurance;
import org.apache.jdo.tck.pc.company.IDepartment;
import org.apache.jdo.tck.pc.company.IFullTimeEmployee;
import org.apache.jdo.tck.pc.company.IMedicalInsurance;
import org.apache.jdo.tck.pc.company.IMeetingRoom;
import org.apache.jdo.tck.pc.company.IPartTimeEmployee;
import org.apache.jdo.tck.pc.company.IProject;

/*
 * CompanyFactoryAnnotatedPMFieldClass.java
 *
 * This class uses the PersistenceManager.newInstance method with the concrete
 * class as a parameter.
 */
public class CompanyFactoryAnnotatedPCAppPM implements CompanyFactoryNewInstance {

  PersistenceManager pm = null;

  /**
   * Creates a new instance of CompanyFactoryAnnotatedPCAppPM
   *
   * @param pm the PersistenceManager
   */
  public CompanyFactoryAnnotatedPCAppPM(PersistenceManager pm) {
    this.pm = pm;
  }

  public Class[] getTearDownClasses() {
    return new Class[] {
      PCAppDentalInsurance.class,
      PCAppMedicalInsurance.class,
      PCAppPartTimeEmployee.class,
      PCAppFullTimeEmployee.class,
      PCAppProject.class,
      PCAppDepartment.class,
      PCAppCompany.class,
      PCAppAddress.class
    };
  }

  public IAddress newAddress() {
    return (IAddress) pm.newInstance(PCAppAddress.class);
  }

  public IMeetingRoom newMeetingRoom() {
    return (IMeetingRoom) null;
  }

  public ICompany newCompany() {
    return (ICompany) pm.newInstance(PCAppCompany.class);
  }

  public IDentalInsurance newDentalInsurance() {
    return (IDentalInsurance) pm.newInstance(PCAppDentalInsurance.class);
  }

  public IDepartment newDepartment() {
    return (IDepartment) pm.newInstance(PCAppDepartment.class);
  }

  public IFullTimeEmployee newFullTimeEmployee() {
    return (IFullTimeEmployee) pm.newInstance(PCAppFullTimeEmployee.class);
  }

  public IMedicalInsurance newMedicalInsurance() {
    return (IMedicalInsurance) pm.newInstance(PCAppMedicalInsurance.class);
  }

  public IPartTimeEmployee newPartTimeEmployee() {
    return (IPartTimeEmployee) pm.newInstance(PCAppPartTimeEmployee.class);
  }

  public IProject newProject() {
    return (IProject) pm.newInstance(PCAppProject.class);
  }
}
