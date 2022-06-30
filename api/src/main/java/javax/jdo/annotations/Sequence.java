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

/**
 * Annotation for a datastore sequence.
 * Maps across to the JDO2 element "sequence".
 * 
 * @version 2.1
 * @since 2.1
 */
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Sequence
{
    /** The symbolic name of the datastore sequence. 
     * @return the name of the sequence
     */
    String name();

    /** Strategy for the sequence. 
     * @return the strategy for the sequence
     */
    SequenceStrategy strategy();

    /** Name of the sequence in the datastore. 
     * @return the name of the datastore sequence
     */
    String datastoreSequence() default "";

    /** Name of a factory class for generating the sequence values. 
     * @return the name of the factory class for the sequence
     */
    Class factoryClass() default void.class;

    /** Vendor extensions for this sequence. 
     * @return vendor extensions
     */
    Extension[] extensions() default {};

    /**
     * Initial value for the sequence.
     * @return Initial value for the sequence
     * @since 3.1
     */
    int initialValue() default 1;

    /**
     * Allocation size for the sequence.
     * @return Allocation size for the sequence
     * @since 3.1
     */
    int allocationSize() default 50;
}
