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

package org.apache.jdo.tck.pc.company;

import java.math.BigDecimal;
import javax.jdo.annotations.PersistenceCapable;
import org.apache.jdo.tck.util.EqualityHelper;

/**
 * This class represents a dental insurance carrier selection for a particular <code>Employee</code>
 * .
 */
@PersistenceCapable
public class DentalInsurance extends Insurance implements IDentalInsurance {

  private static final long serialVersionUID = 1L;

  private BigDecimal lifetimeOrthoBenefit;

  /**
   * This is the JDO-required no-args constructor. The TCK relies on this constructor for testing
   * PersistenceManager.newInstance(PCClass).
   */
  public DentalInsurance() {}

  /**
   * Construct a <code>DentalInsurance</code> instance.
   *
   * @param insid The insurance instance identifier.
   * @param carrier The insurance carrier.
   * @param lifetimeOrthoBenefit The lifetimeOrthoBenefit.
   */
  public DentalInsurance(long insid, String carrier, BigDecimal lifetimeOrthoBenefit) {
    super(insid, carrier);
    this.lifetimeOrthoBenefit = lifetimeOrthoBenefit;
  }

  /**
   * Construct a <code>DentalInsurance</code> instance.
   *
   * @param insid The insurance instance identifier.
   * @param carrier The insurance carrier.
   * @param employee The employee associated with this insurance.
   * @param lifetimeOrthoBenefit The lifetimeOrthoBenefit.
   */
  public DentalInsurance(
      long insid, String carrier, IEmployee employee, BigDecimal lifetimeOrthoBenefit) {
    super(insid, carrier, employee);
    this.lifetimeOrthoBenefit = lifetimeOrthoBenefit;
  }

  /**
   * Get the insurance lifetimeOrthoBenefit.
   *
   * @return The insurance lifetimeOrthoBenefit.
   */
  public BigDecimal getLifetimeOrthoBenefit() {
    return lifetimeOrthoBenefit;
  }

  /**
   * Set the insurance lifetimeOrthoBenefit.
   *
   * @param lifetimeOrthoBenefit The insurance lifetimeOrthoBenefit.
   */
  public void setLifetimeOrthoBenefit(BigDecimal lifetimeOrthoBenefit) {
    this.lifetimeOrthoBenefit = lifetimeOrthoBenefit;
  }

  /**
   * Returns a String representation of a <code>DentalInsurance</code> object.
   *
   * @return a String representation of a <code>DentalInsurance</code> object.
   */
  @Override
  public String toString() {
    return "DentalInsurance(" + getFieldRepr() + ")";
  }

  /**
   * Returns a String representation of the non-relationship fields.
   *
   * @return a String representation of the non-relationship fields.
   */
  @Override
  protected String getFieldRepr() {
    StringBuilder rc = new StringBuilder();
    rc.append(super.getFieldRepr());
    rc.append(", lifetimeOrthoBenefit ").append(lifetimeOrthoBenefit);
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
  @Override
  public boolean deepCompareFields(Object other, EqualityHelper helper) {
    IDentalInsurance otherIns = (IDentalInsurance) other;
    String where = "DentalInsurance<" + getInsid() + ">";
    return super.deepCompareFields(otherIns, helper)
        & helper.equals(
            lifetimeOrthoBenefit,
            otherIns.getLifetimeOrthoBenefit(),
            where + ".lifetimeOrthoBenefit");
  }
}
