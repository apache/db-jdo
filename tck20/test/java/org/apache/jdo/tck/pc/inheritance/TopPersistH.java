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
public class TopPersistH extends TopPersistG { // persistent

    public int intH; // persistent
    
    public TopPersistH() {
        intH = Constants.intH_V[0];
    }
    
    public TopPersistH (int intA, double doubleB, int intB, char charC, boolean booleanD,
        float floatE, short shortF, short shortG, int intVal) {
        super(intA, doubleB, intB, charC, booleanD, floatE, shortF, shortG);
        intH = intVal;
    }
}
