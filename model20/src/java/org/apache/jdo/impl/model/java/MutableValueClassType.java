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
 * A MutableValueClassType instance represents a mutable class whoses
 * values may be treated as values rather than refernces during
 * storing. Note, MutableValueClassType instances are trackable which is
 * the only difference in behavior to instances of the superclass
 * ValueClassType. 
 * <p>
 * Class PredefinedType provides public static final variables referring
 * to the JavaType representation for mutable value class types.
 * 
 * @see PredefinedType#dateType
 * @see PredefinedType#sqlDateType
 * @see PredefinedType#sqlTimeType 
 * @see PredefinedType#sqlTimestampType
 * @see PredefinedType#bitsetType
 *
 * @author Michael Bouschen
 * @since JDO 1.0.1
 */
public class MutableValueClassType
    extends ValueClassType
{
    /** 
     * Constructor.
     * @param clazz the Class instance representing the type
     * @param superclass JavaType instance representing the superclass.
     * @param orderable flag indicating whether this type is orderable.
     */
    public MutableValueClassType(Class clazz, JavaType superclass, boolean orderable)
    {
        super(clazz, superclass, orderable);
    }

    /** 
     * Returns <code>true</code> if this JavaType represents a trackable
     * Java class. A JDO implementation may replace a persistent field of
     * a trackable type with an assignment compatible instance of its own
     * implementation of this type which notifies the owning FCO of any
     * change of this field.
     * @return <code>true</code> if this JavaType represents a trackable
     * Java class, <code>false</code> otherwise.
     */
    public boolean isTrackable() 
    {
        return true;
    }
}
