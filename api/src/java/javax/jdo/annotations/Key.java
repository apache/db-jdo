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

import javax.jdo.AttributeConverter;
import javax.jdo.AttributeConverter.UseDefault;

/**
 * Annotation for the key of a map relation.
 * Corresponds to the xml element "key".
 * 
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Key
{
    /**
     * Types of the keys. This can be determined if using JDK1.5 generics
     * but is required otherwise. Multiple types can be specified if the
     * implementation supports multiple types.
     * @return the types of keys
     */
    Class[] types() default {};

    /**
     * Whether the key is to be stored serialized (into a single column of a
     * join table).
     * @return whether the key is to be stored serialized
     */
    String serialized() default "";

    /** Whether this key is embedded. 
     * @return whether this key is embedded
     */
    String embedded() default "";

    /**
     * The embedded mapping for the key.
     * @return the embedded mapping for the key
     */
    Embedded[] embeddedMapping() default {};

    /**
     * Whether the key is dependent on the owner (and will be deleted 
     * when the owner is deleted).
     * @return whether the key is dependent on the owner
     */
    String dependent() default "";

    /**
     * Name of the table for the key.
     * @return name of the table for the key
     */
    String table() default "";

    /**
     * Name of the column to store the key in.
     * @return name of the column to store the key in
     */
    String column() default "";

    /**
     * Delete action to apply to the foreign key for the key.
     * @return delete action to apply to the foreign key for the key
     */
    ForeignKeyAction deleteAction() default ForeignKeyAction.UNSPECIFIED;

    /**
     * Update action to apply to the foreign key for the key.
     * @return update action to apply to the foreign key for the key
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
     * Whether the element column(s) contents should be considered unique
     * @return whether the element column(s) contents should be considered unique
     */
    String unique() default "";

    /**
     * The name of the unique key constraint to generate.
     * @return the name of the unique key constraint
     */
    String uniqueKey() default "";

    /**
     * Name of a member in the value class where this key is stored.
     * @return the name of a member in the value class where this key is stored
     */
    String mappedBy() default "";

    /**
     * The column(s) for the key
     * @return the column(s) for the key
     */
    Column[] columns() default {};

    /** Generate or assume a foreign key constraint exists on the column
     * or columns associated with this join. Specify "true" or "false".
     * @return whether to generate or assume a foreign key constraint
     */
    String generateForeignKey() default "";

    /** Name for a generated foreign key constraint.
     * @return the name of the generated foreign key constraint
     */
    String foreignKey() default "";

	/**
	 * Optional {@link AttributeConverter} to use for converting this key.
	 * @return Optional converter class for converting this key (when non-PersistenceCapable)
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends AttributeConverter> converter() default UseDefault.class;

	/**
	 * Whether we should disable any converter that was specified as default for this type on the PMF.
	 * If the converter is specified on this annotation then this is ignored
	 * @return Whether PMF attribute conversion is to be disabled.
	 */
	boolean disableConversion() default false;

    /** 
     * Vendor extensions.
     * @return the vendor extensions
     */
    Extension[] extensions() default {};
}
