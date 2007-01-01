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
import java.io.ObjectStreamException;

/**
 * There is the following inheritance hierarchy:
 * PCSuper <- Transient <- PCSub.
 * PCSuper and PCSub are pc classes, Transient is a non-pc class.
 * PCSuper does not implement java.io.Serializable, but Transient does.
 * PCSub automatically implement java.io.Serializable, because its super
 * class implements the interface.
 * PCSub provides an implementation of method writeReplace.
 * 
 * The enhancer should add a method jdoPreSerialize to the PCSuper and
 * should add a call of method jdoPreSerialize() to the existing method
 * writeObject. It needs to be the first statement of writeReplace.
 *
 * @author Michael Bouschen
 */
public class PCSub4B
  extends Transient
{
    private           String s03;
    private transient String t04;

    public PCSub4B() 
    { 
        this(Integer.MIN_VALUE, Integer.MIN_VALUE, "empty", "empty");
    }
    public PCSub4B(int s01, int t02, String s03, String t04) 
    {
        super(s01, t02);
        this.s03 = s03;
        this.t04 = t04;
    }

    public boolean equals(Object obj) {
        if (obj==null || !(obj instanceof PCSub4B)) return false;
        PCSub4B o = (PCSub4B)obj;
        if (this.getS01() != o.getS01()) return false;
        if (this.getT02() != o.getT02()) return false;
        if (!stringEquals(this.s03, o.s03)) return false;
        if (!stringEquals(this.t04, o.t04)) return false;
        return true;
    }

    private boolean stringEquals(String s1, String s2)
    {
        return (s1 == null) ? (s2 == null) : s1.equals(s2);
    }

    public String toString() 
    {
        StringBuffer repr = new StringBuffer();
        addFieldRepr(repr);
        return "PCSub4B(" + repr.toString() + ")";
    }
        
    protected void addFieldRepr(StringBuffer repr)
    {
        super.addFieldRepr(repr);
        repr.append(", s03="); repr.append(s03);
        repr.append(", t04="); repr.append(t04);
    }

    // --- implement writeReplace ---
    
    private Object writeReplace() 
        throws ObjectStreamException
    {
        return this;
    }
}

