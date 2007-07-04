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

package org.apache.jdo.tck.pc.companyAnnotatedDS;

import javax.jdo.annotations.*;

import java.util.Date;
import java.util.Set;

import org.apache.jdo.tck.pc.company.ICompany;

/**
 * This interface represents the persistent state of Company.
 * Javadoc was deliberately omitted because it would distract from
 * the purpose of the interface.
 */
@PersistenceCapable(table="companies")
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy=DiscriminatorStrategy.CLASS_NAME,
        column="DISCRIMINATOR")
@DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, 
        column="DATASTORE_IDENTITY")
public interface PICompany extends ICompany {
    
    @Property(persistenceModifier=FieldPersistenceModifier.PERSISTENT)
    @Embedded(nullIndicatorColumn="COUNTRY",
        fields={
            @Field(name="addrid", columns=@Column(name="ADDRID")),
            @Field(name="street", columns=@Column(name="STREET")),
            @Field(name="city", columns=@Column(name="CITY")),
            @Field(name="state", columns=@Column(name="STATE")),
            @Field(name="zipcode", columns=@Column(name="ZIPCODE")),
            @Field(name="country", columns=@Column(name="COUNTRY"))
    })
    PIAddress getAddress();
    @Column(name="ID")
    long getCompanyid();
    @Property(persistenceModifier=FieldPersistenceModifier.PERSISTENT)
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedDS.PIDepartment.class)
    Set getDepartments();
    @Column(name="FOUNDEDDATE")
    Date getFounded();
    @Column(name="NAME", jdbcType="VARCHAR")
    String getName();
    
    void setAddress(PIAddress a);
    void setCompanyid(long id);
    void setDepartments(Set depts);
    void setFounded(Date date);
    void setName(String string);
}
