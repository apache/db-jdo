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
 

package org.apache.jdo.tck.pc.companyAnnotatedJPA;

import javax.persistence.*;

import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.jdo.tck.pc.company.IAddress;

import org.apache.jdo.tck.pc.company.IPerson;
import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

/**
 * This class represents a person.
 */
@Entity
@Table(name="persons")
@IdClass(org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppPerson.Oid.class)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING,
        name="DISCRIMINATOR")
public class JPAAppPerson 
    implements IPerson, Serializable, Comparable, Comparator, DeepEquality  {

    @Id
    @Column(name="PERSONID")
    private long    personid;
    @Column(name="FIRSTNAME")
    private String  firstname;
    @Column(name="LASTNAME")
    private String  lastname;
    @Basic(optional=true, fetch=FetchType.LAZY)
    @Column(name="MIDDLENAME")
    private String  middlename;
    @Column(name="BIRTHDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date    birthdate;
    @Embedded
        @AttributeOverrides({
            @AttributeOverride(name="street",
                column=@Column(name="STREET")),
            @AttributeOverride(name="city",
                column=@Column(name="CITY")),
            @AttributeOverride(name="state",
                   column=@Column(name="STATE")),
            @AttributeOverride(name="zipcode",
                   column=@Column(name="ZIPCODE")),
            @AttributeOverride(name="country",
                   column=@Column(name="COUNTRY"))
        })
    private JPAAppAddress address;

    @OneToMany(mappedBy="person", cascade=CascadeType.ALL)
    @MapKey(name="type")
    private Map<String, JPAAppPhoneNumber> phoneNumbers
            = new HashMap<String, JPAAppPhoneNumber>();    
    protected static SimpleDateFormat formatter =
        new SimpleDateFormat("d/MMM/yyyy");

    /** This is the JDO-required no-args constructor. */
    protected JPAAppPerson() {}

    /**
     * Construct a <code>JPAAppPerson</code> instance.
     * 
     * 
     * @param personid The person identifier.
     * @param firstname The person's first name.
     * @param lastname The person's last name.
     * @param middlename The person's middle name.
     * @param birthdate The person's birthdate.
     */
    public JPAAppPerson(long personid, String firstname, String lastname, 
                  String middlename, Date birthdate) {
        this.personid = personid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.middlename = middlename;
        this.birthdate = birthdate;
    }

    /**
     * Construct a <code>JPAAppPerson</code> instance.
     * 
     * 
     * @param personid The person identifier.
     * @param firstname The person's first name.
     * @param lastname The person's last name.
     * @param middlename The person's middle name.
     * @param birthdate The person's birthdate.
     * @param address The person's address.
     */
    public JPAAppPerson(long personid, String firstname, String lastname, 
                  String middlename, Date birthdate, IAddress address) {
        this(personid, firstname, lastname, middlename, birthdate);
        this.address = (JPAAppAddress)address;
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
     * Get the address.
     * @return The address.
     */
    public IAddress getAddress() {
        return address;
    }

    /**
     * Set the address.
     * @param address The address.
     */
    public void setAddress(IAddress address) {
        this.address = (JPAAppAddress)address;
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
     * Get the map of phone numbers as an unmodifiable map.
     * @return A Map of phone numbers.
     */
    public Map getPhoneNumbers() {
        return (convertPhone2String(phoneNumbers));
    }

    /**
     * Get the phone number for the specified phone number type. 
     * @param type The phone number type ("home", "work", "mobile", etc.).
     * @return The phone number associated with specified type, or
     * <code>null</code> if there was no phone number for the type. 
     */
    public String getPhoneNumber(String type) {
        JPAAppPhoneNumber pnum = phoneNumbers.get(type);
        return pnum.getPhoneNumber();
    }
    
    /**
     * Associates the specified phone number with the specified type in the
     * map of phone numbers of this person. 
     * @param type The phone number type ("home", "work", "mobile", etc.).
     * @param phoneNumber The phone number 
     * @return The previous phone number associated with specified type, or
     * <code>null</code> if there was no phone number for the type. 
     */
    public String putPhoneNumber(String type, String phoneNumber) {
        JPAAppPhoneNumber pnum = phoneNumbers.get(type);
        String pnumAsString = null;
        if (pnum != null) {
            pnumAsString = pnum.getPhoneNumber(); // old val
        }
        pnum = phoneNumbers.put(type,
                new JPAAppPhoneNumber(this, type, phoneNumber));
        return pnumAsString;
    }

    /**
     * Remove a phoneNumber from the map of phone numbers.
     * @param type The phone number type ("home", "work", "mobile", etc.).
     * @return The previous phone number associated with specified type, or
     * <code>null</code> if there was no phone number for the type. 
     */
    public String removePhoneNumber(String type) {
        JPAAppPhoneNumber pnum = phoneNumbers.get(type);
        if (pnum == null)
            return null;
        String pnumAsString = pnum.getPhoneNumber(); // old val
        pnum = phoneNumbers.remove(type);
        return pnumAsString;
    }

    /**
     * Set the phoneNumber map to be in this person.
     * @param phoneNumbers A Map of phoneNumbers for this person.
     */
    public void setPhoneNumbers(Map phoneNumbers) {
        this.phoneNumbers = (phoneNumbers != null) ? 
                convertString2Phone(phoneNumbers) : null;
    }
    
    /**
     * Converts HashMap of String, String to HashMap of String, JPAAppPhoneNmber
     * @param pnums Map of phoneNumbers
     * @return Map of phoneNumbers
     */
    protected HashMap convertString2Phone(Map pnums) {
        HashMap retval = new HashMap();
        for (Object objEntry: pnums.entrySet()) {
            Map.Entry entry = (Map.Entry)objEntry;
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            JPAAppPhoneNumber newValue = 
                    new JPAAppPhoneNumber(this, key, value);
//            System.out.println("Key = " + key + "  Value = " + value);
            retval.put(key, newValue);
        }
        return retval;
    }
    
    /**
     * Converts HashMap of String, JPAAppPhoneNmber to HashMap of String, String
     * @param pnums Map of phoneNumbers
     * @return Map of phoneNumbers
     */
    protected HashMap convertPhone2String(Map pnums) {
        HashMap retval = new HashMap();
        for (Object objEntry: pnums.entrySet()) {
            Map.Entry entry = (Map.Entry)objEntry;
            String key = (String)entry.getKey();
            JPAAppPhoneNumber value = (JPAAppPhoneNumber)entry.getValue();
            String newValue = 
                    value.getPhoneNumber();
            retval.put(key, newValue);
        }
        return retval;
    }

    /**
     * Returns a String representation of a <code>JPAAppPerson</code> object.
     * 
     * @return a string representation of a <code>JPAAppPerson</code> object.
     */
    public String toString() {
        return "JPAPerson(" + getFieldRepr() + ")";
    }
    
    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    protected String getFieldRepr() {
        StringBuffer rc = new StringBuffer();
        rc.append(personid);
        rc.append(", ").append(lastname);
        rc.append(", ").append(firstname);
        rc.append(", born ").append(
            birthdate==null ? "null" : formatter.format(birthdate));
        rc.append(", phone ").append(convertPhone2String(phoneNumbers));
        return rc.toString();
    }

    /**
     * 
     * Returns <code>true</code> if all the fields of this instance are
     * deep equal to the coresponding fields of the specified JPAAppPerson.
     * 
     * 
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
        JPAAppPerson otherPerson = (JPAAppPerson)other;
        String where = "JPAPerson<" + personid + ">";
        return 
            helper.equals(personid, otherPerson.getPersonid(), where + ".personid") &
            helper.equals(firstname, otherPerson.getFirstname(), where + ".firstname") &
            helper.equals(lastname, otherPerson.getLastname(), where + ".lastname") &
            helper.equals(middlename, otherPerson.getMiddlename(), where + ".middlename") &
            helper.equals(birthdate, otherPerson.getBirthdate(), where + ".birthdate") &
            helper.deepEquals(address, otherPerson.getAddress(), where + ".address") &
            helper.deepEquals(convertPhone2String(phoneNumbers), otherPerson.getPhoneNumbers(), where + ".phoneNumbers");
    }

    /** 
     * Compares this object with the specified object for order. Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object. 
     * @param o The Object to be compared. 
     * @return a negative integer, zero, or a positive integer as this 
     * object is less than, equal to, or greater than the specified object. 
     * @throws ClassCastException - if the specified object's type prevents
     * it from being compared to this Object. 
     */
    public int compareTo(Object o) {
        return compareTo((JPAAppPerson)o);
    }

    /** 
     * Compare two instances. This is a method in Comparator.
     */
    public int compare(Object o1, Object o2) {
        return compare((JPAAppPerson)o1, (JPAAppPerson)o2);
    }

    /**
     * 
     * Compares this object with the specified JPAAppPerson object for
     * order. Returns a negative integer, zero, or a positive integer as
     * this object is less than, equal to, or greater than the specified
     * object.  
     * 
     * 
     * @param other The JPAAppPerson object to be compared.
     * @return a negative integer, zero, or a positive integer as this
     * object is less than, equal to, or greater than the specified JPAAppPerson
     * object.
     */
    public int compareTo(JPAAppPerson other) {
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
    public static int compare(JPAAppPerson o1, JPAAppPerson o2) {
        return EqualityHelper.compare(o1.getPersonid(), o2.getPersonid());
    }
    
    /** 
     * Indicates whether some other object is "equal to" this one.
     * @param obj the object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     * argument; <code>false</code> otherwise. 
     */
    public boolean equals(Object obj) {
        if (obj instanceof JPAAppPerson) {
            return compareTo((JPAAppPerson)obj) == 0;
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
    public static class Oid implements Serializable, Comparable {

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
        public int compareTo(Object obj) {
            // may throw ClassCastException which the user must handle
            Oid other = (Oid) obj;
            if( personid < other.personid ) return -1;
            if( personid > other.personid ) return 1;
            return 0;
        }

    }

}
