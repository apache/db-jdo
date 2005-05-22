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

/*
 * PCFullTimeEmployee1.java
 *
 * Created on May 22, 2001, 9:36 AM
 */

package org.apache.jdo.pc;

/**
 *
 * @author  clr
 * @version 
 */
public class PCFullTimeEmployee1 extends PCEmployee1 {

    public int range;
    
    /** Creates new PCFullTimeEmployee1 */
    public PCFullTimeEmployee1() {
    }
    public PCFullTimeEmployee1(
            String _first,  
            String _last, 
            java.util.Date _born, 
            long _empid, 
            java.util.Date _hired,
            double _sal,
            int _range ) {
        super (_empid, _last, _first, _sal, _born, _hired);
        range = _range;
    }
    
    public double getRange() {
        return range;
    }
    
    public void setRange(int range) {
        this.range = range;
    }
    
    public String toString() {
        StringBuffer rc = new StringBuffer("PCFullTimeEmployee1: ");
        rc.append(super.toString());
        rc.append(" range: " + range);
        return rc.toString();
    }
            

}
