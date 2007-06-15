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
 * Annotation for the join of a relation.
 * Maps across to the JDO2 element "join".
 * 
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
public @interface Join
{
    /** Table to join to (used when joining to secondary tables). */
    String table() default "";

    /** Name of the column in the join table. */
    String column() default "";

    /** Whether the join column is indexed. */
    String indexed() default "";

    /** Whether the join column is unique */
    String unique() default "";

    /** Whether to use an outer join. */
    String outer() default "";

    /** Delete action to be applied to any ForeignKey on this join. */
    ForeignKeyAction deleteAction() default ForeignKeyAction.UNKNOWN;

    /** Detail definition of the join column(s). */
    Column[] columns() default {};
}