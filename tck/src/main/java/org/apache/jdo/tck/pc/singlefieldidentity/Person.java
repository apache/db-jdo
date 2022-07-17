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
 

package org.apache.jdo.tck.pc.singlefieldidentity;

import org.apache.jdo.tck.util.JDOCustomDateEditor;

import java.io.Serializable;

import java.util.Date;

/**
 * This class represents a person.
 */
public class Person 
    implements Serializable  {

    private static final long serialVersionUID = 1L;

    private long    id;
    private String  firstname;
    private String  lastname;
    private String  middlename;
    private Date    birthdate;

    /** This is the JDO-required no-args constructor. */
    protected Person() {}

    /**
     * Construct a <code>Person</code> instance.
     * @param personid The person identifier.
     * @param firstname The person's first name.
     * @param lastname The person's last name.
     * @param middlename The person's middle name.
     * @param birthdate The person's birthdate.
     */
    public Person(long personid, String firstname, String lastname, 
                  String middlename, Date birthdate) {
        this.id = personid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.middlename = middlename;
        this.birthdate = birthdate;
    }

    /**
     * Set the id associated with this object.
     * @param id the id.
     */
    public void setPersonid(long id) {
        if (this.id != 0)
            throw new IllegalStateException("Id is already set.");
        this.id = id;
    }

    /**
     * Get the person's id.
     * @return The id.
     */
    public long getPersonid() {
        return id;
    }

    /**
     * Get the person's last name.
     * @return The last name.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Set the person's last name.
     * @param lastname The last name.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Get the person's first name.
     * @return The first name.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Set the person's first name.
     * @param firstname The first name.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Get the person's middle name.
     * @return The middle name.
     */
    public String getMiddlename() {
        return middlename;
    }

    /**
     * Set the person's middle name.
     * @param middlename The middle name.
     */
    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    /**
     * Get the person's birthdate.
     * @return The person's birthdate.
     */
    public Date getBirthdate() {
        return birthdate;
    }

    /**
     * Set the person's birthdate.
     * @param birthdate The person's birthdate.
     */
    public void setBirthdate(Date birthdate) {
        this. birthdate = birthdate;
    }

    /**
     * Returns a String representation of a <code>Person</code> object.
     * @return a string representation of a <code>Person</code> object.
     */
    @Override
    public String toString() {
        return "Person(" + getFieldRepr() + ")";
    }
    
    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    protected String getFieldRepr() {
        StringBuilder rc = new StringBuilder();
        rc.append(id);
        rc.append(", ").append(lastname);
        rc.append(", ").append(firstname);
        rc.append(", born ").append(JDOCustomDateEditor.getDateRepr(birthdate));
        return rc.toString();
    }

}
