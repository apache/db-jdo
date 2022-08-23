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

import javax.jdo.annotations.*;

import java.util.Date;
import org.apache.jdo.tck.pc.company.IAddress;
import org.apache.jdo.tck.pc.company.IPartTimeEmployee;
import org.apache.jdo.tck.util.EqualityHelper;

/** This class represents a part-time employee. */
@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "DATASTORE_IDENTITY")
public class PCDSPartTimeEmployee extends PCDSEmployee implements IPartTimeEmployee {

  @NotPersistent() private double _wage;

  /**
   * This is the JDO-required no-args constructor. The TCK relies on this constructor for testing
   * PersistenceManager.newInstance(PCClass).
   */
  public PCDSPartTimeEmployee() {}

  /**
   * Construct a part-time employee.
   *
   * @param personid The identifier for the person.
   * @param first The person's first name.
   * @param last The person's last name.
   * @param middle The person's middle name.
   * @param born The person's birthdate.
   * @param hired The date the person was hired.
   * @param wage The person's wage.
   */
  public PCDSPartTimeEmployee(
      long personid, String first, String last, String middle, Date born, Date hired, double wage) {
    super(personid, first, last, middle, born, hired);
    this._wage = wage;
  }

  /**
   * Construct a part-time employee.
   *
   * @param personid The identifier for the person.
   * @param first The person's first name.
   * @param last The person's last name.
   * @param middle The person's middle name.
   * @param born The person's birthdate.
   * @param addr The person's address.
   * @param hired The date the person was hired.
   * @param wage The person's wage.
   */
  public PCDSPartTimeEmployee(
      long personid,
      String first,
      String last,
      String middle,
      Date born,
      IAddress addr,
      Date hired,
      double wage) {
    super(personid, first, last, middle, born, (PCDSAddress) addr, hired);
    this._wage = wage;
  }

  /**
   * Get the wage of the part-time employee.
   *
   * @return The wage of the part-time employee.
   */
  @Column(name = "WAGE")
  public double getWage() {
    return _wage;
  }

  /**
   * Set the wage of the part-time employee.
   *
   * @param wage The wage of the part-time employee.
   */
  public void setWage(double wage) {
    this._wage = wage;
  }

  /**
   * Returns a String representation of a <code>PCDSPartTimeEmployee</code> object.
   *
   * @return a String representation of a <code>PCDSPartTimeEmployee</code> object.
   */
  public String toString() {
    return "FCPartTimeEmployee(" + getFieldRepr() + ")";
  }

  /**
   * Returns a String representation of the non-relationship fields.
   *
   * @return a String representation of the non-relationship fields.
   */
  public String getFieldRepr() {
    StringBuffer rc = new StringBuffer();
    rc.append(super.getFieldRepr());
    rc.append(", $" + _wage);
    return rc.toString();
  }

  /**
   * Returns <code>true</code> if all the fields of this instance are deep equal to the coresponding
   * fields of the specified PCDSPartTimeEmployee.
   *
   * @param other the object with which to compare.
   * @param helper EqualityHelper to keep track of instances that have already been processed.
   * @return <code>true</code> if all the fields are deep equal; <code>false</code> otherwise.
   * @throws ClassCastException if the specified instances' type prevents it from being compared to
   *     this instance.
   */
  public boolean deepCompareFields(Object other, EqualityHelper helper) {
    PCDSPartTimeEmployee otherEmp = (PCDSPartTimeEmployee) other;
    String where = "FCPartTimeEmployee<" + getPersonid() + ">";
    return super.deepCompareFields(otherEmp, helper)
        & helper.closeEnough(_wage, otherEmp.getWage(), where + ".wage");
  }
}
