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
@Target({ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Property
{
    /** Modifier for this property. 
     * @return the persistence modifier
     */
    FieldPersistenceModifier persistenceModifier() 
        default FieldPersistenceModifier.UNKNOWN;

    /** Table to use for persisting this property.
     * @return the table to use for persisting this property
     */
    String table() default "";

    /** Whether this property is in the default fetch group. 
     * @return whether this property is in the default fetch group
     */
    String defaultFetchGroup() default "";

    /** Behavior when this property contains a null value. 
     * @return the behavior when this property contains a null value
     */
    NullValue nullValue() default NullValue.NONE;

    /** Whether this property is embedded. 
     * @return whether this property is embedded
     */
    String embedded() default "";

    /** Whether the elements of this field are embedded. 
     * @return whether the elements of this field are embedded
     */
    String embeddedElement() default "";

    /** Whether the keys of this field are embedded. 
     * @return whether the keys of this field are embedded
     */
    String embeddedKey() default "";

    /** Whether the values of this field are embedded. 
     * @return whether the values of this field are embedded
     */
    String embeddedValue() default "";

    /** Whether this property is serialized into a single column. 
     * @return whether this property is serialized into a single column
     */
    String serialized() default "";

    /** Whether related object(s) of this property are dependent
     * and so deleted when this object is deleted. 
     * @return whether the related object(s) of this property are dependent
     */
    String dependent() default "";

    /** Whether this property is part of the primary key of the class. 
     * @return whether this property is part of the primary key of the class
     */
    String primaryKey() default "";

    /** Value strategy to use to populate this property (if any).
     * @return the generated value strategy
     */
    IdGeneratorStrategy valueStrategy() default IdGeneratorStrategy.UNKNOWN;

    /** Name of the sequence to use with particular value strategies. 
     * @return the name of the sequence
     */
    String sequence() default "";

    /** Name of the fetch-group to use when this property is loaded 
     * due to being referenced when not already loaded 
     * @return the name of the load fetch group
     */
    String loadFetchGroup() default "";

    /** Type of the property. Used when the property is a reference type 
     * and we want to be specific. 
     * @return the property type
     */
    Class fieldType() default void.class;

    /** Type of the property. This is used as an alternative to "fieldType" 
     * when the implementation supports specification of multiple property 
     * types. If "fieldType" is specified then this is ignored.
     * @return the property types
     */
    Class[] fieldTypes() default {};

    /** Name of the related property in the other class where this value is 
     * mapped 
     * (bidirectional relationship). 
     * @return the related property in the other class
     */
    String mappedBy() default "";

    /** Column definition(s) for this property. Used for mapping multiple 
     * columns
     * to the same property, for example relationships with multiple column
     * foreign keys. 
     * @return the columns for this property
     */
    Column[] columns() default {}; 

    /** Column name for this property. Used for mapping embedded properties 
     * where both the property name and column name are specified in the same
     * annotation.
     * @return the name of the column
     */
    String column() default "";

    /** Null indicator column for this property. Used for nested embedded 
     * properties where the null indicator column is needed.
     * @return the null indicator column
     */
    String nullIndicatorColumn() default "";

    /** Name of the property when this is embedded in another object. 
     * @return the name of the property
     */
    String name() default ""; 

    /** Vendor extensions for this property. 
     * @return the vendor extensions
     */
    Extension[] extensions() default {};
}
