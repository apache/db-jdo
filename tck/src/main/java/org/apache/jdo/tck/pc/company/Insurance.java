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

import java.io.Serializable;

import java.util.Comparator;

import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

import javax.jdo.annotations.PersistenceCapable;

/**
 * This class represents an insurance carrier selection for a particular
 * <code>Employee</code>.
 */
@PersistenceCapable
public abstract class Insurance
    implements IInsurance, Serializable, Comparable, Comparator, DeepEquality  {

    private long     insid;
    private String   carrier;
    private Employee employee;

    /** This is the JDO-required no-args constructor. */
    protected Insurance() {}

    /**
     * Construct an <code>Insurance</code> instance.
     * @param insid The insurance instance identifier.
     * @param carrier The insurance carrier.
     */
    protected Insurance(long insid, String carrier) {
        this.insid = insid;
        this.carrier = carrier;
    }

    /**
     * Construct an <code>Insurance</code> instance.
     * @param insid The insurance instance identifier.
     * @param carrier The insurance carrier.
     * @param employee The employee associated with this insurance. 
     */
    protected Insurance(long insid, String carrier, IEmployee employee) {
        this.insid = insid;
        this.carrier = carrier;
        this.employee = (Employee)employee;
    }

    /**
     * Get the insurance ID.
     * @return the insurance ID.
     */
    public long getInsid() {
        return insid;
    }

    /**
     * Set the insurance ID.
     * @param id The insurance ID value.
     */
    public void setInsid(long id) {
        if (this.insid != 0) 
            throw new IllegalStateException("Id is already set.");
        this.insid = id;
    }

    /**
     * Get the insurance carrier.
     * @return The insurance carrier.
     */
    public String getCarrier() {
        return carrier;
    }

    /**
     * Set the insurance carrier.
     * @param carrier The insurance carrier.
     */
    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }
    
    /**
     * Get the associated employee.
     * @return The employee for this insurance.
     */
    public IEmployee getEmployee() {
        return employee;
    }

    /**
     * Set the associated employee.
     * @param employee The associated employee.
     */
    public void setEmployee(IEmployee employee) {
        this.employee = (Employee)employee;
    }

    /**
     * Returns a String representation of a <code>Insurance</code> object.
     * @return a String representation of a <code>Insurance</code> object.
     */
    public String toString() {
        return "Insurance(" + getFieldRepr() + ")";
    }

    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    protected String getFieldRepr() {
        StringBuffer rc = new StringBuffer();
        rc.append(insid);
        rc.append(", carrier ").append(carrier);
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
        IInsurance otherIns = (IInsurance)other;
        String where = "Insurance<" + insid + ">";
        return
            helper.equals(insid, otherIns.getInsid(), where + ".insid") &
            helper.equals(carrier, otherIns.getCarrier(), where + ".carrier") &
            helper.deepEquals(employee, otherIns.getEmployee(), where + ".employee");
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
        return compareTo((IInsurance)o);
    }

    /** 
     * Compare two instances. This is a method in Comparator.
     */
    public int compare(Object o1, Object o2) {
        return compare((IInsurance)o1, (IInsurance)o2);
    }

    /** 
     * Compares this object with the specified Insurance object for
     * order. Returns a negative integer, zero, or a positive integer as
     * this object is less than, equal to, or greater than the specified
     * object.  
     * @param other The Insurance object to be compared. 
     * @return a negative integer, zero, or a positive integer as this
     * object is less than, equal to, or greater than the specified
     * Insurance object. 
     */
    public int compareTo(IInsurance other) {
        return compare(this, other);
    }

    /**
     * Compares its two IInsurance arguments for order. Returns a negative
     * integer, zero, or a positive integer as the first argument is less
     * than, equal to, or greater than the second. 
     * @param o1 the first IInsurance object to be compared. 
     * @param o2 the second IInsurance object to be compared. 
     * @return a negative integer, zero, or a positive integer as the first
     * object is less than, equal to, or greater than the second object. 
     */
    public static int compare(IInsurance o1, IInsurance o2) {
        return EqualityHelper.compare(o1.getInsid(), o2.getInsid());
    }
    
    /** 
     * Indicates whether some other object is "equal to" this one.
     * @param obj the object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     * argument; <code>false</code> otherwise. 
     */
    public boolean equals(Object obj) {
        if (obj instanceof IInsurance) {
            return compareTo((IInsurance)obj) == 0;
        }
        return false;
    }
        
    /**
     * Returns a hash code value for the object. 
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return (int)insid;
    }

    /**
     * This class is used to represent the application
     * identifier for the <code>Insurance</code> class.
     */
    public static class Oid implements Serializable, Comparable 
    {
        /**
         * This field represents the application identifier for the
         * <code>Insurance</code> class. It must match the field in the
         * <code>Insurance</code> class in both name and type. 
         */
        public long insid;
        
        /**
         * The required public no-args constructor.
         */
        public Oid() { }

        /**
         * Initialize with an insurance identifier.
         * @param insid the insurance ID.
         */
        public Oid(long insid) {
            this.insid = insid;
        }
        
        public Oid(String s) { insid = Long.parseLong(justTheId(s)); }

        public String toString() { return this.getClass().getName() + ": "  + insid;}


        /** */
        public boolean equals(java.lang.Object obj) {
            if( obj==null || !this.getClass().equals(obj.getClass()) )
                return( false );
            Oid o=(Oid) obj;
            if( this.insid!=o.insid ) return( false );
            return( true );
        }

        /** */
        public int hashCode() {
            return( (int) insid );
        }
        
        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }

        /** */
        public int compareTo(Object obj) {
            // may throw ClassCastException which the user must handle
            Oid other = (Oid) obj;
            if( insid < other.insid ) return -1;
            if( insid > other.insid ) return 1;
            return 0;
        }

    }

}

