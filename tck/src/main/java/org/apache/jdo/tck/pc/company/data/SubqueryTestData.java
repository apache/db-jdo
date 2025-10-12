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

import org.apache.jdo.tck.pc.company.*;
import org.apache.jdo.tck.util.DefaultListableInstanceFactory;

public class SubqueryTestData {

  public static void initSubqueryTest(
          CompanyFactory factory, DefaultListableInstanceFactory registry) {
    IAddress addr1 =
        factory.newAddress(1, "Unter den Linden 1", "Berlin", "  ", "12345", "Germany");
    IAddress addr2 = factory.newAddress(2, "Broadway 1", "New York", "NY", "10000", "USA");
    IAddress addr3 = factory.newAddress(3, "Market St.", "San Francisco", "CA", "94102", "USA");

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
    IFullTimeEmployee emp1 =
            factory.newFullTimeEmployee(
                    1, "emp1First", "emp1Last", "emp1Middle", null, addr1, null, 30000);
    emp1.setBirthdate(date(1970, 6, 10));
    emp1.setHiredate(date(1999, 1, 1));
    IFullTimeEmployee emp2 =
        factory.newFullTimeEmployee(
            2, "emp2First", "emp2Last", "emp2Middle", null, addr2, null, 20000);
    emp2.setBirthdate(date(1975, 12, 22));
    emp2.setHiredate(date(2003, 7, 1));
    IPartTimeEmployee emp3 =
        factory.newPartTimeEmployee(
            3, "emp3First", "emp3Last", "emp3Middle", null, addr3, null, 10000);
    emp3.setBirthdate(date(1972, 7, 5));
    emp3.setHiredate(date(2002, 8, 15));
    IPartTimeEmployee emp4 =
        factory.newPartTimeEmployee(
            4, "emp4First", "emp4Last", "emp4Middle", null, addr2, null, 25000);
    emp4.setBirthdate(date(1973, 7, 6));
    emp4.setHiredate(date(2001, 4, 15));
    IFullTimeEmployee emp5 =
            factory.newFullTimeEmployee(
                    5, "emp5First", "emp5Last", "emp5Middle", null, addr2, null, 18000);
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
    emp1.setWeeklyhours(40);
    emp1.setDepartment(dept1);
    emp1.setManager(null);
    emp1.setTeam(toSet(emp2, emp3, emp10));

    emp2.setWeeklyhours(40);
    emp2.setDepartment(dept1);
    emp2.setManager(emp1);
    emp2.setTeam(toSet());

    emp3.setWeeklyhours(25);
    emp3.setDepartment(dept1);
    emp3.setManager(emp1);
    emp3.setTeam(toSet());

    emp4.setWeeklyhours(40);
    emp4.setDepartment(dept1);
    emp4.setManager(null);
    emp4.setTeam(toSet(emp5, emp6));

    emp5.setWeeklyhours(35);
    emp5.setDepartment(dept1);
    emp5.setManager(emp4);
    emp5.setTeam(toSet());

    emp6.setWeeklyhours(40);
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
    dept1.setMeetingRooms(toList(room1, room2));

    dept2.setEmployees(toSet(emp7, emp8, emp9, emp10));
    dept2.setMeetingRooms(toList(room3));

    // root objects
    registry.register("company1", company1);

    registry.register("dept1", dept1);
    registry.register("dept2", dept2);
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
    registry.register("room1", room1);
    registry.register("room2", room2);
  }
}
