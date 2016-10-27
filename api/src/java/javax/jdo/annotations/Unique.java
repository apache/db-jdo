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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for a database unique constraint. Used for database schema
 * generation to create unique constraints. Also used to reorder database
 * operations when flushing changes to avoid unique constraint violations.
 * Corresponds to the xml element "unique".
 *
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Uniques.class)
public @interface Unique
{
    /** Name of the unique constraint.
     * @return the name of the unique constraint
     */
    String name() default "";

    /** Table for the unique constraint. This is needed iff annotating a type 
     * where this unique constraint is not for the primary table for 
     * the persistent class or interface.
     * @return the table on which the unique constraint is defined
     */
    String table() default "";

    /** Whether this unique constraint is deferred until commit.
     * @return whether this unique constraint is deferred until commit
     */
    String deferred() default "";

    /** Member (field and property) names that compose this unique constraint.
     * @return member names that compose this unique constraint
     */
    String[] members() default {};

    /** Columns that compose this unique constraint.
     * @return columns that compose this unique constraint
     */
    Column[] columns() default {};

    /** Vendor extensions.
     * @return the vendor extensions
     */
    Extension[] extensions() default {};
}
