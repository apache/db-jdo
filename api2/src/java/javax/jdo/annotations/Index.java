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
 * Annotation for a JDO index.
 * Maps across to the JDO2 element "index".
 *
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Index
{
    /** Name of the index
     * @return the name of the index
     */
    String name() default "";

    /** Table for the index. This is needed iff annotating a type where
     * the index is not defined on the primary table for the type.
     * @return the table on which the index is defined
     */
    String table() default "";

    /** Whether this index is unique 
     */
    String unique() default "";

    /** Field names that comprise this index. 
     */
    String[] fields() default {};

    /** Columns that comprise this index. 
     */
    Column[] columns() default {};
}
