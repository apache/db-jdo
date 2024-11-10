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

package org.apache.jdo.tck.pc.companyAnnotatedPC;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Key;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PersistenceModifier;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Value;
import org.apache.jdo.tck.pc.company.IAddress;
import org.apache.jdo.tck.pc.company.IPerson;
import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;
import org.apache.jdo.tck.util.JDOCustomDateEditor;

/** This class represents a person. */
@PersistenceCapable(identityType = IdentityType.APPLICATION, table = "persons")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(
    strategy = DiscriminatorStrategy.CLASS_NAME,
    column = "DISCRIMINATOR",
    indexed = "true")
public class PCAppPerson
    implements IPerson, Serializable, Comparable<IPerson>, Comparator<IPerson>, DeepEquality {

  private static final long serialVersionUID = 1L;

  @NotPersistent() private long _personid;
  @NotPersistent() private String _firstname;
  @NotPersistent() private String _lastname;
  @NotPersistent() private String _middlename;
  @NotPersistent() private Date _birthdate;
  @NotPersistent() private PCAppAddress _address;
  @NotPersistent() private Map<String, String> _phoneNumbers = new HashMap<>();
  @NotPersistent() private Set<String> _languages = new HashSet<>();

  /** This is the JDO-required no-args constructor. */
  protected PCAppPerson() {}

  /**
   * Construct a <code>PCAppPerson</code> instance.
   *
   * @param personid The person identifier.
   * @param firstname The person's first name.
   * @param lastname The person's last name.
   * @param middlename The person's middle name.
   * @param birthdate The person's birthdate.
   */
  public PCAppPerson(
      long personid, String firstname, String lastname, String middlename, Date birthdate) {
    this._personid = personid;
    this._firstname = firstname;
    this._lastname = lastname;
    this._middlename = middlename;
    this._birthdate = birthdate;
  }

  /**
   * Construct a <code>PCAppPerson</code> instance.
   *
   * @param personid The person identifier.
   * @param firstname The person's first name.
   * @param lastname The person's last name.
   * @param middlename The person's middle name.
   * @param birthdate The person's birthdate.
   * @param address The person's address.
   */
  public PCAppPerson(
      long personid,
      String firstname,
      String lastname,
      String middlename,
      Date birthdate,
      IAddress address) {
    this(personid, firstname, lastname, middlename, birthdate);
    this._address = (PCAppAddress) address;
  }

  /**
   * Set the id associated with this object.
   *
   * @param id the id.
   */
  public void setPersonid(long id) {
    this._personid = id;
  }

  /**
   * Get the person's id.
   *
   * @return The personid.
   */
  @PrimaryKey
  @Column(name = "PERSONID")
  public long getPersonid() {
    return _personid;
  }

  /**
   * Get the person's last name.
   *
   * @return The last name.
   */
  @Column(name = "LASTNAME")
  public String getLastname() {
    return _lastname;
  }

  /**
   * Set the person's last name.
   *
   * @param lastname The last name.
   */
  public void setLastname(String lastname) {
    this._lastname = lastname;
  }

  /**
   * Get the person's first name.
   *
   * @return The first name.
   */
  @Column(name = "FIRSTNAME")
  public String getFirstname() {
    return _firstname;
  }

  /**
   * Set the person's first name.
   *
   * @param firstname The first name.
   */
  public void setFirstname(String firstname) {
    this._firstname = firstname;
  }

  /**
   * Get the person's middle name.
   *
   * @return The middle name.
   */
  @Persistent(defaultFetchGroup = "false")
  @Column(name = "MIDDLENAME", allowsNull = "true")
  public String getMiddlename() {
    return _middlename;
  }

  /**
   * Set the person's middle name.
   *
   * @param middlename The middle name.
   */
  public void setMiddlename(String middlename) {
    this._middlename = middlename;
  }

  /**
   * Get the address.
   *
   * @return The address.
   */
  @Persistent(
      persistenceModifier = PersistenceModifier.PERSISTENT,
      types = org.apache.jdo.tck.pc.companyAnnotatedPC.PCAppAddress.class)
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
  public IAddress getAddress() {
    return _address;
  }

  /**
   * Set the address.
   *
   * @param address The address.
   */
  public void setAddress(IAddress address) {
    this._address = (PCAppAddress) address;
  }

  /**
   * Get the person's birthdate.
   *
   * @return The person's birthdate.
   */
  @Column(name = "BIRTHDATE")
  public Date getBirthdate() {
    return _birthdate;
  }

  /**
   * Set the person's birthdate.
   *
   * @param birthdate The person's birthdate.
   */
  public void setBirthdate(Date birthdate) {
    this._birthdate = birthdate;
  }

  /**
   * Get the map of phone numbers
   *
   * @return The map of phone numbers.
   */
  // maps phone number types ("home", "work", "mobile", etc.)
  // to phone numbers specified as String
  @Persistent(table = "employee_phoneno_type")
  @Join(column = "EMPID")
  @Key(types = java.lang.String.class, column = "TYPE")
  @Value(types = java.lang.String.class, column = "PHONENO")
  public Map<String, String> getPhoneNumbers() {
    return _phoneNumbers;
  }

  /**
   * Get the phone number for the specified phone number type.
   *
   * @param type The phone number type ("home", "work", "mobile", etc.).
   * @return The phone number associated with specified type, or <code>null</code> if there was no
   *     phone number for the type.
   */
  public String getPhoneNumber(String type) {
    return _phoneNumbers.get(type);
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
    return _phoneNumbers.put(type, phoneNumber);
  }

  /**
   * Remove a phoneNumber from the map of phone numbers.
   *
   * @param type The phone number type ("home", "work", "mobile", etc.).
   * @return The previous phone number associated with specified type, or <code>null</code> if there
   *     was no phone number for the type.
   */
  public String removePhoneNumber(String type) {
    return _phoneNumbers.remove(type);
  }

  /**
   * Set the phoneNumber map to be in this person.
   *
   * @param phoneNumbers The map of phoneNumbers for this person.
   */
  public void setPhoneNumbers(Map<String, String> phoneNumbers) {
    // workaround: create a new HashMap, because fostore does not
    // support LinkedHashMap
    this._phoneNumbers = (phoneNumbers != null) ? new HashMap<>(phoneNumbers) : null;
  }

  /**
   * Get the map of languages as an unmodifiable Set.
   *
   * @return The set of languages, as an unmodifiable set.
   */
  public Set<String> getLanguages() {
    return Collections.unmodifiableSet(_languages);
  }

  /**
   * Set the languages set to be in this person.
   *
   * @param languages The set of languages for this person.
   */
  public void setLanguages(Set<String> languages) {
    this._languages = new HashSet(languages);
  }

  /**
   * Returns a String representation of a <code>PCAppPerson</code> object.
   *
   * @return a string representation of a <code>PCAppPerson</code> object.
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
    rc.append(_personid);
    rc.append(", ").append(_lastname);
    rc.append(", ").append(_firstname);
    rc.append(", born ").append(JDOCustomDateEditor.getDateRepr(_birthdate));
    rc.append(", phone ").append(_phoneNumbers);
    return rc.toString();
  }

  /**
   * Returns <code>true</code> if all the fields of this instance are deep equal to the coresponding
   * fields of the specified PCAppPerson.
   *
   * @param other the object with which to compare.
   * @param helper EqualityHelper to keep track of instances that have already been processed.
   * @return <code>true</code> if all the fields are deep equal; <code>false</code> otherwise.
   * @throws ClassCastException if the specified instances' type prevents it from being compared to
   *     this instance.
   */
  public boolean deepCompareFields(Object other, EqualityHelper helper) {
    PCAppPerson otherPerson = (PCAppPerson) other;
    String where = "FCPerson<" + _personid + ">";
    return helper.equals(_personid, otherPerson.getPersonid(), where + ".personid")
        & helper.equals(_firstname, otherPerson.getFirstname(), where + ".firstname")
        & helper.equals(_lastname, otherPerson.getLastname(), where + ".lastname")
        & helper.equals(_middlename, otherPerson.getMiddlename(), where + ".middlename")
        & helper.equals(_birthdate, otherPerson.getBirthdate(), where + ".birthdate")
        & helper.deepEquals(_address, otherPerson.getAddress(), where + ".address")
        & helper.deepEquals(_phoneNumbers, otherPerson.getPhoneNumbers(), where + ".phoneNumbers");
  }

  /**
   * Compares this object with the specified PCAppPerson object for order. Returns a negative
   * integer, zero, or a positive integer as this object is less than, equal to, or greater than the
   * specified object.
   *
   * @param other The PCAppPerson object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified PFCAppPerson object.
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
    if (obj instanceof PCAppPerson) {
      return compareTo((PCAppPerson) obj) == 0;
    }
    return false;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for this object.
   */
  public int hashCode() {
    return (int) _personid;
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
