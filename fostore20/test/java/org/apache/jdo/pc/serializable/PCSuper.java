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

package org.apache.jdo.pc.serializable;

/**
 * A persistence capable class that does not implement
 * java.io.Serializable, used as superclass for other pc classes: PCSub3,
 * PCSub4A, PCSub4B.
 *
 * The enhancer should add a method jdoPreSerialize:
 * protected final void jdoPreSerialize() {
 *     final javax.jdo.spi.StateManager sm = this.jdoStateManager;
 *     if (sm != null) { sm.preSerialize(this); }
 *  }
 * 
 * @author Michael Bouschen
 */
public class PCSuper
{
    private           int s01;
    private transient int t02;

    public PCSuper()
    {
        this(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
    public PCSuper(int s01, int t02) 
    {
        this.s01 = s01;
        this.t02 = t02;
    }

    public int getS01() { return s01; }
    public void setS01(int value) { this.s01 = value; }

    public int getT02() { return t02; }
    public void setT02(int value) { this.t02 = value; }

    public String toString() 
    {
        StringBuffer repr = new StringBuffer();
        addFieldRepr(repr);
        return "PCSuper(" + repr.toString() + ")";
    }
        
    protected void addFieldRepr(StringBuffer repr)
    {
        repr.append("s01="); repr.append(s01);
        repr.append(", t02="); repr.append(t02);
    }
}

