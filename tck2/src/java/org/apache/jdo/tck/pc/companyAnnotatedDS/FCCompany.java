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
 
package org.apache.jdo.tck.pc.companyAnnotatedDS;

import javax.jdo.annotations.*;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;

//import org.apache.jdo.tck.pc.company.*;
import org.apache.jdo.tck.pc.company.ICompany;
import org.apache.jdo.tck.pc.company.IAddress;
import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

/**
 * This class represents information about a company.
 */
@PersistenceCapable
@Implements ("org.apache.jdo.tck.pc.company.ICompany")
@Table(table="companies")
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy=DiscriminatorStrategy.CLASS_NAME,
        column="DISCRIMINATOR")
@DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="DATASTORE_IDENTITY")
public class FCCompany 
    implements ICompany, Serializable, Comparable, Comparator, DeepEquality {

    @Column(name="ID")
    private long        companyid;
    @Column(name="NAME", jdbcType="VARCHAR")
    private String      name;
    @Column(name="FOUNDEDDATE")
    private Date        founded;
    @Field(persistenceModifier=FieldPersistenceModifier.PERSISTENT)
    @Embedded(nullIndicatorColumn="COUNTRY",
        fields={
            @Field(embeddedFieldName="addrid", columns=@Column(name="ADDRID")),
            @Field(embeddedFieldName="street", columns=@Column(name="STREET")),
            @Field(embeddedFieldName="city", columns=@Column(name="CITY")),
            @Field(embeddedFieldName="state", columns=@Column(name="STATE")),
            @Field(embeddedFieldName="zipcode", columns=@Column(name="ZIPCODE")),
            @Field(embeddedFieldName="country", columns=@Column(name="COUNTRY"))
    })
    private FCAddress     address;
    @Field(persistenceModifier=FieldPersistenceModifier.PERSISTENT,
            mappedBy="company")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedDS.FCDepartment.class)
    private transient Set departments = new HashSet();

    protected static SimpleDateFormat formatter =
        new SimpleDateFormat("d/MMM/yyyy");

    /** This is the JDO-required no-args constructor. The TCK relies on
     * this constructor for testing PersistenceManager.newInstance(PCClass).
     */
    public FCCompany() {}

    /** 
     * Initialize the <code>FCCompany</code> instance.
     * @param companyid The company id.
     * @param name The company name.
     * @param founded The date the company was founded.
     */
    public FCCompany(long companyid, String name, Date founded) {
        this.companyid = companyid;
        this.name = name;
        this.founded = founded;
    }

    /** 
     * Initialize the <code>Company</code> instance.
     * @param companyid The company id.
     * @param name The company name.
     * @param founded The date the company was founded.
     * @param addr The company's address.
     */
    public FCCompany(long companyid, String name, Date founded, IAddress addr) {
        this(companyid, name, founded);
        this.address = (FCAddress)addr;
    }

    /**
     * Get the company id.
     * @return The company id.
     */
    public long getCompanyid() {
        return companyid;
    }
    
    /** 
     * Set the id associated with this object.
     * @param id the id.
     */
    public void setCompanyid(long id) {
        if (this.companyid != 0)
            throw new IllegalStateException("Id is already set.");
        this.companyid = id;
    }

    /**
     * Get the name of the company.
     * @return The name of the company.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the  name of the company.
     * @param name The value to use for the name of the company.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the date that the company was founded.
     * @return The date the company was founded.
     */
    public Date getFounded() {
        return founded;
    }

    /**
     * Set the date that the company was founded.
     * @param founded The date to set that the company was founded.
     */
    public void setFounded(Date founded) {
        this.founded = founded;
    }

    /**
     * Get the address of the company.
     * @return The primary address of the company.
     */
    public IAddress getAddress() {
        return address;
    }
    
    /**
     * Set the primary address for the company.
     * @param address The address to set for the company.
     */
    public void setAddress(IAddress address) {
        this.address = (FCAddress)address;
    }

    /**
     * Get the departments contained in the company.
     * @return An unmodifiable <code>Set</code> that contains all the
     * <code>FCDepartment</code>s of the company.
     */
    public Set getDepartments() {
        return Collections.unmodifiableSet(departments);
    }

    /**
     * Add a <code>FCDepartment</code> instance to the company.
     * @param dept The <code>FCDepartment</code> instance to add.
     */
    public void addDepartment(FCDepartment dept) {
        departments.add(dept);
    }

    /**
     * Remove a <code>FCDepartment</code> instance from the company.
     * @param dept The <code>FCDepartment</code> instance to remove.
     */
    public void removeDepartment(FCDepartment dept) {
        departments.remove(dept);
    }

    /**
     * Initialize the set of <code>FCDepartment</code>s in the company to the 
     * parameter. 
     * @param departments The set of <code>FCDepartment</code>s for the
     * company. 
     */
    public void setDepartments(Set departments) {
        // workaround: create a new HashSet, because fostore does not
        // support LinkedHashSet
        this.departments = 
            (departments != null) ? new HashSet(departments) : null;
    }
    
    /** Serialization support: initialize transient fields. */
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        departments = new HashSet();
    }

    /**
     * Returns a String representation of a <code>Company</code> object.
     * @return a String representation of a <code>Company</code> object.
     */
    public String toString() {
        return "Company(" + getFieldRepr()+ ")";
    }
    
    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    protected String getFieldRepr() {
        StringBuffer rc = new StringBuffer();
        rc.append(companyid);
        rc.append(", name ").append(name);
        rc.append(", founded ").append(formatter.format(founded));
        return rc.toString();
    }

    /** 
     * Returns <code>true</code> if all the fields of this instance are
     * deep equal to the coresponding fields of the specified Person.
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
        ICompany otherCompany = (ICompany)other;
        String where = "Company<" + companyid + ">";
        return 
            helper.equals(companyid, otherCompany.getCompanyid(), where + ".companyid") &
            helper.equals(name, otherCompany.getName(), where + ".name") &
            helper.equals(founded, otherCompany.getFounded(), where + ".founded") &
            helper.deepEquals(address, otherCompany.getAddress(), where + ".address") &
            helper.deepEquals(departments, otherCompany.getDepartments(), where + ".departments");
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
        return compareTo((ICompany)o);
    }

    /** 
     * Compare two instances. This is a method in Comparator.
     */
    public int compare(Object o1, Object o2) {
        return compare((ICompany)o1, (ICompany)o2);
    }

    /** 
     * Compares this object with the specified Company object for
     * order. Returns a negative integer, zero, or a positive integer as
     * this object is less than, equal to, or greater than the specified
     * object.  
     * @param other The Company object to be compared. 
     * @return a negative integer, zero, or a positive integer as this
     * object is less than, equal to, or greater than the specified Company
     * object. 
     */
    public int compareTo(ICompany other) {
        return compare(this, other);
    }

    /**
     * Compares its two ICompany arguments for order. Returns a negative
     * integer, zero, or a positive integer as the first argument is less
     * than, equal to, or greater than the second. 
     * @param o1 the first ICompany object to be compared. 
     * @param o2 the second ICompany object to be compared. 
     * @return a negative integer, zero, or a positive integer as the first
     * object is less than, equal to, or greater than the second object. 
     */
    public static int compare(ICompany o1, ICompany o2) {
        return EqualityHelper.compare(o1.getCompanyid(), o2.getCompanyid());
    }
    
    /** 
     * Indicates whether some other object is "equal to" this one.
     * @param obj the object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     * argument; <code>false</code> otherwise. 
     */
    public boolean equals(Object obj) {
        if (obj instanceof ICompany) {
            return compareTo((ICompany)obj) == 0;
        }
        return false;
    }
        
    /**
     * Returns a hash code value for the object. 
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return (int)companyid;
    }
    
    /**
     * The class to be used as the application identifier
     * for the <code>Company</code> class. It consists of both the company 
     * name and the date that the company was founded.
     */
    public static class Oid implements Serializable, Comparable {

        /**
         * This field is part of the identifier and should match in name
         * and type with a field in the <code>Company</code> class.
         */
        public long companyid;

        /** The required public no-arg constructor. */
        public Oid() { }

        /**
         * Initialize the identifier.
         * @param companyid The id of the company.
         */
        public Oid(long companyid) {
            this.companyid = companyid;
        }
        
        public Oid(String s) { companyid = Long.parseLong(justTheId(s)); }

        public String toString() { return this.getClass().getName() + ": "  + companyid;}

        
        /** */
        public boolean equals(Object obj) {
            if (obj==null || !this.getClass().equals(obj.getClass())) 
                return false;
            Oid o = (Oid) obj;
            if (this.companyid != o.companyid) 
                return false;
            return true;
        }

        /** */
        public int hashCode() {
            return (int)companyid;
        }
        
        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }

        /** */
        public int compareTo(Object obj) {
            // may throw ClassCastException which the user must handle
            Oid other = (Oid) obj;
            if( companyid < other.companyid ) return -1;
            if( companyid > other.companyid ) return 1;
            return 0;
        }
        
    }

}

