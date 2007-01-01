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

package org.apache.jdo.impl.model.java;

/**
 * A FloatingPointType instance represents a floating point type as defined
 * in the Java language. There are two floating point types: 
 * <code>float</code> and <code>double</code>.
 * <p>
 * Class PredefinedType provides public static final variables referring
 * to the JavaType representation for floating point types.
 * 
 * @see PredefinedType#floatType
 * @see PredefinedType#doubleType
 *
 * @author Michael Bouschen
 * @since JDO 1.0.1
 */
public class FloatingPointType
    extends PrimitiveType 
{
    /** Constructor. */
    public FloatingPointType(Class clazz)
    {
        super(clazz);
    }

    /** 
     * Returns <code>true</code> if this JavaType represents a floating
     * point type. 
     * @return <code>true</code> if this JavaType represents a floating
     * point type; <code>false</code> otherwise.
     */
    public boolean isFloatingPoint() 
    {
        return true;
    }

    /**
     * Returns <code>true</code> if this JavaType represents an orderable
     * type as specified by JDO.
     * @return <code>true</code> if this JavaType represents an orderable
     * type; <code>false</code> otherwise.
     */
    public boolean isOrderable() 
    {
        return true;
    }
}
