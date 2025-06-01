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

package org.apache.jdo.tck.pc.company;

import org.apache.jdo.tck.util.DefaultListableInstanceFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class CompanyModelData {

    public static void initQueryTest(CompanyFactory companyFactory, DefaultListableInstanceFactory registry) {
        IAddress addr1 = companyFactory.newAddress(1,
                "Unter den Linden 1",
                "Berlin",
                "  ",
                "12345",
                "Germany");
        IAddress addr2 = companyFactory.newAddress(2,
                "Broadway 1",
                "New York",
                "NY",
                "10000",
                "USA");
        IAddress addr3 = companyFactory.newAddress(3,
                "Market St.",
                "San Francisco",
                "CA",
                "94102",
                "USA");

        // Insurance constructors
        IMedicalInsurance medicalIns1 = companyFactory.newMedicalInsurance(1, "Carrier1", "PPO");
        IMedicalInsurance medicalIns2 = companyFactory.newMedicalInsurance(2, "Carrier2", "HMO");
        IMedicalInsurance medicalIns3 = companyFactory.newMedicalInsurance(3, "Carrier3", "HMO");
        IMedicalInsurance medicalIns4 = companyFactory.newMedicalInsurance(4, "Carrier4", "HMO");
        IMedicalInsurance medicalIns5 = companyFactory.newMedicalInsurance(5, "Carrier5", "HMO");
        IDentalInsurance dentalIns1 = companyFactory.newDentalInsurance(11,
                "Carrier1", BigDecimal.valueOf(99.995));
        IDentalInsurance dentalIns2 = companyFactory.newDentalInsurance(12,
                "Carrier2", BigDecimal.valueOf(99.996));
        IDentalInsurance dentalIns3 = companyFactory.newDentalInsurance(13,
                "Carrier3", BigDecimal.valueOf(99.997));
        IDentalInsurance dentalIns4 = companyFactory.newDentalInsurance(14,
                "Carrier4", BigDecimal.valueOf(99.998));
        IDentalInsurance dentalIns5 = companyFactory.newDentalInsurance(15,
                "Carrier5", BigDecimal.valueOf(99.999));
        IDentalInsurance dentalIns99 = companyFactory.newDentalInsurance(99, "Carrier99", null);

        // Project constructors
        IProject proj1 = companyFactory.newProject(1, "orange", BigDecimal.valueOf(2500000.99));
        IProject proj2 = companyFactory.newProject(2, "blue", BigDecimal.valueOf(50000.00));
        IProject proj3 = companyFactory.newProject(3, "green", BigDecimal.valueOf(2000.99));

        // Company constructor
        ICompany company1 =
                companyFactory.newCompany(1L, "Sun Microsystems, Inc.", new Date(1952, 4, 11, 0, 0, 0), addr1);

        // Meeting room constructors
        IMeetingRoom room1 = companyFactory.newMeetingRoom(1, "Comfy Room");
        IMeetingRoom room2 = companyFactory.newMeetingRoom(2, "Large Discussion Room");
        IMeetingRoom room3 = companyFactory.newMeetingRoom(3, "Conference Room");

        // Department Constructors
        IDepartment dept1 = companyFactory.newDepartment(1, "Development", company1);
        IDepartment dept2 = companyFactory.newDepartment(2, "Human Resources", company1);
        company1.setDepartments(toSet(dept1, dept2));

        // Employee Constructors
        IFullTimeEmployee emp1 =
                companyFactory.newFullTimeEmployee(
                        1,
                        "emp1First",
                        "emp1Last",
                        "emp1Middle",
                        new Date(1970, 6, 10, 0, 0, 0),
                        addr1,
                        new Date(1999, 1, 1, 0, 0, 0),
                        20000);

        IFullTimeEmployee emp2 =
                companyFactory.newFullTimeEmployee(
                        2,
                        "emp2First",
                        "emp2Last",
                        "emp2Middle",
                        new Date(1975, 12, 22, 0, 0, 0),
                        addr2,
                        new Date(2003, 7, 1, 0, 0, 0),
                        10000);

        IPartTimeEmployee emp3 =
                companyFactory.newPartTimeEmployee(
                        3,
                        "emp3First",
                        "emp3Last",
                        "emp3Middle",
                        new Date(1972, 7, 5, 0, 0, 0),
                        addr3,
                        new Date(2002, 8, 15, 0, 0, 0),
                        15);

        IPartTimeEmployee emp4 =
                companyFactory.newPartTimeEmployee(
                        4,
                        "emp4First",
                        "emp4Last",
                        "emp4Middle",
                        new Date(1973, 7, 6, 0, 0, 0),
                        addr3,
                        new Date(2001, 4, 15, 0, 0, 0),
                        13);

        IFullTimeEmployee emp5 =
                companyFactory.newFullTimeEmployee(
                        5,
                        "emp5First",
                        "emp5Last",
                        "emp5Middle",
                        new Date(1962, 7, 5, 0, 0, 0),
                        addr3,
                        new Date(1998, 8, 15, 0, 0, 0),
                        45000);

        // Employee properties
        emp1.setWeeklyhours(40);
        emp1.setMedicalInsurance(medicalIns1);
        emp1.setDentalInsurance(dentalIns1);
        emp1.setPhoneNumbers(toMap("home", "1111", "work", "123456-1"));
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
        emp5.setDepartment(dept2);
        emp5.setFundingDept(dept2);
        emp5.setManager(emp2);
        emp5.setMentor(emp1);
        emp5.setProtege(emp4);
        emp5.setProjects(toSet(proj3));
        emp5.setReviewedProjects(toSet(proj2));
        emp5.setHradvisees(toSet(emp1, emp2, emp3, emp4));

        // Finish departments
        dept1.setEmployees(toSet(emp1, emp2, emp3));
        dept1.setFundedEmps(toSet(emp2, emp3));
        dept1.setMeetingRooms(toList(room1, room2, room3));

        dept2.setEmployees(toSet(emp4, emp5));
        dept2.setFundedEmps(toSet(emp1, emp4, emp5));
        dept2.setMeetingRooms(toList());




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

        // TODO why do we need this, i.e. what is the difference to root objects?
        registry.register("dept1", dept1);
        registry.register("dept2", dept2);
        registry.register("emp1", emp1);
        registry.register("emp2", emp2);
        registry.register("emp3", emp3);
        registry.register("emp4", emp4);
        registry.register("emp5", emp5);
        registry.register("proj1", proj1);
        registry.register("proj2", proj2);
        registry.register("proj3", proj3);
    }

    private static <T> List<T> toList(T... objs) {
        return Arrays.stream(objs).collect(Collectors.toList());
    }

    private static Map<String, String> toMap(String... objs) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < objs.length; i+=2) {
            map.put(objs[i], objs[i + 1]);
        }
        return map;
    }

    private static <T> Set<T> toSet(T... objs) {
        return Arrays.stream(objs).collect(Collectors.toSet());
    }
}
