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
public class AllPersist2 extends AllPersist {

    public char charC;  // not managed
    public boolean booleanD; // not managed
    public float floatE;  // transactional
    
    public AllPersist2() {
        charC = '3';
        booleanD = false;
        floatE = -4.4f;
    }
    
    public AllPersist2 (int intA, double doubleB, int intB, char charC, boolean booleanVal, float floatE) {
        super(intA, doubleB, intB);
        this.charC = charC;
        booleanD = booleanVal;
        this.floatE = floatE;
    }
}
