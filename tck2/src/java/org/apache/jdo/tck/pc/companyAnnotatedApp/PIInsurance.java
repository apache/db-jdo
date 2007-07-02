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

/**
 * This interface represents the persistent state of Insurance.
 * Javadoc was deliberately omitted because it would distract from
 * the purpose of the interface.
 */
@PersistenceCapable(identityType=IdentityType.APPLICATION)
@Table(table="insuranceplans")
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy=DiscriminatorStrategy.CLASS_NAME,
        column="DISCRIMINATOR", indexed="true")
@Index(name="INS_DISCRIMINATOR_INDEX", unique="false",
        columns=@Column(name="DISCRIMINATOR"))
    public interface PIInsurance {

    @Field(primaryKey="true")
    @Column(name="INSID")
    long getInsid();
    @Column(name="CARRIER")
    String getCarrier();
    @Column(name="EMPLOYEE")
    PIEmployee getEmployee();

    void setInsid(long insid);
    void setCarrier(String carrier);
    void setEmployee(PIEmployee employee);
}
