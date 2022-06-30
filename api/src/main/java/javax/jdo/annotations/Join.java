/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
 * Annotation for the join of a relation.
 * Corresponds to the xml element "join".
 * 
 * @version 3.2
 * @since 2.1
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Joins.class)
public @interface Join
{
    /** Table to join to (used when joining to secondary tables). 
     * @return the table
     */
    String table() default "";

    /** Name of the column in the join table. 
     * @return the name of the column in the join table
     */
    String column() default "";

    /** Whether the join column is indexed. 
     * @return whether the join column(s) is(are) indexed
     */
    String indexed() default "";

    /** The name of the index to generate. 
     * @return the name of the index
     */
    String index() default "";

    /** Whether the join column is unique.
     * @return whether the join column(s) is(are) is unique
     */
    String unique() default "";

    /**
     * The name of the unique key constraint to generate.
     * @return the name of the unique key constraint
     */
    String uniqueKey() default "";

    /** Whether to use an outer join. 
     * @return whether to use an outer join
     */
    String outer() default "";

    /** Delete action to be applied to any ForeignKey on this join.
     * @return the delete action
     */
    ForeignKeyAction deleteAction() default ForeignKeyAction.UNSPECIFIED;

    /** Detail definition of the join column(s). This is needed for
     * more than one join column.
     * @return the join columns
     */
    Column[] columns() default {};

    /** Generate or assume a primary key constraint exists on the column
     * or columns associated with this join. Specify "true" or "false".
     * @return whether to generate or assume a primary key constraint
     */
    String generatePrimaryKey() default "";

    /** Name for a generated primary key constraint.
     * @return the name of the generated primary key constraint
     */
    String primaryKey() default "";

    /** Generate or assume a foreign key constraint exists on the column
     * or columns associated with this join. Specify "true" or "false".
     * @return whether to generate or assume a foreign key constraint
     */
    String generateForeignKey() default "";

    /** Name for a generated foreign key constraint.
     * @return the name of the generated foreign key constraint
     */
    String foreignKey() default "";

    /** Vendor extensions.
     * @return the vendor extensions
     */
    Extension[] extensions() default {};
}
