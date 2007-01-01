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

package org.apache.jdo.pc.xempdept;

import java.io.Serializable;
import java.util.HashSet;
import java.math.BigDecimal;

public class Project implements Serializable, Identifiable {

    private long projid;
    private String name;
    private BigDecimal budget;
    private HashSet reviewers;
    private HashSet employees;

    public Object getOid() {
        Oid oid = new Oid();
        oid.projid = this.projid;
        return oid;
    }

    public String toString() {
        return "Project(" + name +
               ", projid=" + projid +
               ", budget=" + budget +
               ", reviewers=" + ((reviewers==null) ? 0 : reviewers.size()) +
               ", employees=" + ((employees==null) ? 0 : employees.size()) +
               ")";
    }

    public Project() {
    }

    public Project(long projid,
                   String name,
                   BigDecimal budget,
                   HashSet reviewers,
                   HashSet employees) {
        this.projid = projid;
        this.name = name;
        this.budget = budget;
        this.reviewers = reviewers;
        this.employees = employees;
    }

    public long getProjid() {
        return projid;
    }

    public void setProjid(long projid) {
        this.projid = projid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public HashSet getReviewers() {
        return reviewers;
    }

    public void setReviewers(HashSet reviewers) {
        this.reviewers = reviewers;
    }

    public HashSet getEmployees() {
        return employees;
    }

    public void setEmployees(HashSet employees) {
        this.employees = employees;
    }

    public static class Oid implements Serializable, Comparable {

        public long projid;

        public Oid() {
        }

        public boolean equals(java.lang.Object obj) {
            if( obj==null ||
                !this.getClass().equals(obj.getClass()) ) return( false );
            Oid o=(Oid) obj;
            if( this.projid!=o.projid ) return( false );
            return( true );
        }

        public int hashCode() {
            int hashCode=0;
            hashCode += projid;
            return( hashCode );
        }

        public int compareTo(Object o) {
            if (o == null)
                throw new ClassCastException();
            if (o == this)
                return 0;
            long otherProjid = ((Oid)o).projid;
            if (projid == otherProjid)
                return 0;
            else if (projid < otherProjid)
                return -1;
            return 1;
        }

    }

}

