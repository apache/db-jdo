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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.jdo.AttributeConverter;
import javax.jdo.AttributeConverter.UseDefault;

/**
 * Annotation for defining the persistence of a member.
 * This corresponds to the xml elements "field" and "property". 
 * 
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Persistent
{
    /** Modifier for this field. This is normally not specified, and the 
     * defaults are used, or the @Transactional or @NotPersistent 
     * annotation is specified directly on the member. One possible use
     * for specifying persistenceModifier is for embedded instances in which
     * a member is not persistent but in the non-embedded instances the
     * member is persistent. Note that it is not portable to specify a
     * member to be not persistent in the non-embedded case and persistent
     * in the embedded usage.
     * @return the persistence modifier
     */
    PersistenceModifier persistenceModifier() 
        default PersistenceModifier.UNSPECIFIED;

    /** Table to use for persisting this member.
     * @return the table to use for persisting this member
     */
    String table() default "";

    /** Whether this member is in the default fetch group. 
     * @return whether this member is in the default fetch group
     */
    String defaultFetchGroup() default "";

    /** Behavior when this member contains a null value. 
     * @return the behavior when this member contains a null value
     */
    NullValue nullValue() default NullValue.NONE;

    /** Whether this member is embedded. 
     * @return whether this member is embedded
     */
    String embedded() default "";

    /** Whether the elements of this member are embedded. 
     * @return whether the elements of this member are embedded
     */
    String embeddedElement() default "";

    /** Whether the keys of this member are embedded. 
     * @return whether the keys of this member are embedded
     */
    String embeddedKey() default "";

    /** Whether the values of this member are embedded. 
     * @return whether the values of this member are embedded
     */
    String embeddedValue() default "";

    /** Whether this member is serialized into a single column. 
     * @return whether this member is serialized into a single column
     */
    String serialized() default "";

    /** Whether the elements of this member are serialized. 
     * @return whether the elements of this member are serialized
     */
    String serializedElement() default "";

    /** Whether the keys of this member are serialized. 
     * @return whether the keys of this member are serialized
     */
    String serializedKey() default "";

    /** Whether the values of this member are serialized. 
     * @return whether the values of this member are serialized
     */
    String serializedValue() default "";

    /** Whether related object(s) of this member are dependent
     * and so deleted when this object is deleted. 
     * @return whether the related object(s) of this member
     * are dependent
     */
    String dependent() default "";

    /** Whether the elements of this member are dependent. 
     * @return whether the elements of this member are dependent
     */
    String dependentElement() default "";

    /** Whether the keys of this member are dependent. 
     * @return whether the keys of this member are dependent
     */
    String dependentKey() default "";

    /** Whether the values of this member are dependent. 
     * @return whether the values of this member are dependent
     */
    String dependentValue() default "";

    /** Whether this member is part of the primary key for application
     * identity. This is equivalent to specifying @PrimaryKey as 
     * a separate annotation on the member.
     * @return whether this member is part of the primary key
     */
    String primaryKey() default "";

    /** Value strategy to use to generate the value for this field 
     * or property (if any).
     * @return the generated value strategy
     */
    IdGeneratorStrategy valueStrategy() default IdGeneratorStrategy.UNSPECIFIED;

    /** Custom value strategy to use to generate the value for this field 
     * or property (if any). If customValueStrategy is non-empty, then
     * valueStrategy must be UNSPECIFIED.
     * @return the custom value strategy
     */
    String customValueStrategy() default "";

    /** Name of the sequence to use with particular value strategies. 
     * @return the name of the sequence
     */
    String sequence() default "";

    /** Name of the fetch-group to use when this member is loaded 
     * due to being referenced when not already loaded.
     * @return the name of the load fetch group
     */
    String loadFetchGroup() default "";

    /** Types of the member. Used when the declared 
     * member type is a supertype of the actual type that is stored in the 
     * member. For example, the declared member type might be an interface type
     * that must contain an object of a concrete type when used
     * for persistence.
     * @return the types
     */
    Class[] types() default {};

    /** Name of the related member in the other class 
     * where this value is mapped (bidirectional relationship). 
     * @return the related member in the other class
     */
    String mappedBy() default "";

    /** Column definition(s) for this member. Used for mapping 
     * multiple columns
     * to the same member, for example relationships with 
     * multiple column foreign keys. 
     * @return the columns for this member
     */
    Column[] columns() default {}; 

    /** Column name where the values are stored for this member. 
     * @return the name of the column
     */
    String column() default "";

    /** Null indicator column for this member. Used for nested 
     * embedded fields or properties to indicate whether the embedded
     * instance should have a null value.
     * @return the null indicator column
     */
    String nullIndicatorColumn() default "";

    /** Name of the member when this is embedded in another object.
     * The fully-qualified member name is used. For example, 
     * "line.point1.x" refers to the member x in class Point 
     * that is embedded as member point1 in class Line that is embedded 
     * in a member called line.
     * @return the name of the member
     */
    String name() default ""; 

    /**
     * Recursion depth for this member. Used only when
     * the annotation is used within the definition of a FetchGroup.
     * @return the recursion depth
     */
    int recursionDepth() default 1;

    /**
     * Whether this field/property is cacheable in a Level2 cache.
     * @return Whether the field is L2 cacheable
     * @since 2.2
     */
    String cacheable() default "true";

	/**
	 * Optional {@link AttributeConverter} to use for converting this member.
	 * @return Converter class for converting this member when not PersistenceCapable
	 *   (or NullAttributeConverter when not specified).
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends AttributeConverter> converter() default UseDefault.class; // TODO Current JDK doesn't allow "default null"

	/**
	 * Whether we should disable any converter that was specified as default for this type on the PMF.
	 * Only has any effect when this is explicitly set to true, when any AttributeConverter specified for this type
	 * either here or for the class or at the PMF will be ignored and will use the JDO implementation default handling.
	 * @return Whether PMF attribute conversion is to be disabled.
	 */
	boolean useDefaultConversion() default false; // TODO Current JDK doesn't allow "default null"

    /** Vendor extensions for this member. 
     * @return the vendor extensions
     */
    Extension[] extensions() default {};
}
