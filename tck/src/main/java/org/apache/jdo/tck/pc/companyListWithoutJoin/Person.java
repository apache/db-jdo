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

import java.io.Serializable;

import java.util.Comparator;
import java.util.Date;

import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;
import org.apache.jdo.tck.util.JDOCustomDateEditor;

/**
 * This class represents a person.
 */
public class Person 
    implements IPerson, Serializable, Comparable<IPerson>, Comparator<IPerson>, DeepEquality  {

    private static final long serialVersionUID = 1L;

    private long    personid;
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
        this.personid = personid;
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
        if (this.personid != 0)
            throw new IllegalStateException("Id is already set.");
        this.personid = id;
    }

    /**
     * Get the person's id.
     * @return The personid.
     */
    public long getPersonid() {
        return personid;
    }

    /**
     * Set the person's id.
     * @param personid The personid.
     */
    public void setLastname(long personid) {
        this.personid = personid;
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
    public String toString() {
        return "Person(" + getFieldRepr() + ")";
    }
    
    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    protected String getFieldRepr() {
        StringBuilder rc = new StringBuilder();
        rc.append(personid);
        rc.append(", ").append(lastname);
        rc.append(", ").append(firstname);
        rc.append(", born ").append(JDOCustomDateEditor.getDateRepr(birthdate));
        return rc.toString();
    }

    /** 
     * Returns <code>true</code> if all the fields of this instance are
     * deep equal to the coresponding fields of the specified Person.
     * @param other the object with which to compare.
     * @param helper EqualityHelper to keep track of instances that have
     * already been processed. 
     * @return <code>true</code> if all the fields are deep equal;
     * <code>false</code> otherwise.  
     * @throws ClassCastException if the specified instances' type prevents
     * it from being compared to this instance. 
     */
    public boolean deepCompareFields(Object other, 
                                     EqualityHelper helper) {
        IPerson otherPerson = (IPerson)other;
        String where = "Person<" + personid + ">";
        return 
            helper.equals(personid, otherPerson.getPersonid(), where + ".personid") &
            helper.equals(firstname, otherPerson.getFirstname(), where + ".firstname") &
            helper.equals(lastname, otherPerson.getLastname(), where + ".lastname") &
            helper.equals(middlename, otherPerson.getMiddlename(), where + ".middlename") &
            helper.equals(birthdate, otherPerson.getBirthdate(), where + ".birthdate") ;
    }

    /** 
     * Compares this object with the specified Person object for
     * order. Returns a negative integer, zero, or a positive integer as
     * this object is less than, equal to, or greater than the specified
     * object.  
     * @param other The Person object to be compared. 
     * @return a negative integer, zero, or a positive integer as this
     * object is less than, equal to, or greater than the specified Person 
     * object. 
     */
    public int compareTo(IPerson other) {
        return compare(this, other);
    }

    /**
     * Compares its two IPerson arguments for order. Returns a negative
     * integer, zero, or a positive integer as the first argument is less
     * than, equal to, or greater than the second. 
     * @param o1 the first IPerson object to be compared. 
     * @param o2 the second IPerson object to be compared. 
     * @return a negative integer, zero, or a positive integer as the first
     * object is less than, equal to, or greater than the second object. 
     */
    public int compare(IPerson o1, IPerson o2) {
        return EqualityHelper.compare(o1.getPersonid(), o2.getPersonid());
    }
    
    /** 
     * Indicates whether some other object is "equal to" this one.
     * @param obj the object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     * argument; <code>false</code> otherwise. 
     */
    public boolean equals(Object obj) {
        if (obj instanceof IPerson) {
            return compareTo((IPerson)obj) == 0;
        }
        return false;
    }
        
    /**
     * Returns a hash code value for the object. 
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return (int)personid;
    }
    /**
     * This class is used to represent the application identifier
     * for the <code>Person</code> class.
     */
    public static class Oid implements Serializable, Comparable<Oid> {

        private static final long serialVersionUID = 1L;

        /**
         * This field represents the identifier for the <code>Person</code>
         * class. It must match a field in the <code>Person</code> class in
         * both name and type. 
         */
        public long personid;

        /**
         * The required public no-arg constructor.
         */
        public Oid() { }

        /**
         * Initialize the identifier.
         * @param personid The person identifier.
         */
        public Oid(long personid) {
            this.personid = personid;
        }
        
        public Oid(String s) { personid = Long.parseLong(justTheId(s)); }

        public String toString() { return this.getClass().getName() + ": "  + personid;}

        /** */
        public boolean equals(java.lang.Object obj) {
            if( obj==null ||
                !this.getClass().equals(obj.getClass()) ) return( false );
            Oid o = (Oid) obj;
            if( this.personid != o.personid ) return( false );
            return( true );
        }

        /** */
        public int hashCode() {
            return( (int) personid );
        }
        
        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }

        /** */
        public int compareTo(Oid obj) {
            return Long.compare(personid, obj.personid);
        }

    }

}
