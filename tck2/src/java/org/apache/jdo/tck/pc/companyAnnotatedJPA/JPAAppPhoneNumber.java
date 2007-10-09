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
 

package org.apache.jdo.tck.pc.companyAnnotatedJPA;

import javax.persistence.*;

import java.io.Serializable;

/**
 * This class represents a person.
 */
@Entity
@IdClass(org.apache.jdo.tck.pc.companyAnnotatedJPA.JPAAppPhoneNumber.Oid.class)
@Table(name="employee_phoneno_type")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING,
        name="DISCRIMINATOR")
public class JPAAppPhoneNumber implements Serializable {

    @Id
    @Column(name="PERSONID")
    private long personid;
    @Id
    @Column(name="TYPE")
    private String  type;
    @Column(name="PHONENO")
    private String  phoneNumber;
    
    /** This is the JDO-required no-args constructor. */
    protected JPAAppPhoneNumber() {}

    /**
     * Construct a <code>JPAAppPhoneNumber</code> instance.
     * 
     * @param phonenumid The phone number identifier.
     * @param personid The person id
     * @param type The type of the phone for this number
     * @param phoneNumber The phone number
     */
    public JPAAppPhoneNumber(long personid, String type, String phoneNumber) {
        this.personid = personid;
        this.type = type;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Set the id associated with this object.
     * @param id the id.
     */
    public void setPersonid(long id) {
        if (this.personid != 0)
            throw new IllegalStateException("Id is already set.");
        this.personid = id;
    }

    /**
     * Get the person's id.
     * @return The personid.
     */
    public long getPersonid() {
        return personid;
    }

    /**
     * Get the person's last name.
     * @return The last name.
     */

    public String getType() {
        return type;
    }

    /**
     * Set the phone number type
     * @param type The phone number type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the phone number.
     * @return The phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the phone number.
     * @param phone number The phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns a String representation of a <code>JPAAppPhoneNumber</code> object.
     * 
     * @return a string representation of a <code>JPAAppPhoneNumber</code> object.
     */
    public String toString() {
        return "JPAAppPhoneNumber(" + getFieldRepr() + ")";
    }
    
    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    protected String getFieldRepr() {
        StringBuffer rc = new StringBuffer();
        rc.append(personid);
        rc.append(", ").append(type);
        rc.append(", phone ").append(phoneNumber);
        return rc.toString();
    }

    /**
     * This class is used to represent the application identifier
     * for the <code>Person</code> class.
     */
    public static class Oid implements Serializable, Comparable {

        /**
         * This field represents the identifier for the <code>Person</code>
         * class. It must match a field in the <code>Person</code> class in
         * both name and type. 
         */
        public long personid;

        public String type;

        /**
         * The required public no-arg constructor.
         */
        public Oid() { }

        /**
         * Initialize the identifier.
         * @param personid The person identifier.
         */
        public Oid(long personid, String type) {
            this.personid = personid;
            this.type = type;
        }
        
        public Oid(String s) {
            personid = Long.parseLong(justTheId(s));
            type = justTheType(s);
        }

        public String toString() {
            return this.getClass().getName() + ": "  + personid + " + " + type;
        }

        /** */
        public boolean equals(java.lang.Object obj) {
            if( obj==null ||
                !this.getClass().equals(obj.getClass()) ) return( false );
            Oid o = (Oid) obj;
            if( this.personid != o.personid ) return( false );
            if( this.type != o.type ) return( false );
            return( true );
        }

        /** */
        public int hashCode() {
            return( (int) personid + type.hashCode() );
        }
        
        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1, str.indexOf('+') - 1);
        }
        
        protected static String justTheType(String str) {
            return str.substring(str.indexOf('+') + 1);
        }

        /** */
        public int compareTo(Object obj) {
            // may throw ClassCastException which the user must handle
            Oid other = (Oid) obj;
            if( personid < other.personid ) return -1;
            if( personid > other.personid ) return 1;

            return type.compareTo(other.type);
        }

    }

}
