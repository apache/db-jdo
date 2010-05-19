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
 
package org.apache.jdo.tck.pc.companyAnnotatedFC;

import javax.jdo.annotations.*;
import org.apache.jdo.tck.pc.company.IMedicalInsurance;
import org.apache.jdo.tck.util.EqualityHelper;

/**
 * This class represents a dental insurance carrier selection for a
 * particular <code>Employee</code>.
 */
@PersistenceCapable
@DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="DATASTORE_IDENTITY")
public class FCDSMedicalInsurance extends FCDSInsurance
        implements IMedicalInsurance {

    @Column(name="PLANTYPE")
    private String planType; // possible values: "PPO", "EPO", "NPO" 

    /** This is the JDO-required no-args constructor. The TCK relies on
     * this constructor for testing PersistenceManager.newInstance(PCClass).
     */
    public FCDSMedicalInsurance() {}

    /**
     * Construct a <code>FCDSMedicalInsurance</code> instance.
     * 
     * @param insid The insurance instance identifier.
     * @param carrier The insurance carrier.
     * @param planType The planType.
     */
    public FCDSMedicalInsurance(long insid, String carrier, 
                            String planType)
    {
        super(insid, carrier);
        this.planType = planType;
    }

    /**
     * Construct a <code>FCDSMedicalInsurance</code> instance.
     * 
     * @param insid The insurance instance identifier.
     * @param carrier The insurance carrier.
     * @param employee The employee associated with this insurance.
     * @param planType The planType.
     */
    public FCDSMedicalInsurance(long insid, String carrier, 
                            FCDSEmployee employee, String planType)
    {
        super(insid, carrier, employee);
        this.planType = planType;
    }

    /**
     * Get the insurance planType.
     * @return The insurance planType.
     */
    public String getPlanType() {
        return planType;
    }

    /**
     * Set the insurance planType.
     * @param planType The insurance planType.
     */
    public void setPlanType(String planType) {
        this.planType = planType;
    }

    /**
     * Returns a String representation of a <code>FCDSMedicalInsurance</code>
     * object.
     * 
     * @return a String representation of a <code>FCDSMedicalInsurance</code>
     * object.
     */
    public String toString() {
        return "FCMedicalInsurance(" + getFieldRepr() + ")";
    }

    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    protected String getFieldRepr() {
        StringBuffer rc = new StringBuffer();
        rc.append(super.getFieldRepr());
        rc.append(", planType ").append(planType);
        return rc.toString();
    }

    /** 
     * Returns <code>true</code> if all the fields of this instance are
     * deep equal to the coresponding fields of the other Object.
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
        FCDSMedicalInsurance otherIns = (FCDSMedicalInsurance)other;
        String where = "FCMedicalInsurance<" + getInsid() + ">";
        return super.deepCompareFields(otherIns, helper) &
            helper.equals(planType, otherIns.getPlanType(), where + ".planType");
    }
}

