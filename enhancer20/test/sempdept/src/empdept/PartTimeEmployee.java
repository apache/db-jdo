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

package empdept;


/**
 * A sample employee subclass.
 */
public class PartTimeEmployee extends Employee {
    private int cid;
    private double wage;
    
    public PartTimeEmployee() {
    }
    
    public PartTimeEmployee(String str) {
        super();
    }

    public Object clone() throws java.lang.CloneNotSupportedException {
        // clone() method for enhancer testing purpose
        final empdept.PartTimeEmployee pc = (empdept.PartTimeEmployee)super.clone();
        pc.cid = this.cid;
        pc.wage = this.wage;
        return pc;
    }

    public int getCid()
    {
        return this.cid;
    }

    public void setCid(int cid)
    {
        this.cid = cid;
    }

    public double getWage() {
        return wage;
    }
    
    public void setWage(double wage) {
        this.wage = wage;
    }

    public static class Oid
        extends empdept.Employee.Oid
        implements java.io.Serializable {
        public int cid;
    
        public Oid() {
            super();
        }
    
        public Oid(String str) {
            super(str);
            // not implemented yet
            throw new UnsupportedOperationException();
        }
    
        public int hashCode() {
            int hash = super.hashCode();
            hash += (int)cid;
            return hash;
        }
    
        public boolean equals(java.lang.Object pk) {
            if (!super.equals(pk)) {
                return false;
            }
            Oid oid = (Oid)pk;
            if (this.cid != oid.cid) return false;
            return true;
        }    
    }    
}
