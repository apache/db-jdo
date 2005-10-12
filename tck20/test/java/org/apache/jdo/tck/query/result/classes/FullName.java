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
 
package org.apache.jdo.tck.query.result.classes;

/**
 * JDOQL result class having bean properties <code>firstname</code>,
 * <code>firstName</code>, <code>lastname</code>, and <code>lastName</code>.
 * These properties are used by JDO query tests in result clauses.
 * There are two different bean properties on each field,
 * <code>firstname</code> and <code>lastname</code> 
 * because some result clauses in JDO query tests specify
 * those properties in <code>AS</code> clauses. 
 */
public class FullName {

    private String firstName;
    private String lastName;
    
    public FullName() {}
    
    public FullName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        int result = 0;
        result += this.firstName != null ? this.firstName.hashCode() : 0;
        result += this.lastName != null ? this.lastName.hashCode() : 0;
        return result;
    }
    
    /**
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        FullName other = (FullName) o;
        if (this.firstName == null) {
            return other.firstName == null;
        }
        if (this.lastName == null) {
            return other.lastName == null;
        }
        return this.firstName.equals(other.firstName) &&
               this.lastName.equals(other.lastName);
    }
    
    /**
     * @see Object#toString()
     */
    public String toString() {
        return this.firstName + ' ' + this.lastName;
    }
    
    /**
     * @return Returns the firstName.
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * @param firstName The firstName to set.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * @return Returns the lastName.
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * @param lastName The lastName to set.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return Returns the firstName.
     */
    public String getFirstname() {
        return firstName;
    }
    
    /**
     * @param firstName The firstName to set.
     */
    public void setFirstname(String firstname) {
        this.firstName = firstname;
    }
    
    /**
     * @return Returns the lastName.
     */
    public String getLastname() {
        return lastName;
    }
    
    /**
     * @param lastName The lastName to set.
     */
    public void setLastname(String lastname) {
        this.lastName = lastname;
    }

}
