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


package org.apache.jdo.tck.pc.lifecycle;

public class StateTransitionObj {
    private int      int_field;
    private transient int nonmanaged_field;
    
    public StateTransitionObj()
    {
        int_field = 0;
    }
    public StateTransitionObj(int v)
    {
        int_field = v;
    }
    public int readField()
    { 
        int value = int_field;
        return value;
    } 
    public void writeField(int value)
    {
        int_field = value;
    }
    public int readNonmanagedField()
    {
        return nonmanaged_field;
    }
    public void writeNonmanagedField(int value)
    {
        nonmanaged_field = value;
    }
}
