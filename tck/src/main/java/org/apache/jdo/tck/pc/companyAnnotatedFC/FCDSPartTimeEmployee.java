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

package org.apache.jdo.tck.pc.companyAnnotatedFC;

import javax.jdo.annotations.*;

import java.util.Date;
import org.apache.jdo.tck.pc.company.IAddress;
import org.apache.jdo.tck.pc.company.IPartTimeEmployee;
import org.apache.jdo.tck.util.EqualityHelper;

/** This class represents a part-time employee. */
@DatastoreIdDiscriminatorClassNameInheritanceSuperclass
@PersistenceCapable(table = "persons")
public class FCDSPartTimeEmployee extends FCDSEmployee implements IPartTimeEmployee {

  @Column(name = "WAGE")
  private double wage;

  /**
   * This is the JDO-required no-args constructor. The TCK relies on this constructor for testing
   * PersistenceManager.newInstance(PCClass).
   */
  public FCDSPartTimeEmployee() {}

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
  public FCDSPartTimeEmployee(
      long personid, String first, String last, String middle, Date born, Date hired, double wage) {
    super(personid, first, last, middle, born, hired);
    this.wage = wage;
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
  public FCDSPartTimeEmployee(
      long personid,
      String first,
      String last,
      String middle,
      Date born,
      IAddress addr,
      Date hired,
      double wage) {
    super(personid, first, last, middle, born, (FCDSAddress) addr, hired);
    this.wage = wage;
  }

  /**
   * Get the wage of the part-time employee.
   *
   * @return The wage of the part-time employee.
   */
  public double getWage() {
    return wage;
  }

  /**
   * Set the wage of the part-time employee.
   *
   * @param wage The wage of the part-time employee.
   */
  public void setWage(double wage) {
    this.wage = wage;
  }

  /**
   * Returns a String representation of a <code>FCDSPartTimeEmployee</code> object.
   *
   * @return a String representation of a <code>FCDSPartTimeEmployee</code> object.
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
    rc.append(", $" + wage);
    return rc.toString();
  }

  /**
   * Returns <code>true</code> if all the fields of this instance are deep equal to the coresponding
   * fields of the specified FCDSPartTimeEmployee.
   *
   * @param other the object with which to compare.
   * @param helper EqualityHelper to keep track of instances that have already been processed.
   * @return <code>true</code> if all the fields are deep equal; <code>false</code> otherwise.
   * @throws ClassCastException if the specified instances' type prevents it from being compared to
   *     this instance.
   */
  public boolean deepCompareFields(Object other, EqualityHelper helper) {
    FCDSPartTimeEmployee otherEmp = (FCDSPartTimeEmployee) other;
    String where = "FCPartTimeEmployee<" + getPersonid() + ">";
    return super.deepCompareFields(otherEmp, helper)
        & helper.closeEnough(wage, otherEmp.getWage(), where + ".wage");
  }
}
