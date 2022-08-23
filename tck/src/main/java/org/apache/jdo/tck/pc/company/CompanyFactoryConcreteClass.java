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

import java.math.BigDecimal;

import java.util.Date;

import javax.jdo.PersistenceManager;

/*
 * This class is the company factory that uses constructors of the
 * concrete classes.
 */
public class CompanyFactoryConcreteClass implements CompanyFactory {

  /** */
  public static final Class[] tearDownClasses =
      new Class[] {
        DentalInsurance.class,
        MedicalInsurance.class,
        PartTimeEmployee.class,
        FullTimeEmployee.class,
        Project.class,
        Department.class,
        Company.class,
        MeetingRoom.class
      };

  /**
   * Creates a new instance of CompanyFactory
   *
   * @param pm the PersistenceManager
   */
  public CompanyFactoryConcreteClass(PersistenceManager pm) {}

  public CompanyFactoryConcreteClass() {}

  public ICompany newCompany(long companyid, String name, Date founded) {
    return new Company(companyid, name, founded);
  }

  public ICompany newCompany(long companyid, String name, Date founded, IAddress addr) {
    return new Company(companyid, name, founded, addr);
  }

  public IAddress newAddress(
      long addrid, String street, String city, String state, String zipcode, String country) {
    return new Address(addrid, street, city, state, zipcode, country);
  }

  public IMeetingRoom newMeetingRoom(long roomid, String name) {
    return new MeetingRoom(roomid, name);
  }

  public IDentalInsurance newDentalInsurance(
      long insid, String carrier, BigDecimal lifetimeOrthoBenefit) {
    return new DentalInsurance(insid, carrier, lifetimeOrthoBenefit);
  }

  public IDentalInsurance newDentalInsurance(
      long insid, String carrier, IEmployee employee, BigDecimal lifetimeOrthoBenefit) {
    return new DentalInsurance(insid, carrier, lifetimeOrthoBenefit);
  }

  public IDepartment newDepartment(long deptid, String name) {
    return new Department(deptid, name);
  }

  public IDepartment newDepartment(long deptid, String name, ICompany company) {
    return new Department(deptid, name, company);
  }

  public IDepartment newDepartment(
      long deptid, String name, ICompany company, IEmployee employeeOfTheMonth) {
    return new Department(deptid, name, company, employeeOfTheMonth);
  }

  public IFullTimeEmployee newFullTimeEmployee(
      long personid, String first, String last, String middle, Date born, Date hired, double sal) {
    return new FullTimeEmployee(personid, first, last, middle, born, hired, sal);
  }

  public IFullTimeEmployee newFullTimeEmployee(
      long personid,
      String first,
      String last,
      String middle,
      Date born,
      IAddress addr,
      Date hired,
      double sal) {
    return new FullTimeEmployee(personid, first, last, middle, born, addr, hired, sal);
  }

  public IMedicalInsurance newMedicalInsurance(long insid, String carrier, String planType) {
    return new MedicalInsurance(insid, carrier, planType);
  }

  public IMedicalInsurance newMedicalInsurance(
      long insid, String carrier, IEmployee employee, String planType) {
    return new MedicalInsurance(insid, carrier, planType);
  }

  public IPartTimeEmployee newPartTimeEmployee(
      long personid, String first, String last, String middle, Date born, Date hired, double wage) {
    return new PartTimeEmployee(personid, first, last, middle, born, hired, wage);
  }

  public IPartTimeEmployee newPartTimeEmployee(
      long personid,
      String first,
      String last,
      String middle,
      Date born,
      IAddress addr,
      Date hired,
      double wage) {
    return new PartTimeEmployee(personid, first, last, middle, born, addr, hired, wage);
  }

  public IPerson newPerson(
      long personid, String firstname, String lastname, String middlename, Date birthdate) {
    return new Person(personid, firstname, lastname, middlename, birthdate);
  }

  public IPerson newPerson(
      long personid,
      String firstname,
      String lastname,
      String middlename,
      Date birthdate,
      IAddress address) {
    return new Person(personid, firstname, lastname, middlename, birthdate, address);
  }

  public IProject newProject(long projid, String name, BigDecimal budget) {
    return new Project(projid, name, budget);
  }

  public Class[] getTearDownClasses() {
    return tearDownClasses;
  }
}
