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

package org.apache.jdo.tck.pc.companyMapWithoutJoin;

import java.util.Date;

import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

/** This class represents a full-time employee. */
public class FullTimeEmployee extends Employee implements IFullTimeEmployee {

  private double salary;

  /**
   * This is the JDO-required no-args constructor. The TCK relies on this constructor for testing
   * PersistenceManager.newInstance(PCClass).
   */
  public FullTimeEmployee() {}

  /**
   * Construct a full-time employee.
   *
   * @param personid The person identifier.
   * @param first The person's first name.
   * @param last The person's last name.
   * @param middle The person's middle name.
   * @param born The person's birthdate.
   * @param hired The date that the person was hired.
   * @param role The person's role
   * @param sal The salary of the full-time employee.
   */
  public FullTimeEmployee(
      long personid,
      String first,
      String last,
      String middle,
      Date born,
      Date hired,
      String role,
      double sal) {
    super(personid, first, last, middle, born, hired, role);
    salary = sal;
  }

  /**
   * Get the salary of the full time employee.
   *
   * @return The salary of the full time employee.
   */
  public double getSalary() {
    return salary;
  }

  /**
   * Set the salary for the full-time employee.
   *
   * @param salary The salary to set for the full-time employee.
   */
  public void setSalary(double salary) {
    this.salary = salary;
  }

  /**
   * Return a String representation of a <code>FullTimeEmployee</code> object.
   *
   * @return a String representation of a <code>FullTimeEmployee</code> object.
   */
  public String toString() {
    return "FullTimeEmployee(" + getFieldRepr() + ")";
  }

  /**
   * Returns a String representation of the non-relationship fields.
   *
   * @return a String representation of the non-relationship fields.
   */
  public String getFieldRepr() {
    StringBuffer rc = new StringBuffer();
    rc.append(super.getFieldRepr());
    rc.append(", $").append(salary);
    return rc.toString();
  }

  /**
   * Returns <code>true</code> if all the fields of this instance are deep equal to the coresponding
   * fields of the specified FullTimeEmployee.
   *
   * @param other the object with which to compare.
   * @param helper EqualityHelper to keep track of instances that have already been processed.
   * @return <code>true</code> if all the fields are deep equal; <code>false</code> otherwise.
   * @throws ClassCastException if the specified instances' type prevents it from being compared to
   *     this instance.
   */
  public boolean deepCompareFields(Object other, EqualityHelper helper) {
    IFullTimeEmployee otherEmp = (IFullTimeEmployee) other;
    String where = "FullTimeEmployee<" + getPersonid() + ">";
    return super.deepCompareFields(otherEmp, helper)
        & helper.closeEnough(salary, otherEmp.getSalary(), where + ".salary");
  }
}
