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

/*
 * UNDEFINED.java
 *
 * Created on September 11, 2001
 */

package org.apache.jdo.impl.jdoql.scope;

/**
 * An instance of this class represents an undefined value in a ValueTable
 * (ParameterTable or VariableTable).
 *
 * @author  Michael Bouschen
 */
public class UNDEFINED
{
    /** The singleton UNDEFINED instance. */    
    private static UNDEFINED undefined = new UNDEFINED();
    
    /** 
     * Get an instance of UNDEFINED
     * @return an instance of UNDEFINED
     */    
    public static UNDEFINED getInstance()
    {
        return undefined;
    }

    /**
     * Creates new UNDEFINED. This constructor should not be 
     * called directly; instead, the singleton access method 
     * {@link #getInstance} should be used.
     */
    private UNDEFINED() {}

    /**
     * Returns a string representation of an UNDEFINED instance.
     */
    public String toString()
    {
        return "UNDEFINED"; //NOI18N
    }
    
    
}

