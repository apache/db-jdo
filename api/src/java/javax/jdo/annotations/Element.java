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
 * Corresponds to the xml element "element".
 * 
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Element
{
    /**
     * Types of the elements. This can be determined if using JDK1.5 generics
     * but is required otherwise. Multiple types can be specified if the
     * implementation supports multiple types.
     * @return the types of elements
     */
    Class[] types() default {};

    /**
     * Whether the element is to be stored serialized (into a join table)
     * @return whether the element is to be stored serialized 
     * (into a join table)
     */
    String serialized() default "";

    /** Whether this element is embedded. 
     * @return whether this element is embedded
     */
    String embedded() default "";

    /**
     * The embedded mapping for the element.
     * @return the embedded mapping for the element
     */
    Embedded[] embeddedMapping() default {};

    /**
     * Whether the element is dependent on the owner, and will be deleted 
     * when the owner is deleted.
     * @return whether the element is dependent on the owner, and will be 
     * deleted when the owner is deleted
     */
    String dependent() default "";

    /**
     * Name of the table for the element.
     * @return name of the table for the element
     */
    String table() default "";

    /**
     * Name of the column to store the element in.
     * @return name of the column to store the element in
     */
    String column() default "";

    /**
     * Delete action to apply to any foreign key for the element.
     * @return delete action to apply to any foreign key for the element
     */
    ForeignKeyAction deleteAction() default ForeignKeyAction.UNSPECIFIED;

    /**
     * Update action to apply to any foreign key for the element
     * @return update action to apply to any foreign key for the element
     */
    ForeignKeyAction updateAction() default ForeignKeyAction.UNSPECIFIED;

    /**
     * Whether the value column(s) should be indexed.
     * @return whether the value column(s) should be indexed.
     */
    String indexed() default "";

    /** The name of the index to generate. 
     * @return the name of the index
     */
    String index() default "";

    /**
     * Whether a unique constraint should be generated or assumed.
     * @return whether a unique constraint should be generated or assumed
     */
    String unique() default "";

    /**
     * The name of the unique key constraint to generate.
     * @return the name of the unique key constraint
     */
    String uniqueKey() default "";

    /**
     * Name of the member in the target class that forms a bidirectional 
     * relationship with this member. 
     * @return name of the member in the target class that forms a bidirectional 
     * relationship with this member
     */
    String mappedBy() default "";

    /**
     * The column(s) for the element.
     * @return the column(s) for the element
     */
    Column[] columns() default {};

    /** Generate or assume a foreign key constraint exists on the column
     * or columns associated with this join. Specify "true" or "false".
     * @return whether to generate or assume a primary key constraint
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
