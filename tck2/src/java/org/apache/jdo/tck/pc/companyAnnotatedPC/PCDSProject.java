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
 
package org.apache.jdo.tck.pc.companyAnnotatedPC;

import javax.jdo.annotations.*;

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
import java.math.BigDecimal;
import org.apache.jdo.tck.pc.company.IProject;

import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

/**
 * This class represents a project, a budgeted task with one or more
 * employees working on it.
 */
@PersistenceCapable(table="projects")
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy=DiscriminatorStrategy.CLASS_NAME,
        column="DISCRIMINATOR")
@DatastoreIdentity(strategy=IdGeneratorStrategy.IDENTITY, column="DATASTORE_IDENTITY")
public class PCDSProject 
    implements IProject, Serializable, Comparable, Comparator, DeepEquality  {

    @NotPersistent()
    private long _projid;
    @NotPersistent()
    private String     _name;
    @NotPersistent()
    private BigDecimal _budget;
    @NotPersistent()
    private transient Set _reviewers = new HashSet();
    @NotPersistent()
    private transient Set _members = new HashSet();

    /** This is the JDO-required no-args constructor. The TCK relies on
     * this constructor for testing PersistenceManager.newInstance(PCClass).
     */
    public PCDSProject() {}

    /**
     * Initialize a project.
     * @param projid The project identifier.
     * @param name The name of the project.
     * @param budget The budget for the project.
     */
    public PCDSProject(long projid, String name, BigDecimal budget) {
        this._projid = projid;
        this._name = name;
        this._budget = budget;
    }

    /**
     * Set the id associated with this object.
     * @param id the id.
     */
    public void setProjid(long id) {
        if (this._projid != 0)
            throw new IllegalStateException("Id is already set.");
        this._projid = id;
    }

    /**
     * Get the project ID.
     * @return The project ID.
     */

    @Column(name="PROJID")
    public long getProjid() {
        return _projid;
    }

    /**
     * Get the name of the project.
     * @return The name of the project.
     */

    @Column(name="NAME")
    public String getName() {
        return _name;
    }

    /**
     * Set the name of the project.
     * @param name The name of the project.
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * Get the project's budget.
     * @return The project's budget.
     */

    @Column(name="BUDGET", jdbcType="DECIMAL", length=11, scale=2)
    public BigDecimal getBudget() {
        return _budget;
    }

    /**
     * Set the project's budget.
     * @param budget The project's budget.
     */
    public void setBudget(BigDecimal budget) {
        this._budget = budget;
    }

    /**
     * Get the reviewers associated with this project.
     */

    @Persistent(table="project_reviewer")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSEmployee.class,
            column="REVIEWER", foreignKey="PR_REV_FK")
    @Join(column="PROJID", foreignKey="PR_PROJ_FK")
    public Set getReviewers() {
        return _reviewers;
    }

    /**
     * Add a reviewer to the project.
     * @param emp The employee to add as a reviewer.
     */
    public void addReviewer(PCDSEmployee emp) {
        _reviewers.add(emp);
    }

    /**
     * Remove a reviewer from the project.
     * @param emp The employee to remove as a reviewer of this project.
     */
    public void removeReviewer(PCDSEmployee emp) {
        _reviewers.remove(emp);
    }

    /**
     * Set the reviewers associated with this project.
     * @param reviewers The set of reviewers to associate with this project.
     */
    public void setReviewers(Set reviewers) {
        // workaround: create a new HashSet, because fostore does not
        // support LinkedHashSet
        this._reviewers = (reviewers != null) ? new HashSet(reviewers) : null;
    }

    /**
     * Get the project members.
     * 
     * 
     * @return The members of the project is returned as a
     * set of <code>PCDSEmployee</code>s.
     */

    @Persistent(table="project_member")
    @Element(types=org.apache.jdo.tck.pc.companyAnnotatedPC.PCDSEmployee.class,
            column="MEMBER", foreignKey="PR_MEMB_FK")
    @Join(column="PROJID", foreignKey="PR_PROJ_FK")
    public Set getMembers() {
        return _members;
    }

    /**
     * Add a new member to the project.
     * @param emp The employee to add to the project.
     */
    public void addMember(PCDSEmployee emp) {
        _members.add(emp);
    }

    /**
     * Remove a member from the project.
     * @param emp The employee to remove from the project.
     */
    public void removeMember(PCDSEmployee emp) {
        _members.remove(emp);
    }

    /**
     * Set the members of the project.
     * @param employees The set of employees to be the members of this
     * project. 
     */
    public void setMembers(Set employees) {
        // workaround: create a new HashSet, because fostore does not
        // support LinkedHashSet
        this._members = (employees != null) ? new HashSet(employees) : null;
    }

    /** Serialization support: initialize transient fields. */
    private void readObject(ObjectInputStream in)
        throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        _reviewers = new HashSet();
        _members = new HashSet();
    }

    /**
     * Returns a String representation of a <code>PCDSProject</code> object.
     * 
     * 
     * @return a String representation of a <code>PCDSProject</code> object.
     */
    public String toString() {
        return "FCProject(" + getFieldRepr() + ")";
    }
    
    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    protected String getFieldRepr() {
        StringBuffer rc = new StringBuffer();
        rc.append(_projid);
        rc.append(", name ").append(_name);
        rc.append(", budget ").append(_budget);
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
        PCDSProject otherProject = (PCDSProject)other;
        String where = "FCProject<" + _projid + ">";
        return 
            helper.equals(_projid, otherProject.getProjid(), where + ".projid") &
            helper.equals(_name, otherProject.getName(), where + ".name") &
            helper.equals(_budget, otherProject.getBudget(), where + ".budget") &
            helper.deepEquals(_reviewers, otherProject.getReviewers(), where + ".reviewers") &
            helper.deepEquals(_members, otherProject.getMembers(), where + ".members");
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
        return compareTo((PCDSProject)o);
    }

    /** 
     * Compare two instances. This is a method in Comparator.
     */
    public int compare(Object o1, Object o2) {
        return compare((PCDSProject)o1, (PCDSProject)o2);
    }

    /**
     * 
     * Compares this object with the specified PCDSProject object for
     * order. Returns a negative integer, zero, or a positive integer as
     * this object is less than, equal to, or greater than the specified
     * object.  
     * 
     * 
     * @param other The PCDSProject object to be compared.
     * @return a negative integer, zero, or a positive integer as this
     * object is less than, equal to, or greater than the specified PCDSProject object.
     */
    public int compareTo(PCDSProject other) {
        return compare(this, other);
    }

    /**
     * Compares its two IProject arguments for order. Returns a negative
     * integer, zero, or a positive integer as the first argument is less
     * than, equal to, or greater than the second. 
     * @param o1 the first IProject object to be compared. 
     * @param o2 the second IProject object to be compared. 
     * @return a negative integer, zero, or a positive integer as the first
     * object is less than, equal to, or greater than the second object. 
     */
    public static int compare(PCDSProject o1, PCDSProject o2) {
        return EqualityHelper.compare(o1.getProjid(), o2.getProjid());
    }

    /** 
     * Indicates whether some other object is "equal to" this one.
     * @param obj the object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     * argument; <code>false</code> otherwise. 
     */
    public boolean equals(Object obj) {
        if (obj instanceof PCDSProject) {
            return compareTo((PCDSProject)obj) == 0;
        }
        return false;
    }
        
    /**
     * Returns a hash code value for the object. 
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return (int)_projid;
    }

    /**
     * This class is used to represent the application identity
     * for the <code>PCDSProject</code> class.
     */
    public static class Oid implements Serializable, Comparable {

        /**
         * This field represents the identifier for the
         * <code>PCDSProject</code> class. It must match a field in the
         * <code>PCDSProject</code> class in both name and type.
         */
        public long projid;

        /**
         * The required public no-arg constructor.
         */
        public Oid() { }

        /**
         * Initialize the application identifier with a project ID.
         * @param projid The id of the project.
         */
        public Oid(long projid) {
            this.projid = projid;
        }
        
        public Oid(String s) { projid = Long.parseLong(justTheId(s)); }

        public String toString() { return this.getClass().getName() + ": "  + projid;}

        /** */
        public boolean equals(java.lang.Object obj) {
            if( obj==null || !this.getClass().equals(obj.getClass()) )
                return( false );
            Oid o = (Oid) obj;
            if( this.projid != o.projid ) return( false );
            return( true );
        }

        /** */
        public int hashCode() {
            return( (int) projid );
        }
        
        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }

        /** */
        public int compareTo(Object obj) {
            // may throw ClassCastException which the user must handle
            Oid other = (Oid) obj;
            if( projid < other.projid ) return -1;
            if( projid > other.projid ) return 1;
            return 0;
        }

    }

}

