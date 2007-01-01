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

import java.util.HashSet;
import java.util.Date;

public class FullTimeEmployee
    extends Employee
{
    private double salary;

    public String toString() 
    {
        StringBuffer repr = new StringBuffer();
        addFieldRepr(repr);
        return "FullTimeEmployee(" + repr.toString() + ")";
    }
    
    public FullTimeEmployee() {}

    public FullTimeEmployee(long empid,
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
                            HashSet hradvisees,
                            double salary) 
    {
        super(empid, lastname, firstname, hiredate, birthdate, weeklyhours, 
              discriminator, reviewedProjects, projects, insurance, department,
              manager, team, mentor, protege, hradvisor, hradvisees);
        this.salary = salary;
    }

    public double getSalary()
    {
        return salary;
    }

    public void setSalary(double salary) 
    {
        this.salary = salary;
    }

    protected void addFieldRepr(StringBuffer repr)
    {
        super.addFieldRepr(repr);
        repr.append(", salary="); repr.append(salary);
    }
}

