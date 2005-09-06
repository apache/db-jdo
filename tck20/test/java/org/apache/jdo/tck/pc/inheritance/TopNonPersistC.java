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
public class TopNonPersistC extends TopNonPersistB { // not persistent
    
    public char charC;
    
    public TopNonPersistC() {
        charC = '3';
    }
    
    public TopNonPersistC (int intA, double doubleB, int intB, char charC) {
        super(intA, doubleB, intB);
        this.charC = charC;
    }
}
