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

package org.apache.jdo.tck.pc.companyMapWithoutJoin;

import java.util.Date;
import java.util.Set;

/**
 * This interface represents the persistent state of Employee. Javadoc was deliberately omitted
 * because it would distract from the purpose of the interface.
 */
public interface IEmployee extends IPerson {

  Date getHiredate();

  double getWeeklyhours();

  String getRole();

  IDepartment getDepartment();

  IDepartment getFundingDept();

  IEmployee getManager();

  Set getTeam();

  IEmployee getMentor();

  IEmployee getProtege();

  IEmployee getHradvisor();

  Set getHradvisees();

  void setHiredate(Date hiredate);

  void setWeeklyhours(double weeklyhours);

  void setRole(String role);

  void setDepartment(IDepartment department);

  void setFundingDept(IDepartment department);

  void setManager(IEmployee manager);

  void setTeam(Set team);

  void setMentor(IEmployee mentor);

  void setProtege(IEmployee protege);

  void setHradvisor(IEmployee hradvisor);

  void setHradvisees(Set hradvisees);
}
