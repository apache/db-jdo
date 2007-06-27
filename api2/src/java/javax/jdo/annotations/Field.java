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
 * Annotation for defining the persistence of a field.
 * 
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Field
{
    /** Modifier for this field. 
     * @return the persistence modifier
     */
    FieldPersistenceModifier persistenceModifier() 
        default FieldPersistenceModifier.UNKNOWN;

    /** Whether this field is in the default fetch group. 
     * @return whether this field is in the default fetch group
     */
    String defaultFetchGroup() default "";

    /** Behavior when this field contains a null value. 
     * @return the behavior when this field contains a null value
     */
    NullValue nullValue() default NullValue.NONE;

    /** Whether this field is embedded. 
     * @return whether this field is embedded
     */
    String embedded() default "";

    /** Whether this field is serialised into a single column. 
     * @return whether this field is serialized into a single column
     */
    String serialized() default "";

    /** Whether related object(s) of this field are dependent
     * and so deleted when this object is deleted. 
     * @return whether the related object(s) of this field are dependent
     */
    String dependent() default "";

    /** Whether this field is part of the primary key of the class. 
     * @return whether this field is part of the primary key of the class
     */
    String primaryKey() default "";

    /** Value strategy to use to populate this field (if any).
     * @return the generated value strategy
     */
    IdGeneratorStrategy valueStrategy() default IdGeneratorStrategy.UNKNOWN;

    /** Name of a sequence to use with particular value strategies. 
     */
    String sequence() default "";

    /** Name of the fetch-group to use when this field is loaded 
     * due to being referenced etc. 
     */
    String loadFetchGroup() default "";

    /** Type of the field. Used when the field is a reference type 
     * and we want to be specific. 
     */
    Class fieldType() default void.class;

    /** Type of the field. This is used as an alternative to "fieldType" 
     * when the implementation supports specification of multiple key types.
     * If "fieldType" is specified then this is ignored. 
     */
    Class[] fieldTypes() default {};

    /** Name of the field in the fields class where this value is stored 
     * (bidir relations). 
     */
    String mappedBy() default "";

    /** Column definition(s) for this field. Used for embedded fields. 
     */
    Column[] columns() default {}; 

    /** Name of the field when this is embedded in another object. 
     */
    String embeddedFieldName() default ""; 

    /** Vendor extensions for this field. 
     */
    Extension[] extensions() default {};
}
