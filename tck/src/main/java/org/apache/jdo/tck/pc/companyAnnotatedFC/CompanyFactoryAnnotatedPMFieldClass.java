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

package org.apache.jdo.tck.pc.companyAnnotatedFC;

import javax.jdo.PersistenceManager;
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
public class CompanyFactoryAnnotatedPMFieldClass
    extends org.apache.jdo.tck.pc.company.CompanyFactoryAbstractImpl {

  Class addressClass = null;
  Class dentalInsuranceClass = null;
  Class medicalInsuranceClass = null;
  Class partTimeEmployeeClass = null;
  Class fullTimeEmployeeClass = null;
  Class projectClass = null;
  Class departmentClass = null;
  Class meetingRoomClass = null;
  Class companyClass = null;

  /**
   * Creates a new instance of CompanyFactoryAnnotatedPMFieldClass
   *
   * @param pm the PersistenceManager
   */
  public CompanyFactoryAnnotatedPMFieldClass(PersistenceManager pm) {
    super(pm);
    if (isAppIdentity) {
      addressClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCAppAddress.class;
      dentalInsuranceClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCAppDentalInsurance.class;
      medicalInsuranceClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCAppMedicalInsurance.class;
      partTimeEmployeeClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCAppPartTimeEmployee.class;
      fullTimeEmployeeClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCAppFullTimeEmployee.class;
      projectClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCAppProject.class;
      departmentClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCAppDepartment.class;
      meetingRoomClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCAppMeetingRoom.class;
      companyClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCAppCompany.class;
    } else { // datastoreidentity
      addressClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCDSAddress.class;
      dentalInsuranceClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCDSDentalInsurance.class;
      medicalInsuranceClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCDSMedicalInsurance.class;
      partTimeEmployeeClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCDSPartTimeEmployee.class;
      fullTimeEmployeeClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCDSFullTimeEmployee.class;
      projectClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCDSProject.class;
      departmentClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCDSDepartment.class;
      meetingRoomClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCDSMeetingRoom.class;
      companyClass = org.apache.jdo.tck.pc.companyAnnotatedFC.FCDSCompany.class;
    }
  }

  public Class[] getTearDownClasses() {
    return new Class[] {
      dentalInsuranceClass,
      medicalInsuranceClass,
      partTimeEmployeeClass,
      fullTimeEmployeeClass,
      projectClass,
      departmentClass,
      meetingRoomClass,
      companyClass
    };
  }

  public IAddress newAddress() {
    return (IAddress) pm.newInstance(addressClass);
  }

  public IMeetingRoom newMeetingRoom() {
    return (IMeetingRoom) pm.newInstance(meetingRoomClass);
  }

  public ICompany newCompany() {
    return (ICompany) pm.newInstance(companyClass);
  }

  public IDentalInsurance newDentalInsurance() {
    return (IDentalInsurance) pm.newInstance(dentalInsuranceClass);
  }

  public IDepartment newDepartment() {
    return (IDepartment) pm.newInstance(departmentClass);
  }

  public IFullTimeEmployee newFullTimeEmployee() {
    return (IFullTimeEmployee) pm.newInstance(fullTimeEmployeeClass);
  }

  public IMedicalInsurance newMedicalInsurance() {
    return (IMedicalInsurance) pm.newInstance(medicalInsuranceClass);
  }

  public IPartTimeEmployee newPartTimeEmployee() {
    return (IPartTimeEmployee) pm.newInstance(partTimeEmployeeClass);
  }

  public IProject newProject() {
    return (IProject) pm.newInstance(projectClass);
  }
}
