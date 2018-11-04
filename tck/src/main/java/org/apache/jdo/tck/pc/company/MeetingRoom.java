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
 * This class represents a meeting room.
 */
@PersistenceCapable
public class MeetingRoom
    implements IMeetingRoom, Serializable, Comparable, Comparator, DeepEquality {

    private long    roomid;
    private String  name;

    /** This is the JDO-required no-args constructor. The TCK relies on
     * this constructor for testing PersistenceManager.newInstance(PCClass).
     */
    public MeetingRoom() {}

    /**
     * This constructor initializes the <code>MeetingRoom</code> components.
     * @param roomid The room ID.
     * @param name The name of the room
     */
    public MeetingRoom(long roomid, String name)
    {
        this.roomid = roomid;
        this.name = name;
    }

    /**
     * Get the room id associated with this object.
     * @return the room id.
     */
    public long getRoomid() {
        return roomid;
    }

    /**
     * Set the id associated with this object.
     * @param id the id.
     */
    public void setRoomid(long id) {
        if (this.roomid != 0)
            throw new IllegalStateException("Id is already set.");
        this.roomid = id;
    }

    /** 
     * Get the name of the meeting room.
     * @return The name of the meeting room.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the meeting room.
     * @param name The name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a String representation of a <code>Address</code> object.
     * @return a String representation of a <code>MeetingRoom</code> object.
     */
    public String toString() {
        return "MeetingRoom(" + getFieldRepr() + ")";
    }
    
    /**
     * Returns a String representation of the non-relationship fields.
     * @return a String representation of the non-relationship fields.
     */
    protected String getFieldRepr() {
        StringBuffer rc = new StringBuffer();
        rc.append(roomid);
        rc.append(", name ").append(name);
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
        IMeetingRoom otherMeetingRoom = (IMeetingRoom)other;
        String where = "MeetingRoom<" + roomid + ">";
        return
            helper.equals(roomid, otherMeetingRoom.getRoomid(), where + ".roomid") &
            helper.equals(name, otherMeetingRoom.getName(), where + ".name");
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
        return compareTo((IMeetingRoom)o);
    }

    /** 
     * Compare two instances. This is a method in Comparator.
     */
    public int compare(Object o1, Object o2) {
        return compare((IMeetingRoom)o1, (IMeetingRoom)o2);
    }

    /** 
     * Compares this object with the specified MeetingRoom object for
     * order. Returns a negative integer, zero, or a positive integer as
     * this object is less than, equal to, or greater than the specified
     * object.  
     * @param other The MeetingRoom object to be compared. 
     * @return a negative integer, zero, or a positive integer as this
     * object is less than, equal to, or greater than the specified MeetingRoom
     * object. 
     */
    public int compareTo(IMeetingRoom other) {
        return compare(this, other);
    }
    
    /**
     * Compares its two IMeetingRoom arguments for order. Returns a negative
     * integer, zero, or a positive integer as the first argument is less
     * than, equal to, or greater than the second. 
     * @param o1 the first IMeetingRoom object to be compared. 
     * @param o2 the second IMeetingRoom object to be compared. 
     * @return a negative integer, zero, or a positive integer as the first
     * object is less than, equal to, or greater than the second object. 
     */
    public static int compare(IMeetingRoom o1, IMeetingRoom o2) {
        return EqualityHelper.compare(o1.getRoomid(), o2.getRoomid());
    }

    /** 
     * Indicates whether some other object is "equal to" this one.
     * @param obj the object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     * argument; <code>false</code> otherwise. 
     */
    public boolean equals(Object obj) {
        if (obj instanceof IMeetingRoom) {
            return compareTo((IMeetingRoom)obj) == 0;
        }
        return false;
    }

    /**
     * Returns a hash code value for the object. 
     * @return a hash code value for this object.
     */
    public int hashCode() {
        return (int)roomid;
    }
    
    /**
     * This class is used to represent the application identifier 
     * for the <code>MeetingRoom</code> class.
     */
    public static class Oid implements Serializable, Comparable {

        /**
         * This is the identifier field for <code>MeetingRoom</code> and must
         * correspond in type and name to the field in
         * <code>MeetingRoom</code>. 
         */
        public long roomid;
        
        /** The required public, no-arg constructor. */
        public Oid()
        {
            roomid = 0;
        }

        /**
         * A constructor to initialize the identifier field.
         * @param roomid the id of the MeetingRoom.
         */
        public Oid(long roomid) {
            this.roomid = roomid;
        }
        
        public Oid(String s) { roomid = Long.parseLong(justTheId(s)); }

        public String toString() { return this.getClass().getName() + ": "  + roomid;}


        /** */
        public boolean equals(java.lang.Object obj) {
            if( obj==null || !this.getClass().equals(obj.getClass()) )
                return( false );
            Oid o = (Oid) obj;
            if( this.roomid != o.roomid ) return( false );
            return( true );
        }

        /** */
        public int hashCode() {
            return( (int) roomid );
        }
        
        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }

        /** */
        public int compareTo(Object obj) {
            // may throw ClassCastException which the user must handle
            Oid other = (Oid) obj;
            if( roomid < other.roomid ) return -1;
            if( roomid > other.roomid ) return 1;
            return 0;
        }
    }
}
