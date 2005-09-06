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
 

package org.apache.jdo.tck.pc.inheritance;

/** */
public class FieldSameName4 extends FieldSameName3 {
    short n1;  // not managed
    FieldSameName4 n2; // not managed
    int n3; // persistent
    
    public FieldSameName4() {
        n1 = -360;
        n3 = -6;
    }
    
    public FieldSameName4 (int intA, double doubleB, int intB, char charC, boolean booleanD,
       float floatE, short shortF, short shortG, int intVal) {
        super(intA, doubleB, intB, charC, booleanD, floatE, shortF);
        n1 = shortG;
        n3 = intVal;
    }
    
    public void setShortG(short shortG) {
        n1 = shortG;
    }
    
    public short getShortG() {
        return n1;
    }
    
    public void setFourthObj(FieldSameName4 fourthObj) {
        n2 = fourthObj;
    }
    
    public FieldSameName4 getFourthObj() {
        return n2;
    }
    
    public void setIntH(int intH) {
        n3 = intH;
    }
    
    public int getIntH() {
        return n3;
    }
    
}
