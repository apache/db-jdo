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
 * Annotation to define that the object is embedded into the table of the 
 * owning object.
 * Corresponds to the xml element "embedded".
 * 
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Embedded
{
    /** The member in the embedded object that links back to the owning object
     * where it has a bidirectional relationship. 
     * @return the member that refers to the owner
     */
    String ownerMember() default "";

    /** The column in the embedded object used to judge if the embedded object
     * is null. 
     * @return the null indicator column
     */
    String nullIndicatorColumn() default "";

    /** The value in the null column to interpret the object as being null.
     * @return the null indicator value
     */
    String nullIndicatorValue() default "";

    /** Members for this embedding. 
     * @return the members embedded in the field or property being annotated
     */
    Persistent[] members() default {};

    /**
     * Discriminator for cases where the embedded object has inheritance.
     * @return the discriminator for inheritance determination
     */
    Discriminator discriminatorColumnName() default @Discriminator;
}
