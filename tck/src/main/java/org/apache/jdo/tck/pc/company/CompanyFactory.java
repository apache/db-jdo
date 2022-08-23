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

/*
 * CompanyFactory.java
 *
 */

package org.apache.jdo.tck.pc.company;

import java.math.BigDecimal;

import java.util.Date;

/**
 * This interface is implemented by a factory class that can create Company model instances. The
 * factory instance is registered with CompanyFactoryRegistry.
 */
public interface CompanyFactory {
  ICompany newCompany(long companyid, String name, Date founded);

  ICompany newCompany(long companyid, String name, Date founded, IAddress addr);

  IAddress newAddress(
      long addrid, String street, String city, String state, String zipcode, String country);

  IMeetingRoom newMeetingRoom(long roomid, String name);

  IDentalInsurance newDentalInsurance(long insid, String carrier, BigDecimal lifetimeOrthoBenefit);

  IDentalInsurance newDentalInsurance(
      long insid, String carrier, IEmployee employee, BigDecimal lifetimeOrthoBenefit);

  IDepartment newDepartment(long deptid, String name);

  IDepartment newDepartment(long deptid, String name, ICompany company);

  IDepartment newDepartment(
      long deptid, String name, ICompany company, IEmployee employeeOfTheMonth);

  IFullTimeEmployee newFullTimeEmployee(
      long personid,
      String first,
      String last,
      String middle,
      Date born,
      IAddress addr,
      Date hired,
      double sal);

  IFullTimeEmployee newFullTimeEmployee(
      long personid, String first, String last, String middle, Date born, Date hired, double sal);

  IMedicalInsurance newMedicalInsurance(long insid, String carrier, String planType);

  IMedicalInsurance newMedicalInsurance(
      long insid, String carrier, IEmployee employee, String planType);

  IPartTimeEmployee newPartTimeEmployee(
      long personid, String first, String last, String middle, Date born, Date hired, double wage);

  IPartTimeEmployee newPartTimeEmployee(
      long personid,
      String first,
      String last,
      String middle,
      Date born,
      IAddress addr,
      Date hired,
      double wage);

  IProject newProject(long projid, String name, BigDecimal budget);

  Class[] getTearDownClasses();
}
