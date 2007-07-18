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
 * Annotation for a JDO unique constraint.
 * Maps across to the JDO2 element "unique".
 *
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique
{
    /** Name of the unique constraint.
     * @return the name of the unique constraint
     */
    String name() default "";

    /** Table for the unique constraint. This is needed iff annotating a type 
     * where the unique constraint is not defined on the primary table for 
     * the type.
     * @return the table on which the unique constraint is defined
     */
    String table() default "";

    /** Whether this unique constraint is deferred until commit.
     * @return whether this unique constraint is deferred until commit
     */
    String deferred() default "";

    /** Field names that comprise this unique constraint.
     * @return field names that comprise this unique constraint
     */
    String[] fields() default {};

    /** Columns that comprise this unique constraint.
     * @return columns that comprise this unique constraint
     */
    Column[] columns() default {};
}
