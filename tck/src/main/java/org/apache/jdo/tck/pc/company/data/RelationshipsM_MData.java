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

import static org.apache.jdo.tck.util.DataSourceUtil.date;
import static org.apache.jdo.tck.util.DataSourceUtil.toSet;

import java.math.BigDecimal;

import org.apache.jdo.tck.pc.company.CompanyFactory;
import org.apache.jdo.tck.pc.company.IAddress;
import org.apache.jdo.tck.pc.company.ICompany;
import org.apache.jdo.tck.pc.company.IDentalInsurance;
import org.apache.jdo.tck.pc.company.IDepartment;
import org.apache.jdo.tck.pc.company.IFullTimeEmployee;
import org.apache.jdo.tck.pc.company.IMedicalInsurance;
import org.apache.jdo.tck.pc.company.IPartTimeEmployee;
import org.apache.jdo.tck.pc.company.IProject;
import org.apache.jdo.tck.util.DefaultListableInstanceFactory;

public class RelationshipsM_MData implements CompanyDataSource {

  @Override
  public void init(CompanyFactory factory, DefaultListableInstanceFactory registry) {
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
        factory.newDentalInsurance(11, "Carrier1", BigDecimal.valueOf(99.999));
    IDentalInsurance dentalIns2 =
        factory.newDentalInsurance(12, "Carrier2", BigDecimal.valueOf(99.999));
    IDentalInsurance dentalIns3 =
        factory.newDentalInsurance(13, "Carrier3", BigDecimal.valueOf(99.999));
    IDentalInsurance dentalIns4 =
        factory.newDentalInsurance(14, "Carrier4", BigDecimal.valueOf(99.999));
    IDentalInsurance dentalIns5 =
        factory.newDentalInsurance(15, "Carrier5", BigDecimal.valueOf(99.999));

    // Project constructors
    IProject proj1 = factory.newProject(1, "orange", BigDecimal.valueOf(2500000.99));
    IProject proj2 = factory.newProject(2, "blue", BigDecimal.valueOf(50000.00));
    IProject proj3 = factory.newProject(3, "green", BigDecimal.valueOf(2000.99));

    // Company constructor
    ICompany company1 = factory.newCompany(1L, "Sun Microsystems, Inc.", date(1952, 4, 11), addr1);

    // Meeting room constructors

    // Department Constructors
    IDepartment dept1 = factory.newDepartment(1, "Development", company1);
    IDepartment dept2 = factory.newDepartment(2, "Human Resources", company1);
    company1.setDepartments(toSet(dept1, dept2));

    // Employee Constructors
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
            4, "emp4First", "emp4Last", "emp4Middle", null, addr3, null, 13);
    emp4.setBirthdate(date(1973, 7, 6));
    emp4.setHiredate(date(2001, 4, 15));
    IFullTimeEmployee emp5 =
        factory.newFullTimeEmployee(
            5, "emp5First", "emp5Last", "emp5Middle", null, addr3, null, 45000);
    emp5.setBirthdate(date(1962, 7, 5));
    emp5.setHiredate(date(1998, 8, 15));

    // Employee properties
    emp1.setWeeklyhours(40);
    emp1.setReviewedProjects(toSet(proj3));
    emp1.setProjects(toSet(proj1));

    emp2.setWeeklyhours(40);
    emp2.setProjects(toSet(proj1, proj2));

    emp3.setWeeklyhours(19);
    emp3.setProjects(toSet(proj1, proj2));

    emp4.setReviewedProjects(toSet(proj2));
    emp4.setProjects(toSet(proj3));

    emp5.setReviewedProjects(toSet(proj2));
    emp5.setProjects(toSet(proj3));

    // Department properties

    // Insurance properties

    // Project properties
    proj1.setMembers(toSet(emp1, emp2, emp3));

    proj2.setReviewers(toSet(emp4, emp5));
    proj2.setMembers(toSet(emp2, emp3));

    proj3.setReviewers(toSet(emp1));
    proj3.setMembers(toSet(emp4, emp5));

    // root objects
    registry.register("company1", company1);
    registry.register("dept1", dept1);
    registry.register("dept2", dept2);
    registry.register("emp1", emp1);
    registry.register("emp2", emp2);
    registry.register("emp3", emp3);
    registry.register("emp4", emp4);
    registry.register("emp5", emp5);
    registry.register("medicalIns1", medicalIns1);
    registry.register("medicalIns2", medicalIns2);
    registry.register("medicalIns3", medicalIns3);
    registry.register("dentalIns1", dentalIns1);
  }
}
