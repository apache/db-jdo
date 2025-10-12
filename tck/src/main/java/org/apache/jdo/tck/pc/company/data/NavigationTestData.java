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

import static org.apache.jdo.tck.pc.company.data.CompanyModelData.*;

import java.math.BigDecimal;
import org.apache.jdo.tck.pc.company.*;
import org.apache.jdo.tck.util.DefaultListableInstanceFactory;

public class NavigationTestData {

  public static void initNavigationTest(
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
    IMedicalInsurance medicalIns98 = factory.newMedicalInsurance(98, "Carrier98", "HMO");
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
    IDepartment dept1 = factory.newDepartment(1, "Development", company1);
    IDepartment dept2 = factory.newDepartment(2, "Human Resources", company1);
    company1.setDepartments(toSet(dept1, dept2));

    // Employee Constructors
    IFullTimeEmployee emp0 =
            factory.newFullTimeEmployee(
                    0, "emp0First", "emp0Last", "emp0Middle", null, addr1, null, 50000);
    emp0.setBirthdate(date(1962, 7, 10));
    emp0.setHiredate(date(1997, 1, 1));
    IFullTimeEmployee emp1 =
            factory.newFullTimeEmployee(
                    1, "emp1First", "emp1Last", "emp1Middle", null, addr1, null, 20000);
    emp1.setBirthdate(date(1970, 6, 10));
    emp1.setHiredate(date(1999, 1, 1));
    IFullTimeEmployee emp2 =
        factory.newFullTimeEmployee(
            2, "emp2First", "emp2Last", "emp2Middle", null, addr2, null, 10000);
    emp2.setBirthdate(date(1975, 12, 22));
    emp2.setHiredate(date(2003, 7, 1));
    IPartTimeEmployee emp3 =
        factory.newPartTimeEmployee(
            3, "emp3First", "emp3Last", "emp3Middle", null, addr3, null, 15);
    emp3.setBirthdate(date(1972, 7, 5));
    emp3.setHiredate(date(2002, 8, 15));
    IPartTimeEmployee emp4 =
        factory.newPartTimeEmployee(
            4, "emp4First", "emp4Last", "emp4Middle", null, addr3, null, 25000);
    emp4.setBirthdate(date(1973, 7, 6));
    emp4.setHiredate(date(2001, 4, 15));
    IFullTimeEmployee emp5 =
            factory.newFullTimeEmployee(
                    5, "emp5First", "emp5Last", "emp5Middle", null, addr3, null, 18000);
    emp5.setBirthdate(date(1962, 7, 5));
    emp5.setHiredate(date(2002, 11, 1));
    IFullTimeEmployee emp6 =
            factory.newFullTimeEmployee(
                    6, "emp6First", "emp6Last", "emp6Middle", null, addr3, null, 22000);
    emp6.setBirthdate(date(1969, 6, 10));
    emp6.setHiredate(date(2002, 6, 1));
    IFullTimeEmployee emp7 =
            factory.newFullTimeEmployee(
                    7, "emp7First", "emp7Last", "emp7Middle", null, addr1, null, 40000);
    emp7.setBirthdate(date(1970, 6, 10));
    emp7.setHiredate(date(2000, 1, 1));
    IFullTimeEmployee emp8 =
            factory.newFullTimeEmployee(
                    8, "emp8First", "emp8Last", "emp8Middle", null, addr2, null, 10000);
    emp8.setBirthdate(date(1975, 12, 22));
    emp8.setHiredate(date(2003, 8, 1));
    IFullTimeEmployee emp9 =
            factory.newFullTimeEmployee(
                    9, "emp9First", "emp9Last", "emp9Middle", null, addr3, null, 12000);
    emp9.setBirthdate(date(1972, 7, 5));
    emp9.setHiredate(date(2002, 5, 1));
    IFullTimeEmployee emp10 =
            factory.newFullTimeEmployee(
                    10, "emp10First", "emp10Last", "emp10Middle", null, addr3, null, 24000);
    emp10.setBirthdate(date(1972, 7, 5));
    emp10.setHiredate(date(2002, 10, 1));

    // Employee properties
    emp0.setWeeklyhours(40);
    emp0.setMedicalInsurance(null);
    emp0.setDentalInsurance(null);
    emp0.setPhoneNumbers(toMap("home", "3232", "work", "223311-1"));
    emp0.setDepartment(dept1);
    emp0.setFundingDept(dept2);
    emp0.setManager(null);
    emp0.setTeam(toSet(emp1));
    emp0.setMentor(null);
    emp0.setProtege(null);
    emp0.setHradvisor(emp5);
    emp0.setReviewedProjects(toSet(proj3));
    emp0.setProjects(toSet(proj1));

    emp1.setWeeklyhours(40);
    emp1.setMedicalInsurance(medicalIns1);
    emp1.setDentalInsurance(dentalIns1);
    emp1.setPhoneNumbers(toMap("home", "1111", "work", "123456-1"));
    emp1.setDepartment(dept1);
    emp1.setFundingDept(dept2);
    emp1.setManager(emp2);
    emp1.setTeam(toSet(emp2, emp3, emp10));
    emp1.setMentor(emp2);
    emp1.setProtege(emp5);
    emp1.setHradvisor(emp5);
    emp1.setReviewedProjects(toSet(proj3));
    emp1.setProjects(toSet(proj1));

    emp2.setWeeklyhours(40);
    emp2.setMedicalInsurance(medicalIns2);
    emp2.setDentalInsurance(dentalIns2);
    emp2.setPhoneNumbers(toMap("home", "2222", "work", "123456-2"));
    emp2.setDepartment(dept1);
    emp2.setFundingDept(dept1);
    emp2.setMentor(emp3);
    emp2.setProtege(emp1);
    emp2.setHradvisor(emp5);
    emp2.setProjects(toSet(proj1, proj2));

    emp3.setWeeklyhours(19);
    emp3.setMedicalInsurance(medicalIns3);
    emp3.setDentalInsurance(dentalIns3);
    emp3.setPhoneNumbers(toMap("home", "3333", "work", "123456-3"));
    emp3.setDepartment(dept1);
    emp3.setFundingDept(dept1);
    emp3.setManager(emp1);
    emp3.setTeam(toSet());
    emp3.setMentor(emp4);
    emp3.setProtege(emp2);
    emp3.setHradvisor(emp5);
    emp3.setProjects(toSet(proj1, proj2));

    emp4.setWeeklyhours(40);
    emp4.setMedicalInsurance(medicalIns4);
    emp4.setDentalInsurance(dentalIns4);
    emp4.setPhoneNumbers(toMap("home", "3343", "work", "124456-3"));
    emp4.setDepartment(dept1);
    emp4.setFundingDept(dept2);
    emp4.setManager(null);
    emp4.setTeam(toSet(emp5, emp6));
    emp4.setMentor(emp5);
    emp4.setProtege(emp3);
    emp4.setHradvisor(emp5);
    emp4.setProjects(toSet(proj3));
    emp4.setReviewedProjects(toSet(proj2));

    emp5.setWeeklyhours(35);
    emp5.setMedicalInsurance(medicalIns5);
    emp5.setDentalInsurance(dentalIns5);
    emp5.setPhoneNumbers(toMap("home", "3363", "work", "126456-3"));
    emp5.setDepartment(dept1);
    emp5.setFundingDept(dept2);
    emp5.setManager(emp4);
    emp5.setTeam(toSet());
    emp5.setMentor(emp1);
    emp5.setProtege(emp4);
    emp5.setProjects(toSet(proj3));
    emp5.setReviewedProjects(toSet(proj2));
    emp5.setHradvisees(toSet(emp1, emp2, emp3, emp4));

    emp6.setWeeklyhours(60);
    emp6.setDepartment(dept1);
    emp6.setManager(emp4);
    emp6.setTeam(toSet());

    emp7.setWeeklyhours(40);
    emp7.setDepartment(dept2);
    emp7.setManager(null);
    emp7.setTeam(toSet(emp8, emp9));

    emp8.setWeeklyhours(15);
    emp8.setDepartment(dept2);
    emp8.setManager(emp7);
    emp8.setTeam(toSet());

    emp9.setWeeklyhours(20);
    emp9.setDepartment(dept2);
    emp9.setManager(emp7);
    emp9.setTeam(toSet());

    emp10.setWeeklyhours(40);
    emp10.setDepartment(dept2);
    emp10.setManager(emp1);
    emp10.setTeam(toSet());

    // Department properties
    dept1.setEmployees(toSet(emp1, emp2, emp3, emp4, emp5, emp6));

    dept2.setEmployees(toSet(emp7, emp8, emp9, emp10));

    // Insurance properties
    medicalIns1.setEmployee(emp1);
    medicalIns2.setEmployee(emp2);
    medicalIns3.setEmployee(emp3);
    medicalIns4.setEmployee(emp4);
    medicalIns5.setEmployee(emp5);
    medicalIns98.setEmployee(null);
    dentalIns1.setEmployee(emp1);
    dentalIns2.setEmployee(emp2);
    dentalIns3.setEmployee(emp3);
    dentalIns4.setEmployee(emp4);
    dentalIns5.setEmployee(emp5);
    dentalIns99.setEmployee(null);

    // Project properties
    proj1.setMembers(toSet(emp1, emp2, emp3));
    proj2.setReviewers(toSet(emp4, emp5));
    proj2.setMembers(toSet(emp2, emp3));
    proj3.setReviewers(toSet(emp1));
    proj3.setMembers(toSet(emp4, emp5));

    // root objects
    registry.register("company1", company1);
    registry.register("medicalIns98", medicalIns98);
    registry.register("dentalIns99", dentalIns99);

    registry.register("dept1", dept1);
    registry.register("dept2", dept2);
    registry.register("emp0", emp0);
    registry.register("emp1", emp1);
    registry.register("emp2", emp2);
    registry.register("emp3", emp3);
    registry.register("emp4", emp4);
    registry.register("emp5", emp5);
    registry.register("emp6", emp6);
    registry.register("emp7", emp7);
    registry.register("emp8", emp8);
    registry.register("emp9", emp9);
    registry.register("emp10", emp10);
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
