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

import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.IOException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.apache.jdo.tck.util.DeepEquality;
import org.apache.jdo.tck.util.EqualityHelper;

/**
 * This class represents a department within a company.
 */
public class Department
    implements IDepartment, Serializable, Comparable, Comparator, DeepEquality {

    public static final int RECOMMENDED_NO_OF_EMPS = 2;
    
    private long    deptid;
    private String  name;
    private Company company;
    private Map roles;

    /** This is the JDO-required no-args constructor. The TCK relies on
     * this constructor for testing PersistenceManager.newInstance(PCClass).
     */
    public Department() {}

    /**
     * Construct a <code>Department</code> instance.
     * @param deptid The department id.
     * @param name The name of the department.
     */
    public Department(long deptid, String name) {
        this.deptid = deptid;
        this.name = name;
    }

    /**
     * Construct a <code>Department</code> instance.
     * @param deptid The department id.
     * @param name The name of the department.
     * @param company The company that the department is associated with. 
     */
    public Department(long deptid, String name, ICompany company) {
        this.deptid = deptid;
        this.name = name;
        this.company = (Company)company;
    }

    /**
     * Construct a <code>Department</code> instance.
     * @param deptid The department id.
     * @param name The name of the department.
     * @param company The company that the department is associated with.
     * @param roles Map of employees and their roles
     */
    public Department(long deptid, String name, Company company, 
                      Map roles) {
        this.deptid = deptid;
        this.name = name;
        this.company = (Company)company;
        this.roles = roles;
    }

    /**
     * Set the id associated with this object.
     * @param id the id.
     */
    public void setDeptid(long id) {
        if (this.deptid != 0)
            throw new IllegalStateException("Id is already set.");
        this.deptid = id;
    }

    /**
     * Get the department id.
     * @return The department id.
     */
    public long getDeptid() {
        return deptid;
    }

    /**
     * Get the name of the department.
     * @return The name of the department.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the department.
     * @param name The name to set for the department.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the company associated with the department.
     * @return The company.
     */
    public ICompany getCompany() {
        return company;
    }

    /**
     * Set the company for the department.
     * @param company The company to associate with the department.
     */
    public void setCompany(ICompany company) {
        this.company = (Company)company;
    }

    /**
     * Get the roles in the department as an unmodifiable set.
     ******************* FIX??
     * @return The set of employees in the department, as an unmodifiable
     * set. 
     */
    public Map getRoles() {
        return roles;
    }

    /**
     * Add an role to the department.
     * @param role The role to add to the department.
     * @param emp The employee in the role
     */
    public void addRole(String role, Employee emp) {
        roles.put(role, emp);
    }

    /**
     * Remove an role from the department.
     * @param key The key of the role to remove from the department.
     */
    public void removeRole(String key) {
        roles.remove(key);
    }

    /**
     * Set the roles to be in this department.
     * @param roles The roles for this department.
     */
    public void setRoles(Map roles) {
        this.roles = roles;
    }

    /** 
     * Returns <code>true</code> if all the fields of this instance are
     * deep equal to the coresponding fields of the other Department.
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
        IDepartment otherDept = (IDepartment)other;
        String where = "Department<" + deptid + ">";
        return 
            helper.equals(deptid, otherDept.getDeptid(), where + ".deptid") & 
            helper.equals(name, otherDept.getName(), where + ".name") &
            helper.deepEquals(roles, otherDept.getRoles(), where + ".roles") &
            helper.deepEquals(company, otherDept.getCompany(), where + ".company") ;
    }
    
    /**
     * Returns a String representation of a <code>Department</code> object.
     * @return a String representation of a <code>Department</code> object.
     */
    public String toString() {
        return "Department(" + getFieldRepr()+ ")";
    }

    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    protected String getFieldRepr() {
        StringBuffer rc = new StringBuffer();
        rc.append(deptid);
        rc.append(", name ").append(name);
        return rc.toString();
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
        return compareTo((IDepartment)o);
    }

    /** 
     * Compare two instances. This is a method in Comparator.
     */
    public int compare(Object o1, Object o2) {
        return compare((IDepartment)o1, (IDepartment)o2);
    }

    /** 
     * Compares this object with the specified Department object for
     * order. Returns a negative integer, zero, or a positive integer as
     * this object is less than, equal to, or greater than the specified
     * object.  
     * @param other The Department object to be compared. 
     * @return a negative integer, zero, or a positive integer as this
     * object is less than, equal to, or greater than the specified
     * Department object. 
     */
    public int compareTo(IDepartment other) {
        return compare(this, other);
    }

    /**
     * Compares its two IDepartment arguments for order. Returns a negative
     * integer, zero, or a positive integer as the first argument is less
     * than, equal to, or greater than the second. 
     * @param o1 the first IDepartment object to be compared. 
     * @param o2 the second IDepartment object to be compared. 
     * @return a negative integer, zero, or a positive integer as the first
     * object is less than, equal to, or greater than the second object. 
     */
    public static int compare(IDepartment o1, IDepartment o2) {
        return EqualityHelper.compare(o1.getDeptid(), o2.getDeptid());
    }
    
    /** 
     * Indicates whether some other object is "equal to" this one.
     * @param obj the object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     * argument; <code>false</code> otherwise. 
     */
    public boolean equals(Object obj) {
        if (obj instanceof IDepartment) {
            return compareTo((IDepartment)obj) == 0;
        }
        return false;
    }
        
    /**
     * Returns a hash code value for the object. 
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return (int)deptid;
    }

    /**
     * The application identity class associated with the
     * <code>Department</code> class. 
     */
    public static class Oid implements Serializable, Comparable {

        /**
         * This field represents the application identifier field 
         * for the <code>Department</code> class. 
         * It must match in name and type with the field in the
         * <code>Department</code> class. 
         */
        public long deptid;

        /**
         * The required public, no-arg constructor.
         */
        public Oid() { }

        /**
         * A constructor to initialize the identifier field.
         * @param deptid the deptid of the Department.
         */
        public Oid(long deptid) {
            this.deptid = deptid;
        }
        
        public Oid(String s) { deptid = Long.parseLong(justTheId(s)); }

        public String toString() { return this.getClass().getName() + ": "  + deptid;}


        /** */
        public boolean equals(java.lang.Object obj) {
            if( obj==null || !this.getClass().equals(obj.getClass()) )
                return( false );
            Oid o = (Oid) obj;
            if( this.deptid != o.deptid ) return( false );
            return( true );
        }

        /** */
        public int hashCode() {
            return( (int) deptid );
        }
        
        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }

        /** */
        public int compareTo(Object obj) {
            // may throw ClassCastException which the user must handle
            Oid other = (Oid) obj;
            if( deptid < other.deptid ) return -1;
            if( deptid > other.deptid ) return 1;
            return 0;
        }

    }

}

