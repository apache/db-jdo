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
 
package org.apache.jdo.tck.pc.companyAnnotatedApp;

import javax.jdo.annotations.*;

import java.util.Set;

/**
 * This interface represents the persistent state of Department.
 * Javadoc was deliberately omitted because it would distract from
 * the purpose of the interface.
 */
@PersistenceCapable(identityType=IdentityType.APPLICATION)
@Implements ("org.apache.jdo.tck.pc.company.IDepartment")
@Table(table="departments")
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy=DiscriminatorStrategy.CLASS_NAME,
        column="DISCRIMINATOR")
public interface PIDepartment {

    @Property(primaryKey="true")
    @Column(name="ID")
    long getDeptid();
    @Column(name="NAME")
    String getName();
    @Column(name="COMPANYID")
    PICompany getCompany();
    @Column(name="EMP_OF_THE_MONTH")
    PIEmployee getEmployeeOfTheMonth();
    @Property(persistenceModifier=FieldPersistenceModifier.PERSISTENT, 
            mappedBy="department")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedApp.PIEmployee.class)
    Set getEmployees();
    @Property(persistenceModifier=FieldPersistenceModifier.PERSISTENT,
            mappedBy="fundingDept")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedApp.PIEmployee.class)
    Set getFundedEmps();
    
    void setDeptid(long deptid);
    void setName(String name);
    void setCompany(PICompany company);
    void setEmployeeOfTheMonth(PIEmployee employeeOfTheMonth);
    void setEmployees(Set employees);
    void setFundedEmps(Set employees);

}
