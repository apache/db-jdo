/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.apache.jdo.pc.xempdept;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

public class Company implements Serializable, Identifiable {

    private String name;
    private Date founded;
    private String address;
    private HashSet departments;

    public Object getOid() {
        Oid oid = new Oid();
        oid.name = this.name;
        oid.founded = this.founded;
        return oid;
    }

    public String toString() {
        return "Company(" + name + 
               ", founded=" + founded +
               ", address=" + address +
               ", departments: " + ((departments == null) ? 0 : departments.size()) + 
               ")";
    }

    public Company() {
    }

    public Company(String name,
                   Date founded,
                   String address,
                   HashSet departments) {
        this.name = name;
        this.founded = founded;
        this.address = address;
        this.departments = departments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFounded() {
        return founded;
    }

    public void setFounded(java.util.Date founded) {
        this.founded = founded;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public HashSet getDepartments() {
        return departments;
    }

    public void setDepartments(HashSet departments) {
        this.departments = departments;
    }

    public static class Oid implements Serializable, Comparable {

        public String name;

        public java.util.Date founded;

        public Oid() {
        }

        public boolean equals(java.lang.Object obj) {
            if( obj==null ||
                !this.getClass().equals(obj.getClass()) ) return( false );
            Oid o=(Oid) obj;
            if( (this.name!=o.name) &&
                (this.name==null || !this.name.equals(o.name)) )
                return( false );
            if( (this.founded!=o.founded) &&
                (this.founded==null || !this.founded.equals(o.founded)) )
                return( false );
            return( true );
        }

        public int hashCode() {
            int hashCode=0;
            if( name!=null )
                hashCode += name.hashCode();
            if( founded!=null )
                hashCode += founded.hashCode();
            return( hashCode );
        }

        public int compareTo(Object o) {
            if (o == null)
                throw new ClassCastException();
            if (o == this)
                return 0;
            // WARNING: this only compares the names
            String thisName = name;
            String otherName = ((Oid)o).name;
            if (thisName == null)
                return (otherName == null) ? 0 : -1;
            if (otherName == null)
                return 1;
            return thisName.compareTo(otherName);
        }

    }

}

