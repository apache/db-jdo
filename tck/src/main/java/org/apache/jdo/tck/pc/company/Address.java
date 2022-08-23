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

package org.apache.jdo.tck.pc.company;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

/** This class represents a postal address. */
public class Address implements IAddress, Serializable, Comparable, Comparator, DeepEquality {

  private long addrid;
  private String street;
  private String city;
  private String state;
  private String zipcode;
  private String country;

  /**
   * This is the JDO-required no-args constructor. The TCK relies on this constructor for testing
   * PersistenceManager.newInstance(PCClass).
   */
  public Address() {}

  /**
   * This constructor initializes the <code>Address</code> components.
   *
   * @param addrid The address ID.
   * @param street The street address.
   * @param city The city.
   * @param state The state.
   * @param zipcode The zip code.
   * @param country The zip country.
   */
  public Address(
      long addrid, String street, String city, String state, String zipcode, String country) {
    this.addrid = addrid;
    this.street = street;
    this.city = city;
    this.state = state;
    this.zipcode = zipcode;
    this.country = country;
  }

  /**
   * Get the addrid associated with this object.
   *
   * @return the addrid.
   */
  public long getAddrid() {
    return addrid;
  }

  /**
   * Set the id associated with this object.
   *
   * @param id the id.
   */
  public void setAddrid(long id) {
    if (this.addrid != 0) throw new IllegalStateException("Id is already set.");
    this.addrid = id;
  }

  /**
   * Get the street component of the address.
   *
   * @return The street component of the address.
   */
  public String getStreet() {
    return street;
  }

  /**
   * Set the street component of the address.
   *
   * @param street The street component.
   */
  public void setStreet(String street) {
    this.street = street;
  }

  /**
   * Get the city.
   *
   * @return The city component of the address.
   */
  public String getCity() {
    return city;
  }

  /**
   * Set the city component of the address.
   *
   * @param city The city.
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Get the state component of the address.
   *
   * @return The state.
   */
  public String getState() {
    return state;
  }

  /**
   * Set the state component of the address.
   *
   * @param state The state.
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Get the zipcode component of the address.
   *
   * @return The zipcode.
   */
  public String getZipcode() {
    return zipcode;
  }

  /**
   * Set the zip code component of the address.
   *
   * @param zipcode The zipcode.
   */
  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  /**
   * Get the country component of the address.
   *
   * @return The country.
   */
  public String getCountry() {
    return country;
  }

  /**
   * Set the country component of the address.
   *
   * @param country The country.
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Returns a String representation of a <code>Address</code> object.
   *
   * @return a String representation of a <code>Address</code> object.
   */
  public String toString() {
    return "Address(" + getFieldRepr() + ")";
  }

  /**
   * Returns a String representation of the non-relationship fields.
   *
   * @return a String representation of the non-relationship fields.
   */
  protected String getFieldRepr() {
    StringBuffer rc = new StringBuffer();
    rc.append(addrid);
    rc.append(", street ").append(street);
    rc.append(", city ").append(city);
    rc.append(", state ").append(state);
    rc.append(", zipcode ").append(zipcode);
    rc.append(", country ").append(country);
    return rc.toString();
  }

  /**
   * Returns <code>true</code> if all the fields of this instance are deep equal to the coresponding
   * fields of the specified Person.
   *
   * @param other the object with which to compare.
   * @param helper EqualityHelper to keep track of instances that have already been processed.
   * @return <code>true</code> if all the fields are deep equal; <code>false</code> otherwise.
   * @throws ClassCastException if the specified instances' type prevents it from being compared to
   *     this instance.
   */
  public boolean deepCompareFields(Object other, EqualityHelper helper) {
    IAddress otherAddress = (IAddress) other;
    String where = "Address<" + addrid + ">";
    return helper.equals(addrid, otherAddress.getAddrid(), where + ".addrid")
        & helper.equals(street, otherAddress.getStreet(), where + ".street")
        & helper.equals(city, otherAddress.getCity(), where + ".city")
        & helper.equals(state, otherAddress.getState(), where + ".state")
        & helper.equals(zipcode, otherAddress.getZipcode(), where + ".zipcode")
        & helper.equals(country, otherAddress.getCountry(), where + ".country");
  }

  /**
   * Compares this object with the specified object for order. Returns a negative integer, zero, or
   * a positive integer as this object is less than, equal to, or greater than the specified object.
   *
   * @param o The Object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified object.
   * @throws ClassCastException - if the specified object's type prevents it from being compared to
   *     this Object.
   */
  public int compareTo(Object o) {
    return compareTo((IAddress) o);
  }

  /** Compare two instances. This is a method in Comparator. */
  public int compare(Object o1, Object o2) {
    return compare((IAddress) o1, (IAddress) o2);
  }

  /**
   * Compares this object with the specified Address object for order. Returns a negative integer,
   * zero, or a positive integer as this object is less than, equal to, or greater than the
   * specified object.
   *
   * @param other The Address object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified Address object.
   */
  public int compareTo(IAddress other) {
    return compare(this, other);
  }

  /**
   * Compares its two IAddress arguments for order. Returns a negative integer, zero, or a positive
   * integer as the first argument is less than, equal to, or greater than the second.
   *
   * @param o1 the first IAddress object to be compared.
   * @param o2 the second IAddress object to be compared.
   * @return a negative integer, zero, or a positive integer as the first object is less than, equal
   *     to, or greater than the second object.
   */
  public static int compare(IAddress o1, IAddress o2) {
    return EqualityHelper.compare(o1.getAddrid(), o2.getAddrid());
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>
   *     otherwise.
   */
  public boolean equals(Object obj) {
    if (obj instanceof IAddress) {
      return compareTo((IAddress) obj) == 0;
    }
    return false;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for this object.
   */
  public int hashCode() {
    return (int) addrid;
  }

  /**
   * This class is used to represent the application identifier for the <code>Address</code> class.
   */
  public static class Oid implements Serializable, Comparable {

    /**
     * This is the identifier field for <code>Address</code> and must correspond in type and name to
     * the field in <code>Address</code>.
     */
    public long addrid;

    /** The required public, no-arg constructor. */
    public Oid() {
      addrid = 0;
    }

    /**
     * A constructor to initialize the identifier field.
     *
     * @param addrid the id of the Address.
     */
    public Oid(long addrid) {
      this.addrid = addrid;
    }

    public Oid(String s) {
      addrid = Long.parseLong(justTheId(s));
    }

    public String toString() {
      return this.getClass().getName() + ": " + addrid;
    }

    /** */
    public boolean equals(java.lang.Object obj) {
      if (obj == null || !this.getClass().equals(obj.getClass())) return (false);
      Oid o = (Oid) obj;
      if (this.addrid != o.addrid) return (false);
      return (true);
    }

    /** */
    public int hashCode() {
      return ((int) addrid);
    }

    protected static String justTheId(String str) {
      return str.substring(str.indexOf(':') + 1);
    }

    /** */
    public int compareTo(Object obj) {
      // may throw ClassCastException which the user must handle
      Oid other = (Oid) obj;
      if (addrid < other.addrid) return -1;
      if (addrid > other.addrid) return 1;
      return 0;
    }
  }
}
