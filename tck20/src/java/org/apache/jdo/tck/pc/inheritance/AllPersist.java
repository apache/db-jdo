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
 

package org.apache.jdo.tck.pc.inheritance;

import java.io.Serializable;

/** */
public class AllPersist {

    public int intA;  // not managed
    public double doubleB;  // persistent
    public int intB; // persistent
    
    private int keyValue;  // persistent--used as key field in application identity
    private static int nextKeyValue = 1;
    
    public int hashCode() {
        return keyValue;
    }
    
    public boolean equals(Object obj) {
        if( obj == null || !this.getClass().equals(obj.getClass()) ) return false;
        else return keyValue == ((Id)obj).keyValue;
    }
    
    public AllPersist() {
        intA = -1;
        doubleB = 2.0;
        intB = -5;
    }
    
    public AllPersist(int intA, double doubleVal, int intB) {
        keyValue = nextKeyValue++;
        this.intA = intA;
        doubleB = doubleVal;
        this.intB = intB;
    }
    
    public static class Id implements Serializable {
        public int keyValue;

        public Id() {
        }
    
        public Id(int keyValue) {
            this.keyValue = keyValue;
        }
    
        public Id(String s) {
            try {
                keyValue = Integer.parseInt(s);}
            catch(NumberFormatException e){
                keyValue = 0;
            }
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
