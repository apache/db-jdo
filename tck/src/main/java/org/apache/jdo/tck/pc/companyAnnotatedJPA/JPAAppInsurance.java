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

import java.io.Serializable;
import java.util.Comparator;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import org.apache.jdo.tck.pc.company.IEmployee;
import org.apache.jdo.tck.pc.company.IInsurance;
import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

/**
 * This class represents an insurance carrier selection for a particular <code>JPAAppEmployee</code>
 * .
 */
@Entity
@Table(name = "insuranceplans")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "DISCRIMINATOR")
public class JPAAppInsurance
    implements IInsurance,
        Serializable,
        Comparable<IInsurance>,
        Comparator<IInsurance>,
        DeepEquality {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "INSID")
  private long insid;

  @Column(name = "CARRIER")
  private String carrier;

  @Column(name = "EMPLOYEE")
  private JPAAppEmployee employee;

  /** This is the JDO-required no-args constructor. */
  protected JPAAppInsurance() {}

  /**
   * Construct an <code>JPAAppInsurance</code> instance.
   *
   * @param insid The insurance instance identifier.
   * @param carrier The insurance carrier.
   */
  protected JPAAppInsurance(long insid, String carrier) {
    this.insid = insid;
    this.carrier = carrier;
  }

  /**
   * Construct an <code>JPAAppInsurance</code> instance.
   *
   * @param insid The insurance instance identifier.
   * @param carrier The insurance carrier.
   * @param employee The employee associated with this insurance.
   */
  protected JPAAppInsurance(long insid, String carrier, IEmployee employee) {
    this.insid = insid;
    this.carrier = carrier;
    this.employee = (JPAAppEmployee) employee;
  }

  /**
   * Get the insurance ID.
   *
   * @return the insurance ID.
   */
  public long getInsid() {
    return insid;
  }

  /**
   * Set the insurance ID.
   *
   * @param id The insurance ID value.
   */
  public void setInsid(long id) {
    if (this.insid != 0) throw new IllegalStateException("Id is already set.");
    this.insid = id;
  }

  /**
   * Get the insurance carrier.
   *
   * @return The insurance carrier.
   */
  public String getCarrier() {
    return carrier;
  }

  /**
   * Set the insurance carrier.
   *
   * @param carrier The insurance carrier.
   */
  public void setCarrier(String carrier) {
    this.carrier = carrier;
  }

  /**
   * Get the associated employee.
   *
   * @return The employee for this insurance.
   */
  public IEmployee getEmployee() {
    return employee;
  }

  /**
   * Set the associated employee.
   *
   * @param employee The associated employee.
   */
  public void setEmployee(IEmployee employee) {
    this.employee = (JPAAppEmployee) employee;
  }

  /**
   * Returns a String representation of a <code>JPAAppInsurance</code> object.
   *
   * @return a String representation of a <code>JPAAppInsurance</code> object.
   */
  public String toString() {
    return "JPAAppInsurance(" + getFieldRepr() + ")";
  }

  /**
   * Returns a String representation of the non-relationship fields.
   *
   * @return a String representation of the non-relationship fields.
   */
  protected String getFieldRepr() {
    StringBuilder rc = new StringBuilder();
    rc.append(insid);
    rc.append(", carrier ").append(carrier);
    return rc.toString();
  }

  /**
   * Returns <code>true</code> if all the fields of this instance are deep equal to the coresponding
   * fields of the other Object.
   *
   * @param other the object with which to compare.
   * @param helper EqualityHelper to keep track of instances that have already been processed.
   * @return <code>true</code> if all the fields are deep equal; <code>false</code> otherwise.
   * @throws ClassCastException if the specified instances' type prevents it from being compared to
   *     this instance.
   */
  public boolean deepCompareFields(Object other, EqualityHelper helper) {
    JPAAppInsurance otherIns = (JPAAppInsurance) other;
    String where = "JPAAppInsurance<" + insid + ">";
    return helper.equals(insid, otherIns.getInsid(), where + ".insid")
        & helper.equals(carrier, otherIns.getCarrier(), where + ".carrier")
        & helper.deepEquals(employee, otherIns.getEmployee(), where + ".employee");
  }

  /**
   * Compares this object with the specified Insurance object for order. Returns a negative integer,
   * zero, or a positive integer as this object is less than, equal to, or greater than the
   * specified object.
   *
   * @param other The Insurance object to be compared.
   * @return a negative integer, zero, or a positive integer as this object is less than, equal to,
   *     or greater than the specified Insurance object.
   */
  public int compareTo(IInsurance other) {
    return compare(this, other);
  }

  /**
   * Compares its two IInsurance arguments for order. Returns a negative integer, zero, or a
   * positive integer as the first argument is less than, equal to, or greater than the second.
   *
   * @param o1 the first IInsurance object to be compared.
   * @param o2 the second IInsurance object to be compared.
   * @return a negative integer, zero, or a positive integer as the first object is less than, equal
   *     to, or greater than the second object.
   */
  public int compare(IInsurance o1, IInsurance o2) {
    return EqualityHelper.compare(o1.getInsid(), o2.getInsid());
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the object with which to compare.
   * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>
   *     otherwise.
   */
  public boolean equals(Object obj) {
    if (obj instanceof JPAAppInsurance) {
      return compareTo((JPAAppInsurance) obj) == 0;
    }
    return false;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for this object.
   */
  public int hashCode() {
    return (int) insid;
  }

  /**
   * This class is used to represent the application identifier for the <code>Insurance</code>
   * class.
   */
  public static class Oid implements Serializable, Comparable<Oid> {

    private static final long serialVersionUID = 1L;

    /**
     * This field represents the application identifier for the <code>Insurance</code> class. It
     * must match the field in the <code>Insurance</code> class in both name and type.
     */
    public long insid;

    /** The required public no-args constructor. */
    public Oid() {}

    /**
     * Initialize with an insurance identifier.
     *
     * @param insid the insurance ID.
     */
    public Oid(long insid) {
      this.insid = insid;
    }

    public Oid(String s) {
      insid = Long.parseLong(justTheId(s));
    }

    public String toString() {
      return this.getClass().getName() + ": " + insid;
    }

    /** */
    public boolean equals(java.lang.Object obj) {
      if (obj == null || !this.getClass().equals(obj.getClass())) return (false);
      Oid o = (Oid) obj;
      if (this.insid != o.insid) return (false);
      return (true);
    }

    /** */
    public int hashCode() {
      return ((int) insid);
    }

    protected static String justTheId(String str) {
      return str.substring(str.indexOf(':') + 1);
    }

    /** */
    public int compareTo(Oid obj) {
      return Long.compare(insid, obj.insid);
    }
  }
}
