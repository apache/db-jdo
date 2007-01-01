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
 * A IntegralType instance represents an integral type as defined in the
 * Java language. There are five are integral types: <code>byte</code>, 
 * <code>short</code>, <code>int</code>, <code>long</code>, and
 * <code>char</code>.
 * <p>
 * Class PredefinedType provides public static final variables referring
 * to the JavaType representation for integral types.
 *
 * @see PredefinedType#byteType
 * @see PredefinedType#shortType
 * @see PredefinedType#intType
 * @see PredefinedType#longType
 * @see PredefinedType#charType
 *
 * @author Michael Bouschen
 * @since JDO 1.0.1
 */
public class IntegralType
    extends PrimitiveType 
{
    /** Constructor. */
    public IntegralType(Class clazz)
    {
        super(clazz);
    }
    
    /**
     * Returns <code>true</code> if this JavaType represents an integral
     * type. 
     * @return <code>true</code> if this JavaTypre represents an integral
     * type; <code>false</code> otherwise.
     */
    public boolean isIntegral() 
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
