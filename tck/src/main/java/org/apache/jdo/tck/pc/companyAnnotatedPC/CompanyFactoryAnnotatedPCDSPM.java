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
 * CompanyFactoryAnnotatedPCDSPM.java
 *
 * This class uses the PersistenceManager.newInstance method with the concrete
 * class as a parameter.
 */
public class CompanyFactoryAnnotatedPCDSPM implements CompanyFactoryNewInstance {

  PersistenceManager pm = null;

  /**
   * Creates a new instance of CompanyFactoryAnnotatedPCDSPM
   *
   * @param pm the PersistenceManager
   */
  public CompanyFactoryAnnotatedPCDSPM(PersistenceManager pm) {
    this.pm = pm;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Class<?>[] getTearDownClasses() {
    return new Class[] {
      PCDSDentalInsurance.class,
      PCDSMedicalInsurance.class,
      PCDSPartTimeEmployee.class,
      PCDSFullTimeEmployee.class,
      PCDSProject.class,
      PCDSDepartment.class,
      PCDSCompany.class,
      PCDSAddress.class
    };
  }

  public IAddress newAddress() {
    return pm.newInstance(PCDSAddress.class);
  }

  public IMeetingRoom newMeetingRoom() {
    return null;
  }

  public ICompany newCompany() {
    return pm.newInstance(PCDSCompany.class);
  }

  public IDentalInsurance newDentalInsurance() {
    return pm.newInstance(PCDSDentalInsurance.class);
  }

  public IDepartment newDepartment() {
    return pm.newInstance(PCDSDepartment.class);
  }

  public IFullTimeEmployee newFullTimeEmployee() {
    return pm.newInstance(PCDSFullTimeEmployee.class);
  }

  public IMedicalInsurance newMedicalInsurance() {
    return pm.newInstance(PCDSMedicalInsurance.class);
  }

  public IPartTimeEmployee newPartTimeEmployee() {
    return pm.newInstance(PCDSPartTimeEmployee.class);
  }

  public IProject newProject() {
    return pm.newInstance(PCDSProject.class);
  }
}
