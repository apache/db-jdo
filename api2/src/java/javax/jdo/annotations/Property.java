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
package javax.jdo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for defining the persistence of a property of a persistent interface.
 * 
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.FIELD, ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
public @interface Property
{
    /** Modifier for this property. */
    FieldPersistenceModifier persistenceModifier() default FieldPersistenceModifier.UNKNOWN;

    /** Whether this property is in the default fetch group. */
    String defaultFetchGroup() default "";

    /** Behaviour when inserting a null value. */
    NullValue nullValue() default NullValue.NONE;

    /** Whether this property is embedded. */
    String embedded() default "";

    /** Whether this property is serialised into a single column. */
    String serialized() default "";

    /** Whether related object(s) of this property are dependent and so deleted when this object is deleted. */
    String dependent() default "";

    /** Whether this property is part of the PK of the class. */
    String primaryKey() default "";

    /** Value strategy to use to populate this property (if any): */
    IdGeneratorStrategy valueStrategy() default IdGeneratorStrategy.UNKNOWN;

    /** Name of a sequence to use with particular value strategies. */
    String sequence() default "";

    /** Name of the fetch-group to use when this property is loaded due to being referenced etc */
    String loadFetchGroup() default "";

    /** Type of the field. Used when the property is a reference type and we want to be specific. */
    Class fieldType() default void.class;

    /** Name of the field in the properties class where this value is stored (bidir relations). */
    String mappedBy() default "";

    /** Vendor extensions for this property. */
    Extension[] extensions() default {};
}
