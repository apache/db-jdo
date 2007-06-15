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
 * Annotation for the element of a collection/array relation.
 * Maps across to the JDO2 element "element".
 * 
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.FIELD, ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
public @interface Element
{
    /**
     * The type of the element. This can be determined for an array, or if using JDK1.5 generics
     * but is required otherwise.
     * @return The type of the element.
     */
    Class type() default void.class;

    /**
     * Types of the elements. This is used as an alternative to "type" when the implementation supports
     * specification of multiple element types. If "type" is specified then this is ignored.
     * @return The types of elements
     */
    Class[] types() default {};

    /**
     * Whether the element is to be stored serialised (into a join table)
     * @return Whether the element is to be stored serialised (into a join table)
     */
    String serialized() default "";

    /**
     * Whether the element is to be stored embedded (into a join table)
     * @return Whether the element is to be stored embedded (into a join table)
     */
    String embedded() default "";

    /**
     * Whether the element is dependent on the owner (and so will be deleted when it is)
     * @return Whether the element is dependent on the owner (and so will be deleted when it is)
     */
    String dependent() default "";

    /**
     * Name of the table for the element
     * @return Name of the table for the element
     */
    String table() default "";

    /**
     * Name of the column to store the element in
     * @return Name of the column to store the element in
     */
    String column() default "";

    /**
     * Delete action to apply to any foreign-key for the element
     * @return Delete action to apply to any foreign-key for the element
     */
    ForeignKeyAction deleteAction() default ForeignKeyAction.UNKNOWN;

    /**
     * Update action to apply to any foreign-key for the element
     * @return Update action to apply to any foreign-key for the element
     */
    ForeignKeyAction updateAction() default ForeignKeyAction.UNKNOWN;

    /**
     * Whether the element column(s) should be indexed.
     * @return Whether the element column(s) should be indexed.
     */
    String indexed() default "";

    /**
     * Whether the element column(s) contents should be considered unique
     * @return Whether the element column(s) contents should be considered unique
     */
    String unique() default "";

    /**
     * Name of a field in the target class that forms a bidirectional relation with this field
     * @return Name of a field in the target class that forms a bidirectional relation with this field
     */
    String mappedBy() default "";

    /**
     * The column(s) for the element.
     * @return The column(s) for the element.
     */
    Column[] columns() default {};
}