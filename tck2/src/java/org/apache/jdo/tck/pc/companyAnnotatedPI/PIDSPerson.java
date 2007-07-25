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

import java.util.Date;
import java.util.Map;
import org.apache.jdo.tck.pc.company.IAddress;

import org.apache.jdo.tck.pc.company.IPerson;

/**
 * This interface represents the persistent state of Person.
 * Javadoc was deliberately omitted because it would distract from
 * the purpose of the interface.
 */
@PersistenceCapable(table="persons")
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy=DiscriminatorStrategy.CLASS_NAME,
        column="DISCRIMINATOR", indexed="true")
@DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, 
        column="DATASTORE_IDENTITY")
public interface PIDSPerson extends IPerson {

    @Column(name="PERSONID")
    long getPersonid();
    @Column(name="LASTNAME")
    String getLastname();
    @Column(name="FIRSTNAME")
    String getFirstname();
    @Persistent(defaultFetchGroup="false")
    @Column(name="MIDDLENAME", allowsNull="true")
    String getMiddlename();
    @Persistent(boundTypes=org.apache.jdo.tck.pc.companyAnnotatedPI.PIAppAddress.class)
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
    Date getBirthdate();
    @Persistent(table="employee_phoneno_type")
    @Join(column="EMPID")
    @Key(boundTypes=java.lang.String.class)
    @Value(boundTypes=java.lang.String.class)
    Map getPhoneNumbers();
    
    void setPersonid(long personid);
    void setLastname(String lastname);
    void setFirstname(String firstname);
    void setMiddlename(String middlename);
    void setAddress(IAddress address);
    void setBirthdate(Date birthdate);
    void setPhoneNumbers(Map phoneNumbers);

}
