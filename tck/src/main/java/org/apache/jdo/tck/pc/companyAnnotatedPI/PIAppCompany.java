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

package org.apache.jdo.tck.pc.companyAnnotatedPI;

import javax.jdo.annotations.*;

import org.apache.jdo.tck.pc.company.ICompany;
import java.util.Date;
import java.util.Set;
import org.apache.jdo.tck.pc.company.IAddress;
import org.apache.jdo.tck.pc.company.IDepartment;

/**
 * This interface represents the persistent state of Company.
 * Javadoc was deliberately omitted because it would distract from
 * the purpose of the interface.
 */
@PersistenceCapable(identityType=IdentityType.APPLICATION, table="companies")
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy=DiscriminatorStrategy.CLASS_NAME,
        column="DISCRIMINATOR")
public interface PIAppCompany extends ICompany {
    
    @Persistent(types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppAddress.class)
    @Embedded(nullIndicatorColumn="COUNTRY",
        members={
            @Persistent(name="addrid", columns=@Column(name="ADDRID")),
            @Persistent(name="street", columns=@Column(name="STREET")),
            @Persistent(name="city", columns=@Column(name="CITY")),
            @Persistent(name="state", columns=@Column(name="STATE")),
            @Persistent(name="zipcode", columns=@Column(name="ZIPCODE")),
            @Persistent(name="country", columns=@Column(name="COUNTRY"))
    })
    IAddress getAddress();
    @PrimaryKey
    @Column(name="ID")
    long getCompanyid();
    @Persistent(mappedBy="company")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppDepartment.class)
    Set<IDepartment> getDepartments();
    @Column(name="FOUNDEDDATE")
    Date getFounded();
    @Column(name="NAME", jdbcType="VARCHAR")
    String getName();
    
    void setAddress(IAddress a);
    void setCompanyid(long id);
    void setDepartments(Set<IDepartment> depts);
    void setFounded(Date date);
    void setName(String string);
}
