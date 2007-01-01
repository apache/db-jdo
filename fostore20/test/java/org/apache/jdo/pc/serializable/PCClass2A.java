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

package org.apache.jdo.pc.serializable;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * A pc class has no persistence-capable superclass, implements 
 * java.io.Serializable and provides an implementation of method
 * writeObject. 
 * 
 * The enhancer should add a method jdoPreSerialize
 * protected final void jdoPreSerialize() {
 *     final javax.jdo.spi.StateManager sm = this.jdoStateManager;
 *     if (sm != null) { sm.preSerialize(this); }
 * }
 * and should add a call of method jdoPreSerialize() to the existing
 * method writeObject. It needs to be the first statement of writeObject.
 *
 * @author Michael Bouschen
 */
public class PCClass2A
    implements Serializable
{
    private           int s01;
    private transient int t02;

    public PCClass2A()
    {
        this(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
    public PCClass2A(int s01, int t02) 
    {
        this.s01 = s01;
        this.t02 = t02;
    }

    public int getS01() { return s01; }
    public void setS01(int value) { this.s01 = value; }

    public int getT02() { return t02; }
    public void setT02(int value) { this.t02 = value; }

    public boolean equals(Object obj) {
        if (obj==null || !(obj instanceof PCClass2A)) return false;
        PCClass2A o = (PCClass2A)obj;
        if (this.s01 != o.s01) return false;
        if (this.t02 != o.t02) return false;
        return true;
    }

    public String toString() 
    {
        StringBuffer repr = new StringBuffer();
        addFieldRepr(repr);
        return "PCClass2A(" + repr.toString() + ")";
    }
        
    protected void addFieldRepr(StringBuffer repr)
    {
        repr.append("s01="); repr.append(s01);
        repr.append(", t02="); repr.append(t02);
    }

    // --- implement writeObject ---
    
    private void writeObject(ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
    }
}

