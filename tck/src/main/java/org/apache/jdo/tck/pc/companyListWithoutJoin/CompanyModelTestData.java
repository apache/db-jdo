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

package org.apache.jdo.tck.pc.companyListWithoutJoin;

import static org.apache.jdo.tck.util.DataSourceUtil.*;

import org.apache.jdo.tck.util.DataSource;
import org.apache.jdo.tck.util.DefaultListableInstanceFactory;

/**
 * Utility class to create a graph of company model instances.
 *
 * @author Michael Bouschen
 */
public class CompanyModelTestData implements DataSource<CompanyFactory> {

  /** Company instances for CompletenessTest with Map without join table. */
  @Override
  public void init(CompanyFactory factory, DefaultListableInstanceFactory registry) {

    // Instances / Constructors

    ICompany company1 = factory.newCompany(1L, "Sun Microsystems, Inc.", date(1952, 4, 11));

    IDepartment dept1 = factory.newDepartment(1, "Development", company1);
    IDepartment dept2 = factory.newDepartment(2, "Human Resources", company1);
    company1.setDepartments(toSet(dept1, dept2));

    IFullTimeEmployee emp1 =
        factory.newFullTimeEmployee(
            1, "emp1First", "emp1Last", "emp1Middle", date(1970, 6, 10), date(1999, 1, 1), 60000);

    IFullTimeEmployee emp2 =
        factory.newFullTimeEmployee(
            2, "emp2First", "emp2Last", "emp2Middle", date(1975, 12, 22), date(2003, 7, 1), 47000);

    IFullTimeEmployee emp3 =
        factory.newFullTimeEmployee(
            3, "emp3First", "emp3Last", "emp3Middle", date(1972, 7, 5), date(2002, 8, 15), 67.00);

    IFullTimeEmployee emp4 =
        factory.newFullTimeEmployee(
            4, "emp4First", "emp4Last", "emp4Middle", date(1973, 7, 6), date(2001, 4, 15), 37.00);

    IFullTimeEmployee emp5 =
        factory.newFullTimeEmployee(
            5, "emp5First", "emp5Last", "emp5Middle", date(1962, 7, 5), date(1998, 8, 15), 73000);

    emp1.setWeeklyhours(40);
    emp1.setDepartment(dept1);
    emp1.setFundingDept(dept2);
    emp1.setManager(emp2);
    emp1.setHradvisor(emp5);

    emp2.setWeeklyhours(40);
    emp2.setDepartment(dept1);
    emp2.setFundingDept(dept1);
    emp2.setHradvisor(emp5);
    emp2.setTeam(toSet(emp1, emp3, emp4, emp5));

    emp3.setWeeklyhours(19);
    emp3.setDepartment(dept1);
    emp3.setFundingDept(dept1);
    emp3.setManager(emp2);
    emp3.setHradvisor(emp5);

    emp4.setDepartment(dept2);
    emp4.setFundingDept(dept2);
    emp4.setManager(emp2);
    emp4.setHradvisor(emp5);

    emp5.setDepartment(dept2);
    emp5.setFundingDept(dept2);
    emp5.setManager(emp2);
    emp5.setHradvisees(toSet(emp1, emp2, emp3, emp4));

    dept1.setEmployees(toList(emp1, emp2, emp3));
    dept2.setEmployees(toList(emp4, emp5));

    // Register named objects

    registry.register("company1", company1);
  }
}
