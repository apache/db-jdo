/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.pc.companyAnnotatedPI;

import java.util.Set;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PersistenceModifier;
import javax.jdo.annotations.Persistent;
import org.apache.jdo.tck.pc.company.ICompany;
import org.apache.jdo.tck.pc.company.IDepartment;
import org.apache.jdo.tck.pc.company.IEmployee;

/**
 * This interface represents the persistent state of Department. Javadoc was deliberately omitted
 * because it would distract from the purpose of the interface.
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION, table = "departments")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME, column = "DISCRIMINATOR")
public interface PIAppDepartment extends IDepartment {

  @Persistent(primaryKey = "true")
  @Column(name = "ID")
  long getDeptid();

  @Column(name = "NAME")
  String getName();

  @Persistent(types = org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppCompany.class)
  @Column(name = "COMPANYID")
  ICompany getCompany();

  @Persistent(types = org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppEmployee.class)
  @Column(name = "EMP_OF_THE_MONTH")
  IEmployee getEmployeeOfTheMonth();

  @Persistent(persistenceModifier = PersistenceModifier.PERSISTENT, mappedBy = "department")
  @Element(types = org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppEmployee.class)
  Set<IEmployee> getEmployees();

  @Persistent(persistenceModifier = PersistenceModifier.PERSISTENT, mappedBy = "fundingDept")
  @Element(types = org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppEmployee.class)
  Set<IEmployee> getFundedEmps();

  void setDeptid(long deptid);

  void setName(String name);

  void setCompany(ICompany company);

  void setEmployeeOfTheMonth(IEmployee employeeOfTheMonth);

  void setEmployees(Set<IEmployee> employees);

  void setFundedEmps(Set<IEmployee> employees);
}
