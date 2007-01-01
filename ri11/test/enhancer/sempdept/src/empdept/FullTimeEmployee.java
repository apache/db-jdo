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

package empdept;


/**
 * A sample employee subclass.
 */
public class FullTimeEmployee extends Employee {
    private double salary;
    
    public FullTimeEmployee() {
    }
    
    public FullTimeEmployee(String str) {
        super();
    }

    public Object clone() throws java.lang.CloneNotSupportedException {
        // clone() method for enhancer testing purpose
        final empdept.FullTimeEmployee pc = (empdept.FullTimeEmployee)super.clone();
        pc.salary = this.salary;
        return pc;
    }

    public double getSalary() {
        return salary;
    }
    
    public void setSalary(double salary) {
        this.salary = salary;
    }
}
