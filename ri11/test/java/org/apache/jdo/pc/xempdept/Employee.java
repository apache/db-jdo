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
import java.util.HashSet;
import java.util.Date;

public class Employee 
    extends Person 
    implements Serializable, Identifiable 
{
    private long empid;
    private Date hiredate;
    private double weeklyhours;
    private char discriminator;
    private HashSet reviewedProjects;
    private HashSet projects;
    private Insurance insurance;
    private Department department;
    private Employee manager;
    private HashSet team;
    private Employee mentor;
    private Employee protege;
    private Employee hradvisor;
    private HashSet hradvisees;

    public Object getOid() 
    {
        Oid oid = new Oid();
        oid.empid = this.empid;
        return oid;
    }

    public String toString() 
    {
        StringBuffer repr = new StringBuffer();
        addFieldRepr(repr);
        return "Employee(" + repr.toString() + ")";
    }
    
    public Employee() {}

    public Employee(long empid,
                    String lastname,
                    String firstname,
                    Date hiredate,
                    Date birthdate,
                    double weeklyhours,
                    char discriminator,
                    HashSet reviewedProjects,
                    HashSet projects,
                    Insurance insurance,
                    Department department,
                    Employee manager,
                    HashSet team,
                    Employee mentor,
                    Employee protege,
                    Employee hradvisor,
                    HashSet hradvisees) 
    {
        super(lastname, firstname, birthdate);
        this.empid = empid;
        this.hiredate = hiredate;
        this.weeklyhours = weeklyhours;
        this.discriminator = discriminator;
        this.reviewedProjects = reviewedProjects;
        this.projects = projects;
        this.insurance = insurance;
        this.department = department;
        this.manager = manager;
        this.team = team;
        this.mentor = mentor;
        this.protege = protege;
        this.hradvisor = hradvisor;
        this.hradvisees = hradvisees;
    }

    public long getEmpid() {
        return empid;
    }

    public void setEmpid(long empid) {
        this.empid = empid;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public double getWeeklyhours() {
        return weeklyhours;
    }

    public void setWeeklyhours(double weeklyhours) {
        this.weeklyhours = weeklyhours;
    }

    public char getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(char discriminator) {
        this.discriminator = discriminator;
    }

    public HashSet getReviewedProjects() {
        return reviewedProjects;
    }

    public void setReviewedProjects(HashSet reviewedProjects) {
        this.reviewedProjects = reviewedProjects;
    }

    public HashSet getProjects() {
        return projects;
    }

    public void setProjects(HashSet projects) {
        this.projects = projects;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public HashSet getTeam() {
        return team;
    }

    public void setTeam(HashSet team) {
        this.team = team;
    }

    public Employee getMentor() {
        return mentor;
    }

    public void setMentor(Employee mentor) {
        this.mentor = mentor;
    }

    public Employee getProtege() {
        return protege;
    }

    public void setProtege(Employee protege) {
        this.protege = protege;
    }

    public Employee getHradvisor() {
        return hradvisor;
    }

    public void setHradvisor(Employee hradvisor) {
        this.hradvisor = hradvisor;
    }

    public HashSet getHradvisees() {
        return hradvisees;
    }

    public void setHradvisees(HashSet hradvisees) {
        this.hradvisees = hradvisees;
    }

    protected void addFieldRepr(StringBuffer repr)
    {
        super.addFieldRepr(repr);
        repr.append(", empid="); repr.append(empid);
        repr.append(", hiredate="); repr.append(hiredate);
        repr.append(", weeklyhours="); repr.append(weeklyhours);
        repr.append(", reviewedProjects="); repr.append(((reviewedProjects==null) ? 0 : reviewedProjects.size()));
        repr.append(", projects="); repr.append(((projects==null) ? 0 : projects.size()));
        repr.append(", insurance="); repr.append(((insurance==null) ? "null" : insurance.getCarrier()));
        repr.append(", department="); repr.append(((department==null) ? "null" : department.getName()));
        repr.append(", manager="); repr.append(((manager==null) ? "null" : manager.getLastname()));
        repr.append(", team="); repr.append(((team==null) ? 0 : team.size()));
        repr.append(", mentor="); repr.append(((mentor==null) ? "null" : mentor.getLastname()));
        repr.append(", protege="); repr.append(((protege==null) ? "null" : protege.getLastname()));
        repr.append(", hradvisor="); repr.append(((hradvisor==null) ? "null" : hradvisor.getLastname()));
        repr.append(", hradvisees="); repr.append(((hradvisees ==null) ? 0 : hradvisees.size()));
    }

    public static class Oid implements Serializable, Comparable {

        public long empid;

        public Oid() {
        }

        public boolean equals(java.lang.Object obj) {
            if( obj==null ||
                !this.getClass().equals(obj.getClass()) ) return( false );
            Oid o=(Oid) obj;
            if( this.empid!=o.empid ) return( false );
            return( true );
        }

        public int hashCode() {
            int hashCode=0;
            hashCode += empid;
            return( hashCode );
        }

        public int compareTo(Object o) {
            if (o == null)
                throw new ClassCastException();
            if (o == this)
                return 0;
            long otherEmpid = ((Oid)o).empid;
            if (empid == otherEmpid)
                return 0;
            else if (empid < otherEmpid)
                return -1;
            return 1;
        }
    }

}

