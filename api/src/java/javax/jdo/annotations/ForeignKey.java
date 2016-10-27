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
 * Annotation for a database foreign-key.
 * Corresponds to the xml element "foreign-key".
 *
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ForeignKeys.class)
public @interface ForeignKey
{
    /** Name of the foreign key.
     * @return the name of the foreign key
     */
    String name() default "";

    /** Table for the foreign key. This is needed iff annotating a type where
     * the foreign key is not defined on the primary table for the type.
     * @return the table on which the foreign key is defined
     */
    String table() default "";

    /** Whether this foreign key is deferred 
     * (constraint is checked only at commit).
     * @return whether this foreign key is deferred
     */
    String deferred() default "";

    /** Whether this foreign key is unique.
     * @return whether this foreign key is unique
     */
    String unique() default "";

    /** The delete action of this foreign key.
     * @return the delete action of this foreign key
     */
    ForeignKeyAction deleteAction() default ForeignKeyAction.RESTRICT;

    /** The update action of this foreign key.
     * @return the update action of this foreign key
     */
    ForeignKeyAction updateAction() default ForeignKeyAction.RESTRICT;

    /** Member (field and property) names that compose this foreign key.
     * @return the member names that compose this foreign key
     */
    String[] members() default {};

    /** Columns that compose this foreign key.
     * @return the columns that compose this foreign key
     */
    Column[] columns() default {};

    /** Vendor extensions.
     * @return the vendor extensions
     */
    Extension[] extensions() default {};
}
