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
 * Annotation for the order of a container field.
 * Maps across to the JDO2 element "order".
 * 
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Order
{
    /** The name of the column to use for the ordering column (1-N and M-N
     * relations).
     * @return the name of the ordering column
     */
    String column() default "";

    /** Name of a field or property in the target class that acts as the 
     * ordering field or property for this Collection/List/Array.
     * Return the name of the field or property in the target class
     */
    String mappedBy() default "";

    /** The definition of the column(s) to use for ordering.
     * @return the columns to use for ordering
     */
    Column[] columns() default {};
}