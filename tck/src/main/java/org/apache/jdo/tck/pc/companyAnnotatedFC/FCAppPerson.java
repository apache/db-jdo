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

package org.apache.jdo.tck.pc.companyAnnotatedFC;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Key;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Value;
import org.apache.jdo.tck.pc.company.IAddress;
import org.apache.jdo.tck.pc.company.IPerson;
import org.apache.jdo.tck.pc.compositeAnnotation.ApplicationIdDiscriminatorClassName;
import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;
import org.apache.jdo.tck.util.JDOCustomDateEditor;

/** This class represents a person. */
@PersistenceCapable(table = "persons")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@ApplicationIdDiscriminatorClassName
public class FCAppPerson
    implements IPerson, Serializable, Comparable<IPerson>, Comparator<IPerson>, DeepEquality {

  private static final long serialVersionUID = 1L;

  @PrimaryKey
  @Column(name = "PERSONID")
  private long personid;

  @Column(name = "FIRSTNAME")
  private String firstname;

  @Column(name = "LASTNAME")
  private String lastname;

  @Persistent(defaultFetchGroup = "false")
  @Column(name = "MIDDLENAME", allowsNull = "true")
  private String middlename;

  @Column(name = "BIRTHDATE")
  private Date birthdate;

  @Embedded(
      nullIndicatorColumn = "COUNTRY",
      members = {
        @Persistent(name = "addrid", columns = @Column(name = "ADDRID")),
        @Persistent(name = "street", columns = @Column(name = "STREET")),
        @Persistent(name = "city", columns = @Column(name = "CITY")),
        @Persistent(name = "state", columns = @Column(name = "STATE")),
        @Persistent(name = "zipcode", columns = @Column(name = "ZIPCODE")),
        @Persistent(name = "country", columns = @Column(name = "COUNTRY"))
      })
  private FCAppAddress address;

  // maps phone number types ("home", "work", "mobile", etc.)
  // to phone numbers specified as String
  @Persistent(table = "employee_phoneno_type")
  @Join(column = "EMPID")
  @Key(types = java.lang.String.class, column = "TYPE")
  @Value(types = java.lang.String.class, column = "PHONENO")
  private Map<String, String> phoneNumbers = new HashMap<>();

  /** This is the JDO-required no-args constructor. */
  protected FCAppPerson() {}

  /**
   * Construct a <code>FCAppPerson</code> instance.
   *
   * @param personid The person identifier.
   * @param firstname The person's first name.
   * @param lastname The person's last name.
   * @param middlename The person's middle name.
   * @param birthdate The person's birthdate.
   */
  public FCAppPerson(
      long personid, String firstname, String lastname, String middlename, Date birthdate) {
    this.personid = personid;
    this.firstname = firstname;
    this.lastname = lastname;
    this.middlename = middlename;
    this.birthdate = birthdate;
  }

  /**
   * Construct a <code>FCAppPerson</code> instance.
   *
   * @param personid The person identifier.
   * @param firstname The person's first name.
   * @param lastname The person's last name.
   * @param middlename The person's middle name.
   * @param birthdate The person's birthdate.
   * @param address The person's address.
   */
  public FCAppPerson(
      long personid,
      String firstname,
      String lastname,
      String middlename,
      Date birthdate,
      IAddress address) {
    this(personid, firstname, lastname, middlename, birthdate);
    this.address = (FCAppAddress) address;
  }

  /**
   * Set the id associated with this object.
   *
   * @param id the id.
   */
  public void setPersonid(long id) {
    if (this.personid != 0) throw new IllegalStateException("Id is already set.");
    this.personid = id;
  }

  /**
   * Get the person's id.
   *
   * @return The personid.
   */
  public long getPersonid() {
    return personid;
  }

  /**
   * Set the person's id.
   *
   * @param personid The personid.
   */
  public void setLastname(long personid) {
    this.personid = personid;
  }

  /**
   * Get the person's last name.
   *
   * @return The last name.
   */
  public String getLastname() {
    return lastname;
  }

  /**
   * Set the person's last name.
   *
   * @param lastname The last name.
   */
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  /**
   * Get the person's first name.
   *
   * @return The first name.
   */
  public String getFirstname() {
    return firstname;
  }

  /**
   * Set the person's first name.
   *
   * @param firstname The first name.
   */
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  /**
   * Get the person's middle name.
   *
   * @return The middle name.
   */
  public String getMiddlename() {
    return middlename;
  }

  /**
   * Set the person's middle name.
   *
   * @param middlename The middle name.
   */
  public void setMiddlename(String middlename) {
    this.middlename = middlename;
  }

  /**
   * Get the address.
   *
   * @return The address.
   */
  public IAddress getAddress() {
    return address;
  }

  /**
   * Set the address.
   *
   * @param address The address.
   */
  public void setAddress(IAddress address) {
    this.address = (FCAppAddress) address;
  }

  /**
   * Get the person's birthdate.
   *
   * @return The person's birthdate.
   */
  public Date getBirthdate() {
    return birthdate;
  }

  /**
   * Set the person's birthdate.
   *
   * @param birthdate The person's birthdate.
   */
  public void setBirthdate(Date birthdate) {
    this.birthdate = birthdate;
  }

  /**
   * Get the map of phone numbers as an unmodifiable map.
   *
   * @return The map of phone numbers, as an unmodifiable map.
   */
  public Map<String, String> getPhoneNumbers() {
    return Collections.unmodifiableMap(phoneNumbers);
  }

  /**
   * Get the phone number for the specified phone number type.
   *
   * @param type The phone number type ("home", "work", "mobile", etc.).
   * @return The phone number associated with specified type, or <code>null</code> if there was no
   *     phone number for the type.
   */
  public String getPhoneNumber(String type) {
    return phoneNumbers.get(type);
  }

  /**
   * Associates the specified phone number with the specified type in the map of phone numbers of
   * this person.
   *
   * @param type The phone number type ("home", "work", "mobile", etc.).
   * @param phoneNumber The phone number
   * @return The previous phone number associated with specified type, or <code>null</code> if there
   *     was no phone number for the type.
   */
  public String putPhoneNumber(String type, String phoneNumber) {
    return phoneNumbers.put(type, phoneNumber);
  }

  /**
   * Remove a phoneNumber from the map of phone numbers.
   *
   * @param type The phone number type ("home", "work", "mobile", etc.).
   * @return The previous phone number associated with specified type, or <code>null</code> if there
   *     was no phone number for the type.
   */
  public String removePhoneNumber(String type) {
    return phoneNumbers.remove(type);
  }

  /**
   * Set the phoneNumber map to be in this person.
   *
   * @param phoneNumbers The map of phoneNumbers for this person.
   */
  public void setPhoneNumbers(Map<String, String> phoneNumbers) {
    // workaround: create a new HashMap, because fostore does not
    // support LinkedHashMap
    this.phoneNumbers = (phoneNumbers != null) ? new HashMap<>(phoneNumbers) : null;
  }

  /**
   * Returns a String representation of a <code>FCAppPerson</code> object.
   *
   * @return a string representation of a <code>FCAppPerson</code> object.
   */
  public String toString() {
    return "FCPerson(" + getFieldRepr() + ")";
  }

  /**
   * Returns a String representation of the non-relationship fields.
   *
   * @return a String representation of the non-relationship fields.
   */
  protected String getFieldRepr() {
    StringBuilder rc = new StringBuilder();
    rc.append(personid);
    rc.append(", ").append(lastname);
    rc.append(", ").append(firstname);
    rc.append(", born ").append(JDOCustomDateEditor.getDateRepr(birthdate));
    rc.append(", phone ").append(phoneNumbers);
    return rc.toString();
  }

  /**
   * Returns <code>true</code> if all the fields of this instance are deep equal to the coresponding
   * fields of the specified FCAppPerson.
   *
   * @param other the object with which to compare.
   * @param helper EqualityHelper to keep track of instances that have already been processed.
   * @return <code>true</code> if all the fields are deep equal; <code>false</code> otherwise.
   * @throws ClassCastException if the specified instances' type prevents it from being compared to
   *     this instance.
   */
  public boolean deepCompareFields(Object other, EqualityHelper helper) {
    FCAppPerson otherPerson = (FCAppPerson) other;
    String where = "FCPerson<" + personid + ">";
    return helper.equals(personid, otherPerson.getPersonid(), where + ".personid")
        & helper.equals(firstname, otherPerson.getFirstname(), where + ".firstname")
        & helper.equals(lastname, otherPerson.getLastname(), where + ".lastname")
        & helper.equals(middlename, otherPerson.getMiddlename(), where + ".middlename")
        & helper.equals(birthdate, otherPerson.getBirthdate(), where + ".birthdate")
        & helper.deepEquals(address, otherPerson.getAddress(), where + ".address")
        & helper.deepEquals(phoneNumbers, otherPerson.getPhoneNumbers(), where + ".phoneNumbers");
  }

  /**
   * Compares this object with the specified FCAppPerson object for order. Returns a negative
   * integer, zero, or a positive integer as this object is less than, equal to, or greater than the
   * specified object.
   *
   * @param other The FCAppPerson object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified FFCAppPerson object.
   */
  public int compareTo(IPerson other) {
    return compare(this, other);
  }

  /**
   * Compares its two IPerson arguments for order. Returns a negative integer, zero, or a positive
   * integer as the first argument is less than, equal to, or greater than the second.
   *
   * @param o1 the first IPerson object to be compared.
   * @param o2 the second IPerson object to be compared.
   * @return a negative integer, zero, or a positive integer as the first object is less than, equal
   *     to, or greater than the second object.
   */
  public int compare(IPerson o1, IPerson o2) {
    return EqualityHelper.compare(o1.getPersonid(), o2.getPersonid());
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>
   *     otherwise.
   */
  public boolean equals(Object obj) {
    if (obj instanceof FCAppPerson) {
      return compareTo((FCAppPerson) obj) == 0;
    }
    return false;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for this object.
   */
  public int hashCode() {
    return (int) personid;
  }
  /**
   * This class is used to represent the application identifier for the <code>Person</code> class.
   */
  public static class Oid implements Serializable, Comparable<Oid> {

    private static final long serialVersionUID = 1L;

    /**
     * This field represents the identifier for the <code>Person</code> class. It must match a field
     * in the <code>Person</code> class in both name and type.
     */
    public long personid;

    /** The required public no-arg constructor. */
    public Oid() {}

    /**
     * Initialize the identifier.
     *
     * @param personid The person identifier.
     */
    public Oid(long personid) {
      this.personid = personid;
    }

    public Oid(String s) {
      personid = Long.parseLong(justTheId(s));
    }

    public String toString() {
      return this.getClass().getName() + ": " + personid;
    }

    /** */
    public boolean equals(java.lang.Object obj) {
      if (obj == null || !this.getClass().equals(obj.getClass())) return (false);
      Oid o = (Oid) obj;
      if (this.personid != o.personid) return (false);
      return (true);
    }

    /** */
    public int hashCode() {
      return ((int) personid);
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
