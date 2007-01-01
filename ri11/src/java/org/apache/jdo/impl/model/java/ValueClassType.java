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

import org.apache.jdo.model.java.JavaType;

/**
 * A ValueClassType instance represents a class whoses values may be treated
 * as values rather than refernces during storing. 
 * <p>
 * Class PredefinedType provides public static final variables referring
 * to the JavaType representation for value class types.
 * 
 * @see PredefinedType#numberType
 * @see PredefinedType#stringType
 * @see PredefinedType#localeType 
 * @see PredefinedType#bigDecimalType
 * @see PredefinedType#bigIntegerType
 *
 * @author Michael Bouschen
 * @since JDO 1.0.1
 */
public class ValueClassType 
    extends PredefinedType
{
    /** The orderable property. */
    private boolean orderable;

    /** 
     * Constructor.
     * @param clazz the Class instance representing the type
     * @param superclass JavaType instance representing the superclass.
     * @param orderable flag indicating whether this type is orderable.
     */
    public ValueClassType(Class clazz, JavaType superclass, boolean orderable)
    {
        super(clazz, superclass);
        this.orderable = orderable;
    }

    /** 
     * Returns <code>true</code> if this JavaType represents a type whoses
     * values may be treated as values rather than refernces during
     * storing.
     * @return <code>true</code> if this JavaType represents a value type;
     * <code>false</code> otherwise.
     */
    public boolean isValue() 
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
        return orderable;
    }
    
}
