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
 * Annotation for the key of a map relation.
 * Maps across to the JDO2 element "key".
 * 
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.FIELD, ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
public @interface Key
{
    /**
     * The type of the key. This can be determined if using JDK1.5 generics but is required otherwise.
     * @return The type of the key.
     */
    Class type() default void.class;

    /**
     * Types of the keys. This is used as an alternative to "type" when the implementation supports
     * specification of multiple key types. If "type" is specified then this is ignored.
     * @return The types of keys
     */
    Class[] types() default {};

    /**
     * Whether the key is to be stored serialised (into a join table)
     * @return Whether the key is to be stored serialised (into a join table)
     */
    String serialized() default "";

    /**
     * Whether the key is to be stored embedded (into a join table)
     * @return Whether the key is to be stored embedded (into a join table)
     */
    String embedded() default "";

    /**
     * Whether the key is dependent on the owner (and so will be deleted when it is)
     * @return Whether the key is dependent on the owner (and so will be deleted when it is)
     */
    String dependent() default "";

    /**
     * Name of the table for the key
     * @return Name of the table for the key
     */
    String table() default "";

    /**
     * Name of the column to store the key in
     * @return Name of the column to store the key in
     */
    String column() default "";

    /**
     * Delete action to apply to any foreign-key for the key
     * @return Delete action to apply to any foreign-key for the key
     */
    ForeignKeyAction deleteAction() default ForeignKeyAction.UNKNOWN;

    /**
     * Update action to apply to any foreign-key for the key
     * @return Update action to apply to any foreign-key for the key
     */
    ForeignKeyAction updateAction() default ForeignKeyAction.UNKNOWN;

    /**
     * Whether the key column(s) should be indexed.
     * @return Whether the key column(s) should be indexed.
     */
    String indexed() default "";

    /**
     * Whether the key column(s) contents should be considered unique
     * @return Whether the key column(s) contents should be considered unique
     */
    String unique() default "";

    /**
     * Name of a field in the value class where this key value is stored.
     * @return Name of a field in the value where this key is stored
     */
    String mappedBy() default "";

    /**
     * The column(s) for the key
     * @return The column(s) for the key
     */
    Column[] columns() default {};
}