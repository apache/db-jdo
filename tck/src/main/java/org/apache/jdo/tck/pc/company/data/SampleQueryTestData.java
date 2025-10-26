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

package org.apache.jdo.tck.pc.company.data;

import java.math.BigDecimal;

import org.apache.jdo.tck.pc.company.*;
import org.apache.jdo.tck.util.DefaultListableInstanceFactory;

import static org.apache.jdo.tck.pc.company.data.CompanyModelData.*;

public class SampleQueryTestData implements CompanyDataSource {

  @Override
  public void initMe(CompanyFactory factory, DefaultListableInstanceFactory registry) {
    init(factory, registry);
  }

  public static void init(
          CompanyFactory factory, DefaultListableInstanceFactory registry) {
    IAddress addr1 =
            factory.newAddress(1, "Unter den Linden 1", "Berlin", "  ", "12345", "Germany");
    IAddress addr2 = factory.newAddress(2, "Broadway 1", "New York", "NY", "10000", "USA");
    IAddress addr3 = factory.newAddress(3, "Market St.", "San Francisco", "CA", "94102", "USA");

    // Insurance constructors
    IMedicalInsurance medicalIns1 = factory.newMedicalInsurance(1, "Carrier1", "PPO");
    IMedicalInsurance medicalIns2 = factory.newMedicalInsurance(2, "Carrier2", "HMO");
    IMedicalInsurance medicalIns3 = factory.newMedicalInsurance(3, "Carrier3", "HMO");
    IMedicalInsurance medicalIns4 = factory.newMedicalInsurance(4, "Carrier4", "HMO");
    IMedicalInsurance medicalIns5 = factory.newMedicalInsurance(5, "Carrier5", "HMO");
    IDentalInsurance dentalIns1 =
            factory.newDentalInsurance(11, "Carrier1", BigDecimal.valueOf(99.995));
    IDentalInsurance dentalIns2 =
            factory.newDentalInsurance(12, "Carrier2", BigDecimal.valueOf(99.996));
    IDentalInsurance dentalIns3 =
            factory.newDentalInsurance(13, "Carrier3", BigDecimal.valueOf(99.997));
    IDentalInsurance dentalIns4 =
            factory.newDentalInsurance(14, "Carrier4", BigDecimal.valueOf(99.998));
    IDentalInsurance dentalIns5 =
            factory.newDentalInsurance(15, "Carrier5", BigDecimal.valueOf(99.999));
    IDentalInsurance dentalIns99 = factory.newDentalInsurance(99, "Carrier99", null);

    // Project constructors
    IProject proj1 = factory.newProject(1, "orange", BigDecimal.valueOf(2500000.99));
    IProject proj2 = factory.newProject(2, "blue", BigDecimal.valueOf(50000.00));
    IProject proj3 = factory.newProject(3, "green", BigDecimal.valueOf(2000.99));

    // Company constructor
    ICompany company1 = factory.newCompany(1L, "Sun Microsystems, Inc.", date(1952, 4, 11), addr1);

    // Meeting room constructors
    IMeetingRoom room1 = factory.newMeetingRoom(1, "Comfy Room");
    IMeetingRoom room2 = factory.newMeetingRoom(2, "Large Discussion Room");
    IMeetingRoom room3 = factory.newMeetingRoom(3, "Conference Room");

    // Department Constructors
    IDepartment dept1 = factory.newDepartment(1, "R&D", company1);
    IDepartment dept2 = factory.newDepartment(2, "Sales", company1);
    IDepartment dept3 = factory.newDepartment(3, "Marketing", company1);
    company1.setDepartments(toSet(dept1, dept2));

    // Employee Constructors
    IFullTimeEmployee emp1 =
            factory.newFullTimeEmployee(
                    1, "Michael", "Bouschen", "", null, addr1, null, 40000);
    emp1.setBirthdate(date(1970, 6, 10));
    emp1.setHiredate(date(1999, 1, 1));
    IFullTimeEmployee emp2 =
            factory.newFullTimeEmployee(
                    2, "Craig", "Russell", "L.", null, addr2, null, 50000);
    emp2.setBirthdate(date(1975, 12, 22));
    emp2.setHiredate(date(2003, 7, 1));
    IPartTimeEmployee emp3 =
            factory.newPartTimeEmployee(
                    3, "Joe", "Doe", "", null, addr3, null, 15);
    emp3.setBirthdate(date(1972, 7, 5));
    emp3.setHiredate(date(2002, 8, 15));
    IPartTimeEmployee emp4 =
            factory.newPartTimeEmployee(
                    4, "Jane", "Roe", "", null, addr3, null, 13);
    emp4.setBirthdate(date(1973, 7, 6));
    emp4.setHiredate(date(2001, 4, 15));
    IFullTimeEmployee emp5 =
            factory.newFullTimeEmployee(
                    5, "Tilmann", "Zäschke", "", null, addr3, null, 45000);
    emp5.setBirthdate(date(1979, 7, 5));
    emp5.setHiredate(date(1999, 8, 15));

    // Employee properties
    emp1.setWeeklyhours(40);
    emp1.setMedicalInsurance(medicalIns1);
    emp1.setDentalInsurance(dentalIns1);
    emp1.setPhoneNumbers(toMap("home", "1111", "work", "123456-1"));
    emp1.setLanguages(toSet("German", "English"));
    emp1.setDepartment(dept1);
    emp1.setFundingDept(dept2);
    emp1.setManager(emp2);
    emp1.setMentor(emp2);
    emp1.setProtege(emp5);
    emp1.setHradvisor(emp5);
    emp1.setReviewedProjects(toSet(proj3));
    emp1.setProjects(toSet(proj1));

    emp2.setWeeklyhours(40);
    emp2.setMedicalInsurance(medicalIns2);
    emp2.setDentalInsurance(dentalIns2);
    emp2.setPhoneNumbers(toMap("home", "2222", "work", "123456-2"));
    emp2.setLanguages(toSet("English", "Japanese"));
    emp2.setDepartment(dept1);
    emp2.setFundingDept(dept1);
    emp2.setMentor(emp3);
    emp2.setProtege(emp1);
    emp2.setHradvisor(emp5);
    emp2.setProjects(toSet(proj1, proj2));
    emp2.setTeam(toSet(emp1, emp3, emp4, emp5));

    emp3.setWeeklyhours(19);
    emp3.setMedicalInsurance(medicalIns3);
    emp3.setDentalInsurance(dentalIns3);
    emp3.setPhoneNumbers(toMap("home", "3333", "work", "123456-3"));
    emp3.setLanguages(toSet("English", "French"));
    emp3.setDepartment(dept1);
    emp3.setFundingDept(dept1);
    emp3.setManager(emp2);
    emp3.setMentor(emp4);
    emp3.setProtege(emp2);
    emp3.setHradvisor(emp5);
    emp3.setProjects(toSet(proj1, proj2));

    emp4.setMedicalInsurance(medicalIns4);
    emp4.setDentalInsurance(dentalIns4);
    emp4.setPhoneNumbers(toMap("home", "3343", "work", "124456-3"));
    emp4.setLanguages(toSet("English"));
    emp4.setDepartment(dept2);
    emp4.setFundingDept(dept2);
    emp4.setManager(emp2);
    emp4.setMentor(emp5);
    emp4.setProtege(emp3);
    emp4.setHradvisor(emp5);
    emp4.setProjects(toSet(proj3));
    emp4.setReviewedProjects(toSet(proj2));

    emp5.setMedicalInsurance(medicalIns5);
    emp5.setDentalInsurance(dentalIns5);
    emp5.setPhoneNumbers(toMap("home", "3363", "work", "126456-3"));
    emp5.setLanguages(toSet("German", "English", "French", "Japanese"));
    emp5.setDepartment(dept2);
    emp5.setFundingDept(dept2);
    emp5.setManager(emp2);
    emp5.setMentor(emp1);
    emp5.setProtege(emp4);
    emp5.setProjects(toSet(proj3));
    emp5.setReviewedProjects(toSet(proj2));
    emp5.setHradvisees(toSet(emp1, emp2, emp3, emp4));

    // Department properties
    dept1.setEmployees(toSet(emp1, emp2, emp3));
    dept1.setFundedEmps(toSet(emp2, emp3));
    dept1.setMeetingRooms(toList(room1, room2, room3));

    dept2.setEmployees(toSet(emp4, emp5));
    dept2.setFundedEmps(toSet(emp1, emp4, emp5));
    dept2.setMeetingRooms(toList());

    dept3.setEmployees(toSet());
    dept3.setFundedEmps(toSet());
    dept3.setMeetingRooms(toList());

    // Insurance properties
    medicalIns1.setEmployee(emp1);
    medicalIns2.setEmployee(emp2);
    medicalIns3.setEmployee(emp3);
    medicalIns4.setEmployee(emp4);
    medicalIns5.setEmployee(emp5);
    dentalIns1.setEmployee(emp1);
    dentalIns2.setEmployee(emp2);
    dentalIns3.setEmployee(emp3);
    dentalIns4.setEmployee(emp4);
    dentalIns5.setEmployee(emp5);

    // Project properties
    proj1.setMembers(toSet(emp1, emp2, emp3));
    proj2.setReviewers(toSet(emp4, emp5));
    proj2.setMembers(toSet(emp2, emp3));
    proj3.setReviewers(toSet(emp1));
    proj3.setMembers(toSet(emp4, emp5));

    // root objects
    registry.register("company1", company1);
    registry.register("dentalIns99", dentalIns99);

    registry.register("dept1", dept1);
    registry.register("dept2", dept2);
    registry.register("dept3", dept3);
    registry.register("emp1", emp1);
    registry.register("emp2", emp2);
    registry.register("emp3", emp3);
    registry.register("emp4", emp4);
    registry.register("emp5", emp5);
    registry.register("proj1", proj1);
    registry.register("proj2", proj2);
    registry.register("proj3", proj3);
    registry.register("dentalIns1", dentalIns1);
    registry.register("dentalIns2", dentalIns2);
    registry.register("dentalIns3", dentalIns3);
    registry.register("dentalIns4", dentalIns4);
    registry.register("dentalIns5", dentalIns5);
    registry.register("medicalIns1", medicalIns1);
    registry.register("medicalIns2", medicalIns2);
    registry.register("medicalIns3", medicalIns3);
    registry.register("medicalIns4", medicalIns4);
    registry.register("medicalIns5", medicalIns5);
    registry.register("room1", room1);
    registry.register("room2", room2);
    registry.register("room3", room3);
  }
}
