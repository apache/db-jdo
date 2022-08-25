/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
 

package org.apache.jdo.tck.pc.inheritance;

import java.io.Serializable;

/** */
public class FieldSameName {
    int n1;  // not managed
    double n2;  // persistent
    int n3; // persistent
    
    private int keyValue;  // persistent--used as key field in application identity
    private static int nextKeyValue = 1;
    
    public FieldSameName() {
        n1 = -1;
        n2 = 2.0;
        n3 = -5;
    }
    
    public FieldSameName(int intA, double doubleVal, int intB) {
        keyValue = nextKeyValue++;
        n1 = intA;
        n2 = doubleVal;
        n3 = intB;
    }
    
    public void setIntA(int intA) {
        n1 = intA;
    }
    
    public int getIntA() {
        return n1;
    }

    public void setDoubleB(double doubleB) {
        n2 = doubleB;
    }
    
    public double getDoubleB() {
        return n2;
    }
    
    public void setIntB(int intB) {
        n3 = intB;
    }
    
    public int getIntB() {
        return n3;
    }
    
    public static class Id implements Serializable {

        private static final long serialVersionUID = 1L;

        public int keyValue;

        public Id() {
        }
    
        public Id(int keyValue) {
            this.keyValue = keyValue;
        }
    
        public Id(String s) {
            try{ keyValue = Integer.parseInt(s);}
            catch(NumberFormatException e){
                keyValue = 0;}
        }
    
        public boolean equals(Object obj) {
            if( obj == null || !this.getClass().equals(obj.getClass()) ) return false;
            else return keyValue == ((Id)obj).keyValue;
        }
    
        public int hashCode() {
            return keyValue;
        }
    
        public String toString() {
            return Integer.toString(keyValue);
        } 
    }
}
